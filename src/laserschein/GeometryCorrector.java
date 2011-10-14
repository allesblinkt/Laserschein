package laserschein;

import processing.core.PMatrix3D;
import processing.core.PVector;


public class GeometryCorrector {
	private final Homography _myHomography;
	private float _myXScale;
	private float _myYScale;
	
	private PVector _myOffset;
	
	/**
	 * Cached transformation matrix
	 */
	private PMatrix3D _myMatrix;
	


	public GeometryCorrector() {
		_myHomography = new Homography(new PVector(-1,-1),new PVector(1,-1),new PVector(1,1),new PVector(-1,1), new PVector(-1,-1),new PVector(1,-1),new PVector(1,1),new PVector(-1,1));
		_myXScale = 0.5f;
		_myYScale = 0.5f;
		
		_myOffset = new PVector();
		
		_myMatrix = new PMatrix3D();
		
		updateTransforms();
	}
	
	
	/**
	 * This should be called every time the parameters have changed.
	 */
	public void updateTransforms() {
		_myMatrix.reset();
		//_myMatrix.mul(_myHomography.modelViewMatrix());

		_myMatrix.translate(_myOffset.x, _myOffset.y);
		_myMatrix.scale(_myXScale, _myYScale);
		//_myHomography.transform(theV)
		// TODO: how to apply the homography
		
		//_myMatrix.reset();
	}
	
	
	/**
	 * Transforms the input vector. Does not change the input.
	 * 
	 * @param thePVector
	 * @return
	 */
	public PVector transform(final PVector thePVector) {
		final PVector myResult = _myMatrix.mult(thePVector, null);
		
		return myResult;
	}
	
	
	/**
	 * Transforms a whole shape. Does not change the input.
	 * 
	 * @param myShape
	 * @return
	 */
	private LaserShape transform(final LaserShape theShape) {
		final LaserShape myShape = new LaserShape(theShape);
		
		for(final LaserPoint myPoint:myShape.points()){
			final PVector myPosition = transform(new PVector(myPoint.x, myPoint.y));
			myPoint.x = myPosition.x;
			myPoint.y = myPosition.y;
		}
		
		return myShape;
	}


	public LaserGraphic correct(final LaserGraphic theGraphic) {
		final LaserGraphic myResult = new LaserGraphic();
		
		for(final LaserShape myShape:theGraphic.shapes()) {
			final LaserShape myTransformedShape = transform(myShape);
	
			myResult.shapes().add(myTransformedShape);
			
		}
		
		return myResult;		
	}




}
