package main.Util;

import java.util.ArrayList;
import java.util.List;

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
	
	public static String invertBitString(String bits)
	{
		String newBits = "";
		for (int i = 0; i < bits.length(); i++)
		{
			if (bits.charAt(i) == '1')
			{
				newBits = newBits + "0";
			}
			else
			{
				newBits = newBits + "1";
			}
		}
		return newBits;
	}
	
	public static List<Integer> convertBitStringToIntegerList(String bits)
	{
		List<Integer> values = new ArrayList<>();
		while (bits.length() > 7)
		{
			String x = bits.substring(0, 8);
			values.add(convertBitsToInteger(x));
			bits = bits.substring(8, bits.length());
		}
		
		return values;
	}
	
	public static int convertBitsToInteger(String bits)
	{
		int sum = 0;
		for (int i = 0; i < 8; i++)
		{
			sum += Math.pow(2, i) * ((int) bits.charAt(bits.length()-1-i) - 48);
		}
		return sum;
	}
}
