package gui.prototype.physical;

import java.util.Vector;

import model.Block;
import model.BlockFactory;
import processing.core.PApplet;
import processing.serial.Serial;

public class OnlySerial{
	private Serial myPort; 
	private Vector<Block> blocks;
	private boolean serialIsReady;
	private ProbatioEventListener probatioListener;
	private PApplet p;

	public OnlySerial(PApplet pApplet, ProbatioEventListener pel){
		this.p = pApplet;
		this.serialIsReady = false;
		this.probatioListener = pel;
		this.blocks = new Vector<Block>();

		long startTime = p.millis();

		System.out.println(Serial.list());
		myPort = new Serial(p, Serial.list()[5], 115200);
		//myPort = new Serial(this, "/dev/cu.usbmodem1421", 115200);
		myPort.bufferUntil('\n');
		myPort.clear();
		System.out.println("Initializing serial port...");
		while (p.millis() - startTime < 500) {
			if (myPort.available() > 0) {
				myPort.readStringUntil('\n');
			}
		}
		System.out.println("Serial port ready!");
		serialIsReady = true;
	}

	public void update() {
		removeBlockDetector();
	}

	public void serialEvent (Serial myPort) {
		if(serialIsReady){
			try {
				byte[] meusBytes = myPort.readBytes();
				boolean isGoodFormat = (meusBytes.length == 21) && 
						(meusBytes[0] == 2) && 
						(meusBytes[meusBytes.length-1] == 10);
				if(isGoodFormat){
					int[] meusInts = new int[meusBytes.length];
					for (int i = 0; i < meusBytes.length; i++) {
						meusInts[i] = meusBytes[i] & 0xFF;
					}
					parseAndAddOrUpdate(meusInts);
					p.delay(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void parseAndAddOrUpdate(int[] ints) {
		boolean isGoodFormat = (ints.length == 21) && 
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
				Block newBlock = BlockFactory.createBlock(blockId, values, p.millis());
				addBlockEvent(newBlock);
			} 
		}						
	}

	private void removeBlockDetector(){
		try {
			for (int i = 0; i < blocks.size(); i++) {
				Block currentBlock = blocks.elementAt(i);
				if(p.millis() - currentBlock.getLastTimeUpdated() > 1000){
					//registeredRemovals.addElement(currentBlock.getId());
					removeBlockEvent(currentBlock.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addBlockEvent(Block block) {
		if (block != null) {
			if(!repositoryContainsBlock(block.getId())){
				blocks.addElement(block);
				System.out.println(block.getName() + " added");
				// ====== ADD EVENT =====

				probatioListener.addBlockEvent(block);

				// ====== ADD EVENT =====
			}
		}
	}

	private void updateBlockEvent(int idBlock, int[] values) {
		Block block = getBlockById(idBlock);
		if (block != null) {
			block.updateValues(values, p.millis());
			// ====== UPDATE EVENT ======

			probatioListener.updateBlockEvent(block);

			// ====== UPDATE EVENT ======
		}
	}

	private void removeBlockEvent(int idBlock) {
		Block block = getBlockById(idBlock);
		if (block != null) {
			String name = block.getName();
			// ====== REMOVE EVENT ======

			probatioListener.removeBlockEvent(block);

			// ====== REMOVE EVENT ======
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

}

