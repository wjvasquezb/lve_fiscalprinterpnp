import java.io.*;
import javax.swing.JOptionPane;

/**
 * 2015-04 
 * @author Ing. Victor Suárez - victor.suarez.is@gmail.com - vsuarez@as-solucionesintegrales.com.ve
 */

String conected = "TRUE";
String ready = "TRUE";
String status = "4";
String error = "0";
String path = "C:\\IntTFHKA\\";
String spoolerBixolon = "";
String line = "";
FileReader file = null;
BufferedReader in = null;

public void checkStatus() {
    // Check Status
    System.out.println("--------------------    VERIFICANDO STATUS DE LA IMPRESORA FISCAL BIXOLON    --------------------");
    spoolerBixolon = path+"IntTFHKA.exe ReadFpStatus()";
    System.out.println("Se empieza a ejecutar el comando 'IntTFHKA.exe ReadFpStatus()'");
    // new ProcessBuilder(spoolerBixolon).start().waitFor();
    Runtime.getRuntime().exec(spoolerBixolon).waitFor();
    System.out.println("Se termina de ejecutar el comando 'IntTFHKA.exe ReadFpStatus()'");
    // Después de que se termina de ejecutar comando, se lee el archivo.
    file = new FileReader(path+"Status_Error.txt");
    in = new BufferedReader(file);
    line = in.readLine();  
    conected = line.split("\t")[0];
    status = line.split("\t")[1];
    error = line.split("\t")[2];
    in.close();
}

try {
    checkStatus();
    if(!"TRUE".equals(conected.trim())) {
        System.out.println("Impresora Bixolon Desconectada o Apagada - Respuesta: " + conected.trim());
        JOptionPane.showMessageDialog(null, "Impresora Bixolon Desconectada o Apagada \nRESPUESTA: " + conected.trim(), "PRECAUCIÓN", JOptionPane.WARNING_MESSAGE);
        ready = "FALSE";
    }

    if(!"4".equals(status.trim()) && "TRUE".equals(conected.trim())) {
        System.out.println("Impresora Bixolon No está lista - Estatus: " + status.trim());
        if("5".equals(status.trim())) {
            ready = "FALSE";
            int selection = JOptionPane.showConfirmDialog(null, "Impresora Bixolon presenta Estatus 5, No termino de imprimir el Último Documento "
                + "\n¿Desea Completar impresión?", "Estatus Bixolon", JOptionPane.YES_NO_OPTION);
            if(selection == 1) {
                System.out.println("El Usuario No desea terminar de imprimir el Documento");
            } else {
            File spooler = new File("C:\\IntTFHKA\\selectrapos.txt");
            if(spooler.exists()) {
                FileReader fileR = new FileReader(spooler);
                BufferedReader br = new BufferedReader(fileR);
                String lines;
                boolean containsPayments = false;
                String payments = "";
                System.out.println("--- Contenido de la Factura ---");
                while((lines = br.readLine()) != null) {
                    System.out.println(lines);
                    if("3".equals(lines)) {
                        while((lines = br.readLine()) != null) {
                        payments += lines;
                        System.out.println("--- Pagos de la Factura ---");
                        System.out.println(payments);
                        }
                    }
                }
                fileR.close();
                    if(!"".equals(payments) || payments != null) {
                        Writer wr = new BufferedWriter(new FileWriter(spooler));
                        wr.write(payments);
                        wr.close();
                        if(spooler.length() != 0) {
                            spoolerBixolon = path+"IntTFHKA.exe SendFileCmd(C:/IntTFHKA/selectrapos.txt)";
                            System.out.println("--------------------    IMPRIMIENDO FACTURA    --------------------");
                            Runtime.getRuntime().exec(spoolerBixolon).waitFor();
                            System.out.println("--------------------    SE TERMINA DE IMPRIMIR FACTURA    --------------------");
                            checkStatus();
                            if("TRUE".equals(conected.trim()) && "4".equals(status.trim()) && "0".equals(error.trim())) {
                                ready = "TRUE";
                                System.out.println("Impresora Conectada, y sin errores.");
                            }
                        } else {
                            System.out.println("El Spooler " + spooler.toString() + " está vacio.");
                        }
                    } else {
                        System.out.println("El Spooler no contiene los pagos de la Factura");
                    }
            } else {
                System.out.println("Spooler " + spooler.toString() + " no existe.");
            }
        }
        } else {
            JOptionPane.showMessageDialog(null, "Impresora Bixolon No está lista \nESTATUS: " + status.trim(), "ERROR - STATUS " + status.trim(), JOptionPane.ERROR_MESSAGE);
            ready = "FALSE";
        }
    }

    if(!"0".equals(error.trim()) && "TRUE".equals(conected.trim())) {
        System.out.println("Impresora Bixolon presenta error N° " + error.trim());
        JOptionPane.showMessageDialog(null, "Impresora Bixolon presenta error N° " + error.trim() + "\nERROR: " + error.trim(), JOptionPane.ERROR_MESSAGE);
        ready = "FALSE";
    }

    /* Delete "Status_Error" after Check Status. - 07/2016
        Ing. Victor Suárez - victor.suarez.is@gmail.com - vsuarez@as-solucionesintegrales.com.ve
    */
    File statusFile = new File(path+"Status_Error.txt");
    if (statusFile.exists()) {
        if (statusFile.delete())
        System.out.println("Archivo de Status \"Status_Error\" eliminado exitosamente");
        else 
        System.out.println("No se pudo eliminar el Archivo de Status \"Status_Error\"");
    } else {
        System.out.println("El Archivo de Status \"Status_Error\" no Existe");
    }

} catch (FileNotFoundException ex) {
    JOptionPane.showMessageDialog(null, "Error al chequear status impresora Bixolon\n" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
} catch (IOException ex) {
    JOptionPane.showMessageDialog(null, "Error al chequear status impresora Bixolon\n" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
} catch (InterruptedException ex) {
    JOptionPane.showMessageDialog(null, "Error al chequear status impresora Bixolon\n" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
}
return ready.trim();