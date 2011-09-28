package laserschein.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class NumberTweaker extends JPanel {
	
	private final JLabel _myLabel;
	private final JSlider _mySlider;
	private final JTextField _myTextField;

	public NumberTweaker(String theTitle, Object theModel) {
		super(new GridBagLayout());
		
		this.setOpaque(false);
		
		GridBagConstraints myConstraints = new GridBagConstraints();
		
		_myLabel = new JLabel(theTitle);
		_myLabel.setFocusable(false);
		myConstraints.gridwidth = 1;
		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		myConstraints.gridx = 0; myConstraints.gridy = 0; 
		this.add(_myLabel, myConstraints);

	
		_mySlider = new JSlider(0, 10);
		_mySlider.setFocusable(true);
		_mySlider.setPreferredSize(new Dimension(200, 8));
		myConstraints.gridwidth = 3;
		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		myConstraints.gridx = 0; myConstraints.gridy = 1; 
		this.add(_mySlider, myConstraints);

		
		_myTextField = new JTextField();
		_myTextField.setColumns(2);
		_myTextField.setFocusable(true);
		myConstraints.gridwidth = 1;
		

		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		myConstraints.gridx = 3; myConstraints.gridy = 1; 
		this.add(_myTextField, myConstraints);

		
		registerListeners();
		
		setValue(1);
		
	}
	
	
	public boolean valueValid(int theValue) {
		if(theValue <= _mySlider.getMaximum() && theValue >= _mySlider.getMinimum()){
			return true;
		} else {
			return false;
		}
	}
	
	public void setValue(int theValue) {
		if(valueValid(theValue)){
			_mySlider.setValue(theValue);
			_myTextField.setText(theValue+"");
		}
	}
	
	
	public int getValue() {
		return _mySlider.getValue();
	}

	private void registerListeners() {
		_mySlider.addChangeListener(new ChangeListener() {
	
			@Override
			public void stateChanged(ChangeEvent e) {
				sliderChanged();
			}
		});
		
		
		_myTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				textChanged();
			}
			
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
	}
	
	private void sliderChanged() {
		final int myValue = _mySlider.getValue();
		_myTextField.setText(myValue+"");
	}

	
	
	private void textChanged() {
		try {
			int myValue = Integer.valueOf( _myTextField.getText() );
			
			System.out.println(myValue);
			
			if(valueValid(myValue)){
				_mySlider.setValue(myValue);
			}
			
		} catch(NumberFormatException e) {
			_myTextField.setText(_mySlider.getValue()+"");
		}
	}

}
