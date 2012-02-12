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
package laserschein;

import java.util.ArrayList;
import java.util.List;



/**
 * Clips a LaserGraphic, so it fits inside the bounds of [-1, 1].
 * The new clipped LaserGraphic might not contain the same number of shapes, as some 
 * might be removed completely and some split.
 * 
 * @author Benjamin Maus
 *
 */
public class BoundsClipper {

	public BoundsClipper() {

	}


	/**
	 * Clips a laser graphic and returns the clipped one
	 * @param theGraphic
	 * @return
	 */
	public LaserGraphic clip(final LaserGraphic theGraphic){
		final LaserGraphic myResult = new LaserGraphic();

		for(final LaserShape myShape:theGraphic.shapes()) {
			final List<LaserShape> myClipedShapes = clip(myShape);

			myResult.shapes().addAll(myClipedShapes);

		}

		return myResult;
	}


	private List<LaserShape> clip(final LaserShape theShape) {
		ArrayList<LaserShape> myResult = new ArrayList<LaserShape>();
		myResult.add(theShape);

		/* Divide & Conquer (each edge consecutively) */
		for(final EdgeCase myCase:EdgeCase.values()){
			ArrayList<LaserShape> myEdgeResult = new ArrayList<LaserShape>();

			for(LaserShape myShape:myResult) {
				myEdgeResult.addAll(clip(myShape, myCase));
			}
			
			myResult = myEdgeResult;
		}

		return myResult;
	}


	private List<LaserShape> clip(final LaserShape theShape, final EdgeCase theEdge) {
		final ArrayList<LaserPoint> myPoints = theShape.points();

		
		ArrayList<LaserShape> myResult = new ArrayList<LaserShape>();

		
		LaserShape myNewShape = new LaserShape();
		myNewShape.closed(theShape.closed());


		for(int i = 0; i < myPoints.size() - 1; i++) {
			final LaserPoint myP = myPoints.get(i);
			final LaserPoint myNextP = myPoints.get(i + 1);

			if(isInside(myP, theEdge)){
				myNewShape.addPoint(new LaserPoint(myP));
				
				if(!isInside(myNextP, theEdge)) {
					myNewShape.closed(false);

					LaserPoint myNewPoint = intersectWithEdge(theEdge, myP, myNextP); 
					myNewShape.addPoint(myNewPoint);
					
					if(myNewShape.points().size() > 1){
						myResult.add(myNewShape);
					}
					
					myNewShape = new LaserShape();
					myNewShape.closed(false);

				}

			} else {
				if(isInside(myNextP, theEdge)) {	
					
					LaserPoint myNewPoint = intersectWithEdge(theEdge, myP, myNextP);
					myNewShape.addPoint(myNewPoint);

					myNewShape.closed(false);

					
				} 	

			}
		}
		
		LaserPoint myLast = myPoints.get(myPoints.size() -1 );
		
		if(isInside(myLast, theEdge)){
			myNewShape.addPoint(myLast);
		}
		
		if(myNewShape.points().size() > 1){
			myResult.add(myNewShape);
		}

		return myResult;
	}



	private LaserPoint intersectWithEdge(final EdgeCase theEdge, final LaserPoint theP1, final LaserPoint theP2) {
		float myX = 0;
		float myY = 0;

		switch (theEdge) {
		case NORD:
			myX = theP1.x + ((-1 - theP1.y) * (theP2.x - theP1.x)) / (theP2.y - theP1.y);
			myY = -1;
			break;

		case OST:
			myX = 1;
			myY = theP1.y + ((1 - theP1.x) * (theP2.y - theP1.y)) / (theP2.x - theP1.x);
			break;

		case SUED:			
			myX = theP1.x + ((1 - theP1.y) * (theP2.x - theP1.x)) / (theP2.y - theP1.y);
			myY = 1;
			break;
			
		case WEST:
			myX = -1;
			myY = theP1.y+ ((-1 - theP1.x) * (theP2.y - theP1.y)) / (theP2.x - theP1.x);
			break;
		}
		
		final LaserPoint myResult = new LaserPoint(theP2);
		
		myResult.x = myX;
		myResult.y = myY;
		
		return myResult;
	}


	private boolean isInside(final LaserPoint theP, final EdgeCase theEdge) {
		switch (theEdge) {
		case NORD:
			return !(theP.y < -1);

		case OST:
			return !(theP.x > 1);

		case SUED:
			return !(theP.y > 1);

		case WEST:
			return !(theP.x < -1);
		}

		return true;
	}



	public enum EdgeCase {
		NORD, OST, SUED, WEST
	}
}
