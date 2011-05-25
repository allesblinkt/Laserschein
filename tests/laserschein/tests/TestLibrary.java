package laserschein.tests;

import processing.core.PApplet;
import laserschein.Laser3D;
import laserschein.Laserschein;

@SuppressWarnings("serial")
public class TestLibrary extends PApplet {

	Laserschein laserschein;


	@Override
	public void setup() {

		frameRate(-1);

		size(500, 500, P3D);

		laserschein = new Laserschein(this);
	}


	@Override
	public void draw() {
		background(60);
		smooth();
		text(frameRate, 5, 15);

		Laser3D laser = laserschein.renderer();

		
		pushMatrix();
		beginRaw(laser);
		laser.noSmooth();

		stroke(255, 0, 0);

		for (int i = 0; i < 10; i++) {
			// line(mouseX, mouseY + (i * 6), width / 2, height / 2);
		}
		// line(cos(millis() * 0.004f) * 100 + 100,
		// sin(millis() * 0.004f) * 100 + 100, width / 2, height / 2);

		stroke(0, 255, 255);

		// line(cos(millis() * 0.004f) * 100 + 100,
		// sin(millis() * 0.004f) * 100 + 100, mouseX, mouseY + (0 * 6));

		stroke(255, 255, 255);
		rectMode(CENTER);

		noFill();

		stroke(255, 255, 0);

		translate(width / 2, height / 2);

		ellipseMode(CENTER);

		laser.smooth();
		rotate(millis() * 0.00051f);

		ellipseMode(CENTER);
		ellipse(-100, 0, 200, 200);
		
		laser.noSmooth();

		
		//ellipse(-100, 0, 200, 200);
		//ellipse(100, 0, 200, 200);

		laser.noSmooth();

		// ellipse(100,0,200,200);

		//rect(0, 0, 200, 200);

		// line(mouseX,mouseY, 200+int(random(50)),200);

		rotateX(millis() * 0.0051f);
		rotateY(millis() * 0.0042f);

		//rect(0, 0, 200, 200);

		noFill();
		// bezier(85, 20, 10, 10, 90, 90, 15, 80);

		endRaw();
		popMatrix();
		
		laserschein.drawDebugView();

	}


	public static void main(String[] args) {
		PApplet.main(new String[] { TestLibrary.class.getCanonicalName() });
	}
}
