package freetest;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class JSONTest extends PApplet {
	
	public void setup() {
		JSONObject json = this.loadJSONObject("sculpture_namespace.json");
		JSONObject device = json.getJSONObject("device");
		JSONArray inputs = device.getJSONArray("inputs");
		for (int i = 0; i < inputs.size(); i++) {
			JSONObject input = inputs.getJSONObject(i);
			String name = input.getString("name");
			//String type = input.getString("type");
			Integer minimum = input.getInt("minimum");
			Integer maximum = input.getInt("maximum");
			String[] parts = split(name, "/");
			Integer channel = Integer.parseInt(split(parts[1], "_")[0]);
			Integer controlNumber = Integer.parseInt(parts[3]);
			String description = parts[4] + "->" + parts[5];
			System.out.println(channel + " " + controlNumber + " " + description);
		}
	}

	public void draw() {

	}

	public static void main(String[] args) {
		String[] appletArgs = new String[] {"freetest.JSONTest"};
		if (args != null) {
			PApplet.main(concat(appletArgs, args));
		} else {
			PApplet.main(appletArgs);
		}

	}

}
