package laserschein.tests;

import com.sun.jna.ptr.IntByReference;
import laserschein.EasylaseUsb2Native;

public class TestEasyLaseUsb2 {

	/**
	 * Temporary class for testing EasyLase -box.
	 * @author MPallas
	 */
	public static void main(String[] args) {
		
		IntByReference iref = new IntByReference();
		iref.setValue(0);
		
		EasylaseUsb2Native.PTSTRUCT.ByReference x = new EasylaseUsb2Native.PTSTRUCT.ByReference();
		
		x.x = 10;
		x.y = 10;
		x.r = 255;
		x.g = 0;
		x.b = 0;
		x.i = 250;
		
		System.out.println(EasylaseUsb2Native.EasyLaseGetCardNum());
		
		System.out.println(EasylaseUsb2Native.EasyLaseWriteFrame(iref, x, 8, (short)1000));
		
		System.out.println(EasylaseUsb2Native.EasyLaseGetStatus(iref));
		
		System.out.println(EasylaseUsb2Native.EasyLaseClose());
		
	}

}
