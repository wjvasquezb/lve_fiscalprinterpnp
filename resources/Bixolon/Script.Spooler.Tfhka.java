import java.io.*;
import java.text.*;
import java.lang.String;
import java.lang.Object;
import com.openbravo.format.Formats;
import com.openbravo.pos.ticket.*;
import com.openbravo.pos.customers.*;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.payment.PaymentInfo;
import java.util.Properties;

public String strpad(String dato, String cformat, int espacio, String pad_type) {
	tam = dato.length();
        if (tam <= espacio) {
		if (dato.equals(" ")) {
	    	dato = cformat;
		}
		temp = dato;
		vcs = espacio - tam;
		if(pad_type.equals("STR_PAD_RIGHT")) {
    		for (int i = 0; i < vcs; i++) {
        		temp += cformat;
			}
			dato = temp;
		}
		if(pad_type.equals("STR_PAD_LEFT")) {
			aux = dato;
    		for (int i = 0; i < vcs; i++) {
				temp = cformat + aux;
        	    aux = temp;
			}
			dato = temp;
		}		
        }
        return dato;
}

// Se agregan al Spooler los pagos de la factura
public void addPayments(int espacio) {
	double efectivo=0.0;
	double deb=0.0;
	double tc=0.0;
	double paper=0.0;
	double dif=0.0;
	double total_pago=0.0;
	double total_ticket=0.0;
	for (int j = 0; j < ticket.payments.size(); j++) {
	//foreach (PaymentInfo p : ticket.payments)
		PaymentInfo p = ticket.payments.get(j) ;
	   	if ("cash".equals(p.getName() )) {
          	efectivo=efectivo+p.getPaid();
     	}   	     	
   	  	else if ("magcard".equals(p.getName() )) {
        	tc=tc+p.getTotal();
     	}
    	else if ("paperin".equals(p.getName() )) {
			paper=paper+p.getTotal();
    	}
	}

	if (ticket.getTotal() <= efectivo || ticket.getTotal() <= tc || ticket.getTotal() <= paper) {                       
        if (ticket.getTotal() <= efectivo) {
            salida.write("101\n");
        }
		else if (ticket.getTotal() <= tc) {
            salida.write("109\n");
        }
		else if (ticket.getTotal() <= paper) {
		    salida.write("113\n");
        }                        
    }	
	else {             
	    if (efectivo > 0) {
		    efect = df1.format(efectivo).toString();
            efect = efect.replace(".","");
		    efect = strpad(efect, "0", espacio, "STR_PAD_LEFT");
            salida.write("201" + efect.replace(",","") + "\n");
        }
        if (tc > 0) {
            tarjeta = df1.format(tc).toString();
            tarjeta = tarjeta.replace(".","");
		    tarjeta = strpad(tarjeta, "0", espacio, "STR_PAD_LEFT");
            salida.write("209" + tarjeta.replace(",","") + "\n");
        }
        if (paper > 0) {
            cestaticket = df1.format(paper).toString();
	        cestaticket = cestaticket.replace(".","");
		    cestaticket = strpad(cestaticket, "0", espacio, "STR_PAD_LEFT");
            salida.write("213" + cestaticket.replace(",","") + "\n");
        }
		total_pago = efectivo + tc + paper;
		total_ticket = ticket.getTotal();
		if (total_pago < total_ticket) {
            dif = total_ticket - total_pago;
		    diferencia = df1.format(dif).toString();
            diferencia = diferencia.replace(".","");
		    diferencia = strpad(diferencia, "0", espacio, "STR_PAD_LEFT");
            salida.write("201" + diferencia.replace(",","") + "\n");
        }                        
    }
}

