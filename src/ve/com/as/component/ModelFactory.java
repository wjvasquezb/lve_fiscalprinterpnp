package ve.com.as.component;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.compiere.model.MInvoice;
import org.compiere.model.PO;
import org.compiere.util.Env;

import ve.com.as.model.MInvoiceFP;
import ve.com.as.model.MLVEFiscalPResources;
import ve.com.as.model.MLVEFiscalPrinter;

public class ModelFactory implements IModelFactory {

	@Override
	public Class<?> getClass(String tableName) {
	    if (tableName.equals(MLVEFiscalPrinter.Table_Name)) {
	        return MLVEFiscalPrinter.class;
	    }
	    if (tableName.equals(MLVEFiscalPResources.Table_Name)) {
	        return MLVEFiscalPResources.class;
	    }
	    if (tableName.equals(MInvoice.Table_Name)) {
	        return MInvoiceFP.class;
	    }
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
	    if (tableName.equals(MLVEFiscalPrinter.Table_Name)) {
	        return new MLVEFiscalPrinter(Env.getCtx(), Record_ID, trxName);
	      }
	      if (tableName.equals(MLVEFiscalPResources.Table_Name)) {
	        return new MLVEFiscalPResources(Env.getCtx(), Record_ID, trxName);
	      }
	      if (tableName.equals(MInvoice.Table_Name)) {
	        return new MInvoiceFP(Env.getCtx(), Record_ID, trxName);
	      }
		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
	    if (tableName.equals(MLVEFiscalPrinter.Table_Name)) {
	        return new MLVEFiscalPrinter(Env.getCtx(), rs, trxName);
	      }
	      if (tableName.equals(MLVEFiscalPResources.Table_Name)) {
	        return new MLVEFiscalPResources(Env.getCtx(), rs, trxName);
	      }
	      if (tableName.equals(MInvoice.Table_Name)) {
	        return new MInvoiceFP(Env.getCtx(), rs, trxName);
	      }
		return null;
	}
}
