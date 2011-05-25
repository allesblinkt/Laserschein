package laserschein;

public abstract class AbstractLaserOutput {

	/**
	 * Initializes the driver and other stuff.
	 */
	public abstract void initialize();

	
	/**
	 * This gets called for every frame to be displayed.
	 * @param theFrame to be displayed
	 */
	public abstract void draw(final LaserFrame theFrame);

	
	/**
	 * This gets called when the processing application shuts down.
	 * A good place to unload DLLs and clean up
	 */
	public abstract void destroy();
}
