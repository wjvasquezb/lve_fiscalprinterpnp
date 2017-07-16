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
package org.idempiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for LVE_CloseZ
 *  @author iDempiere (generated) 
 *  @version Release 4.1
 */
@SuppressWarnings("all")
public interface I_LVE_CloseZ 
{

    /** TableName=LVE_CloseZ */
    public static final String Table_Name = "LVE_CloseZ";

    /** AD_Table_ID=1000030 */
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

    /** Column name AmountPay */
    public static final String COLUMNNAME_AmountPay = "AmountPay";

	/** Set Amount Payable	  */
	public void setAmountPay (BigDecimal AmountPay);

	/** Get Amount Payable	  */
	public BigDecimal getAmountPay();

    /** Column name BaseTaxableExtra */
    public static final String COLUMNNAME_BaseTaxableExtra = "BaseTaxableExtra";

	/** Set BaseTaxable Extra	  */
	public void setBaseTaxableExtra (BigDecimal BaseTaxableExtra);

	/** Get BaseTaxable Extra	  */
	public BigDecimal getBaseTaxableExtra();

    /** Column name BaseTaxableExtra2 */
    public static final String COLUMNNAME_BaseTaxableExtra2 = "BaseTaxableExtra2";

	/** Set Base Taxable Extra 2	  */
	public void setBaseTaxableExtra2 (BigDecimal BaseTaxableExtra2);

	/** Get Base Taxable Extra 2	  */
	public BigDecimal getBaseTaxableExtra2();

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

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

    /** Column name ExemptSales */
    public static final String COLUMNNAME_ExemptSales = "ExemptSales";

	/** Set ExemptSales	  */
	public void setExemptSales (BigDecimal ExemptSales);

	/** Get ExemptSales	  */
	public BigDecimal getExemptSales();

    /** Column name ExemptSalesCN */
    public static final String COLUMNNAME_ExemptSalesCN = "ExemptSalesCN";

	/** Set ExemptSalesCN	  */
	public void setExemptSalesCN (BigDecimal ExemptSalesCN);

	/** Get ExemptSalesCN	  */
	public BigDecimal getExemptSalesCN();

    /** Column name GeneralTaxRate */
    public static final String COLUMNNAME_GeneralTaxRate = "GeneralTaxRate";

	/** Set General Tax Rate	  */
	public void setGeneralTaxRate (BigDecimal GeneralTaxRate);

	/** Get General Tax Rate	  */
	public BigDecimal getGeneralTaxRate();

    /** Column name GeneralTaxRateCN */
    public static final String COLUMNNAME_GeneralTaxRateCN = "GeneralTaxRateCN";

	/** Set General Tax Rate CN	  */
	public void setGeneralTaxRateCN (BigDecimal GeneralTaxRateCN);

	/** Get General Tax Rate CN	  */
	public BigDecimal getGeneralTaxRateCN();

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

    /** Column name LastInvFiscalDate */
    public static final String COLUMNNAME_LastInvFiscalDate = "LastInvFiscalDate";

	/** Set Last Invoice Fiscal Date	  */
	public void setLastInvFiscalDate (Timestamp LastInvFiscalDate);

	/** Get Last Invoice Fiscal Date	  */
	public Timestamp getLastInvFiscalDate();

    /** Column name LVE_CloseZ_ID */
    public static final String COLUMNNAME_LVE_CloseZ_ID = "LVE_CloseZ_ID";

	/** Set Close Z	  */
	public void setLVE_CloseZ_ID (int LVE_CloseZ_ID);

	/** Get Close Z	  */
	public int getLVE_CloseZ_ID();

    /** Column name LVE_CloseZ_UU */
    public static final String COLUMNNAME_LVE_CloseZ_UU = "LVE_CloseZ_UU";

	/** Set Close Z UU	  */
	public void setLVE_CloseZ_UU (String LVE_CloseZ_UU);

	/** Get Close Z UU	  */
	public String getLVE_CloseZ_UU();

    /** Column name LVE_FiscalPrinter_ID */
    public static final String COLUMNNAME_LVE_FiscalPrinter_ID = "LVE_FiscalPrinter_ID";

	/** Set Fiscal Printer	  */
	public void setLVE_FiscalPrinter_ID (int LVE_FiscalPrinter_ID);

