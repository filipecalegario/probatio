import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestMain {
	
	public static void main(String[] args) {
//		MapperManager.freeOnShutdown();
//		MapperManager.printDeviceInitialization();
//		for (int i = 0; i < 20; i++) {
//			MapperManager.addOutput("teste" + i, 1, 'i', "unit", 0.0d, 255.0d);
//			System.out.println("Signal added == " + i);
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		
		java.util.Date date= new java.util.Date();
		System.out.println(new Timestamp(date.getTime()));
		System.out.println(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_E").format(new Date()));
	}
	
}
