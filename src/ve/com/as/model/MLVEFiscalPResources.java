package ve.com.as.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MLVEFiscalPResources extends X_LVE_FiscalPResources {

	public MLVEFiscalPResources(Properties ctx, int LVE_FiscalPResources_ID,
			String trxName) {
		super(ctx, LVE_FiscalPResources_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MLVEFiscalPResources(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1340195052421626543L;

}
