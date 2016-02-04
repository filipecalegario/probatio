package gui.prototype.physical;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Controller;
import controlP5.ControllerView;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class PhysicalGUI extends PApplet{

	private State state;
	private ControlP5 runControlP5;
	private ControlP5 addBlockControlP5;
	private ControlP5 mainControlP5;
	private Map<BlockPlacedIcon, OutputObject> connections;
	private PVector[] positions;
	private BlockNotPlaced selected;
	private Vector<BlockNotPlaced> notPlaced;

	private PImage bg;

	private BlockPlacedIcon origin;
	private OutputObject destination;

	private Button btn;

	private boolean isAboutToMakeAConnection;

	public void settings() {
		size(800, 600);
		//pixelDensity(2);
		smooth();
	}

	public void setup() {
		//bg = loadImage("gui/Base3x3-1600x1200.png");
		notPlaced = new Vector<BlockNotPlaced>();
		changeState(State.RUN);
		connections = new HashMap<PhysicalGUI.BlockPlacedIcon, PhysicalGUI.OutputObject>();
		bg = loadImage("gui/Base3x3-800x600.png");
		
		positions = new PVector[9];
		positions[0] = new PVector(251.175f, 150.99f);
		positions[1] = new PVector(364.575f, 150.99f);
		positions[2] = new PVector(477.975f, 150.99f);
		positions[3] = new PVector(251.175f, 264.39f);
		positions[4] = new PVector(364.575f, 264.39f);
		positions[5] = new PVector(477.975f, 264.39f);
		positions[6] = new PVector(251.175f, 377.79f);
		positions[7] = new PVector(364.575f, 377.79f);
		positions[8] = new PVector(477.975f, 377.79f);

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
		mainControlP5.addButton("addNewBlock")
		.setValue(0)
		.setPosition(200,0)
		.setSize(200,19)
		.setLabel("Add block ");
	}

	private void runControlP5Setup() {
		runControlP5 = new ControlP5(this);
		runControlP5.setAutoDraw(false);
		runControlP5.enableShortcuts();
		runControlP5.addGroup("g3x3");
		BlockPlacedIcon[] mbs = new BlockPlacedIcon[9];
		for (int i = 0; i < mbs.length; i++) {
//			mbs[i] = new BlockIconButton(runControlP5, "b"+i, "bellows");
//			mbs[i].setSize(71,71);
//			mbs[i].setGroup("g3x3");
//			mbs[i].setPosition(positions[i].x,positions[i].y);
		}
		
		OutputObject oo = new OutputObject(runControlP5, "output", "");
		oo.setSize(71,71);
		oo.setPosition(30,30);
		oo.setLabel("OUTPUT1");
	}

	private void addBlockControlP5Setup() {
		addBlockControlP5 = new ControlP5(this);
		addBlockControlP5.setAutoDraw(false);
		ButtonToAddBlock[] array = new ButtonToAddBlock[9];
		for (int i = 0; i < array.length; i++) {
			array[i] = new ButtonToAddBlock(addBlockControlP5, "add"+i, "", i);
			array[i].setSize(71,71);
			array[i].setPosition(positions[i].x,positions[i].y);
		}
		BlockNotPlaced bnp = new BlockNotPlaced(addBlockControlP5, "ouo", "bellows");
		bnp.setSize(71,71);
		bnp.setPosition(30,80);
		notPlaced.addElement(bnp);
	}

	public void draw() {
		background(255);
		//System.out.println("The state is: " + state);
		image(bg,0,0);
		mainControlP5.draw();
		if(state == State.RUN){
			runControlP5.draw();
			displayConnections();
			selected = null;
			if(!notPlaced.isEmpty()){
				changeState(State.ADDBLOCK);
			}
		} else if(state == State.ADDBLOCK){
			addBlockControlP5.draw();
			if(notPlaced.isEmpty()){
				changeState(State.RUN);
			}
		}
		if(isAboutToMakeAConnection){
			stroke(255, 0, 0);
			strokeWeight(5);
			line(origin.getMiddlePoint().x, origin.getMiddlePoint().y, mouseX, mouseY);
		}
		if(selected != null){
			stroke(255,0,0);
			strokeWeight(3);
			noFill();
			rect(selected.getPosition()[0], selected.getPosition()[1], selected.getWidth(), selected.getHeight());
		}
	}

	public void makeAConnection(BlockPlacedIcon origin, OutputObject destination){
		if(origin != null && destination != null){
			connections.put(origin, destination);
			System.out.println("connection made: " + origin + " -> " + destination);
			isAboutToMakeAConnection = false;
		}
	}

	public void displayConnections(){
		for (Entry<BlockPlacedIcon, OutputObject> entry : connections.entrySet()) {
			stroke(255,0,0);
			strokeWeight(5);
			BlockPlacedIcon _origin = entry.getKey();
			OutputObject _dest = entry.getValue();
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
	
	public void addNewBlock(int theValue) {
		BlockNotPlaced bnp = new BlockNotPlaced(addBlockControlP5, "ouo"+Math.random(), "turntable");
		bnp.setSize(71,71);
		bnp.setPosition(30,80);
		notPlaced.addElement(bnp);
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


	class BlockPlacedIcon extends Controller {

		int current = 0xffff0000;
		PImage icon;

		float a = 128;

		float na;

		int y;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		BlockPlacedIcon(ControlP5 cp5, String theName, String iconName) {
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
			if(connections.containsKey(this)){
				connections.remove(this);
			}
			if(isAboutToMakeAConnection){
				isAboutToMakeAConnection = false;
			}
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

		@Override
		protected void onDoublePress() {
			if(connections.containsKey(this)){
				connections.remove(this);
			}
			runControlP5.remove(getName());
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
			if(isAboutToMakeAConnection){
				destination = this;
				makeAConnection(origin, destination);
			}
			System.out.println("Output Block enter");
		}
	}

	class ButtonToAddBlock extends Controller {

		PImage icon;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ButtonToAddBlock(ControlP5 cp5, String theName, String iconName, int value) {
			super(cp5, theName);
			this.setValue(value);
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
			int clickedPosition = (int)getValue();
			System.out.println("clicked on: " + getName() + " " + clickedPosition);
			if(selected != null){
				BlockPlacedIcon bib = new BlockPlacedIcon(runControlP5, "b"+clickedPosition, selected.getIcon());
				bib.setSize(71,71);
				bib.setGroup("g3x3");
				bib.setPosition(positions[clickedPosition].x,positions[clickedPosition].y);
				notPlaced.remove(selected);
				addBlockControlP5.remove(selected.getName());
			}
			
		}

		public void onRelease() {
			//changeState(State.RUN);
		}

		public void onLeave() {
			//println("leave");
			cursor(ARROW);
			//a = 128;
		}

	}
	
	class BlockNotPlaced extends Controller {

		PImage icon;
		String myIconName;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		BlockNotPlaced(ControlP5 cp5, String theName, String iconName) {
			super(cp5, theName);
			myIconName = iconName;
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

		public String getIcon() {
			// TODO Auto-generated method stub
			return myIconName;
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
			//System.out.println("pressed on: " + getName());
		}

		public void onClick() {
			selected = this;
		}

		public void onRelease() {
			//changeState(State.RUN);
		}

		public void onLeave() {
			//println("leave");
			cursor(ARROW);
			//a = 128;
		}

	}
}