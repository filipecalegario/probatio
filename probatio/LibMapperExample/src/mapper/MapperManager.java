package mapper;

import java.util.Vector;

import Mapper.Device;
import Mapper.Device.Signal;
import model.Block;

public class MapperManager {

	private Vector<SignalSlot> signalSlots;
	private Device dev;

	public MapperManager(String deviceName) {
		this.dev = new Device(deviceName, 9000);
		this.signalSlots = new Vector<SignalSlot>();
	}

	public void addSignal(Block block){
		for (int i = 0; i < block.getValues().length; i++) {
			Signal signal = dev.add_output(block.getName() + "-" + block.getValuesLabels()[i], 1,'i', "unit", 0.0, 255.0);
			SignalSlot signalSlot = new SignalSlot(block.getId(), i, signal);
			this.signalSlots.addElement(signalSlot);
		}
	}

	public void updateSignal(Block block){
		for (int i = 0; i < block.getValues().length; i++) {
			SignalSlot signalSlot = getSignalSlot(block.getId(), i);
			signalSlot.getSignal().update(block.getValues()[i]);
		}
	}

	public void removeSignal(Block block){
		for (int i = 0; i < block.getValues().length; i++) {
			SignalSlot signalSlot = getSignalSlot(block.getId(), i);
			Signal signal = signalSlot.getSignal();
			dev.remove_output(signal);
			signalSlots.remove(signalSlot);
		}
	}

	public void freeDevice(){
		System.out.println("Device has been closed");
		dev.free();
	}

	public void pollDevice(){
		dev.poll(0);
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

	private SignalSlot getSignalSlot(int idBlock, int idValue){
		SignalSlot result = null;
		for (SignalSlot signalSlot : signalSlots) {
			if (signalSlot.getIdBlock() == idBlock && signalSlot.getIdValue() == idValue) {
				result = signalSlot;
			}
		}
		return result;
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
