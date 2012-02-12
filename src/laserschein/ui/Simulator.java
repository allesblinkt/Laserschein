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
package laserschein.ui;

import javax.media.opengl.GL;

import laserschein.LaserFrame;
import laserschein.LaserPoint;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;


/**
 * This is a rudimentary simulator for laser output. It is based on the simulator 
 * in openlase by Hector Martin.
 * @see http://git.marcansoft.com/?p=openlase.git
 *
 */
public class Simulator {

	static final int HISTORY_SIZE = 48 * 50;
	static final int BUFFER_SIZE = HISTORY_SIZE + 48000;
	static final int BLANKING_DELAY = 2;

	private int _myBufferPointer = 0;
	private PreviewSample[] _myBuffer = new PreviewSample[BUFFER_SIZE];


	public Simulator() {
		for (int i = 0; i < BUFFER_SIZE; i++) {
			_myBuffer[i] = new PreviewSample();
		}
	}


	public void draw(final PGraphics theG){
		theG.noFill();

		PGraphicsOpenGL myPgl = (PGraphicsOpenGL) theG;

		final GL myGl = myPgl.gl;

		theG.hint(PGraphics.DISABLE_DEPTH_TEST);

		myGl.glEnable(GL.GL_BLEND);
		myGl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		myGl.glBlendEquation(GL.GL_FUNC_ADD);
		myGl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);


		theG.pushMatrix();

		synchronized (this) {

			theG.strokeWeight(2);

			int myIndex = (_myBufferPointer - HISTORY_SIZE + BUFFER_SIZE) % BUFFER_SIZE;

			final PreviewSample myLastSample = new PreviewSample();

			float[] rdelay = new float[BLANKING_DELAY];
			float[] gdelay = new float[BLANKING_DELAY];
			float[] bdelay = new float[BLANKING_DELAY];

			for (int i = 0; i < HISTORY_SIZE; i++) {

				final PreviewSample mySample = new PreviewSample(_myBuffer[myIndex]);

				float myR = 0, myG = 0, myB = 0;

				/* Lowpass filter */
				mySample.x = myLastSample.x * 0.65f + mySample.x * 0.35f;
				mySample.y = myLastSample.y * 0.65f + mySample.y * 0.35f;
				
				/* Brightness delay */
				rdelay[(BLANKING_DELAY + i - 1) % BLANKING_DELAY] = mySample.r;
				mySample.r = rdelay[i % BLANKING_DELAY];

				gdelay[(BLANKING_DELAY + i - 1) % BLANKING_DELAY] = mySample.g;
				mySample.g = gdelay[i % BLANKING_DELAY];

				bdelay[(BLANKING_DELAY + i - 1) % BLANKING_DELAY] = mySample.b;
				mySample.b = bdelay[i % BLANKING_DELAY];

				
				float myDistance = PApplet.dist(mySample.x, mySample.y, myLastSample.x, myLastSample.y);
				if (myDistance == 0) {
					myDistance = 0.0001f;
				}

				float myDistanceFactor = 0.01f / myDistance;
				if (myDistanceFactor > 1.5f) {
					myDistanceFactor = 1.5f;
				}

				int myAge = HISTORY_SIZE - i;

				float myFactor = (HISTORY_SIZE - myAge) / (float) HISTORY_SIZE;
				myFactor = myFactor * myFactor;

				if (PApplet.abs(mySample.x - myLastSample.x) < 0.001f && 
					PApplet.abs(mySample.y - myLastSample.y) < 0.001f) {
					myR = (mySample.r - 0.2f) * myFactor * 1.4f;
					myG = (mySample.g - 0.2f) * myFactor * 1.4f;
					myB = (mySample.b - 0.2f) * myFactor * 1.4f;

					theG.beginShape(PGraphics.POINTS);
					laserColor(theG, myR, myG, myB, 0.08f);
					theG.vertex(mapX(mySample.x), mapY(mySample.y), 0);
					theG.endShape();
				} else {
					myR = (mySample.r - 0.2f) * myFactor * myDistanceFactor * 1.8f;
					myG = (mySample.g - 0.2f) * myFactor * myDistanceFactor * 1.8f;
					myB = (mySample.b - 0.2f) * myFactor * myDistanceFactor * 1.8f;

					theG.beginShape();
					laserColor(theG, myLastSample.r, myLastSample.g, myLastSample.b, 0.8f);
					theG.vertex(mapX(myLastSample.x), mapY(myLastSample.y), 0);
					laserColor(theG, myR, myG, myB, 0.8f);
					theG.vertex(mapX(mySample.x), mapY(mySample.y), 0);
					theG.endShape();
				}

				myLastSample.x = mySample.x;
				myLastSample.y = mySample.y;

				myLastSample.r = myR;
				myLastSample.g = myG;
				myLastSample.b = myB;
				
				myIndex++;
				if (myIndex >= BUFFER_SIZE) {
					myIndex = 0;
				}

			}
		}

		theG.popMatrix();

		myGl.glDisable(GL.GL_BLEND);
	}



	public synchronized void update(final LaserFrame theFrame) {

		for (final LaserPoint myPoint : theFrame.points()) {
			PreviewSample mySample = new PreviewSample();
			_myBuffer[_myBufferPointer] = mySample;

			mySample.x = PApplet.constrain(myPoint.x, -1, 1);
			mySample.y = PApplet.constrain(myPoint.y, -1, 1);
			mySample.r = PApplet.map(myPoint.r, 0, 255, 0, 1.0f);
			mySample.g = PApplet.map(myPoint.g, 0, 255, 0, 1.0f);
			mySample.b = PApplet.map(myPoint.b, 0, 255, 0, 1.0f);

			if (myPoint.isBlanked) {
				mySample.r = 0;
				mySample.g = 0;
				mySample.b = 0;
			}

			_myBufferPointer++;
			if (_myBufferPointer >= BUFFER_SIZE) {
				_myBufferPointer = 0;
			}

		}
	}


	private float mapX(float theX) {
		return theX;
	}


	private float mapY(float theY) {
		return theY;
	}


	private void laserColor(final PGraphics theGd, float theRed, float theGreen, float theBlue, 
							float theAlphaScale) {
		theGd.stroke(
				PApplet.constrain(theRed * 255, 0, 255), 
				PApplet.constrain(theGreen * 255, 0, 255), 
				PApplet.constrain(theBlue * 255, 0, 255), 
				PApplet.constrain(theAlphaScale * 255, 0, 255)
				);
	}	


	private class PreviewSample {
		float x;
		float y;
		
		float r;
		float g;
		float b;
		

		public PreviewSample() {
			this.x = 0;
			this.y = 0;
			
			this.r = 0;
			this.g = 0;
			this.b = 0;
		}


		public PreviewSample(final PreviewSample theOther) {
			this.x = theOther.x;
			this.y = theOther.y;
			
			this.r = theOther.r;
			this.g = theOther.g;
			this.b = theOther.b;
		}
	}
}



