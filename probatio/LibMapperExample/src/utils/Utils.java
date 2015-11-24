package utils;
import processing.core.PApplet;

public class Utils {

	private PApplet processing;
	
	public Utils(PApplet core) {
		this.processing = core;
	}
	
	public int pickAColor(int index){
		int[] colors = new int[12];
		colors[0] = processing.color(221, 28, 133);
		colors[1] = processing.color(149, 40, 137);
		colors[2] = processing.color(87, 42, 139);
		colors[3] = processing.color(0, 61, 150);
		colors[4] = processing.color(0, 125, 198);
		colors[5] = processing.color(2, 155, 124);
		colors[6] = processing.color(124, 255, 0);
		colors[7] = processing.color(254, 246, 0);
		colors[8] = processing.color(255, 184, 0);
		colors[9] = processing.color(245, 124, 0);
		colors[10] = processing.color(240, 90, 6);
		colors[11] = processing.color(233, 0, 19);
		return colors[index%12];
	}
	
}
