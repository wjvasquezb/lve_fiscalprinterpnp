package ve.com.as.component;

import org.adempiere.base.IModelValidatorFactory;
import org.compiere.model.ModelValidator;

import ve.com.as.model.LVE_FiscalPrinter;

public class ModelFactory implements IModelValidatorFactory {

	@Override
	public ModelValidator newModelValidatorInstance(String className) {
		if(className.equalsIgnoreCase("ve.com.as.model.LVE_FiscalPrinter"))
			return new LVE_FiscalPrinter();
		
		return null;
	}

}
