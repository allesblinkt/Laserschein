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
