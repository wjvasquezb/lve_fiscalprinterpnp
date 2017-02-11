package ve.com.as.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.model.MUser;
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
	
	public int ZNo = 0;
	public BigDecimal exemptSales = Env.ZERO;
	public BigDecimal salesGeneralFee = Env.ZERO;
	public BigDecimal generalTaxRate = Env.ZERO;
	public BigDecimal salesReducedRate = Env.ZERO;
	public BigDecimal reducedRateTax = Env.ZERO;
	public BigDecimal subTotalTaxBase = Env.ZERO;
	public BigDecimal subTotalIVA = Env.ZERO;
	public BigDecimal taxDiscount = Env.ZERO;
	public BigDecimal taxableReturns = Env.ZERO;
	public BigDecimal taxAdditionalFee = Env.ZERO;
	public BigDecimal salesAdditionalRate = Env.ZERO;
	
	public BigDecimal exemptSalesCN = Env.ZERO;
	public BigDecimal subTotalIVACN = Env.ZERO;
	public BigDecimal salesGeneralFeeCN = Env.ZERO;
	public BigDecimal generalTaxRateCN = Env.ZERO;
	public BigDecimal salesReducedRateCN = Env.ZERO;
	public BigDecimal reducedRateTaxCN = Env.ZERO;
	public BigDecimal taxAdditionalFeeCN = Env.ZERO;
	public BigDecimal salesAdditionalRateCN = Env.ZERO;
	public BigDecimal subTotalTaxBaseCN = Env.ZERO;
	public BigDecimal totalReturnsAmt = Env.ZERO;
	
	public BigDecimal totalZ = Env.ZERO;
	public String LVE_ZDate = "";
	public String statusZ = "";
	public String lastFiscalDocNo = "";
	public MInvoice invoice;
	public MUser salesRep;
	
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
			LVE_FiscalPrinter.dllPnP.PFestatus("N");
			statusZ = LVE_FiscalPrinter.dllPnP.PFultimo();
			status = LVE_FiscalPrinter.dllPnP.PFrepz();
			fiscalInfo = LVE_FiscalPrinter.dllPnP.PFultimo();
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
			msg = "No se puede obtener información compoleta de Cierre Z - " + fiscalInfo;
			log.warning(msg);
		} else {
			salesRep = new MUser(getCtx(), Integer.valueOf(Env.getContext(getCtx(), "#AD_User_ID")), get_TrxName());
			exemptSales = new BigDecimal(fiscalInfoSplit[2]);
			salesGeneralFee = new BigDecimal(fiscalInfoSplit[3]);
			generalTaxRate = new BigDecimal(fiscalInfoSplit[4]);
			totalReturnsAmt =  new BigDecimal(fiscalInfoSplit[5]);
			taxDiscount =  new BigDecimal(fiscalInfoSplit[6]);
			salesReducedRate = new BigDecimal(fiscalInfoSplit[10]);
			reducedRateTax = new BigDecimal(fiscalInfoSplit[11]);
			
			taxableReturns =  new BigDecimal(fiscalInfoSplit[7]);
			salesGeneralFeeCN =  new BigDecimal(fiscalInfoSplit[8]);
			generalTaxRateCN = new BigDecimal(fiscalInfoSplit[14]);
			salesReducedRateCN = new BigDecimal(fiscalInfoSplit[15]);
			reducedRateTaxCN = new BigDecimal(fiscalInfoSplit[16]);
			
			if(statusZ.length() >= 12) {
				ZNo = Integer.valueOf(statusZ.split(",")[11]) + 1;
				lastFiscalDocNo = statusZ.split(",")[9];
				LVE_ZDate = statusZ.split(",")[5] + statusZ.split(",")[6];
				invoice = new Query(getCtx(), MInvoice.Table_Name, "LVE_FiscalDocNo=?", get_TrxName())
				.setParameters(lastFiscalDocNo).setOnlyActiveRecords(true).first();
			}
			subTotalTaxBase = salesGeneralFee.add(salesReducedRate);
			subTotalIVA = generalTaxRate.add(reducedRateTax);
			totalZ = subTotalTaxBase.add(subTotalIVA);
			
			subTotalTaxBaseCN = salesGeneralFeeCN.add(salesReducedRateCN);
			subTotalIVACN = generalTaxRateCN.add(reducedRateTaxCN);
			totalReturnsAmt = subTotalTaxBaseCN.add(subTotalIVACN);
		}
	}

	private void saveCloseZ() {
		MLVECloseZ closeZ = new MLVECloseZ(getCtx(), 0,get_TrxName());
		closeZ.setLVE_FiscalPrinter_ID(fiscalPrinter.get_ID());
		closeZ.setAD_Org_ID(fiscalPrinter.getAD_Org_ID());
		closeZ.setSalesRep_ID(salesRep.get_ID());
		closeZ.setExemptSales(formatNum(exemptSales));
		closeZ.setSalesGeneralFee(formatNum(salesGeneralFee));
		closeZ.setGeneralTaxRate(formatNum(generalTaxRate));
		closeZ.setSalesReducedRate(formatNum(salesReducedRate));
		closeZ.setReducedRateTax(formatNum(reducedRateTax));
		closeZ.setSalesAdditionalRate(salesAdditionalRate);
		closeZ.setTaxAdditionalFee(taxAdditionalFee);
		closeZ.setSubTotalTaxBase(formatNum(subTotalTaxBase));
		closeZ.setSubTotalIVA(formatNum(subTotalIVA));
		closeZ.setTotalAmt(formatNum(totalZ));
		
		closeZ.setTaxableReturns(formatNum(taxableReturns));
		closeZ.setSubTotalIVACN(formatNum(subTotalIVACN));
		closeZ.setExemptSalesCN(formatNum(exemptSalesCN));
		closeZ.setSalesGeneralFeeCN(formatNum(salesGeneralFeeCN));
		closeZ.setGeneralTaxRateCN(formatNum(generalTaxRateCN));
		closeZ.setSalesReducedRateCN(formatNum(salesReducedRateCN));
		closeZ.setReducedRateTaxCN(formatNum(reducedRateTaxCN));
		closeZ.setTaxAdditionalFeeCN(formatNum(taxAdditionalFeeCN));
		closeZ.setSalesAdditionalRateCN(formatNum(salesAdditionalRateCN));
		closeZ.setSubTotalTaxBaseCN(formatNum(subTotalTaxBaseCN));
		closeZ.setTotalReturnsAmt(formatNum(totalReturnsAmt));
		
		closeZ.setLVE_ZDate(formatDate(LVE_ZDate));
		closeZ.setLVE_ZNo(String.valueOf(ZNo));
		if(invoice != null)
			closeZ.setC_Invoice_ID(invoice.get_ID());
		closeZ.saveEx();
	}

	private Timestamp formatDate(String lve_ZDate) {
		log.warning("Date: " + lve_ZDate);
		System.out.println("Date: " + lve_ZDate);
		String dateStr = "";
		if(lve_ZDate.length() < 2) 
			dateStr = "2017-02-11 00:00:00"; 
		else {
		String anio = lve_ZDate.substring(0,2);
		String month = lve_ZDate.substring(2,4);
		String day = lve_ZDate.substring(4,6);
		String hour = lve_ZDate.substring(6,8);
		String min = lve_ZDate.substring(8,10);
		String ss = lve_ZDate.substring(10,12);
		dateStr = "20"+anio+"-"+month+"-"+day+" "+hour+":"+min+":"+ss;
		}
		log.warning("DateStr: " + dateStr);
		Timestamp xDate = Timestamp.valueOf(dateStr);
		return xDate;
	}
	
	private BigDecimal formatNum(BigDecimal num) {
		num = num.divide(Env.ONEHUNDRED);
		return num; 
	}
}