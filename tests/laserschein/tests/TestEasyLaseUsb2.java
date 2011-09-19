package laserschein.tests;

import com.sun.jna.ptr.IntByReference;
import laserschein.EasylaseUsb2Native;

public class TestEasyLaseUsb2 {

	/**
	 * Temporary class for testing EasyLase -box.
	 * @author MPallas
	 */
	public static void main(String[] args) {
		
		
		
		int cardNumber = -1;
		
		EasylaseUsb2Native.Point.ByReference x = new EasylaseUsb2Native.Point.ByReference();
		
		x.x = 10;
		x.y = 10;
		x.r = (byte)255;
		x.g = 0;
		x.b = 0;
		x.i = (byte)250;
		
		
		System.out.println( "Number of cards: " + EasylaseUsb2Native.EasyLaseGetCardNum() );
		cardNumber = 0;
		System.out.println("Card number :" + cardNumber);
		
		System.out.println(EasylaseUsb2Native.EasyLaseWriteFrame(new IntByReference(cardNumber), x, 8, (short)1000));
		
		System.out.println(EasylaseUsb2Native.EasyLaseGetStatus(new IntByReference(cardNumber)));
		
		System.out.println(EasylaseUsb2Native.EasyLaseStop(new IntByReference(cardNumber)));

		System.out.println(EasylaseUsb2Native.EasyLaseClose());
		
	}

}
