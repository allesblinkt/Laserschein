package laserschein;

import processing.xml.XMLElement;


public interface TweakableSettings {
	public String xmlNamespace();
	public XMLElement toXML();
	public void loadFromXml(final XMLElement theXML);
}

