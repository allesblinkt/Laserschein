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
 * The Optimizer takes a bunch of connected points (i.e. graphics) and 
 * converts (optimizes) it for output on a laser scanner, where each 
 * sample is one fixed time slice. 
 * 
 * @author allesblinkt
 *
 */
public class Optimizer {

	private OptimizerSettings _mySettings;

	private static final float SMALLEST_DISTANCE = 2/16000.0f;
	
	public Optimizer() {
		_mySettings = new OptimizerSettings();
	}


	/**
	 * Does all the magic. 
	 * 
	 * @param theGraphic
	 * @return the optimized frame
	 */
	public LaserFrame optimize(LaserGraphic theGraphic) {
		LaserFrame myOptimizedFrame = new LaserFrame();
		
		LaserGraphic myLaserGraphic = theGraphic;

		myLaserGraphic = eliminateDuplicates(theGraphic);

		if (_mySettings.reorderFrame) {
			myLaserGraphic.sort();
		}

		myOptimizedFrame = createFrameWithSingleBlanks(myLaserGraphic);
		myOptimizedFrame = enhanceAngles(myOptimizedFrame);
		myOptimizedFrame = interpolatePoints(myOptimizedFrame);
		myOptimizedFrame = shiftPoints(myOptimizedFrame, _mySettings.blankShift);
		
		
		return myOptimizedFrame;
	}

		


	private LaserFrame shiftPoints(final LaserFrame theFrame, int theShift) {
		final LaserFrame myNewFrame = new LaserFrame();
		
		for(int i = theShift; i < theFrame.points().size() + theShift; i++) {
			
			int myPositionIndex = i;
			int myColorIndex = i - theShift;
			
			LaserPoint myPointPosition = new LaserPoint();
			LaserPoint myPointColor = new LaserPoint();
			
			if(myPositionIndex >= 0 &&  myPositionIndex < theFrame.points().size()){
				myPointPosition = theFrame.points().get(myPositionIndex);
			}
			
			if(myColorIndex >= 0 &&  myColorIndex < theFrame.points().size()){
				myPointColor = theFrame.points().get(myColorIndex);
			}
			
			final LaserPoint myNewPoint = new LaserPoint(myPointPosition);
			myNewPoint.r = myPointColor.r;
			myNewPoint.g = myPointColor.g;
			myNewPoint.b = myPointColor.b;
			myNewPoint.isBlanked = myPointColor.isBlanked;

			myNewFrame.points().add(myNewPoint);
		}
	
		return myNewFrame;
	}


	private LaserFrame createFrameWithSingleBlanks(final LaserGraphic theGraphic) {
		final LaserFrame myFrame = new LaserFrame();

		LaserPoint myPreviousPoint = null;
		LaserPoint myNextPoint = null;

		for (int i = 0; i < theGraphic.shapes().size(); i++) {

			final LaserShape myShape = theGraphic.shapes().get(i);
			LaserShape myNextShape = null;

			if (i < theGraphic.shapes().size() - 1) {
				myNextShape = theGraphic.shapes().get(i + 1);
			}

			/* Add blank */
			if (myShape.points().size() > 0) {
				final LaserPoint myPoint = new LaserPoint(myShape.start());
				myPoint.isBlanked = true;

				myFrame.points().add(myPoint);

			}

			for (int j = 0; j < myShape.size(); j++) {

				final LaserPoint myPoint = myShape.points().get(j);

				if (j < myShape.size() - 1) {
					myNextPoint = myShape.points().get(j + 1);
				} else if (myNextShape != null && myNextShape.size() > 0) {
					myNextPoint = myNextShape.start();
				} else {
					myNextPoint = null;
				}

				if (myNextPoint != null && myPreviousPoint != null) {
					myPoint.turningAngle = LaserPoint.getAngle(myPreviousPoint, myPoint, myNextPoint);
				} else {
					myPoint.turningAngle = 0;
				}

				myFrame.points().add(myPoint);

				myPreviousPoint = myPoint;

			}

			/* Add blank */
			if (myShape.size() > 0) {

				final LaserPoint myPoint = new LaserPoint(myShape.end());
				myPoint.isBlanked = true;

				myFrame.points().add(myPoint);
			}
		}

		return myFrame;
	}


	private LaserGraphic eliminateDuplicates(final LaserGraphic theGraphic) {

		final LaserGraphic myNewGraphic = new LaserGraphic();

		for (final LaserShape myShape : theGraphic.shapes()) {

			final LaserShape myNewShape = new LaserShape();

			LaserPoint myLastPoint = null;
			for (final LaserPoint myPoint : myShape.points()) {
				if (myLastPoint != null && myPoint.isCoincided(myLastPoint)) {
					// Omit the vertex
				} else {
					myNewShape.addPoint(myPoint);
				}

				myLastPoint = myPoint;
			}

			myNewGraphic.shapes().add(myNewShape);
		}

		return myNewGraphic;
	}


