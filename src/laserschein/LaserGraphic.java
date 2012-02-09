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

import java.util.Vector;

/**
 * Collects graphics (i.e. connected shapes) for one frame to be displayed. 
 * {@link laserschein.Optimizer} turns this into a 
 * {@link laserschein.LaserFrame} suitable for output on a laser scanner.
 * 
 * @author Benjamin Maus
 */
public class LaserGraphic {

	private Vector<LaserShape> _myShapes;


	public LaserGraphic() {
		_myShapes = new Vector<LaserShape>();
	}

	/**
	 * @return all the connected shapes the graphic consists of.
	 * 
	 */
	public Vector<LaserShape> shapes() {
		return _myShapes;
	}
	
	
	/**
	 * Performs a greedy sort to minimize the jumps  between the shapes. 
	 * Also reverses the order sometimes to find better results.
	 */
	public void sort() {
		
		final Vector<LaserShape> myShapes = new Vector<LaserShape>();
		
		for(int i = 1; i < _myShapes.size(); i++) {
			myShapes.add(_myShapes.get(i));
		}
		
		
		final Vector<LaserShape> mySortedShapes = new Vector<LaserShape>();
			
		LaserPoint myLastPoint = null;
		
		if(_myShapes.size() > 0) {
			mySortedShapes.add(_myShapes.get(0));
			myLastPoint = _myShapes.get(0).end();
		}

		
		while(myShapes.size() > 0){

			float myBestDistanceSquared = Float.MAX_VALUE;
			int myBestIndex = -1;
			boolean myBestWasReversed = false;


			for(int i=0; i < myShapes.size(); i++){
				final LaserShape myShape = myShapes.get(i);

				float myDistanceSquared = myLastPoint.distanceSquared(myShape.start());
				if(myDistanceSquared < myBestDistanceSquared){
					myBestWasReversed = false;
					myBestDistanceSquared = myDistanceSquared;
					myBestIndex = i;
				}

				myDistanceSquared = myLastPoint.distanceSquared(myShape.end());
				if(myDistanceSquared < myBestDistanceSquared){
					myBestWasReversed = true;
					myBestDistanceSquared = myDistanceSquared;
					myBestIndex = i;			
				}
				
				
				// TODO: merge shapes if distance & color match!
				// TODO: Think about greedy scatter search
			}

			final LaserShape myLastShape = myShapes.get(myBestIndex);

			if(myBestWasReversed) {
				myLastShape.reverse();
			}

			myShapes.remove(myBestIndex);
			myLastPoint = myLastShape.end();
			
			mySortedShapes.add(myLastShape);


		}
		
		_myShapes.clear();
		_myShapes.addAll(mySortedShapes);
	}


	/**
	 * Clears all the lists.
	 */
	public void reset() {
		_myShapes.clear();
	}
}
