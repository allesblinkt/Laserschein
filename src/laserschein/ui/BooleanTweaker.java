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
		_myCheckbox.setSelected(true);
		
		if(theNotify) {
			notifyListeners();
		}
	}
	
	
	public boolean getValue() {
		return _myCheckbox.isSelected();
	}


	

}
