import java.util.Arrays;
import java.util.HashMap;

import Mapper.Device;
import Mapper.Device.Signal;
import Mapper.PropertyValue;
import model.Block;
import model.BlockType;
import processing.core.PApplet;
import processing.serial.Serial;
import repository.BlockRepository; 

public class Serial_Processing_libMapper_v0_0_3 extends PApplet {

	final Device dev = new Device("Serial_Processing", 9001);
	private Signal out1;

	Serial myPort;        // The serial port
	//PrintWriter output;
	int xPos = 1;         // horizontal position of the graph
	float inByte = 0;
	float lastInByte = 0;
	float lastDerivate = 0;
	float energy = 0;

	BlockRepository repo;
	HashMap<Integer, Block> blocks;

	public void settings() {  
		size(800, 600);
		pixelDensity(2);
		smooth();
	}

	public void setup () {

		//pixelDensity(2);
		//smooth();
		//output = createWriter("output.txt");

		repo = new BlockRepository();
		blocks = repo.getBlocks();

		freeOnShutdown();
		out1 = dev.add_output("out1", 1,'i', "unit", 0.0, 255.0);

		dev.set_property("width", new PropertyValue(256));
		dev.set_property("height", new PropertyValue(12.5));
		dev.set_property("depth", new PropertyValue("67"));

		out1.set_property("width", new PropertyValue(128));
		out1.set_property("height", new PropertyValue(6.25));
		out1.set_property("depth", new PropertyValue("test"));

		initializeDevice();

		println(Serial.list());
		myPort = new Serial(this, Serial.list()[5], 115200);
		myPort.bufferUntil('\n');
		myPort.clear();
		background(255);
	}

	public void draw () {
		float derivate = inByte - lastInByte;
		float derivate2 = abs(derivate) - lastDerivate;

		if(derivate > 0) {
			stroke(0);
		} else if(derivate < 0){
			stroke(255,0,0);
		} else {
			stroke(127);
		}
		line(xPos, height, xPos, height - map(inByte, 0, 255, 0, height));

		stroke(0,0,255);
		line(xPos, 0, xPos, 0 + map(abs(derivate), 0, 255, 0, height));

		stroke(0,255,0);
		line(xPos, 0, xPos, 0 + map(abs(derivate2), 0, 255, 0, height));

		lastDerivate = abs(derivate);

		energy = energy + abs(derivate2);
		energy = constrain(energy, 0, height);

		stroke(255,0,255);
		//line(xPos, 0, xPos, 0 + energy);
		energy = energy - 10;  

		int valueToLibmapper = (int) inByte;
		out1.update(valueToLibmapper);

		if (xPos >= width) {
			xPos = 0;
			background(255);
		} else {
			xPos++;
		}
		dev.poll(0);
		checkBlocksPresence();
		for (Integer index : blocks.keySet()) {
			Block currentBlock = blocks.get(index);
			if(currentBlock.getId() != BlockType.NONE){
				//System.out.println(currentBlock);
			}
		}
	}

	private void checkBlocksPresence() {
		
		try {
			for (Integer index : blocks.keySet()) {
				Block currentBlock = blocks.get(index);
				if(millis() - currentBlock.getLastTimeUpdated() > 100){
					System.out.println(currentBlock.getId() + " removed");
					blocks.remove(index);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop(){
		dev.free();
	}

	public void serialEvent (Serial myPort) {

		try {
			String inString = myPort.readStringUntil('\n');
			String subString = "";
			if(inString != null && inString.length() > 0){
				subString = inString.substring(inString.indexOf('#')+1, inString.indexOf('\n')-1);
				String[] pieces = split(subString, ';');
				//System.out.println(Arrays.toString(pieces));
				int address = Integer.parseInt(pieces[0]);
				int bytesQuant = Integer.parseInt(pieces[1]);
				int[] values = new int[bytesQuant];
				for (int i = 0; i < bytesQuant; i++) {
					values[i] = Integer.parseInt(pieces[i+2]);
				}
				if(blocks.containsKey(address)){
					blocks.get(address).updateValues(values, millis());
				} else {
					Block newBlock = new Block(address, values, millis()); 
					blocks.put(address, newBlock);
					System.out.println(newBlock.getId() + " added");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	private BlockType getBlockType(int address){
//		BlockType type = BlockType.NONE;
//		if(address == 50){
//			type = BlockType.BELLOWS;
//		} else if(address == 49){
//			type = BlockType.RESTOUCH;
//		} else if(address == 45){
//			type = BlockType.CRANK;
//		} 
//		return type;
//	}

	public void keyPressed() {
		//output.flush(); // Writes the remaining data to the file
		//output.close(); // Finishes the file
		exit(); // Stops the program
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "Serial_Processing_libMapper_v0_0_3" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

	private void initializeDevice() {
		System.out.println("Waiting for ready...");
		while (!dev.ready()) {
			dev.poll(100);
		}
		System.out.println("Device is ready.");

		System.out.println("Device name: "+dev.name());
		System.out.println("Device port: "+dev.port());
		System.out.println("Device ordinal: "+dev.ordinal());
		System.out.println("Device interface: "+dev.iface());
		System.out.println("Device ip4: "+dev.ip4());
	}

	private void freeOnShutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				dev.free();
			}
		});
	}
}
