package mapper;

import Mapper.Device;
import Mapper.Device.Signal;
import Mapper.PropertyValue;
import mvc.controller.BlockController;
import mvc.model.BlockType;
import serial.BlockParserObserver;

public class MapperManager implements BlockParserObserver{

	//private Vector<SignalSlot> signalSlots;
	public final static Device dev = new Device("probatio", 9000);

	boolean DEBUG_enableLibmapper = false;

	//	public MapperManager() {
	//		this.signalSlots = new Vector<SignalSlot>();
	//		freeOnShutdown();
	//	}

	/*
	public void addSignalFromBlock(Block block){
		if (block != null) {
			for (int i = 0; i < block.getValues().length; i++) {
				Signal signal = dev.add_output(block.getName() + "-" + block.getValuesLabels()[i], 1, 'i', "unit", 0.0, 255.0);
				SignalSlot signalSlot = new SignalSlot(block.getId(), i, signal);
				this.signalSlots.addElement(signalSlot);
			} 
		}
	}

	public void updateSignalFromBlock(Block block){
		if (block != null) {
			for (int i = 0; i < block.getValues().length; i++) {
				SignalSlot signalSlot = getSignalSlot(block.getId(), i);
				if (signalSlot != null) {
					signalSlot.getSignal().update(block.getValues()[i]);
				}
			} 
		}
	}

	public void removeSignalFromBlock(Block block){
		if (block != null) {
			for (int i = 0; i < block.getValues().length; i++) {
				SignalSlot signalSlot = getSignalSlot(block.getId(), i);
				if (signalSlot != null) {
					Signal signal = signalSlot.getSignal();
					dev.remove_output(signal);
					signalSlots.remove(signalSlot);
				}
			} 
		}
	}*/

	public static Signal addOutput(String name, int quantity, char type, String unit, int minLimit, int maxLimit) {
		//		return dev.add_output(name, quantity, type, unit, minLimit, maxLimit);
		Signal outSignal = dev.addOutput(name, quantity, type, unit, new PropertyValue('i', minLimit), new PropertyValue('i', maxLimit));
		outSignal.setMaximum(new PropertyValue(255));
		outSignal.setMinimum(new PropertyValue(0));
		return outSignal;
	}

	public static void removeOutput(Signal arg0) {
		dev.removeOutput(arg0);
	}

	public static void freeDevice(){
		System.out.println("Device has been closed");
		dev.free();
	}

	public static void pollDevice(){
		dev.poll(0);
	}

	public static void printDeviceInitialization() {
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

	//	private SignalSlot getSignalSlot(int idBlock, int idValue){
	//		SignalSlot result = null;
	//		for (SignalSlot signalSlot : signalSlots) {
	//			if (signalSlot.getIdBlock() == idBlock && signalSlot.getIdValue() == idValue) {
	//				result = signalSlot;
	//			}
	//		}
	//		return result;
	//	}

	public static void freeOnShutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				freeDevice();
			}
		});
	}

	@Override
	public void onAddBlockEvent(BlockController blockController) {
		for (int i = 0; i < blockController.getDataSize(); i++) {
			String blockNameById = blockController.getName();
			String blockValueLabel = blockController.getValuesLabels()[i];
			blockController.getSignals()[i] = MapperManager.addOutput(blockNameById + "-" + blockValueLabel, 1, 'i', "unit", 0, 255);
		}
	}

	@Override
	public void onUpdateBlockEvent(BlockController blockController) {
		for (int i = 0; i < blockController.getDataSize(); i++) {
			blockController.getSignals()[i].update(blockController.getValues()[i]);
		}
	}

	@Override
	public void onRemoveBlockEvent(BlockController blockController) {
		for (int i = 0; i < blockController.getDataSize(); i++) {
			if (blockController.getSignals()[i] != null) {
				try {
					MapperManager.removeOutput(blockController.getSignals()[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
