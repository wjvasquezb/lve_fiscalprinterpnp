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
import org.compiere.model.MBPartner;
import org.compiere.model.MCharge;
import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrderLine;
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
	public String creditNoteInfo;
	public BufferedReader bReader;
	public String LVE_FiscalDocNo = "";
	public String msg = "";
	public String status = "";
	public String LVE_FiscalHour = "";
	public String LVE_FiscalDate = "";

	public static IDLLPnP dllPnP;

	CLogger log = CLogger.getCLogger(LVE_FiscalPrinter.class);
	
	public LVE_FiscalPrinter() {
		msg = "Imprimir Factura Fiscal: LVE_FiscalPrinter()";
		System.out.println(msg);
		log.info(msg);
		if(Ini.isClient()) {
			msg = "Se carga la librería";
			System.out.println(msg);
			log.info(msg);
			System.load("C:\\adempiere-client\\PnP\\pnpdll.dll");
			dllPnP = (IDLLPnP)Native.loadLibrary("pnpdll", IDLLPnP.class);
		}
	}

	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		if(client != null)
			ad_client_id = client.getAD_Client_ID();
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addModelChange(MOrderLine.Table_Name, this);
		engine.addModelChange(MInvoiceLine.Table_Name, this);
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
		
		if(type != TYPE_AFTER_CHANGE && type != TYPE_AFTER_NEW) 
			return null;
		if(!po.get_TableName().equals(MOrderLine.Table_Name) && !po.get_TableName().equals(MInvoiceLine.Table_Name))
			return null;

		if(po.get_TableName().equals(MOrderLine.Table_Name)) {
			MOrderLine orderLine = (MOrderLine) po;
			String description = orderLine.getDescription();
			if(orderLine.getM_Product_ID() != 0) {
				MProduct product = new MProduct(orderLine.getCtx(), orderLine.getM_Product_ID(), orderLine.get_TrxName());
				description = product.getName();
			} 
			if(orderLine.getC_Charge_ID() != 0) {
				MCharge charge = new MCharge(orderLine.getCtx(), orderLine.getC_Charge_ID(), orderLine.get_TrxName());
				description = charge.getName();
			}
			if(description == null || description == "")
				return "Debe agregar una Descripción, un Producto o un Cargo.";
		}
		if(po.get_TableName().equals(MInvoiceLine.Table_Name)) {
			MInvoiceLine invoiceLine = (MInvoiceLine) po;
			String description = invoiceLine.getDescription();
			if(invoiceLine.getM_Product_ID() != 0) {
				MProduct product = new MProduct(invoiceLine.getCtx(), invoiceLine.getM_Product_ID(), invoiceLine.get_TrxName());
				description = product.getName();
			} 
			if(invoiceLine.getC_Charge_ID() != 0) {
				MCharge charge = new MCharge(invoiceLine.getCtx(), invoiceLine.getC_Charge_ID(), invoiceLine.get_TrxName());
				description = charge.getName();
			}
			if(description == null || description == "")
				return "Debe agregar una Descripción, un Producto o un Cargo.";
		}
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
			String text = "Ficha: " + partner.getValue() + " - DocNo: " + invoice.getDocumentNo();
			if(partner.isManufacturer() && partner.getBPartner_Parent_ID() != 0)
				partner = new MBPartner(invoice.getCtx(), partner.getBPartner_Parent_ID(), invoice.get_TrxName());
			invoiceInfo = printInvoice(partner, invoice, docType, text);
			if(invoiceInfo.toUpperCase().contains("ERROR")) {
				msg = invoiceInfo;
				log.warning(msg);
				return msg;
			} else  {
				invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalDocNo"), LVE_FiscalDocNo);
				invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalDate"), LVE_FiscalDate);
				invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalHour"), LVE_FiscalHour);
				invoice.saveEx();
				msg = "Documento Fiscal Nro: " + LVE_FiscalDocNo + " - Impresa correctamente. - " + invoiceInfo;
				log.warning(msg);
			}
		}
		return null;
	}

	private String printInvoice(MBPartner partner, MInvoice invoice, MDocType docType, String text) {
		PO taxIdType = new Query(partner.getCtx(), MTable.get(partner.getCtx(), "LCO_TaxIdType"), "LCO_TaxIdType_ID =? ", partner.get_TrxName()).setParameters(partner.get_ValueOfColumn(MColumn.getColumn_ID(MBPartner.Table_Name, "LCO_TaxIdType_ID"))).first();			
		String typePerson = taxIdType.get_Value("Name").toString();
		
		/**	DATOS DE LA FACTURA FISCAL	**/
		String name = partner.getName().toUpperCase();
		String taxID = typePerson + partner.getTaxID();
		int fiscalPrinterID = (int) docType.get_ValueOfColumn(MColumn.getColumn_ID(MDocType.Table_Name, "LVE_FiscalPrinter_ID")); 
		MLVEFiscalPrinter fiscalPrinter = new MLVEFiscalPrinter(invoice.getCtx(), fiscalPrinterID, invoice.get_TrxName()); 
		int port = fiscalPrinter.getLVE_FiscalPort();
		
		if(port == 0) 
			return "ERROR - Debe seleccionar Puerto de Impresora Fiscal";
		msg = dllPnP.PFabrepuerto(String.valueOf(port));
		if(!msg.equals("OK"))
			return "ERROR abriendo Puerto Impresora - " + msg;
		
		dllPnP.PFestatus("N");
		status = dllPnP.PFultimo();
		LVE_FiscalHour = status.split(",")[6].substring(0, 4);
		LVE_FiscalDate = formatDate(status.split(",")[5]);
		invoiceInfo = status;
		log.warning("Estado Impresora Fiscal: " + status);
		if(!status.split(",")[3].equals("00")) {
			msg = "ERROR - El estado de la Impresora Fiscal no es correcto: " + status.split(",")[3];
			log.warning(msg);
			return msg;
		}
		int LVE_FiscalDocNoStr = Integer.valueOf(status.split(",")[9]) + 1;
		LVE_FiscalDocNo = String.valueOf(LVE_FiscalDocNoStr);
		if(docType.getDocBaseType().equals(MDocType.DOCBASETYPE_ARInvoice)) {
			log.warning("Imprimiendo Factura Fiscal de " + partner.getName() + " - " + partner.getTaxID());
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
			log.warning("Imprimiendo Nota de Crédito Fiscal de " + partner.getName() + " - " + partner.getTaxID());
			MInvoice invoiceAffected = new MInvoice(invoice.getCtx(), invoice.get_ValueAsInt(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_InvoiceAffected")), invoice.get_TrxName());
			String date = (String) invoiceAffected.get_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalDate"));
			String hour = (String) invoiceAffected.get_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalHour"));
			
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
		
		/**	Obtener Número de Nota de Crédito Fiscal	**/
		if(docType.getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo)) {
			dllPnP.PFestatus("T");
			creditNoteInfo = dllPnP.PFultimo();
			LVE_FiscalDocNo = creditNoteInfo.split(",")[7];
		}
		
		msg = dllPnP.PFcierrapuerto();
		if(!msg.equals("OK"))
			return "ERROR agregando Total al Documento Fiscal - " + msg;
		return invoiceInfo;
	}

	private String formatDate(String fiscalDate) {
		String dateStr = fiscalDate.substring(4,6) + fiscalDate.substring(2,4) + fiscalDate.substring(0,2);
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
