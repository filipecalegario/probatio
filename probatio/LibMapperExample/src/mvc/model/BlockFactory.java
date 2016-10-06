package mvc.model;

import utils.Utils;

public class BlockFactory {
	
	public static Block createBlock(int id, int[] values, long millis){
		String[] labels = BlockFactory.populateValuesLabels(id);
		Block block = initBlock(id, values);
		block.setValues(values);
		block.setLastTimeUpdated(millis);
		block.setValuesLabels(labels);
		block.setDataSize(values.length);
		//Block block = new Block(id, values, millis, labels);
		return block;
	}
	
	private static Block initBlock(int id, int[] values){
		Block result = new Block(id);
		Utils utils = new Utils();
		int colorIndex = 0;
		int screenIndex = 0;
		switch (id) {
		case BlockType.BELLOWS:	
			colorIndex = 0;
			screenIndex = 0;
			break;
		case BlockType.CRANK:	
			colorIndex = 1;
			screenIndex = 1;
			break;
		case BlockType.RESTOUCH:	
			colorIndex = 2;
			screenIndex = 2;
			break;
		case BlockType.BUTTON:	
			colorIndex = 8;
			screenIndex = 8;
			break;
		case BlockType.FOURBUTTONS:	
			colorIndex = 3;
			screenIndex = 3;
			break;
		case BlockType.TURNTABLE:	
			colorIndex = 4;
			screenIndex = 4;
			break;
		case BlockType.DEBUG:	
			colorIndex = 5;
			screenIndex = 5;
			break;
		case BlockType.BREATH:	
			colorIndex = 6;
			screenIndex = 6;
			break;
		default:
			break;
		}
		int[] colors = new int[values.length];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = utils.pickAColor(colorIndex+i);
		}
		result.setScreenGraphColor(colors);
		result.setScreenIndex(screenIndex);
		return result;
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
		case BlockType.FOURBUTTONS:	
			result = new String[4];
			result[0] = "Pressing Button 1";
			result[1] = "Pressing Button 2";
			result[2] = "Pressing Button 3";
			result[3] = "Pressing Button 4";
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
		case BlockType.BREATH:	
			result = new String[1];
			result[0] = "Breathing";
			break;
		default:
			break;
		}
		return result;
	}
	
}
