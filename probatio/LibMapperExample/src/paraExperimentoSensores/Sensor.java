package paraExperimentoSensores;

import Mapper.Device.Signal;

public class Sensor {
	
	private Signal signal;
	private String name;
	private int value;
	
	public Sensor(Signal signal, String name, int value) {
		super();
		this.signal = signal;
		this.name = name;
		this.value = value;
	}

	public Signal getSignal() {
		return signal;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
	
	public void updateValueSignal(int value){
		this.value = value;
		this.signal.update(value);
	}
	
}
