package kinematic;

import processing.core.PApplet;

public class KinematicGeneralBlocks implements Kinematics{
	
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
	private float[] averageSpeedWindow;
	private int index;
	
	public KinematicGeneralBlocks() {
		this.lastTime = 0;
		this.currentTime = System.nanoTime();
		this.lastPosition = 0;
		this.currentPosition = 0;
		this.lastSpeed = 0;
		this.currentSpeed = 0;
		this.acceleration = 0;
		this.index = 0;
		this.averageSpeedWindow = new float[100];
	}
	
	public void updateValue(float value){
		this.lastPosition = this.currentPosition;
		this.lastTime = this.currentTime;
		this.lastSpeed = this.currentSpeed;
		this.currentPosition = value;
		this.currentTime = System.nanoTime();
		float deltaT = (this.currentTime - this.lastTime)/1000.0f;
		this.currentSpeed = (this.currentPosition - this.lastPosition)/deltaT;
		this.acceleration = (this.currentSpeed - this.lastSpeed)/deltaT;
		this.maxSpeed = Math.max(this.maxSpeed, this.currentSpeed);
		this.minSpeed = Math.min(this.maxSpeed, this.currentSpeed);
		this.maxAcceleration = Math.max(this.maxAcceleration, this.acceleration);
		this.minAcceleration = Math.min(this.maxAcceleration, this.acceleration);
		this.averageSpeedWindow[index] = Math.abs(currentSpeed);
		index = (index + 1)%100;
		//System.out.println("Dt = " + deltaT + " | speed = ");
		//System.out.printf("Speed = [%f , %f] | Acc = [%f, %f] | AverageSpeed = %f%n", Math.abs(minSpeed), Math.abs(maxSpeed), Math.abs(minAcceleration), Math.abs(maxAcceleration),this.getAverageSpeed());
	}

	private float getCurrentSpeed() {
		return (float) (Math.abs(currentSpeed*1e6));
	}
	
	private float getAverageSpeed(){
		float result = 0;
		float sum = 0;
		for (int i = 0; i < averageSpeedWindow.length; i++) {
			sum = sum + averageSpeedWindow[i];
		}
		result = sum / (1.0f*averageSpeedWindow.length);
		return result;
	}

	public float getAcceleration() {
		return acceleration;
	}

	@Override
	public float getSpeed() {
		float f = this.getAverageSpeed()*1000;
		f = PApplet.map(f, 0.0f, 4.3f, 0, 255);
		f = PApplet.constrain(f, 0, 255);
		//System.out.println(f);
		return f;
	}

}
