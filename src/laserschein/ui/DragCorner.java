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
package laserschein.ui;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;


public class DragCorner implements Draggable {

	private float _myX;
	private float _myY;

	float W = 0.05f;

	boolean isDragged = false;
	boolean isMouseOver = false;
	
	private PickQuad _myParent;
	

	public DragCorner(final PickQuad theParent) {
		this(theParent, 0, 0);
	} 





	public DragCorner(final PickQuad theParent, float theX, float theY) {
		this._myX = theX; 
		this._myY = theY;
		
		_myParent = theParent;
	}


	void startDrag() {
		isDragged = true;
	}


	void endDrag() {
		isDragged = false;
	}


	PVector toVector() {
		final PVector myResult = new PVector(this._myX, this._myY);
		myResult.add(_myParent.offset);
		return myResult; 

	}


	public boolean mouseOver(float theX, float theY) {
		final float myX = _myParent.offset.x + _myX;
		final float myY = _myParent.offset.y + _myY;

		isMouseOver = (theX < myX + W * 0.5f &&  theX > myX - W * 0.5f &&  theY < myY + W * 0.5f &&  theY > myY - W * 0.5f ) ;
		return isMouseOver;
	}

	
	void draw(final PGraphics theG) {
		theG.rectMode(PApplet.CENTER);
		theG.stroke(255);

		
		if (isMouseOver || isDragged) {
			theG.stroke(255, 0, 0);
			theG.fill(128);
		}

		theG.rect(this._myX + _myParent.offset.x, this._myY + _myParent.offset.y, W, W);
	}
	
	
	public void rawX(float theX) {
		_myX = theX;
	}   
	
	public void rawY(float theY) {
		_myY = theY;
	}   
	
	public float rawX() {
		return _myX;
	}   
	
	public float rawY() {
		return _myY;
	}   
	
	
	public float x() {
		return _myX + _myParent.offset.x;
	}   
	
	public float y() {
		return _myY + _myParent.offset.y;
	}   
	
	public void x(float theX) {
		_myX = theX - _myParent.offset.x;
	}
	
	public void y(float theY) {
		_myY = theY - _myParent.offset.y;
	}




}
