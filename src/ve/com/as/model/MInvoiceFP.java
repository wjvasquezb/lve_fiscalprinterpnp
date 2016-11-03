package ve.com.as.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MBankAccount;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MConversionRateUtil;
import org.compiere.model.MDocType;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMatchInv;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentProcessor;
import org.compiere.model.MPaymentTransaction;
import org.compiere.model.MProject;
import org.compiere.model.MRMALine;
import org.compiere.model.MRule;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.model.Scriptlet;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MInvoiceFP extends MInvoice {
	
	HashMap<String, Object> m_scriptCtx = new HashMap<String, Object>();
	Object result = "";

	public MInvoiceFP(Properties ctx, int C_Invoice_ID, String trxName) {
		super(ctx, C_Invoice_ID, trxName);
	}

	public MInvoiceFP(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	public MInvoiceFP (MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate) {
		super(order, C_DocTypeTarget_ID, invoiceDate);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7407816376348204113L;
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			m_justPrepared = false;
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		// Set the definite document number after completed (if needed)
		setDefiniteDocumentNo();

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Implicit Approval
		if (!isApproved())
			approveIt();
		if (log.isLoggable(Level.INFO)) log.info(toString());
		StringBuilder info = new StringBuilder();
		
		// POS supports multiple payments
		boolean fromPOS = false;
		if ( getC_Order_ID() > 0 )
		{
			fromPOS = getC_Order().getC_POS_ID() > 0;
		}

  		//	Create Cash Payment
		if (PAYMENTRULE_Cash.equals(getPaymentRule()) && !fromPOS )
		{
			String whereClause = "AD_Org_ID=? AND C_Currency_ID=?";
			MBankAccount ba = new Query(getCtx(),MBankAccount.Table_Name,whereClause,get_TrxName())
				.setParameters(getAD_Org_ID(), getC_Currency_ID())
				.setOnlyActiveRecords(true)
				.setOrderBy("IsDefault DESC")
				.first();
			if (ba == null) {
				m_processMsg = "@NoAccountOrgCurrency@";
				return DocAction.STATUS_Invalid;
			}
			
			String docBaseType = "";
			if (isSOTrx())
				docBaseType=MDocType.DOCBASETYPE_ARReceipt;
			else
				docBaseType=MDocType.DOCBASETYPE_APPayment;
			
			MDocType[] doctypes = MDocType.getOfDocBaseType(getCtx(), docBaseType);
			if (doctypes == null || doctypes.length == 0) {
				m_processMsg = "No document type ";
				return DocAction.STATUS_Invalid;
			}
			MDocType doctype = null;
			for (MDocType doc : doctypes) {
				if (doc.getAD_Org_ID() == this.getAD_Org_ID()) {
					doctype = doc;
					break;
				}
			}
			if (doctype == null)
				doctype = doctypes[0];

			MPayment payment = new MPayment(getCtx(), 0, get_TrxName());
			payment.setAD_Org_ID(getAD_Org_ID());
			payment.setTenderType(MPayment.TENDERTYPE_Cash);
			payment.setC_BankAccount_ID(ba.getC_BankAccount_ID());
			payment.setC_BPartner_ID(getC_BPartner_ID());
			payment.setC_Invoice_ID(getC_Invoice_ID());
			payment.setC_Currency_ID(getC_Currency_ID());			
			payment.setC_DocType_ID(doctype.getC_DocType_ID());
			payment.setPayAmt(getGrandTotal());
			payment.setIsPrepayment(false);					
			payment.setDateAcct(getDateAcct());
			payment.setDateTrx(getDateInvoiced());

			//	Save payment
			payment.saveEx();

			payment.setDocAction(MPayment.DOCACTION_Complete);
			if (!payment.processIt(MPayment.DOCACTION_Complete)) {
				m_processMsg = "Cannot Complete the Payment : [" + payment.getProcessMsg() + "] " + payment;
				return DocAction.STATUS_Invalid;
			}

			payment.saveEx();
			info.append("@C_Payment_ID@: " + payment.getDocumentInfo());

			// IDEMPIERE-2588 - add the allocation generation with the payment
			if (payment.getJustCreatedAllocInv() != null)
				addDocsPostProcess(payment.getJustCreatedAllocInv());
		}	//	Payment

		//	Update Order & Match
		int matchInv = 0;
		int matchPO = 0;
		MInvoiceLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];

			//	Matching - Inv-Shipment
			if (!isSOTrx()
				&& line.getM_InOutLine_ID() != 0
				&& line.getM_Product_ID() != 0
				&& !isReversal())
			{
				MInOutLine receiptLine = new MInOutLine (getCtx(),line.getM_InOutLine_ID(), get_TrxName());
				BigDecimal matchQty = line.getQtyInvoiced();

				if (receiptLine.getMovementQty().compareTo(matchQty) < 0)
					matchQty = receiptLine.getMovementQty();

				MMatchInv inv = new MMatchInv(line, getDateInvoiced(), matchQty);
				if (!inv.save(get_TrxName()))
				{
					m_processMsg = CLogger.retrieveErrorString("Could not create Invoice Matching");
					return DocAction.STATUS_Invalid;
				}
				matchInv++;
				addDocsPostProcess(inv);
			}
					
			//	Update Order Line
			MOrderLine ol = null;
			if (line.getC_OrderLine_ID() != 0)
			{
				if (isSOTrx()
					|| line.getM_Product_ID() == 0)
				{
					ol = new MOrderLine (getCtx(), line.getC_OrderLine_ID(), get_TrxName());
					if (line.getQtyInvoiced() != null)
						ol.setQtyInvoiced(ol.getQtyInvoiced().add(line.getQtyInvoiced()));
					if (!ol.save(get_TrxName()))
					{
						m_processMsg = "Could not update Order Line";
						return DocAction.STATUS_Invalid;
					}
				}
				//	Order Invoiced Qty updated via Matching Inv-PO
				else if (!isSOTrx()
					&& line.getM_Product_ID() != 0
					&& !isReversal())
				{
					//	MatchPO is created also from MInOut when Invoice exists before Shipment
					BigDecimal matchQty = line.getQtyInvoiced();
					MMatchPO po = MMatchPO.create (line, null,
						getDateInvoiced(), matchQty);
					if (po != null) 
					{
						if (!po.save(get_TrxName()))
						{
							m_processMsg = "Could not create PO Matching";
							return DocAction.STATUS_Invalid;
						}
						matchPO++;
						if (!po.isPosted() && po.getM_InOutLine_ID() > 0) // match po don't post if receipt is not assigned, and it doesn't create avg po record
							addDocsPostProcess(po);
						
						MMatchInv[] matchInvoices = MMatchInv.getInvoiceLine(getCtx(), line.getC_InvoiceLine_ID(), get_TrxName());
						if (matchInvoices != null && matchInvoices.length > 0) 
						{
							for(MMatchInv matchInvoice : matchInvoices)
							{
								if (!matchInvoice.isPosted())
								{
									addDocsPostProcess(matchInvoice);
								}
							}
						}
					}
				}
			}

			//Update QtyInvoiced RMA Line
			if (line.getM_RMALine_ID() != 0)
			{
				MRMALine rmaLine = new MRMALine (getCtx(),line.getM_RMALine_ID(), get_TrxName());
				if (rmaLine.getQtyInvoiced() != null)
					rmaLine.setQtyInvoiced(rmaLine.getQtyInvoiced().add(line.getQtyInvoiced()));
				else
					rmaLine.setQtyInvoiced(line.getQtyInvoiced());
				if (!rmaLine.save(get_TrxName()))
				{
					m_processMsg = "Could not update RMA Line";
					return DocAction.STATUS_Invalid;
				}
			}
			//			
		}	//	for all lines
		if (matchInv > 0)
			info.append(" @M_MatchInv_ID@#").append(matchInv).append(" ");
		if (matchPO > 0)
			info.append(" @M_MatchPO_ID@#").append(matchPO).append(" ");



		//	Update BP Statistics
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
		DB.getDatabase().forUpdate(bp, 0);
		//	Update total revenue and balance / credit limit (reversed on AllocationLine.processIt)
		BigDecimal invAmt = MConversionRate.convertBase(getCtx(), getGrandTotal(true),	//	CM adjusted
			getC_Currency_ID(), getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
		if (invAmt == null)
		{
			m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingCurrencyToBaseCurrency",
					getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), getC_ConversionType_ID(), getDateAcct(), get_TrxName());
			return DocAction.STATUS_Invalid;
		}
		//	Total Balance
		BigDecimal newBalance = bp.getTotalOpenBalance();
		if (newBalance == null)
			newBalance = Env.ZERO;
		if (isSOTrx())
		{
			newBalance = newBalance.add(invAmt);
			//
			if (bp.getFirstSale() == null)
				bp.setFirstSale(getDateInvoiced());
			BigDecimal newLifeAmt = bp.getActualLifeTimeValue();
			if (newLifeAmt == null)
				newLifeAmt = invAmt;
			else
				newLifeAmt = newLifeAmt.add(invAmt);
			BigDecimal newCreditAmt = bp.getSO_CreditUsed();
			if (newCreditAmt == null)
				newCreditAmt = invAmt;
			else
				newCreditAmt = newCreditAmt.add(invAmt);
			//
			if (log.isLoggable(Level.FINE)) log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
				+ ") BP Life=" + bp.getActualLifeTimeValue() + "->" + newLifeAmt
				+ ", Credit=" + bp.getSO_CreditUsed() + "->" + newCreditAmt
				+ ", Balance=" + bp.getTotalOpenBalance() + " -> " + newBalance);
			bp.setActualLifeTimeValue(newLifeAmt);
			bp.setSO_CreditUsed(newCreditAmt);
		}	//	SO
		else
		{
			newBalance = newBalance.subtract(invAmt);
			if (log.isLoggable(Level.FINE)) log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
				+ ") Balance=" + bp.getTotalOpenBalance() + " -> " + newBalance);
		}
		bp.setTotalOpenBalance(newBalance);
		bp.setSOCreditStatus();
		if (!bp.save(get_TrxName()))
		{
			m_processMsg = "Could not update Business Partner";
			return DocAction.STATUS_Invalid;
		}

		//	User - Last Result/Contact
		if (getAD_User_ID() != 0)
		{
			MUser user = new MUser (getCtx(), getAD_User_ID(), get_TrxName());
			user.setLastContact(new Timestamp(System.currentTimeMillis()));
			StringBuilder msgset = new StringBuilder().append(Msg.translate(getCtx(), "C_Invoice_ID")).append(": ").append(getDocumentNo());
			user.setLastResult(msgset.toString());
			if (!user.save(get_TrxName()))
			{
				m_processMsg = "Could not update Business Partner User";
				return DocAction.STATUS_Invalid;
			}
		}	//	user

		//	Update Project
		if (isSOTrx() && getC_Project_ID() != 0)
		{
			MProject project = new MProject (getCtx(), getC_Project_ID(), get_TrxName());
			BigDecimal amt = getGrandTotal(true);
			int C_CurrencyTo_ID = project.getC_Currency_ID();
			if (C_CurrencyTo_ID != getC_Currency_ID())
				amt = MConversionRate.convert(getCtx(), amt, getC_Currency_ID(), C_CurrencyTo_ID,
					getDateAcct(), 0, getAD_Client_ID(), getAD_Org_ID());
			if (amt == null)
			{
				m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingCurrencyToProjectCurrency",
						getC_Currency_ID(), C_CurrencyTo_ID, 0, getDateAcct(), get_TrxName());
				return DocAction.STATUS_Invalid;
			}
			BigDecimal newAmt = project.getInvoicedAmt();
			if (newAmt == null)
				newAmt = amt;
			else
				newAmt = newAmt.add(amt);
			if (log.isLoggable(Level.FINE)) log.fine("GrandTotal=" + getGrandTotal(true) + "(" + amt
				+ ") Project " + project.getName()
				+ " - Invoiced=" + project.getInvoicedAmt() + "->" + newAmt);
			project.setInvoicedAmt(newAmt);
			if (!project.save(get_TrxName()))
			{
				m_processMsg = "Could not update Project";
				return DocAction.STATUS_Invalid;
			}
		}	//	project
		
		// auto delay capture authorization payment
		if (isSOTrx() && !isReversal())
		{
			StringBuilder whereClause = new StringBuilder();
			whereClause.append("C_Order_ID IN (");
			whereClause.append("SELECT C_Order_ID ");
			whereClause.append("FROM C_OrderLine ");
			whereClause.append("WHERE C_OrderLine_ID IN (");
			whereClause.append("SELECT C_OrderLine_ID ");
			whereClause.append("FROM C_InvoiceLine ");
			whereClause.append("WHERE C_Invoice_ID = ");
			whereClause.append(getC_Invoice_ID()).append("))");
			int[] orderIDList = MOrder.getAllIDs(MOrder.Table_Name, whereClause.toString(), get_TrxName());
			
			int[] ids = MPaymentTransaction.getAuthorizationPaymentTransactionIDs(orderIDList, getC_Invoice_ID(), get_TrxName());			
			if (ids.length > 0)
			{
				boolean pureCIM = true;
				ArrayList<MPaymentTransaction> ptList = new ArrayList<MPaymentTransaction>();
				BigDecimal totalPayAmt = BigDecimal.ZERO;
				for (int id : ids)
				{
					MPaymentTransaction pt = new MPaymentTransaction(getCtx(), id, get_TrxName());
					
					if (!pt.setPaymentProcessor())
					{
						if (pt.getC_PaymentProcessor_ID() > 0)
						{
							MPaymentProcessor pp = new MPaymentProcessor(getCtx(), pt.getC_PaymentProcessor_ID(), get_TrxName());
							m_processMsg = Msg.getMsg(getCtx(), "PaymentNoProcessorModel") + ": " + pp.toString();
						}
						else
							m_processMsg = Msg.getMsg(getCtx(), "PaymentNoProcessorModel");
						return DocAction.STATUS_Invalid;
					}
					
					boolean isCIM = pt.getC_PaymentProcessor_ID() > 0 && pt.getCustomerPaymentProfileID() != null && pt.getCustomerPaymentProfileID().length() > 0;
					if (pureCIM && !isCIM)
						pureCIM = false;
					
					totalPayAmt = totalPayAmt.add(pt.getPayAmt());
					ptList.add(pt);
				}
				
				// automatically void authorization payment and create a new sales payment when invoiced amount is NOT equals to the authorized amount (applied to CIM payment processor)
				if (getGrandTotal().compareTo(totalPayAmt) != 0 && ptList.size() > 0 && pureCIM)
				{
					// create a new sales payment
					MPaymentTransaction newSalesPT = MPaymentTransaction.copyFrom(ptList.get(0), new Timestamp(System.currentTimeMillis()), MPayment.TRXTYPE_Sales, "", get_TrxName());
					newSalesPT.setIsApproved(false);
					newSalesPT.setIsVoided(false);
					newSalesPT.setIsDelayedCapture(false);
					newSalesPT.setDescription("InvoicedAmt: " + getGrandTotal() + " <> TotalAuthorizedAmt: " + totalPayAmt);
					newSalesPT.setC_Invoice_ID(getC_Invoice_ID());
					newSalesPT.setPayAmt(getGrandTotal());
					
					// void authorization payment
					for (MPaymentTransaction pt : ptList)
					{
						pt.setDescription("InvoicedAmt: " + getGrandTotal() + " <> AuthorizedAmt: " + pt.getPayAmt());
						boolean ok = pt.voidOnlineAuthorizationPaymentTransaction();
						pt.saveEx();
						if (!ok)
						{
							m_processMsg = Msg.getMsg(getCtx(), "VoidAuthorizationPaymentFailed") + ": " + pt.getErrorMessage();
							return DocAction.STATUS_Invalid;
						}					
					}
					
					// process the new sales payment
					boolean ok = newSalesPT.processOnline();
					newSalesPT.saveEx();
					if (!ok)
					{
						m_processMsg = Msg.getMsg(getCtx(), "CreateNewSalesPaymentFailed") + ": " + newSalesPT.getErrorMessage();
						return DocAction.STATUS_Invalid;
					}
				}
				else if (getGrandTotal().compareTo(totalPayAmt) != 0 && ptList.size() > 0)
				{
					m_processMsg = "InvoicedAmt: " + getGrandTotal() + " <> AuthorizedAmt: " + totalPayAmt;
					return DocAction.STATUS_Invalid;
				}
				else
				{
					// delay capture authorization payment
					for (MPaymentTransaction pt : ptList)
					{
						boolean ok = pt.delayCaptureOnlineAuthorizationPaymentTransaction(getC_Invoice_ID());
						pt.saveEx();
						if (!ok)
						{
							m_processMsg = Msg.getMsg(getCtx(), "DelayCaptureAuthFailed") + ": " + pt.getErrorMessage();
							return DocAction.STATUS_Invalid;
						}					
					}
				}
			}
		}

		if (PAYMENTRULE_Cash.equals(getPaymentRule())) {
			if (testAllocation(true)) {
				saveEx();
			}
		}
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		//	Counter Documents
		MInvoice counter = createCounterDoc();
		if (counter != null)
			info.append(" - @CounterDoc@: @C_Invoice_ID@=").append(counter.getDocumentNo());
		
		MDocType c_doctype = new MDocType(getCtx(), getDocTypeID(), get_TrxName());
		if(c_doctype != null) {
			if(c_doctype.get_Value("IsFiscalDocument").equals(true)) {
				int printer_ID = (int) c_doctype.get_Value("LVE_FiscalPrinter_ID");
				if (printer_ID != 0) {
					MLVEFiscalPrinter printer = new MLVEFiscalPrinter(getCtx(), printer_ID, get_TrxName());
					MLVEFiscalPResources printerResource = null; 
					printerResource = (MLVEFiscalPResources) new Query(getCtx(), "LVE_FiscalPResources", " LVE_FiscalPrinter_ID = ? AND Value = ? ", null)
					.setParameters(new Object[] { printer_ID, "PrintIInvoice" }).first();
					MRule rule = new MRule(getCtx(), printerResource.getAD_Rule_ID(), get_TrxName());
					if(rule.get_ID() != 0) {
						MBPartner bPartner = new MBPartner(getCtx(), getC_BPartner_ID(), get_TrxName());
						MUser salesRep = new MUser(getCtx(), this.getSalesRep_ID(), get_TrxName());
						String nameSalesRep = salesRep.getName();
						MInvoiceLine[] invoiceLines = getLines();
						m_scriptCtx.put("Invoice", this);
						m_scriptCtx.put("Lines", invoiceLines);
						m_scriptCtx.put("getCtx", getCtx());
						m_scriptCtx.put("get_TrxName", get_TrxName());
						m_scriptCtx.put("BPartner", bPartner);
						m_scriptCtx.put("SalesRep", nameSalesRep);
						result = executeScript(rule.get_ID());
					} else
						result = "No ha seleccionado regla para Impresora " + printer.getName();
					info.append(result);
					if (result == null) {
						result = "Ejecución de Regla " + rule.getName() + " ha retornado null";
						m_processMsg = info.toString().trim();
						setProcessed(false);
						return DocAction.STATUS_Invalid;
					}
				}
			}
		}

		m_processMsg = info.toString().trim();
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	
	public Object executeScript(int AD_Rule_ID) {
		MRule rule = MRule.get(getCtx(), AD_Rule_ID);
		Object result = null;
		Object m_description = null;
		String errorMsg = "";
		try {
			Scriptlet engine = new Scriptlet(Scriptlet.VARIABLE, rule.getScript(), m_scriptCtx);
			Exception ex = engine.execute();
			m_description = engine.getDescription();
			if (m_description != null) {
				if (m_description.toString().length() >= "AdempiereException"
						.length())
					if (m_description.toString().contains("AdempiereException")) {
						errorMsg = m_description.toString();
						throw ex;
					}
			}
			if (ex != null) {

				throw ex;
			}
			result = engine.getResult(false);

		} catch (Exception e) {
			throw new AdempiereException("Execution error - @AD_Rule_ID@="
					+ rule.getValue() + " \n " + errorMsg);
		}
		return result;
	}
	
}
