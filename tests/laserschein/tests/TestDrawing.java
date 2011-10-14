package laserschein.tests;

import laserschein.Laser3D;
import laserschein.Laserschein;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class TestDrawing extends PApplet {

	Laser3D _myRenderer;
	Laserschein _mySchein;
	
	@Override
	public void setup() {

		frameRate(-1);

		size(500, 500, P3D);
		
		_mySchein = new Laserschein(this, Laserschein.EASYLASEUSB2);
	}


	@Override
	public void draw() {
		background(60);

		text(frameRate, 5, 15);

		drawOnLaser();
//		if(frameCount < 10){
//			drawOnLaser();
//		} else {
//			_myRenderer.redraw();
//		}
		
		
		

	}
	
	
	public void drawOnLaser(){
		 _myRenderer = _mySchein.renderer();

			beginRaw(_myRenderer);
			_myRenderer.noSmooth();
			_myRenderer.noFill();

			stroke(255, 0, 0);

			for (int i = 0; i < 10; i++) {
				 line(mouseX, mouseY + (i * 6), width / 2, height / 2);
			}
			// line(cos(millis() * 0.004f) * 100 + 100,
			// sin(millis() * 0.004f) * 100 + 100, width / 2, height / 2);

			stroke(255,0,0);

			rect(mouseX, mouseY, 200, 200);

			// line(cos(millis() * 0.004f) * 100 + 100,
			// sin(millis() * 0.004f) * 100 + 100, mouseX, mouseY + (0 * 6));

			stroke(255, 0, 0);
			rectMode(CENTER);

			noFill();
			stroke(255, 0, 0);

			translate(width / 2, height / 2);

			ellipseMode(CENTER);

			_myRenderer.smooth();

			ellipse(-100, 0, 200, 200);
			

			ellipse(100, 0, 200, 200);
			_myRenderer.noSmooth();
			


			// ellipse(100,0,200,200);

			rect(0, 0, 200, 200);

			// line(mouseX,mouseY, 200+int(random(50)),200);

			rotateX(millis() * 0.0051f);
			rotateY(millis() * 0.0042f);

			rect(0, 0, 200, 200);

			noFill();
			// bezier(85, 20, 10, 10, 90, 90, 15, 80);

			endRaw();
	}


	public static void main(String[] args) {
		PApplet.main(new String[] { TestDrawing.class.getCanonicalName() });
	}
}
