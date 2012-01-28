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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.sun.jna.Platform;


import laserschein.GeometrySettings;
import laserschein.LaserFrame;
import laserschein.Laserschein;
import laserschein.OptimizerSettings;
import laserschein.ui.AbstractTweaker.TweakerChangeListener;
import processing.core.PApplet;
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

	
	/* Optimizer */
	private NumberTweaker _myOptBlankhiftSlider;
	private BooleanTweaker _myOptReorderToggle;
	private NumberTweaker _myOptAngleThresholdSlider;
	private NumberTweaker _myOptOverdrawSlider;
	private NumberTweaker _myOptExtraPointsStartSlider;
	private NumberTweaker _myOptExtraPointsEndSlider;
	private NumberTweaker _myOptExtraPointsCornerSlider;
	private NumberTweaker _myOptExtraPointsCurveSlider;
	private NumberTweaker _myOptExtraBlanksStart;
	private NumberTweaker _myOptExtraBlanksEnd;
	private NumberTweaker _myOptMaxTravelBlank;
	private NumberTweaker _myOptMaxTravelDrawing;
	
	
	boolean _myUiEventsEnabled = true;


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

		final ControlWindow myApplet = new ControlWindow(theSchein, 550, 550);
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

		myPane.addTab("Output", initSimulationPanel());

		myPane.addTab("Optimizer", initOptimizerPanel());
		myPane.addTab("Geometry", initGeometryPanel());


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

		_myFrame.pack();
	}


	private JPanel initOptimizerPanel() {
		final JPanel myOptimizerPanel = new JPanel();
		myOptimizerPanel.setOpaque(false);
		
		
		_myOptBlankhiftSlider = new NumberTweaker("Blanking shift", 0, -15, 15, true); 
		_myOptBlankhiftSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptBlankhiftSlider);
		
		
		_myOptReorderToggle = new BooleanTweaker("Reorder frame", false); 
		_myOptReorderToggle.addChangeEventListener(new TweakerChangeListener<BooleanTweaker>() {
			@Override
			public void changed(BooleanTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptReorderToggle);

		
		
		_myOptAngleThresholdSlider = new NumberTweaker("Angle threshold", 1, 0, 90, false); 
		_myOptAngleThresholdSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
	
//		myOptimizerPanel.add(_myOptAngleThresholdSlider);	// TODO: reenable
		
		
		

		_myOptOverdrawSlider = new NumberTweaker("Closed overdraw", 1, 0, 10, true); 
		_myOptOverdrawSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		_myOptOverdrawSlider.setEnabled(false);

//		myOptimizerPanel.add(_myOptOverdrawSlider);	 // TODO: reenable
		
		
		
		
		
		_myOptExtraPointsStartSlider = new NumberTweaker("Extra points at start", 1, 0, 10, true); 
		_myOptExtraPointsStartSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptExtraPointsStartSlider);	
		
		
		
		_myOptExtraPointsEndSlider = new NumberTweaker("Extra points at end", 1, 0, 10, true); 
		_myOptExtraPointsEndSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptExtraPointsEndSlider);	
		
		
		_myOptExtraPointsCornerSlider = new NumberTweaker("Extra points at corner", 1, 0, 10, true); 
		_myOptExtraPointsCornerSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptExtraPointsCornerSlider);	
		
		
		_myOptExtraPointsCurveSlider = new NumberTweaker("Extra points at curve", 1, 0, 10, true); 
		_myOptExtraPointsCurveSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		_myOptExtraPointsCurveSlider.setEnabled(false);