	/** Get Fiscal Printer	  */
	public int getLVE_FiscalPrinter_ID();

	public I_LVE_FiscalPrinter getLVE_FiscalPrinter() throws RuntimeException;

    /** Column name LVE_ZDate */
    public static final String COLUMNNAME_LVE_ZDate = "LVE_ZDate";

	/** Set Z Date	  */
	public void setLVE_ZDate (Timestamp LVE_ZDate);

	/** Get Z Date	  */
	public Timestamp getLVE_ZDate();

    /** Column name LVE_ZNo */
    public static final String COLUMNNAME_LVE_ZNo = "LVE_ZNo";

	/** Set Z No	  */
	public void setLVE_ZNo (String LVE_ZNo);

	/** Get Z No	  */
	public String getLVE_ZNo();

    /** Column name ReducedRateTax */
    public static final String COLUMNNAME_ReducedRateTax = "ReducedRateTax";

	/** Set Reduced Rate Tax	  */
	public void setReducedRateTax (BigDecimal ReducedRateTax);

	/** Get Reduced Rate Tax	  */
	public BigDecimal getReducedRateTax();

    /** Column name ReducedRateTaxCN */
    public static final String COLUMNNAME_ReducedRateTaxCN = "ReducedRateTaxCN";

	/** Set Reduced Rate Tax CN	  */
	public void setReducedRateTaxCN (BigDecimal ReducedRateTaxCN);

	/** Get Reduced Rate Tax CN	  */
	public BigDecimal getReducedRateTaxCN();

    /** Column name SalesAdditionalRate */
    public static final String COLUMNNAME_SalesAdditionalRate = "SalesAdditionalRate";

	/** Set Sales Additional Rate	  */
	public void setSalesAdditionalRate (BigDecimal SalesAdditionalRate);

	/** Get Sales Additional Rate	  */
	public BigDecimal getSalesAdditionalRate();

    /** Column name SalesAdditionalRateCN */
    public static final String COLUMNNAME_SalesAdditionalRateCN = "SalesAdditionalRateCN";

	/** Set Sales Additional Rate CN	  */
	public void setSalesAdditionalRateCN (BigDecimal SalesAdditionalRateCN);

	/** Get Sales Additional Rate CN	  */
	public BigDecimal getSalesAdditionalRateCN();

    /** Column name SalesGeneralFee */
    public static final String COLUMNNAME_SalesGeneralFee = "SalesGeneralFee";

	/** Set Sales General Fee	  */
	public void setSalesGeneralFee (BigDecimal SalesGeneralFee);

	/** Get Sales General Fee	  */
	public BigDecimal getSalesGeneralFee();

    /** Column name SalesGeneralFeeCN */
    public static final String COLUMNNAME_SalesGeneralFeeCN = "SalesGeneralFeeCN";

	/** Set Sales General Fee CN	  */
	public void setSalesGeneralFeeCN (BigDecimal SalesGeneralFeeCN);

	/** Get Sales General Fee CN	  */
	public BigDecimal getSalesGeneralFeeCN();

    /** Column name SalesReducedRate */
    public static final String COLUMNNAME_SalesReducedRate = "SalesReducedRate";

	/** Set Sales Reduced Rate	  */
	public void setSalesReducedRate (BigDecimal SalesReducedRate);

	/** Get Sales Reduced Rate	  */
	public BigDecimal getSalesReducedRate();

    /** Column name SalesReducedRateCN */
    public static final String COLUMNNAME_SalesReducedRateCN = "SalesReducedRateCN";

	/** Set Sales Reduced Rate CN	  */
	public void setSalesReducedRateCN (BigDecimal SalesReducedRateCN);

	/** Get Sales Reduced Rate CN	  */
	public BigDecimal getSalesReducedRateCN();

    /** Column name SalesRep_ID */
    public static final String COLUMNNAME_SalesRep_ID = "SalesRep_ID";

	/** Set Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID);

	/** Get Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public int getSalesRep_ID();

	public org.compiere.model.I_AD_User getSalesRep() throws RuntimeException;

    /** Column name SubTotalIVA */
    public static final String COLUMNNAME_SubTotalIVA = "SubTotalIVA";

	/** Set SubTotal IVA	  */
	public void setSubTotalIVA (BigDecimal SubTotalIVA);

