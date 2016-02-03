package gui.prototype.physical;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ControllerView;
import controlP5.Group;
import controlP5.Pointer;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class PhysicalGUI extends PApplet{

	private State state;
	private ControlP5 runControlP5;
	private ControlP5 addBlockControlP5;
	private ControlP5 mainControlP5;
	private Map<BlockIconButton, BlockIconButton> connections;

	private PImage bg;

	private BlockIconButton origin;
	private BlockIconButton destination;

	private Button btn;

	private boolean isAboutToMakeAConnection;

	public void settings() {
		size(800, 600);
		//pixelDensity(2);
		smooth();
	}

	public void setup() {
		//bg = loadImage("gui/Base3x3-1600x1200.png");
		changeState(State.RUN);
		connections = new HashMap<PhysicalGUI.BlockIconButton, PhysicalGUI.BlockIconButton>();
		bg = loadImage("gui/Base3x3-800x600.png");

		mainControlP5Setup();
		runControlP5Setup();
		addBlockControlP5Setup();
	}

	private void mainControlP5Setup() {
		mainControlP5 = new ControlP5(this);
		mainControlP5.setAutoDraw(false);
		btn = mainControlP5.addButton("colorA")
				.setValue(0)
				.setPosition(0,0)
				.setSize(200,19)
				.setLabel("Current State: " + state);
	}

	private void runControlP5Setup() {
		runControlP5 = new ControlP5(this);
		runControlP5.setAutoDraw(false);
		runControlP5.enableShortcuts();
		Group g3x3 = runControlP5.addGroup("g3x3");
		BlockIconButton[] mbs = new BlockIconButton[9];
		for (int i = 0; i < mbs.length; i++) {
			mbs[i] = new BlockIconButton(runControlP5, "b"+i, "bellows");
			mbs[i].setSize(71,71);
			mbs[i].setGroup(g3x3);
		}
		mbs[0].setPosition(251.175f, 150.99f);
		mbs[1].setPosition(364.575f, 150.99f);
		mbs[2].setPosition(477.975f, 150.99f);
		mbs[3].setPosition(251.175f, 264.39f);
		mbs[4].setPosition(364.575f, 264.39f);
		mbs[5].setPosition(477.975f, 264.39f);
		mbs[6].setPosition(251.175f, 377.79f);
		mbs[7].setPosition(364.575f, 377.79f);
		mbs[8].setPosition(477.975f, 377.79f);
		OutputObject oo = new OutputObject(runControlP5, "output", "");
		oo.setSize(71,71);
		oo.setPosition(30,30);
	}

	private void addBlockControlP5Setup() {
		addBlockControlP5 = new ControlP5(this);
		addBlockControlP5.setAutoDraw(false);
		ButtonToAddBlock[] array = new ButtonToAddBlock[9];
		for (int i = 0; i < array.length; i++) {
			array[i] = new ButtonToAddBlock(addBlockControlP5, "add"+i, "");
			array[i].setSize(71,71);
		}
		array[0].setPosition(251.175f, 150.99f);
		array[1].setPosition(364.575f, 150.99f);
		array[2].setPosition(477.975f, 150.99f);
		array[3].setPosition(251.175f, 264.39f);
		array[4].setPosition(364.575f, 264.39f);
		array[5].setPosition(477.975f, 264.39f);
		array[6].setPosition(251.175f, 377.79f);
		array[7].setPosition(364.575f, 377.79f);
		array[8].setPosition(477.975f, 377.79f);
	}

	public void draw() {
		background(255);
		//System.out.println("The state is: " + state);
		image(bg,0,0);
		mainControlP5.draw();
		if(state == State.RUN){
			runControlP5.draw();
			displayConnections();
		} else if(state == State.ADDBLOCK){
			addBlockControlP5.draw();
		}
		if(isAboutToMakeAConnection){
			stroke(255, 0, 0);
			line(origin.getMiddlePoint().x, origin.getMiddlePoint().y, mouseX, mouseY);
		}
		//		switch (state) {
		//		case RUN:
		//			runControlP5.setVisible(true);
		//			runControlP5.draw();
		//			displayConnections();
		//			break;
		//		case ADDBLOCK:
		//			//runControlP5.setVisible(false);
		//			//runControlP5.setUpdate(false);
		//			runControlP5.getGroup("g3x3").hide();
		//			addBlockControlP5.draw();
		//			break;
		//		default:
		//			break;
		//		}

	}

	public void makeAConnection(BlockIconButton origin, BlockIconButton destination){
		if(origin != null && destination != null){
			connections.put(origin, destination);
			System.out.println("connection made: " + origin + " -> " + destination);
			isAboutToMakeAConnection = false;
		}
	}

	public void displayConnections(){
		for (Entry<BlockIconButton, BlockIconButton> entry : connections.entrySet()) {
			stroke(255,0,0);
			strokeWeight(5);
			BlockIconButton _origin = entry.getKey();
			BlockIconButton _dest = entry.getValue();
			PVector destinationPoint = _dest.getMiddlePoint();
			PVector originPoint = _origin.getMiddlePoint();
			line(originPoint.x,originPoint.y, destinationPoint.x, destinationPoint.y);
			noStroke();
			fill(0);
			ellipse(destinationPoint.x, destinationPoint.y, 20, 20);
			//line(_origin.getPosition()[0],_origin.getPosition()[1], _dest.getPosition()[0], _dest.getPosition()[1]);			
		}
	}

	public void colorA(int theValue) {
		if(state == State.RUN){
			changeState(State.ADDBLOCK);
		} else if(state == State.ADDBLOCK){
			changeState(State.RUN);
		}
	}

	public void changeState(State newState){
		state = newState;
		if(btn != null){
			btn.setLabel("current state: " + state);
		}
		System.out.println(state);
	}

	//	void controlEvent(ControlEvent theEvent) {
	//		System.out.println("something");
	//		if(theEvent.isController()){
	//			System.out.println(theEvent.getController().getName());
	//		}
	//	}

	public static void main(String[] args) {
		PApplet.main(PhysicalGUI.class.getName());
	}


	class BlockIconButton extends Controller {

		int current = 0xffff0000;
		PImage icon;

		float a = 128;

		float na;

		int y;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		BlockIconButton(ControlP5 cp5, String theName, String iconName) {
			super(cp5, theName);
			if(!iconName.equals("")){
				this.icon = loadImage("gui/"+iconName+".png");
			}

			// replace the default view with a custom view.
			setView(new ControllerView() {
				public void display(PGraphics p, Object b) {
					na += (a-na) * 0.1f; 
					p.fill(current,na);
					p.rect(0, 0, getWidth(), getHeight());
					ellipseMode(CENTER);
					p.imageMode(CENTER);
					if(icon != null){
						p.image(icon,getAbsoluteMiddlePoint().x, getAbsoluteMiddlePoint().y);
					}
					p.ellipse(getAbsoluteMiddlePoint().x, getAbsoluteMiddlePoint().y, 10, 10);
				}
			}
					);
		}

		public PVector getAbsoluteMiddlePoint(){
			PVector middlePoint = new PVector();
			middlePoint.set(getAbsolutePosition()[0]+this.getWidth()/2.0f, getAbsolutePosition()[1]+this.getHeight()/2.0f);
			return middlePoint;
		}

		public PVector getMiddlePoint(){
			PVector middlePoint = new PVector();
			middlePoint.set(getPosition()[0]+this.getWidth()/2.0f, getPosition()[1]+this.getHeight()/2.0f);
			return middlePoint;
		}

		// override various input methods for mouse input control
		public void onEnter() {
			cursor(HAND);
			//isAboutToMakeAConnection = false;
			if(isAboutToMakeAConnection){
				destination = this;
				makeAConnection(origin, destination);
			}
			println("enter: " + getName());
		}

		public void onScroll(int n) {
			//println("scrolling");
		}

		public void onPress() {
			//println("press");
		}

		public void onClick() {
			//		    Pointer p1 = getPointer();
			//		    println("clicked at "+p1.x()+", "+p1.y());
		}

		public void onRelease() {
			//println("release");
		}

		public void onMove() {
			//println("moving "+this+" "+_myControlWindow.getMouseOverList());
		}

		public void onDrag() {
			//current = 0xff0000ff;
			//			stroke(255, 0, 0);
			//			line(getMiddlePoint().x, getMiddlePoint().y, mouseX, mouseY);
			isAboutToMakeAConnection = true;
			origin = this;
			//setPosition(mouseX, mouseY);
			//setPosition(mouseX-getWidth()/2.0f, mouseY-getHeight()/2.0f);
			//println("dragging at "+p1.x()+", "+p1.y()+" "+dif);
		}

		public void onReleaseOutside() {
			//			if(isAboutToMakeAConnection && destination == null){
			//				connections.remove(origin);
			//				origin = null;
			//				isAboutToMakeAConnection = false;
			//			}
			//println("release outside: " + getName());
			//isAboutToMakeAConnection = false;
		}

		public void onLeave() {
			//println("leave");
			cursor(ARROW);
			//a = 128;
		}

		@Override
		protected void onEndDrag() {
			//println("end drag");
		}

		@Override
		protected void onStartDrag() {
			//println("start drag");
		}

	}

	class OutputObject extends Controller{
		PImage icon;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		OutputObject(ControlP5 cp5, String theName, String iconName) {
			super(cp5, theName);
			if(!iconName.equals("")){
				this.icon = loadImage("gui/"+iconName+".png");
			}

			// replace the default view with a custom view.
			setView(new ControllerView() {
				public void display(PGraphics p, Object b) {
					p.fill(0,0,255,50);
					p.rect(0, 0, getWidth(), getHeight());
					ellipseMode(CENTER);
					p.imageMode(CENTER);
					if(icon != null){
						p.image(icon,getAbsoluteMiddlePoint().x, getAbsoluteMiddlePoint().y);
					}
					p.ellipse(getAbsoluteMiddlePoint().x, getAbsoluteMiddlePoint().y, 10, 10);
				}
			}
					);
		}
		public PVector getAbsoluteMiddlePoint(){
			PVector middlePoint = new PVector();
			middlePoint.set(getAbsolutePosition()[0]+this.getWidth()/2.0f, getAbsolutePosition()[1]+this.getHeight()/2.0f);
			return middlePoint;
		}

		public PVector getMiddlePoint(){
			PVector middlePoint = new PVector();
			middlePoint.set(getPosition()[0]+this.getWidth()/2.0f, getPosition()[1]+this.getHeight()/2.0f);
			return middlePoint;
		}
		
		@Override
		protected void onEnter() {
			System.out.println("Output Block enter");
		}
	}

	class ButtonToAddBlock extends Controller {

		PImage icon;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ButtonToAddBlock(ControlP5 cp5, String theName, String iconName) {
			super(cp5, theName);
			if(!iconName.equals("")){
				this.icon = loadImage("gui/"+iconName+".png");
			}

			// replace the default view with a custom view.
			setView(new ControllerView() {
				public void display(PGraphics p, Object b) {
					p.fill(255,0,0,50);
					p.rect(0, 0, getWidth(), getHeight());
					ellipseMode(CENTER);
					p.imageMode(CENTER);
					if(icon != null){
						p.image(icon,getAbsoluteMiddlePoint().x, getAbsoluteMiddlePoint().y);
					}
					p.ellipse(getAbsoluteMiddlePoint().x, getAbsoluteMiddlePoint().y, 10, 10);
				}
			}
					);
		}

		public PVector getAbsoluteMiddlePoint(){
			PVector middlePoint = new PVector();
			middlePoint.set(getAbsolutePosition()[0]+this.getWidth()/2.0f, getAbsolutePosition()[1]+this.getHeight()/2.0f);
			return middlePoint;
		}

		public PVector getMiddlePoint(){
			PVector middlePoint = new PVector();
			middlePoint.set(getPosition()[0]+this.getWidth()/2.0f, getPosition()[1]+this.getHeight()/2.0f);
			return middlePoint;
		}

		// override various input methods for mouse input control
		public void onEnter() {
			cursor(HAND);
			//isAboutToMakeAConnection = false;
		}

		public void onPress() {
			System.out.println("pressed on: " + getName());
		}

		public void onClick() {
			System.out.println("clicked on: " + getName());
		}

		public void onRelease() {
			changeState(State.RUN);
		}

		public void onLeave() {
			//println("leave");
			cursor(ARROW);
			//a = 128;
		}

	}
}