/**
 * 2015-04 
 * @author Ing. Victor Suárez - victor.suarez.is@gmail.com - vsuarez@as-solucionesintegrales.com.ve
 */

String status = "TRUE";

 if(status.trim().equals("TRUE")) {
     System.out.println("Impresora Vmax Encendida, y Conectada");
  } else {
     System.out.println("Impresora Vmax Desconectada o Apagada - " + status.trim());
  }

return status.trim();