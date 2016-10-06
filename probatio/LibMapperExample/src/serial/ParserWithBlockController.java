package serial;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import display.DisplayManager;
import mvc.controller.BlockController;
import mvc.model.Block;
import mvc.model.BlockFactory;
import mvc.view.BlockView;
import processing.core.PApplet;

public class ParserWithBlockController{

	public int BUFFER_SIZE = 23;

	private List<BlockParserObserver> observers = new ArrayList<BlockParserObserver>();
	PApplet core;
	Vector<BlockController> blockControllers;
	DisplayManager display;

	public ParserWithBlockController(PApplet papplet) {
		this.core = papplet;
		blockControllers = new Vector<BlockController>();
	}

	public void update(byte[] meusBytes) {
		this.checkForRemoval();
		try {
			boolean isGoodFormat = (meusBytes.length == BUFFER_SIZE) && 
					(meusBytes[0] == 2) && 
					(meusBytes[meusBytes.length-1] == 10);
			if(isGoodFormat){
				int[] meusInts = new int[meusBytes.length];
				for (int i = 0; i < meusBytes.length; i++) {
					meusInts[i] = meusBytes[i] & 0xFF;
				}
				parseAndAddOrUpdate(meusInts);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkForRemoval() {
		try {
			for (int i = 0; i < blockControllers.size(); i++) {
				BlockController currentBlock = blockControllers.elementAt(i);
				if(core.millis() - currentBlock.getLastTimeUpdated() > 1000){
					removeBlockEvent(currentBlock.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseAndAddOrUpdate(int[] ints) {
		boolean isGoodFormat = (ints.length == 23) && 
				(ints[0] == 2) && 
				(ints[ints.length-1] == 10);
		if (isGoodFormat) {
			//[2, RESTOUCH, N, N, CRANK, N, N, BELLOWS, N, TURNTABLE, N,  N, DEBUG,  N,  N, FOURBUTTONS,  N,  N,  N,  N, BREATH,  N, 10]
			//[0,        1, 2, 3,     4, 5, 6,       7, 8,         9, 10, 11,   12, 13, 14,          15, 16, 17, 18, 19,     20, 21, 22]

			int[] blockRestouchValue = new int[2];
			int[] blockCrankValue = new int[2];
			int[] blockBellowsValue = new int[1];
			int[] blockTurntableValue = new int[2];
			int[] blockDebugValue = new int[2];
			int[] blockFourButtonsValue = new int[4];
			int[] blockBreathValue = new int[1];

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

			int blockFourButtonsId = ints[15];
			blockFourButtonsValue[0] = ints[16];
			blockFourButtonsValue[1] = ints[17];
			blockFourButtonsValue[2] = ints[18];
			blockFourButtonsValue[3] = ints[19];

			int blockBreathId = ints[20];
			blockBreathValue[0] = ints[21];

			parseBlock(blockRestouchId, blockRestouchValue);
			parseBlock(blockCrankId, blockCrankValue);
			parseBlock(blockBellowsId, blockBellowsValue);
			parseBlock(blockTurntableId, blockTurntableValue);
			parseBlock(blockDebugId, blockDebugValue);
			parseBlock(blockFourButtonsId, blockFourButtonsValue);
			parseBlock(blockBreathId, blockBreathValue);
		}
	}

	private void parseBlock(int blockId, int[] values){
		if (blockId != 0) {
			if (repositoryContainsBlock(blockId)) {
				//System.out.println("UPDATED Block " + BlockType.getBlockNameById(blockId));
				updateBlockEvent(blockId, values);
			} else {
				Block newBlock = BlockFactory.createBlock(blockId, values, core.millis());
				addBlockEvent(newBlock);
			} 
		}						
	}

	private void addBlockEvent(Block block) {
		if (block != null) {
			if(!repositoryContainsBlock(block.getId())){
				BlockView blockView = new BlockView();
				BlockController blockController = new BlockController(block, blockView);
				blockControllers.addElement(blockController);
				notifyAllObservers(blockController, BlockEventType.ADD);
				//System.out.println(block.getName() + " added");
			}
		}
	}

	private void updateBlockEvent(int idBlock, int[] values) {
		BlockController blockController = getBlockById(idBlock);
		if (blockController != null) {
			blockController.updateValues(values, core.millis());
			notifyAllObservers(blockController, BlockEventType.UPDATE);
		}
	}

	private void removeBlockEvent(int idBlock) {
		BlockController blockController = getBlockById(idBlock);
		if (blockController != null) {
			String name = blockController.getName();
			notifyAllObservers(blockController, BlockEventType.REMOVE);
			blockControllers.removeElement(blockController);
			//System.out.println(name + " removed");
		}
	}

	private void notifyAllObservers(BlockController blockController, BlockEventType event){
		synchronized(blockControllers){
			for (BlockParserObserver so : observers) {
				if(event.equals(BlockEventType.ADD)){
					so.onAddBlockEvent(blockController);
				} else if(event.equals(BlockEventType.UPDATE)){
					so.onUpdateBlockEvent(blockController);
				} else if(event.equals(BlockEventType.REMOVE)) {
					so.onRemoveBlockEvent(blockController);
				}
			}
		}
	}

	//TODO This is not cool!
	private boolean repositoryContainsBlock(int id) {
		boolean result = false;
		for (BlockController blockController : blockControllers) {
			if(blockController != null){
				if(blockController.getId() == id){
					result = true;
				}
			}
		}
		return result;
	}

	//TODO Two walks in the array: This is not cool!
	private BlockController getBlockById(int id) {
		BlockController result = null;
		if(repositoryContainsBlock(id)){
			for (BlockController blockController : blockControllers) {
				if(blockController.getId() == id){
					result = blockController;
				}
			}
		}
		return result;
	}

	public void attach(BlockParserObserver observer){
		observers.add(observer);
	}

}
