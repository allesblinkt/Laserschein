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

import laserschein.AbstractLaserOutput.OutputState;
import processing.core.PApplet;

/**
 * The main library
 * 
 * @author allesblinkt
 */
public class Laserschein {
	
	public static final Class<LD2000Adaptor> LD2000 = LD2000Adaptor.class;
	public static final Class<EasylaseUsb2Adaptor> EASYLASEUSB2 = EasylaseUsb2Adaptor.class;


	private PApplet _myParent;
	private Laser3D _myRenderer;

	private AbstractLaserOutput _myOutput;
//	private final SimulatorWindow _myControlWindow;

	

	/**
	 * Inits the library "Processing style"
	 * 
	 * @param theParent the main Processing applet. Almost anytime: this
	 */
	public Laserschein(PApplet theParent) {
		this(theParent, LD2000Adaptor.class);
	}

	
	/**
	 * Lets you choose what output module should be used for the laser output.
	 * Currently supported are "LD2000" and "Easylase"
	 * 
	 * @see Laserschein()
	 * @param theParent Your PApplet
	 * @param theOutputClassName Name of the output module
	 */
	public Laserschein(PApplet theParent, final String theOutputClassName){

		Class<? extends AbstractLaserOutput> myClass = LD2000Adaptor.class;
				
		if(theOutputClassName.equalsIgnoreCase("LD2000")){
			myClass = LD2000Adaptor.class;
		} else if(theOutputClassName.equalsIgnoreCase("Easylase")){
			myClass = EasylaseUsb2Adaptor.class;
		} else {
			Logger.printError("Laserschein", "No suitable output module found. Currently LD2000 and Easylase are supported as output modules.");
		}
		
		init(theParent, myClass);
	}
	
		
	/**
	 * Init the library and the output module directly. Accepts a class, so you could write your own output module
	 * 
	 * @param theParent Your PApplet
	 * @param theOutputClass The class: e.g.   LD2000Adaptor.class
	 */
	public Laserschein(PApplet theParent, Class<? extends AbstractLaserOutput> theOutputClass){
		init(theParent, theOutputClass);
	}
		
	
	private void init(final PApplet theParent, final Class<? extends AbstractLaserOutput> theOutputClass) {
		_myParent = theParent;
		_myParent.registerDispose(this);
	
				
		Logger.printInfo("Laserschein", "Initializing the Laser");

		try {
			_myOutput = (AbstractLaserOutput)theOutputClass.newInstance();
			_myOutput.initialize();
		} catch (InstantiationException e) {
			Logger.printError("Laserschein", "This is not a class supported as an output");
		} catch (IllegalAccessException e) {
			Logger.printError("Laserschein", "This is not a class supported as an output");
		}

	
		_myRenderer = new Laser3D(_myParent, this);
		//	_myControlWindow = SimulatorWindow.create();
	}
	
	
	
	
	/**
	 * Sends an optimized laser frame to the output.
	 * 
	 * @param theFinalFrame
	 */
	public void draw(final LaserFrame theFinalFrame) {
		if(_myOutput != null && _myOutput.getState() == OutputState.READY){
			_myOutput.draw(theFinalFrame);
		}
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
	
	

	/**
	 * @return the output module which is currently being used
	 */
	public AbstractLaserOutput output() {
		return _myOutput;
	}

	
	/**
	 * Same as {@link laserschein.Laserschein#output()}
	 * 
	 * @see laserschein.Laserschein#output()
	 */
	public AbstractLaserOutput getOutput() {
		return _myOutput;
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

		if(_myOutput != null && _myOutput.getState() == OutputState.READY){
			_myOutput.destroy();
		}
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

		float myWidth = theFrame.points().size() / 6000f; // TODO: remove hardcode
		myWidth *= pa.width;

		pa.rectMode(PApplet.CORNER);
		pa.rect(0, 0, myWidth, 20);

	}


	private void renderPointView(LaserFrame theFrame) {
		final PApplet pa = _myParent;

		pa.noFill();

		pa.stroke(255);

		pa.beginShape();
		for (LaserPoint p : theFrame.points()) {

			if (p.isBlanked) {
				pa.endShape();
				pa.beginShape();
			}

			pa.vertex(screenX(p.x), screenY(p.y));
		}

		pa.endShape();

		pa.stroke(255, 255, 0);

		pa.beginShape();
		for (LaserPoint p : theFrame.points()) {

			if (!p.isBlanked) {
				pa.endShape();
				pa.beginShape();
			}

			pa.vertex(screenX(p.x), screenY(p.y));
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

			
			pa.rect(screenX(pv.x), screenY(pv.y), 10, 10);

			if (isNew && ppv != null) {
				pa.fill(255, 0, 0);
				pa.noStroke();

				pa.text(oldCount, screenX(ppv.x), screenY(ppv.y));
			}

			ppv = pv;

		}

		if (ppv != null) {
			pa.fill(255, 0, 0);
			pa.noStroke();
			pa.text(oldCount, screenX(ppv.x), screenY(ppv.y));
		}
	}
	
	
	
	private float screenX(final float theX) {
		return PApplet.map(theX, -1, 1, 0, _myParent.width);
	}
	
	
	private float screenY(final float theY) {
		return PApplet.map(theY, -1, 1, 0, _myParent.height);
	}
	



}
