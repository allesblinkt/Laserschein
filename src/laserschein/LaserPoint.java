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

/**
 * Representation of a two-dimensional sample point. 
 * 
 * @author allesblinkt
 *
 */
public class LaserPoint {

	public int x; // -
	public int y;
	
	public int r;
	public int g;
	public int b;
	
	public boolean isCorner = false;
	public boolean isBlanked = false;
	public float turningAngle = 180;


	public LaserPoint() {
		this.x = 0;
		this.y = 0;
	}


	public LaserPoint(int theX, int theY) {
		this.x = theX;
		this.y = theY;
	}


	/**
	 * Copy Constructor
	 * 
	 * @param theLaserPoint
	 */
	public LaserPoint(final LaserPoint theLaserPoint) {
		this.x = theLaserPoint.x;
		this.y = theLaserPoint.y;
		this.r = theLaserPoint.r;
		this.g = theLaserPoint.g;
		this.b = theLaserPoint.b;
		this.isCorner = theLaserPoint.isCorner;
		this.isBlanked = theLaserPoint.isBlanked;
	}


	/**
	 * Are two points at the same position?
	 * 
	 * @param theOther
	 * @return
	 */
	public boolean isCoincided(final LaserPoint theOther) {
		if (this.x == theOther.x &&
				this.y == theOther.y &&
				this.r == theOther.r &&
				this.g == theOther.g &&
				this.b == theOther.b &&
				this.isCorner == theOther.isCorner &&
				this.isBlanked == theOther.isBlanked) {
			return true;
		}
		return false;
	}


	/**
	 * Gets the angle between A and C  in respect to B
	 * 
	 * @param theA A
	 * @param theB B
	 * @param theC C
	 * @return angle in radians
	 */
	public static float getAngle(LaserPoint theA, LaserPoint theB, LaserPoint theC) {

		final LaserPoint myAB = new LaserPoint(theA);
		myAB.subtract(theB);

		final LaserPoint myCB = new LaserPoint(theC);
		myCB.subtract(theB);

		float d = myAB.dotProduct(myCB) / (myAB.length() * myCB.length());

		if (d < -1.0f) {
			d = -1.0f;
		}

		if (d > 1.0f) {
			d = 1.0f;
		}

		return (float) Math.acos(d);

	}


	public void add(LaserPoint theOther) {
		this.x += theOther.x;
		this.y += theOther.y;
	}


	public void subtract(LaserPoint theOther) {
		this.x -= theOther.x;
		this.y -= theOther.y;
	}


	public float dotProduct(LaserPoint theOther) {
		return this.x * theOther.x + this.y * theOther.y;
	}


	private float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y);
	}


	public float distanceSquared(final LaserPoint theOther) {
		int myX = theOther.x - this.x;
		int myY = theOther.y - this.y;
		return myX * myX + myY * myY;
	}


	public float distance(final LaserPoint theOther) {
		int myX = theOther.x - this.x;
		int myY = theOther.y - this.y;
		return (float) Math.sqrt(myX * myX + myY * myY);
	}


	/**
	 * Interpolates between this and another point (linear)
	 * 
	 * @param theOther 
	 * @param theProgress <i>(0 - 1.0)</i>
	 * @return
	 */
	public LaserPoint getPointBetween(LaserPoint theOther, float theProgress) {
		final LaserPoint myNewPoint = new LaserPoint(theOther);

		final int myX = Math.round(this.x + (theOther.x - this.x) * theProgress);
		final int myY = Math.round(this.y + (theOther.y - this.y) * theProgress);

		myNewPoint.x = myX;
		myNewPoint.y = myY;

		return myNewPoint;
	}

}
