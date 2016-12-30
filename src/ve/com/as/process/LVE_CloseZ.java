package ve.com.as.process;

import org.compiere.process.SvrProcess;


public class LVE_CloseZ extends SvrProcess {
	
	@Override
	protected void prepare() {
		System.out.println("Prepare Close Z");
	}

	@Override
	protected String doIt() throws Exception {
		LVE_PrintDocument printZ = new LVE_PrintDocument();
		printZ.sendCommand("cierrez");
		return "Cierre Z realizado Exitosamente!";
	}
}