/**
 *  
 *  Laserschein. interactive ILDA output from processing and java
 *
 *  2011 by Benjamin Maus
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
}
