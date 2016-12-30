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
	public String command = "java -jar jpfbatch -AsClient -i ";
	public File outFile = null; 
	public Writer outWtr = null;
	public FileReader fileR = null;
	public BufferedReader out = null;
	
	public LVE_PrintDocument() {
	}
	
	public BufferedReader sendCommand(String inFileStr) throws FileNotFoundException {
		if(Env.isWindows()) {
			path = "C:/jpfbatch/";
		}
		inFile = path + inFileStr.concat(".in");
		outFileStr = path + inFileStr;
		outFile = new File(outFileStr);
		command.concat(inFile);
		try {
			System.out.println("Imprimiendo Factura Fiscal");
			Runtime.getRuntime().exec(command).waitFor();
			System.out.println("Factura Fiscal Impresa Correctamente");
		} catch (IOException eF) {
			eF.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		fileR = new FileReader(outFile);
		out = new BufferedReader(fileR);
		return out;
	}

}
