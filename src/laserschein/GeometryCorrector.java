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

import processing.core.PMatrix2D;
import processing.core.PVector;


public class GeometryCorrector {
	private Homography _myHomography;
	
	private GeometrySettings _mySettings;
	
	
	/**
	 * Cached transformation matrix
	 */
	private PMatrix2D _myMatrix;
	
	
	public GeometryCorrector() {
		
		_myMatrix = new PMatrix2D();	
		_mySettings = new GeometrySettings();	
		
		_myHomography = new Homography(_mySettings.homographySource1, _mySettings.homographySource2, 
										_mySettings.homographySource3, _mySettings.homographySource4, 
										_mySettings.homographyDestination1, _mySettings.homographyDestination2,
										_mySettings.homographyDestination3, _mySettings.homographyDestination4 );

		updateTransforms();
	}
	
	
	/**
	 * This should be called every time the parameters have changed.
	 */
	public void updateTransforms() {
		_myMatrix.reset();

		_myMatrix.translate(_mySettings.offset.x, _mySettings.offset.y);
		_myMatrix.scale(_mySettings.scale.x, _mySettings.scale.y);

		_myHomography = new Homography(_mySettings.homographySource1, _mySettings.homographySource2, 
									   _mySettings.homographySource3, _mySettings.homographySource4, 
									   _mySettings.homographyDestination1, _mySettings.homographyDestination2, 
									   _mySettings.homographyDestination3, _mySettings.homographyDestination4 );

	}
	
	
	/**
	 * Transforms the input vector. Does not change the input.
	 * 
	 * @param thePVector
	 * @return
	 */
	public PVector transform(final PVector theVector) {
		PVector myPosition = _myHomography.transform(new PVector(theVector.x, theVector.y));
		myPosition = _myMatrix.mult(myPosition, null);
		
		return myPosition;
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
	
	
	public GeometrySettings settings() {
		return _mySettings;
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
