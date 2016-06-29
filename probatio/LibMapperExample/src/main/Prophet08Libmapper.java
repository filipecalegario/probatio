package main;

import java.util.Arrays;

import Mapper.Device;
import Mapper.InputListener;
import Mapper.PropertyValue;
import Mapper.TimeTag;
import processing.core.PApplet;
import themidibus.ControlChange;
import themidibus.MidiBus;

public class Prophet08Libmapper extends PApplet{

	final Device dev = new Device("Prophet08");
	public MidiBus myBus;

	public void settings() {
		size(50, 50);
		pixelDensity(2);
		smooth();
	}

	public void setup() {
		// This is how to ensure the device is freed when the program
		// exits, even on SIGINT.  The Device must be declared "final".
		//myBus = new MidiBus(this, -1, "porta 1");
		myBus = new MidiBus(this, -1, "FastTrack Pro");

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				dev.free();
			}
		});

		createInput("Mod_Wheel", 1);
		createInput("Breath_Controller", 2);
		createInput("Foot_Controller", 4);
		createInput("Volume", 7);
		createInput("Brightness", 74);
		createInput("Expression_Controller", 11);
		createInput("Bank_Select_OBS", 32);
		createInput("Damper_Pedal", 64);
		createInput("AllNotesOff", 123);
		createInput("Reset_All_Controllers", 121);
		createInput("Osc_1_Frequency", 20);
		createInput("Osc_1_Freq_Fine", 21);
		createInput("Osc_1_Shape", 22);
		createInput("Glide_1", 23);
		createInput("Osc_2_Frequency", 24);
		createInput("Osc_2_Freq_Fine", 25);
		createInput("Osc_2_Shape", 26);
		createInput("Glide_2", 27);
		createInput("Osc_Mix", 28);
		createInput("Noise_Level", 29);
		createInput("Filter_Frequency", 102);
		createInput("Resonance", 103);
		createInput("Filter_Key_Amt", 104);
		createInput("Filter_Audio_Mod", 105);
		createInput("Filter_Env_Amt", 106);
		createInput("Filter_Env_Vel_Amt", 107);
		createInput("Filter_Delay", 108);
		createInput("Filter_Attack", 109);
		createInput("Filter_Decay", 110);
		createInput("Filter_Sustain", 111);
		createInput("Filter_Release", 112);
		createInput("VCA_Level", 113);
		createInput("Pan_Spread", 114);
		createInput("Amp_Env_Amt", 115);
		createInput("Amp_Velocity_Amt", 116);
		createInput("Amp_Delay", 117);
		createInput("Amp_Attack", 118);
		createInput("Amp_Decay", 119);
		createInput("Amp_Sustain", 75);
		createInput("Amp_Release", 76);
		createInput("Env_3_Destination", 85);
		createInput("Env_3_Amt", 86);
		createInput("Env_3_Velocity_Amt", 87);
		createInput("Env_3_Delay", 88);
		createInput("Env_3_Attack", 89);
		createInput("Env_3_Decay", 90);
		createInput("Env_3_Sustain", 77);
		createInput("Env_3_Release", 78);
		createInput("BPM", 14);
		createInput("Clock_Divide", 15);


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

	private void createInput(String name, final int ccNumber) {
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

