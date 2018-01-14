package serial;

import mvc.controller.BlockController;

public interface BlockParserObserver {
	
	//protected SerialParser parser;
	public abstract void onAddBlockEvent(BlockController blockController);
	public abstract void onUpdateBlockEvent(BlockController blockController);
	public abstract void onRemoveBlockEvent(BlockController blockController);

}
