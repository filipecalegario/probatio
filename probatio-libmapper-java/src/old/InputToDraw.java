package old;

import java.util.Arrays;

import Mapper.Device;
import Mapper.InputListener;
import Mapper.PropertyValue;
import Mapper.TimeTag;
import processing.core.PApplet;

public class InputToDraw extends PApplet {

	final Device dev = new Device("grapher", 9000);
	private float inputValue = 0;
	private int xPos = 0;

	public void settings(){
		size(800, 600);
		pixelDensity(2);
		smooth();
	}

	public void setup(){
		freeOnShutdown();

		Mapper.Device.Signal inp1 = dev.addInput("insig1", 1, 'f', "Hz", new PropertyValue(0.0), new PropertyValue(20.0), new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					Mapper.Db.Signal props,
					int instance_id,
					float[] v,
					TimeTag tt) {
				System.out.println("in onInput() for " +props.name()+": " +Arrays.toString(v));
				inputValue = v[0];
			}});

		dev.setProperty("width", new PropertyValue(256));
		dev.setProperty("height", new PropertyValue(12.5));
		dev.setProperty("depth", new PropertyValue("67"));

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
		background(255);
	}

	public void draw(){
		dev.poll(0);
		//println(inputValue);
		float screenValue = map(inputValue, 0, 255, 0, height);

		stroke(255,0,0,60);
		line(xPos, height, xPos, height - screenValue);

		if (xPos >= width) {
			xPos = 0;
			background(255);
		} else {
			// increment the horizontal position:
			xPos++;
		}
	}

	public void stop(){
		dev.free();
	}

	public static void main(String args[]) {
		PApplet.main(InputToDraw.class.getName());
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
