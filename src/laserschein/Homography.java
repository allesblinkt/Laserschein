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


import processing.core.PMatrix3D;
import processing.core.PVector;

/**
 * Calculates a homography transformation from four correlated point pairs
 *
 */
public class Homography {

	private final HomographyMatrix _myMatrix;	
	private final HomographyMatrix _myInverseMatrix;

	public Homography( final PVector[] theSources, final PVector[] theDestinations ) {
		 this(	
				 theSources[0], theSources[1], theSources[2], theSources[3], 
				 theDestinations[0], theDestinations[1], theDestinations[2], theDestinations[3]
		);
	}
	

	public Homography(	final PVector theSrc1,final PVector theSrc2,final PVector theSrc3,final PVector theSrc4,
			final PVector theDst1,final PVector theDst2,final PVector theDst3,final PVector theDst4 ) {

		final HomographyMatrix myTmp1;
		myTmp1 = getQuadToSquare(theSrc1.x, theSrc1.y, theSrc2.x, theSrc2.y, theSrc3.x, theSrc3.y, theSrc4.x, theSrc4.y);

		final HomographyMatrix myTmp2;
		myTmp2 = getSquareToQuad(theDst1.x, theDst1.y, theDst2.x, theDst2.y, theDst3.x, theDst3.y, theDst4.x, theDst4.y);

		_myMatrix = HomographyMatrix.concatenate(myTmp1, myTmp2);

		_myInverseMatrix =  _myMatrix.inverse(); // will get null if there is no inverse
	}
	
	
	public PMatrix3D modelViewMatrix() {
		final PMatrix3D myM = new PMatrix3D();
	    
	    myM.m00 = (float) _myMatrix.m00;  	myM.m10 = (float) _myMatrix.m10;  	myM.m20 = 0;	myM.m30 = (float) _myMatrix.m20;
	    myM.m01 = (float) _myMatrix.m01;  	myM.m11 = (float) _myMatrix.m11;  	myM.m21 = 0;	myM.m31 = (float) _myMatrix.m21;
	    myM.m02 = 0;  						myM.m12 = 0;  						myM.m22 = 0;	myM.m32 = 0;
	    myM.m03 = (float) _myMatrix.m02;  	myM.m13 = (float) _myMatrix.m12;  	myM.m23 = 0;	myM.m33 = (float) _myMatrix.m22;
	    
	    return myM;
	}
	
	
    public PVector transform(final PVector theV) {
        
        final double myOrigX = theV.x;
        final double myOrigY = theV.y;
        
        final double myW = _myMatrix.m20 * myOrigX + _myMatrix.m21 * myOrigY + _myMatrix.m22;
        
        final double myX = (_myMatrix.m00 * myOrigX + _myMatrix.m01 * myOrigY + _myMatrix.m02) / myW; 
        final double myY = (_myMatrix.m10 * myOrigX + _myMatrix.m11 * myOrigY + _myMatrix.m12) / myW;

        final PVector myResult = new PVector((float)myX, (float)myY);
          	 
             
        return myResult;
    }
    
    
    public boolean hasInverse() {
    	return _myInverseMatrix != null;
    }
    
    
    public PVector transformInverse(final PVector theV) {
        
        final double myOrigX = theV.x;
        final double myOrigY = theV.y;
        
        final double myW = _myInverseMatrix.m20 * myOrigX + _myInverseMatrix.m21 * myOrigY + _myInverseMatrix.m22;
        
        final double myX = (_myInverseMatrix.m00 * myOrigX + _myInverseMatrix.m01 * myOrigY + _myInverseMatrix.m02) / myW; 
        final double myY = (_myInverseMatrix.m10 * myOrigX + _myInverseMatrix.m11 * myOrigY + _myInverseMatrix.m12) / myW;

        final PVector myResult = new PVector((float)myX, (float)myY);
          	 
        return myResult;
    }
	
	    
    public static HomographyMatrix getQuadToSquare(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
        final HomographyMatrix myTransform = getSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
        myTransform.set(myTransform.adjoint());
        return myTransform;
    }
 
	
    private static final HomographyMatrix getSquareToQuad(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
    	final HomographyMatrix myTransform = new HomographyMatrix();
    	
    	double dx3 = x0 - x1 + x2 - x3;
        double dy3 = y0 - y1 + y2 - y3;

        myTransform.m22 = 1.0F;

        if ((dx3 == 0.0f) && (dy3 == 0.0f)) { 
        	myTransform.m00 = x1 - x0;
        	myTransform.m01 = x2 - x1;
        	myTransform.m02 = x0;
        	myTransform.m10 = y1 - y0;
        	myTransform.m11 = y2 - y1;
        	myTransform.m12 = y0;
        	myTransform.m20 = 0.0F;
        	myTransform.m21 = 0.0F;
        } else {
            double dx1 = x1 - x2;
            double dy1 = y1 - y2;
            double dx2 = x3 - x2;
            double dy2 = y3 - y2;

            double invdet = 1.0F / (dx1 * dy2 - dx2 * dy1);
            myTransform.m20 = (dx3 * dy2 - dx2 * dy3) * invdet;
            myTransform.m21 = (dx1 * dy3 - dx3 * dy1) * invdet;
            myTransform.m00 = x1 - x0 + myTransform.m20 * x1;
            myTransform.m01 = x3 - x0 + myTransform.m21 * x3;
            myTransform.m02 = x0;
            myTransform.m10 = y1 - y0 + myTransform.m20 * y1;
            myTransform.m11 = y3 - y0 + myTransform.m21 * y3;
            myTransform.m12 = y0;
        }
        
        return myTransform;
    }
    
    
}
