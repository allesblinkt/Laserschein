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

import processing.core.PVector;
import processing.xml.XMLElement;


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


	
	
	public static XMLElement xmlFromPVector(String theName, final PVector theV) {
		final XMLElement myXml = new XMLElement();
		myXml.setName(theName);
		
		myXml.setFloat("x", theV.x);
		myXml.setFloat("y", theV.y);
		myXml.setFloat("z", theV.z);

		return myXml;
		
	}
	
	
	public static PVector pVectorFromXml(final XMLElement theXml) {
		PVector myVector = null;
		
		if(theXml != null) {
			myVector = new PVector();
			myVector.x = theXml.getFloat("x");
			myVector.y = theXml.getFloat("y");
			myVector.z = theXml.getFloat("z");
		}
	
		return myVector;

	}


	public static PVector pVectorFromXml(XMLElement theXml, PVector theFallback) {
		PVector myVector = pVectorFromXml(theXml);
		
		if(myVector == null) {
			myVector = new PVector();
			myVector.set(theFallback);
		}
		
		return myVector;
	}

	
}
