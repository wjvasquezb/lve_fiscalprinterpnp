package ve.com.as.process;

import org.compiere.process.SvrProcess;


public class LVE_CloseX extends SvrProcess {
	
	@Override
	protected void prepare() {
		System.out.println("Prepare Close X");
	}

	@Override
	protected String doIt() throws Exception {
		LVE_PrintDocument printX = new LVE_PrintDocument();
		printX.sendCommand("cierrex");
		return "Cierre X realizado Exitosamente!";
	}
}