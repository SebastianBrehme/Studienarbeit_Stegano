package main.Util;

public class BitConverter {

	public static String convertToBitString(int number) {
		String result = "";
		if(number == 0) {
			return "0";
		}
		while(number > 0) {
			int modulo = number % 2;
			result = String.valueOf(modulo) + result;
			number = number /2;
		}
		return result;
	}
	
	public static int getLowerBits(int number) {
		return number % 16;
	}
	
	public static int getHigherBits(int number) {
		return number / 16;
	}
}