public void executeSpooler(String spoolerBixolon) {
try {
	//String spoolerBixolon = ruta+"cmd.bat";
	Runtime.getRuntime().exec(spoolerBixolon,null,new File(ruta)).waitFor();
   } catch (IOException eF)  {
   	 // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.
    	eF.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al ejecutar comando: \n" + spoolerBixolon + "\n" + eF, "ERROR", JOptionPane.ERROR_MESSAGE);
  } catch (InterruptedException ex) {
        JOptionPane.showMessageDialog(null, "Error al ejecutar comando: \n" + spoolerBixolon + "\n" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
  }
}

String ruta = "C:/IntTFHKA/";
File spooler = new File(ruta+"iDempiereSpooler.txt");
Writer	salida = null;
try {
    salida = new BufferedWriter(new FileWriter(spooler));
} catch (IOException ex) {
    JOptionPane.showMessageDialog(null, "Error al leer archivo: " + spooler + "\n" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
}

salida.write("i01NOMBRE/RAZON SOCIAL: " + BPartner.getName().toUpperCase() + "\n");
salida.write("i02CI/RIF: " + BPartner.getTaxID().toUpperCase() + "\n");

controlNumber=Invoice.get_Value("LVE_controlNumber");	
salida.write("@CAJERO: " + SalesRep.toUpperCase() + " - Nro Control: " + controlNumber + "\n");

NumberFormat df1 = new DecimalFormat("#0.00"); 
NumberFormat df2 = new DecimalFormat("#0.000"); 

for (MInvoiceLine invoiceLine : invoiceLines) {
	producto_iva="";
	producto_precio="";
	producto="";
	MTax tax = new MTax(getCtx, invoiceLine.getC_Tax_ID(), get_TrxName);
	
	if(tax.getRate().compareTo(new BigDecimal(0.0))) {
		producto_iva = " ";
	}
	if (tax.getRate().compareTo(new BigDecimal(12))) {
		producto_iva = "!";getPriceActual()
	}
	if (tax.getRate().compareTo(new BigDecimal)(8))) {
		producto_iva = "\"";
	}		

	precio = df1.format(invoiceLine.getPriceActual()).toString();
	//String precios[] = precio.split(".");
	producto_precio = precio.replace(".","");
	producto_precio = strpad(producto_precio.replace(",",""), "0", 10, "STR_PAD_LEFT");
	//salida.write(producto_precio + "\n");	

	cantidad = df2.format(invoiceLine.getQtyInvoiced()).toString();
	//String cantidades[] = cantidad.split(".");
	producto_cantidad = cantidad.replace(".","");
	producto_cantidad = strpad(producto_cantidad.replace(",",""), "0", 8, "STR_PAD_LEFT");
	//salida.write(producto_cantidad + "\n");
	
	if (invoiceLine.getM_Product_ID() != 0) {
	MProduct product = new MProduct(getCtx(), invoiceLine.getM_Product_ID(), get_TrxName());
	producto = product.getName().toUpperCase();
	} else 
		producto = invoiceLine.getDescription();
	if (producto.length() > 40) {
		producto = producto.substring(0,40);
	}
	
	salida.write(producto_iva + producto_precio.replace(",","") + producto_cantidad.replace(",","") + producto + "\n" );
}

salida.write("3\n");

// Se agregan al Spooler los pagos de la factura
//addPayments(13);

salida.close();

String spoolerBixolon = ruta+"IntTFHKA.exe SendFileCmd(C:/IntTFHKA/iDempiereSpooler.txt)";
executeSpooler(spoolerBixolon);

boolean isCash = false;
String change = "";
PaymentInfo p = ticket.payments.getFirst();
File file = new File(ruta+"Status_Error.txt");
FileReader fileR = new FileReader(file);
BufferedReader in = new BufferedReader(fileR);
String line = in.readLine();  
in.close();
System.out.println("Respuesta de la Impresora Fiscal Bixolon (Retorno, Status, Error): " + line);

if(!"".equals(line) && line != null) {
	String error = line.split("\\t")[2];
	if("0".equals(error.trim())) {
		ready = "TRUE";
	}
	else {
		ready = "FALSE";
		System.out.println("La Impresora Bixolon Presenta error: " + error);
		JOptionPane.showMessageDialog(null, "La Impresora Bixolon Presenta error: " + error + "\nSi se apagó, enciendala y espere unos segundos", "ERROR", JOptionPane.ERROR_MESSAGE);
		if(file.exists()) {
			file.delete();
			System.out.println("263");
		}
		in = null;
		line = null;
		System.out.println("267");
		String conected = "FALSE";
		String status = "";
		error = "";
		spoolerBixolon = ruta + "IntTFHKA.exe ReadFpStatus()";
		while("FALSE".equals(conected)) {
		executeSpooler(spoolerBixolon);
		if(file.exists()) {
			in = new BufferedReader(fileR);
			line = in.readLine();
			in.close();
			System.out.println("277");
			conected = line.split("\\t")[0];
			status = line.split("\\t")[1];
			error = line.split("\\t")[2];
			if("FALSE".equals(conected))
				JOptionPane.showMessageDialog(null, "Aún no ha iniciado la Impresora, Espere...", "PRECAUCIÓN", JOptionPane.WARNING_MESSAGE);
		} else {
			System.out.println("Error al leer Estado de la Impresora");
			JOptionPane.showMessageDialog(null, "Error al leer Estado de la Impresora", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	if("4".equals(status) || "0".equals(error)) {
		System.out.println("Documento Fiscal Impreso Correctamente");
		ready = "TRUE";
	} else {
		System.out.println("Documento Fiscal Impreso Parcialmente");
		JOptionPane.showMessageDialog(null, "Documento Fiscal Impreso Parcialmente\nA continuación se completará la impresión...", "PRECAUCIÓN", JOptionPane.WARNING_MESSAGE);
		salida.write("");
		salida.close();
		if ("1".equals(String.valueOf(ticket.getTicketType())))
			addPayments(12);
		if ("0".equals(String.valueOf(ticket.getTicketType())))
			addPayments(13);
		spoolerBixolon = ruta+"IntTFHKA.exe SendFileCmd(C:/IntTFHKA/iDempiereSpooler.txt)";
		executeSpooler(spoolerBixolon);
		if(file.exists()) {
			in = new BufferedReader(fileR);
			line = in.readLine();
			in.close();
			conected = line.split("\\t")[0];
			status = line.split("\\t")[1];
			error = line.split("\\t")[2];
			if("TRUE".equals(conected) && "0".equals(status) && "0".equals(error)) {
				System.out.println("Documento Fiscal Impreso Correctamente");
				ready = "TRUE";
			} else {
				System.out.println("No se imprimió el Documento Fiscal");
				System.out.println("Retorno: " + conected + " - Status: " + status + " - Error: " + error);
				JOptionPane.showMessageDialog(null, "No se imprimió el Documento Fiscal\n" +
					"Retorno: " + conected + " - Status: " + status + " - Error: " + error, "ERROR", JOptionPane.ERROR_MESSAGE);
				ready = "FALSE";
			}
		}
	}
	}
}

if ("cash".equals(p.getName())) {
    isCash = true;
    change = p.printChange();
}
if(isCash && ready == "TRUE") {
    JOptionPane.showMessageDialog(null, "CAMBIO: " + change, "CAMBIO", JOptionPane.INFORMATION_MESSAGE);
}

return ready;

//Thread.sleep(40000);
//File spooler = new File(ruta+"selectrapos.txt");
//Writer	salida = new BufferedWriter(new FileWriter(spooler));
//salida.write(0);
//salida.close();