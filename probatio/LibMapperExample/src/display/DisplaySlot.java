package display;

import model.Block;
import model.BlockType;
import processing.core.PApplet;
import processing.core.PImage;
import utils.Utils;

public class DisplaySlot {

	private PApplet processing;
	private Grapher grapher;
	private PImage icon;
	private Utils utils;
	private float x;
	private float y;
	private float width;
	private float height;
	private float left;
	private float right;
	private float top;
	private float bottom;
	private int background;
	private int idBlock;
	private String label;
	private int idValue;
	private float valueLabelWidth;
	private float margin;
	private float value;

	public DisplaySlot(int idBlock, int idValue, PApplet processing, float x, float y, float width, float height, int background, int colorIndex, String label) {
		this.idBlock = idBlock;
		this.idValue = idValue;
		this.processing = processing;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.left = x;
		this.right = x + width;
		this.top = y;
		this.bottom = y + height;	
		this.background = background;
		this.label = label;
		this.utils = new Utils(processing);
		String blockNameById = BlockType.getBlockNameById(idBlock);
		this.icon = processing.loadImage(blockNameById + ".png");
		this.icon.resize(0, (int)(height));
		//this.grapher = new Grapher(processing, x+width/3, y, 2*width/3, height, background, utils.pickAColor(colorIndex));
		this.margin = 10;
		this.valueLabelWidth = 100;
		this.grapher = new Grapher(processing, x+this.icon.width+margin, y, this.width-this.icon.width-margin-margin-valueLabelWidth, height, background, utils.pickAColor(colorIndex));
	}

	public int getIdBlock() {
		return idBlock;
	}

	public int getIdValue() {
		return idValue;
	}

	public String getLabel(){
		return this.label;
	}

	public void updateDisplay(){
		processing.image(this.icon, this.x, this.y);
		grapher.updateDisplay();
		processing.fill(255);
		processing.noStroke();
		float valueLabelX = grapher.getRight()+this.margin;
		processing.rect(valueLabelX, this.y, this.valueLabelWidth, this.height);
		processing.fill(0);
		processing.text(this.value, valueLabelX + 5, this.y + this.height/2.0f);
		processing.fill(0);
		processing.text(this.label, this.grapher.getLeft() + 5, this.grapher.getBottom() - 5);
	}

	public void updateValue(int value){
		this.grapher.updateValue(value);
		this.value = value;
		//		float imgX = this.width/6 - this.icon.width/2;
		//		processing.image(this.icon, imgX, 0);
		//System.out.println(value);
	}

	public void clearRect(){
		processing.fill(this.background);
		processing.noStroke();
		processing.rect(this.x, this.y, this.width, this.height);
	}

	public void prepareToBeRemoved(){
		this.grapher.clearBackground();
		this.clearRect();
	}

}