	private LaserFrame enhanceAngles(final LaserFrame theFrame) {
		final LaserFrame myNewFrame = new LaserFrame();

		final Vector<LaserPoint> myPoints = theFrame.points();

		LaserPoint myPreviousPoint = null;
		LaserPoint myNextPoint = null;

		for (int i = 0; i < myPoints.size(); i++) {

			final LaserPoint myPoint = myPoints.get(i);

			if (i < myPoints.size() - 1) {
				myNextPoint = myPoints.get(i + 1);
			} else {
				myNextPoint = null;
			}

			
			/* Blanks */
			if (myPoint.isBlanked) {
				
				int myNumber = 0;
				
		
				
				if (myPreviousPoint == null || myPreviousPoint.isBlanked) {
					myNumber = _mySettings.extraBlankPointsStart;

				}
				
				if (myNextPoint == null || myNextPoint.isBlanked) {
					myNumber = _mySettings.extraBlankPointsEnd;

				}
				
				
				for (int j = 0; j < myNumber; j++) {
					final LaserPoint myBlankPoint = new LaserPoint(myPoint);
					myNewFrame.points().add(myBlankPoint);
				}
			}

			/* Add extra points (angle independent) */
			if (myPoint.isCorner ) { // && !_mySettings.analyzeCornerAngles ) {

				int myNumber = _mySettings.extraPointsCorner;

				if (myPreviousPoint == null || myPreviousPoint.isBlanked) {
					myNumber = _mySettings.extraPointsStart;

				}
				
				
				if (myNextPoint == null || myNextPoint.isBlanked) {
					myNumber = _mySettings.extraPointsEnd;
				}

				for (int j = 0; j < myNumber; j++) {
					myNewFrame.points().add(new LaserPoint(myPoint));
				}

			}

//			/* Add extra points (angle dependent) */
//			if (myPoint.isCorner && _mySettings.analyzeCornerAngles) {
//				float myAngle = myPoint.turningAngle;
//
//				int myNumber = _mySettings.extraCornerPoints;
//				int myExtraPointCount = (int) ((1f - (myAngle / (float) Math.PI)) * _mySettings.extraCornerPointsAngleDependent);
//				
//
//				if (myPreviousPoint == null || myPreviousPoint.isBlanked) {
//					myNumber = _mySettings.extraCornerPointsStart;
//				}
//								
//				if (myNextPoint == null || myNextPoint.isBlanked) {
//					myNumber = _mySettings.extraCornerPointsEnd;
//				}
//				
//				myNumber += myExtraPointCount;
//
//				
//
//				for (int j = 0; j < myNumber; j++) {
//					myNewFrame.points().add(new LaserPoint(myPoint));
//				}
//
//			}
			
			
			/* Add extra points for smoot shapes at the end */
			if (!myPoint.isCorner ) {
				//float myAngle = myPoint.turningAngle;

				int myNumber = 0;
				
				int myExtraPointCount = 0;
				
				//if (myPreviousPoint == null || myPreviousPoint.isBlanked) {
					myNumber = _mySettings.extraPointsStart;
					//myExtraPointCount = (int) ((1f - (myAngle / (float) Math.PI)) * _mySettings.extraCornerPointsAngleDependent);

				//}
								
				//if (myNextPoint == null || myNextPoint.isBlanked) {
					myNumber = _mySettings.extraPointsEnd;
					//myExtraPointCount = (int) ((1f - (myAngle / (float) Math.PI)) * _mySettings.extraCornerPointsAngleDependent);
				//}
				
				myNumber += myExtraPointCount;

				for (int j = 0; j < myNumber; j++) {
					myNewFrame.points().add(new LaserPoint(myPoint));
				}

			}
			
			
			myNewFrame.points().add(myPoint);

			myPreviousPoint = myPoint;
		}

		return myNewFrame;
	}


	private LaserFrame interpolatePoints(final LaserFrame theFrame) {
		final LaserFrame myNewFrame = new LaserFrame();

		final Vector<LaserPoint> myPoints = theFrame.points();
		final float myMaximumTravel = Math.max(_mySettings.maxTravel, SMALLEST_DISTANCE);
		final float myMaximumTravelBlank = Math.max(_mySettings.maxTravelBlank, SMALLEST_DISTANCE);

		
		LaserPoint myNextPoint = null;
		for (int i = 0; i < myPoints.size(); i++) {
			final LaserPoint myPoint = myPoints.get(i);

			if (i < myPoints.size() - 1) {
				myNextPoint = myPoints.get(i + 1);
			} else {
				myNextPoint = null;
			}

			myNewFrame.points().add(myPoint);

			if (myNextPoint != null && myPoint.isCorner && myNextPoint.isCorner )
				
				if(	(!myNextPoint.isBlanked && myPoint.distance(myNextPoint) > myMaximumTravel) 
						|| (myNextPoint.isBlanked && myPoint.distance(myNextPoint) > myMaximumTravelBlank) ) {
					
				float myMaxDistance = myMaximumTravel;
				
				if(myNextPoint.isBlanked) {
					myMaxDistance = myMaximumTravelBlank;
				}
				
				float myDistance = myPoint.distance(myNextPoint);

				int mySteps = (int) (myDistance / myMaxDistance);
				float myIncrement = 1f / (float) (mySteps + 1);

				for (int j = 0; j < mySteps; j++) {
					float myProgress = myIncrement + myIncrement * j;
					LaserPoint myBetweenPoint = myPoint.getPointBetween(myNextPoint, myProgress);

					myNewFrame.points().add(myBetweenPoint);
				}
			}
		}

		return myNewFrame;
	}


	public OptimizerSettings settings() {
		return _mySettings;
	}

}
