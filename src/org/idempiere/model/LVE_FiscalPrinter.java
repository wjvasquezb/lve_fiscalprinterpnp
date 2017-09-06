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
 * @author Ing. Victor Su�rez - victor.suarez.is@gmail.com - 2016/12		*
 *****************************************************************************/
package org.idempiere.model;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCharge;
import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLocation;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPaymentTerm;
import org.compiere.model.MProduct;
import org.compiere.model.MTable;
import org.compiere.model.MTax;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.idempiere.component.IDLLPnP;

import com.sun.jna.Native;

/**
 * 
 * @author Ing. Victor Suarez - victor.suarez.is@gmail.com - 2016/12
 * 	Clase para Imprimir Factura Fiscal antes de completar Documentos.
 * @collaborator Ing. Jorge Colmenarez - jcolmenarez@frontuari.com - 2017/09 - Frontuari, C.A.
 * 	Support for Group product + price by distinct ASI, Fixed bug to print page footer info, Support for reverse documents
 *
 */

public class LVE_FiscalPrinter implements ModelValidator {
	
	public int ad_client_id;
	public static String invoiceInfo;
	public static String creditNoteInfo;
	public BufferedReader bReader;
	public static String LVE_FiscalDocNo = "";
	public static String msg = "";
	public static String status = "";
	public static String LVE_FiscalHour = "";
	public static String LVE_FiscalDate = "";
	public static int LVE_Zno = 0;

	public static IDLLPnP dllPnP;

	static CLogger log = CLogger.getCLogger(LVE_FiscalPrinter.class);
	
	public LVE_FiscalPrinter() {
		msg = "Plugin para Impresoras Fiscales PnP: LVE_FiscalPrinterPnP";
		System.out.println(msg);
		log.info(msg);
		if(Ini.isClient()) {
			String oS = System.getProperty("os.name");
			if(oS.toLowerCase().indexOf("win") >= 0) {
				msg = "Se carga la libreria pnpdll.dll";
				System.out.println(msg);
				log.info(msg);
				System.load("C:\\adempiere-client\\PnP\\pnpdll.dll");
				dllPnP = (IDLLPnP)Native.loadLibrary("pnpdll", IDLLPnP.class);
				System.out.println("Libreria Cargada: "+dllPnP);
			} else {
				msg = "No se puede cargar la libreria pnpdll.dll porque el Sistema Operativo es: " + oS;
				System.out.println(msg);
				log.warning(msg);
			}
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
				return "Debe agregar una Descripcion, un Producto o un Cargo.";
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
				return "Debe agregar una Descripcion, un Producto o un Cargo.";
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
			//	Print only when complete a document not reversal, added by Jorge Colmenarez, 2017-09-06 9:37
			if(invoice.getReversal_ID() == 0){
				MDocType docType = new MDocType(invoice.getCtx(), invoice.getC_DocType_ID(), invoice.get_TrxName());
				
				if(invoice.getGrandTotal().compareTo(Env.ZERO) == 0)
					return "El Documento " + docType.getName() + " No se puede Imprimir porque tiene valor 0";
				
				if(!(boolean)docType.get_ValueOfColumn(MColumn.getColumn_ID(MDocType.Table_Name, "IsUseFiscalPrinter")))
					return null;
				if(!Ini.isClient())
					return "No puede Completar Documento Fiscal desde el Cliente Web, debe usar el Cliente Swing";
				
				MBPartner partner = new MBPartner(invoice.getCtx(), invoice.getC_BPartner_ID(), invoice.get_TrxName());
				String text = "Numero de Documento: " + invoice.getDocumentNo();
//				if(partner.isManufacturer() && partner.getBPartner_Parent_ID() != 0) {
//					partner = new MBPartner(invoice.getCtx(), partner.getBPartner_Parent_ID(), invoice.get_TrxName());
//					invoice.set_ValueOfColumn("Bill_BPartner_ID", partner.getC_BPartner_ID());
//				}
				invoiceInfo = printInvoice(partner, invoice, docType, text);
				if(invoiceInfo.toUpperCase().contains("ERROR")) {
					msg = invoiceInfo;
					log.warning(msg);
					return msg;
				} else  {
					invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalDocNo"), LVE_FiscalDocNo);
					invoice.setDocumentNo(LVE_FiscalDocNo);;
					invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalDate"), LVE_FiscalDate);
					invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalHour"), LVE_FiscalHour);
					invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_ZNo"), LVE_Zno);
//					invoice.set_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "Bill_BPartner_ID"), partner.getC_BPartner_ID());
					invoice.setIsPrinted(true);
					invoice.saveEx();
					msg = "Documento Fiscal Nro: " + LVE_FiscalDocNo + " - Impreso correctamente. - " + invoiceInfo;
					log.warning(msg);
				}
			}
		}
		return null;
	}

