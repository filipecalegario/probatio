package main;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import display.DisplayManager_v0_0_3;
import kinematic.KinematicCrank;
import mapper.MapperManager;
import model.Block;
import model.BlockFactory;
import model.BlockType;
import processing.core.PApplet;
import processing.serial.Serial; 

//Compatible with XS_Hub_v0_0_13
//Compatible with MockupSerial_v0.0.3
//Parsing one big line

public class Serial_Processing_libMapper_v0_0_10 extends PApplet {

	Serial myPort;        

	//MapperManager mapperManager;
	Vector<Block> blocks;
	Vector<Integer> registeredRemovals;
	DisplayManager_v0_0_3 display;
	long lastSerialEventTime;

	boolean serialIsReady;

	public void settings() {
		size(800, 600);
		pixelDensity(2);
		smooth();
	}

	public void setup () {
		long startTime = millis();
		serialIsReady = false;
		blocks = new Vector<Block>();
		registeredRemovals = new Vector<Integer>();
		int numberOfSlots = 12;
		display = new DisplayManager_v0_0_3(this,numberOfSlots);
		MapperManager.freeOnShutdown();
		MapperManager.printDeviceInitialization();

		//println(Serial.list());
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

		MapperManager.pollDevice();
		display.updateDrawDisplaySlot();
		if(blocks.isEmpty()){
			display.cleanScreen();
			display.resetCounter();
		}
		//		try {
		//			for (int i = 0; i < blocks.size(); i++) {
		//				Block currentBlock = blocks.elementAt(i);
		//				if(millis() - currentBlock.getLastTimeUpdated() > 100){
		//					removeBlockEvent(currentBlock);
		//				}
		//			}
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
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
			this.lastSerialEventTime = currentSerialEventTime;
			//System.out.println(deltaSerialEventTime);
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
						registeredRemovals.add(id);
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
		MapperManager.freeDevice();
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
