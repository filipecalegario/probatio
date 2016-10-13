package serial;
import controlP5.Chart;
import controlP5.ControlP5;
import mapper.MapperManager;
import mvc.controller.BlockController;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.serial.Serial; 

public class SerialControlP5chart extends PApplet implements BlockParserObserver{

	/**
	 * ControlP5 Chart
	 * 
	 * find a list of public methods available for the Chart Controller
	 * at the bottom of this sketch.
	 * 
	 * by Andreas Schlegel, 2014
	 * www.sojamo.de/libraries/controlp5
	 *
	 */

	ControlP5 cp5;

	ParserWithBlockController parser;
	
	Serial myPort; 
	boolean serialIsReady = false;
	
	int index = 0;

	public void setup() {
		long startTime = millis();
		cp5 = new ControlP5(this);

		initializeSerial(startTime, "/dev/cu.usbmodem1411");
		
		parser = new ParserWithBlockController(this);
		parser.attach(this);
		
		MapperManager mapper = new MapperManager();
		parser.attach(mapper);
		
		MapperManager.freeOnShutdown();
		MapperManager.printDeviceInitialization();
	}

	public void draw() {
		MapperManager.pollDevice();
		background(200);
	}

	@Override
	public void onAddBlockEvent(BlockController blockController) {
		try {
			System.out.println("Block added: " + blockController.getName());
//			int modulo = 4;
//			float xSize = 100;
//			float ySize = 50;
//			float x = ((index%modulo) * xSize)+((index%modulo)+1)*20;
//			float linha = (index - (index%modulo))/modulo;
//			float y = (ySize * linha) + ((linha + 1)*20);
			Chart myChart = cp5.addChart(blockController.getName());
			//blockController.setChart(myChart, x, y, color(40));
			blockController.setChart(myChart);
			index++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpdateBlockEvent(BlockController blockController) {
		try {
			blockController.updateView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onRemoveBlockEvent(BlockController blockController) {
		try {
			System.out.println("Block removed: " + blockController.getName());
			cp5.remove(blockController.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void serialEvent(Serial myPort){
		if(serialIsReady){
			byte[] bytes = myPort.readBytes();
			//println(bytes);
			parser.update(bytes);
		}
	}
	
	public void initializeSerial(long startTime, String portName){
		System.out.println("Printing Serial.list()");
		println(Serial.list());
		myPort = new Serial(this, portName, 115200);
		myPort.bufferUntil('\n');
		myPort.clear();
		System.out.println("Initializing serial port...");
		while (millis() - startTime < 500) {
			if (myPort.available() > 0) {
				//System.out.println("ENTROU");
				myPort.readStringUntil('\n');
			}
		}
		System.out.println("Serial port ready!");
		serialIsReady = true;
	}

	public void settings() {  size(800, 400);  }
	
	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKey() == 'q' || event.getKey() == 'Q'){
			MapperManager.freeDevice();
			exit();
		}
	}
	
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "serial.SerialControlP5chart" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
