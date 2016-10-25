package utils;
import processing.core.PApplet;

public class Utils {

	private PApplet processing;
	
	public Utils() {
		this.processing = new PApplet();
	}
	
	public Utils(PApplet processing){
		this.processing = processing;
	}
	
	public PApplet getProcessing() {
		return processing;
	}

//	public int pickAColor(int index){
//		int[] colors = new int[12];
//		int alpha = 200;
//		colors[0] = processing.color(221, 28, 133, alpha);
//		colors[1] = processing.color(149, 40, 137, alpha);
//		colors[2] = processing.color(87, 42, 139, alpha);
//		colors[3] = processing.color(0, 61, 150, alpha);
//		colors[4] = processing.color(0, 125, 198, alpha);
//		colors[5] = processing.color(2, 155, 124, alpha);
//		colors[6] = processing.color(124, 255, 0, alpha);
//		colors[7] = processing.color(254, 246, 0, alpha);
//		colors[8] = processing.color(255, 184, 0, alpha);
//		colors[9] = processing.color(245, 124, 0, alpha);
//		colors[10] = processing.color(240, 90, 6, alpha);
//		colors[11] = processing.color(233, 0, 19, alpha);
//		return colors[index%12];
//	}
	public int pickAColor(int index){
		int[] colors = new int[6];
		int alpha = 255;
		colors[0] = processing.color(255, 0, 0, alpha);
		colors[1] = processing.color(0, 255, 0, alpha);
		colors[2] = processing.color(0, 0, 255, alpha);
		colors[3] = processing.color(255, 255, 0, alpha);
		colors[4] = processing.color(255, 0, 255, alpha);
		colors[5] = processing.color(0, 255, 255, alpha);
		return colors[index%6];
	}
	
}
