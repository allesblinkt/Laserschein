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

import processing.xml.XMLElement;

/**
 * Settings for turning graphics into sample points.
 * This will likely be different for every laser system.
 * 
 * @author Benjamin Maus
 *
 */
public class OptimizerSettings implements TweakableSettings {
	
	/**
	 * Does a greedy sort on the paths. Also switches direction if needed.
	 */
	public boolean reorderFrame = true; 

	
	/**
	 * Blanking shift
	 */
	public int blankShift = 0; 
	
	
	/**
	 * When is it considered a curve
	 */
	public int smoothAngleTreshold = 1; 	 // TODO: to do
	
	
	/**
	 * How many points should be drawn on closed curves
	 */
	public int closedOverdraw = 1; 	 // TODO: to do
	
	/**
	 * This amount of points is always added at the start of a path
	 */
	public int extraPointsStart = 3; 
	
	/**
	 * This amount of points is always added at corners
	 */
	public int extraPointsCorner = 6;
	
	/**
	 * This amount of points is always added at curves
	 */
	public int extraPointsCurve = 0;  // TODO: to do
	
	/**
	 * This amount of points is always added at the end of a path
	 */
	public int extraPointsEnd = 3;  

	/**
	 * The maximum distance between individual points. If a distance is smaller 
	 * than this, it will be subdivided.
	 */
	public float maxTravel = 600/16000.0f; 

	/**
	 * The maximum distance between individual points. If a distance is smaller 
	 * than this, it will be subdivided.
	 */
	public float maxTravelBlank = 8000/16000.0f;   

	
	/**
	 * This amount of blanking points is added at the start of a path
	 */
	public int extraBlankPointsStart = 6;
	
	
	/**
	 * This amount of blanking points is added at the end of a path
	 */
	public int extraBlankPointsEnd = 6; 


	



	@Override
	public void loadFromXml(final XMLElement theXml) {
		final XMLElement myXml = theXml;
		
		final XMLElement myOptimize = myXml.getChild("optimize");
		reorderFrame = myOptimize.getBoolean("reorder", reorderFrame);
		blankShift = myOptimize.getInt("blankshift", blankShift);


		
		final XMLElement mySmooth = myXml.getChild("smooth");
		smoothAngleTreshold = mySmooth.getInt("angle", smoothAngleTreshold);
	
		
		final XMLElement myClosed = myXml.getChild("closed");
		closedOverdraw = myClosed.getInt("overdraw", closedOverdraw);
		
		
		final XMLElement myExtra = myXml.getChild("extra");
		extraPointsStart = myExtra.getInt("start", extraPointsStart);
		extraPointsCorner = myExtra.getInt("corner", extraPointsCorner);
		extraPointsCurve = myExtra.getInt("curve", extraPointsCurve);
		extraPointsEnd = myExtra.getInt("end", extraPointsEnd);
	
				
		final XMLElement myBlanks = myXml.getChild("blanks");
		extraBlankPointsStart = myBlanks.getInt("start", extraBlankPointsStart);
		extraBlankPointsEnd = myBlanks.getInt("end", extraBlankPointsEnd);

		
		final XMLElement myTravel = myXml.getChild("travel");
		maxTravel = myTravel.getFloat("drawing", maxTravel);
		maxTravelBlank = myTravel.getFloat("blank", maxTravelBlank);
	}



	@Override
	public XMLElement toXML() {
		final XMLElement myXml = new XMLElement();
		myXml.setName(this.xmlNamespace());
				
		final XMLElement myOptimize = new XMLElement();
		myOptimize.setName("optimize");
		myOptimize.setBoolean("reorder", reorderFrame);
		myOptimize.setInt("blankshift", blankShift);

		myXml.addChild(myOptimize);

		
		final XMLElement mySmooth = new XMLElement();
		mySmooth.setName("smooth");
		mySmooth.setInt("angle", smoothAngleTreshold);
		myXml.addChild(mySmooth);

		
		final XMLElement myClosed = new XMLElement();
		myClosed.setName("closed");
		myClosed.setInt("overdraw", closedOverdraw);
		myXml.addChild(myClosed);

		
		final XMLElement myExtra = new XMLElement();
		myExtra.setName("extra");
		myExtra.setInt("start", extraPointsStart);
		myExtra.setInt("corner", extraPointsCorner);
		myExtra.setInt("curve", extraPointsCurve);
		myExtra.setInt("end", extraPointsEnd);
		myXml.addChild(myExtra);

		
		final XMLElement myBlanks = new XMLElement();
		myBlanks.setName("blanks");
		myBlanks.setInt("start", extraBlankPointsStart);
		myBlanks.setInt("end", extraBlankPointsEnd);
		myXml.addChild(myBlanks);

		
		final XMLElement myTravel = new XMLElement();
		myTravel.setName("travel");
		myTravel.setFloat("drawing", maxTravel);
		myTravel.setFloat("blank", maxTravelBlank);
		myXml.addChild(myTravel);
		
		
		
		return myXml;
	}



	@Override
	public String xmlNamespace() {
		return "optimizer";
	} 

	
	
	
	
}
