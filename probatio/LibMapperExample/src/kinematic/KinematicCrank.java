package kinematic;

import processing.core.PApplet;

public class KinematicCrank implements Kinematics{

	private long lastTime;
	private long currentTime;
	private float lastPosition;
	private float currentPosition;
	private float lastSpeed;
	private float currentSpeed;
	private float acceleration;
	private float maxSpeed;
	private float minSpeed;
	private float maxAcceleration;
	private float minAcceleration;
	private float[] averageDeltaT;
	private float currentAverageSpeed;
	private float lastAverageSpeed;
	private int index;
	private int stuckCounter;

	public KinematicCrank() {
		this.lastTime = 0;
		this.currentTime = System.nanoTime();
		this.lastPosition = 0;
		this.currentPosition = 0;
		this.lastSpeed = 0;
		this.currentSpeed = 0;
		this.acceleration = 0;
		this.index = 0;
		this.currentAverageSpeed = 0;
		this.averageDeltaT = new float[100];
	}

	public void updateValue(float value) {
		this.lastPosition = this.currentPosition;
		this.lastAverageSpeed = this.currentAverageSpeed;
		this.currentPosition = value;
		float deltaP = this.currentPosition - this.lastPosition;
		
		if(deltaP != 0){
			this.lastTime = this.currentTime;
			this.currentTime = (long) System.nanoTime();
			float deltaT = (this.currentTime - this.lastTime)/1000.0f;
			this.averageDeltaT[index] = deltaT;
			//System.out.println(1.0f/getAverageDeltaT()*1e4);
			this.currentAverageSpeed = 1.0f/getAverageDeltaT()*1e4f;
			float deltaSpeed = this.currentAverageSpeed - this.lastAverageSpeed;
			this.acceleration = Math.abs(deltaSpeed/getAverageDeltaT()*1e8f);
			index = (index + 1)%this.averageDeltaT.length;
			stuckCounter = 0;
		}
		
		
		
		
		
		
		if(this.lastAverageSpeed == this.currentAverageSpeed){
			//System.out.println(stuckCounter++);
			stuckCounter++;
			if(stuckCounter > 100){
				this.currentAverageSpeed = this.currentAverageSpeed - 0.01f;
				if (this.currentAverageSpeed < 0.0f) {
					this.currentAverageSpeed = 0.0f;
				}
			}
		}
	}
	
	private float getAverageDeltaT(){
		float result = 0;
		float sum = 0;
		for (int i = 0; i < this.averageDeltaT.length; i++) {
			sum = sum + this.averageDeltaT[i];
		}
		result = sum / (1.0f*this.averageDeltaT.length);
		return result;
	}

	private float getAverageSpeed() {
		//float mappedValue = PApplet.map(this.currentAverageSpeed, 0.0f, 1.7f, 0, 255);
		float mappedValue = PApplet.map(this.currentAverageSpeed, 0.0f, 2.0f, 0, 255);
		mappedValue = PApplet.constrain(mappedValue, 0, 255);
		//System.out.println(this.averageSpeed + " => " + mappedValue);
		return mappedValue;
	}

	private float getAcceleration() {
		System.out.println(this.acceleration);
		return this.acceleration;
	}

	@Override
	public float getSpeed() {
		return getAverageSpeed();
	}

}
