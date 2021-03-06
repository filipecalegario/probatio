package old;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import cooked.KinematicCrank;
import display.DisplayManager;
import model.Block;
import model.BlockFactory;
import model.BlockType;
import processing.core.PApplet;
import processing.serial.Serial; 

//Compatible with XS_Hub_v0_0_15_EXPERIMENTANDO_WRITE_ARRAY_REDUCED_PROTOCOL_WITH and
// XS_Hub_v0_0_16.ino
//Compatible with MockupSerial_v0.0.3
//Parsing one big line

public class Serial_Processing_libMapper_v0_0_11 extends PApplet {

	Serial myPort;        

	//MapperManager mapperManager;
	Vector<Block> blocks;
	Vector<Integer> registeredRemovals;
	DisplayManager display;
	long lastSerialEventTime;
	long[] deltaSerialEventTimeArray;
	float[] averageSerialTime;
	int indexSerialEventTime;
	int indexAverageSerialTime;
	float lastMaxAverageSerialTime;

	boolean serialIsReady;

	public void settings() {
		size(800, 600);
		pixelDensity(2);
		smooth();
	}

	public void setup () {
		frameRate(120);
		long startTime = millis();
		serialIsReady = false;
		blocks = new Vector<Block>();
		registeredRemovals = new Vector<Integer>();
		int numberOfSlots = 20;
		display = new DisplayManager(this,numberOfSlots);
		MapperManager.freeOnShutdown();
		MapperManager.printDeviceInitialization();

		println(Serial.list());
		myPort = new Serial(this, Serial.list()[5], 115200);
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
		//TODO remover SerialEventTime
		deltaSerialEventTimeArray = new long[1000];
		averageSerialTime = new float[1000];
		indexSerialEventTime = 0;
		indexAverageSerialTime = 0;
	}

	public void draw () {
		MapperManager.pollDevice();
		display.updateDrawDisplaySlot();
		if(blocks.isEmpty()){
			display.cleanScreen();
			display.resetCounter();
		}
		try {
			for (int i = 0; i < blocks.size(); i++) {
				Block currentBlock = blocks.elementAt(i);
				if(millis() - currentBlock.getLastTimeUpdated() > 1000){
					//registeredRemovals.addElement(currentBlock.getId());
					removeBlockEvent(currentBlock.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//	public void stop(){
	//		mapperManager.freeDevice();
	//	}

	public void serialEvent (Serial myPort) {
		if(serialIsReady){
			long currentSerialEventTime = System.currentTimeMillis();
			long deltaSerialEventTime = currentSerialEventTime - lastSerialEventTime;
			deltaSerialEventTimeArray[indexSerialEventTime] = deltaSerialEventTime;
			indexSerialEventTime = (indexSerialEventTime + 1)%deltaSerialEventTimeArray.length;
			long serialEventTimeSum = 0;
			long maxTime = 0;
			long minTime = Long.MAX_VALUE;
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

			this.lastSerialEventTime = currentSerialEventTime;
			try {
				//String inString = myPort.readStringUntil('\n');
				//System.out.print(inString);
				//String out = inString.substring(0, inString.indexOf("\n")-1);
				byte[] meusBytes = myPort.readBytes();
				int[] meusInts = new int[meusBytes.length];
				for (int i = 0; i < meusBytes.length; i++) {
					meusInts[i] = meusBytes[i] & 0xFF;
				}
				//System.out.println(meusBytes.length);
				//System.out.println(Arrays.toString(meusInts));
				if(meusInts.length == 14){
					parseAndAddOrUpdate(meusInts);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void parseAndAddOrUpdate(int[] ints) {
		//[BLOCK_RESTOUCH, 0, 25, BLOCK_CRANK, 22, BLOCK_BELLOWS, 0, BLOCK_TURNTABLE, 155, 0, BLOCK_DEBUG, 156, 99, 10]
		int[] blockRestouchValue = new int[2];
		int[] blockCrankValue = new int[1];
		int[] blockBellowsValue = new int[1];
		int[] blockTurntableValue = new int[2];
		int[] blockDebugValue = new int[2];

		int blockRestouchId = ints[0];
		blockRestouchValue[0] = ints[1];
		blockRestouchValue[1] = ints[2];
		int blockCrankId = ints[3];
		blockCrankValue[0] = ints[4];
		int blockBellowsId = ints[5];
		blockBellowsValue[0] = ints[6];
		int blockTurntableId = ints[7];
		blockTurntableValue[0] = ints[8];
		blockTurntableValue[1] = ints[9];
		int blockDebugId = ints[10];
		blockDebugValue[0] = ints[11];
		blockDebugValue[1] = ints[12];

		parseBlock(blockRestouchId, blockRestouchValue);
		parseBlock(blockCrankId, blockCrankValue);
		parseBlock(blockBellowsId, blockBellowsValue);
		parseBlock(blockTurntableId, blockTurntableValue);
		parseBlock(blockDebugId, blockDebugValue);
	}

	private void parseBlock(int blockId, int[] values){
		if (blockId != 0) {
			if (repositoryContainsBlock(blockId)) {
				updateBlockEvent(blockId, values);
			} else {
				Block newBlock = BlockFactory.createBlock(blockId, values, millis());
				addBlockEvent(newBlock);
			} 
		}						
	}

	private void addBlockEvent(Block block) {
		if (block != null) {
			if(!repositoryContainsBlock(block.getId())){
				blocks.addElement(block);
				System.out.println(block.getName() + " added");
				//mapperManager.addSignalFromBlock(block);
				for (int i = 0; i < block.getValues().length; i++) {
					display.addDisplaySlot(block.getId(), i, block.getValuesLabels()[i]);
				}
			}
		}
	}

	private void updateBlockEvent(int idBlock, int[] values) {
		Block block = getBlockById(idBlock);
		if (block != null) {
			block.updateValues(values, millis());
			try {
				//mapperManager.updateSignalFromBlock(block);
			} catch (Exception e) {
				//TODO Be careful!
				//e.printStackTrace();
			}
			for (int i = 0; i < block.getValues().length; i++) {
				display.updateValueDisplaySlot(block.getId(), i, values[i]);
			} 
		}
	}

	private void removeBlockEvent(int idBlock) {
		Block block = getBlockById(idBlock);
		if (block != null) {
			String name = block.getName();
			//mapperManager.removeSignalFromBlock(block);
			for (int i = 0; i < block.getValues().length; i++) {
				display.removeDisplaySlot(block.getId(), i);
			}
			blocks.removeElement(block);
			System.out.println(name + " removed");
		}
	}

	//TODO This is not cool!
	private boolean repositoryContainsBlock(int id) {
		boolean result = false;
		for (Block block : blocks) {
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
			for (Block block : blocks) {
				if(block.getId() == id){
					result = block;
				}
			}
		}
		return result;
	}

	public void keyPressed() {
		//MapperManager.freeDevice();
		exit();
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] {"main.Serial_Processing_libMapper_v0_0_11"};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

}
