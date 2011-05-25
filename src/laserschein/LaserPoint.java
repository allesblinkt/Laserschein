package laserschein;

public class LaserPoint {

	public int x;
	public int y;
	public int color;
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


	public LaserPoint(LaserPoint theLaserPoint) {
		this.x = theLaserPoint.x;
		this.y = theLaserPoint.y;
		this.color = theLaserPoint.color;
		this.isCorner = theLaserPoint.isCorner;
		this.isBlanked = theLaserPoint.isBlanked;
	}


	public boolean isCoincided(LaserPoint theOther) {
		if (this.x == theOther.x &&
				this.y == theOther.y &&
				this.color == theOther.color &&
				this.isCorner == theOther.isCorner &&
				this.isBlanked == theOther.isBlanked) {
			return true;
		}
		return false;
	}


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


	public float lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}


	public float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y);
	}


	public float distanceSquared(LaserPoint theOther) {
		int myX = theOther.x - this.x;
		int myY = theOther.y - this.y;
		return myX * myX + myY * myY;
	}


	public float distance(LaserPoint theOther) {
		int myX = theOther.x - this.x;
		int myY = theOther.y - this.y;
		return (float) Math.sqrt(myX * myX + myY * myY);
	}


	public LaserPoint getPointBetween(LaserPoint thePoint, float theProgress) {
		final LaserPoint myNewPoint = new LaserPoint(thePoint);

		final int myX = Math.round(this.x + (thePoint.x - this.x) * theProgress);
		final int myY = Math.round(this.y + (thePoint.y - this.y) * theProgress);

		myNewPoint.x = myX;
		myNewPoint.y = myY;

		return myNewPoint;
	}

}
