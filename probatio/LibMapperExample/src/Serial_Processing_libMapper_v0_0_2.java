import Mapper.Device;
import Mapper.Device.Signal;
import Mapper.PropertyValue;
import processing.core.PApplet;
import processing.serial.Serial; 

public class Serial_Processing_libMapper_v0_0_2 extends PApplet {

	final Device dev = new Device("Serial_Processing", 9001);
	private Signal out1;

	Serial myPort;        // The serial port
	//PrintWriter output;
	int xPos = 1;         // horizontal position of the graph
	float inByte = 0;
	float lastInByte = 0;
	float lastDerivate = 0;
	float energy = 0;

	public void settings() {  
		size(800, 600);
		pixelDensity(2);
		smooth();
	}

	public void setup () {

		//pixelDensity(2);
		//smooth();
		//output = createWriter("output.txt");

		freeOnShutdown();
		out1 = dev.add_output("out1", 1,'i', "unit", 0.0, 255.0);

		dev.set_property("width", new PropertyValue(256));
		dev.set_property("height", new PropertyValue(12.5));
		dev.set_property("depth", new PropertyValue("67"));

		out1.set_property("width", new PropertyValue(128));
		out1.set_property("height", new PropertyValue(6.25));
		out1.set_property("depth", new PropertyValue("test"));

		initializeDevice();

		println(Serial.list());
		myPort = new Serial(this, Serial.list()[5], 115200);
		myPort.bufferUntil('\n');
		myPort.clear();
		background(255);
	}

	public void draw () {
		float derivate = inByte - lastInByte;
		float derivate2 = abs(derivate) - lastDerivate;

		if(derivate > 0) {
			stroke(0);
		} else if(derivate < 0){
			stroke(255,0,0);
		} else {
			stroke(127);
		}
		line(xPos, height, xPos, height - map(inByte, 0, 255, 0, height));

		stroke(0,0,255);
		line(xPos, 0, xPos, 0 + map(abs(derivate), 0, 255, 0, height));

		stroke(0,255,0);
		line(xPos, 0, xPos, 0 + map(abs(derivate2), 0, 255, 0, height));

		lastDerivate = abs(derivate);

		energy = energy + abs(derivate2);
		energy = constrain(energy, 0, height);

		stroke(255,0,255);
		//line(xPos, 0, xPos, 0 + energy);
		energy = energy - 10;  

		int valueToLibmapper = (int) inByte;
		out1.update(valueToLibmapper);

		if (xPos >= width) {
			xPos = 0;
			background(255);
		} else {
			xPos++;
		}
		dev.poll(0);
	}

	public void stop(){
		dev.free();
	}

	public void serialEvent (Serial myPort) {
		String inString = myPort.readStringUntil('\n');
		print(inString);
	}

	public void keyPressed() {
		//output.flush(); // Writes the remaining data to the file
		//output.close(); // Finishes the file
		exit(); // Stops the program
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "Serial_Processing_libMapper_v0_0_2" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

	private void initializeDevice() {
		System.out.println("Waiting for ready...");
		while (!dev.ready()) {
			dev.poll(100);
		}
		System.out.println("Device is ready.");

		System.out.println("Device name: "+dev.name());
		System.out.println("Device port: "+dev.port());
		System.out.println("Device ordinal: "+dev.ordinal());
		System.out.println("Device interface: "+dev.iface());
		System.out.println("Device ip4: "+dev.ip4());
	}

	private void freeOnShutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				dev.free();
			}
		});
	}
}
