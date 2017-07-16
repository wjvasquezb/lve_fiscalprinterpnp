package org.idempiere.component;

import org.adempiere.base.IModelValidatorFactory;
import org.compiere.model.ModelValidator;
import org.idempiere.model.LVE_FiscalPrinter;

public class ModelFactory implements IModelValidatorFactory {

	@Override
	public ModelValidator newModelValidatorInstance(String className) {
		if(className.equalsIgnoreCase("org.idempiere.model.LVE_FiscalPrinter"))
			return new LVE_FiscalPrinter();
		
		return null;
	}

}
