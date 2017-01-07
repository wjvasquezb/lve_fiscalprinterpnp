/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html			*          
 * @author Ing. Victor Suárez - victor.suarez.is@gmail.com - 2016/12		*
 *****************************************************************************/
package ve.com.as.model;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.compiere.model.MBPartner;
import org.compiere.model.MCharge;
import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MProduct;
import org.compiere.model.MTable;
import org.compiere.model.MTax;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Ini;

import ve.com.as.component.IDLLPnP;

import com.sun.jna.Native;

/**
 * 
 * @author Ing. Victor Suárez - victor.suarez.is@gmail.com - 2016/12
 * 	Clase para Imprimir Factura Fiscal antes de completar Documentos.
 *
 */

public class LVE_FiscalPrinter implements ModelValidator {
	
	public int ad_client_id;
	public String invoiceInfo;
	public BufferedReader bReader;
	public String LVE_FiscalDocNo = "";
	
	CLogger log = CLogger.getCLogger(LVE_FiscalPrinter.class);
	public String msg = "";

	public static IDLLPnP dllPnP;

	public LVE_FiscalPrinter() {
		msg = "Imprimir Factura Fiscal: LVE_FiscalPrinter()";
		log.info(msg);
		if(Ini.isClient()) {
			msg = "Se carga la librería";
			System.load("C:\\adempiere-client\\PnP\\pnpdll.dll");
			dllPnP = (IDLLPnP)Native.loadLibrary("pnpdll", IDLLPnP.class);
		}
	}

	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		if(client != null)
			ad_client_id = client.getAD_Client_ID();
		engine.addDocValidate(MInvoice.Table_Name, this);
	}

	@Override
	public int getAD_Client_ID() {
		return ad_client_id;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.warning("----- User Logged in with org/role/user: " + AD_Org_ID + " - " + AD_Role_ID + " - " + AD_User_ID + " - LVE_FiscalPrinter");
		return null;
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		log.warning("------ Validating Model: " + po.get_TableName() + " with type: " + type);
		return null;
	}

	@Override
	public String docValidate(PO po, int timing) {
		log.warning("------ Validating Model docValidate: " + po.get_TableName() + " with timing: " + timing);
		
		if(timing == TIMING_BEFORE_COMPLETE) {
			if(!po.get_TableName().equals(MInvoice.Table_Name))
				return null;			
			
			MInvoice invoice = (MInvoice)po;
			MDocType docType = new MDocType(invoice.getCtx(), invoice.getC_DocType_ID(), invoice.get_TrxName());
			
			if(!(boolean)docType.get_ValueOfColumn(MColumn.getColumn_ID(MDocType.Table_Name, "IsFiscalDocument")))
				return null;
			if(!Ini.isClient())
				return "No puede Completar Documento Fiscal desde el Cliente Web, debe usar el Cliente Swing";
			
			MBPartner partner = new MBPartner(invoice.getCtx(), invoice.getC_BPartner_ID(), invoice.get_TrxName());
			log.warning("Imprimiendo Factura " + invoice.getDocumentNo() + " de " + partner.getName() + " - " + partner.getTaxID());
			invoiceInfo = printInvoice(partner, invoice, docType);
			if(invoiceInfo.toUpperCase().contains("ERROR")) {
				msg = invoiceInfo;
				log.warning(msg);
				return msg;
			} else  {
				if(invoiceInfo != null)
					LVE_FiscalDocNo = invoiceInfo.split(",")[2];
				invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalDocNo"), LVE_FiscalDocNo);
				invoice.saveEx();
				msg = "Documento Fiscal Nro: " + LVE_FiscalDocNo + " - Impresa correctamente. - " + invoiceInfo;
				log.warning(msg);
			}
		}
		return null;
	}

	private String printInvoice(MBPartner partner, MInvoice invoice, MDocType docType) {
		PO taxIdType = new Query(partner.getCtx(), MTable.get(partner.getCtx(), "LCO_TaxIdType"), "LCO_TaxIdType_ID =? ", partner.get_TrxName()).setParameters(partner.get_ValueOfColumn(MColumn.getColumn_ID(MBPartner.Table_Name, "LCO_TaxIdType_ID"))).first();			
		String typePerson = taxIdType.get_Value("Name").toString();
		
		/**	DATOS DE LA FACTURA FISCAL	**/
		String name = partner.getName().toUpperCase();
		String taxID = typePerson + partner.getTaxID();
		String text = "Ficha: " + partner.getValue();
		int fiscalPrinterID = (int) docType.get_ValueOfColumn(MColumn.getColumn_ID(MDocType.Table_Name, "LVE_FiscalPrinter_ID")); 
		MLVEFiscalPrinter fiscalPrinter = new MLVEFiscalPrinter(invoice.getCtx(), fiscalPrinterID, invoice.get_TrxName()); 
		int port = fiscalPrinter.getLVE_FiscalPort();
		
		if(port == 0) 
			return "ERROR - Debe seleccionar Puerto de Impresora Fiscal";
		msg = dllPnP.PFabrepuerto(String.valueOf(port));
		if(!msg.equals("OK"))
			return "ERROR abriendo Puerto Impresora - " + msg;
		
		if(docType.getDocBaseType().equals(MDocType.DOCBASETYPE_ARInvoice)) {
			msg = dllPnP.PFabrefiscal(name, taxID);
			if(!msg.equals("OK"))
				return "ERROR abriendo Factura Fiscal - " + msg;
			msg = dllPnP.PFTfiscal(text);
			if(!msg.equals("OK"))
				return "ERROR agregando Ficha a la Factura Fiscal - " + msg;
			/**	DATOS DE LA LINEA DE LA FACTURA FISCAL	**/
			for(MInvoiceLine invoiceLine : invoice.getLines()) {
				String description = invoiceLine.getDescription();
				if(invoiceLine.getM_Product_ID() != 0) {
					MProduct product = new MProduct(invoiceLine.getCtx(), invoiceLine.getM_Product_ID(), invoiceLine.get_TrxName());
					description = product.getName();
				} 
				if(invoiceLine.getC_Charge_ID() != 0) {
					MCharge charge = new MCharge(invoiceLine.getCtx(), invoiceLine.getC_Charge_ID(), invoiceLine.get_TrxName());
					description = charge.getName();
				}
				MTax tax = new MTax(invoiceLine.getCtx(), invoiceLine.getC_Tax_ID(), invoice.get_TrxName());
				String price = formatPrice(invoiceLine.getPriceEntered());
				String iva = formatIVA(tax.getRate());
				String qty = formatQty(invoiceLine.getQtyEntered());
				
				msg = dllPnP.PFrenglon(description, qty, price, iva);
				if(!msg.equals("OK"))
					return "ERROR agregando Lineas - " + msg;
		}
		} else if (docType.getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo)){
			MInvoice invoiceAffected = new MInvoice(invoice.getCtx(), invoice.get_ValueAsInt(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_InvoiceAffected")), invoice.get_TrxName());
			String date = formatDate(invoiceAffected.getDateAcct());
			String hour = formatHour(invoiceAffected.getDateAcct());
			
			msg = dllPnP.PFDevolucion(name, taxID, invoiceAffected.get_ValueAsString("LVE_FiscalDocNo"), fiscalPrinter.getLVE_SerialFiscal(), date, hour);
			if(!msg.equals("OK"))
				return "ERROR abriendo Factura Fiscal - " + msg;
			msg = dllPnP.PFTfiscal(text);
			if(!msg.equals("OK"))
				return "ERROR agregando Ficha a la Factura Fiscal - " + msg;
			/**	DATOS DE LA LINEA DE LA NOTA DE CRÉDITO	**/
			for(MInvoiceLine invoiceLine : invoice.getLines()) {
				String description = invoiceLine.getDescription();
				if(invoiceLine.getM_Product_ID() != 0) {
					MProduct product = new MProduct(invoiceLine.getCtx(), invoiceLine.getM_Product_ID(), invoiceLine.get_TrxName());
					description = product.getName();
				} 
				if(invoiceLine.getC_Charge_ID() != 0) {
					MCharge charge = new MCharge(invoiceLine.getCtx(), invoiceLine.getC_Charge_ID(), invoiceLine.get_TrxName());
					description = charge.getName();
				}
				MTax tax = new MTax(invoiceLine.getCtx(), invoiceLine.getC_Tax_ID(), invoice.get_TrxName());
				String price = formatPrice(invoiceLine.getPriceEntered());
				String iva = formatIVA(tax.getRate());
				String qty = formatQty(invoiceLine.getQtyEntered());
				
				msg = dllPnP.PFrenglon(description, qty, price, iva);
				if(!msg.equals("OK"))
					return "ERROR agregando Lineas - " + msg;
		}
		}
		
		msg = dllPnP.PFtotal();
		if(!msg.equals("OK"))
			return "ERROR agregando Total al Documento Fiscal - " + msg;
		
		/**	Obtener Información de la Impresora Fiscal	**/
		String fiscalInf = dllPnP.PFultimo();
		
		msg = dllPnP.PFcierrapuerto();
		if(!msg.equals("OK"))
			return "ERROR agregando Total al Documento Fiscal - " + msg;
		return fiscalInf;
	}
	
	private String formatHour(java.sql.Timestamp timestamp) {
		String hourStr = new SimpleDateFormat("HHmm").format(timestamp);
		return hourStr;
	}

	private String formatDate(java.sql.Timestamp timestamp) {
		String dateStr = new SimpleDateFormat("ddMMyyy").format(timestamp);
		return dateStr;
	}

	public String formatQty(BigDecimal monto) {
		String value="";		
		DecimalFormat decf = new DecimalFormat("######.000");
		value = decf.format(monto);
		value = value.replace(",", ".");
		return value;
	}
	
	public String formatIVA(BigDecimal monto) {
		String value="";		
		DecimalFormat decf = new DecimalFormat("######.00");
		value = decf.format(monto);
		value = value.replace(",", "");
		return value;
	}
	
	public String formatPrice(BigDecimal monto) {
		String value="";		
		DecimalFormat decf = new DecimalFormat("######.00");
		value = decf.format(monto);
		value = value.replace(",", ".");
		return value;
	}
}
