package org.idempiere.component;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.idempiere.process.LVE_CheckStatusFP;
import org.idempiere.process.LVE_CloseX;
import org.idempiere.process.LVE_CloseZ;
import org.idempiere.process.LVE_GetFiscalInfo;
import org.idempiere.process.LVE_PrintInvoice;;

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
