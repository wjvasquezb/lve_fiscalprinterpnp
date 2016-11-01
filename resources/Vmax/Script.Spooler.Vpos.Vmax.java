//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Script.Spooler.Vpos.VMAX
 * Recurso para Imprimir los recibos y reportes emitidos por el VPOS en la Impresora Fiscal Vmax.
 * @author Ing. Victor Suárez - victor.suarez.is@gmail.com - vsuarez@as-solucionesintegrales.com.ve - Venezuela
 * @version 07/2015
 */

Object result = null;
File file = new File(path);
String pathSpooler = "C:/Elepos/Spooler/";
if(file.exists()) {
    File spooler = new File(pathSpooler + "files/selectrapos.txt");
    try {
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        Writer wSpooler = new BufferedWriter(new FileWriter(spooler));
        String content = null;
        wSpooler.write("<ABRIR_DNF>");
        while((content = bReader.readLine())!=null) {
            wSpooler.write("<TEXTO_DNF," + content + ",0>" + "\n");
        }
        wSpooler.write("<CERRAR_DNF>");
        bReader.close();
        wSpooler.close();
    } catch (IOException ex) {
        Logger.getLogger(ScriptSpoolerVposVmax.class.getName()).log(Level.SEVERE, null, ex);
    }
    try {
        String spoolerVmax = pathSpooler + "epsSpoolerVmax.exe";
        Runtime.getRuntime().exec(spoolerVmax,null, new File(pathSpooler)).waitFor();
        result = "TRUE";
        // Delete Spooler
        if(spooler.exists())
        if(spooler.delete())
        System.out.println("Spooler eliminado exitosamente");
        else
        System.out.println("No se pudo eliminar el Spooler");
        else 
        System.out.println("Spooler no existe");
    } catch (IOException eF) {
    // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.
        eF.printStackTrace();
        result = "FALSE";
    } catch (InterruptedException ex) {
        JOptionPane.showMessageDialog(null, "Error al Imprimir Documento NO FISCAL VPOS \n" + ex);
    result = "FALSE";
    }
}

return result;