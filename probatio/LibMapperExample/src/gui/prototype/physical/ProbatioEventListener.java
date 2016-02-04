package gui.prototype.physical;

import model.Block;

public interface ProbatioEventListener {

	public void addBlockEvent(Block block);
	
	public void updateBlockEvent(Block block);

	public void removeBlockEvent(Block block);
	
}
