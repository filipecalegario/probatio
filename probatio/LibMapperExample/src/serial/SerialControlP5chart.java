package serial;
import controlP5.Chart;
import controlP5.ControlP5;
import mvc.model.Block;
import processing.core.PApplet;
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

	Chart myChart;
	BlockParser parser;
	
	Serial myPort; 
	boolean serialIsReady = false;

	public void setup() {
		long startTime = millis();
		cp5 = new ControlP5(this);
		myChart = cp5.addChart("dataflow")
				.setPosition(50, 50)
				.setSize(200, 150)
				.setRange(0, 1023)
				.setView(Chart.AREA) // use Chart.LINE, Chart.PIE, Chart.AREA, Chart.BAR_CENTERED
				.setStrokeWeight(1.5f)
				.setColorCaptionLabel(color(40))
				.setCaptionLabel("Incoming")
				.setLabelVisible(true);
				;

		myChart.addDataSet("incoming");
		myChart.setData("incoming", new float[100]);

		initializeSerial(startTime, "/dev/cu.usbmodem32");
		
		parser = new BlockParser(this);
		parser.attach(this);
	}

	public void draw() {
		background(200);
		// unshift: add data from left to right (first in)
		//myChart.unshift("incoming", (sin(frameCount*0.1)*20));

		// push: add data from right to left (last in)
		//myChart.push("incoming", (sin(frameCount*0.1f)*10));
		//sp.update();
	}

	@Override
	public void onAddBlockEvent(Block block) {
		System.out.println("Block added: " + block.getName());
	}


	@Override
	public void onUpdateBlockEvent(Block block) {
		if(block.getId() == 51){
			int value = block.getValues()[0];
			//System.out.println(value);
			myChart.push("incoming", value);
		}
	}

	@Override
	public void onRemoveBlockEvent(Block block) {
		// TODO Auto-generated method stub
		System.out.println("Block removed: " + block.getName());
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

	public void settings() {  size(800, 400); }
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "serial.SerialControlP5chart" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
