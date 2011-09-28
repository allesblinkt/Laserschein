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

import laserschein.ui.TweakableSettings;
import laserschein.ui.Tweaker;

/**
 * Settings for turning graphics into sample points.
 * This will likely be different for every laser system.
 * 
 * @author allesblinkt
 *
 */
public class OptimizerSettings implements TweakableSettings {
	
	/**
	 * Does a greedy sort on the paths. Also switches direction if needed.
	 */
	@Tweaker(name = "Reorder frame", min=0, max=1, description="")
	public boolean reorderFrame = true; 

	
	/**
	 * When is it considered a curve
	 */
	@Tweaker(name = "Smooth angle treshold", min=0, max=10, description="")
	public int smoothAngleTreshold = 1; 	

	
	
	/**
	 * How many points should be drawn on closed curves
	 */
	@Tweaker(name = "Closed overdraw", min=0, max=10, description="")
	public int closedOverdraw = 1; 	

	

	
	/**
	 * This amount of points is always added at the start of a path
	 */
	@Tweaker(name = "Extra start", min=0, max=10, description="")
	public int extraCornerPointsStart = 3; 
	
	/**
	 * This amount of points is always added at corners
	 */
	@Tweaker(name = "Extra corner", min=0, max=10, description="")
	public int extraCornerPoints = 6;
	
	/**
	 * This amount of points is always added at curves
	 */
	@Tweaker(name = "Extra corner", min=0, max=10, description="")
	public int extraCurvePoints = 0;
	
	/**
	 * This amount of points is always added at the end of a path
	 */
	@Tweaker(name = "Extra end", min=0, max=10, description="")
	public int extraCornerPointsEnd = 3; 

	/**
	 * The maximum distance between individual points. If a distance is smaller 
	 * than this, it will be subdivided.
	 */
	@Tweaker(name = "Maximum Travel", min=0, max=2000, description="")
	public int maxTravel = 600; 

	/**
	 * The maximum distance between individual points. If a distance is smaller 
	 * than this, it will be subdivided.
	 */
	@Tweaker(name = "Maximum Travel Blank", min=0, max=8000, description="")
	public int maxTravelBlank = 8000; 

	
	/**
	 * This amount of blanking points is added at the start of a path
	 */
	@Tweaker(name="Blanks at Start", min=0, max=20, description="")
	public int extraBlankPointsStart = 6;
	
	
	/**
	 * This amount of blanking points is added at the end of a path
	 */
	@Tweaker(name="Blanks at End", min=0, max=20, description="")
	public int extraBlankPointsEnd = 6; 


	
	/**
	 * Eliminates duplicate points
	 */
	@Tweaker(name = "Eliminate duplicates", min=0, max=1, description="")
	public boolean eliminateDuplicates = true; 

	
	
	
	
}
