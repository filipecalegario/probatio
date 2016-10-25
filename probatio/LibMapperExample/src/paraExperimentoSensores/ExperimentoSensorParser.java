package paraExperimentoSensores;

import java.util.HashMap;

import Mapper.Device.Signal;
import processing.core.PApplet;
import processing.serial.Serial;

public class ExperimentoSensorParser extends PApplet{

	private boolean serialIsReady;
	private Serial myPort;
	private HashMap<Integer, Sensor> sensors; 

	private void parseAndAddOrUpdate(int[] ints) {
		boolean isGoodFormat = (ints.length == 50) && 
				(ints[0] == 2) && 
				(ints[ints.length-1] == 10);
		if (isGoodFormat) {
			int[] trimmed = new int[48];
			for (int i = 1; i <= 48; i++) {
				trimmed[i-1] = ints[i];
			}
			for (int i = 0; i < trimmed.length/2; i++) {
				int status = trimmed[i*2];
				int value = trimmed[i*2+1];
				int id = i;
				if(status == 1){
					if(sensors.containsKey(id)){
						Sensor s = sensors.get(id);
						s.updateValueSignal(value);
					} else {
						String nameFromIndex = SensorNames.getNameFromIndex(id);
						Signal signal = ExperimentoMapper.addOutput(nameFromIndex, 1, 'i', "unit", 0, 255);
						Sensor s = new Sensor(signal, nameFromIndex, value);
						sensors.put(id, s);
					}
				} else {
					if(sensors.containsKey(id)){
						Sensor s = sensors.get(id);
						ExperimentoMapper.removeOutput(s.getSignal());
						sensors.remove(id);
					}
				}
			}
		}
	}

	public void setup () {
		frameRate(120);
		long startTime = millis();
		serialIsReady = false;
		println(Serial.list());
		//myPort = new Serial(this, Serial.list()[5], 115200);
		myPort = new Serial(this, "/dev/tty.usbmodem1421", 115200);
		//myPort = new Serial(this, "/dev/cu.usbmodem14111", 115200);
		myPort.bufferUntil('\n');
		myPort.clear();
		println("Initializing serial port...");
		while (millis() - startTime < 500) {
			if (myPort.available() > 0) {
				myPort.readStringUntil('\n');
			}
		}
		println("Serial port ready!");
		serialIsReady = true;
		background(255);
		this.sensors = new HashMap<>();
		ExperimentoMapper.initDevice();
	}

	public void serialEvent (Serial myPort) {
		//System.out.println("serial event = " + myPort.available());
		try {
			if(serialIsReady){
				byte[] meusBytes = myPort.readBytes();
				int[] meusInts = new int[meusBytes.length];
				String message = "[";
				for (int i = 0; i < meusBytes.length; i++) {
					meusInts[i] = meusBytes[i] & 0xFF;
					message = message + String.format("%3d", meusInts[i]);
					if(i != (meusBytes.length - 1)){
						message = message + ", ";
					}
				}
				message = message.trim();
				message = message + "]";
				this.parseAndAddOrUpdate(meusInts);
				//System.out.println(message + "\t");
				//System.out.println(Arrays.toString(meusInts)+ "\t");
				//System.out.println(parse(meusInts) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(){
		ExperimentoMapper.pollDevice();
	}

	public void settings() {
		size(100, 100);
		//pixelDensity(2);
		//smooth();
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] {ExperimentoSensorParser.class.getName()};
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}

}
