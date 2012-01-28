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
package laserschein;

/**
 * AbstractLaserOutput defines the minimum functionality for an adaptor to 
 * a laser hardware interface.
 * 
 * @author allesblinkt
 */
public abstract class AbstractLaserOutput {
	
	protected OutputState _myState = OutputState.UNINITALIZED;
	
	protected int _myScanSpeed = 40000;

	
	
	/**
	 * Initializes the driver and other stuff. Gets called once when Laserschein is initialized.
	 */
	public abstract void initialize();

	
	/**
	 * This gets called for every frame to be displayed.
	 * @param theFrame to be displayed
	 */
	public abstract void draw(final LaserFrame theFrame);

	
	/**
	 * This gets called when the processing application shuts down.
	 * A good place to unload DLLs and clean up.  
	 */
	public abstract void destroy();
	
	
	/**
	 * Returns the maximum number of points per frame supported by the laser device.
	 * <b>Note:</b> This is fixed for some devices and dynamic for others...
	 * @return The maximum number of points per frame
	 */
	public abstract int getMaximumNumberOfPoints();
	
	
	/**
	 * Get the current scan speed.
	 * @return The scan speed in PPS
	 */
	public int getScanSpeed() {
		return _myScanSpeed;
	}
	
	
	/**
	 * Set the scan speed for the frames to follow
	 * @param theSpeed in PPS
	 */
	public void setScanSpeed(int theSpeed) {
		if(theSpeed > getMaximumScanSpeed()) {
			_myScanSpeed = getMaximumScanSpeed();
		} else if( theSpeed < getMinumumScanSpeed()){
			_myScanSpeed = getMinumumScanSpeed();
		} else {
			_myScanSpeed = theSpeed;
		}	
	}	
	
	/**
	 * Query the minimum supported scan speed
	 * @return The scan speed in PPS
	 */
	public abstract int getMinumumScanSpeed();

	

	/**
	 * Query the maximum supported scan speed
	 * @return The scan speed in PPS
	 */
	public abstract int getMaximumScanSpeed();


	/**
	 * @return
	 */
	public OutputState getState() {
		return _myState;
	}
	
	
	/**
	 * Describes the state the output module is in
	 * 
	 * @author allesblinkt
	 */
	public enum OutputState {
		UNINITALIZED,
		UNSUPPORTED_PLATFORM,
		LIBRARY_ERROR,
		NO_DEVICES_FOUND, 
		READY
	}

	
	
}
