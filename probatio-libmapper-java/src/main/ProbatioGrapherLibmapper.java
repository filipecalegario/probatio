package main;
import java.lang.reflect.Field;
import java.util.Vector;

import display.DisplayManager;
import mapper.MapperManagerProbatio;
import mvc.model.Block;
import mvc.model.BlockFactory;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.serial.Serial; 


//TESTE É AQUI
//Compatible with XS_Hub_v0_0_15_EXPERIMENTANDO_WRITE_ARRAY_REDUCED_PROTOCOL_WITH and
// XS_Hub_v0_0_16.ino
//Compatible with MockupSerial_v0.0.3
//Parsing one big line

public class ProbatioGrapherLibmapper extends PApplet {

	Serial myPort;        

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
		//pixelDensity(2);
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
		MapperManagerProbatio.freeOnShutdown();
		MapperManagerProbatio.initializeDevice();

		println(Serial.list());
		//myPort = new Serial(this, Serial.list()[5], 115200);
		myPort = new Serial(this, "/dev/cu.usbmodem621", 115200);
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
		MapperManagerProbatio.pollDevice();
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
			try {
				byte[] meusBytes = myPort.readBytes();
				boolean isGoodFormat = (meusBytes.length == 23) && 
						(meusBytes[0] == 2) && 
						(meusBytes[meusBytes.length-1] == 10);
				if(isGoodFormat){
					int[] meusInts = new int[meusBytes.length];
					for (int i = 0; i < meusBytes.length; i++) {
						meusInts[i] = meusBytes[i] & 0xFF;
					}
					parseAndAddOrUpdate(meusInts);
					delay(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void parseAndAddOrUpdate(int[] ints) {
		boolean isGoodFormat = (ints.length == 23) && 
				(ints[0] == 2) && 
				(ints[ints.length-1] == 10);
		if (isGoodFormat) {
			//[2, BLOCK_RESTOUCH, 0, 25, BLOCK_CRANK, 22, 22, BLOCK_BELLOWS, 0, BLOCK_TURNTABLE, 155,  0, BLOCK_DEBUG, 156, 99, BLOCK_FOURBUTTONS, 255, 255, 255, 255, 10]
			//[0,              1, 2,  3,           4,  5,  6,             7, 8,               9,  10, 11,          12,  13, 14,                15,  16,  17,  18,  19, 20]
			int[] blockRestouchValue = new int[2];
			int[] blockCrankValue = new int[2];
			int[] blockBellowsValue = new int[1];
			int[] blockTurntableValue = new int[2];
			int[] blockDebugValue = new int[2];
			int[] blockFourButtonsValue = new int[4];
			
			int blockRestouchId = ints[1];
			blockRestouchValue[0] = ints[2];
			blockRestouchValue[1] = ints[3];
			
			int blockCrankId = ints[4];
			blockCrankValue[0] = ints[5];
			blockCrankValue[1] = ints[6];
			
			int blockBellowsId = ints[7];
			blockBellowsValue[0] = ints[8];
			
			int blockTurntableId = ints[9];
			blockTurntableValue[0] = ints[10];
			blockTurntableValue[1] = ints[11];
			
			int blockDebugId = ints[12];
			blockDebugValue[0] = ints[13];
			blockDebugValue[1] = ints[14];
			
			int blockFounButtonsId = ints[15];
			blockFourButtonsValue[0] = ints[16];
			blockFourButtonsValue[1] = ints[17];
			blockFourButtonsValue[2] = ints[18];
			blockFourButtonsValue[3] = ints[19];
			
			parseBlock(blockRestouchId, blockRestouchValue);
			parseBlock(blockCrankId, blockCrankValue);
			parseBlock(blockBellowsId, blockBellowsValue);
			parseBlock(blockTurntableId, blockTurntableValue);
			parseBlock(blockDebugId, blockDebugValue);
			parseBlock(blockFounButtonsId, blockFourButtonsValue);
		}
	}

	private void parseBlock(int blockId, int[] values){
		if (blockId != 0) {
			if (repositoryContainsBlock(blockId)) {
				//System.out.println("UPDATED Block " + BlockType.getBlockNameById(blockId));
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
			if(block != null){
				if(block.getId() == id){
					result = true;
				}
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

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKey() == 'q' || event.getKey() == 'Q'){
			MapperManagerProbatio.freeDevice();
			exit();
		}
	}

	static public void main(String[] passedArgs) {
		System.setProperty( "java.library.path", "/usr/local/lib" );
		 
		Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
			fieldSysPath.setAccessible( true );
			fieldSysPath.set( null, null );
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PApplet.main(ProbatioGrapherLibmapper.class.getName());
//		String[] appletArgs = new String[] {"main.Serial_Processing_libMapper_v0_0_13"};
//		if (passedArgs != null) {
//			PApplet.main(concat(appletArgs, passedArgs));
//		} else {
//			PApplet.main(appletArgs);
//		}
	}

}