	/** Get SubTotal IVA	  */
	public BigDecimal getSubTotalIVA();

    /** Column name SubTotalIVACN */
    public static final String COLUMNNAME_SubTotalIVACN = "SubTotalIVACN";

	/** Set SubTotal IVA CN	  */
	public void setSubTotalIVACN (BigDecimal SubTotalIVACN);

	/** Get SubTotal IVA CN	  */
	public BigDecimal getSubTotalIVACN();

    /** Column name SubTotalTaxBase */
    public static final String COLUMNNAME_SubTotalTaxBase = "SubTotalTaxBase";

	/** Set Subtotal of Taxable Bases	  */
	public void setSubTotalTaxBase (BigDecimal SubTotalTaxBase);

	/** Get Subtotal of Taxable Bases	  */
	public BigDecimal getSubTotalTaxBase();

    /** Column name SubTotalTaxBaseCN */
    public static final String COLUMNNAME_SubTotalTaxBaseCN = "SubTotalTaxBaseCN";

	/** Set Subtotal of Taxable Bases CN	  */
	public void setSubTotalTaxBaseCN (BigDecimal SubTotalTaxBaseCN);

	/** Get Subtotal of Taxable Bases CN	  */
	public BigDecimal getSubTotalTaxBaseCN();

    /** Column name TaxableBaseExtra */
    public static final String COLUMNNAME_TaxableBaseExtra = "TaxableBaseExtra";

	/** Set Taxable Base Extra	  */
	public void setTaxableBaseExtra (BigDecimal TaxableBaseExtra);

	/** Get Taxable Base Extra	  */
	public BigDecimal getTaxableBaseExtra();

    /** Column name TaxableBaseExtra2 */
    public static final String COLUMNNAME_TaxableBaseExtra2 = "TaxableBaseExtra2";

	/** Set Taxable Base Extra 2	  */
	public void setTaxableBaseExtra2 (BigDecimal TaxableBaseExtra2);

	/** Get Taxable Base Extra 2	  */
	public BigDecimal getTaxableBaseExtra2();

    /** Column name TaxableReturns */
    public static final String COLUMNNAME_TaxableReturns = "TaxableReturns";

	/** Set Taxable Returns	  */
	public void setTaxableReturns (BigDecimal TaxableReturns);

	/** Get Taxable Returns	  */
	public BigDecimal getTaxableReturns();

    /** Column name TaxAdditionalFee */
    public static final String COLUMNNAME_TaxAdditionalFee = "TaxAdditionalFee";

	/** Set Tax Additional Fee	  */
	public void setTaxAdditionalFee (BigDecimal TaxAdditionalFee);

	/** Get Tax Additional Fee	  */
	public BigDecimal getTaxAdditionalFee();

    /** Column name TaxAdditionalFeeCN */
    public static final String COLUMNNAME_TaxAdditionalFeeCN = "TaxAdditionalFeeCN";

	/** Set Tax Additional Fee CN	  */
	public void setTaxAdditionalFeeCN (BigDecimal TaxAdditionalFeeCN);

	/** Get Tax Additional Fee CN	  */
	public BigDecimal getTaxAdditionalFeeCN();

    /** Column name TotalAmt */
    public static final String COLUMNNAME_TotalAmt = "TotalAmt";

	/** Set Total Amount.
	  * Total Amount
	  */
	public void setTotalAmt (BigDecimal TotalAmt);

	/** Get Total Amount.
	  * Total Amount
	  */
	public BigDecimal getTotalAmt();

    /** Column name TotalReturnsAmt */
    public static final String COLUMNNAME_TotalReturnsAmt = "TotalReturnsAmt";

	/** Set Total Returns Amt	  */
	public void setTotalReturnsAmt (BigDecimal TotalReturnsAmt);

	/** Get Total Returns Amt	  */
	public BigDecimal getTotalReturnsAmt();

    /** Column name TotalTaxReturns */
    public static final String COLUMNNAME_TotalTaxReturns = "TotalTaxReturns";

	/** Set Total Tax Returns	  */
	public void setTotalTaxReturns (BigDecimal TotalTaxReturns);

	/** Get Total Tax Returns	  */
	public BigDecimal getTotalTaxReturns();

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
}
