package laserschein.ui;

import java.util.ArrayList;


public class DragManager {
	  private final ArrayList<Draggable> _myElements; 
	  Draggable active = null;

	  float diffX;
	  float diffY;

	  
	  public DragManager() {
	    _myElements = new ArrayList<Draggable>();
	  } 

	  
	  public void add(Draggable theElement) {
	    _myElements.add(theElement);
	  }

	  
	  public void draw() {

	    if (active != null) {
	    } 
	    else {
	    }
	  }


	  public void pressed(float theX, float theY) {
	    for (Draggable element: _myElements) {
	      if (element.mouseOver(theX, theY) ) {
	        active = element;
	      }
	    } 

	    if (active != null) {
	      diffX = active.x() - theX;
	      diffY = active.y() - theY;
	    }
	  }


	  public void dragged(float theX, float theY) {
	    if (active != null) {
	      active.x(theX + diffX); 
	      active.y(theY + diffY);
	    }
	  }


	  public void released() {
	    active = null;
	  }
	}
