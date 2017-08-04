package oiraga.model;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;

public class Player extends GameObject{
	private int speed;
	private String name;
	public Player(int x, int y, int radius,int speed, Color color,String name) {
		super(x, y, radius, color);
		this.speed=speed;
		this.name=name;
		
		
		
		
	}
	
	
	/**
	 * 
	 * Drawing Player
	 */
	
	@Override
	public void draw(Graphics2D g2d) {
			super.draw(g2d);
	
			FontMetrics fontMetrics=g2d.getFontMetrics(g2d.getFont());
			int widht=fontMetrics.stringWidth(name);
			int nameX=getX()+ ((getRadius()-widht)/2);
			int nameY=getY()-fontMetrics.getHeight();
			
			g2d.drawString(name, nameX, nameY);
			
			
	}
	
	

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
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
