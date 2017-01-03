package ve.com.as.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import ve.com.as.model.LVE_FiscalPrinter;
import ve.com.as.model.MLVEFiscalPrinter;


public class LVE_GetFiscalInfo extends SvrProcess {
	
	public int p_LVE_FiscalPrinter_ID = 0;
	public MLVEFiscalPrinter fiscalPrinter = null;
	public int port = 0;
	public String status = "";
	public String p_TypeStatus = "N";
	
	@Override
	protected void prepare() {
		System.out.println("Prepare Check Status Printer");
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equals("LVE_FiscalPrinter_ID"))
				p_LVE_FiscalPrinter_ID = para[i].getParameterAsInt();
			else if (name.equals("TypeStatus"))
				p_TypeStatus = para[i].getParameterAsString();
		}
		log.log(Level.SEVERE, "Impresora Fiscal : " + p_LVE_FiscalPrinter_ID);		
	}

	@Override
	protected String doIt() throws Exception {	
		if(p_LVE_FiscalPrinter_ID != 0) {
			fiscalPrinter = new MLVEFiscalPrinter(getCtx(), p_LVE_FiscalPrinter_ID, get_TrxName());
			port = fiscalPrinter.getLVE_FiscalPort();
			LVE_FiscalPrinter.dllPnP.PFabrepuerto(String.valueOf(port));
			status = LVE_FiscalPrinter.dllPnP.PFestatus(p_TypeStatus);
			if("OK".equals(status)) {
				fiscalPrinter.setIsConnected(true);
				fiscalPrinter.saveEx();
			} else {
				fiscalPrinter.setIsConnected(false);
				fiscalPrinter.saveEx();
			}
			LVE_FiscalPrinter.dllPnP.PFcierrapuerto();
		}
		return status;
	}
}