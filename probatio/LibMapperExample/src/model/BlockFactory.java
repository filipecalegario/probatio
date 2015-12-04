package model;

public class BlockFactory {
	
	public static Block createBlock(int id, int[] values, long millis){
		String[] labels = BlockFactory.populateValuesLabels(id);
		Block block = new Block(id, values, millis, labels);
		return block;
	}
	
	private static String[] populateValuesLabels(int id){
		String[] result = null;
		switch (id) {
		case BlockType.BELLOWS:	
			result = new String[1];
			result[0] = "Pushing";
			break;
		case BlockType.CRANK:	
			result = new String[2];
			result[0] = "Turning";
			result[1] = "Pressing Button";
			break;
		case BlockType.RESTOUCH:	
			result = new String[2];
			result[0] = "Pressing";
			result[1] = "Selecting";
			break;
		case BlockType.BUTTON:	
			result = new String[1];
			result[0] = "Pressing Button";
			break;
		case BlockType.TURNTABLE:	
			result = new String[2];
			result[0] = "Turning";
			result[1] = "Pressing button";
			break;
		case BlockType.DEBUG:	
			result = new String[2];
			result[0] = "Debug1";
			result[1] = "Debug2";
			break;
		default:
			break;
		}
		return result;
	}
	
}
