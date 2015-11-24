package repository;

import java.util.ArrayList;
import java.util.HashMap;

import model.Block;

public class BlockRepository {
	
	private HashMap<Integer, Block> blocks;
	private ArrayList<Block> blocksList;
	private int index;
	
	public BlockRepository(){
		index = 0;
		this.blocksList = new ArrayList<Block>();
		this.blocks = new HashMap<Integer,Block>();
	}
	
	public void addBlock(Block toAdd){
		blocks.put(index, toAdd);
		blocksList.add(toAdd);
		index++;
	}

	public ArrayList<Block> getBlocksList() {
		return blocksList;
	}

	public HashMap<Integer, Block> getBlocks() {
		return blocks;
	}

}
