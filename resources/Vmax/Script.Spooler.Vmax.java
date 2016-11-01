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

String ruta = "C:/Elepos/Spooler/";
//String ruta ="";

File spooler = new File(ruta+"files/selectrapos.txt");
Writer	salida = new BufferedWriter(new FileWriter(spooler));

//CASO DEVOLUCION
if("1".equals(ticket.getTicketType().toString())){
	DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
	DateFormat hora = new SimpleDateFormat("HHmm");
	numero_factura = javax.swing.JOptionPane.showInputDialog(null, "Introduzca el código de la Factura:", "Nota de Crédito", JOptionPane.WARNING_MESSAGE);
	serial_impresora = javax.swing.JOptionPane.showInputDialog(null, "Introduzca el serial de la Impresora:", "Nota de Crédito", JOptionPane.WARNING_MESSAGE);
	razon_social = ticket.getCustomer().getName().toString().toUpperCase();
	rif_ci = ticket.getCustomer().getTaxid().toString();
	fecha_factura = fecha.format(ticket.getDate()).toString(); 
	hora_factura = hora.format(ticket.getDate()).toString();	
	if ( numero_factura != null && serial_impresora != null)
	{ 
       	 // the user pressed OK 
		salida.write("<ABRIR_DEVOLUCION," + razon_social + "," + rif_ci + "," + numero_factura.toUpperCase() + "," + serial_impresora.toUpperCase() + "," + fecha_factura + "," + hora_factura + ">" + "\n");
		ticket.setFiscalNumber(numero_factura.toUpperCase());
		ticket.setFiscalSerial(serial_impresora.toUpperCase());
	}

	NumberFormat df = new DecimalFormat("#0.00"); 

	lineas = ticket.getLinesCount();

	for (int i = 0; i < lineas; i++) 
	{
		producto_iva="";
		producto_precio="";
		producto = ticket.getLine(i).getProductName().toString().toUpperCase();
		if (producto.length() > 70)
		{
			producto = producto.substring(0,70);
		}
		producto = producto.replace(",",".");

		producto_cantidad = Formats.INT.formatValue(ticket.getLine(i).getMultiply()*(-1000));
		producto_cantidad = producto_cantidad.toString().replace(".","");
		if(ticket.getLine(i).getTaxRate().toString().equals("0.0")){
			producto_iva = "0";
		}
		if (ticket.getLine(i).getTaxRate().toString().equals("0.12")){
			producto_iva = "1";
		}
		if (ticket.getLine(i).getTaxRate().toString().equals("0.08")){
			producto_iva = "2";
		}	
		producto_precio = ticket.getLine(i).getPrice()*100;
		sP=producto_precio.toString();
		if (sP.contains(","))
			sP=sP.substring(0,sP.indexOf(","));
		else if (sP.contains("."))
			sP=sP.substring(0,sP.indexOf("."));
		//producto_precio = producto_precio.toString().replace(".0","");
		salida.write("<ITEM_CF," + producto + "," + producto_cantidad + "," + sP + "," + producto_iva + "," + "0" + "," + "0" + ">" + "\n");
	}

	salida.write("<" + "SUBTOTAL_CF" + ">" + "\n");
	
	salida.write("<" + "TEXTO_CF" + "," + "--------------------" + "," + "0" + ">" + "\n");

	salida.write("<" + "TEXTO_CF" + "," + "AUTORIZADO POR: " + ticket.getUser().getName().toString().toUpperCase()  + "," + "0" + ">" + "\n");
		
	salida.write("<" + "CERRAR_CF" + ">" + "\n");
}

