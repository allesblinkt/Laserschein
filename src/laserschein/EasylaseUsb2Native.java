/**
 * 
 */
package laserschein;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

/**
 * Provides a Java interface for talking with the jmlaser.dll
 * 
 * @author MPallas
 */
public class EasylaseUsb2Native {
	
	
	public static NativeState state = NativeState.UNINITIALIZED;
	
	
	/**
	 * Static initialization, so we do not crash at runtime
	 */
	static {
		if(Platform.isWindows()) { 
			try { 
				Native.register("jmlaser");
				state = NativeState.READY;
			} 
			catch (Exception e) { state = NativeState.NOT_FOUND; } 
			catch (Error e) { state = NativeState.NOT_FOUND; }
		}
		else {
			state = NativeState.UNSUPPORTED_PLATFORM;
		}
	}
	
	
	/**
	 * Will return the number of connected cards and opens the devices.
	 * Must be used before all other functions!
	 * @return 0 = no cards, 1-20 number of cards connected.
	 */
	public static native int EasyLaseGetCardNum();
	
	
	/**
	 * Send a frame from @ Datapointer to card number (CardNumber).
	 * Maximum framesize is 16.000 points or 128 Kbytes. Value in Bytenumber is number of
	 * points in the frame multiplied with 8
	 * Pointspeed is outputspeed in pps and should be between 1000 and 65.535.
	 * @param CardNumber  0 – number of cards
	 * @param Datapointer The frame
	 * @param Bytenumber 0 – 1FFFFh 
	 * @param Pointspeed 1000 - FFFFh 
	 * @return true -> Okay, false -> USB error
	 */
	public static native boolean EasyLaseWriteFrame(IntByReference CardNumber, Point.ByReference Datapointer, int Bytenumber, short Pointspeed);
	
	
	/**
	 * Send a frame like EasyLaseWriteFrame, but without automatic frame repeat, if there is no
	 * new frame following the actual one. Output stops at the last point until new frame comes in.
	 * @param CardNumber 0 – number of cards
	 * @param Datapointer The frame
	 * @param Bytenumber 0 – 1FFFFh
	 * @param Pointspeed 1000 - FFFFh
	 * @param RepNum 0
	 * @return true -> Okay, false -> USB error
	 */
	public static native boolean EasyLaseWriteFrameNR(IntByReference CardNumber, Point.ByReference Datapointer, int Bytenumber, short Pointspeed, short RepNum);
	
	
	/**
	 * Output is stopped. USB connection will be left open.
	 * @param CardNumber
	 * @return true -> Okay, false -> USB error.
	 */
	public static native boolean EasyLaseStop(IntByReference CardNumber);
	
	
	/**
	 * Send 16 Bit TTL-value to card number (CardNumber).
	 * Only the lower 8 Bits will be output by the device.
	 * @param CardNumber
	 * @param TTLValue
	 * @return  true -> Okay, false -> USB error
	 */
	public static native boolean EasyLaseWriteTTL(IntByReference CardNumber, short TTLValue); 
	// Short equivalent for a word-type ??
	
	
	/**
	 * Returns the last status of the USB driver
	 * @param CardNumber
	 * @return 0 = okay, <>0 = error
	 */
	public static native int EasyLaseGetLastError(IntByReference CardNumber);
	
	
	/**
	 * Returns the status of the on board framebuffers.
	 * @param CardNumber
	 * @return 0 = USB error, 1 = Ready (a new frame can be received), 2 = Busy (all buffers full, incoming frames will be discarded)
	 */
	public static native int EasyLaseGetStatus(IntByReference CardNumber);
	
	
	/**
	 * Close all USB connections to EasyLase cards.
	 * Call this when closing the application!
	 * @return true -> Okay, false -> USB error
	 */
	public static native boolean EasyLaseClose();
	
	
	
	public static class Point extends Structure {
		
		public short x; // 2 bytes, value: 0-4095 (x-coordinate)
		public short y; // 2 bytes, value: 0-4095 (y-coordinate)
		public byte r;  // 1 byte, value: 0-255 (red)
		public byte g;  // 1 byte, value: 0-255 (green)
		public byte b;  // 1 byte, value: 0-255 (blue)
		public byte i;  // 1 byte, value: 0-255 (intensity)
		
		public Point() {
			super();
		}
		
		
		public static class ByReference extends Point implements Structure.ByReference {
		};
		
		
		public static class ByValue extends Point implements Structure.ByValue {
		};
	}
	
	
}
