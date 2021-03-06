/**
 *  
 *  Laserschein. interactive ILDA output from processing and java
 *
 *  2012 by Benjamin Maus
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

import processing.core.PGraphics;
import processing.core.PVector;


public class PickQuad implements Draggable {
		  DragCorner corner1; 
		  DragCorner corner2; 
		  DragCorner corner3; 
		  DragCorner corner4;
		  
		  PVector offset;


		  public PickQuad() {
		    corner1 = new DragCorner(this, -1, -1);
		    corner2 = new DragCorner(this, 1, -1);
		    corner3 = new DragCorner(this, 1, 1);
		    corner4 = new DragCorner(this, -1, 1);
		    
		    offset = new PVector(0,0);
		  } 


		  void draw(final PGraphics theG) {
			 theG.pushMatrix(); 
			  
		    corner1.draw(theG);
		    corner2.draw(theG);
		    corner3.draw(theG);
		    corner4.draw(theG);

		    theG.noFill();
		    theG.stroke(255,0,255);
		    
		    theG.beginShape();
		    theG.vertex(corner1.x(), corner1.y());
		    theG.vertex(corner2.x(), corner2.y());
		    theG.vertex(corner3.x(), corner3.y());
		    theG.vertex(corner4.x(), corner4.y());

		    theG.endShape(PGraphics.CLOSE);
		    
		    theG.popMatrix();
		  }


		@Override
		public float x() {
			return offset.x;
		}


		@Override
		public float y() {
			return offset.y;
		}


		@Override
		public void x(float theX) {
			offset.x = theX;	
		}


		@Override
		public void y(float theY) {
			offset.y = theY;	
		}


		@Override
		public boolean mouseOver(float theX, float theY) {
			return true; // TODO: check
		}
		


}
