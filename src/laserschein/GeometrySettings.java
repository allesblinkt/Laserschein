package laserschein;

import processing.core.PVector;
import processing.xml.XMLElement;


public class GeometrySettings implements TweakableSettings{

	public PVector scale;
	public PVector offset;
	
	public PVector homographySource1;
	public PVector homographySource2;
	public PVector homographySource3;
	public PVector homographySource4;

	public PVector homographyDestination1;
	public PVector homographyDestination2;
	public PVector homographyDestination3;
	public PVector homographyDestination4;
	
	
	public GeometrySettings() {
		reset();
	}
	

	
	@Override
	public void loadFromXml(final XMLElement theXml) {
		final XMLElement myXml = theXml;
		
		final XMLElement myHomography = myXml.getChild("homography");
		
		if(myHomography != null) {
			homographySource1.set( Util.pVectorFromXml(myHomography.getChild("source1"), homographySource1));
			homographySource2.set( Util.pVectorFromXml(myHomography.getChild("source2"), homographySource2));
			homographySource3.set( Util.pVectorFromXml(myHomography.getChild("source3"), homographySource3));
			homographySource4.set( Util.pVectorFromXml(myHomography.getChild("source4"), homographySource4));
			
			homographyDestination1.set( Util.pVectorFromXml(myHomography.getChild("destination1"), homographyDestination1));
			homographyDestination2.set( Util.pVectorFromXml(myHomography.getChild("destination2"), homographyDestination2));
			homographyDestination3.set( Util.pVectorFromXml(myHomography.getChild("destination3"), homographyDestination3));
			homographyDestination4.set( Util.pVectorFromXml(myHomography.getChild("destination4"), homographyDestination4));
		}
		
	
		scale.set( Util.pVectorFromXml(myXml.getChild("scale"), scale));
		offset.set( Util.pVectorFromXml(myXml.getChild("offset"), offset));
	
	}



	@Override
	public XMLElement toXML() {
		final XMLElement myXml = new XMLElement();
		myXml.setName(this.xmlNamespace());
				
		final XMLElement myHomography = new XMLElement();
		
		myHomography.setName("homography");
		
		myHomography.addChild(Util.xmlFromPVector("src1", homographySource1));
		myHomography.addChild(Util.xmlFromPVector("src2", homographySource2));
		myHomography.addChild(Util.xmlFromPVector("src3", homographySource3));
		myHomography.addChild(Util.xmlFromPVector("src4", homographySource4));
		
		myHomography.addChild(Util.xmlFromPVector("destination1", homographyDestination1));
		myHomography.addChild(Util.xmlFromPVector("destination2", homographyDestination2));
		myHomography.addChild(Util.xmlFromPVector("destination3", homographyDestination3));
		myHomography.addChild(Util.xmlFromPVector("destination4", homographyDestination4));
		myXml.addChild(myHomography);

	
		myXml.addChild(Util.xmlFromPVector("scale", scale));
		myXml.addChild(Util.xmlFromPVector("offset", offset));
	
		
		return myXml;
	}



	@Override
	public String xmlNamespace() {
		return "geometry";
	}



	public void reset() {
		homographySource1 = new PVector(-1,-1);
		homographySource2 = new PVector(1,-1);
		homographySource3 = new PVector(1,1);
		homographySource4 = new PVector(-1,1);
		
		homographyDestination1 = new PVector(-1,-1);
		homographyDestination2 = new PVector(1,-1);
		homographyDestination3 = new PVector(1,1);
		homographyDestination4 = new PVector(-1,1);
		
		scale = new PVector(1,1);
		offset = new PVector(0,0);		
	} 
	
	
	
}
