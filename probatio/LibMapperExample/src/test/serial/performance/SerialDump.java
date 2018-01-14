package test.serial.performance;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import mvc.model.BlockType;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.serial.Serial;

public class SerialDump extends PApplet{

	private boolean serialIsReady;
	private Serial myPort;

	public void settings() {
		size(100, 100);
		//pixelDensity(2);
		//smooth();
	}

	public void setup () {
		frameRate(120);
		long startTime = millis();
		serialIsReady = false;
		println(Serial.list());
		//myPort = new Serial(this, Serial.list()[5], 115200);
		myPort = new Serial(this, "/dev/tty.usbmodem14131", 115200);
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
				System.out.println(message + "\t");
				//System.out.println(Arrays.toString(meusInts)+ "\t");
				//System.out.println(parse(meusInts) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private String parse(int[] meusInts) {
//		String blocks = "";
//		int[] blockIds = new int[5];
//		if(meusInts.length == 14){
//			blockIds[0] = meusInts[0];
//			blockIds[1] = meusInts[3];
//			blockIds[2] = meusInts[5];
//			blockIds[3] = meusInts[7];
//			blockIds[4] = meusInts[10];
//			for (int i = 0; i < blockIds.length; i++) {
//				blocks = blocks + "[" + stringBlock(blockIds[i]) + "] ";
//			}
//
//			blocks = blocks.trim();
//		}
//		return blocks;
//	}
//
//	private String stringBlock(int blockId){
//		return BlockType.getBlockStringCodeById(blockId);
//	}
//
//	private String stringBlockNoSpaces(int blockId){
//		return BlockType.getBlockStringCodeById(blockId).replace(" ", "");
//	}

	@Override
	public void draw() {
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKey() == 'q') {
			exit(); // Stops the program
		}
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] {SerialDump.class.getName()};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

	public String getTimestampNow(){
		java.util.Date date= new java.util.Date();
		String string = "" + new Timestamp(date.getTime());
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		return string;
	}

	public String customFormat(String pattern, float value) {
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		String output = myFormatter.format(value);
		return output;
	}


}
