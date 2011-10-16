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
