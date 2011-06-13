/**
 * 
 */
package laserschein;

import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * Provides a Java interface for talking with the jmlaser.dll
 * 
 * @author MPallas
 */
public class EasylaseUsb2Native {
	
	public static boolean IS_SUPPORTED_PLATFORM = false;
	
	static {
		if(Platform.isWindows()) { 
			try { 
				Native.register("jmlaser");
				IS_SUPPORTED_PLATFORM = true;
			} 
			catch (Exception e) { IS_SUPPORTED_PLATFORM = false; } 
			catch (Error e) { IS_SUPPORTED_PLATFORM = false; }
		}
		else {
			IS_SUPPORTED_PLATFORM = false;
		}
	}
	
	/**
	 * Will return the number of connected cards and opens the devices.
	 * Must be used before all other functions!
	 * @return 0 = no cards, 1-20 number of cards connected.
	 */
	public static native int EasyLaseGetCardNum();
	
	/**
	 * Close all USB connections to EasyLase cards.
	 * Call this when closing the application!
	 * @return true -> Okay, false -> USB error
	 */
	public static native boolean EasyLaseClose();
	
	
}
