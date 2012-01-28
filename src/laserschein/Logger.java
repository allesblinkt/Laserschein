/**
 *  
 *  Laserschein. interactive ILDA output from processing and java
 *
 *  2012 by Benjamin Maus
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
 * @author Andreas Schlegel
 *
 */
package laserschein;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sun.reflect.Reflection;


/**
 * A simple logger class. Derived from the Logger class in Andreas Schlegels NetP5
 * 
 * @author Benjamin Maus
 *
 */
public class Logger {

	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("[yyyy.MM.dd HH:mm:ss]");
	private static boolean[] flags = new boolean[] { true, true, true, true, false };
	//private static String STRIP = "com.allesblinkt.psa.";

	/**
	 * Determines if a certain log level should be displayed
	 * 
	 * @param theLevel
	 * @param theFlag
	 */
	public static void set(LogLevel theLevel, boolean theFlag) {
		flags[theLevel.ordinal()] = theFlag;
	}
	
	
	/**
	 * Switches logging on or off for all levels
	 * 
	 * @param theFlag
	 */
	public static void setAll(boolean theFlag) {
		for(int i = 0; i < flags.length; i++) {
			flags[i] = theFlag;
		}
	}


	public static void printError(String theLocation, String theMsg) {
		if (flags[LogLevel.ERROR.ordinal()] == true) {
			println(formattedDateTime() + " ERROR @ " + theLocation + " | " + theMsg);
		}
	}


	public static void printProcess(String theLocation, String theMsg) {
		if (flags[LogLevel.PROCESS.ordinal()] == true) {
			println(formattedDateTime() + " PROCESS @ " + theLocation + " | " + theMsg);
		}
	}


	public static void printWarning(String theLocation, String theMsg) {
		if (flags[LogLevel.WARNING.ordinal()] == true) {
			println(formattedDateTime() + " WARNING @ " + theLocation + " | " + theMsg);
		}
	}


	public static void printInfo(String theLocation, String theMsg) {
		if (flags[LogLevel.INFO.ordinal()] == true) {
			println(formattedDateTime() + " INFO @ " + theLocation + " | " + theMsg);
		}
	}


	public static void printDebug(String theLocation, String theMsg) {
		if (flags[LogLevel.DEBUG.ordinal()] == true) {
			println(formattedDateTime() + " DEBUG @ " + theLocation + " | " + theMsg);
		}
	}



	public static void printError(String theMsg) {
		printError(callerLocation(), theMsg);
	}


	public static void printProcess(String theMsg) {
		printProcess(callerLocation(), theMsg);
	}


	public static void printWarning(String theMsg) {
		printWarning(callerLocation(), theMsg);
	}


	public static void printInfo(String theMsg) {
		printInfo(callerLocation(), theMsg);	
	}


	public static void printDebug(String theMsg) {
		printDebug(callerLocation(), theMsg);
	}


	public static void print(String theMsg) {
		System.out.print(theMsg);
	}


	public static void println(String theMsg) {
		System.out.println(theMsg);
	}


	public static String callerLocation() {
		String myString  = Reflection.getCallerClass(3).getCanonicalName() + "/" + new Throwable().fillInStackTrace().getStackTrace()[2].getMethodName();
		//return myString.replaceFirst(STRIP, "");
		return myString;
	}


	public static void printBytes(byte[] theByteArray) {
		for (int i = 0; i < theByteArray.length; i++) {
			print(theByteArray[i] + " (" + (char) theByteArray[i] + ")  ");
			
			if ((i + 1) % 4 == 0) {
				print("\n");
			}
		}
		
		print("\n");
	}


	public static String formattedDateTime() {
		Calendar myCalendar = Calendar.getInstance();
		return dateTimeFormat.format(myCalendar.getTime());
		
	}



	public enum LogLevel {
		ERROR, WARNING, PROCESS, INFO, DEBUG;
	}

}
