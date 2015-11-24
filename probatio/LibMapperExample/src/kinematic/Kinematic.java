package kinematic;

public class Kinematic {
	
	private long lastTime;
	private long currentTime;
	private float lastPosition;
	private float currentPosition;
	private float lastSpeed;
	private float currentSpeed;
	private float acceleration;
	
	public Kinematic(float value, long time) {
		this.lastTime = 0;
		this.currentTime = time;
		this.lastPosition = 0;
		this.currentPosition = value;
		this.lastSpeed = 0;
		this.currentSpeed = 0;
		this.acceleration = 0;
	}
	
	public void updateValue(float value, long time){
		this.lastPosition = this.currentPosition;
		this.lastTime = this.currentTime;
		this.lastSpeed = this.currentSpeed;
		this.currentPosition = value;
		this.currentTime = time;
		this.currentSpeed = (this.currentPosition - this.lastPosition)/(this.currentTime - this.lastTime);
		this.acceleration = (this.currentSpeed - this.lastSpeed)/(this.currentTime - this.lastTime);
	}

	public float getCurrentSpeed() {
		return currentSpeed;
	}

	public float getAcceleration() {
		return acceleration;
	}
	
	
	
	

}
