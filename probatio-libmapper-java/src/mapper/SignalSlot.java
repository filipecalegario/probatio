package mapper;

import Mapper.Device.Signal;

public class SignalSlot {
	
	private int idBlock;
	private int idValue;
	private Signal signal;
	
	public SignalSlot(int idBlock, int idValue, Signal signal) {
		super();
		this.idBlock = idBlock;
		this.idValue = idValue;
		this.signal = signal;
	}
	
	public int getIdBlock() {
		return idBlock;
	}
	public void setIdBlock(int idBlock) {
		this.idBlock = idBlock;
	}
	public int getIdValue() {
		return idValue;
	}
	public void setIdValue(int idValue) {
		this.idValue = idValue;
	}
	public Signal getSignal() {
		return signal;
	}
	public void setSignal(Signal signal) {
		this.signal = signal;
	}
	
	
	
}
