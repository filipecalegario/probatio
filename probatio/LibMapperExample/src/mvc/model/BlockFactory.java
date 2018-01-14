package mvc.model;

import paraExperimentoSensores.SensorNames;
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
	
	public static Block createSensor(int id, int[] values, long millis){
		Block result = new Block(id);
		Utils utils = new Utils();
		int colorIndex = id;
		int screenIndex = id;
		int[] colors = new int[values.length];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = utils.pickAColor(colorIndex+i);
		}
		String[] labels = BlockFactory.populateSensorValuesLabels(id);
		result.setScreenGraphColor(colors);
		result.setScreenIndex(screenIndex);
		result.setValues(values);
		result.setLastTimeUpdated(millis);
		result.setValuesLabels(labels);
		result.setDataSize(values.length);
		result.setName(SensorNames.getNameFromIndex(id));
		return result;
	}
	
	private static String[] populateSensorValuesLabels(int id) {
		String[] result = {""};
		return result;
	}

	private static Block initBlock(int id, int[] values){
		Block result = new Block(id);
		Utils utils = new Utils();
		int colorIndex = 0;
		int screenIndex = 0;
		switch (id) {
		case BlockType.BLOCK_BELLOWS:
			colorIndex = 0;
			screenIndex = 0;
		break;
		case BlockType.BLOCK_BREATH:
			colorIndex = 1;
			screenIndex = 1;
		break;
		case BlockType.BLOCK_CRANK:
			colorIndex = 2;
			screenIndex = 2;
		break;
		case BlockType.BLOCK_DEBUG:
			colorIndex = 3;
			screenIndex = 3;
		break;
		case BlockType.BLOCK_JOYSTICK:
			colorIndex = 4;
			screenIndex = 4;
		break;
		case BlockType.BLOCK_ONETAP:
			colorIndex = 5;
			screenIndex = 5;
		break;
		case BlockType.BLOCK_TWOPOTS:
			colorIndex = 6;
			screenIndex = 6;
		break;
		case BlockType.BLOCK_RESTOUCH:
			colorIndex = 7;
			screenIndex = 7;
		break;
		case BlockType.BLOCK_THREETAPS:
			colorIndex = 8;
			screenIndex = 8;
		break;
		case BlockType.BLOCK_TURNTABLE:
			colorIndex = 9;
			screenIndex = 9;
		break;
		case BlockType.BLOCK_TWOBUTTONS1:
			colorIndex = 10;
			screenIndex = 10;
		break;
		case BlockType.BLOCK_TWOBUTTONS2:
			colorIndex = 11;
			screenIndex = 11;
		break;
		case BlockType.BLOCK_TWOBUTTONS3:
			colorIndex = 12;
			screenIndex = 12;
		break;
		case BlockType.BLOCK_TWOBUTTONS4:
			colorIndex = 13;
			screenIndex = 13;
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
		case BlockType.BLOCK_BELLOWS:
			result = new String[1];
			result[0] = "Aperto";
		break;
		case BlockType.BLOCK_BREATH:
			result = new String[1];
			result[0] = "Sopro";
		break;
		case BlockType.BLOCK_CRANK:
			result = new String[2];
			result[0] = "Giro";
			result[1] = "Botao";
		break;
		case BlockType.BLOCK_DEBUG:
			result = new String[4];
			result[0] = "Debug1";
			result[1] = "Debug2";
			result[2] = "Debug3";
			result[3] = "Debug4";
		break;
		case BlockType.BLOCK_JOYSTICK:
			result = new String[2];
			result[0] = "X";
			result[1] = "Y";
		break;
		case BlockType.BLOCK_ONETAP:
			result = new String[1];
			result[0] = "Batida";
		break;
		case BlockType.BLOCK_TWOPOTS:
			result = new String[2];
			result[0] = "Knob A";
			result[1] = "Knob B";
		break;
		case BlockType.BLOCK_RESTOUCH:
			result = new String[2];
			result[0] = "Posicao";
			result[1] = "Pressao";
		break;
		case BlockType.BLOCK_THREETAPS:
			result = new String[3];
			result[0] = "Batida A";
			result[1] = "Batida B";
			result[2] = "Batida C";
		break;
		case BlockType.BLOCK_TURNTABLE:
			result = new String[2];
			result[0] = "Giro";
			result[1] = "Botao";
		break;
		case BlockType.BLOCK_TWOBUTTONS1:
			result = new String[2];
			result[0] = "Botao A";
			result[1] = "Botao B";
		break;
		case BlockType.BLOCK_TWOBUTTONS2:
			result = new String[2];
			result[0] = "Botao C";
			result[1] = "Botao D";
		break;
		case BlockType.BLOCK_TWOBUTTONS3:
			result = new String[2];
			result[0] = "Botao E";
			result[1] = "Botao F";
		break;
		case BlockType.BLOCK_TWOBUTTONS4:
			result = new String[2];
			result[0] = "Botao G";
			result[1] = "Botao H";
		break;
		default:
			break;
		}
		return result;
	}
	
}
