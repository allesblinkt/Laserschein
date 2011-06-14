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
		
		System.out.println(EasylaseUsb2Native.EasyLaseGetCardNum());
		
		System.out.println(EasylaseUsb2Native.EasyLaseGetStatus(iref));
		
		System.out.println(EasylaseUsb2Native.EasyLaseClose());
		
	}

}