//		myOptimizerPanel.add(_myOptExtraPointsCurveSlider);	 // TODO: reenable
		

		
		_myOptExtraBlanksStart = new NumberTweaker("Extra blanks at start", 1, 0, 10, true); 
		_myOptExtraBlanksStart.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptExtraBlanksStart);	
		
		
		_myOptExtraBlanksEnd = new NumberTweaker("Extra blanks at end", 1, 0, 10, true); 
		_myOptExtraBlanksEnd.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptExtraBlanksEnd);	

		

		_myOptMaxTravelBlank = new NumberTweaker("Max travel blank", 1, 0, 1, false); 
		_myOptMaxTravelBlank.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptMaxTravelBlank);	
		
		
		_myOptMaxTravelDrawing = new NumberTweaker("Max travel drawing", 1, 0, 1, false); 
		_myOptMaxTravelDrawing.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {
			@Override
			public void changed(NumberTweaker theTweaker) {
				updateOptimizerSettingsFromUi();
			}
		});
		myOptimizerPanel.add(_myOptMaxTravelDrawing);	
		
		
		return myOptimizerPanel;
	}


	private JPanel initSimulationPanel() {
		final JPanel mySimulationPanel = new JPanel();
		mySimulationPanel.setOpaque(false);



		final JButton myLoadButton = new JButton("Load Settings");
		myLoadButton.setPreferredSize(new Dimension(200, 20));
		myLoadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_mySchein.loadDefaultSettings();
			}
		});
		mySimulationPanel.add(myLoadButton);		


		final JButton mySaveButton = new JButton("Save Settings");
		mySaveButton.setPreferredSize(new Dimension(200, 20));
		mySaveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_mySchein.saveDefaultSettings();

			}
		});
		mySimulationPanel.add(mySaveButton);	


		final BooleanTweaker mySimulationShowToggle = new BooleanTweaker("Show Simulation", true);
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
		if(_myUiEventsEnabled) {
		
		synchronized(_mySchein.optimizer()) {
			final OptimizerSettings mySettings = _mySchein.optimizer().settings();
			
			mySettings.blankShift = Math.round(_myOptBlankhiftSlider.getValue());
			
			mySettings.reorderFrame = _myOptReorderToggle.getValue();

			mySettings.smoothAngleTreshold = Math.round(_myOptAngleThresholdSlider.getValue());
			mySettings.closedOverdraw = Math.round(_myOptOverdrawSlider.getValue());
			
			mySettings.extraPointsStart = Math.round(_myOptExtraPointsStartSlider.getValue());
			mySettings.extraPointsCorner = Math.round(_myOptExtraPointsCornerSlider.getValue());
			mySettings.extraPointsCurve = Math.round(_myOptExtraPointsCurveSlider.getValue());
			mySettings.extraPointsEnd = Math.round(_myOptExtraPointsEndSlider.getValue());
					
			mySettings.extraBlankPointsStart = Math.round(_myOptExtraBlanksStart.getValue());
			mySettings.extraBlankPointsEnd = Math.round(_myOptExtraBlanksEnd.getValue());

			mySettings.maxTravel = _myOptMaxTravelDrawing.getValue();
			mySettings.maxTravelBlank = _myOptMaxTravelBlank.getValue();
		}
		
		}
	}
	

	private void updateOptimizerUiFromSettings() {
		
		disableUiEvents();
		
		synchronized(_mySchein.optimizer()) {
			final OptimizerSettings mySettings = _mySchein.optimizer().settings();
			
			_myOptBlankhiftSlider.setValue(mySettings.blankShift, false);

			_myOptReorderToggle.setValue(mySettings.reorderFrame, false);

			_myOptAngleThresholdSlider.setValue(mySettings.smoothAngleTreshold, false);
			_myOptOverdrawSlider.setValue(mySettings.closedOverdraw, false);
			
			_myOptExtraPointsStartSlider.setValue(mySettings.extraPointsStart, false);
			_myOptExtraPointsCornerSlider.setValue(mySettings.extraPointsCorner, false);
			_myOptExtraPointsCurveSlider.setValue(mySettings.extraPointsCurve, false);
			_myOptExtraPointsEndSlider.setValue(mySettings.extraPointsEnd, false);
					
			_myOptExtraBlanksStart.setValue(mySettings.extraBlankPointsStart, false);
			_myOptExtraBlanksEnd.setValue(mySettings.extraBlankPointsEnd, false);

			_myOptMaxTravelDrawing.setValue(mySettings.maxTravel, false);
			_myOptMaxTravelBlank.setValue(mySettings.maxTravelBlank, false);
		}
		
		
		enableUiEvents();
	}


	private void enableUiEvents() {
		_myUiEventsEnabled = true;	
	}


	private void disableUiEvents() {
		_myUiEventsEnabled = false;
	}


	private void updateGeometrySettingsFromUi() {
		if(_myUiEventsEnabled) {

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
	}

	
	
	public void refreshAllSettings() {
		updateGeometryUiFromSettings();
		updateOptimizerUiFromSettings();
	}


	private void resetGeometrySettings() {
		synchronized(_mySchein.geometry()){
			_mySchein.geometry().settings().reset();	
			_mySchein.geometry().updateTransforms();
		}

		updateGeometryUiFromSettings();

	}

	private void updateGeometryUiFromSettings() {
		disableUiEvents();

		
		synchronized(_mySchein.geometry()) {
			final GeometrySettings mySettings = _mySchein.geometry().settings();
			_myPicker.corner1.rawX(mySettings.homographyDestination1.x);
			_myPicker.corner1.rawY(mySettings.homographyDestination1.y);

			_myPicker.corner2.rawX(mySettings.homographyDestination2.x);
			_myPicker.corner2.rawY(mySettings.homographyDestination2.y);

			_myPicker.corner3.rawX(mySettings.homographyDestination3.x);
			_myPicker.corner3.rawY(mySettings.homographyDestination3.y);

			_myPicker.corner4.rawX(mySettings.homographyDestination4.x);
			_myPicker.corner4.rawY(mySettings.homographyDestination4.y);

			_myPicker.offset.set(mySettings.offset);
			_myScaleXSlider.setValue(mySettings.scale.x, false);
			_myScaleYSlider.setValue(mySettings.scale.y, false);


		}
		
		enableUiEvents();
	}


	private JPanel initGeometryPanel() {
		final JPanel myGeometryPanel = new JPanel();
		myGeometryPanel.setOpaque(false);

		_myScaleXSlider = new NumberTweaker("Scale X", 1, 0, 1, false); 
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

		_myScaleYSlider = new NumberTweaker("Scale Y", 1, 0, 1, false); 
		_myScaleYSlider.addChangeEventListener(new TweakerChangeListener<NumberTweaker>() {

			@Override
			public void changed(NumberTweaker theTweaker) {

				if(_myLockScaleSlider.getValue()) {
					_myScaleXSlider.setValue(_myScaleYSlider.getValue(), false);
				}

				updateGeometrySettingsFromUi();

			}
		});
		myGeometryPanel.add(_myScaleYSlider);

		_myLockScaleSlider = new BooleanTweaker("Lock", true);
		_myLockScaleSlider.addChangeEventListener(new TweakerChangeListener<BooleanTweaker>() {

			@Override
			public void changed(BooleanTweaker theTweaker) {
				if(theTweaker.getValue()){
					_myScaleYSlider.setValue(_myScaleXSlider.getValue(), true);

				}
			}
		});


		myGeometryPanel.add(_myLockScaleSlider);

		final JButton myButton = new JButton("reset");	
		myButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetGeometrySettings();
			}
		});
		myGeometryPanel.add(myButton);


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



		updateGeometryUiFromSettings();
		updateOptimizerUiFromSettings();


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
	
	
	public boolean isOpen() {
		return _myFrame.isVisible();
	}



}
