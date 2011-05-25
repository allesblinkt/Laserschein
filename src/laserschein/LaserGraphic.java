package laserschein;

import java.util.Vector;

public class LaserGraphic {

	private Vector<Vector<LaserPoint>> _myShapes;


	public LaserGraphic() {
		_myShapes = new Vector<Vector<LaserPoint>>();
	}


	public Vector<Vector<LaserPoint>> shapes() {
		return _myShapes;
	}


	public void reset() {
		_myShapes.clear();
	}
}
