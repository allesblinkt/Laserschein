package laserschein.tests;


import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;
import processing.core.PShape;
import laserschein.*;

/**
 * Adjust settings
 * 
 * @author allesblinkt
 * 
 */
@SuppressWarnings("serial")
public class TestAdjustSettings extends PApplet {

	Laserschein laser;
	ControlP5 controlP5;

	Slider sliderAngleCornerPoints;
	Slider sliderExtraCornerPoints ;
	Slider sliderExtraCornerPointsStart ;
	Slider sliderExtraCornerPointsEnd ;

	Slider sliderMaxTravel;

	Slider sliderExtraBlanksStart;
	Slider sliderExtraBlanksEnd;


	PShape svg;




	public void setup() {
		size(400, 400, P3D);
		frameRate(-1); // as fast as possible

		smooth();

		laser = new Laserschein(this);

		svg = loadShape("foo.svg");









		controlP5 = new ControlP5(this);

		OptimizerSettings settings = laser.settings();


		sliderAngleCornerPoints = controlP5.addSlider("angleCornerPoints", 0, 20, settings.extraCornerPointsAngleDependent, 10, 40, 100, 20);
		sliderAngleCornerPoints.setNumberOfTickMarks(21);
		sliderAngleCornerPoints.snapToTickMarks(true);


		sliderExtraCornerPoints = controlP5.addSlider("extraCornerPoints", 0, 20, settings.extraCornerPoints, 10, 70, 100, 20);
		sliderExtraCornerPoints.setNumberOfTickMarks(21);
		sliderExtraCornerPoints.snapToTickMarks(true);


		sliderExtraCornerPointsStart = controlP5.addSlider("extraCornerPointsStart", 0, 20, settings.extraCornerPointsStart, 10, 100, 100, 20);
		sliderExtraCornerPointsStart.setNumberOfTickMarks(21);
		sliderExtraCornerPointsStart.snapToTickMarks(true);

		sliderExtraCornerPointsEnd = controlP5.addSlider("extraCornerPointsEnd", 0, 20, settings.extraCornerPointsEnd, 10, 130, 100, 20);
		sliderExtraCornerPointsEnd.setNumberOfTickMarks(21);
		sliderExtraCornerPointsEnd.snapToTickMarks(true);


		sliderExtraBlanksStart = controlP5.addSlider("extraBlanksStart", 0, 20, settings.extraBlankPointsStart, 10, 160, 100, 20);
		sliderExtraBlanksStart.setNumberOfTickMarks(21);
		sliderExtraBlanksStart.snapToTickMarks(true);

		sliderExtraBlanksEnd = controlP5.addSlider("extraBlanksEnd", 0, 20, settings.extraBlankPointsEnd, 10, 190, 100, 20);
		sliderExtraBlanksEnd.setNumberOfTickMarks(21);
		sliderExtraBlanksEnd.snapToTickMarks(true);

		sliderMaxTravel = controlP5.addSlider("maxTravel", 2, 2000, settings.maxTravel, 10, 220, 100, 20);
		sliderMaxTravel.setNumberOfTickMarks(21);
		sliderMaxTravel.snapToTickMarks(true);

	}


	public void applySliders(){
		laser.settings().extraCornerPointsAngleDependent = (int) sliderAngleCornerPoints.value();
		laser.settings().extraCornerPoints = (int) sliderExtraCornerPoints.value();
		laser.settings().extraCornerPointsStart = (int) sliderExtraCornerPointsStart.value();
		laser.settings().extraCornerPointsEnd = (int) sliderExtraCornerPointsEnd.value();

		laser.settings().extraBlankPointsStart = (int) sliderExtraBlanksStart.value();
		laser.settings().extraBlankPointsEnd = (int) sliderExtraBlanksEnd.value();

		laser.settings().maxTravel = (int) sliderMaxTravel.value();

	}


