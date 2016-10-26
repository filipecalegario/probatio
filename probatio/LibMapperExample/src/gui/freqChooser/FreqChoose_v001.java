package gui.freqChooser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jfugue.theory.Note;

import Mapper.Device;
import Mapper.Device.Signal;
import Mapper.InputListener;
import Mapper.PropertyValue;
import Mapper.TimeTag;
import controlP5.CColor;
import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import controlP5.Numberbox;
import controlP5.ScrollableList;
import controlP5.Textlabel;
import mapper.MapperManagerProbatio;
import processing.core.PApplet;
import processing.core.PVector; 

public class FreqChoose_v001 extends PApplet implements CallbackListener{

	final Device mapper = new Device("FreqChooser", 9001);
	ControlP5 cp5;
	PVector[] rectPos = {new PVector(175.33f,200),
			new PVector(239.52f,200),
			new PVector(303.71f,200),
			new PVector(367.9f,200),
			new PVector(432.1f,200),
			new PVector(496.29f,200),
			new PVector(560.48f,200),
			new PVector(624.67f,200),
	};
	int slotGeral = 0;
	float slotFreq = 0;
	
	Textlabel[] notesLabels = new Textlabel[8];
	String[] notesToScreen = new String[8];
	Numberbox[] slots = new Numberbox[8];
	
	Note[] notes;
	Note rootNote = new Note(60);
	String scaleName;
	String octaveName;
	
	Numberbox slotSaida;
	Numberbox slotGeralEntrada;
	
	Signal outputFreqSignal;
	double lastFreq;
	
	int index = 0;

