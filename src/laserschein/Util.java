package laserschein;

import processing.core.PVector;


public class Util {
	public static float distanceSegmentPoint(PVector theA, PVector theB, PVector thePoint) {

		final float myDeltaX = theB.x - theA.x;
		final float myDeltaY = theB.y - theA.y;

		if ((myDeltaX == 0) && (myDeltaY == 0)) {
			return theA.dist(thePoint);
		}

		final float myU = ((thePoint.x - theA.x) * myDeltaX + (thePoint.y - theA.y) * myDeltaY) / (myDeltaX * myDeltaX + myDeltaY * myDeltaY);

		final PVector myClosestPoint;
		if (myU < 0) {
			myClosestPoint = theA;
		} 
		else if (myU > 1) {
			myClosestPoint = theB;
		} 
		else {
			myClosestPoint = new PVector(theA.x + myU * myDeltaX, theA.y + myU * myDeltaY);
		}

		return myClosestPoint.dist(thePoint);
	}


	
}
