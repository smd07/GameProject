package oiraga.model;

import java.awt.Color;

public class Enemy extends GameObject{
	private int speed;

	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public Enemy(int x, int y, int radius,int speed, Color color) {
		super(x, y, radius, color);
		this.speed=speed;
	}

	@Override
	public void setRadius(int radius) {
		// TODO Auto-generated method stub
		super.setRadius(radius);
		if (radius>200) {
			setRadius(200);
		}
		if (radius<10) {
			setRadius(10);
		}
	}
}
