package laserschein.tests;


public class TestBinary {
	public static void main(String[] args) {
		int foo = 254;
		char foochar = (char)foo;
		byte foobyte = (byte)foo;


		System.out.println(foo);
		System.out.println((int)foochar + " " + Integer.toBinaryString(foochar));
		System.out.println((int)foobyte + " " + Integer.toBinaryString(foobyte));


		int bar = 65534;
		short barshort = (short)bar;
		//byte foobyte = (byte)foo;



		System.out.println(bar + "  " + Integer.toBinaryString(bar));
		System.out.println((int)barshort + "  " + Integer.toBinaryString(barshort));
		System.out.println(foobyte);


	}
}
