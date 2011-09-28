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
 * This graphics object (renderer) receives raw vertices from the Processing
 * drawing routines. The vertices are already 2D. 
 * 
 * @author allesblinkt
 */
public class Laser3D extends PGraphics2D {

	private final PApplet _myPApplet;
	private final LaserGraphic _myGraphic;

	private LaserShape _myShape;

	private float _myScale;

	private Optimizer _myOptimizer;

	private Laserschein _myLaserschein;


	public Laser3D(PApplet thePApplet, Laserschein theSchein) {
		_myGraphic = new LaserGraphic();
		_myPApplet = thePApplet;
		_myOptimizer = new Optimizer();
		_myLaserschein = theSchein;
	}

	
	/**
	 * @see processing.core.PGraphics2D#beginDraw()
	 * 
	 * Also deletes the stored graphics in memory.
	 */
	@Override
	public void beginDraw() {
		_myGraphic.reset();
		_myScale = LaserPoint.COORDINATE_RANGE / _myPApplet.width;
	}

	
	/**
	 * @see processing.core.PGraphics#smooth()
	 * 
	 * This function differs very much in Laserschein.
	 * Basically setting smooth skips most of the optimization 
	 * (brake points etc.) and allows for sending raw points
	 * to the laser.
	 *
	 */
	@Override
	public void smooth() {
		smooth = true;
	}

	
	/**
	 * @see processing.core.PGraphics#noSmooth()
	 * 
	 * The opposite of smooth(). Tries to optimize all angles
	 * to be very exact.
	 */
	@Override
	public void noSmooth() {
		smooth = false;
	}
	
	
	@Override
	public void ellipse(float arg0, float arg1, float arg2, float arg3) {
		System.out.println("Foo");	
	};

	@Override
	public void beginShape(int kind) {
		_myShape = new LaserShape();
	}
	

	/* (non-Javadoc)
	 * @see processing.core.PGraphics#vertex(float, float)
	 */
	public void vertex(float theX, float theY) {
	
		final LaserPoint myPoint = new LaserPoint();
	
		myPoint.x = (int) (theX * _myScale);
		myPoint.y = (int) (theY * _myScale);
	
		myPoint.r = Math.round(strokeR * 255);
		myPoint.g = Math.round(strokeG * 255);
		myPoint.b = Math.round(strokeB * 255);

	
		if (smooth) {
			myPoint.isCorner = false;
		} else {
			myPoint.isCorner = true;
		}
	
		_myShape.addPoint(myPoint);
	
	}
	
	
	@Override
	public void vertex(float theX, float theY, float theZ) {
		vertex(theX, theY);
	}
	

	@Override
	public void endShape(int mode) {
		if(_myShape.isValid()){
			_myGraphic.shapes().add(_myShape);
		}
	}
	

	/** 
	 * @see processing.core.PGraphics2D#endDraw()
	 * 
	 * The {@link laserschein.Optimizer} kicks in at this point
	 */
	public void endDraw() {
		_myOptimizer.optimize(_myGraphic);
		
		redraw();
	}


	@Override
	public void dispose() {
	}
	

	@Override
	public boolean displayable() {
		return false;
	}


	/**
	 * Redraws the last frame
	 */
	private void redraw() {
		_myLaserschein.draw(this.finalFrame());
	}


	/**
	 * Convert three int color values to one integer value
	 * 
	 * @param theR Red value <i>(0 - 255)</i>
	 * @param theG Green value <i>(0 - 255)</i>
	 * @param theB Blue value <i>(0 - 255)</i>
	 * @return an int representing the color
	 */
	public static int rgbToInt(int theR, int theG, int theB) {
		return (theB << 16) | (theG << 8) | theR;
	}

	
	/**
	 * Convert three float color values to one integer value
	 * 
	 * @param theR Red value <i>(0 - 1.0)</i>
	 * @param theG Green value <i>(0 - 1.0)</i>
	 * @param theB Blue value <i>(0 - 1.0)</i>
	 * @return an int representing the color
	 */
	public static int frgbToInt(float theR, float theG, float theB) {
		return rgbToInt((int) (theR * 255), (int) (theG * 255), (int) (theB * 255));
	}
	

	public boolean hasFinishedFrame() {
		return true;
	}
	
	
	/**
	 * After having drawn stuff and finished it with endRaw(), you can get the final
	 * frame here
	 * 
	 * @return
	 */
	public LaserFrame finalFrame() {
		return _myOptimizer.optimizedFrame();
	}
	
	
	/**
	 * @see laserschein.Optimizer#settings()
	 */
	public OptimizerSettings settings(){
		return _myOptimizer.settings();
	}
	
	
	/**
	 * @see laserschein.Optimizer#setSettingsRef()
	 */
	public void setSettingsRef(OptimizerSettings theSettings) {
		_myOptimizer.setSettingsRef(theSettings);
	}


}
