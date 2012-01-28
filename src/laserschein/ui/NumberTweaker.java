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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class NumberTweaker extends AbstractTweaker<NumberTweaker> {

	private final JLabel _myLabel;
	private final JSlider _mySlider;
	private final JTextField _myTextField;

	private  float _myMin;
	private  float _myMax;

	private NumberFormat _myFormat;

	private static int DIVISIONS = 200;

	public NumberTweaker(String theTitle, float theDefault, float theMin, float theMax, boolean theIsInt) {

		_myMin = theMin;
		_myMax = theMax;



		if(theIsInt) {
			_myFormat = NumberFormat.getIntegerInstance();
		} else {
			_myFormat = NumberFormat.getNumberInstance();
		}


		this.setLayout(new GridBagLayout());

		this.setOpaque(false);

		GridBagConstraints myConstraints = new GridBagConstraints();

		_myLabel = new JLabel(theTitle);
		_myLabel.setFocusable(false);
		myConstraints.gridwidth = 1;
		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		myConstraints.gridx = 0; myConstraints.gridy = 0; 
		this.add(_myLabel, myConstraints);


		_mySlider = new JSlider(0, DIVISIONS);
		_mySlider.setFocusable(true);
		_mySlider.setPreferredSize(new Dimension(200, 16));
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

		registerSwingListeners();

		setValue(theDefault, true);

	}



	private int transformValueToSlider(float theValue) {
		float myValue = PApplet.map(theValue, _myMin, _myMax, 0, DIVISIONS);;
		return Math.round(myValue); 
	}


	private float transformSliderToValue(int theValue) {
		float myValue = PApplet.map(theValue, 0, DIVISIONS, _myMin, _myMax);
		return myValue;
	}

	public boolean valueValid(float theValue) {
		if(theValue <= _myMax && theValue >= _myMin){
			return true;
		} else {
			return false;
		}
	}

	public void setValue(float theValue, boolean theNotify) {
		if(valueValid(theValue)){

			_mySlider.setValue(transformValueToSlider(theValue));
			_myTextField.setText(_myFormat.format(theValue));

			if(theNotify) {
				notifyListeners();
			} 
		}
	}


	public float getValue() {
		return transformSliderToValue(_mySlider.getValue());
	}

	private void registerSwingListeners() {
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
		_myTextField.setText(_myFormat.format(transformSliderToValue(myValue)));
		notifyListeners();
	}



	private void setTextFieldToSliderValue() {
		_myTextField.setText(_myFormat.format(transformSliderToValue(_mySlider.getValue())));
	}


	private void textChanged() {
		try {
			float myValue =  _myFormat.parse( _myTextField.getText() ).floatValue();


			if(valueValid(myValue)){
				_mySlider.setValue(transformValueToSlider(myValue));
				notifyListeners();
			} else {
				setTextFieldToSliderValue();
			}
		} catch(NumberFormatException e) {
			setTextFieldToSliderValue();

		} catch (ParseException e) {
			setTextFieldToSliderValue();


		}
	}

}
