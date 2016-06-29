package main;

import java.util.Arrays;

import javax.sound.midi.MidiMessage;

import Mapper.Device;
import Mapper.InputListener;
import Mapper.PropertyValue;
import Mapper.TimeTag;
import processing.core.PApplet;
import themidibus.ControlChange;
import themidibus.MidiBus;
import themidibus.Note;

public class Prophet08Libmapper extends PApplet{

	final Device dev = new Device("Prophet08");
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
		createInputController("Mod_Wheel", 1);
		createInputController("Breath_Controller", 2);
		createInputController("Foot_Controller", 4);
		createInputController("Volume", 7);
		createInputController("Brightness", 74);
		createInputController("Expression_Controller", 11);
		createInputController("Bank_Select_OBS", 32);
		createInputController("Damper_Pedal", 64);
		createInputController("AllNotesOff", 123);
		createInputController("Reset_All_Controllers", 121);
		createInputController("Osc_1_Frequency", 20);
		createInputController("Osc_1_Freq_Fine", 21);
		createInputController("Osc_1_Shape", 22);
		createInputController("Glide_1", 23);
		createInputController("Osc_2_Frequency", 24);
		createInputController("Osc_2_Freq_Fine", 25);
		createInputController("Osc_2_Shape", 26);
		createInputController("Glide_2", 27);
		createInputController("Osc_Mix", 28);
		createInputController("Noise_Level", 29);
		createInputController("Filter_Frequency", 102);
		createInputController("Resonance", 103);
		createInputController("Filter_Key_Amt", 104);
		createInputController("Filter_Audio_Mod", 105);
		createInputController("Filter_Env_Amt", 106);
		createInputController("Filter_Env_Vel_Amt", 107);
		createInputController("Filter_Delay", 108);
		createInputController("Filter_Attack", 109);
		createInputController("Filter_Decay", 110);
		createInputController("Filter_Sustain", 111);
		createInputController("Filter_Release", 112);
		createInputController("VCA_Level", 113);
		createInputController("Pan_Spread", 114);
		createInputController("Amp_Env_Amt", 115);
		createInputController("Amp_Velocity_Amt", 116);
		createInputController("Amp_Delay", 117);
		createInputController("Amp_Attack", 118);
		createInputController("Amp_Decay", 119);
		createInputController("Amp_Sustain", 75);
		createInputController("Amp_Release", 76);
		createInputController("Env_3_Destination", 85);
		createInputController("Env_3_Amt", 86);
		createInputController("Env_3_Velocity_Amt", 87);
		createInputController("Env_3_Delay", 88);
		createInputController("Env_3_Attack", 89);
		createInputController("Env_3_Decay", 90);
		createInputController("Env_3_Sustain", 77);
		createInputController("Env_3_Release", 78);
		createInputController("BPM", 14);
		createInputController("Clock_Divide", 15);

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

	private void createInputController(String name, final int ccNumber) {
		Mapper.Device.Signal inp1 = dev.addInput(name, 1, 'f', "u",
				new PropertyValue('f', 0.0),
				new PropertyValue('f', 127.0), 
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					float[] v,
					TimeTag tt) {
				System.out.println(" >> in onInput() for "+sig.name()+": "+Arrays.toString(v));
				//sendCC(v[0]);
				ControlChange change = new ControlChange(0, ccNumber, (int)v[0]);
				myBus.sendControllerChange(change);
			}});
		System.out.println("Input signal name: "+inp1.name());
	}

	private void createInputNoteTrigger(String name){
		Mapper.Device.Signal inp1 = dev.addInput(name, 1, 'f', "u",
				new PropertyValue('f', 0.0),
				new PropertyValue('f', 127.0), 
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					float[] v,
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
		System.out.println("Input signal name: "+inp1.name());
	}
	
	private void createPitchBend() {
		Mapper.Device.Signal inp1 = dev.addInput("Pitch_Bend", 1, 'f', "u",
				new PropertyValue('f', -8192.0),
				new PropertyValue('f', 8191.0), 
				new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					int instanceId,
					float[] v,
					TimeTag tt) {
				System.out.println(" >> in onInput() for "+sig.name()+": "+Arrays.toString(v));
				//sendCC(v[0]);
				sendPitchBend((int)v[0]);
			}});
		System.out.println("Input signal name: "+inp1.name());
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
		String[] appletArgs = new String[] {"main.Prophet08Libmapper"};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}

