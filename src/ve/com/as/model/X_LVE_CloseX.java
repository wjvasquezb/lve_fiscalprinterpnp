/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package ve.com.as.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for LVE_CloseX
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_LVE_CloseX extends PO implements I_LVE_CloseX, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20161230L;

    /** Standard Constructor */
    public X_LVE_CloseX (Properties ctx, int LVE_CloseX_ID, String trxName)
    {
      super (ctx, LVE_CloseX_ID, trxName);
      /** if (LVE_CloseX_ID == 0)
        {
			setLVE_XDate (new Timestamp( System.currentTimeMillis() ));
			setSalesRep_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LVE_CloseX (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_LVE_CloseX[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount Payable.
		@param AmountPay Amount Payable	  */
	public void setAmountPay (BigDecimal AmountPay)
	{
		set_Value (COLUMNNAME_AmountPay, AmountPay);
	}

	/** Get Amount Payable.
		@return Amount Payable	  */
	public BigDecimal getAmountPay () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountPay);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set BaseTaxable Extra.
		@param BaseTaxableExtra BaseTaxable Extra	  */
	public void setBaseTaxableExtra (BigDecimal BaseTaxableExtra)
	{
		set_Value (COLUMNNAME_BaseTaxableExtra, BaseTaxableExtra);
	}

	/** Get BaseTaxable Extra.
		@return BaseTaxable Extra	  */
	public BigDecimal getBaseTaxableExtra () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BaseTaxableExtra);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Base Taxable Extra 2.
		@param BaseTaxableExtra2 Base Taxable Extra 2	  */
	public void setBaseTaxableExtra2 (BigDecimal BaseTaxableExtra2)
	{
		set_Value (COLUMNNAME_BaseTaxableExtra2, BaseTaxableExtra2);
	}

	/** Get Base Taxable Extra 2.
		@return Base Taxable Extra 2	  */
	public BigDecimal getBaseTaxableExtra2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BaseTaxableExtra2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set ExemptSales.
		@param ExemptSales ExemptSales	  */
	public void setExemptSales (BigDecimal ExemptSales)
	{
		set_Value (COLUMNNAME_ExemptSales, ExemptSales);
	}

	/** Get ExemptSales.
		@return ExemptSales	  */
	public BigDecimal getExemptSales () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ExemptSales);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set General Tax Rate.
		@param GeneralTaxRate General Tax Rate	  */
	public void setGeneralTaxRate (BigDecimal GeneralTaxRate)
	{
		set_Value (COLUMNNAME_GeneralTaxRate, GeneralTaxRate);
	}

	/** Get General Tax Rate.
		@return General Tax Rate	  */
	public BigDecimal getGeneralTaxRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GeneralTaxRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Last Invoice Fiscal Date.
		@param LastInvFiscalDate Last Invoice Fiscal Date	  */
	public void setLastInvFiscalDate (Timestamp LastInvFiscalDate)
	{
		set_Value (COLUMNNAME_LastInvFiscalDate, LastInvFiscalDate);
	}

	/** Get Last Invoice Fiscal Date.
		@return Last Invoice Fiscal Date	  */
	public Timestamp getLastInvFiscalDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_LastInvFiscalDate);
	}

	/** Set Close X.
		@param LVE_CloseX_ID Close X	  */
	public void setLVE_CloseX_ID (int LVE_CloseX_ID)
	{
		if (LVE_CloseX_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_CloseX_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_CloseX_ID, Integer.valueOf(LVE_CloseX_ID));
	}

	/** Get Close X.
		@return Close X	  */
	public int getLVE_CloseX_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_CloseX_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LVE_CloseX_UU.
		@param LVE_CloseX_UU LVE_CloseX_UU	  */
	public void setLVE_CloseX_UU (String LVE_CloseX_UU)
	{
		set_ValueNoCheck (COLUMNNAME_LVE_CloseX_UU, LVE_CloseX_UU);
	}

	/** Get LVE_CloseX_UU.
		@return LVE_CloseX_UU	  */
	public String getLVE_CloseX_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_CloseX_UU);
	}

	public ve.com.as.model.I_LVE_FiscalPrinter getLVE_FiscalPrinter() throws RuntimeException
    {
		return (ve.com.as.model.I_LVE_FiscalPrinter)MTable.get(getCtx(), ve.com.as.model.I_LVE_FiscalPrinter.Table_Name)
			.getPO(getLVE_FiscalPrinter_ID(), get_TrxName());	}

	/** Set Fiscal Printer.
		@param LVE_FiscalPrinter_ID Fiscal Printer	  */
	public void setLVE_FiscalPrinter_ID (int LVE_FiscalPrinter_ID)
	{
		if (LVE_FiscalPrinter_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_FiscalPrinter_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_FiscalPrinter_ID, Integer.valueOf(LVE_FiscalPrinter_ID));
	}

	/** Get Fiscal Printer.
		@return Fiscal Printer	  */
	public int getLVE_FiscalPrinter_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_FiscalPrinter_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set X Date.
		@param LVE_XDate X Date	  */
	public void setLVE_XDate (Timestamp LVE_XDate)
	{
		set_Value (COLUMNNAME_LVE_XDate, LVE_XDate);
	}

	/** Get X Date.
		@return X Date	  */
	public Timestamp getLVE_XDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_LVE_XDate);
	}

	/** Set Reduced Rate Tax.
		@param ReducedRateTax Reduced Rate Tax	  */
	public void setReducedRateTax (BigDecimal ReducedRateTax)
	{
		set_Value (COLUMNNAME_ReducedRateTax, ReducedRateTax);
	}

	/** Get Reduced Rate Tax.
		@return Reduced Rate Tax	  */
	public BigDecimal getReducedRateTax () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ReducedRateTax);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sales Additional Rate.
		@param SalesAdditionalRate Sales Additional Rate	  */
	public void setSalesAdditionalRate (BigDecimal SalesAdditionalRate)
	{
		set_Value (COLUMNNAME_SalesAdditionalRate, SalesAdditionalRate);
	}

	/** Get Sales Additional Rate.
		@return Sales Additional Rate	  */
	public BigDecimal getSalesAdditionalRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SalesAdditionalRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sales General Fee.
		@param SalesGeneralFee Sales General Fee	  */
	public void setSalesGeneralFee (BigDecimal SalesGeneralFee)
	{
		set_Value (COLUMNNAME_SalesGeneralFee, SalesGeneralFee);
	}

	/** Get Sales General Fee.
		@return Sales General Fee	  */
	public BigDecimal getSalesGeneralFee () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SalesGeneralFee);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sales Reduced Rate.
		@param SalesReducedRate Sales Reduced Rate	  */
	public void setSalesReducedRate (BigDecimal SalesReducedRate)
	{
		set_Value (COLUMNNAME_SalesReducedRate, SalesReducedRate);
	}

	/** Get Sales Reduced Rate.
		@return Sales Reduced Rate	  */
	public BigDecimal getSalesReducedRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SalesReducedRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_AD_User getSalesRep() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_SalesRep_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SubTotal IVA.
		@param SubTotalIVA SubTotal IVA	  */
	public void setSubTotalIVA (BigDecimal SubTotalIVA)
	{
		set_Value (COLUMNNAME_SubTotalIVA, SubTotalIVA);
	}

	/** Get SubTotal IVA.
		@return SubTotal IVA	  */
	public BigDecimal getSubTotalIVA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SubTotalIVA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Subtotal of Taxable Bases.
		@param SubTotalTaxBase Subtotal of Taxable Bases	  */
	public void setSubTotalTaxBase (BigDecimal SubTotalTaxBase)
	{
		set_Value (COLUMNNAME_SubTotalTaxBase, SubTotalTaxBase);
	}

	/** Get Subtotal of Taxable Bases.
		@return Subtotal of Taxable Bases	  */
	public BigDecimal getSubTotalTaxBase () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SubTotalTaxBase);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Taxable Base Extra.
		@param TaxableBaseExtra Taxable Base Extra	  */
	public void setTaxableBaseExtra (BigDecimal TaxableBaseExtra)
	{
		set_Value (COLUMNNAME_TaxableBaseExtra, TaxableBaseExtra);
	}

	/** Get Taxable Base Extra.
		@return Taxable Base Extra	  */
	public BigDecimal getTaxableBaseExtra () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxableBaseExtra);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Taxable Base Extra 2.
		@param TaxableBaseExtra2 Taxable Base Extra 2	  */
	public void setTaxableBaseExtra2 (BigDecimal TaxableBaseExtra2)
	{
		set_Value (COLUMNNAME_TaxableBaseExtra2, TaxableBaseExtra2);
	}

	/** Get Taxable Base Extra 2.
		@return Taxable Base Extra 2	  */
	public BigDecimal getTaxableBaseExtra2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxableBaseExtra2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Taxable Returns.
		@param TaxableReturns Taxable Returns	  */
	public void setTaxableReturns (BigDecimal TaxableReturns)
	{
		set_Value (COLUMNNAME_TaxableReturns, TaxableReturns);
	}

	/** Get Taxable Returns.
		@return Taxable Returns	  */
	public BigDecimal getTaxableReturns () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxableReturns);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Tax Additional Fee.
		@param TaxAdditionalFee Tax Additional Fee	  */
	public void setTaxAdditionalFee (BigDecimal TaxAdditionalFee)
	{
		set_Value (COLUMNNAME_TaxAdditionalFee, TaxAdditionalFee);
	}

	/** Get Tax Additional Fee.
		@return Tax Additional Fee	  */
	public BigDecimal getTaxAdditionalFee () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAdditionalFee);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Amount.
		@param TotalAmt 
		Total Amount
	  */
	public void setTotalAmt (BigDecimal TotalAmt)
	{
		set_ValueNoCheck (COLUMNNAME_TotalAmt, TotalAmt);
	}

	/** Get Total Amount.
		@return Total Amount
	  */
	public BigDecimal getTotalAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Returns Amt.
		@param TotalReturnsAmt Total Returns Amt	  */
	public void setTotalReturnsAmt (BigDecimal TotalReturnsAmt)
	{
		set_Value (COLUMNNAME_TotalReturnsAmt, TotalReturnsAmt);
	}

	/** Get Total Returns Amt.
		@return Total Returns Amt	  */
	public BigDecimal getTotalReturnsAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalReturnsAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Tax Returns.
		@param TotalTaxReturns Total Tax Returns	  */
	public void setTotalTaxReturns (BigDecimal TotalTaxReturns)
	{
		set_Value (COLUMNNAME_TotalTaxReturns, TotalTaxReturns);
	}

	/** Get Total Tax Returns.
		@return Total Tax Returns	  */
	public BigDecimal getTotalTaxReturns () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalTaxReturns);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}