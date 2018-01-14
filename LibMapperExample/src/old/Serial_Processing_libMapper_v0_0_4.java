package old;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import Mapper.Device;
import Mapper.Device.Signal;
import Mapper.PropertyValue;
import model.Block;
import model.BlockType;
import processing.core.PApplet;
import processing.serial.Serial;
import repository.BlockRepository; 

public class Serial_Processing_libMapper_v0_0_4 extends PApplet {

	//private Signal out1;

	Serial myPort;        // The serial port
	//PrintWriter output;
	int xPos = 1;         // horizontal position of the graph
	float inByte = 0;
	float lastInByte = 0;
	float lastDerivate = 0;
	float energy = 0;

	MapperDelegate mapperDelegate;
	BlockRepository repo;
	HashMap<Integer, Block> blocks;
	Vector<Block> blocksAlt;

	boolean serialIsReady;

	public void settings() {  
		size(800, 600);
		pixelDensity(2);
		smooth();
	}

	public void setup () {
		long startTime = millis();
		//pixelDensity(2);
		//smooth();
		//output = createWriter("output.txt");
		serialIsReady = false;
		repo = new BlockRepository();
		blocks = repo.getBlocks();
		mapperDelegate = new MapperDelegate("probatio");
		blocksAlt = new Vector<Block>();

		//freeOnShutdown();

		mapperDelegate.printDeviceInitialization();

		println(Serial.list());
		myPort = new Serial(this, Serial.list()[5], 115200);
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

		//		int valueToLibmapper = (int) inByte;
		//		out1.update(valueToLibmapper);

		if (xPos >= width) {
			xPos = 0;
			background(255);
		} else {
			xPos++;
		}
		//dev.poll(0);
		mapperDelegate.pollDevice();
		//checkBlocksPresence();
		try {
			//Iterator<Entry<Integer, Block>> it = blocks.entrySet().iterator();
			for (int i = 0; i < blocksAlt.size(); i++) {
				Block currentBlock = blocksAlt.elementAt(i);
				if(currentBlock.getId() != BlockType.NONE){
					//System.out.println(currentBlock);
				}
				if(millis() - currentBlock.getLastTimeUpdated() > 100){
					removeBlockEvent(currentBlock);
				}
			}
			//			while(it.hasNext()){
			//				Entry<Integer, Block> blockEntry = it.next();
			//				Block currentBlock = blockEntry.getValue();
//							if(currentBlock.getId() != BlockType.NONE){
//								//System.out.println(currentBlock);
//							}
			//				if(millis() - currentBlock.getLastTimeUpdated() > 100){
			//					removeBlockEvent(currentBlock);
			//				}
			//			}
			//			for (Integer index : blocks.keySet()) {
			//				Block currentBlock = blocks.get(index);
			//				if(currentBlock.getId() != BlockType.NONE){
			//					//System.out.println(currentBlock);
			//				}
			//				if(millis() - currentBlock.getLastTimeUpdated() > 100){
			//					removeBlockEvent(currentBlock);
			//				}
			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void checkBlocksPresence() {
		//		try {
		//			for (Integer index : blocks.keySet()) {
		//				Block currentBlock = blocks.get(index);
		//				if(millis() - currentBlock.getLastTimeUpdated() > 100){
		//					System.out.println(currentBlock.getType() + " removed");
		//					blocks.remove(index);
		//				}
		//			}
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}

	public void stop(){
		mapperDelegate.freeDevice();
	}

	public void serialEvent (Serial myPort) {
		if(serialIsReady){
			try {
				String inString = myPort.readStringUntil('\n');
				parseAndAddOrUpdate(inString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void parseAndAddOrUpdate(String inString) {
		String subString = "";
		if(inString != null && inString.length() > 0){
			subString = inString.substring(inString.indexOf('#')+1, inString.indexOf('\n')-1);
			String[] pieces = split(subString, ';');
			//System.out.println(Arrays.toString(pieces));
			int id = Integer.parseInt(pieces[0]);
			int bytesQuant = Integer.parseInt(pieces[1]);
			int[] values = new int[bytesQuant];
			for (int i = 0; i < bytesQuant; i++) {
				values[i] = Integer.parseInt(pieces[i+2]);
			}
			if(repositoryContainsBlock(id)){
				updateBlockEvent(id, values);
			} else {
				Block newBlock = new Block(id, values, millis()); 
				addBlockEvent(newBlock);
			}
		}
	}
	
	private void addBlockEvent(Block block) {
		System.out.println(block.getName() + " added");
		//blocks.put(block.getId(), block);
		blocksAlt.addElement(block);
		mapperDelegate.addSignal(block);
	}

	private void updateBlockEvent(int id, int[] values) {
		Block block = getBlockById(id);
		block.updateValues(values, millis());
		mapperDelegate.updateSignal(block);
	}

	private void removeBlockEvent(Block block) {
		String name = block.getName();
		//int type = block.getId();
		mapperDelegate.removeSignal(block);
		//blocks.remove(type);
		blocksAlt.removeElement(block);
		System.out.println(name + " removed");
	}

	//TODO This is not cool!
	private boolean repositoryContainsBlock(int id) {
		boolean result = false;
		for (Block block : blocksAlt) {
			if(block.getId() == id){
				result = true;
			}
		}
		return result;
	}
	
	//TODO Two walks in the array: This is not cool!
	private Block getBlockById(int id) {
		Block result = null;
		if(repositoryContainsBlock(id)){
			for (Block block : blocksAlt) {
				if(block.getId() == id){
					result = block;
				}
			}
		}
		return result;
	}

	public void keyPressed() {
		mapperDelegate.freeDevice();
		exit();
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] {"Serial_Processing_libMapper_v0_0_4"};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

}