//CASO VENTA DIRECTA
if ("0".equals(String.valueOf(ticket.getTicketType())))
{
	if(ticket.printCustomer().equals("")){
		salida.write("<ABRIR_CF," + "GENERAL,00000000>\n");	
	}
	else{
		salida.write("<ABRIR_CF," + ticket.getCustomer().getName().toString().toUpperCase() + "," + ticket.getCustomer().getTaxid().toString() + ">" + "\n");
	}

	NumberFormat df = new DecimalFormat("#0.00"); 

	lineas = ticket.getLinesCount();

	for (int i = 0; i < lineas; i++) 
	{
		producto_iva="";
		producto_precio="";
		producto = ticket.getLine(i).getProductName().toString().toUpperCase();
		if (producto.length() > 70)
		{
			producto = producto.substring(0,70);
		}
		producto = producto.replace(",",".");
		
		producto_cantidad = Formats.INT.formatValue(ticket.getLine(i).getMultiply()*1000);
		producto_cantidad = producto_cantidad.toString().replace(".","");
		if(ticket.getLine(i).getTaxRate().toString().equals("0.0")){
			producto_iva = "0";
		}
		if (ticket.getLine(i).getTaxRate().toString().equals("0.12")){
			producto_iva = "1";
		}
		if (ticket.getLine(i).getTaxRate().toString().equals("0.08")){
			producto_iva = "2";
		}	
		producto_precio = ticket.getLine(i).getPrice()*100;
		sP=producto_precio.toString();
		if (sP.contains(","))
			sP=sP.substring(0,sP.indexOf(","));
		else if (sP.contains("."))
			sP=sP.substring(0,sP.indexOf("."));
		//producto_precio = producto_precio.toString().replace(".0","");
		salida.write("<ITEM_CF," + producto + "," + producto_cantidad + "," + sP + "," + producto_iva + "," + "0" + "," + "0" + ">" + "\n");
	}

	double efectivo=0.0;
	double deb=0.0;
	double tc=0.0;
	double paper=0.0;
	for (int j = 0; j < ticket.payments.size(); j++)
	//foreach (PaymentInfo p : ticket.payments)
	{
		PaymentInfo p = ticket.payments.get(j) ;
    	   	if ("cash".equals(p.getName() ))
		{
  	              	efectivo=efectivo+p.getPaid();
   	     	}   	     	
   	  	else if ("magcard".equals(p.getName() ))
		{
    	            	tc=tc+p.getTotal();
       	     	}
        	else if ("paperin".equals(p.getName() ))
		{
			paper=paper+p.getTotal();
        	}
	}
	salida.write("<" + "SUBTOTAL_CF" + ">" + "\n");
	if (efectivo>0){
		efectivo=efectivo*100; 
		sE=efectivo.toString();
		if (sE.contains(","))
			sE=sE.substring(0,sE.indexOf(","));
		else if (sE.contains("."))
			sE=sE.substring(0,sE.indexOf("."));
		salida.write("<" + "PAGO_CF" + "," + sE + ",EFECTIVO>\n");
	}
	
	if (tc>0){
		tc=tc*100;
		stc=tc.toString();
		if (stc.contains(","))
			stc=stc.substring(0,stc.indexOf(","));
		else if (stc.contains("."))
			stc=stc.substring(0,stc.indexOf("."));
		salida.write("<" + "PAGO_CF" + "," + stc + ",TARJETA>\n");
	}
	if (paper>0){
		paper=paper*100;
		spaper=paper.toString();
		if (spaper.contains(","))
			spaper=spaper.substring(0,spaper.indexOf(","));
		else if (spaper.contains("."))
			spaper=spaper.substring(0,spaper.indexOf("."));
		salida.write("<" + "PAGO_CF" + "," + spaper + ",CESTATICKET>\n");
	}
	salida.write("<" + "TEXTO_CF" + "," + "--------------------" + "," + "0" + ">" + "\n");

	salida.write("<" + "TEXTO_CF" + "," + "CAJERO: " + ticket.getUser().getName().toString().toUpperCase()  + "," + "0" + ">" + "\n");

	recibo=ticket.printId().toString(); 
	
	salida.write("<" + "TEXTO_CF" + "," + "RECIBO: " + recibo  + "," + "0" + ">" + "\n");
	
	salida.write("<" + "CERRAR_CF" + ">" + "\n");
}

salida.write("<" + "GAVETA_A" + ">" + "\n");

salida.close();

try {
	String spoolerVmax = ruta+"epsSpoolerVmax.exe";    
	Runtime.getRuntime().exec(spoolerVmax,null,new File(ruta)).waitFor();
  } catch (IOException eF) {
   	 // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.
    	eF.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al Imprimir Factura Fiscal \n" + ex);
        return "FALSE";
  } catch (InterruptedException ex) {
        JOptionPane.showMessageDialog(null, "Error al Imprimir Factura Fiscal \n" + ex);
        return "FALSE";
  }

return "TRUE";