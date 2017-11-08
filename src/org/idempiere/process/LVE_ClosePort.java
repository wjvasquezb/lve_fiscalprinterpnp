/**
 * 
 */
package org.idempiere.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.idempiere.model.LVE_FiscalPrinter;
import org.idempiere.model.MLVEFiscalPrinter;

/**
 * @author jcolmenarez,4 oct. 2017
 *
 */
public class LVE_ClosePort extends SvrProcess {
	
	public int p_LVE_FiscalPrinter_ID = 0;
	public MLVEFiscalPrinter fiscalPrinter = null;
	public String port = "";

	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (name.equals("LVE_FiscalPrinter_ID"))
				p_LVE_FiscalPrinter_ID = para[i].getParameterAsInt();
		}
	}
	
	protected String doIt() throws Exception {
		fiscalPrinter = new MLVEFiscalPrinter(getCtx(), p_LVE_FiscalPrinter_ID, get_TrxName());
		port = fiscalPrinter.getLVE_FiscalPort();
		log.info("Puerto a cerrar: "+port);
		LVE_FiscalPrinter.dllPnP.PFcierrapuerto();
		return "OK";
	}

}
