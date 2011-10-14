package laserschein.tests;

import processing.core.PApplet;
import laserschein.*;

/**
 * This test the drawing of random lines
 * 
 * @author allesblinkt
 * 
 */
@SuppressWarnings("serial")
public class TestRandom extends PApplet {

	Laserschein laser;


	public void setup() {
		size(400, 400, P3D);
		frameRate(-1); // as fast as possible

		smooth();
		
		laser = new Laserschein(this, Laserschein.EASYLASEUSB2);
		
	}


	public void draw() {
		background(60);
		text(frameRate, 5, 15);



		Laser3D renderer = laser.renderer();
		beginRaw(renderer);
		
		int myCount = (mouseX * 200)/width;
		
		
		noFill();
		

		beginShape();
		for(int i = 0; i < myCount; i++){
			vertex(random(0,width),random(0,height) );
		}
		endShape();
		stroke(random(255),random(255),random(255));

		if(frameCount% 2 == 0){


		}
		
		//rect(100,100,200,200);

		
		noFill(); // important!
		endRaw();
		
		
		//laser.drawDebugView();

	}



	public static void main(String[] args) {
		PApplet.main(new String[] { TestRandom.class.getCanonicalName() });
	}
}