	public static String printInvoice(MBPartner partner, MInvoice invoice, MDocType docType, String text) {		
		/**	DATOS DE LA FACTURA FISCAL	**/
		String name = cleanName(partner.getName().toUpperCase());
		String taxID = partner.getTaxID();
		if(!taxID.substring(0, 1).matches("[VEJG]")) {
			if(MColumn.getColumn_ID(MBPartner.Table_Name, "LCO_TaxIdType_ID") != 0) {
				if(partner.get_ValueOfColumn(MColumn.getColumn_ID(MBPartner.Table_Name, "LCO_TaxIdType_ID")) != null) {
					PO taxIdType = new Query(partner.getCtx(), MTable.get(partner.getCtx(), "LCO_TaxIdType"), "LCO_TaxIdType_ID =? ", partner.get_TrxName()).setParameters(partner.get_ValueOfColumn(MColumn.getColumn_ID(MBPartner.Table_Name, "LCO_TaxIdType_ID"))).first();
					String typePerson = taxIdType.get_Value("Name").toString();
					taxID = typePerson + taxID;
				}
			}
		}
		int fiscalPrinterID = (int) docType.get_ValueOfColumn(MColumn.getColumn_ID(MDocType.Table_Name, "LVE_FiscalPrinter_ID")); 
		MLVEFiscalPrinter fiscalPrinter = new MLVEFiscalPrinter(invoice.getCtx(), fiscalPrinterID, invoice.get_TrxName()); 
		String port = fiscalPrinter.getLVE_FiscalPort();
		
		if(port == "" || port == null) 
			return "ERROR - Debe seleccionar Puerto de Impresora Fiscal";
		msg = dllPnP.PFabrepuerto(String.valueOf(port));
		if(!msg.equals("OK"))
			return "ERROR abriendo Puerto Impresora - " + msg;
		
		dllPnP.PFestatus("N");
		status = dllPnP.PFultimo();
		LVE_FiscalHour = status.split(",")[6].substring(0, 4);
		LVE_FiscalDate = formatDate(status.split(",")[5]);
		LVE_Zno = Integer.valueOf(status.split(",")[11]) + 1;
		invoiceInfo = status;
		log.warning("Estado Impresora Fiscal: " + status);
		if(!status.split(",")[3].equals("00")) {
			msg = "ERROR - El estado de la Impresora Fiscal no es correcto: " + status.split(",")[3];
			log.warning(msg);
			return msg;
		}
		int LVE_FiscalDocNoStr = Integer.valueOf(status.split(",")[9]) + 1;
		LVE_FiscalDocNo = String.format("%08d",LVE_FiscalDocNoStr);
		

		//	Variables for group product+price info
		String OldDescPrice = "-1";
		String OldDescription = "-1";
		String OldQty = "-1";
		String OldPrice = "-1";
		String OldIVA = "-1";
		BigDecimal cumulatedQty = BigDecimal.ZERO;
		
		if(docType.getDocBaseType().equals(MDocType.DOCBASETYPE_ARInvoice)) {
			log.warning("Imprimiendo Factura Fiscal de " + partner.getName() + " - " + partner.getTaxID());
			msg = dllPnP.PFabrefiscal(name, taxID);
			if(!msg.equals("OK"))
				return "ERROR abriendo Factura Fiscal - " + msg;
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
				//	Support for concatenate description into product or charge if it's not null
				if(invoiceLine.getDescription()!=null){
					description = description+" "+invoiceLine.getDescription();
				}
				String price = formatPrice(invoiceLine.getPriceEntered());
				MTax tax = new MTax(invoiceLine.getCtx(), invoiceLine.getC_Tax_ID(), invoice.get_TrxName());
				String iva = formatIVA(tax.getRate());
				//	Added by Jorge Colmenarez 2017-09-03 21:46
				//	Group product when use attribute set instance and in the invoice have a product with same price but distinct ASI
				String CurrentDescPrice=(description+"_"+price).trim();
				if(!OldDescPrice.trim().equals(CurrentDescPrice.trim()) && !OldDescPrice.equals("-1")){
					String qty = formatQty(cumulatedQty);
					//	Send Info to Fiscal Printer
					msg = dllPnP.PFrenglon(OldDescription, qty, OldPrice, OldIVA);
					if(!msg.equals("OK"))
						return "ERROR agregando Lineas - " + msg;
					//	Reset cumulated qty
					cumulatedQty = BigDecimal.ZERO;
				}
				//	Cumulated Qty Entered from Invoice Line
				cumulatedQty = cumulatedQty.add(invoiceLine.getQtyEntered());
				OldDescPrice=(description+"_"+price).trim();
				OldDescription = description;
				OldPrice = price;
				OldQty = formatQty(cumulatedQty);
				OldIVA = iva;
				//	End Jorge Colmenarez
			}
		} else if (docType.getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo)){
			/**	Obtener Numero de Nota de Credito Fiscal	**/
			dllPnP.PFestatus("T");
			creditNoteInfo = dllPnP.PFultimo();
			if(creditNoteInfo.split(",").length <= 7)
				creditNoteInfo = "0000,0000,45,00,45,170118,151119,00000000";
			log.warning("Datos de la Nota de Credito: " + creditNoteInfo);
			int LVE_creditNoteNoStr = Integer.valueOf(creditNoteInfo.split(",")[7]) + 1;
			LVE_FiscalDocNo = String.format("%08d", LVE_creditNoteNoStr);
			
			log.warning("Imprimiendo Nota de Credito Fiscal # " + LVE_FiscalDocNo + " de " + partner.getName() + " - " + partner.getTaxID());
			MInvoice invoiceAffected = new MInvoice(invoice.getCtx(), (int)invoice.get_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_invoiceAffected_ID")), invoice.get_TrxName());
			String date = (String) invoiceAffected.get_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalDate"));
			String hour = (String) invoiceAffected.get_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_FiscalHour"));
			//	Support for search DocNo invoice affected from LVE_FiscalDocNo or DocumentNo 
			String invoiceAffectedNo;
			if(invoiceAffected.get_ValueAsString("LVE_FiscalDocNo")!="")
				invoiceAffectedNo = invoiceAffected.get_ValueAsString("LVE_FiscalDocNo");
			else
				invoiceAffectedNo = invoiceAffected.getDocumentNo();
			//	End Support			
			msg = dllPnP.PFDevolucion(name, taxID, invoiceAffectedNo, fiscalPrinter.getLVE_SerialFiscal(), date, hour);
			if(!msg.equals("OK"))
				return "ERROR abriendo Factura Fiscal - " + msg;
//			msg = dllPnP.PFTfiscal(text);
//			if(!msg.equals("OK"))
//				return "ERROR agregando Ficha a la Factura Fiscal - " + msg;
			/**	DATOS DE LA LINEA DE LA NOTA DE CREDITO	**/
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
				//	Support for concatenate description into product or charge if it's not null
				if(invoiceLine.getDescription()!=null){
					description = description+" "+invoiceLine.getDescription();
				}
				MTax tax = new MTax(invoiceLine.getCtx(), invoiceLine.getC_Tax_ID(), invoice.get_TrxName());
				String price = formatPrice(invoiceLine.getPriceEntered());
				String iva = formatIVA(tax.getRate());
				//	Added by Jorge Colmenarez 2017-09-03 21:46
				//	Group product when use attribute set instance and in the invoice have a product with same price but distinct ASI
				String CurrentDescPrice=(description+"_"+price).trim();
				if(!OldDescPrice.trim().equals(CurrentDescPrice.trim()) && !OldDescPrice.equals("-1")){
					String qty = formatQty(cumulatedQty);
					//	Send Info to Fiscal Printer
					msg = dllPnP.PFrenglon(OldDescription, qty, OldPrice, OldIVA);
					if(!msg.equals("OK"))
						return "ERROR agregando Lineas - " + msg;
					//	Reset cumulated qty
					cumulatedQty = BigDecimal.ZERO;
				}
				//	Cumulated Qty Entered from Invoice Line
				cumulatedQty = cumulatedQty.add(invoiceLine.getQtyEntered());
				OldDescPrice=(description+"_"+price).trim();
				OldDescription = description;
				OldPrice = price;
				OldQty = formatQty(cumulatedQty);
				OldIVA = iva;
				//	End Jorge Colmenarez
			}
		}
		
		//	For send to printer the last line
		//	Send Info to Fiscal Printer
		msg = dllPnP.PFrenglon(OldDescription, OldQty, OldPrice, OldIVA);
		if(!msg.equals("OK"))
			return "ERROR agregando Lineas - " + msg;
		
		
		msg = dllPnP.PFparcial();
		if(!msg.equals("OK"))
			return "ERROR generando cierre parcial del Documento Fiscal - " + msg;
		msg = dllPnP.PFTfiscal(getText(partner, invoice, docType, 1));
		if(!msg.equals("OK"))
			return "ERROR agregando Linea 1 al pie del Documento Fiscal - " + msg;
		msg = dllPnP.PFTfiscal(getText(partner, invoice, docType, 2));
		if(!msg.equals("OK"))
			return "ERROR agregando Linea 2 al pie del Documento Fiscal - " + msg;
		msg = dllPnP.PFTfiscal(getText(partner, invoice, docType, 3));
		if(!msg.equals("OK"))
			return "ERROR agregando Linea 3 al pie del Documento Fiscal - " + msg;
		msg = dllPnP.PFTfiscal(getText(partner, invoice, docType, 4));
		if(!msg.equals("OK"))
			return "ERROR agregando Linea 4 al pie del Documento Fiscal - " + msg;
		msg = dllPnP.PFTfiscal(getText(partner, invoice, docType, 5));
		if(!msg.equals("OK"))
			return "ERROR agregando Linea 5 al pie del Documento Fiscal - " + msg;
		msg = dllPnP.PFTfiscal(getText(partner, invoice, docType, 6));
		if(!msg.equals("OK"))
			return "ERROR agregando Linea 6 al pie del Documento Fiscal - " + msg;
		msg = dllPnP.PFtotal();
		if(!msg.equals("OK"))
			return "ERROR agregando Total al Documento Fiscal - " + msg;
		msg = dllPnP.PFcierrapuerto();
		if(!msg.equals("OK"))
			return "ERROR cerrando Puerto Impresora - " + msg;
		return invoiceInfo;
	}
	
	private static String getText(MBPartner partner, MInvoice invoice, MDocType docType, int line) {
		String text;
		MBPartnerLocation bpLocation = new MBPartnerLocation(invoice.getCtx(), invoice.getC_BPartner_Location_ID(), invoice.get_TrxName());
		MLocation location = new MLocation(invoice.getCtx(), bpLocation.getC_Location_ID(), invoice.get_TrxName());
		String docNo = "TR:"+LVE_FiscalDocNo;
		MUser user = new MUser(invoice.getCtx(), invoice.getSalesRep_ID(), invoice.get_TrxName());
		String salesRep = "Vd:" + user.getName();
		String payTerm = "CP:CONTADO";
		if(invoice.getC_PaymentTerm_ID() > 0) {
			MPaymentTerm paymentTerm = new MPaymentTerm(invoice.getCtx(), invoice.getC_PaymentTerm_ID(), invoice.get_TrxName());
			payTerm = "CP:"+paymentTerm.getName();
		}
		String address = "DIR: " + location.getAddress1() + ", " + (location.getAddress2() == null ? "" : location.getAddress2()+", ") + "\n"
					+ 	(location.getAddress3() == null ? "" : location.getAddress3()+", ") + (location.getAddress4() == null ? "" : location.getAddress4()+", ");
		String city = location.getCity() + ((location.getRegionName()==null || location.getRegionName()=="") ? "" : " - "+location.getRegionName());
		String phone = "TLF: " + (bpLocation.getPhone()==null ? "" : bpLocation.getPhone()) + (bpLocation.getPhone2()==null ? "" : " - " + bpLocation.getPhone2()); 
		String invoiceAffected = "";
		if(docType.getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo)){
			MInvoice affected = new MInvoice(invoice.getCtx(), (int)invoice.get_ValueOfColumn(MColumn.getColumn_ID(MInvoice.Table_Name, "LVE_invoiceAffected_ID")), invoice.get_TrxName());
			invoiceAffected = "Afecta Fc: " + affected.getDocumentNo()+" ";
		}
		String bpCode = "Cliente: " + partner.getValue();
		String name = partner.getName();
		if(line == 1){
			text = docNo + " " + salesRep + " " + payTerm;
		}
		else if(line == 2){
			text = address.substring(0, address.length()-2);
		}
		else if(line == 3){
			text = city;
		}
		else if(line == 4){
			text = phone;
		}
		else if(line == 5){
			text = (invoiceAffected!="" ? invoiceAffected : "") + bpCode;
		}
		else{
			text = name;
		}
		/*
		 * Se comenta para imprimir el texto por cortes
		String text = docNo + " " + salesRep + " " + payTerm + "\n"
				+ address + "\n"
				+ invoiceAffected + " " + bpCode + "\n"
				+ name;
		*/
		log.log(Level.INFO, text);
		return text;
	}

	private static String formatDate(String fiscalDate) {
		String dateStr = fiscalDate.substring(4,6) + fiscalDate.substring(2,4) + fiscalDate.substring(0,2);
		return dateStr;
	}

	public static String formatQty(BigDecimal monto) {
		String value="";		
		DecimalFormat decf = new DecimalFormat("######.000");
		value = decf.format(monto);
		value = value.replace(",", ".");
		return value;
	}
	
	public static String formatIVA(BigDecimal monto) {
		String value="";		
		DecimalFormat decf = new DecimalFormat("######.00");
		value = decf.format(monto);
		value = value.replace(",", "");
		return value;
	}
	
	public static String formatPrice(BigDecimal monto) {
		String value="";		
		DecimalFormat decf = new DecimalFormat("######.00");
		value = decf.format(monto);
		value = value.replace(",", ".");
		return value;
	}
	
	private static String cleanName(String value){	
		String name="";
		int size=value.length();
		
		 for (int i = 0; i < size; i++){ 
			 if ((value.charAt(i)==',') || (value.charAt(i)=='.')){
				 name+= ' ';
			 }else{
				 name+= value.charAt(i);
			 }		  		 
		 }	
	//	Support for Truncate string to 80 characters
	//	modified by Jorge Colmenarez, 2017-09-05 16:00, jcolmenarez@frontuari.com, Frontuari, C.A.
	return name.trim().substring(0, 80);
	}

}
