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
package org.idempiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LVE_FiscalPResources
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_LVE_FiscalPResources extends PO implements I_LVE_FiscalPResources, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170715L;

    /** Standard Constructor */
    public X_LVE_FiscalPResources (Properties ctx, int LVE_FiscalPResources_ID, String trxName)
    {
      super (ctx, LVE_FiscalPResources_ID, trxName);
      /** if (LVE_FiscalPResources_ID == 0)
        {
			setAD_Rule_ID (0);
			setLVE_FiscalPrinter_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LVE_FiscalPResources (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_FiscalPResources[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Rule getAD_Rule() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Rule)MTable.get(getCtx(), org.compiere.model.I_AD_Rule.Table_Name)
			.getPO(getAD_Rule_ID(), get_TrxName());	}

	/** Set Rule.
		@param AD_Rule_ID Rule	  */
	public void setAD_Rule_ID (int AD_Rule_ID)
	{
		if (AD_Rule_ID < 1) 
			set_Value (COLUMNNAME_AD_Rule_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Rule_ID, Integer.valueOf(AD_Rule_ID));
	}

	/** Get Rule.
		@return Rule	  */
	public int getAD_Rule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Rule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Fiscal Printer Resources.
		@param LVE_FiscalPResources_ID Fiscal Printer Resources	  */
	public void setLVE_FiscalPResources_ID (int LVE_FiscalPResources_ID)
	{
		if (LVE_FiscalPResources_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LVE_FiscalPResources_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LVE_FiscalPResources_ID, Integer.valueOf(LVE_FiscalPResources_ID));
	}

	/** Get Fiscal Printer Resources.
		@return Fiscal Printer Resources	  */
	public int getLVE_FiscalPResources_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LVE_FiscalPResources_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LVE_FiscalPResources_UU.
		@param LVE_FiscalPResources_UU LVE_FiscalPResources_UU	  */
	public void setLVE_FiscalPResources_UU (String LVE_FiscalPResources_UU)
	{
		set_ValueNoCheck (COLUMNNAME_LVE_FiscalPResources_UU, LVE_FiscalPResources_UU);
	}

	/** Get LVE_FiscalPResources_UU.
		@return LVE_FiscalPResources_UU	  */
	public String getLVE_FiscalPResources_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_FiscalPResources_UU);
	}

	public I_LVE_FiscalPrinter getLVE_FiscalPrinter() throws RuntimeException
    {
		return (I_LVE_FiscalPrinter)MTable.get(getCtx(), I_LVE_FiscalPrinter.Table_Name)
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}