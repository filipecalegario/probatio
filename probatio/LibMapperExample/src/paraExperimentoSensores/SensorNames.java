package paraExperimentoSensores;

import java.util.HashMap;

public class SensorNames {
	
	public static HashMap<Integer, String> names;
	
	static{
		names = new HashMap<Integer, String>();
		names.put(0, "Sensor de Efeito Hall");
		names.put(1, "Sensor de Sopro");
		names.put(2, "Piezo 1");
		names.put(3, "Piezo 2");
		names.put(4, "Piezo 3");
		names.put(5, "Piezo 4");
		names.put(6, "Joystick Vertical");
		names.put(7, "Joystick Horizontal");
		names.put(8, "Potenciometro 1");
		names.put(9, "Potenciometro 2");
		names.put(10, "Potenc. de Membrana");
		names.put(11, "Sensor de Pressao");
		names.put(12, "Botao 1");
		names.put(13, "Botao 2");
		names.put(14, "Botao 3");
		names.put(15, "Botao 4");
		names.put(16, "Botao 5");
		names.put(17, "Botao 6");
		names.put(18, "Botao 7");
		names.put(19, "Botao 8");
		names.put(20, "Botao do Encoder Rot. 1");
		names.put(21, "Botao do Encoder Rot. 2");
		names.put(22, "Encoder Rotativo 1");
		names.put(23, "Encoder Rotativo 2");
	}
	
	public static String getNameFromIndex(int index){
		return names.get(index);
	}
	
}
