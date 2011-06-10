/**
 *  
 *  Laserschein. interactive ILDA output from processing and java
 *
 *  2011 by Benjamin Maus
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author Benjamin Maus (http://www.allesblinkt.com)
 *
 */
package laserschein;

import processing.core.*;

/**
 * The main library
 * 
 * @author allesblinkt
 */
public class Laserschein {

	private PApplet _myParent;
	private Laser3D _myRenderer;


	
	/**
	 * Inits the library "Processing style"
	 * 
	 * @param theParent the main Processing applet. Almost anytime: this
	 */
	public Laserschein(PApplet theParent) {
		_myParent = theParent;
		_myParent.registerDispose(this);

		Logger.printInfo("Laserschein", "Initializing the Laser");
		
		LD2000Adaptor.getInstance().initialize();  // TODO: make this non-fixed
		
		
		
		_myRenderer = new Laser3D(_myParent);
	}


	/**
	 * Get the renderer that processing can use with beginRaw() and endRaw(). So one can 
	 * draw stuff on the laser with the processing syntax.
	 * 
	 * @return a reference to the renderer 
	 */
	public Laser3D renderer() {
		return _myRenderer;
	}


	/**
	 * Same as {@link laserschein.Laserschein#renderer()}
	 * 
	 * @see laserschein.Laserschein#renderer()
	 */
	public Laser3D getRenderer() {
		return _myRenderer;
	}
	
	public OptimizerSettings settings(){
		return _myRenderer.settings();
	}
	
	
	public void setSettingsRef(OptimizerSettings theSettings) {
		 _myRenderer.setSettingsRef(theSettings);
	}


	/**
	 * Cleans up when the processing applet exits. Gets called automatically. Do not worry.
	 */
	public void dispose() {
		Logger.printInfo("Laserschein.dispose", "Destroying the Laser");
		LD2000Adaptor.getInstance().destroy(); //TODO: make this non-fixed
	}


	/**
	 * Draws a very crude (for now) debug view that shows what the {@link laserschein.Optimizer} generates
	 */
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
	

}
