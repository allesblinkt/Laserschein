package laserschein.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JComponent;




public class SettingsHandler {
	private final TweakableSettings _mySettings;
	
	public SettingsHandler(TweakableSettings theSettings) {
		_mySettings = theSettings;
	}
	
	public ArrayList<JComponent> createUIComponents(){
		final ArrayList<JComponent> myComponents = new ArrayList<JComponent>();
		
		Field[] myFields = _mySettings.getClass().getFields();
		
		for(Field myField:myFields) {
			System.out.println("Field!" + myField.getType());

			 Tweaker myTweaker = myField.getAnnotation(Tweaker.class);
			 
			 if(myTweaker != null) {
					System.out.println(myField.getType().toString());

				if(myField.getType().equals(int.class) ) {
					NumberTweaker myComponent = new NumberTweaker(myTweaker.name(), null);
					myComponents.add(myComponent);
				}
				
				if(myField.getType().equals(boolean.class) ) {
					BooleanTweaker myComponent = new BooleanTweaker(myTweaker.name(), null);
					myComponents.add(myComponent);				
				}
			 }
		}

			
		return myComponents;
	}
}
