package laserschein;

import java.util.Vector;

public class LaserFrame {

	private final Vector<LaserPoint> _myPoints;


	public LaserFrame() {
		_myPoints = new Vector<LaserPoint>();
	}


	public Vector<LaserPoint> points() {
		return _myPoints;
	}

}
