package org.idempiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MLVEFiscalPrinter extends X_LVE_FiscalPrinter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3343178213345391388L;

	public MLVEFiscalPrinter(Properties ctx, int LVE_FiscalPrinter_ID,
			String trxName) {
		super(ctx, LVE_FiscalPrinter_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLVEFiscalPrinter(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

}
