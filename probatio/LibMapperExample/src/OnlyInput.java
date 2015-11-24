import java.util.Arrays;

import Mapper.Device;
import Mapper.InputListener;
import Mapper.InstanceEventListener;
import Mapper.PropertyValue;
import Mapper.TimeTag;
import Mapper.Device.Signal;

public class OnlyInput {
	public static void main(String[] args) {
		final Device dev = new Device("input_to_eclipse", 9000);

		// This is how to ensure the device is freed when the program
		// exits, even on SIGINT.  The Device must be declared "final".
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				dev.free();
			}
		});

		Mapper.Device.Signal inp1 = dev.add_input("insig1", 1, 'f', "Hz", 0.0, 20.0, new InputListener() {
			public void onInput(Mapper.Device.Signal sig,
					Mapper.Db.Signal props,
					int instance_id,
					float[] v,
					TimeTag tt) {
				System.out.println("in onInput() for "
						+props.name()+": "
						+Arrays.toString(v));
			}});

		System.out.println("Input signal name: "+inp1.name());

		dev.set_property("width", new PropertyValue(256));
		dev.set_property("height", new PropertyValue(12.5));
		dev.set_property("depth", new PropertyValue("67"));
		dev.set_property("deletethis", new PropertyValue("should not see me"));
		dev.remove_property("deletethis");

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

		System.out.println(inp1.name() + " oldest instance is " + inp1.oldest_active_instance());
		System.out.println(inp1.name() + " newest instance is " + inp1.newest_active_instance());
		
//		while(true){
//			dev.poll(0);	
//		}
		dev.free();
	}
}
