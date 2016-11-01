import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

String status = "FALSE";
try {
    String files = "C:/Elepos/Spooler/files/";
    File spooler = new File(files + "selectrapos.txt");
    Writer salida = new BufferedWriter(new FileWriter(spooler));
    salida.write("<" + "REPORTE_X" + ">");  
    salida.close();
    
    // Se manda a imprimir Spooler
    String spoolerVmax = "C:\\Elepos\\Spooler\\epsSpoolerVmax.exe";    
    Runtime.getRuntime().exec(spoolerVmax,null,new File("C:/Elepos/Spooler/")).waitFor();
    
    // Leemos si resulto exitoso
    File file = new File(files + "OUT_selectrapos.txt");
    if(file.exists()) {
        FileReader fileR = new FileReader(file);
        BufferedReader in = new BufferedReader(fileR);
        String line = "";
        line = in.readLine();  
        if(line.contains("OK"))
            status = "TRUE";
        else
            status = line;
        in.close();
        
        if(file.delete())
            System.out.println("OUT_selectrapos.txt eliminado exitosamente");
        else
            System.out.println("No se pudo eliminar archivo OUT_selectrapos.txt");
    }
    else {
        System.out.println("Archivo OUT_selectrapos.txt no existe");
    }
    } catch (IOException ex) {
     // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.
  ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error al Imprimir Reporte X \n" + ex);
    } catch (InterruptedException ex) {
        JOptionPane.showMessageDialog(null, "Error al Imprimir Reporte X \n" + ex);
    }
return status;  
}