package display;

import processing.core.PApplet;

public class Grapher {

	private PApplet processing;
	private float xPos;
	private float bottom;
	private float top;
	private float left;
	private float right;
	private float width;
	private float height;
	private float x;
	private float y;
	private int foreground;
	private int background;
	private float pointerSpeed;
	private boolean isFirstRun;
	private float currentValue;

	public Grapher(PApplet processing, float x, float y, float width, float height, int background, int foreground) {
		this.processing = processing;
		this.xPos = x;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.left = x;
		this.right = x + width;
		this.top = y;
		this.bottom = y + height;	
		this.foreground = foreground;
		this.background = background;
		this.pointerSpeed = 1;
		this.isFirstRun = true;
	}
	
	

	public float getBottom() {
		return bottom;
	}



	public float getTop() {
		return top;
	}



	public float getLeft() {
		return left;
	}



	public float getRight() {
		return right;
	}



	public float getPointerSpeed() {
		return pointerSpeed;
	}

	public void setPointerSpeed(float pointerSpeed) {
		this.pointerSpeed = pointerSpeed;
	}
	
	public void updateValue(float value){
		this.currentValue = value;
	}

	public void updateDisplay(){
		if(isFirstRun){
			clearBackground();
			processing.noFill();
			processing.stroke(0);
			processing.strokeWeight(1);
			processing.rect(x, y, width, height);
			//processing.rect(x-5, y-5, width+10, height+10);
			isFirstRun = false;
		} else {
			float valueForScreen = PApplet.map(this.currentValue, 0, 255, bottom, top);
			//valueForScreen = PApplet.constrain(valueForScreen, bottom, top);
			processing.noFill();
			processing.stroke(this.foreground);
			processing.strokeWeight(1);
			processing.line(xPos, bottom, xPos, valueForScreen);
			processing.noFill();
			processing.stroke(0);
			processing.strokeWeight(1);
			processing.rect(x, y, width, height);
			//updateXPos();
		}
	}

	public void updateXPos(int counter){
		float mapped = PApplet.map(counter, 0, processing.width-1, left, right);
		xPos = PApplet.constrain(mapped, left, right);
//		if (xPos >= right) {
//			xPos = left;
//			clearBackground();
//		} else {
//			xPos = xPos + pointerSpeed;
//		}
	}
	
	private void updateXPos(){
		if (xPos >= right) {
			xPos = left;
			clearBackground();
		} else {
			xPos = xPos + pointerSpeed;
		}
		xPos = PApplet.constrain(xPos, left, right);
	}

	public void clearBackground() {
		processing.fill(this.background);
		processing.noStroke();
		processing.rect(x, y, width, height);
		//processing.rect(x-5, y-5, width+10, height+10);
	}

}
