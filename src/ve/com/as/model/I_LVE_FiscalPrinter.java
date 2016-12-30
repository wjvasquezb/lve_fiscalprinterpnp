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
package ve.com.as.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for LVE_FiscalPrinter
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
@SuppressWarnings("all")
public interface I_LVE_FiscalPrinter 
{

    /** TableName=LVE_FiscalPrinter */
    public static final String Table_Name = "LVE_FiscalPrinter";

    /** AD_Table_ID=1000027 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_Rule_ID */
    public static final String COLUMNNAME_AD_Rule_ID = "AD_Rule_ID";

	/** Set Rule	  */
	public void setAD_Rule_ID (int AD_Rule_ID);

	/** Get Rule	  */
	public int getAD_Rule_ID();

	public org.compiere.model.I_AD_Rule getAD_Rule() throws RuntimeException;

    /** Column name Brand */
    public static final String COLUMNNAME_Brand = "Brand";

	/** Set Brand	  */
	public void setBrand (String Brand);

	/** Get Brand	  */
	public String getBrand();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DatePrinter */
    public static final String COLUMNNAME_DatePrinter = "DatePrinter";

	/** Set DatePrinter	  */
	public void setDatePrinter (Timestamp DatePrinter);

	/** Get DatePrinter	  */
	public Timestamp getDatePrinter();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IPHostFiscal */
    public static final String COLUMNNAME_IPHostFiscal = "IPHostFiscal";

	/** Set IP Host Fiscal	  */
	public void setIPHostFiscal (String IPHostFiscal);

	/** Get IP Host Fiscal	  */
	public String getIPHostFiscal();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsConnected */
    public static final String COLUMNNAME_IsConnected = "IsConnected";

	/** Set Is Connected	  */
	public void setIsConnected (boolean IsConnected);

	/** Get Is Connected	  */
	public boolean isConnected();

    /** Column name LastCNFiscalNo */
    public static final String COLUMNNAME_LastCNFiscalNo = "LastCNFiscalNo";

	/** Set Last Credit Note Fiscal No	  */
	public void setLastCNFiscalNo (String LastCNFiscalNo);

	/** Get Last Credit Note Fiscal No	  */
	public String getLastCNFiscalNo();

    /** Column name LastInvFiscalDate */
    public static final String COLUMNNAME_LastInvFiscalDate = "LastInvFiscalDate";

	/** Set Last Invoice Fiscal Date	  */
	public void setLastInvFiscalDate (Timestamp LastInvFiscalDate);

	/** Get Last Invoice Fiscal Date	  */
	public Timestamp getLastInvFiscalDate();

    /** Column name LastInvFiscalNo */
    public static final String COLUMNNAME_LastInvFiscalNo = "LastInvFiscalNo";

	/** Set Last Invoice Fiscal No	  */
	public void setLastInvFiscalNo (String LastInvFiscalNo);

	/** Get Last Invoice Fiscal No	  */
	public String getLastInvFiscalNo();

    /** Column name LVE_CheckStatusFP */
    public static final String COLUMNNAME_LVE_CheckStatusFP = "LVE_CheckStatusFP";

	/** Set Check Status Fiscal Printer	  */
	public void setLVE_CheckStatusFP (String LVE_CheckStatusFP);

	/** Get Check Status Fiscal Printer	  */
	public String getLVE_CheckStatusFP();

    /** Column name LVE_CloseX_Process */
    public static final String COLUMNNAME_LVE_CloseX_Process = "LVE_CloseX_Process";

	/** Set Close X Process	  */
	public void setLVE_CloseX_Process (String LVE_CloseX_Process);

	/** Get Close X Process	  */
	public String getLVE_CloseX_Process();

    /** Column name LVE_CloseZ_Process */
    public static final String COLUMNNAME_LVE_CloseZ_Process = "LVE_CloseZ_Process";

	/** Set Close Z Process	  */
	public void setLVE_CloseZ_Process (String LVE_CloseZ_Process);

	/** Get Close Z Process	  */
	public String getLVE_CloseZ_Process();

