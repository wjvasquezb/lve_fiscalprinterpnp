package ve.com.as.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import ve.com.as.model.LVE_FiscalPrinter;
import ve.com.as.model.MLVECloseZ;
import ve.com.as.model.MLVEFiscalPrinter;

public class LVE_CloseZ extends SvrProcess {
	
	public int p_LVE_FiscalPrinter_ID = 0;
	public MLVEFiscalPrinter fiscalPrinter = null;
	public int port = 0;
	public String status = "";
	public String fiscalInfo = "";
	public String fiscalInfoSplit[];
	public String msg = "";
	
	public String ZNo = "";
	public BigDecimal exemptSales = Env.ZERO;
	public BigDecimal salesGeneralFee = Env.ZERO;
	public BigDecimal generalTaxRate = Env.ZERO;
	public BigDecimal salesReducedRate = Env.ZERO;
	public BigDecimal reducedRateTax = Env.ZERO;
	public BigDecimal subTotalTaxBase = Env.ZERO;
	public BigDecimal subTotalIVA = Env.ZERO;
	public BigDecimal totalReturnsAmt = Env.ZERO;
	public BigDecimal taxDiscount = Env.ZERO;
	public BigDecimal taxableReturns = Env.ZERO;
	public BigDecimal subTotalIVACN = Env.ZERO;
	public BigDecimal totalZ = Env.ZERO;
	public String LVE_ZDate = "";
	public String statusZ = "";
	public String lastFiscalDocNo = "";
	public MInvoice invoice;
	
	@Override
	protected void prepare() {
		System.out.println("Prepare Check Status Printer");
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equals("LVE_FiscalPrinter_ID"))
				p_LVE_FiscalPrinter_ID = para[i].getParameterAsInt();
		}
		log.log(Level.SEVERE, "Impresora Fiscal : " + p_LVE_FiscalPrinter_ID);		
	}

	@Override
	protected String doIt() throws Exception {	
		if(p_LVE_FiscalPrinter_ID != 0) {
			fiscalPrinter = new MLVEFiscalPrinter(getCtx(), p_LVE_FiscalPrinter_ID, get_TrxName());
			port = fiscalPrinter.getLVE_FiscalPort();
			LVE_FiscalPrinter.dllPnP.PFabrepuerto(String.valueOf(port));
			status = LVE_FiscalPrinter.dllPnP.PFrepz();
			fiscalInfo = LVE_FiscalPrinter.dllPnP.PFultimo();
			LVE_FiscalPrinter.dllPnP.PFestatus("N");
			statusZ = LVE_FiscalPrinter.dllPnP.PFultimo();
			LVE_FiscalPrinter.dllPnP.PFcierrapuerto();
			fiscalPrinter.setLVE_FPStatus(fiscalInfo);
			fiscalPrinter.setLVE_FPError(fiscalInfo);
			if("OK".equals(status)) {
				fiscalPrinter.setIsConnected(true);
				fiscalPrinter.saveEx();
				setCloseZInfo();
				saveCloseZ();
			} else {
				fiscalPrinter.setIsConnected(false);
				fiscalPrinter.saveEx();
			}
		}
		return status;
	}

	private void setCloseZInfo() {
		fiscalInfoSplit = fiscalInfo.split(",");
		if(fiscalInfoSplit.length < 14) {
			msg = "No se puede obtener información compoleta de Cierre Z";
			log.warning(msg);
		} else {
			exemptSales = new BigDecimal(fiscalInfoSplit[2]);
			salesGeneralFee = new BigDecimal(fiscalInfoSplit[3]);
			generalTaxRate = new BigDecimal(fiscalInfoSplit[4]);
			totalReturnsAmt =  new BigDecimal(fiscalInfoSplit[5]);
			taxDiscount =  new BigDecimal(fiscalInfoSplit[6]);
			taxableReturns =  new BigDecimal(fiscalInfoSplit[7]);
			subTotalIVACN =  new BigDecimal(fiscalInfoSplit[8]);
			LVE_ZDate =  fiscalInfoSplit[9];
			salesReducedRate = new BigDecimal(fiscalInfoSplit[10]);
			reducedRateTax = new BigDecimal(fiscalInfoSplit[11]);
			if(statusZ.length() >= 12) {
				ZNo = statusZ.split(",")[11];
				lastFiscalDocNo = statusZ.split(",")[9];
				invoice = new Query(getCtx(), MInvoice.Table_Name, "LVE_FiscalDocNo=?", get_TrxName())
				.setParameters(lastFiscalDocNo).setOnlyActiveRecords(true).first();
			}
			subTotalTaxBase = salesGeneralFee.add(salesReducedRate).subtract(taxableReturns);
			subTotalIVA = generalTaxRate.add(reducedRateTax).subtract(subTotalIVACN);
			totalZ = subTotalTaxBase.add(subTotalIVA);
		}
	}

	private void saveCloseZ() {
		MLVECloseZ closeZ = new MLVECloseZ(getCtx(), 0,get_TrxName());
		closeZ.setLVE_FiscalPrinter_ID(fiscalPrinter.get_ID());
		closeZ.setAD_Org_ID(fiscalPrinter.getAD_Org_ID());
		closeZ.setExemptSales(exemptSales);
		closeZ.setSalesGeneralFee(salesGeneralFee);
		closeZ.setGeneralTaxRate(generalTaxRate);
		closeZ.setSalesReducedRate(salesReducedRate);
		closeZ.setReducedRateTax(reducedRateTax);
		closeZ.setSubTotalTaxBase(subTotalTaxBase);
		closeZ.setSubTotalIVA(subTotalIVA);
		closeZ.setTaxableReturns(taxableReturns);
		closeZ.setSubTotalIVACN(subTotalIVACN);
		closeZ.setLVE_ZDate(formatDate(LVE_ZDate));
		closeZ.setLVE_ZNo(ZNo);
		if(invoice != null)
			closeZ.setC_Invoice_ID(invoice.get_ID());
		closeZ.saveEx();
	}

	private Timestamp formatDate(String lve_ZDate) {
		String dateStr = "20" + lve_ZDate.substring(0,2) + "-" + lve_ZDate.substring(2,4) + "-" + lve_ZDate.substring(4,6);
		Timestamp zDate = Timestamp.valueOf(dateStr);
		return zDate;
	}
}