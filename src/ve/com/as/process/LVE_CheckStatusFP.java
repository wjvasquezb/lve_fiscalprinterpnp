package ve.com.as.process;

import java.util.logging.Level;

import org.compiere.model.MRule;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import ve.com.as.model.MLVEFiscalPResources;
import ve.com.as.model.MLVEFiscalPrinter;

public class LVE_CheckStatusFP extends SvrProcess {
	
	int p_LVE_FiscalPrinter_ID = 0;
	int LVE_FiscalPResources_ID = 0;
	int AD_Rule_ID = 0;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
	    for (int i = 0; i < para.length; i++)
	    {
	      String name = para[i].getParameterName();
	      if (name.equals("LVE_FiscalPrinter_ID")) {
	        this.p_LVE_FiscalPrinter_ID = para[i].getParameterAsInt();
	      } else {
	        this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
	      }
	    }
	}

	@Override
	protected String doIt() throws Exception {
		MLVEFiscalPrinter printer = new MLVEFiscalPrinter(getCtx(), this.p_LVE_FiscalPrinter_ID, get_TrxName());
		MLVEFiscalPResources printerResource = new MLVEFiscalPResources(getCtx(), LVE_FiscalPResources_ID, get_TrxName());
		MRule rule = new MRule(getCtx(), AD_Rule_ID, get_TrxName());
		return null;
	}

}
