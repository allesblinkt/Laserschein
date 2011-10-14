package laserschein.candidates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

import laserschein.LaserFrame;
import laserschein.LaserPoint;


public class LasernetzNetworkManager {
	
	private void constructHeader(final LaserFrame theFrame) {
		
	}
	

	private void constructData(final LaserFrame theFrame) {
		
		Vector<LaserPoint> myPoints = theFrame.points();
		
		final int BYTES_PER_POINT = 2 + 2 + 1 + 1 + 1 + 1;
		final int myLength = myPoints.size() * BYTES_PER_POINT;
		
		final ByteBuffer myBuffer = ByteBuffer.allocate(myLength);
		myBuffer.order(ByteOrder.BIG_ENDIAN);
		
		for(int i=0; i<myPoints.size(); i++){
			final LaserPoint myPoint = myPoints.get(i);
			
			// TODO: sanitize output values
			
			myBuffer.putShort( (short) myPoint.x );
			myBuffer.putShort( (short) myPoint.y );
			myBuffer.put(  (byte)  myPoint.r );
			myBuffer.put(  (byte)  myPoint.g );
			myBuffer.put(  (byte)  myPoint.b );
			myBuffer.put(  (byte)  0         );

		}
	}
}
