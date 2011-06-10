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
 * Settings for turning graphics into sample points.
 * This will likely be different for every laser system.
 * 
 * @author allesblinkt
 *
 */
public class OptimizerSettings {
	
	/**
	 * <b>Not implemented yet!</b>
	 */
	public boolean reorderFrame = true; 

	/**
	 * Adds more points for sharper angles
	 */
	public boolean analyzeCornerAngles = true; 
	
	
	/**
	 * Up to this number of extra points are added to sharp angles
	 */
	public int extraCornerPointsAngleDependent = 3; 	

	
	/**
	 * This amount of points is always added at corners
	 */
	public int extraCornerPoints = 6;
	
	/**
	 * This amount of points is always added at the start of a path
	 */
	public int extraCornerPointsStart = 3; 
	
	/**
	 * This amount of points is always added at the end of a path
	 */
	public int extraCornerPointsEnd = 3; 

	
	/**
	 * The maximum distance between individual points. If a distance is smaller 
	 * than this, it will be subdivided.
	 */
	public int maxTravel = 600; 

	
	/**
	 * This amount of blanking points is added at the start of a path
	 */
	public int extraBlankPointsStart = 6;
	
	
	/**
	 * This amount of blanking points is added at the end of a path
	 */
	public int extraBlankPointsEnd = 6; 


	
	
	
}
