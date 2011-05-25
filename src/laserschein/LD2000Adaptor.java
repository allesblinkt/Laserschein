package laserschein;

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import java.util.*;

public class LD2000Adaptor extends AbstractLaserOutput {

	private boolean _myIsInitialized = false;
	private int _myMaxNumberOfPoints = 0;

	private boolean _myIsReady = false;

	private static LD2000Adaptor _myInstance;

	public static int COORDINATE_OFFSET = 8000;

	private LD2000Native.FRAMESTRUCT_EX _myFrame;
	private LD2000Native.PTSTRUCT.ByReference _myFirstPoint;
	private LD2000Native.PTSTRUCT.ByReference[] _myPoints;


	private LD2000Adaptor() {
	}


	public static LD2000Adaptor getInstance() {
		if (_myInstance == null) {
			_myInstance = new LD2000Adaptor();
		}

		return _myInstance;
	}


	public void initialize() {
		if (LD2000Native.IS_SUPPORTED_PLATFORM) {
			Logger.printInfo("LD2000Adaptor.draw", "Initializing");
			_myIsInitialized = true;
			_myInitialize();
		} else {
			Logger.printError("LD2000Adaptor.draw", "Platform not supported or LD2000.DLL not found");
			_myIsInitialized = true;
			_myIsReady = false;
		}
	}


	private void _myInitialize() {
		NativeLongByReference myStatus = new NativeLongByReference();
		NativeLongByReference myVersion = new NativeLongByReference();
		NativeLongByReference myMaxFrames = new NativeLongByReference();
		NativeLongByReference myMaxPoints = new NativeLongByReference();
		NativeLongByReference myMaxBuffer = new NativeLongByReference();
		NativeLongByReference myNumberOfUndos = new NativeLongByReference();

		LD2000Native.BeginSessionEx(myVersion, myMaxFrames, myMaxPoints, myMaxBuffer, myNumberOfUndos, myStatus);

		_myMaxNumberOfPoints = myMaxPoints.getValue().intValue();

		Logger.printInfo("LD2000Adaptor.initialize", "Max number of points: "
				+ _myMaxNumberOfPoints);

		if (myStatus.getValue().intValue() != LD2000Native.LDError.OK.getCode()) {
			Logger.printError("LD2000Adaptor.initialize", "LD2000 failed to initialize with error code: "
					+ myStatus.getValue().intValue());
			_myIsReady = false;

		} else {
			LD2000Native.SetWorkingScanners(new NativeLong(-1));
			LD2000Native.SetWorkingTracks(new NativeLong(1));
			LD2000Native.SetWorkingFrame(new NativeLong(1));

			Logger.printInfo("LD2000Adaptor.initialize", "Initialization was successful");

			_myAllocateMemory();

			_myIsReady = true;
		}

	}


	private void _myAllocateMemory() {
		/* Create frame structure */
		_myFrame = new LD2000Native.FRAMESTRUCT_EX();
		_myFrame.PreferredProjectionZone = new NativeLong(0); // Zone 1

		_myFrame.NumPoints = new NativeLong(_myMaxNumberOfPoints); // Number
																	// of
																	// points
		_myFrame.ScanRate = new NativeLong(0); // Use set in .ini
		_myFrame.VectorFlag = new NativeLong(0); // Point frame
		_myFrame.AnimationCount = new NativeLong(0);

		_myFrame.setAutoRead(false); // we do not expect this to be changed by
										// the DLL

		/* Points */
		// Very uncanny way of allocating contiguous memory. Do not create
		// new Point Objects after that step. Use the allocated!
		_myFirstPoint = new LD2000Native.PTSTRUCT.ByReference();
		_myPoints = (LD2000Native.PTSTRUCT.ByReference[]) _myFirstPoint.toArray(_myMaxNumberOfPoints);

		for (int i = 0; i < _myMaxNumberOfPoints; i++) {
			_myPoints[i].setAutoRead(false); // we do not expect this to be
												// changed by the DLL

			_myPoints[i].XCoord = new NativeLong(0);
			_myPoints[i].YCoord = new NativeLong(0);
			_myPoints[i].ZCoord = new NativeLong(0);
			_myPoints[i].FCoord = new NativeLong(0);

			_myPoints[i].RGBValue = new NativeLong(0);

			_myPoints[i].Status = new NativeLong(0);

			_myPoints[i].setAutoWrite(false); // we do not expect this to be
												// changed by the DLL

		}

		_myFrame.setAutoWrite(false); // we do not expect this to be changed by
										// the DLL

	}


	public void draw(LaserFrame theFrame) {
		/* Initialize DLL if not done */
		if (!_myIsInitialized) {
			initialize();
		}

		if (_myIsInitialized && _myIsReady) {
			final Vector<LaserPoint> myPoints = theFrame.points();

			/* Point count */
			final int myNumberOfPoints = myPoints.size();

			if (myNumberOfPoints > _myMaxNumberOfPoints) {
				Logger.printWarning(
						"LD2000Adaptor.draw", "Too many points to draw. Maximum number is "
								+ _myMaxNumberOfPoints
						);
			}

			final int myDisplayedNumberOfPoints = Math.min(myNumberOfPoints, _myMaxNumberOfPoints);

			/* Create frame structure */
			_myFrame.NumPoints.setValue(myDisplayedNumberOfPoints);
			_myFrame.writeField("NumPoints");

			/* Points */

			for (int i = 0; i < myDisplayedNumberOfPoints; i++) {
				final LaserPoint myPoint = myPoints.get(i);

				_myPoints[i].XCoord.setValue(myPoint.x - COORDINATE_OFFSET);
				_myPoints[i].YCoord.setValue(-myPoint.y + COORDINATE_OFFSET);

				int myStatus = LD2000Native.PT_VECTOR;

				if (myPoint.isCorner) {
					myStatus += LD2000Native.PT_CORNER;
				}

				if (myPoint.isBlanked) {
					myStatus += LD2000Native.PT_TRAVELBLANK;
					_myPoints[i].RGBValue.setValue(0);
				} else {
					_myPoints[i].RGBValue.setValue(myPoint.color);
				}

				_myPoints[i].Status.setValue(myStatus);

				/* Manually update native memory */
				_myPoints[i].writeField("XCoord");
				_myPoints[i].writeField("YCoord");
				_myPoints[i].writeField("Status");
				_myPoints[i].writeField("RGBValue");

			}

			/* Send and update */
			LD2000Native.SetWorkingFrame(new NativeLong(1));
			LD2000Native.WriteFrameFastEx(_myFrame, _myPoints[0]);

			LD2000Native.SetWorkingTracks(new NativeLong(1));
			LD2000Native.DisplayFrame(new NativeLong(1));

			LD2000Native.DisplayUpdate();
		}
	}


	public void destroy() {
		if (_myIsInitialized && _myIsReady) {
			LD2000Native.SetWorkingScanners(new NativeLong(-1)); // Once the
																	// loop

			LD2000Native.SetWorkingTracks(new NativeLong(-1));
			LD2000Native.DisplayFrame(new NativeLong(0));
			LD2000Native.DisplayUpdate();
		}
	}

}