	public void draw() {

		applySliders();
		background(60);

		pushMatrix();
		noFill();
		//drawOnLaser();
		drawOnLaser();

		popMatrix();


		pushMatrix();
		laser.drawDebugView();
		popMatrix();

		noSmooth();
		controlP5.draw();

	}


	public void drawOnLaser2(){
		Laser3D renderer = laser.renderer();

		beginRaw(renderer);
		renderer.noSmooth();
		renderer.noFill();

		shape(svg);

		endRaw();
	}


	public void drawOnLaser(){
		Laser3D renderer = laser.renderer();

		beginRaw(renderer);
		renderer.noSmooth();
		renderer.noFill();

		//			stroke(255, 0, 0);
		//
		//			for (int i = 0; i < 5; i++) {
		//				 line(mouseX, mouseY + (i * 6), width / 2, height / 2);
		//			}
		//			// line(cos(millis() * 0.004f) * 100 + 100,
		//			// sin(millis() * 0.004f) * 100 + 100, width / 2, height / 2);
		//
		//			stroke(0,255,0);
		//
		//			rect(mouseX, mouseY, 200, 200);
		//
		//			// line(cos(millis() * 0.004f) * 100 + 100,
		//			// sin(millis() * 0.004f) * 100 + 100, mouseX, mouseY + (0 * 6));
		//
		//			stroke(255, 0, 0);
		//			rectMode(CENTER);
		//
		//			noFill();
		//			stroke(0, 0, 255);
		//
		//			translate(width / 2, height / 2);
		//
		//			ellipseMode(CENTER);
		//
		//			renderer.smooth();
		//
		//			ellipse(-100, 0, 200, 200);
		//			
		//
		//			ellipse(100, 0, 200, 200);
		//			renderer.noSmooth();
		//			
		//
		//
		//			// ellipse(100,0,200,200);
		//
		//			rect(0, 0, 200, 200);
		//
		//			// line(mouseX,mouseY, 200+int(random(50)),200);
		//
		//			rotateX(millis() * 0.0051f);
		//			rotateY(millis() * 0.0042f);
		//
		//			rect(0, 0, 200, 200);
		//
		//			noFill();
		//			// bezier(85, 20, 10, 10, 90, 90, 15, 80);


		noFill(); // important!


		float progress = -1;


		translate(width/2, height/2);

		/* Cross */
		stroke(255);
		pushMatrix();
		rotate( (PI/3 ) * progress);
		line(100, 0, 0, 0);
		rotate(TWO_PI / 3);
		line(100, 0, 0, 0);
		rotate(TWO_PI / 3);
		line(100, 0, 0, 0);
		popMatrix();



		/* Yellow ring */
		stroke(255);

		beginShape();

		for(int i = 0; i < 7; i++) {
			float angle = (float) ((i / 6.0) * TWO_PI); 
			float x = (float)(cos( angle) * 100 * (1 - progress * 0.1));
			float y = (float) (sin( angle) * 100 * (1-progress * 0.1));

			vertex(x, y);
		}

		endShape();


		/* Magenta */
		stroke(255);


		//myShearYX(PI/3f);
		//rect(0,0,100,100);


		float trapezoidScale =      (float) (0.8 + (  progress * 0.2));

		pushMatrix();
		translate(25, -43);
		scale(trapezoidScale);
		trapezoid();
		popMatrix();


		pushMatrix();
		rotate(TWO_PI / 3);
		translate(25, -43);
		scale(trapezoidScale);
		trapezoid();
		popMatrix();


		pushMatrix();
		rotate(2*TWO_PI / 3);
		translate(25, -43);
		scale(trapezoidScale);
		trapezoid();
		popMatrix();




		endRaw();
	}



	void trapezoid() {



		beginShape();
		vertex(-25, 43);
		vertex(-75, -43);
		vertex(25, -43);
		vertex(75, 43);

		endShape(CLOSE);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { TestAdjustSettings.class.getCanonicalName() });
	}
}
