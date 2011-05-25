package laserschein;

import processing.core.*;

public class Laserschein {

	private PApplet _myParent;
	private Laser3D _myRenderer;


	public Laserschein(PApplet theParent) {
		_myParent = theParent;
		_myParent.registerDispose(this);

		Logger.printInfo("Laserschein", "Initializing the Laser");
		LD2000Adaptor.getInstance().initialize();

		_myRenderer = new Laser3D(_myParent);
	}


	public Laser3D renderer() {
		return _myRenderer;
	}


	public Laser3D getRenderer() {
		return _myRenderer;
	}


	public void dispose() {
		Logger.printInfo("Laserschein.dispose", "Destroying the Laser");
		LD2000Adaptor.getInstance().destroy();
	}


	public void drawDebugView() {
		if (_myRenderer.hasFinishedFrame()) {
			final LaserFrame myFrame = _myRenderer.finalFrame();

			renderPointView(myFrame);
			renderPerformanceView(myFrame);

		}
	}


	private void renderPerformanceView(LaserFrame theFrame) {
		final PApplet pa = _myParent;

		pa.fill(255, 0, 0);
		pa.noStroke();
		pa.text(theFrame.points().size(), 0, 40);

		float myWidth = theFrame.points().size() / 6000f; // TODO: remove
															// hardcode
		myWidth *= pa.width;

		pa.rectMode(PApplet.CORNER);
		pa.rect(0, 0, myWidth, 20);

	}


	private void renderPointView(LaserFrame theFrame) {
		final PApplet pa = _myParent;

		float myScale = pa.width / (float) Laser3D.COORDINATE_RANGE;

		pa.noFill();

		pa.stroke(255);

		pa.beginShape();
		for (LaserPoint p : theFrame.points()) {

			if (p.isBlanked) {
				pa.endShape();
				pa.beginShape();
			}

			pa.vertex(p.x * myScale, p.y * myScale);
		}

		pa.endShape();

		pa.stroke(255, 255, 0);

		pa.beginShape();
		for (LaserPoint p : theFrame.points()) {

			if (!p.isBlanked) {
				pa.endShape();
				pa.beginShape();
			}

			pa.vertex(p.x * myScale, p.y * myScale);
		}

		pa.endShape();

		pa.rectMode(PApplet.CENTER);

		LaserPoint ppv = null;
		int count = 1;
		int oldCount = 1;
		boolean isNew = true;

		for (LaserPoint p : theFrame.points()) {
			pa.stroke(128, 100);
			pa.noFill();

			LaserPoint pv = p;

			if (ppv != null && pv.isCoincided(ppv)) {
				count++;
				oldCount = count;
				isNew = false;
			} else {
				oldCount = count;
				count = 1;
				isNew = true;
			}

			pa.rect(pv.x * myScale, pv.y * myScale, 10, 10);

			if (isNew && ppv != null) {
				pa.fill(255, 0, 0);
				pa.noStroke();

				pa.text(oldCount, ppv.x * myScale, ppv.y * myScale);
			}

			ppv = pv;

		}

		if (ppv != null) {

			pa.fill(255, 0, 0);
			pa.noStroke();
			pa.text(oldCount, ppv.x * myScale, ppv.y * myScale);
		}
	}
	
	public OptimizerSettings settings(){
		return _myRenderer.settings();
	}
	
	
	public void setSettingsRef(OptimizerSettings theSettings) {
		 _myRenderer.setSettingsRef(theSettings);
	}
}
