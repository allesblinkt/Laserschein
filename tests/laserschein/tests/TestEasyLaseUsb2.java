package laserschein.tests;

import laserschein.EasylaseUsb2Native;

public class TestEasyLaseUsb2 {

	/**
	 * Temporary class for testing EasyLase -box.
	 * @author MPallas
	 */
	public static void main(String[] args) {
		
		System.out.println(EasylaseUsb2Native.EasyLaseGetCardNum());
		System.out.println(EasylaseUsb2Native.EasyLaseClose());
		System.out.println("System OK");
		
	}

}
