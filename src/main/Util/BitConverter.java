package main.Util;

public class BitConverter {
	
	public static String convertToBitString(int number, int length) {
		String result = doConvertion(number);
		return addUpToLenght(result, length);
	}

	public static String convertToBitString(int number) {
		String result = doConvertion(number);		
		return addUpToLenght(result,8);
	}
	
	private static String doConvertion(int number) {
		if(number == 0) {
			return "0";
		}
		String result = "";
		while(number > 0) {
			int modulo = number % 2;
			result = String.valueOf(modulo) + result;
			number = number /2;
		}
		return result;
	}
	
	private static String addUpToLenght(String bitString, int length) {
		while(bitString.length() < length) {
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
