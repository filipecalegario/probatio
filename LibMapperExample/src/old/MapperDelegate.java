package old;

import java.util.HashMap;

import Mapper.Device;
import Mapper.Device.Signal;
import Mapper.PropertyValue;
import model.Block;

public class MapperDelegate {

	private Device dev;
	private HashMap<Block, Signal> signals;
	
	public MapperDelegate(String deviceName) {
		this.dev = new Device(deviceName, 9000);
		this.signals = new HashMap<Block, Signal>();
	}
	
	public void addSignal(Block block){
		Signal signal = dev.add_output(block.getName(), block.getDataSize(),'i', "unit", 0.0, 255.0);
		signals.put(block, signal);
	}
	
	public void updateSignal(Block block){
		Signal sig = signals.get(block);
		sig.update(block.getValues());
	}
	
	public void removeSignal(Block block){
		dev.remove_output(signals.get(block));
		signals.remove(block);
	}
	
	public void freeDevice(){
		System.out.println("Device has been closed");
		dev.free();
	}
	
	public void pollDevice(){
		dev.poll(0);
	}
	
	public void iDontKnowIfItIsNecessary(){
		dev.set_property("width", new PropertyValue(256));
		dev.set_property("height", new PropertyValue(12.5));
		dev.set_property("depth", new PropertyValue("67"));
	}
	
	public void printDeviceInitialization() {
		System.out.println("Initializing libMapper device...");
		while (!dev.ready()) {
			dev.poll(100);
		}
		System.out.println("libMapper device is ready!");

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
