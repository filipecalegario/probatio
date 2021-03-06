package old;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import display.DisplayManager;
import model.Block;
import model.BlockFactory;
import processing.core.PApplet;
import processing.serial.Serial; 

//Compatible with XS_Hub_v0_0_15
//Compatible with MockupSerial_v0.0.3
//Parsing one big line

public class Serial_Processing_libMapper_v0_0_10 extends PApplet {

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
		int numberOfSlots = 12;
		display = new DisplayManager(this,numberOfSlots);
		MapperManager.freeOnShutdown();
		MapperManager.printDeviceInitialization();

		println(Serial.list());
		//myPort = new Serial(this, Serial.list()[5], 115200);
		myPort = new Serial(this, "/dev/cu.usbmodem14111", 115200);
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
		deltaSerialEventTimeArray = new long[100];
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
							registeredRemovals.addElement(currentBlock.getId());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		for (int i = 0; i < registeredRemovals.size(); i++) {
			int idBlockToRemove = registeredRemovals.get(i);
			removeBlockEvent(idBlockToRemove);			
		}
		registeredRemovals.removeAllElements();

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
			System.out.println("Serial Event Average Time: ==================> " + serialEventAverageTime + 
					                      "\tMAX = " + maxAverageSerialTime + 
					                      "\tMIN = " + minAverageSerialTime +
					                      "\tMEIO = " + meio);
			this.lastSerialEventTime = currentSerialEventTime;
			try {
				String inString = myPort.readStringUntil('\n');
				//System.out.print(inString);
				//String out = inString.substring(0, inString.indexOf("\n")-1);
				byte[] meusBytes = myPort.readBytes();
				int[] meusInts = new int[meusBytes.length];
				for (int i = 0; i < meusBytes.length; i++) {
					meusInts[i] = meusBytes[i] & 0xFF;
				}
				//System.out.println(meusBytes.length);
				//System.out.println(Arrays.toString(meusInts));
				//TODO CHECK COMPATIBILITY
				parseAndAddOrUpdate(inString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void parseAndAddOrUpdate(String inString) {
		String subString = "";
		if(inString != null && inString.length() > 0){
			subString = inString.substring(0, inString.indexOf('\n')-1);
			String[] pieces = split(subString, ';');
			Iterable<String> piecesIterable = Arrays.asList(pieces);
			Iterator<String> iterator = piecesIterable.iterator();
			while(iterator.hasNext()){
				if(iterator.next().equals("#")){
					int id = Integer.parseInt(iterator.next());
					int quantity = Integer.parseInt(iterator.next()); 
					int[] values = new int[quantity];
					for (int j = 0; j < values.length; j++) {
						values[j] = Integer.parseInt(iterator.next());
					}
					if(values[0] == -1){
						//registeredRemovals.add(id);
					} else {
						if(repositoryContainsBlock(id)){
							updateBlockEvent(id, values);
						} else {
							Block newBlock = BlockFactory.createBlock(id, values, millis()); 
							addBlockEvent(newBlock);
						}						
					}
				}
			}		
		}
	}

	private void addBlockEvent(Block block) {
		if (block != null) {
			if(!repositoryContainsBlock(block.getId())){
				System.out.println(block.getName() + " added");
				blocks.addElement(block);
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
		String[] appletArgs = new String[] {"main.Serial_Processing_libMapper_v0_0_10"};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

}
