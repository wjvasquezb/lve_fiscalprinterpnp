package ve.com.as.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MLVEFiscalPrinter extends X_LVE_FiscalPrinter {

	public MLVEFiscalPrinter(Properties ctx, int LVE_FiscalPrinter_ID,
			String trxName) {
		super(ctx, LVE_FiscalPrinter_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLVEFiscalPrinter(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 924171201179143438L;
	
	

}
