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
import org.compiere.util.KeyNamePair;

/** Generated Model for LVE_FiscalPrinter
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_LVE_FiscalPrinter extends PO implements I_LVE_FiscalPrinter, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20161225L;

    /** Standard Constructor */
    public X_LVE_FiscalPrinter (Properties ctx, int LVE_FiscalPrinter_ID, String trxName)
    {
      super (ctx, LVE_FiscalPrinter_ID, trxName);
      /** if (LVE_FiscalPrinter_ID == 0)
        {
			setLVE_FiscalPrinter_ID (0);
			setLVE_SerialFiscal (null);
        } */
    }

    /** Load Constructor */
    public X_LVE_FiscalPrinter (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_LVE_FiscalPrinter[")
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

	/** Set Brand.
		@param Brand Brand	  */
	public void setBrand (String Brand)
	{
		set_Value (COLUMNNAME_Brand, Brand);
	}

	/** Get Brand.
		@return Brand	  */
	public String getBrand () 
	{
		return (String)get_Value(COLUMNNAME_Brand);
	}

	/** Set DatePrinter.
		@param DatePrinter DatePrinter	  */
	public void setDatePrinter (Timestamp DatePrinter)
	{
		set_Value (COLUMNNAME_DatePrinter, DatePrinter);
	}

	/** Get DatePrinter.
		@return DatePrinter	  */
	public Timestamp getDatePrinter () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePrinter);
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

	/** Set IP Host Fiscal.
		@param IPHostFiscal IP Host Fiscal	  */
	public void setIPHostFiscal (String IPHostFiscal)
	{
		set_Value (COLUMNNAME_IPHostFiscal, IPHostFiscal);
	}

	/** Get IP Host Fiscal.
		@return IP Host Fiscal	  */
	public String getIPHostFiscal () 
	{
		return (String)get_Value(COLUMNNAME_IPHostFiscal);
	}

	/** Set Is Connected.
		@param IsConnected Is Connected	  */
	public void setIsConnected (boolean IsConnected)
	{
		set_Value (COLUMNNAME_IsConnected, Boolean.valueOf(IsConnected));
	}

	/** Get Is Connected.
		@return Is Connected	  */
	public boolean isConnected () 
	{
		Object oo = get_Value(COLUMNNAME_IsConnected);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Last Credit Note Fiscal No.
		@param LastCNFiscalNo Last Credit Note Fiscal No	  */
	public void setLastCNFiscalNo (String LastCNFiscalNo)
	{
		set_Value (COLUMNNAME_LastCNFiscalNo, LastCNFiscalNo);
	}

	/** Get Last Credit Note Fiscal No.
		@return Last Credit Note Fiscal No	  */
	public String getLastCNFiscalNo () 
	{
		return (String)get_Value(COLUMNNAME_LastCNFiscalNo);
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

	/** Set Last Invoice Fiscal No.
		@param LastInvFiscalNo Last Invoice Fiscal No	  */
	public void setLastInvFiscalNo (String LastInvFiscalNo)
	{
		set_Value (COLUMNNAME_LastInvFiscalNo, LastInvFiscalNo);
	}

	/** Get Last Invoice Fiscal No.
		@return Last Invoice Fiscal No	  */
	public String getLastInvFiscalNo () 
	{
		return (String)get_Value(COLUMNNAME_LastInvFiscalNo);
	}

	/** Set Check Status Fiscal Printer.
		@param LVE_CheckStatusFP Check Status Fiscal Printer	  */
	public void setLVE_CheckStatusFP (String LVE_CheckStatusFP)
	{
		set_Value (COLUMNNAME_LVE_CheckStatusFP, LVE_CheckStatusFP);
	}

	/** Get Check Status Fiscal Printer.
		@return Check Status Fiscal Printer	  */
	public String getLVE_CheckStatusFP () 
	{
		return (String)get_Value(COLUMNNAME_LVE_CheckStatusFP);
	}

	/** Set Close X Process.
		@param LVE_CloseX_Process Close X Process	  */
	public void setLVE_CloseX_Process (String LVE_CloseX_Process)
	{
		set_Value (COLUMNNAME_LVE_CloseX_Process, LVE_CloseX_Process);
	}

	/** Get Close X Process.
		@return Close X Process	  */
	public String getLVE_CloseX_Process () 
	{
		return (String)get_Value(COLUMNNAME_LVE_CloseX_Process);
	}

	/** Set Close Z Process.
		@param LVE_CloseZ_Process Close Z Process	  */
	public void setLVE_CloseZ_Process (String LVE_CloseZ_Process)
	{
		set_Value (COLUMNNAME_LVE_CloseZ_Process, LVE_CloseZ_Process);
	}

	/** Get Close Z Process.
		@return Close Z Process	  */
	public String getLVE_CloseZ_Process () 
	{
		return (String)get_Value(COLUMNNAME_LVE_CloseZ_Process);
	}

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

	/** Set LVE_FiscalPrinter_UU.
		@param LVE_FiscalPrinter_UU LVE_FiscalPrinter_UU	  */
	public void setLVE_FiscalPrinter_UU (String LVE_FiscalPrinter_UU)
	{
		set_Value (COLUMNNAME_LVE_FiscalPrinter_UU, LVE_FiscalPrinter_UU);
	}

	/** Get LVE_FiscalPrinter_UU.
		@return LVE_FiscalPrinter_UU	  */
	public String getLVE_FiscalPrinter_UU () 
	{
		return (String)get_Value(COLUMNNAME_LVE_FiscalPrinter_UU);
	}

	/** Set Fiscal Printer Error.
		@param LVE_FPError Fiscal Printer Error	  */
	public void setLVE_FPError (String LVE_FPError)
	{
		set_Value (COLUMNNAME_LVE_FPError, LVE_FPError);
	}

	/** Get Fiscal Printer Error.
		@return Fiscal Printer Error	  */
	public String getLVE_FPError () 
	{
		return (String)get_Value(COLUMNNAME_LVE_FPError);
	}

	/** Set Fiscal Printer Status.
		@param LVE_FPStatus Fiscal Printer Status	  */
	public void setLVE_FPStatus (String LVE_FPStatus)
	{
		set_Value (COLUMNNAME_LVE_FPStatus, LVE_FPStatus);
	}

	/** Get Fiscal Printer Status.
		@return Fiscal Printer Status	  */
	public String getLVE_FPStatus () 
	{
		return (String)get_Value(COLUMNNAME_LVE_FPStatus);
	}

	/** Set Fiscal Status.
		@param LVE_FStatus Fiscal Status	  */
	public void setLVE_FStatus (String LVE_FStatus)
	{
		set_Value (COLUMNNAME_LVE_FStatus, LVE_FStatus);
	}

	/** Get Fiscal Status.
		@return Fiscal Status	  */
	public String getLVE_FStatus () 
	{
		return (String)get_Value(COLUMNNAME_LVE_FStatus);
	}

	/** Set Get Fiscal Info.
		@param LVE_GetFiscalInfo 
		Get Fiscal Information
	  */
	public void setLVE_GetFiscalInfo (String LVE_GetFiscalInfo)
	{
		set_Value (COLUMNNAME_LVE_GetFiscalInfo, LVE_GetFiscalInfo);
	}

	/** Get Get Fiscal Info.
		@return Get Fiscal Information
	  */
	public String getLVE_GetFiscalInfo () 
	{
		return (String)get_Value(COLUMNNAME_LVE_GetFiscalInfo);
	}

	/** Set Last Z No.
		@param LVE_LastZNo Last Z No	  */
	public void setLVE_LastZNo (String LVE_LastZNo)
	{
		set_Value (COLUMNNAME_LVE_LastZNo, LVE_LastZNo);
	}

	/** Get Last Z No.
		@return Last Z No	  */
	public String getLVE_LastZNo () 
	{
		return (String)get_Value(COLUMNNAME_LVE_LastZNo);
	}

	/** Set Printer Registration Number.
		@param LVE_PrinterRegNo Printer Registration Number	  */
	public void setLVE_PrinterRegNo (String LVE_PrinterRegNo)
	{
		set_Value (COLUMNNAME_LVE_PrinterRegNo, LVE_PrinterRegNo);
	}

	/** Get Printer Registration Number.
		@return Printer Registration Number	  */
	public String getLVE_PrinterRegNo () 
	{
		return (String)get_Value(COLUMNNAME_LVE_PrinterRegNo);
	}

	/** Set Qty Z.
		@param LVE_QtyZ Qty Z	  */
	public void setLVE_QtyZ (BigDecimal LVE_QtyZ)
	{
		set_Value (COLUMNNAME_LVE_QtyZ, LVE_QtyZ);
	}

	/** Get Qty Z.
		@return Qty Z	  */
	public BigDecimal getLVE_QtyZ () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LVE_QtyZ);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Serial Fiscal.
		@param LVE_SerialFiscal Serial Fiscal	  */
	public void setLVE_SerialFiscal (String LVE_SerialFiscal)
	{
		set_Value (COLUMNNAME_LVE_SerialFiscal, LVE_SerialFiscal);
	}

	/** Get Serial Fiscal.
		@return Serial Fiscal	  */
	public String getLVE_SerialFiscal () 
	{
		return (String)get_Value(COLUMNNAME_LVE_SerialFiscal);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getLVE_SerialFiscal());
    }

	/** Set Model.
		@param Model Model	  */
	public void setModel (String Model)
	{
		set_Value (COLUMNNAME_Model, Model);
	}

	/** Get Model.
		@return Model	  */
	public String getModel () 
	{
		return (String)get_Value(COLUMNNAME_Model);
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

	/** Set Qty of Invoice by Day.
		@param QtyInvoiceDay Qty of Invoice by Day	  */
	public void setQtyInvoiceDay (BigDecimal QtyInvoiceDay)
	{
		set_Value (COLUMNNAME_QtyInvoiceDay, QtyInvoiceDay);
	}

	/** Get Qty of Invoice by Day.
		@return Qty of Invoice by Day	  */
	public BigDecimal getQtyInvoiceDay () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyInvoiceDay);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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