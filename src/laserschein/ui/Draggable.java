package laserschein.ui;


public interface Draggable {
	   public float x();   
	   public float y();

	   public void x(float theX);
	   public void y(float theY);

	   public boolean mouseOver(float theX, float theY);   
}