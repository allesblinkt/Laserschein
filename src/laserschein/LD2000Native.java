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

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import com.sun.jna.ptr.NativeLongByReference;

/**
 * Provides a Java interface for talking with the LD2000.dll.
 * Also abstracts different datatypes of the Pangolin SDK. Caution: Very weird JNA stuff happening here.
 * 
 * @author allesblinkt
 */
public class LD2000Native {

	public static NativeState state = NativeState.UNINITIALIZED;

	
	/**
	 * Static initialization, so we do not crash at runtime
	 */
	static {
		if (Platform.isWindows()) {
			try {
				Native.register("LD2000");
				 state = NativeState.READY;
			} catch (Exception e) {
				state = NativeState.NOT_FOUND;
			} catch (Error e) {
				state = NativeState.NOT_FOUND;
			}
		} else {
			state = NativeState.UNSUPPORTED_PLATFORM;
		}
	}

	public static final int PT_VECTOR = 0; // ordinary point
	public static final int PT_CORNER = 4096; // corner point
	public static final int PT_TRAVELBLANK = 16384; // blanking travel


	public static native NativeLong BeginSession(NativeLong MaxFrames, NativeLong MaxPoints, NativeLong MaxBuffer, NativeLong UndoFrames, NativeLongByReference RETURN_LDStatus);


	public static native NativeLong BeginSessionEx(NativeLongByReference RETURN_Version, NativeLongByReference RETURN_MaxFrames, NativeLongByReference RETURN_MaxPoints, NativeLongByReference RETURN_MaxBuffer, NativeLongByReference RETURN_UndoFrames, NativeLongByReference RETURN_LDStatus);


	public static native NativeLong EndSession();


	public static native NativeLong OpenLDCommWindow();


	public static native NativeLong CloseLDCommWindow();


	public static native NativeLong SetWorkingScanners(NativeLong Scanner);


	public static native NativeLong SetWorkingTracks(NativeLong Track);


	public static native NativeLong SetWorkingFrame(NativeLong FrameNumber);


	public static native NativeLong DisplayFrame(NativeLong Frame);


	public static native NativeLong DisplayUpdate();


	public static native NativeLong WriteFrame(FRAMESTRUCT SUPPLY_FrameStruct, PTSTRUCT.ByReference SUPPLY_PointArray);


	public static native NativeLong WriteFrameEx(FRAMESTRUCT_EX SUPPLY_FrameStruct, PTSTRUCT.ByReference SUPPLY_PointArray);


	public static native NativeLong WriteFrameFastEx(FRAMESTRUCT_EX SUPPLY_FrameStruct, PTSTRUCT.ByReference SUPPLY_PointArray);

	public static class PTSTRUCT extends Structure {

		public NativeLong XCoord;
		public NativeLong YCoord;
		public NativeLong ZCoord;
		public NativeLong FCoord;
		public NativeLong RGBValue;
		public NativeLong X3DCoord;
		public NativeLong Y3DCoord;
		public NativeLong Group;
		public NativeLong Status;


		public PTSTRUCT() {
			super();
		}


		// public void read() { }
		// public void write() { }

		public PTSTRUCT(NativeLong XCoord, NativeLong YCoord, NativeLong ZCoord, NativeLong FCoord, NativeLong RGBValue, NativeLong X3DCoord, NativeLong Y3DCoord, NativeLong Group, NativeLong Status) {
			super();
			this.XCoord = XCoord;
			this.YCoord = YCoord;
			this.ZCoord = ZCoord;
			this.FCoord = FCoord;
			this.RGBValue = RGBValue;
			this.X3DCoord = X3DCoord;
			this.Y3DCoord = Y3DCoord;
			this.Group = Group;
			this.Status = Status;
		}

		public static class ByReference extends PTSTRUCT implements Structure.ByReference {
		};

		public static class ByValue extends PTSTRUCT implements Structure.ByValue {
		};
	}

	
	public static class FRAMESTRUCT extends Structure {

		public NativeLong VOFlag;
		public NativeLong ScanRateMultiplier;
		public NativeLong AbstractFlag;
		public NativeLong NumPoints;
		// / C type : char[24]
		public byte[] FrameNote = new byte[(24)];


		public FRAMESTRUCT() {
			super();
		}


		// public void read() { }
		// public void write() { }

		// / @param FrameNote C type : char[24]
		public FRAMESTRUCT(NativeLong VOFlag, NativeLong ScanRateMultiplier, NativeLong AbstractFlag, NativeLong NumPoints, byte FrameNote[]) {
			super();
			this.VOFlag = VOFlag;
			this.ScanRateMultiplier = ScanRateMultiplier;
			this.AbstractFlag = AbstractFlag;
			this.NumPoints = NumPoints;
			if (FrameNote.length != this.FrameNote.length)
				throw new java.lang.IllegalArgumentException("Wrong array size !");
			this.FrameNote = FrameNote;
		}

		public static class ByReference extends FRAMESTRUCT implements Structure.ByReference {
		};

		public static class ByValue extends FRAMESTRUCT implements Structure.ByValue {
		};
	}


	
	public static class FRAMESTRUCT_EX extends Structure {