	public void setup() {
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				mapper.free();
			}
		});
		smooth();
		cp5 = new ControlP5(this);
		//		List<String> escalas = Arrays.asList("a", "b", "c", "a1", "b2", "c3");
		List<String> escalas = Scales.getScalesNames();
		/* add a ScrollableList, by default it behaves like a DropdownList */
		cp5.addScrollableList("escala")
		.setPosition(501, 237)
		.setSize(170, 100)
		.setBarHeight(20)
		.setItemHeight(20)
		.addItems(escalas)
		//.setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
		.setOpen(false);
		;
		addNotaPrincipal();
		addOitava();
		boolean lock = false;
		
		for (int i = 0; i < rectPos.length; i++) {
			//rect(rectPos[i].x, rectPos[i].y - 56, 51.834f, 20);
			String theName = "slot" + (i+1);
			slots[i] = cp5.addNumberbox(theName)
			.setPosition(rectPos[i].x - (52f/2f),rectPos[i].y - 56 - 10)
			.setRange(0, 255)
			.setSize(52,20)
			.setLock(lock)
			.setValue(0)
			.setCaptionLabel("")
			//.setDirection(Controller.HORIZONTAL)
			;
			cp5.addTextlabel("label"+"_slot" + (i+1))
			.setText(theName)
			.setPosition(rectPos[i].x - (52f/2f),rectPos[i].y - 56 - 10 -16)
			.setColor(color(0))
			;
			notesLabels[i] = cp5.addTextlabel("noteLabel"+i)
			.setText("")
			.setColor(color(0))
			.setFont(createFont("Consolas", 18))
			.setPosition(rectPos[i].x-7,rectPos[i].y-7)
			;
			createInputSlot(theName);
			//notesLabels[i].setPosition(rectPos[i].x - notesLabels[i].getWidth()/2.0f,rectPos[i].y - notesLabels[i].getHeight()/2.0f);
		}
		
		slotGeralEntrada = cp5.addNumberbox("slotGeral")
				.setPosition(92.83f - (52f/2f),200.00f - 10)
				.setRange(0, 255)
				.setSize(52,20)
				.setLock(lock)
				.setValue(0)
				.setCaptionLabel("")
				;
		
		createInputSlotGeral();
		
		slotSaida = cp5.addNumberbox("slotFreq")
		.setPosition(706.83f - (52f/2f),200 - 10)
		.setSize(52,20)
		.setLock(true)
		.setValue(0)
		.setCaptionLabel("")
		.onChange(this);
		;
		
		createOutputSlotSaida();
		
		//======= LABELS ===========
		cp5.addTextlabel("label"+"_slotGeral")
		.setText("slot geral")
		.setPosition(92.83f - (52f/2f),200.00f - 10 - 16)
		;
		cp5.addTextlabel("label"+"_slotFreq")
		.setText("freq saida")
		.setPosition(706.83f - (52f/2f),200 - 10 - 16)
		;
		cp5.addTextlabel("label"+"_entradas")
		.setText("ENTRADAS")
		.setPosition(66.917f,130.824f)
		;
		cp5.addTextlabel("label"+"_saidas")
		.setText("SAIDA")
		.setPosition(680.917f,130.824f)
		;
		initMapper();
	}

	public void draw() {
		mapper.poll(100);
		//slotSaida.setValue(slotGeral);
		background(255);
		rectMode(CORNER);
		fill(255,0,0,70);
		noStroke();
		rect(57.957f,113.98f,616.39f,149.214f);
		fill(0,0,255,70);
		noStroke();
		rect(674.348f,113.98f,67.272f,149.214f);
		rectMode(CENTER);
		float rectWidth = 58;
		float rectHeight = 58;
		float rectRadius = 14;
		fill(255);
		stroke(0);
		strokeWeight(1.0f);
		rect(400.000f,211.097f,548.695f,104.193f);
		for (int i = 0; i < rectPos.length; i++) {
			rect(rectPos[i].x, rectPos[i].y, rectWidth, rectHeight, rectRadius);
			//rect(rectPos[i].x, rectPos[i].y - 56, 51.834f, 20);
		}
		stroke(255,0,0);
		strokeWeight(3.0f);
		//		int index = Math.round(map(mouseX, 0, width, 0, 7));
		boolean b = false;
		//====
		for (int i = 0; i < slots.length; i++) {
			float value = slots[i].getValue();
			boolean currentTest = value > 127.0f;
			b = b || currentTest;
			if(currentTest){
				index = i;
			} 
		}
		if(b == false){
			index = Math.round(map(slotGeral, 0, 255, 0, 7));
		}
		//===
		//println(" =========== I:" + index);
		drawSelectionRect(rectPos, index);
		
		if(notes != null){
			for (int i = 0; i < notesToScreen.length; i++) {
				byte value = notes[i%notes.length].getValue();
				notesToScreen[i] = Note.getToneString(value);
				notesLabels[i].setText(notesToScreen[i]);
				if(i == index){
					double freq = Note.getFrequencyForNote(value);
					if(freq != lastFreq){
						slotSaida.setValue((float) freq);
						lastFreq = freq;
					}
				}
			}
		} 
	}

	public void drawSelectionRect(PVector[] positions, int index){
		noFill();
		if(index <= (positions.length - 1)){
			rect(positions[index].x, positions[index].y, 61.382f, 61.382f, 14);
		}
	}

	public void updateNotes(){
		if(rootNote != null && scaleName != null && octaveName != null){
			int octave = Integer.parseInt(octaveName);
			String tone = Note.getToneStringWithoutOctave(rootNote.getValue());
			String newTone = tone + octave;
			println(newTone);
			Note newNote = new Note(newTone);
			rootNote = newNote;
			notes = Scales.getNotesFromRootAndIntervalName(rootNote.getValue(), scaleName);
		}
	}

	public void addNotaPrincipal() {
		List<String> l1 = Arrays.asList("C","C#","D","D#","E","F","F#","G", "G#","A", "A#", "B");
		cp5.addScrollableList("root")
		.setLabel("Nota Inicial")
		.setPosition(291, 237)
		.setSize(100, 100)
		.setBarHeight(20)
		.setItemHeight(20)
		.addItems(l1)
		//.setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
		.setOpen(false);
		;
	}

	public void addOitava() {
		List<String> l1 = Arrays.asList("0","1","2","3","4","5","6","7");
		cp5.addScrollableList("oitava")
		.setLabel("Oitava")
		.setPosition(396, 237)
		.setSize(100, 100)
		.setBarHeight(20)
		.setItemHeight(20)
		.addItems(l1)
		//.setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
		.setOpen(false);
		;
	}

	public void oitava(int n){
		Map<String, Object> item = cp5.get(ScrollableList.class, "oitava").getItem(n);
		String name = (String) item.get("name");
		octaveName = name;
		updateNotes();
	}

	public void root(int n){
		Map<String, Object> item = cp5.get(ScrollableList.class, "root").getItem(n);
		String name = (String) item.get("name");
		println(name);
		Note note = new Note(name);
		rootNote = note;
		updateNotes();
	}
	

	public void escala(int n) {
		/* request the selected item based on index n */
		Map<String, Object> item = cp5.get(ScrollableList.class, "escala").getItem(n);
		scaleName = (String)item.get("name");
		updateNotes();
		//println(scaleName);


		/* here an item is stored as a Map  with the following key-value pairs:
		 * name, the given name of the item
		 * text, the given text of the item by default the same as name
		 * value, the given value of the item, can be changed by using .getItem(n).put("value", "abc"); a value here is of type Object therefore can be anything
		 * color, the given color of the item, how to change, see below
		 * view, a customizable view, is of type CDrawable 
		 */

		CColor c = new CColor();
		c.setBackground(color(255,0,0));
		//cp5.get(ScrollableList.class, "dropdown").getItem(n).put("color", c);
	}

	public void initMapper() {
		while (!mapper.ready()) {
			mapper.poll(100);
		}
	
		System.out.println("Device is ready.");
	
		System.out.println("Device name: "+mapper.name());
		System.out.println("Device port: "+mapper.port());
		System.out.println("Device ordinal: "+mapper.ordinal());
		System.out.println("Device interface: "+mapper.iface());
		System.out.println("Device ip4: "+mapper.ip4());
	}

	public void createInputSlot(String name) {
		Mapper.Device.Signal inp1 = mapper.addInput(name, 1, 'i', "u",
				new PropertyValue('i', 0),
				new PropertyValue('i', 255), 
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					int[] v,
					TimeTag tt) {
				String slotMapperName = sig.name();
				//System.out.println(" >> in onInput() for "+slotMapperName+": "+Arrays.toString(v));
				for (int i = 0; i < slots.length; i++) {
					String slotScreenName = "/"+slots[i].getName();
					if(slotScreenName.equals(slotMapperName)){
						slots[i].setValue(v[0]);
					}
				}
			}});
		System.out.println("Input signal name: "+inp1.name());
	}

	public void createInputSlotGeral() {
		Mapper.Device.Signal inp1 = mapper.addInput("slotGeral", 1, 'i', "u",
				new PropertyValue('i', 0),
				new PropertyValue('i', 255), 
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					int[] v,
					TimeTag tt) {
				//System.out.println(" >> in onInput() for "+sig.name()+": "+Arrays.toString(v));
				slotGeralEntrada.setValue(v[0]);
			}});
		System.out.println("Input signal name: "+inp1.name());
	}

	private void createOutputSlotSaida() {
		this.outputFreqSignal = mapper.addOutput("Frequencia de Saida", 1, 'f', "Hz", new PropertyValue('f', 0.0f), new PropertyValue('f', 3000.0f));
	}

	public void keyPressed() {
		switch(key) {
		case('1'):
			/* make the ScrollableList behave like a ListBox */
			break;
		case('2'):
			/* make the ScrollableList behave like a DropdownList */
			break;
		case('3'):
			/*change content of the ScrollableList */
			break;
		case('4'):
			/* remove an item from the ScrollableList */
			break;
		case('5'):
			/* clear the ScrollableList */
			break;
		}
	}
	/*
a list of all methods available for the ScrollableList Controller
use ControlP5.printPublicMethodsFor(ScrollableList.class);
to print the following list into the console.

You can find further details about class ScrollableList in the javadoc.

Format:
ClassName : returnType methodName(parameter type)


controlP5.Controller : CColor getColor() 
controlP5.Controller : ControlBehavior getBehavior() 
controlP5.Controller : ControlWindow getControlWindow() 
controlP5.Controller : ControlWindow getWindow() 
controlP5.Controller : ControllerProperty getProperty(String) 
controlP5.Controller : ControllerProperty getProperty(String, String) 
controlP5.Controller : ControllerView getView() 
controlP5.Controller : Label getCaptionLabel() 
controlP5.Controller : Label getValueLabel() 
controlP5.Controller : List getControllerPlugList() 
controlP5.Controller : Pointer getPointer() 
controlP5.Controller : ScrollableList addCallback(CallbackListener) 
controlP5.Controller : ScrollableList addListener(ControlListener) 
controlP5.Controller : ScrollableList addListenerFor(int, CallbackListener) 
controlP5.Controller : ScrollableList align(int, int, int, int) 
controlP5.Controller : ScrollableList bringToFront() 
controlP5.Controller : ScrollableList bringToFront(ControllerInterface) 
controlP5.Controller : ScrollableList hide() 
controlP5.Controller : ScrollableList linebreak() 
controlP5.Controller : ScrollableList listen(boolean) 
controlP5.Controller : ScrollableList lock() 
controlP5.Controller : ScrollableList onChange(CallbackListener) 
controlP5.Controller : ScrollableList onClick(CallbackListener) 
controlP5.Controller : ScrollableList onDoublePress(CallbackListener) 
controlP5.Controller : ScrollableList onDrag(CallbackListener) 
controlP5.Controller : ScrollableList onDraw(ControllerView) 
controlP5.Controller : ScrollableList onEndDrag(CallbackListener) 
controlP5.Controller : ScrollableList onEnter(CallbackListener) 
controlP5.Controller : ScrollableList onLeave(CallbackListener) 
controlP5.Controller : ScrollableList onMove(CallbackListener) 
controlP5.Controller : ScrollableList onPress(CallbackListener) 
controlP5.Controller : ScrollableList onRelease(CallbackListener) 
controlP5.Controller : ScrollableList onReleaseOutside(CallbackListener) 
controlP5.Controller : ScrollableList onStartDrag(CallbackListener) 
controlP5.Controller : ScrollableList onWheel(CallbackListener) 
controlP5.Controller : ScrollableList plugTo(Object) 
controlP5.Controller : ScrollableList plugTo(Object, String) 
controlP5.Controller : ScrollableList plugTo(Object[]) 
controlP5.Controller : ScrollableList plugTo(Object[], String) 
controlP5.Controller : ScrollableList registerProperty(String) 
controlP5.Controller : ScrollableList registerProperty(String, String) 
controlP5.Controller : ScrollableList registerTooltip(String) 
controlP5.Controller : ScrollableList removeBehavior() 
controlP5.Controller : ScrollableList removeCallback() 
controlP5.Controller : ScrollableList removeCallback(CallbackListener) 
controlP5.Controller : ScrollableList removeListener(ControlListener) 
controlP5.Controller : ScrollableList removeListenerFor(int, CallbackListener) 
controlP5.Controller : ScrollableList removeListenersFor(int) 
controlP5.Controller : ScrollableList removeProperty(String) 
controlP5.Controller : ScrollableList removeProperty(String, String) 
controlP5.Controller : ScrollableList setArrayValue(float[]) 
controlP5.Controller : ScrollableList setArrayValue(int, float) 
controlP5.Controller : ScrollableList setBehavior(ControlBehavior) 
controlP5.Controller : ScrollableList setBroadcast(boolean) 
controlP5.Controller : ScrollableList setCaptionLabel(String) 
controlP5.Controller : ScrollableList setColor(CColor) 
controlP5.Controller : ScrollableList setColorActive(int) 
controlP5.Controller : ScrollableList setColorBackground(int) 
controlP5.Controller : ScrollableList setColorCaptionLabel(int) 
controlP5.Controller : ScrollableList setColorForeground(int) 
controlP5.Controller : ScrollableList setColorLabel(int) 
controlP5.Controller : ScrollableList setColorValue(int) 
controlP5.Controller : ScrollableList setColorValueLabel(int) 
controlP5.Controller : ScrollableList setDecimalPrecision(int) 
controlP5.Controller : ScrollableList setDefaultValue(float) 
controlP5.Controller : ScrollableList setHeight(int) 
controlP5.Controller : ScrollableList setId(int) 
controlP5.Controller : ScrollableList setImage(PImage) 
controlP5.Controller : ScrollableList setImage(PImage, int) 
controlP5.Controller : ScrollableList setImages(PImage, PImage, PImage) 
controlP5.Controller : ScrollableList setImages(PImage, PImage, PImage, PImage) 
controlP5.Controller : ScrollableList setLabel(String) 
controlP5.Controller : ScrollableList setLabelVisible(boolean) 
controlP5.Controller : ScrollableList setLock(boolean) 
controlP5.Controller : ScrollableList setMax(float) 
controlP5.Controller : ScrollableList setMin(float) 
controlP5.Controller : ScrollableList setMouseOver(boolean) 
controlP5.Controller : ScrollableList setMoveable(boolean) 
controlP5.Controller : ScrollableList setPosition(float, float) 
controlP5.Controller : ScrollableList setPosition(float[]) 
controlP5.Controller : ScrollableList setSize(PImage) 
controlP5.Controller : ScrollableList setSize(int, int) 
controlP5.Controller : ScrollableList setStringValue(String) 
controlP5.Controller : ScrollableList setUpdate(boolean) 
controlP5.Controller : ScrollableList setValue(float) 
controlP5.Controller : ScrollableList setValueLabel(String) 
controlP5.Controller : ScrollableList setValueSelf(float) 
controlP5.Controller : ScrollableList setView(ControllerView) 
controlP5.Controller : ScrollableList setVisible(boolean) 
controlP5.Controller : ScrollableList setWidth(int) 
controlP5.Controller : ScrollableList show() 
controlP5.Controller : ScrollableList unlock() 
controlP5.Controller : ScrollableList unplugFrom(Object) 
controlP5.Controller : ScrollableList unplugFrom(Object[]) 
controlP5.Controller : ScrollableList unregisterTooltip() 
controlP5.Controller : ScrollableList update() 
controlP5.Controller : ScrollableList updateSize() 
controlP5.Controller : String getAddress() 
controlP5.Controller : String getInfo() 
controlP5.Controller : String getName() 
controlP5.Controller : String getStringValue() 
controlP5.Controller : String toString() 
controlP5.Controller : Tab getTab() 
controlP5.Controller : boolean isActive() 
controlP5.Controller : boolean isBroadcast() 
controlP5.Controller : boolean isInside() 
controlP5.Controller : boolean isLabelVisible() 
controlP5.Controller : boolean isListening() 
controlP5.Controller : boolean isLock() 
controlP5.Controller : boolean isMouseOver() 
controlP5.Controller : boolean isMousePressed() 
controlP5.Controller : boolean isMoveable() 
controlP5.Controller : boolean isUpdate() 
controlP5.Controller : boolean isVisible() 
controlP5.Controller : float getArrayValue(int) 
controlP5.Controller : float getDefaultValue() 
controlP5.Controller : float getMax() 
controlP5.Controller : float getMin() 
controlP5.Controller : float getValue() 
controlP5.Controller : float[] getAbsolutePosition() 
controlP5.Controller : float[] getArrayValue() 
controlP5.Controller : float[] getPosition() 
controlP5.Controller : int getDecimalPrecision() 
controlP5.Controller : int getHeight() 
controlP5.Controller : int getId() 
controlP5.Controller : int getWidth() 
controlP5.Controller : int listenerSize() 
controlP5.Controller : void remove() 
controlP5.Controller : void setView(ControllerView, int) 
controlP5.ScrollableList : List getItems() 
controlP5.ScrollableList : Map getItem(String) 
controlP5.ScrollableList : Map getItem(int) 
controlP5.ScrollableList : ScrollableList addItem(String, Object) 
controlP5.ScrollableList : ScrollableList addItems(List) 
controlP5.ScrollableList : ScrollableList addItems(Map) 
controlP5.ScrollableList : ScrollableList addItems(String[]) 
controlP5.ScrollableList : ScrollableList clear() 
controlP5.ScrollableList : ScrollableList close() 
controlP5.ScrollableList : ScrollableList open() 
controlP5.ScrollableList : ScrollableList removeItem(String) 
controlP5.ScrollableList : ScrollableList removeItems(List) 
controlP5.ScrollableList : ScrollableList setBackgroundColor(int) 
controlP5.ScrollableList : ScrollableList setBarHeight(int) 
controlP5.ScrollableList : ScrollableList setBarVisible(boolean) 
controlP5.ScrollableList : ScrollableList setItemHeight(int) 
controlP5.ScrollableList : ScrollableList setItems(List) 
controlP5.ScrollableList : ScrollableList setItems(Map) 
controlP5.ScrollableList : ScrollableList setItems(String[]) 
controlP5.ScrollableList : ScrollableList setOpen(boolean) 
controlP5.ScrollableList : ScrollableList setScrollSensitivity(float) 
controlP5.ScrollableList : ScrollableList setType(int) 
controlP5.ScrollableList : boolean isBarVisible() 
controlP5.ScrollableList : boolean isOpen() 
controlP5.ScrollableList : int getBackgroundColor() 
controlP5.ScrollableList : int getBarHeight() 
controlP5.ScrollableList : int getHeight() 
controlP5.ScrollableList : void controlEvent(ControlEvent) 
controlP5.ScrollableList : void keyEvent(KeyEvent) 
controlP5.ScrollableList : void setDirection(int) 
controlP5.ScrollableList : void updateItemIndexOffset() 
java.lang.Object : String toString() 
java.lang.Object : boolean equals(Object) 

created: 2015/03/24 12:21:22

	 */
	public void settings() {  size(800, 400); }
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "gui.freqChooser.FreqChoose_v001" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

	@Override
	public void controlEvent(CallbackEvent theEvent) {
		if(theEvent.getController() == slotSaida){
			outputFreqSignal.update(slotSaida.getValue());
		}
	}
}
