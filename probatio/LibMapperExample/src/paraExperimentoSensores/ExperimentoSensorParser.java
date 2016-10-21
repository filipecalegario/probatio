package paraExperimentoSensores;

import java.util.Arrays;

import processing.core.PApplet;
import processing.serial.Serial;

public class ExperimentoSensorParser extends PApplet{

	private boolean serialIsReady;
	private Serial myPort;
	
	private void parseAndAddOrUpdate(int[] ints) {
		boolean isGoodFormat = (ints.length == 50) && 
				(ints[0] == 2) && 
				(ints[ints.length-1] == 10);
		if (isGoodFormat) {
			int[] trimmed = new int[48];
			for (int i = 1; i <= 48; i++) {
				trimmed[i-1] = ints[i];
			}
			for (int i = 0; i < trimmed.length/2; i++) {
				int status = trimmed[i*2];
				int value = trimmed[i*2+1];
				//System.out.println(i + " " + status + " " + value);
			}
		}
	}
	
	public void setup () {
		frameRate(120);
		long startTime = millis();
		serialIsReady = false;
		println(Serial.list());
		//myPort = new Serial(this, Serial.list()[5], 115200);
		myPort = new Serial(this, "/dev/tty.usbmodem621", 115200);
		//myPort = new Serial(this, "/dev/cu.usbmodem14111", 115200);
		myPort.bufferUntil('\n');
		myPort.clear();
		println("Initializing serial port...");
		while (millis() - startTime < 500) {
			if (myPort.available() > 0) {
				myPort.readStringUntil('\n');
			}
		}
		println("Serial port ready!");
		serialIsReady = true;
		background(255);
	}

	public void serialEvent (Serial myPort) {
		//System.out.println("serial event = " + myPort.available());
		try {
			if(serialIsReady){
				byte[] meusBytes = myPort.readBytes();
				int[] meusInts = new int[meusBytes.length];
				String message = "[";
				for (int i = 0; i < meusBytes.length; i++) {
					meusInts[i] = meusBytes[i] & 0xFF;
					message = message + String.format("%3d", meusInts[i]);
					if(i != (meusBytes.length - 1)){
						message = message + ", ";
					}
				}
				message = message.trim();
				message = message + "]";
				this.parseAndAddOrUpdate(meusInts);
				//System.out.println(message + "\t");
				//System.out.println(Arrays.toString(meusInts)+ "\t");
				//System.out.println(parse(meusInts) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(){
		
	}
	
	public void settings() {
		size(100, 100);
		//pixelDensity(2);
		//smooth();
	}
	
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] {ExperimentoSensorParser.class.getName()};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
	
}
