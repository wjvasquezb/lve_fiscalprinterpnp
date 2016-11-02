package ve.com.as.component;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;

import ve.com.as.process.LVE_CloseX;
import ve.com.as.process.LVE_CloseZ;
import ve.com.as.process.LVE_PrintInvoice;
import ve.com.as.process.LVE_GetFiscalInfo;
import ve.com.as.process.LVE_CheckStatusFP;;

public class ProcessFactory implements IProcessFactory {
	
	public ProcessCall newProcessInstance(String className)
	  {
	    if (className.equals(LVE_CloseX.class.getName())) {
	        return new LVE_CloseX();
	      }
	      if (className.equals(LVE_CloseZ.class.getName())) {
	        return new LVE_CloseZ();
	      }
	      if (className.equals(LVE_PrintInvoice.class.getName())) {
	        return new LVE_PrintInvoice();
	      }
	      if (className.equals(LVE_GetFiscalInfo.class.getName())) {
	        return new LVE_GetFiscalInfo();
	      }
	      if (className.equals(LVE_CheckStatusFP.class.getName())) {
	        return new LVE_CheckStatusFP();
	      }
	      return null;
	  }
}
