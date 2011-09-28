package laserschein.ui;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class BooleanTweaker extends JPanel {
	
	private final JLabel _myLabel;
	private final JCheckBox _myCheckbox;

	public BooleanTweaker(String theTitle, Object theModel) {
		super();
		
		this.setOpaque(false);
		
	
		_myLabel = new JLabel(theTitle);
		_myLabel.setFocusable(false);
		this.add(_myLabel);

		_myCheckbox = new JCheckBox();
		_myCheckbox.setFocusable(true);
		
		this.add(_myCheckbox);

		
		registerListeners();
		
		
	}
	
	
	
	public void setValue(boolean theValue) {
		_myCheckbox.setSelected(true);
	}
	
	
	public boolean getValue() {
		return _myCheckbox.isSelected();
	}

	private void registerListeners() {
	
		
		
	}
	
	
	
	
	
	
	

}