    /** Column name LVE_FiscalPrinter_ID */
    public static final String COLUMNNAME_LVE_FiscalPrinter_ID = "LVE_FiscalPrinter_ID";

	/** Set Fiscal Printer	  */
	public void setLVE_FiscalPrinter_ID (int LVE_FiscalPrinter_ID);

	/** Get Fiscal Printer	  */
	public int getLVE_FiscalPrinter_ID();

    /** Column name LVE_FiscalPrinter_UU */
    public static final String COLUMNNAME_LVE_FiscalPrinter_UU = "LVE_FiscalPrinter_UU";

	/** Set LVE_FiscalPrinter_UU	  */
	public void setLVE_FiscalPrinter_UU (String LVE_FiscalPrinter_UU);

	/** Get LVE_FiscalPrinter_UU	  */
	public String getLVE_FiscalPrinter_UU();

    /** Column name LVE_FPError */
    public static final String COLUMNNAME_LVE_FPError = "LVE_FPError";

	/** Set Fiscal Printer Error	  */
	public void setLVE_FPError (String LVE_FPError);

	/** Get Fiscal Printer Error	  */
	public String getLVE_FPError();

    /** Column name LVE_FPStatus */
    public static final String COLUMNNAME_LVE_FPStatus = "LVE_FPStatus";

	/** Set Fiscal Printer Status	  */
	public void setLVE_FPStatus (String LVE_FPStatus);

	/** Get Fiscal Printer Status	  */
	public String getLVE_FPStatus();

    /** Column name LVE_FStatus */
    public static final String COLUMNNAME_LVE_FStatus = "LVE_FStatus";

	/** Set Fiscal Status	  */
	public void setLVE_FStatus (String LVE_FStatus);

	/** Get Fiscal Status	  */
	public String getLVE_FStatus();

    /** Column name LVE_GetFiscalInfo */
    public static final String COLUMNNAME_LVE_GetFiscalInfo = "LVE_GetFiscalInfo";

	/** Set Get Fiscal Info.
	  * Get Fiscal Information
	  */
	public void setLVE_GetFiscalInfo (String LVE_GetFiscalInfo);

	/** Get Get Fiscal Info.
	  * Get Fiscal Information
	  */
	public String getLVE_GetFiscalInfo();

    /** Column name LVE_LastZNo */
    public static final String COLUMNNAME_LVE_LastZNo = "LVE_LastZNo";

	/** Set Last Z No	  */
	public void setLVE_LastZNo (String LVE_LastZNo);

	/** Get Last Z No	  */
	public String getLVE_LastZNo();

    /** Column name LVE_PrinterRegNo */
    public static final String COLUMNNAME_LVE_PrinterRegNo = "LVE_PrinterRegNo";

	/** Set Printer Registration Number	  */
	public void setLVE_PrinterRegNo (String LVE_PrinterRegNo);

	/** Get Printer Registration Number	  */
	public String getLVE_PrinterRegNo();

    /** Column name LVE_QtyZ */
    public static final String COLUMNNAME_LVE_QtyZ = "LVE_QtyZ";

	/** Set Qty Z	  */
	public void setLVE_QtyZ (BigDecimal LVE_QtyZ);

	/** Get Qty Z	  */
	public BigDecimal getLVE_QtyZ();

    /** Column name LVE_SerialFiscal */
    public static final String COLUMNNAME_LVE_SerialFiscal = "LVE_SerialFiscal";

	/** Set Serial Fiscal	  */
	public void setLVE_SerialFiscal (String LVE_SerialFiscal);

	/** Get Serial Fiscal	  */
	public String getLVE_SerialFiscal();

    /** Column name Model */
    public static final String COLUMNNAME_Model = "Model";

	/** Set Model	  */
	public void setModel (String Model);

	/** Get Model	  */
	public String getModel();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name QtyInvoiceDay */
    public static final String COLUMNNAME_QtyInvoiceDay = "QtyInvoiceDay";

	/** Set Qty of Invoice by Day	  */
	public void setQtyInvoiceDay (BigDecimal QtyInvoiceDay);

	/** Get Qty of Invoice by Day	  */
	public BigDecimal getQtyInvoiceDay();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
