package mvc.model;

import java.util.HashMap;

public class BlockType {

	public static final int BUFFER_SIZE = 44;
	public static final int QUANTITY_BLOCKS = 14;
	public static final int BLOCK_BELLOWS = 50;
	public static final int BLOCK_BREATH = 51;
	public static final int BLOCK_CRANK = 45;
	public static final int BLOCK_DEBUG = 99;
	public static final int BLOCK_JOYSTICK = 48;
	public static final int BLOCK_ONETAP = 52;
	public static final int BLOCK_TWOPOTS = 47;
	public static final int BLOCK_RESTOUCH = 49;
	public static final int BLOCK_THREETAPS = 53;
	public static final int BLOCK_TURNTABLE = 46;
	public static final int BLOCK_TWOBUTTONS1 = 24;
	public static final int BLOCK_TWOBUTTONS2 = 25;
	public static final int BLOCK_TWOBUTTONS3 = 26;
	public static final int BLOCK_TWOBUTTONS4 = 27;
	public static final int SIZE_BLOCK_BELLOWS = 1;
	public static final int SIZE_BLOCK_BREATH = 1;
	public static final int SIZE_BLOCK_CRANK = 2;
	public static final int SIZE_BLOCK_DEBUG = 4;
	public static final int SIZE_BLOCK_JOYSTICK = 2;
	public static final int SIZE_BLOCK_ONETAP = 1;
	public static final int SIZE_BLOCK_TWOPOTS = 2;
	public static final int SIZE_BLOCK_RESTOUCH = 2;
	public static final int SIZE_BLOCK_THREETAPS = 3;
	public static final int SIZE_BLOCK_TURNTABLE = 2;
	public static final int SIZE_BLOCK_TWOBUTTONS1 = 2;
	public static final int SIZE_BLOCK_TWOBUTTONS2 = 2;
	public static final int SIZE_BLOCK_TWOBUTTONS3 = 2;
	public static final int SIZE_BLOCK_TWOBUTTONS4 = 2;
	
	public static final int[] blocks = {BLOCK_BELLOWS, BLOCK_BREATH, BLOCK_CRANK, BLOCK_DEBUG, BLOCK_JOYSTICK, BLOCK_ONETAP, BLOCK_TWOPOTS, BLOCK_RESTOUCH, BLOCK_THREETAPS, BLOCK_TURNTABLE, BLOCK_TWOBUTTONS1, BLOCK_TWOBUTTONS2, BLOCK_TWOBUTTONS3, BLOCK_TWOBUTTONS4};
	public static final int[] sizeBlocks = {SIZE_BLOCK_BELLOWS, SIZE_BLOCK_BREATH, SIZE_BLOCK_CRANK, SIZE_BLOCK_DEBUG, SIZE_BLOCK_JOYSTICK, SIZE_BLOCK_ONETAP, SIZE_BLOCK_TWOPOTS, SIZE_BLOCK_RESTOUCH, SIZE_BLOCK_THREETAPS, SIZE_BLOCK_TURNTABLE, SIZE_BLOCK_TWOBUTTONS1, SIZE_BLOCK_TWOBUTTONS2, SIZE_BLOCK_TWOBUTTONS3, SIZE_BLOCK_TWOBUTTONS4};
	
	public static HashMap<Integer, String> blockNamesbyId;
	public static HashMap<Integer, Integer> blockSizeByID;
	
	static{
		blockNamesbyId = new HashMap<Integer, String>();
		blockSizeByID = new HashMap<Integer, Integer>();
		blockNamesbyId.put(BLOCK_BELLOWS, "FOLE");
		blockNamesbyId.put(BLOCK_BREATH, "SOPRO");
		blockNamesbyId.put(BLOCK_CRANK, "MANIVELA");
		blockNamesbyId.put(BLOCK_DEBUG, "DEBUG");
		blockNamesbyId.put(BLOCK_JOYSTICK, "JOYSTICK");
		blockNamesbyId.put(BLOCK_ONETAP, "UMA_BATIDA");
		blockNamesbyId.put(BLOCK_TWOPOTS, "POTS");
		blockNamesbyId.put(BLOCK_RESTOUCH, "DESLIZAR");
		blockNamesbyId.put(BLOCK_THREETAPS, "TRES_BATIDAS");
		blockNamesbyId.put(BLOCK_TURNTABLE, "TURNTABLE");
		blockNamesbyId.put(BLOCK_TWOBUTTONS1, "BOTOES1");
		blockNamesbyId.put(BLOCK_TWOBUTTONS2, "BOTOES2");
		blockNamesbyId.put(BLOCK_TWOBUTTONS3, "BOTOES3");
		blockNamesbyId.put(BLOCK_TWOBUTTONS4, "BOTOES4");
		for (int i = 0; i < blocks.length; i++) {
			blockSizeByID.put(blocks[i], sizeBlocks[i]);
		}
	}
	
	/* 
	 static{
		blockNamesbyId = new HashMap<Integer, String>();
		blockSizeByID = new HashMap<Integer, Integer>();
		blockNamesbyId.put(BLOCK_BELLOWS, "BELLOWS");
		blockNamesbyId.put(BLOCK_BREATH, "BREATH");
		blockNamesbyId.put(BLOCK_CRANK, "CRANK");
		blockNamesbyId.put(BLOCK_DEBUG, "DEBUG");
		blockNamesbyId.put(BLOCK_JOYSTICK, "JOYSTICK");
		blockNamesbyId.put(BLOCK_ONETAP, "ONETAP");
		blockNamesbyId.put(BLOCK_TWOPOTS, "TWOPOTS");
		blockNamesbyId.put(BLOCK_RESTOUCH, "RESTOUCH");
		blockNamesbyId.put(BLOCK_THREETAPS, "THREETAPS");
		blockNamesbyId.put(BLOCK_TURNTABLE, "TURNTABLE");
		blockNamesbyId.put(BLOCK_TWOBUTTONS1, "TWOBUTTONS1");
		blockNamesbyId.put(BLOCK_TWOBUTTONS2, "TWOBUTTONS2");
		blockNamesbyId.put(BLOCK_TWOBUTTONS3, "TWOBUTTONS3");
		blockNamesbyId.put(BLOCK_TWOBUTTONS4, "TWOBUTTONS4");
		for (int i = 0; i < blocks.length; i++) {
			blockSizeByID.put(blocks[i], sizeBlocks[i]);
		}
	}*/
	
	public static int getSizeByID(int id){
		int result = -1;
		if(blockSizeByID.containsKey(id)){
			result = blockSizeByID.get(id);
		}
		return result;
	}
	
	public static String getBlockNameById(int id){
		String result = "NONE";
		if(blockNamesbyId.containsKey(id)){
			result = blockNamesbyId.get(id);
		}
		return result;
	}

}
