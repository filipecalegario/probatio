package display;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import utils.Utils;

public class GrapherMain extends PApplet {

	private static final long serialVersionUID = 1L;
	private PImage myImage;
	private Grapher[] grapher;
	private Utils utils;
	int counter = 0;
	
	int index = 0;

	public void settings(){
		size(800, 600);
		pixelDensity(2);
		smooth();
	}
	
	public void setup() {
		//println(displayDen≈jsity());
		utils = new Utils(this);
		grapher = new Grapher[12];
		populateGrapher();
		background(255);
	}

	private void populateGrapher() {
		for (int i = 0; i < 6; i++) {
			grapher[i] = new Grapher(this, 0, i*height/6, width/2, height/6, color(255), utils.pickAColor(index++));
			//grapher[i+6] = new Grapher(this, width/2, i*height/6, width/2, height/6, color(255), utils.pickAColor(index++));
		}
		for (int i = 6; i < 12; i++) {
			grapher[i] = new Grapher(this, width/2, (i-6)*height/6, width/2, height/6, color(255), utils.pickAColor(index++));
		}
	}

	public void draw() {
		updateGrapher(counter++);
	}
	
	private void updateGrapher(int x) {
		float mySin = sin((x * PI / 180));
		float sinToGrapher = map(mySin, -1, 1, 0, 255);
		float myCos = cos((x * PI / 180));
		float cosToGrapher = map(myCos, -1, 1, 0, 255);
		for (int j = 0; j < grapher.length; j++) {
			//if(j%2==0){
				//grapher[j].updateDisplay(sinToGrapher);
			//} else {
			//	grapher[j].updateDisplay(cosToGrapher);
			//}
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKey() == ' '){
			//grapher.setPointerSpeed(grapher.getPointerSpeed()+0.1f);
		} else if(event.getKey() == 'c'){
			//grapher.setPointerSpeed(grapher.getPointerSpeed()-0.1f);
		}
		
	}

	public static void main(String args[]) {
		PApplet.main(GrapherMain.class.getName());
	}

}