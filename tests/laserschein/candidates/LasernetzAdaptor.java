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
package laserschein.candidates;

import laserschein.AbstractLaserOutput;
import laserschein.LaserFrame;


public class LasernetzAdaptor extends AbstractLaserOutput{


	
	@Override
	public void initialize() {
		// TODO Open network
		
		
		
	}

	@Override
	public void draw(LaserFrame theFrame) {
		// TODO Check for network
		
		// TODO Update capabilities
		
		
	}

	@Override
	public void destroy() {
		// TODO Close network 
		
	}

	@Override
	public int getMaximumNumberOfPoints() {
		 // TODO: Query from the network? Probably...
		return 0;
	}


	@Override
	public int getMinumumScanSpeed() {
		return 1;
	}

	@Override
	public int getMaximumScanSpeed() {
		return 100000; // TODO: Query from the network? Probably...
	}

}
