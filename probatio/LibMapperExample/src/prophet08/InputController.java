package prophet08;

import java.util.Arrays;

import Mapper.Device;
import Mapper.InputListener;
import Mapper.PropertyValue;
import Mapper.TimeTag;
import themidibus.ControlChange;
import themidibus.MidiBus;

public class InputController {
	
	private String description;
	private int ccNumber;
	private int lastValue;
	private int value;
	private int channel;
	private MidiBus mb;
	//private final Device dev;
	
	public InputController(MidiBus mb, int ccNumber, int channel) {
		this.mb = mb;
		this.ccNumber = ccNumber;
		this.channel = channel;
		this.value = 0;
		this.lastValue = 0;
	}
	
	public void sendValue(int value){
		this.value = value;
		if(this.value != this.lastValue){
			ControlChange change = new ControlChange(this.channel-1, this.ccNumber, value);
			mb.sendControllerChange(change);
		}
		this.lastValue = this.value;
	}
	
	public boolean hasChanged(){
		boolean result = false;
		if(value != lastValue){
			result = true;
		}
		return result;
	}

}
