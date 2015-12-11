package serialTest;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.BlockType;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.serial.Serial;

public class SerialTest_v_0_0_1 extends PApplet{

	private long lastSerialEventTime;
	private boolean serialIsReady;
	private Serial myPort;
	private long[] deltaSerialEventTimeArray;
	private float[] averageSerialTime;
	private int indexSerialEventTime;
	private int indexAverageSerialTime;
	PrintWriter output;
	private long outputPrintTime;
	private boolean writingOnFile;

	public void settings() {
		size(100, 100);
		//pixelDensity(2);
		//smooth();
	}

	public void setup () {
		frameRate(120);
		this.outputPrintTime = millis();
		long startTime = millis();
		serialIsReady = false;
		println(Serial.list());
		//String portName = Serial.list()[5];
		String portName = "/dev/cu.usbmodem14121";
		//myPort = new Serial(this, portName, 115200);
		//myPort = new Serial(this, "/dev/tty.usbmodem782301", 115200);
		myPort = new Serial(this, portName, 115200);
		myPort.bufferUntil('\n');
		myPort.clear();
		println("Initializing serial port: " + portName);

		while (millis() - startTime < 500) {
			if (myPort.available() > 0) {
				myPort.readStringUntil('\n');
			}
		}
		println("Serial port ready!");
		serialIsReady = true;
		background(255);
		//TODO remover SerialEventTime
		deltaSerialEventTimeArray = new long[1000];
		averageSerialTime = new float[1000];
		indexSerialEventTime = 0;
		indexAverageSerialTime = 0;
		initOutputFile();
	}

	private void initOutputFile() {
		if (output != null) {
			//output.println("Base: Teensy 3.1 Mockup 0.0.5");
			output.println("Base: Arduino Uno - XS_Hub_v0_0_16.ino");
			//output.println("Serial Port: " + myPort.port.getPortName());
			output.println(BlockType.getBlockStringCodeById(BlockType.RESTOUCH) + ": ATTiny 85 8Mhz");
			output.println(BlockType.getBlockStringCodeById(BlockType.CRANK) + ": Nano V3.0 Arduino-compatible 16Mhz");
			output.println(BlockType.getBlockStringCodeById(BlockType.BELLOWS) + ": ATTiny 85 8Mhz");
			output.println(
					BlockType.getBlockStringCodeById(BlockType.TURNTABLE) + ": ATTiny 85 8Mhz with interruptions");
			output.println(BlockType.getBlockStringCodeById(BlockType.DEBUG) + ": Pro Mini Arduino-compatible 16Mhz");
			output.println("Beginning: " + getTimestampNow());
			output.println("================================================================");
			output.println("[]#[]#[]#[]#[]#CURR#MAX#MIN#MIDDLE#TIMESTAMP");
		}
	}

	public void serialEvent (Serial myPort) {
		//System.out.println("serial event = " + myPort.available());
		try {
			if(serialIsReady){
				long currentSerialEventTime = System.currentTimeMillis();
				long deltaSerialEventTime = currentSerialEventTime - lastSerialEventTime;
				deltaSerialEventTimeArray[indexSerialEventTime] = deltaSerialEventTime;
				indexSerialEventTime = (indexSerialEventTime + 1)%deltaSerialEventTimeArray.length;
				long serialEventTimeSum = 0;
				for (int i = 0; i < deltaSerialEventTimeArray.length; i++) {
					serialEventTimeSum = serialEventTimeSum + deltaSerialEventTimeArray[i];
				}
				float serialEventAverageTime = (serialEventTimeSum * 1.0f)/(deltaSerialEventTimeArray.length*1.0f);
				averageSerialTime[indexAverageSerialTime] = serialEventAverageTime;
				indexAverageSerialTime = (indexAverageSerialTime+1)%averageSerialTime.length;
				float maxAverageSerialTime = max(averageSerialTime);
				float minAverageSerialTime = min(averageSerialTime);
				float meio = (maxAverageSerialTime+minAverageSerialTime)/2.0f;
				float aux = maxAverageSerialTime;
				//			if(abs(lastMaxAverageSerialTime-maxAverageSerialTime) < 0.1){
				//				aux = lastMaxAverageSerialTime;
				//			} else {
				//			this.lastMaxAverageSerialTime = maxAverageSerialTime;
				//			}
				//			System.out.println("Serial Event Average Time: ==================> " + serialEventAverageTime + 
				//					"\tMAX = " + maxAverageSerialTime + 
				//					"\tMIN = " + minAverageSerialTime +
				//					"\tMEIO = " + meio);
				//String averageS = customFormat("###.##", serialEventAverageTime);
				//String maxS = customFormat("###.##", maxAverageSerialTime);
				//String minS = customFormat("###.##", minAverageSerialTime);
				//String meioS = customFormat("###.##", meio);
				String averageS = String.format("%.3f", serialEventAverageTime);
				String maxS = String.format("%.3f", maxAverageSerialTime);
				String minS = String.format("%.3f", minAverageSerialTime);
				String meioS = String.format("%.3f", meio);

				byte[] meusBytes = myPort.readBytes();
				int[] meusInts = new int[meusBytes.length];
				for (int i = 0; i < meusBytes.length; i++) {
					meusInts[i] = meusBytes[i] & 0xFF;
				}

				int[] blockIds = new int[5];
				if(meusInts.length == 14){
					blockIds[0] = meusInts[0];
					blockIds[1] = meusInts[3];
					blockIds[2] = meusInts[5];
					blockIds[3] = meusInts[7];
					blockIds[4] = meusInts[10];

					String blocks = "";
					String blocksForOutputFile = "";

					for (int i = 0; i < blockIds.length; i++) {
						blocks = blocks + "[" + stringBlock(blockIds[i]) + "] ";
					}

					for (int i = 0; i < blockIds.length; i++) {
						blocksForOutputFile = blocksForOutputFile + "" + stringBlockNoSpaces(blockIds[i]) + "#";
					}

					blocks = blocks.trim();

					String timestampNow = getTimestampNow();
					String outputString = blocks + " ====> " + averageS + 
							//					"\tATTENUATED = " + aux + 
							"\tMAX = " + maxS + 
							"\tMIN = " + minS +
							"\tMEIO = " + meioS +
							"\t=> " + timestampNow;
					System.out.println(outputString);
					if((millis() - outputPrintTime) < 500){
						if (this.output != null) {
							output.println(blocksForOutputFile + "" + averageS + "#" + maxS + "#" + minS + "#" + meioS
									+ "#" + timestampNow);
						}
						writingOnFile = true;
					} else {
						writingOnFile = false;
					}
					this.lastSerialEventTime = currentSerialEventTime;



					if (this.output != null) {
						output.flush(); // Writes the remaining data to the file
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String stringBlock(int blockId){
		return BlockType.getBlockStringCodeById(blockId);
	}

	private String stringBlockNoSpaces(int blockId){
		return BlockType.getBlockStringCodeById(blockId).replace(" ", "");
	}

	@Override
	public void draw() {
		if(writingOnFile){
			background(255,0,0);
		} else {
			background(255);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKey() == 'q') {
			if (this.output != null) {
				output.flush(); // Writes the remaining data to the file
				output.close(); // Finishes the file
			}
			exit(); // Stops the program
		}
		if(event.getKey() == ' '){
			if(this.output == null){
				output = createWriter(
						"report/report_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_E").format(new Date()) + ".txt");
				initOutputFile();
			}
			this.outputPrintTime = millis();
		}
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] {SerialTest_v_0_0_1.class.getName()};
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
