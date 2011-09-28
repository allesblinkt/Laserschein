package laserschein;

import java.util.Vector;


public class LaserShape {
	private Vector<LaserPoint> _myPoints;
	
	public LaserShape() {
		_myPoints = new Vector<LaserPoint>();
	}
	
	public void addPoint(final LaserPoint theLaserPoint) {
		_myPoints.add(theLaserPoint);
	}
	
	/**
	 * Start point of the shape
	 * 
	 * @return
	 */
	public LaserPoint start() {
		return _myPoints.firstElement();
	}
	
	
	/**
	 * End point of the shape
	 * 
	 * @return
	 */
	public LaserPoint end() {
		return _myPoints.lastElement();
	}
	
	
	public boolean isValid() {
		return _myPoints.size() > 1;
	}
	
	
	public Vector<LaserPoint> points() {
		return _myPoints;
	}
	
	public int size() {
		return _myPoints.size();
	}
	
	/**
	 * Reverse the direction of the path
	 */
	public void reverse() {
		final Vector<LaserPoint> myTmp = new Vector<LaserPoint>();
		myTmp.addAll(_myPoints);
		
		_myPoints.clear();
		
		for(int i=myTmp.size()-1; i >= 0; i--) {
			_myPoints.add(myTmp.get(i));
		}
		
		
	}
}
