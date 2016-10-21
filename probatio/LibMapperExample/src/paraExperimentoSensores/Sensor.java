package paraExperimentoSensores;

import Mapper.Device.Signal;

public class Sensor {
	
	private Signal signal;
	private String name;
	private int value;
	
	public Sensor(String name, int value) {
		super();
		this.name = name;
		this.value = value;
	}
	
}
