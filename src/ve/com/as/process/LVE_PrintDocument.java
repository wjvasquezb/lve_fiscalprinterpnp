package ve.com.as.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;

import org.compiere.util.Env;

public class LVE_PrintDocument {

	public String inFile = "";
	public String outFileStr = "";
	public String path = "";
	public String command = "java -jar JPFBatch.jar -AsClient -i ";
	public File outFile = null; 
	public Writer outWtr = null;
	public FileReader fileR = null;
	public BufferedReader out = null;
	public String msg = "";
	
	public LVE_PrintDocument() {
	}
	
	public BufferedReader sendCommand(String inFileStr) throws FileNotFoundException {
		if(Env.isWindows()) {
			path = "C:\\adempiere-client\\jpfbatch\\";
		}
		inFile = path + inFileStr + ".in";
		outFileStr = path + inFileStr + ".out";
		outFile = new File(outFileStr);
		command= command + inFile;
		try {
			System.out.println("Ejecutando Comando: " + command);
			Runtime.getRuntime().exec(command, null, new File(path)).waitFor();
		} catch (IOException eF) {
			msg = "No se ejecuto comando - " + command + " - " + eF.getMessage();
			System.out.println(msg);
		} catch (InterruptedException ex) {
			msg = "No se ejecuto comando - " + command + " - " + ex.getMessage();
			System.out.println(msg);
		} catch (IllegalArgumentException iae) {
			msg = "No se ejecuto comando - " + command + " - " + iae.getMessage();
			System.out.println(msg);
		}

		
		if(!outFile.exists())
			return null;
			
		fileR = new FileReader(outFile);
		out = new BufferedReader(fileR);
		return out;
	}

}
