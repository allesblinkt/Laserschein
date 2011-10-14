package laserschein.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;


import laserschein.LaserFrame;
import laserschein.OptimizerSettings;
import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;


import controlP5.*;


@SuppressWarnings("serial")
public class ControlWindow extends PApplet {

	private int _myWidth, _myHeight;

	private Frame _myFrame;

	private boolean _myDoDrawSimulation = true;

	private boolean _myDoDrawCorrector = true;

	private Simulator _mySimulator;
	
	private PickQuad _myPicker;
	
	private DragManager _myDragManager;

	private ControlP5 _myControls;
	
	public static ControlWindow create() {
		final ControlWindow myApplet = new ControlWindow(500, 500);
		myApplet.init();
		
		return myApplet;
	}


	private ControlWindow(int theWidth, int theHeight) {
		_myWidth = theWidth;
		_myHeight = theHeight;
				
		_myFrame = new Frame("Simulacrum");
		_myFrame.setFont(new Font(Font.DIALOG, Font.PLAIN, 5));
		_myFrame.setResizable(false);
		_myFrame.setLayout(new BorderLayout());

		_myFrame.setFocusTraversalKeysEnabled(true);
		_myFrame.setFocusable(true);

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

		_myFrame.add(myPane, BorderLayout.WEST);

		this.setPreferredSize(new Dimension(_myWidth, _myHeight));
		this.setSize(_myWidth, _myHeight);
		
		
		_myFrame.add(this);
		_myFrame.setSize(_myWidth + 200, _myHeight);
		_myFrame.setVisible(true);
		

		_myFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				unopen();
			}
		});
		
		
		_mySimulator = new Simulator();
	}


	public void updateFrame(final LaserFrame theFrame) {
		if(_myFrame.isVisible()){
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
		
		
		_myControls = new ControlP5(this);
		buildControls();
		
	}


	private void buildControls() {

		Slider mySlider =_myControls.addSlider("foo", 0, 9);
		Slider mySlider2 =_myControls.addSlider("foo2", 0, 9);
		
		
		_myControls.disableKeys();
		_myControls.setAutoDraw(false);

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
		
		drawGui();
	}
	
	
	private void drawGui() {
		  hint(DISABLE_DEPTH_TEST);
		  _myControls.draw();
		  
		  hint(ENABLE_DEPTH_TEST);
	}
	
	
	private void drawCorrector() {
		_myPicker.draw(g);
	}
	
	
	@Override
	public void mousePressed() {
		_myDragManager.pressed(mapMouseX(mouseX), mapMouseY(mouseY));
	}
	
	
	@Override
	public void mouseReleased() {
		_myDragManager.released();
	}
	
	
	@Override
	public void mouseDragged() {
		_myDragManager.dragged(mapMouseX(mouseX), mapMouseY(mouseY));
	}
	
	
	@Override
	public void keyPressed() {
		println("key pressed");
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
