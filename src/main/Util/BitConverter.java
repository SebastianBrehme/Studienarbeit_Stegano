package main.Util;

public class BitConverter {

	public static String convertToBitString(int number) {
		String result = "";
		if(number == 0) {
			return "00000000";
		}
		while(number > 0) {
			int modulo = number % 2;
			result = String.valueOf(modulo) + result;
			number = number /2;
		}
		return addUpTo8Bit(result);
	}
	
	private static String addUpTo8Bit(String bitString) {
		while(bitString.length() < 8) {
			bitString = "0"+bitString;
		}
		return bitString;
	}
	
	public static int getLowerBits(int number) {
		return number % 16;
	}
	
	public static int getHigherBits(int number) {
		return number / 16;
	}
}
