package laserschein.tests;

import processing.core.PApplet;
import laserschein.*;

/**
 * This test the drawing of different angles
 * 
 * @author allesblinkt
 * 
 */
@SuppressWarnings("serial")
public class TestAngle extends PApplet {



	public void setup() {
		size(400, 400, P3D);
		frameRate(-1); // as fast as possible

		smooth();
		
		
	}


	public void draw() {
		background(60);
		
		LaserPoint a = new  LaserPoint(50,200);
		LaserPoint b = new  LaserPoint(mouseX,mouseY);

		LaserPoint c = new  LaserPoint(350,200);

		
		float angle = LaserPoint.getAngle(a, b, c);
		
		beginShape();
		vertex(a.x, a.y);
		vertex(b.x, b.y);
		vertex(c.x, c.y);

		endShape();
		
		
		text(angle, 20, 20);
	}


	

	public static void main(String[] args) {
		PApplet.main(new String[] { TestAngle.class.getCanonicalName() });
	}
}
