package gui.prototype.recife.october;

import controlP5.ControlP5;
import controlP5.Knob;
import processing.core.PApplet;
import processing.sound.SawOsc;
import processing.sound.TriOsc; 

public class ProcessingControlP5Sound extends PApplet {

	/*
This is a saw-wave oscillator. The method .play() starts the oscillator. There
 are several setters like .amp(), .freq(), .pan() and .add(). If you want to set all of them at
 the same time use .set(float freq, float amp, float add, float pan)
	 */

	TriOsc tri;
	SawOsc saw;

	ControlP5 cp5;

	Knob myKnobA;
	Knob myKnobB;
	Knob myKnobC;

	int knobAmp;
	int knobFreq;
	int knobPan;

	boolean toggleSaw = false;
	boolean toggleTri = false;

	public void setup() {

		smooth();
		noStroke();

		cp5 = new ControlP5(this);

		// Create and start the triangle wave oscillator.

		tri = new TriOsc(this);
		saw = new SawOsc(this);

		myKnobA = cp5.addKnob("knobAmp")
				.setRange(0, 255)
				.setValue(14)
				.setPosition(100, 70)
				.setRadius(50)
				.setDragDirection(Knob.VERTICAL)
				;

		myKnobB = cp5.addKnob("knobFreq")
				.setRange(0, 255)
				.setValue(25)
				.setPosition(100, 210)
				.setRadius(50)
				//.setNumberOfTickMarks(10)
				//.setTickMarkLength(4)
				//.snapToTickMarks(true)
				.setColorForeground(color(255))
				.setColorBackground(color(0, 160, 100))
				.setColorActive(color(255, 255, 0))
				.setDragDirection(Knob.VERTICAL)
				;

		myKnobC = cp5.addKnob("knobPan")
				.setRange(0, 255)
				.setValue(50)
				.setPosition(200, 70)
				.setRadius(50)
				.setDragDirection(Knob.VERTICAL)
				;

		cp5.addToggle("toggleSaw")
		.setPosition(400, 100)
		.setSize(20, 20)
		;

		cp5.addToggle("toggleTri")
		.setPosition(400, 150)
		.setSize(20, 20)
		;

		//Start the sqr Oscillator. There will be no sound in the beginning
		//unless the mouse enters the   
		//tri.play();
		//saw.play();
	}

	public void draw() {
		background(0);
		// Map mouseY from 0.0 to 1.0 for amplitude
		tri.amp(map(knobAmp, 0, 255, 0.0f, 1.0f));
		saw.amp(map(knobAmp, 0, 255, 0.0f, 1.0f));

		// Map mouseX from 20Hz to 1000Hz for frequency  
		tri.freq(map(knobFreq, 0, 255, 80.0f, 1000.0f));
		saw.freq(map(knobFreq, 0, 255, 80.0f, 1000.0f));


		// Map mouseX from -1.0 to 1.0 for left to right 
		tri.pan(map(knobPan, 0, 255, -1.0f, 1.0f));
		saw.pan(map(knobPan, 0, 255, -1.0f, 1.0f));
	}

	public void toggleSaw(boolean b){
		if(b){
			saw.play();
		} else {
			saw.stop();
		}
	}

	public void toggleTri(boolean b){
		if(b){
			tri.play();
		} else {
			tri.stop();
		}
	}

	public void keyPressed() {
		switch(key) {
		case('1'):
			tri.play();
		break;
		case('2'):
			tri.stop();
		break;
		case('3'):
			saw.play();
		break;
		case('4'):
			saw.stop();
		break;
		}
	}
	public void settings() {  size(700, 400);  smooth(); }
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "gui.prototype.recife.october.ProcessingControlP5Sound" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
