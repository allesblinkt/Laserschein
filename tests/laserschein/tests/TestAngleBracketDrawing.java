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
public class TestAngleBracketDrawing extends PApplet {

	Laserschein laser;


	public void setup() {
		size(400, 400, P3D);
		frameRate(-1); // as fast as possible

		smooth();
		
		laser = new Laserschein(this, Laserschein.EASYLASEUSB2);
		
	}


	public void draw() {
		background(60);
		


		Laser3D renderer = laser.renderer();
		beginRaw(renderer);

		noFill(); // important!
		
		pushMatrix();
		translate(100,100);
		rotate(millis() * 0.0005f);
		drawBracket(20, (mouseY / (float) width) * 180f);
		popMatrix();

		
		pushMatrix();
		translate(200,100);
		rotate(millis() * 0.0005f);

		drawBracket(60, (mouseY / (float) width) * 180f);
		popMatrix();

		pushMatrix();
		translate(300,100);
		drawBracket(90, (mouseY / (float) width) * 180f);
		popMatrix();
	
		
		pushMatrix();
		translate(100,200);
		drawBracket(120, (mouseY / (float) width) * 180f);
		popMatrix();
		
		
		point(100,100);
		point(300,300);


		endRaw();
		
		
		background(0);
		
		
		
		laser.drawDebugView();

	}


	void drawBracket(float theAngle, float theLength) {
		float myY = sin(radians(theAngle) * 0.5f) * theLength;
		float myX = cos(radians(theAngle) * 0.5f) * theLength;

		stroke(255, 0, 0);
		beginShape();
		vertex(-myX, -myY);
		vertex(0, 0);
		vertex(myX, -myY);
		endShape();

		stroke(255, 0, 0);
		beginShape();
		vertex(myX, myY);
		vertex(0, 0);
		vertex(-myX, myY);
		endShape();
	}


	public static void main(String[] args) {
		PApplet.main(new String[] { TestAngleBracketDrawing.class.getCanonicalName() });
	}
}
