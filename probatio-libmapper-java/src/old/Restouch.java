package old;

import Mapper.Device.Signal;
import display.DisplaySlot;
import model.Block;

public class Restouch extends Block {
	
	DisplaySlot[] display;
	Signal[] signals;
	String[] labels = {"Pressing", "Selecting"};

	public Restouch(int type, int[] values, long timeAdded) {
		super(type, values, timeAdded);
		this.display = new DisplaySlot[values.length];
		this.signals = new Signal[values.length];
	}

}
