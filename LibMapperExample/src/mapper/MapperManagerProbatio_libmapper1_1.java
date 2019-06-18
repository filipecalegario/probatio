package mapper;

import mvc.controller.BlockController;
import serial.BlockParserObserver;

public class MapperManagerProbatioLibmapper1_1_2019 implements BlockParserObserver{

	//private Vector<SignalSlot> signalSlots;
	public final static Device dev = new Device("probatio");

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
		//Signal outSignal = dev.addOutputSignal(name, quantity, type, unit, new Property('i', minLimit), new Property('i', maxLimit));
		Signal outSignal = dev.addOutputSignal(name, quantity, type, unit, new Value('i', minLimit), new Value('i', maxLimit)); 
		outSignal.setMaximum(new Value(255));
		outSignal.setMinimum(new Value(0));
		return outSignal;
	}

	public static void removeOutput(Signal arg0) {
		dev.removeSignal(arg0);
	}

	public static void freeDevice(){
		System.out.println("Device has been closed");
		dev.free();
	}

	public static void pollDevice(){
		try {
			dev.poll(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initializeDevice() {
		freeOnShutdown();
		System.out.println("Initializing libMapper device...");
		while (!dev.ready()) {
			dev.poll(100);
		}
		System.out.println("libMapper device is ready!");

		System.out.println("Device name: "+dev.name());
		System.out.println("Device port: "+dev.port());
		System.out.println("Device ordinal: "+dev.ordinal());
		System.out.println("Device interface: "+dev.network().iface());
		//System.out.println("Device ip4: "+dev.network());
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
			blockController.getSignals()[i] = MapperManagerProbatioLibmapper1_1_2019.addOutput(blockNameById + "-" + blockValueLabel, 1, 'i', "unit", 0, 255);
		}
	}

	@Override
	public void onUpdateBlockEvent(BlockController blockController) {
		for (int i = 0; i < blockController.getDataSize(); i++) {
			Signal signal = blockController.getSignals()[i];
			if(signal != null){
				signal.update(blockController.getValues()[i]);				
			}
		}
	}

	@Override
	public void onRemoveBlockEvent(BlockController blockController) {
		for (int i = 0; i < blockController.getDataSize(); i++) {
			if (blockController.getSignals()[i] != null) {
				try {
					MapperManagerProbatioLibmapper1_1_2019.removeOutput(blockController.getSignals()[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
