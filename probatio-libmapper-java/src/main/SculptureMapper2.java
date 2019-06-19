package main;

import java.util.Arrays;

import javax.sound.midi.MidiMessage;

import Mapper.Device;
import Mapper.InputListener;
import Mapper.PropertyValue;
import Mapper.TimeTag;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import themidibus.ControlChange;
import themidibus.MidiBus;
import themidibus.Note;

public class SculptureMapper2 extends PApplet{

	final Device dev = new Device("Logic_Sculpture");
	public MidiBus myBus;

	boolean lockUp = false;
	boolean lockDown = true;

	public void settings() {
		size(50, 50);
		pixelDensity(2);
		smooth();
	}

	public void setup() {
		// This is how to ensure the device is freed when the program
		// exits, even on SIGINT.  The Device must be declared "final".
		myBus = new MidiBus(this, -1, "porta 1");
		//myBus = new MidiBus(this, -1, "FastTrack Pro");

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				dev.free();
			}
		});

		createInputNoteTrigger("Trigger_Note");
		createPitchBend();
		parseJSON();
		//createInputController("TEST", 10, 1, 0, 127);

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

	private void parseJSON() {
		JSONObject json = this.loadJSONObject("sculpture_namespace.json");
		JSONObject device = json.getJSONObject("device");
		JSONArray inputs = device.getJSONArray("inputs");
		int size = inputs.size();
		for (int i = 0; i < size; i++) {
			JSONObject input = inputs.getJSONObject(i);
			String name = input.getString("name");
			//String type = input.getString("type");
			int minimum = input.getInt("minimum");
			int maximum = input.getInt("maximum");
			String[] parts = split(name, "/");
			int channel = Integer.parseInt(split(parts[1], "_")[0]);
			int controlNumber = Integer.parseInt(parts[3]);
			String supercategory = parts[4];
			int lastIndex = 5;
			if(supercategory.length() < lastIndex){
				lastIndex = supercategory.length();
			}
			String description = supercategory.substring(0, lastIndex) + "_" + parts[5];
			//String description = "my_" + i;
			System.out.println(description + " " + controlNumber + " " + channel + " " + minimum + " " + maximum);
			createInputController(description, controlNumber, channel, minimum, maximum);
		}
	}

	private void createInputController(String name, int ccNumber, int channel, int minimum, int maximum) {
		Mapper.Device.Signal inp1 = dev.addInput(name, 1, 'i', "u",
				new PropertyValue('i', 0),
				new PropertyValue('i', 127),
				//new PropertyValue('i', minimum),
				//new PropertyValue('i', maximum),
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					int[] v,
					TimeTag tt) {
				//System.out.println(" >> in onInput() for "+sig.name()+": "+Arrays.toString(v));
				//sendCC(v[0]);
//				ControlChange change = new ControlChange(channel-1, ccNumber, v[0]);
				ControlChange change = new ControlChange(1-1, 20, v[0]);
				myBus.sendControllerChange(change);
			}});
		//System.out.println("Input signal name: "+inp1.name());
	}

	private void createInputNoteTrigger(String name){
		Mapper.Device.Signal inp1 = dev.addInput(name, 1, 'i', "u",
				new PropertyValue('i', 0.0),
				new PropertyValue('i', 127.0), 
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					int[] v,
					TimeTag tt) {
				System.out.println(" >> in onInput() for "+sig.name()+": "+Arrays.toString(v));
				//sendCC(v[0]);
				Note note = new Note(0, 60, 60);
				if(v[0] > 64 && !lockUp){
					myBus.sendNoteOn(note); // Send a Midi noteOn
					lockUp = true;
					lockDown = false;
				} 
				if(v[0] < 64 && !lockDown){
					myBus.sendNoteOff(note);
					lockDown = true;
					lockUp = false;
				}
			}});
		//System.out.println("Input signal name: "+inp1.name());
	}

	private void createPitchBend() {
		Mapper.Device.Signal inp1 = dev.addInput("Pitch_Bend", 1, 'i', "u",
				new PropertyValue('i', -8192.0),
				new PropertyValue('i', 8191.0), 
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					int[] v,
					TimeTag tt) {
				System.out.println(" >> in onInput() for "+sig.name()+": "+Arrays.toString(v));
				//sendCC(v[0]);
				sendPitchBend(v[0]);
			}});
		//System.out.println("Input signal name: "+inp1.name());
	}

	private void sendPitchBend(int inPitchValue){
		int MIDI_PITCHBEND_MIN = -8192;
		int MIDI_PITCHBEND_MAX = 8191;
		int pitchBendCode = 0xE0;
		int bend = inPitchValue - MIDI_PITCHBEND_MIN;
		byte[] data = {(byte)pitchBendCode, (byte)(bend & 0x7f), (byte)((bend >> 7) & 0x7f), 0};
		myBus.sendMessage(data);
		//myBus.sendMessage(pitchBendCode, (bend & 0x7f), (bend >> 7) & 0x7f, 0);
	}

	@Override
	public void draw() {
		dev.poll(100);
	}

	//	public InputListener createInputListener(){
	//		InputListener inputListener = new InputListener() {
	//			public void onInput(Mapper.Device.Signal sig,
	//					int instanceId,
	//					float[] v,
	//					TimeTag tt) {
	//				System.out.println(" >> in onInput() for "+sig.name()+": "
	//						+Arrays.toString(v));
	//				sendCC(v[0]);
	//			}};
	//			return inputListener;
	//	}
	//
	//	public void sendCC(float v){
	//		int channel = 0;
	//		int number = 1;
	//		int value = 90;
	//		ControlChange change = new ControlChange(channel, number, value);
	//		myBus.sendControllerChange(change); // Send a controllerChange
	//	}


	public static  void main(String[] passedArgs) {
		//		System.setProperty( "java.library.path", "/usr/local/lib" );
		//
		//		Field fieldSysPath;
		//		try {
		//			fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
		//			fieldSysPath.setAccessible( true );
		//			fieldSysPath.set( null, null );
		//		} catch (NoSuchFieldException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (SecurityException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IllegalArgumentException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (IllegalAccessException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		PApplet.main(main.Prophet08Libmapper.class.getName());
		String[] appletArgs = new String[] {"main.SculptureMapper2"};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}


