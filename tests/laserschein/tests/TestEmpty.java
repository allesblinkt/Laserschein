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
		
		laser = new Laserschein(this,Laserschein.EASYLASEUSB2);
		
	}


	public void draw() {
		background(60);
		
		Laser3D renderer = laser.renderer();
		beginRaw(renderer);
		
		stroke(0,0,255);
		translate(mouseX, mouseY);
		noFill(); // important!
		renderer.noSmooth();
		
		laser.output().setScanSpeed(40000);

		
		println(laser.output().getScanSpeed());

		rectMode(CENTER);
		rect(0,0,width*0.1f, height*0.1f);

		//scale(abs(sin(millis() * 0.001f)) + 0.5f);
		
	//	renderer.noSmooth();

		//box(100);
		
		
		//renderer.smooth();
		

	//	ellipse(0,0,200,200);

	//	rotateX(HALF_PI);
	//	ellipse(0,0,200,200);
	///	rotateY(HALF_PI);
	//	ellipse(0,0,200,200);
		
		endRaw();

	}



	public static void main(String[] args) {
		PApplet.main(new String[] { TestEmpty.class.getCanonicalName() });
	}
}
