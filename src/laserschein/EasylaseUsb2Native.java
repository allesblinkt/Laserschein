/**
 * 
 */
package laserschein;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.IntByReference;

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
	 * Output is stopped. USB connection will be left open.
	 * @param CardNumber
	 * @return true -> Okay, false -> USB error.
	 */
	public static native boolean EasyLaseStop(IntByReference CardNumber);
	
	/**
	 * Returns the status of the on board framebuffers.
	 * @param CardNumber
	 * @return 0 = USB error, 1 = Ready (a new frame can be received), 2 = Busy (all buffers full, incoming frames will be discarded)
	 */
	public static native int EasyLaseGetStatus(IntByReference CardNumber);
	
	/**
	 * Returns the last status of the USB driver
	 * @param CardNumber
	 * @return 0 = okay, <>0 = error
	 */
	public static native int EasyLaseGetLastError(IntByReference CardNumber);
	
	/**
	 * Close all USB connections to EasyLase cards.
	 * Call this when closing the application!
	 * @return true -> Okay, false -> USB error
	 */
	public static native boolean EasyLaseClose();
}
