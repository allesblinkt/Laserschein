package laserschein.ui;

import java.util.ArrayList;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class AbstractTweaker<T> extends JPanel {
	private ArrayList<TweakerChangeListener<T>> _myListeners;
	

	public void addChangeEventListener(final TweakerChangeListener<T> theListener) {
		if(_myListeners == null) {
			_myListeners = new ArrayList<TweakerChangeListener<T>>();
		}
		
		_myListeners.add(theListener);
	}
	
	
	public void removeChangeEventListener(final TweakerChangeListener<T> theListener) {
		if(_myListeners != null) {
			_myListeners.remove(theListener);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void notifyListeners() {
		if(_myListeners != null) { 
			for(final TweakerChangeListener<T> myListener:_myListeners) {
				myListener.changed((T)this);
			}
		}
	}
	
	
	public interface TweakerChangeListener<T> {
		public void changed(T theTweaker);
	}
}
