package ve.com.as.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import ve.com.as.model.LVE_FiscalPrinter;
import ve.com.as.model.MLVEFiscalPrinter;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LVE_CheckStatusFP extends SvrProcess {
	
	public int p_LVE_FiscalPrinter_ID = 0;
	public MLVEFiscalPrinter fiscalPrinter = null;
	public int port = 0;
	public String status = "";
	public String fiscalInfo = "";
	public String serialFP = "";
	
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
			status = LVE_FiscalPrinter.dllPnP.PFestatus("N");
			fiscalInfo = LVE_FiscalPrinter.dllPnP.PFultimo();
			LVE_FiscalPrinter.dllPnP.PFSerial();
			serialFP = LVE_FiscalPrinter.dllPnP.PFultimo();
			LVE_FiscalPrinter.dllPnP.PFcierrapuerto();
			if("OK".equals(status)) {
				fiscalPrinter.setIsConnected(true);
				fiscalPrinter.setLVE_SerialFiscal(serialFP.split(",")[2]);
				setFiscalInfo();
			} else {
				fiscalPrinter.setIsConnected(false);
				fiscalPrinter.saveEx();
			}
		}
		return status;
	}
	
	private void setFiscalInfo() {
		String[] fiscalInf = fiscalInfo.split(",");
		String statusPinter = fiscalInf[3];
		Timestamp datePinter = formatDate(fiscalInf[5] + fiscalInf[6]);
		String qtyInvoices = fiscalInf[7];
		String lastZ = fiscalInf[11];
		
		fiscalPrinter.setLVE_FPStatus(statusPinter);
		fiscalPrinter.setDatePrinter(datePinter);
		fiscalPrinter.setQtyInvoiceDay(new BigDecimal(qtyInvoices));
		fiscalPrinter.setLVE_LastZNo(lastZ);
		fiscalPrinter.saveEx();
	}

	public Timestamp formatDate (String date) {
		String anio = date.substring(0,2);
		String month = date.substring(2,4);
		String day = date.substring(4,6);
		String hour = date.substring(6,8);
		String min = date.substring(8,10);
		String ss = date.substring(10,12);
		String dateStr = "20"+anio+"-"+month+"-"+day+" "+hour+":"+min+":"+ss;
		Timestamp dateTimestamp = Timestamp.valueOf(dateStr);
		return dateTimestamp;
	}
}