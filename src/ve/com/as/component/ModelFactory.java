package ve.com.as.component;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;

import ve.com.as.model.MLVEFiscalPResources;
import ve.com.as.model.MLVEFiscalPrinter;

public class ModelFactory implements IModelFactory {

	@Override
	public Class<?> getClass(String tableName) {
	    if (tableName.equals("LVE_FiscalPrinter")) {
	        return MLVEFiscalPrinter.class;
	    }
	    if (tableName.equals("LVE_FiscalPResources")) {
	        return MLVEFiscalPResources.class;
	    }
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
	    if (tableName.equals("LVE_FiscalPrinter")) {
	        return new MLVEFiscalPrinter(Env.getCtx(), Record_ID, trxName);
	      }
	      if (tableName.equals("LVE_FiscalPResources")) {
	        return new MLVEFiscalPResources(Env.getCtx(), Record_ID, trxName);
	      }
		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
	    if (tableName.equals("LVE_FiscalPrinter")) {
	        return new MLVEFiscalPrinter(Env.getCtx(), rs, trxName);
	      }
	      if (tableName.equals("LVE_FiscalPResources")) {
	        return new MLVEFiscalPResources(Env.getCtx(), rs, trxName);
	      }
		return null;
	}
}
