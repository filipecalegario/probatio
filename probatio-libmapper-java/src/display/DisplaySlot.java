package display;

import Mapper.Device.Signal;
import cooked.KinematicFactory;
import cooked.Kinematics;
import mapper.MapperManagerProbatio;
import mvc.model.BlockType;
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
	private Kinematics kinematic;
	private int idSlot;
	private Signal signal;

	public DisplaySlot(int idBlock, int idValue, PApplet processing, float x, float y, float width, float height, int background, int colorIndex, String label) {
		initialize(idBlock, idValue, processing, x, y, width, height, background, colorIndex, label);
	}
	
	public DisplaySlot(int idBlock, int idValue, int idSlot, PApplet processing, float x, float y, float width, float height, int background, int colorIndex, String label) {
		initialize(idBlock, idValue, processing, x, y, width, height, background, colorIndex, label);
		this.idSlot = idSlot;
	}

	private void initialize(int idBlock, int idValue, PApplet processing, float x, float y, float width, float height, int background, int colorIndex, String label) {
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
		this.kinematic = KinematicFactory.createKinematic(idBlock);
		//processing.image(this.icon, this.x, this.y);
		try {
			this.signal = MapperManagerProbatio.addOutput(BlockType.getBlockNameById(idBlock) + "-" + label, 1, 'i', "unit", 0, 255);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateValue(float value) {
		this.value = value;
		kinematic.updateValue(value);
		grapher.updateValue(value);
		if(this.signal != null){			
			try {
				this.signal.update((int)value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public float getSpeed() {
		return kinematic.getSpeed();
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

	public void updateDisplay(int counterXPos){
		this.grapher.updateXPos(counterXPos);
		//TODO This is eating a lot of the CPU
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

	public void clearRect(){
		processing.fill(this.background);
		processing.noStroke();
		processing.rect(this.x, this.y, this.width, this.height);
	}

	public void prepareToBeRemoved(){
		this.grapher.clearBackground();
		this.clearRect();
		if (this.signal != null) {
			try {
				MapperManagerProbatio.removeOutput(this.signal);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getIdSlot() {
		return this.idSlot;
	}

}
