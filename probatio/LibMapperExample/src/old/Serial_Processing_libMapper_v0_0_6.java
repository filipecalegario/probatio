package old;
//import java.util.Vector;
//
//import display.DisplayManager_v0_0_2;
//import display.DisplayManager_v0_0_1;
//import mapper.MapperDelegate;
//import model.Block;
//import processing.core.PApplet;
//import processing.serial.Serial; 
//
//public class Serial_Processing_libMapper_v0_0_6 extends PApplet {
//
//	//private Signal out1;
//
//	Serial myPort;        // The serial port
//	//PrintWriter output;
//	int xPos = 1;         // horizontal position of the graph
//	float inByte = 0;
//	float lastInByte = 0;
//	float lastDerivate = 0;
//	float energy = 0;
//
//	MapperDelegate mapperDelegate;
//	Vector<Block> blocks;
//	DisplayManager_v0_0_2 display;
//
//	boolean serialIsReady;
//
//	public void settings() {  
//		size(800, 600);
//		pixelDensity(2);
//		smooth();
//	}
//
//	public void setup () {
//		long startTime = millis();
//		serialIsReady = false;
//		mapperDelegate = new MapperDelegate("probatio");
//		blocks = new Vector<Block>();
//		display = new DisplayManager_v0_0_2(this);
//		mapperDelegate.printDeviceInitialization();
//
//		//println(Serial.list());
//		myPort = new Serial(this, Serial.list()[5], 115200);
//		myPort.bufferUntil('\n');
//		myPort.clear();
//		println("Initializing serial port...");
//		while (millis() - startTime < 500) {
//			if (myPort.available() > 0) {
//				myPort.readStringUntil('\n');
//			}
//		}
//		println("Serial port ready!");
//		serialIsReady = true;
//		background(255);
//	}
//
//	public void draw () {
//
//		mapperDelegate.pollDevice();
//		display.updateDrawDisplaySlot();
//		try {
//			for (int i = 0; i < blocks.size(); i++) {
//				Block currentBlock = blocks.elementAt(i);
//				if(millis() - currentBlock.getLastTimeUpdated() > 100){
//					removeBlockEvent(currentBlock);
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//	public void stop(){
//		mapperDelegate.freeDevice();
//	}
//
//	public void serialEvent (Serial myPort) {
//		if(serialIsReady){
//			try {
//				String inString = myPort.readStringUntil('\n');
//				parseAndAddOrUpdate(inString);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	private void parseAndAddOrUpdate(String inString) {
//		String subString = "";
//		if(inString != null && inString.length() > 0){
//			subString = inString.substring(inString.indexOf('#')+1, inString.indexOf('\n')-1);
//			String[] pieces = split(subString, ';');
//			//System.out.println(Arrays.toString(pieces));
//			int id = Integer.parseInt(pieces[0]);
//			int bytesQuant = Integer.parseInt(pieces[1]);
//			int[] values = new int[bytesQuant];
//			for (int i = 0; i < bytesQuant; i++) {
//				values[i] = Integer.parseInt(pieces[i+2]);
//			}
//			if(repositoryContainsBlock(id)){
//				updateBlockEvent(id, values);
//			} else {
//				Block newBlock = new Block(id, values, millis()); 
//				addBlockEvent(newBlock);
//			}
//		}
//	}
//
//	private void addBlockEvent(Block block) {
//		System.out.println(block.getName() + " added");
//		blocks.addElement(block);
//		mapperDelegate.addSignal(block);
//		display.addDisplaySlot(block);
//	}
//
//	private void updateBlockEvent(int id, int[] values) {
//		Block block = getBlockById(id);
//		block.updateValues(values, millis());
//		mapperDelegate.updateSignal(block);
//		display.updateValueDisplaySlot(id, values[0]);
//	}
//
//	private void removeBlockEvent(Block block) {
//		String name = block.getName();
//		mapperDelegate.removeSignal(block);
//		display.removeDisplaySlot(block.getId());
//		blocks.removeElement(block);
//		System.out.println(name + " removed");
//	}
//
//	//TODO This is not cool!
//	private boolean repositoryContainsBlock(int id) {
//		boolean result = false;
//		for (Block block : blocks) {
//			if(block.getId() == id){
//				result = true;
//			}
//		}
//		return result;
//	}
//
//	//TODO Two walks in the array: This is not cool!
//	private Block getBlockById(int id) {
//		Block result = null;
//		if(repositoryContainsBlock(id)){
//			for (Block block : blocks) {
//				if(block.getId() == id){
//					result = block;
//				}
//			}
//		}
//		return result;
//	}
//
//	public void keyPressed() {
//		mapperDelegate.freeDevice();
//		exit();
//	}
//
//	static public void main(String[] passedArgs) {
//		String[] appletArgs = new String[] {"Serial_Processing_libMapper_v0_0_6"};
//		if (passedArgs != null) {
//			PApplet.main(concat(appletArgs, passedArgs));
//		} else {
//			PApplet.main(appletArgs);
//		}
//	}
//
//}
