package laserschein.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.sun.jna.Platform;


import laserschein.GeometrySettings;
import laserschein.LaserFrame;
import laserschein.Laserschein;
import laserschein.ui.AbstractTweaker.TweakerChangeListener;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;


@SuppressWarnings("serial")
public class ControlWindow extends PApplet {

	private int _myWidth, _myHeight;

	private Frame _myFrame;

	private boolean _myDoDrawSimulation = true;

	private boolean _myDoDrawCorrector = true;

	private Simulator _mySimulator;

	private PickQuad _myPicker;

	private DragManager _myDragManager;

	private Laserschein _mySchein;

	private NumberTweaker _myScaleXSlider;

	private NumberTweaker _myScaleYSlider;

	private BooleanTweaker _myLockScaleSlider;


	public static ControlWindow create(final Laserschein theSchein) {


		try {
			if(Platform.isWindows()) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} else {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}

		} catch (Exception e) {
			// We don't care
		}



		final ControlWindow myApplet = new ControlWindow(theSchein, 500, 500);
		myApplet.init();

		return myApplet;
	}


	private ControlWindow(final Laserschein theSchein, int theWidth, int theHeight) {
		_myWidth = theWidth;
		_myHeight = theHeight;

		_myFrame = new Frame("Simulacrum");
		_myFrame.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
		_myFrame.setResizable(false);
		_myFrame.setLayout(new BorderLayout());

		_myFrame.setFocusTraversalKeysEnabled(true);
		_myFrame.setFocusable(true);
		_mySchein = theSchein;

		final JTabbedPane myPane = new JTabbedPane();
		myPane.setPreferredSize(new Dimension(250, 0));


		//myPane.addTab("Optimizer", constructOptimizerPanel());
		myPane.addTab("Geometry", constructGeometryPanel());
		myPane.addTab("Simulation", constructSimulationPanel());


		_myFrame.add(myPane, BorderLayout.WEST);

		this.setPreferredSize(new Dimension(_myWidth, _myHeight));
		this.setSize(_myWidth, _myHeight);


		_myFrame.add(this);
		_myFrame.setSize(_myWidth + 250, _myHeight);
		_myFrame.setVisible(true);


		_myFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				unopen();
			}
		});


		_mySimulator = new Simulator();
	}


	private JPanel constructSimulationPanel() {
		final JPanel mySimulationPanel = new JPanel();
		mySimulationPanel.setOpaque(false);

		final BooleanTweaker mySimulationShowToggle = new BooleanTweaker("Show", true);
		mySimulationPanel.add(mySimulationShowToggle);
		mySimulationShowToggle.addChangeEventListener(new TweakerChangeListener<BooleanTweaker>() {

			@Override
			public void changed(BooleanTweaker theTweaker) {
				_myDoDrawSimulation = theTweaker.getValue();
			}
		});

		return mySimulationPanel;
	}



	private void updateOptimizerSettingsFromUi() {
		synchronized(_mySchein.optimizer()) {

		}
	}

	private void updateOptimizerUiFromSettings() {
		synchronized(_mySchein.optimizer()) {

		}
	}


	private void updateGeometrySettingsFromUi() {
		synchronized(_mySchein.geometry()) {
			final GeometrySettings mySettings = _mySchein.geometry().settings();

			mySettings.homographyDestination1.set(_myPicker.corner1.rawX(), _myPicker.corner1.rawY(), 0);
			mySettings.homographyDestination2.set(_myPicker.corner2.rawX(), _myPicker.corner2.rawY(), 0);
			mySettings.homographyDestination3.set(_myPicker.corner3.rawX(), _myPicker.corner3.rawY(), 0);
			mySettings.homographyDestination4.set(_myPicker.corner4.rawX(), _myPicker.corner4.rawY(), 0);

			mySettings.offset.set(_myPicker.offset);
			mySettings.scale.set(_myScaleXSlider.getValue(), _myScaleYSlider.getValue(), 0);


			_mySchein.geometry().updateTransforms();
		}
	}

	private void updateGeometryUiFromSettings() {
		synchronized(_mySchein.geometry()) {

		}
	}


	private JPanel constructGeometryPanel() {
		final JPanel myGeometryPanel = new JPanel();
		myGeometryPanel.setOpaque(false);

		_myScaleXSlider = new NumberTweaker("Scale X", 0, 0, 1, false); //TODO: read from settings
		_myScaleXSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {

			@Override
			public void changed(NumberTweaker theTweaker) {

				if(_myLockScaleSlider.getValue()) {
					_myScaleYSlider.setValue(_myScaleXSlider.getValue(), false);
				}

				updateGeometrySettingsFromUi();

			}
		});
		myGeometryPanel.add(_myScaleXSlider);

		_myScaleYSlider = new NumberTweaker("Scale Y", 1, 0, 1, false); //TODO: read from settings
		_myScaleYSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {

			@Override
			public void changed(NumberTweaker theTweaker) {

				System.out.println("SildeY");

				if(_myLockScaleSlider.getValue()) {
					_myScaleXSlider.setValue(_myScaleYSlider.getValue(), false);
				}

				updateGeometrySettingsFromUi();

			}
		});
		myGeometryPanel.add(_myScaleYSlider);

		_myLockScaleSlider = new BooleanTweaker("Lock", true);

		return myGeometryPanel;
	}


	public void updateFrame(final LaserFrame theFrame) {
		if(_myFrame.isVisible() && _myDoDrawSimulation){
			_mySimulator.update(theFrame);	
		}
	}


	public void setup() {
		size(_myWidth, _myHeight, OPENGL);
		background(0);
		frameRate(60);

		smooth();

		_myPicker = new PickQuad();
		_myDragManager = new DragManager();

		_myDragManager.add(_myPicker); // precedence
		_myDragManager.add(_myPicker.corner1);
		_myDragManager.add(_myPicker.corner2);
		_myDragManager.add(_myPicker.corner3);
		_myDragManager.add(_myPicker.corner4);


	}





	public void draw() {

		@SuppressWarnings("unused")
		final PGraphicsOpenGL myGl = (PGraphicsOpenGL)g;


		background(10);

		pushMatrix();

		scale(width*0.5f, height*0.5f);
		translate(1,1);

		if(_myDoDrawSimulation){
			_mySimulator.draw(g);
		}

		if(_myDoDrawCorrector){
			_myDragManager.draw();
			drawCorrector();	
		}

		popMatrix();

	}



	private void drawCorrector() {
		_myPicker.draw(g);
	}


	@Override
	public void mousePressed() {
		_myDragManager.pressed(mapMouseX(mouseX), mapMouseY(mouseY));
		updateGeometrySettingsFromUi();

	}


	@Override
	public void mouseReleased() {
		_myDragManager.released();
		updateGeometrySettingsFromUi();

	}


	@Override
	public void mouseDragged() {
		_myDragManager.dragged(mapMouseX(mouseX), mapMouseY(mouseY));
		updateGeometrySettingsFromUi();
	}


	@Override
	public void keyPressed() {
	}


	float mapMouseX(float theX) {
		return map(mouseX, 0, width, -1, 1);
	}


	float mapMouseY(float theY) {
		return map(mouseY, 0, height, -1, 1);
	}


	public void open() {
		_myFrame.setVisible(true);
		loop();
	}


	public void unopen() {
		noLoop();
		_myFrame.setVisible(false);
	}

}
