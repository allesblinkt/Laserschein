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

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import java.util.*;


/**
 * Uses the Pangolin SDK to control a Pangolin QM2000 or QM2000.net via Laserschein.
 * Works on windows for now.
 * 
 * @author allesblinkt
 *
 */
public class LD2000Adaptor extends AbstractLaserOutput {

	private int _myMaxNumberOfPoints = 0;

	public static int COORDINATE_OFFSET = 8000;

	private LD2000Native.FRAMESTRUCT_EX _myFrame;
	private LD2000Native.PTSTRUCT.ByReference _myFirstPoint;
	private LD2000Native.PTSTRUCT.ByReference[] _myPoints;



	public LD2000Adaptor() {
		super();
	}



	@Override
	public void initialize() {
		
		if (LD2000Native.state == NativeState.READY) {
			Logger.printInfo("LD2000Adaptor.initialize", "Initializing");
			_myInitialize();
		}
		
		if (LD2000Native.state == NativeState.UNSUPPORTED_PLATFORM) {
			Logger.printError("LD2000Adaptor.initialize", "Platform not supported. Only Windows >XP works for now.");
			_myState = OutputState.UNSUPPORTED_PLATFORM;
		}
		
		if (LD2000Native.state == NativeState.NOT_FOUND) {
			Logger.printError("LD2000Adaptor.initialize", "The LD2000.dll could not be loaded.");
			_myState = OutputState.LIBRARY_ERROR;
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

			_myState = OutputState.NO_DEVICES_FOUND;


		} else {
			LD2000Native.SetWorkingScanners(new NativeLong(-1));
			LD2000Native.SetWorkingTracks(new NativeLong(1));
			LD2000Native.SetWorkingFrame(new NativeLong(1));

			Logger.printInfo("LD2000Adaptor.initialize", "Initialization was successful");

			_myAllocateMemory();

			_myState = OutputState.READY;
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

	@Override
	public void draw(final LaserFrame theFrame) {

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
				int myColor = Laser3D.rgbToInt(myPoint.r, myPoint.g, myPoint.b);
				_myPoints[i].RGBValue.setValue(myColor);
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

	
	@Override
	public void destroy() {
		LD2000Native.SetWorkingScanners(new NativeLong(-1)); 
		LD2000Native.SetWorkingTracks(new NativeLong(-1));
		LD2000Native.DisplayFrame(new NativeLong(0));
		LD2000Native.DisplayUpdate();
	}



	@Override
	public int getMaximumNumberOfPoints() {
		return _myMaxNumberOfPoints;
	}



	@Override
	public int getScanSpeed() {
		// TODO implement
		return 0;
	}



	@Override
	public void setScanSpeed(int theSpeed) {
		Logger.printWarning(
				"LD2000Adaptor.setScanSpeed", "Setting the scan speed is not implemented yet. But you" +
						"can change it in LaserShowDesigner 2000 and save it to the LD2000.ini ..."
				);
		
	}



	@Override
	public int getMinumumScanSpeed() {
		// TODO: implement
		return 0;
	}



	@Override
	public int getMaximumScanSpeed() {
		// TODO: implement
		return 0;
	}

}
