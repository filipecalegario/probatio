package paraExperimentoSensores;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import mvc.controller.BlockController;
import mvc.model.Block;
import mvc.model.BlockFactory;
import mvc.view.BlockView;
import processing.core.PApplet;
import serial.BlockEventType;
import serial.BlockParserObserver;

public class SensoresParserWithBlockController {
	
	public int BUFFER_SIZE = 50;

	private List<BlockParserObserver> observers = new ArrayList<BlockParserObserver>();
	PApplet core;
	Vector<BlockController> blockControllers;

	public SensoresParserWithBlockController(PApplet papplet) {
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
	
	private void parseAndAddOrUpdate(int[] ints) {
		boolean isGoodFormat = (ints.length == BUFFER_SIZE) && 
				(ints[0] == 2) && 
				(ints[ints.length-1] == 10);
		if (isGoodFormat) {
			int[] trimmed = new int[48];
			for (int i = 1; i <= 48; i++) {
				trimmed[i-1] = ints[i];
			}
			for (int i = 0; i < trimmed.length/2; i++) {
				int status = trimmed[i*2];
				int value = trimmed[i*2+1];
				int[] values = {value};
				int id = i;
				parseBlock(status, id, values);
//				if(status == 1){
//					if(sensors.containsKey(id)){
//						Sensor s = sensors.get(id);
//						s.updateValueSignal(value);
//					} else {
//						String nameFromIndex = SensorNames.getNameFromIndex(id);
//						Signal signal = ExperimentoMapper.addOutput(nameFromIndex, 1, 'i', "unit", 0, 255);
//						Sensor s = new Sensor(signal, nameFromIndex, value);
//						sensors.put(id, s);
//					}
//				} else {
//					if(sensors.containsKey(id)){
//						Sensor s = sensors.get(id);
//						ExperimentoMapper.removeOutput(s.getSignal());
//						sensors.remove(id);
//					}
//				}
			}
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
	
	private void parseBlock(int status, int blockId, int[] values){
		if (status != 0) {
			if (repositoryContainsBlock(blockId)) {
				//System.out.println("UPDATED Block " + BlockType.getBlockNameById(blockId));
				updateBlockEvent(blockId, values);
			} else {
				Block newBlock = BlockFactory.createSensor(blockId, values, core.millis());
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
