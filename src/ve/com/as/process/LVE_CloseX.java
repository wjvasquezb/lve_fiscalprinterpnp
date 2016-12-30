package ve.com.as.process;

import java.util.HashMap;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MRule;
import org.compiere.model.Scriptlet;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class LVE_CloseX extends SvrProcess {
	
	int p_LVE_FiscalPrinter_ID = 0;
	int LVE_FiscalPResources_ID = 0;
	int AD_Rule_ID = 0;
	String p_Value = "";
	Object result = "";
	HashMap<String, Object> m_scriptCtx = new HashMap<String, Object>();

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
	    for (int i = 0; i < para.length; i++)
	    {
	      String name = para[i].getParameterName();
	      if (name.equals("LVE_FiscalPrinter_ID")) {
	        this.p_LVE_FiscalPrinter_ID = para[i].getParameterAsInt();
	      } else if (name.equals("Value")) {
	        this.p_Value = para[i].getParameterAsString();
	      } else {
	        this.log.log(Level.SEVERE, "Unknown Parameter: " + name);
	      }
	    }
	}

	@Override
	protected String doIt() throws Exception {
//		MLVEFiscalPrinter printer = new MLVEFiscalPrinter(getCtx(), p_LVE_FiscalPrinter_ID, get_TrxName());
//		MLVEFiscalPResources printerResource = null; 
//		printerResource = (MLVEFiscalPResources) new Query(getCtx(), "LVE_FiscalPResources", " LVE_FiscalPrinter_ID = ? AND Value = ? ", null)
//		.setParameters(new Object[] { p_LVE_FiscalPrinter_ID, p_Value }).first();
//		MRule rule = new MRule(getCtx(), printerResource.getAD_Rule_ID(), get_TrxName());
//		if(rule.get_ID() != 0) {
//			result = executeScript(rule.get_ID());
//		} else
//			result = "No ha seleccionado regla para Impresora " + printer.getName();
//		if (result == null)
//			result = "Ejecución de Regla " + rule.getName() + " ha retornado null";
		return result.toString();
	}
	
	public Object executeScript(int AD_Rule_ID) {
		MRule rule = MRule.get(getCtx(), AD_Rule_ID);
		Object result = null;
		Object m_description = null;
		String errorMsg = "";
		try {
			Scriptlet engine = new Scriptlet(Scriptlet.VARIABLE, rule.getScript(), m_scriptCtx);
			Exception ex = engine.execute();
			m_description = engine.getDescription();
			if (m_description != null) {
				if (m_description.toString().length() >= "AdempiereException"
						.length())
					if (m_description.toString().contains("AdempiereException")) {
						errorMsg = m_description.toString();
						throw ex;
					}
			}
			if (ex != null) {

				throw ex;
			}
			result = engine.getResult(false);

		} catch (Exception e) {
			throw new AdempiereException("Execution error - @AD_Rule_ID@="
					+ rule.getValue() + " \n " + errorMsg);
		}
		return result;
	}
}