package old;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import display.DisplayManager;
import model.Block;
import model.BlockType;
import processing.core.PApplet;

public class TestDisplayManagerMain extends PApplet {
	
	DisplayManager display;
	int counter = 0;
	Block block1;
	Block block2;
	Block block3;
	Block block4;
	Block block5;

	@Override
	public void settings() {
		size(800,600);
		pixelDensity(2);
	}

	@Override
	public void setup() {
		display = new DisplayManager(this,6);
		int[] values = {123};
		int[] values2 = {21, 22};
		block1 = new Block(BlockType.NONE, values, millis());
		block2 = new Block(BlockType.RESTOUCH, values2, millis());
		block3 = new Block(BlockType.BUTTON, values, millis());
		block4 = new Block(BlockType.TURNTABLE, values, millis());
		block5 = new Block(BlockType.BELLOWS, values, millis());
		//display.addDisplaySlot(block1);
		//display.addDisplaySlot(block2);
	}

	@Override
	public void draw() {
		//background(255);
		float mySin = sin((counter * PI / 180));
		float sinToGrapher = map(mySin, -1, 1, 0, 255);
		int[] values = new int[1];
		values[0] = (int) sinToGrapher;
		block1.updateValues(values, millis());
		int[] values2 = new int[2];
		values2[0] = (int)map(sinToGrapher,0,255,255,0);
		values2[1] = (int)map(sinToGrapher,0,255,127,0);
		block2.updateValues(values2, millis());
		//println(Arrays.toString(block1.getValues()));
		display.updateValueDisplaySlot(block1.getId(), 0, block1.getValues()[0]);
		display.updateValueDisplaySlot(block2.getId(), 0, block2.getValues()[0]);
		display.updateValueDisplaySlot(block2.getId(), 1, block2.getValues()[1]);
		counter = counter + 5;
		display.updateDrawDisplaySlot();
	}
	
	@Override
	public void keyPressed(processing.event.KeyEvent event) {
		switch (key) {
		case 'z':
			display.removeDisplaySlot(block1.getId(), 0);
			break;
		case 'x':
			display.removeDisplaySlot(block2.getId(), 0);
			display.removeDisplaySlot(block2.getId(), 1);
			break;
		case 'c':
			display.removeDisplaySlot(block3.getId(), 0);
			break;
		case 'v':
			display.removeDisplaySlot(block4.getId(), 0);
			break;
		case 'b':
			display.removeDisplaySlot(block5.getId(), 0);
			break;
		case 'q':
			display.addDisplaySlot(block1.getId(), 0, "Spinning");
			break;
		case 'w':
			display.addDisplaySlot(block2.getId(), 0, "Pressing");
			display.addDisplaySlot(block2.getId(), 1, "Selecting");
			break;
		case 'e':
			display.addDisplaySlot(block3.getId(), 0, "Switching");
			break;
		case 'r':
			display.addDisplaySlot(block4.getId(), 0, "Turning");
			break;
		case 't':
			display.addDisplaySlot(block5.getId(), 0, "Pressing");
			break;
		case 'a':
			display.updateValueDisplaySlot(block1.getId(), 0, 0);
			break;
		case 's':
			display.updateValueDisplaySlot(block2.getId(), 0, 0);
			display.updateValueDisplaySlot(block2.getId(), 1, 0);
			break;
		case 'd':
			display.updateValueDisplaySlot(block3.getId(), 0, 0);
			break;
		case 'f':
			display.updateValueDisplaySlot(block4.getId(), 0, 0);
			break;
		case 'g':
			display.updateValueDisplaySlot(block5.getId(), 0, 0);
			break;
		case ' ':
			background(255);
			break;
		default:
			break;
		}
	}
	
	public static void main(String[] args) {
		PApplet.main(TestDisplayManagerMain.class.getName());
	}
	
	

}
