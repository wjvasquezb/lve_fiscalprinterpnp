import java.io.*;
import javax.swing.JOptionPane;

String ruta = "C:/IntTFHKA/";

try {
  //String spoolerBixolon = ruta+"cmd.bat"; 
  //String spoolerBixolon = ruta+"IntTFHKA.exe SendFileCmd(C:/IntTFHKA/selectrapos.txt)";   
  String spoolerBixolon = ruta+"IntTFHKA.exe SendCmd(I0Z)";   
  Runtime.getRuntime().exec(spoolerBixolon,null,new File(ruta)).waitFor();
 } catch (IOException eF) {
    // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.
    eF.printStackTrace();
    JOptionPane.showMessageDialog(null, "Error al Imprimir Reporte Z \n" + eF);
 } catch (InterruptedException ex) {
      JOptionPane.showMessageDialog(null, "Error al Imprimir Reporte Z \n" + ex);
 }

FileReader file = new FileReader(ruta+"Status_Error.txt");
BufferedReader in = new BufferedReader(file);
String line = in.readLine();  
Object result = line.substring(0,5);
in.close();

return result.trim();