package serial;

import mvc.model.Block;

public interface BlockParserObserver {
	
	//protected SerialParser parser;
	public abstract void onAddBlockEvent(Block block);
	public abstract void onUpdateBlockEvent(Block block);
	public abstract void onRemoveBlockEvent(Block block);

}
