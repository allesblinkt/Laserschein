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


public class HomographyMatrix {
	double m00, m01, m02;
	double m10, m11, m12;
	double m20, m21, m22;


	private static final double EPSILON = 1.0e-10;


	public HomographyMatrix() {
		m00 = m11 = m22 = 1.0f;
		m01 = m02 = m10 = m12 = m20 = m21 = 0.0f;
	}


	public HomographyMatrix(final HomographyMatrix theOther) {
		this.set(theOther);
	}


	public void concatenate(final HomographyMatrix theM) {
		final HomographyMatrix myTmp = concatenate(this, theM);
		this.set(myTmp);
	}


	public HomographyMatrix inverse() {
		final HomographyMatrix myTmp = adjoint();
		
		if (Math.abs(myTmp.m22) < EPSILON) {
			return null;
		} 

		myTmp.normalize();
		
		return myTmp;
	}
	
	
	public void normalize() {
		final HomographyMatrix myTmp = normalize(this);
		set(myTmp);
	}


	public void set(final HomographyMatrix theM){
		m00 = theM.m00;
		m01 = theM.m01;
		m02 = theM.m02;
		m10 = theM.m10;
		m11 = theM.m11;
		m12 = theM.m12;
		m20 = theM.m20;
		m21 = theM.m21;
		m22 = theM.m22;
	}


	public HomographyMatrix adjoint() {
		final HomographyMatrix myTmp = new HomographyMatrix();

		myTmp.m00 = m11 * m22 - m12 * m21; // +
		myTmp.m10 = m12 * m20 - m10 * m22; // -
		myTmp.m20 = m10 * m21 - m11 * m20; // +
		myTmp.m01 = m02 * m21 - m01 * m22; // -
		myTmp.m11 = m00 * m22 - m02 * m20; // +
		myTmp.m21 = m01 * m20 - m00 * m21; // -
		myTmp.m02 = m01 * m12 - m02 * m11; // +
		myTmp.m12 = m02 * m10 - m00 * m12; // -
		myTmp.m22 = m00 * m11 - m01 * m10; // +

		return myTmp;
	}


	public static HomographyMatrix concatenate( final HomographyMatrix theA, final HomographyMatrix theB) {
		final HomographyMatrix myResult = new HomographyMatrix(); 		

		myResult.m00 = theA.m00 * theB.m00 + theA.m10 * theB.m01 + theA.m20 * theB.m02;
		myResult.m10 = theA.m00 * theB.m10 + theA.m10 * theB.m11 + theA.m20 * theB.m12;
		myResult.m20 = theA.m00 * theB.m20 + theA.m10 * theB.m21 + theA.m20 * theB.m22;
		myResult.m01 = theA.m01 * theB.m00 + theA.m11 * theB.m01 + theA.m21 * theB.m02;
		myResult.m11 = theA.m01 * theB.m10 + theA.m11 * theB.m11 + theA.m21 * theB.m12;
		myResult.m21 = theA.m01 * theB.m20 + theA.m11 * theB.m21 + theA.m21 * theB.m22;
		myResult.m02 = theA.m02 * theB.m00 + theA.m12 * theB.m01 + theA.m22 * theB.m02;
		myResult.m12 = theA.m02 * theB.m10 + theA.m12 * theB.m11 + theA.m22 * theB.m12;
		myResult.m22 = theA.m02 * theB.m20 + theA.m12 * theB.m21 + theA.m22 * theB.m22;

		return myResult;
	}



	public static HomographyMatrix normalize( final HomographyMatrix theM ) {
		final HomographyMatrix myResult = new HomographyMatrix();

		final double myScale = 1.0 / theM.m22;
		myResult.m00 *= myScale;
		myResult.m01 *= myScale;
		myResult.m02 *= myScale;
		myResult.m10 *= myScale;
		myResult.m11 *= myScale;
		myResult.m12 *= myScale;
		myResult.m20 *= myScale;
		myResult.m21 *= myScale;
		myResult.m22 = 1.0;

		return myResult;
	}

}
