package laserschein;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;


import laserschein.ui.SettingsHandler;
import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

@SuppressWarnings("serial")
public class SimulatorWindow extends PApplet {

	private int _myWidth, _myHeight;

	static final int HISTORY_SIZE = 48 * 50;
	static final int BUFFER_SIZE = HISTORY_SIZE + 48000;
	static final int BLANKING_DELAY = 2;

	private int _myBufferPointer = 0;
	private PreviewSample[] _myBuffer = new PreviewSample[BUFFER_SIZE];


	public static SimulatorWindow create() {
		final SimulatorWindow myApplet = new SimulatorWindow(500, 500);
		
		final Frame myFrame = new Frame("Simulacrum");
		myFrame.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
		myFrame.setResizable(false);
		myFrame.setLayout(new BorderLayout());

		myFrame.setFocusTraversalKeysEnabled(true);
		myFrame.setFocusable(true);

		JTabbedPane myPane = new JTabbedPane();
		myPane.setPreferredSize(new Dimension(300, 0));

		final JPanel myGeometryPanel = new JPanel();
		myGeometryPanel.setOpaque(false);
		myPane.addTab("Geometry", myGeometryPanel);

		// myGeometryPanel.setLayout(new BoxLayout(myGeometryPanel,
		// BoxLayout.Y_AXIS));
		// myGeometryPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		for (int i = 0; i < 6; i++) {
			JLabel testlabel = new JLabel("Start", JLabel.LEFT);
			testlabel.setAlignmentX(JSlider.LEFT_ALIGNMENT);
			// testlabel.setMaximumSize(new Dimension(100, 20));

			JSlider testslider = new JSlider(0, 10);
			testslider.setSnapToTicks(true);
			testslider.setPaintLabels(true);

			testslider.setMajorTickSpacing(5);
			testslider.setMinorTickSpacing(1);

			testslider.setPaintTicks(true);
			// testslider.setMaximumSize(new Dimension(100, 20));

			myGeometryPanel.add(testlabel);

			myGeometryPanel.add(testslider);
			// myGeometryPanel.add(new JSlider());
		}

		final JPanel myOutputPanel = new JPanel();
		myOutputPanel.setOpaque(false);
		myPane.addTab("Output", new JSpinner());

		final JPanel mySimulation = new JPanel();
		mySimulation.setOpaque(false);
		myPane.addTab("Simulation", mySimulation);

		SettingsHandler myHandler = new SettingsHandler(new OptimizerSettings());
		ArrayList<JComponent> myComponents = myHandler.createUIComponents();
		
		for(final JComponent myComponent:myComponents){
			mySimulation.add(myComponent);

		}

		myFrame.add(myPane, BorderLayout.WEST);

		myApplet.setPreferredSize(new Dimension(500, 500));
		myFrame.add(myApplet);

		// JSlider mySlider = new JSlider();
		// myFrame.add(mySlider, BorderLayout.WEST);

		myFrame.setSize(800, 500);
		myFrame.setVisible(true);

		myApplet.init();

		myFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent evt) {
				myApplet.dispose();
			}
		});

	

		return myApplet;
	}


	private SimulatorWindow(int theWidth, int theHeight) {
		_myWidth = theWidth;
		_myHeight = theHeight;

	}


	public void update(final LaserFrame theFrame) {
		synchronized (_myBuffer) {

			// _myBufferPointer = 0;

			for (final LaserPoint myPoint : theFrame.points()) {
				PreviewSample mySample = new PreviewSample();
				_myBuffer[_myBufferPointer] = mySample;

				mySample.x = constrain(map(myPoint.x, 0, LaserPoint.COORDINATE_RANGE, -1, 1), -1, 1);
				mySample.y = constrain(map(myPoint.y, 0, LaserPoint.COORDINATE_RANGE, -1, 1), -1, 1);
				mySample.r = map(myPoint.r, 0, 255, 0, 1.0f);
				mySample.g = map(myPoint.g, 0, 255, 0, 1.0f);
				mySample.b = map(myPoint.b, 0, 255, 0, 1.0f);

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

	}


	public void setup() {
		size(_myWidth, _myHeight, OPENGL);
		background(0);
		frameRate(60);

		smooth();

		for (int i = 0; i < BUFFER_SIZE; i++) {
			_myBuffer[i] = new PreviewSample();
		}
	}


	public void draw() {

		background(10);
		noFill();

		PGraphicsOpenGL myPgl = (PGraphicsOpenGL) g;

		GL myGl = myPgl.gl;

		hint(DISABLE_DEPTH_TEST);
		//
		myGl.glEnable(GL.GL_BLEND);
		myGl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		myGl.glBlendEquation(GL.GL_FUNC_ADD);
		myGl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);

		synchronized (_myBuffer) {

			strokeWeight(2);

			int myIndex = (_myBufferPointer - HISTORY_SIZE + BUFFER_SIZE) % BUFFER_SIZE;

			final PreviewSample myLastSample = new PreviewSample();

			float[] rdelay = new float[BLANKING_DELAY];
			float[] gdelay = new float[BLANKING_DELAY];
			float[] bdelay = new float[BLANKING_DELAY];

			for (int i = 0; i < HISTORY_SIZE; i++) {

				final PreviewSample mySample = new PreviewSample(_myBuffer[myIndex]);

				float myR = 0, myG = 0, myB = 0;

				// // lowpass
				mySample.x = myLastSample.x * 0.65f + mySample.x * 0.35f;
				mySample.y = myLastSample.y * 0.65f + mySample.y * 0.35f;
				// // delay brightness

				rdelay[(BLANKING_DELAY + i - 1) % BLANKING_DELAY] = mySample.r;
				mySample.r = rdelay[i % BLANKING_DELAY];

				gdelay[(BLANKING_DELAY + i - 1) % BLANKING_DELAY] = mySample.g;
				mySample.g = gdelay[i % BLANKING_DELAY];

				bdelay[(BLANKING_DELAY + i - 1) % BLANKING_DELAY] = mySample.b;
				mySample.b = bdelay[i % BLANKING_DELAY];

				float myDistance = dist(mySample.x, mySample.y, myLastSample.x, myLastSample.y);
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

				if (abs(mySample.x - myLastSample.x) < 0.001f && abs(mySample.y - myLastSample.y) < 0.001f) {
					myR = (mySample.r - 0.2f) * myFactor * 1.4f;
					myG = (mySample.g - 0.2f) * myFactor * 1.4f;
					myB = (mySample.b - 0.2f) * myFactor * 1.4f;

					beginShape(POINTS);
					laserColor(myGl, myR, myG, myB, 0.08f);
					vertex(mapX(mySample.x), mapY(mySample.y), 0);
					endShape();
				} else {
					myR = (mySample.r - 0.2f) * myFactor * myDistanceFactor * 1.8f;
					myG = (mySample.g - 0.2f) * myFactor * myDistanceFactor * 1.8f;
					myB = (mySample.b - 0.2f) * myFactor * myDistanceFactor * 1.8f;

					beginShape();
					laserColor(myGl, myLastSample.r, myLastSample.g, myLastSample.b, 0.8f);
					vertex(mapX(myLastSample.x), mapY(myLastSample.y), 0);
					laserColor(myGl, myR, myG, myB, 0.8f);
					vertex(mapX(mySample.x), mapY(mySample.y), 0);
					endShape();
				}

				myLastSample.x = mySample.x;
				myLastSample.y = mySample.y;

				myLastSample.r = myR;
				myLastSample.g = myG;
				myLastSample.b = myB;
				//
				myIndex++;
				if (myIndex >= BUFFER_SIZE) {
					myIndex = 0;
				}

			}
		}

	}


	private float mapX(float theX) {
		return ((theX + 1) * 0.5f) * width;
	}


	private float mapY(float theY) {
		return ((theY + 1) * 0.5f) * height;
	}


	private void laserColor(final GL theGl, float theR, float theG, float theB, float theAlphaScale)
	{

		stroke(constrain(theR * 255, 0, 255), constrain(theG * 255, 0, 255), constrain(theB * 255, 0, 255), constrain(theAlphaScale * 255, 0, 255));
	}


	@Override
	public void keyPressed() {
		println("key pressed");
	}

	public class PreviewSample {

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
