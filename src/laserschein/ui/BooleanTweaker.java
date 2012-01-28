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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class BooleanTweaker extends AbstractTweaker<BooleanTweaker> {
	
	private final JLabel _myLabel;
	private final JCheckBox _myCheckbox;

	public BooleanTweaker(String theTitle, boolean theDefault) {
		super();
		
		this.setOpaque(false);
		
	
		_myLabel = new JLabel(theTitle);
		_myLabel.setFocusable(false);
		this.add(_myLabel);

		_myCheckbox = new JCheckBox();
		_myCheckbox.setSelected(theDefault);
		_myCheckbox.setFocusable(true);
		
		this.add(_myCheckbox);		
		
		
		_myCheckbox.addChangeListener(new ChangeListener() {	
			@Override
			public void stateChanged(ChangeEvent e) {
				notifyListeners();
			}
		});
		
	}
	
	
	
	public void setValue(boolean theValue, boolean theNotify) {
		_myCheckbox.setSelected(theValue);
		
		if(theNotify) {
			notifyListeners();
		}
	}
	
	
	public boolean getValue() {
		return _myCheckbox.isSelected();
	}


	

}