		public NativeLong ChangedFlag;
		public NativeLong ThreeDFlag;
		public NativeLong BeamBrushFlag;
		public NativeLong VectorFlag;
		public NativeLong AbstractFlag;
		public NativeLong DMXFlag;
		public NativeLong RasterFlag;
		public NativeLong MaxRenderedFlag;
		public NativeLong SecureFrameFlag;
		public NativeLong Reserved3;
		public NativeLong PreferredPalette;
		public NativeLong PreferredProjectionZone;
		public NativeLong AnimationCount;
		public NativeLong ClipartClass;
		public NativeLong ScanRate;
		public NativeLong NumPoints;

		// / C type : char[24]
		public byte[] FrameNote = new byte[(24)];


		public FRAMESTRUCT_EX() {
			super();
		}

		public static class ByReference extends FRAMESTRUCT_EX implements Structure.ByReference {
		};

		public static class ByValue extends FRAMESTRUCT_EX implements Structure.ByValue {
		};
	}

	
	public enum LDError {
		/*************************************************************************
		 * Error Codes *
		 **************************************************************************/

		OK(0), // Normal Return value

		// ********** These can occur at any time except InitialQMCheck ****
		FIFO_READ_ERROR(-1), // Fifo read zero or non even number of bytes
		QM32_SOFTWARE_ERROR(-2), // QM32 is not responding properly
		DOUBLE_ACCESS_ERROR(-3), // DLL recursively accessed by the same thread

		// ********** These are reported by the loaders including LoadPalette
		X29_LOADED(-11), // File load Successful Return
		LDA_LOADED(-12), // values
		ILDA_LOADED(-13), DEC3_LOADED(-14), LDB_LOADED(-15), LDSECURE_LOADED(
				-16), LDZ20_LOADED(-50), // Zone file with 20 zones loaded
											// (instead of 30 zones)

		// ********* Returned by ConvertToPointFrame **************
		ALREADY_POINT_ORIENTED(-31), // Incase somebody tries to
		// create a point oriented frame
		// from a point oriented frame

		// ******** Currently only returned by CheckSession **************
		NO_SESSION_IN_PROG(-101), // CheckSession return value

		// ******** Returned by LFileRequest and LPaletteRequest *********
		FILE_REQUEST_CANCEL(-201), // File Requestor Cancelled

		// ********* Returned by file functions **************************
		FILE_NOT_FOUND(-401), // Loader errors
		WRONG_FILE_TYPE(-402), DISK_FULL(-403), DISK_WRITE_PROTECTED(-404), FILE_WRITE_PROTECTED(
				-405), MISC_FILE_ERROR(-406), STRING_TOO_LONG(-407), // Supplied
																		// filename
																		// is
																		// over
																		// 128
																		// chars
																		// in
																		// length

		// *********** Returned by frame commands such as DisplayFrame ***
		FRAME_OUT_OF_RANGE(-501), // Misc programming or config errors

		// *********** Returned by point commands such as writepoint *****
		POINT_OUT_OF_RANGE(-502),

		// *********** Returned by show control commands *****************
		TDC_NOT_FOUND(-511), TRANSITION_NOT_FOUND(-512), EFFECT_NOT_FOUND(-513), SCENE_NOT_FOUND(
				-514), MODULE_NOT_FOUND(-515), SHOW_NOT_FOUND(-516), STRUCTURE_NOT_FOUND(
				-519),

		// Once the element has been deleted, this will be returned
		EFFECT_DELETED(-530), SCENE_DELETED(-531), MODULE_DELETED(-532), SHOW_DELETED(
				-533), STRUCTURE_DELETED(-539),

		// If you try to delete something which is inuse one of these will be
		// returned
		EFFECT_INUSE(-540), SCENE_INUSE(-541), MODULE_INUSE(-542), SHOW_INUSE(
				-543), STRUCTURE_INUSE(-549),

		// *********** These should be rare indeed ***********************
		NO_IBM_MEMORY(-601), // No free IBM Memory
		CANT_OPEN_WINDOW(-602), // Can't open Window (Debug, FileRequest)

		// *********** Alot of commands can return this one **************
		NO_QM32_MEMORY(-702), // No free QM32 Memory

		// *********** Ran out of memory while trying to load a file *****
		FILE_TOO_LARGE(-703), // No free QM32 Memory to load frames

		// ********** Several unimplemented DLL commands return this *****
		NOT_IMPLEMENTED(-801), // DLL Command not implemented

		// *** This indicates a timeout during a long communication with the
		// QM32
		// *** This only occurs during file loading and saving and ActiveArray
		// functions
		QM32_ERROR(-901), // QM32 Communication error

		// ***** These should only be returned by InitialQMCheck *********
		QM32_NOT_PRESENT(-1001), // QM32 is not present (like it sounds)
		QM32_ADDRESS_CONFLICT(-1002), // QM32 is not responding properly
		QM32_INITIALIZATION_ERROR(-1003); // Same as above, but even weirder

		private int _myCode;


		private LDError(int theCode) {
			_myCode = theCode;
		}


		public int getCode() {
			return _myCode;
		}
	}

}
