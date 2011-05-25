package laserschein.tests;


import processing.core.PApplet;
import laserschein.*;

/**
 * This test the drawing of nothing
 * 
 * @author allesblinkt
 * 
 */
@SuppressWarnings("serial")
public class TestEmpty extends PApplet {

	Laserschein laser;


	public void setup() {
		size(400, 400, P3D);
		frameRate(-1); // as fast as possible

		smooth();
		
		laser = new Laserschein(this);
		
	}


	public void draw() {
		background(60);
		
		Laser3D renderer = laser.renderer();
		beginRaw(renderer);
		
		stroke(255,0,0);
		translate(width/2, height/2);
		rotateX(millis() * 0.002f);
		rotateY(millis() * 0.002123243f);

		//scale(abs(sin(millis() * 0.001f)) + 0.5f);
		
		renderer.noSmooth();

		box(100);
		
		
		renderer.smooth();
		

		ellipse(0,0,200,200);

		rotateX(HALF_PI);
		ellipse(0,0,200,200);
		rotateY(HALF_PI);
		ellipse(0,0,200,200);
		
		noFill(); // important!
		endRaw();

	}



	public static void main(String[] args) {
		PApplet.main(new String[] { TestEmpty.class.getCanonicalName() });
	}
}
