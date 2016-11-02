package ve.com.as.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class LVE_PrintInvoice extends SvrProcess {
	
	int p_LVE_FiscalPrinter = 0;

	@Override
	protected void prepare() {
	    ProcessInfoParameter[] para = getParameter();
	    for (int i = 0; i < para.length; i++)
	    {
	      String name = para[i].getParameterName();
	      if (name.equals("LVE_FiscalPrinter_ID")) {
	        this.p_LVE_FiscalPrinter = para[i].getParameterAsInt();
	      } else {
	        this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
	      }
	    }		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
