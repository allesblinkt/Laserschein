package laserschein;

import java.util.Vector;

import processing.core.*;

public class Laser3D extends PGraphics2D {

	public static float COORDINATE_RANGE = 16000.0f;

	private final PApplet _myPApplet;
	private final LaserGraphic _myGraphic;

	private Vector<LaserPoint> _myShape;

	private float _myScale;

	private Optimizer _myOptimizer;


	public Laser3D(PApplet thePApplet) {
		_myGraphic = new LaserGraphic();
		_myPApplet = thePApplet;
		_myOptimizer = new Optimizer();
	}


	public void dispose() {
	}


	public boolean displayable() {
		return false;
	}


	public void beginDraw() {
		_myGraphic.reset();
		_myScale = COORDINATE_RANGE / _myPApplet.width;
	}


	public void endDraw() {
		_myOptimizer.optimize(_myGraphic);
		
		redraw();
	}


	private void redraw() {
		LD2000Adaptor.getInstance().draw(this.finalFrame());
	}


	public void vertex(float theX, float theY) {

		final LaserPoint myPoint = new LaserPoint();

		myPoint.x = (int) (theX * _myScale);
		myPoint.y = (int) (theY * _myScale);

		myPoint.color = frgbToInt(strokeR, strokeG, strokeB);

		if (smooth) {
			myPoint.isCorner = false;
		} else {
			myPoint.isCorner = true;
		}

		_myShape.add(myPoint);

	}


	public void vertex(float theX, float theY, float theZ) {
		vertex(theX, theY);
	}


	public void beginShape(int kind) {
		_myShape = new Vector<LaserPoint>();
	}


	public void endShape(int mode) {
		_myGraphic.shapes().add(_myShape);
	}


	@Override
	public void smooth() {
		smooth = true;
	}


	@Override
	public void noSmooth() {
		smooth = false;
	}


	public static int rgbToInt(int theR, int theG, int theB) {
		return (theB << 16) | (theG << 8) | theR;
	}

	
	public static int frgbToInt(float theR, float theG, float theB) {
		return rgbToInt((int) (theR * 255), (int) (theG * 255), (int) (theB * 255));
	}


	public boolean hasFinishedFrame() {
		return true;
	}
	
	public OptimizerSettings settings(){
		return _myOptimizer.settings();
	}
	
	
	public void setSettingsRef(OptimizerSettings theSettings) {
		_myOptimizer.setSettingsRef(theSettings);
	}


	public LaserFrame finalFrame() {
		return _myOptimizer.optimizedFrame();
	}

}
