package test.JPEG;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import main.JPEG.DCTMatrix;
import main.JPEG.HuffmanDecode;
import main.JPEG.ImageData;
import main.JPEG.Matrix;

public class TestHuffmanDecode {

	@Test
	public void testGetDecodedDCACValue() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		Method method = HuffmanDecode.class.getDeclaredMethod("getDecodedDCACValue", String.class, int.class);
		method.setAccessible(true);
		HuffmanDecode huffmancode = new HuffmanDecode();

		assertEquals(0, (int) (method.invoke(huffmancode, "", 0)));
		assertEquals(-1, (int) (method.invoke(huffmancode, "0", 1)));
		assertEquals(1, (int) (method.invoke(huffmancode, "1", 1)));
		assertEquals(-3, (int) (method.invoke(huffmancode, "00", 2)));
		assertEquals(-2, (int) (method.invoke(huffmancode, "01", 2)));
		assertEquals(2, (int) (method.invoke(huffmancode, "10", 2)));
		assertEquals(3, (int) (method.invoke(huffmancode, "11", 2)));
		assertEquals(-7, (int) (method.invoke(huffmancode, "000", 3)));
		assertEquals(-4, (int) (method.invoke(huffmancode, "011", 3)));
		assertEquals(4, (int) (method.invoke(huffmancode, "100", 3)));
		assertEquals(7, (int) (method.invoke(huffmancode, "111", 3)));
		assertEquals(-15, (int) (method.invoke(huffmancode, "0000", 4)));
		assertEquals(-8, (int) (method.invoke(huffmancode, "0111", 4)));
		assertEquals(8, (int) (method.invoke(huffmancode, "1000", 4)));
		assertEquals(15, (int) (method.invoke(huffmancode, "1111", 4)));
		assertEquals(-31, (int) (method.invoke(huffmancode, "00000", 5)));
		assertEquals(-16, (int) (method.invoke(huffmancode, "01111", 5)));
		assertEquals(16, (int) (method.invoke(huffmancode, "10000", 5)));
		assertEquals(31, (int) (method.invoke(huffmancode, "11111", 5)));
		assertEquals(-63, (int) (method.invoke(huffmancode, "000000", 6)));
		assertEquals(-32, (int) (method.invoke(huffmancode, "011111", 6)));
		assertEquals(32, (int) (method.invoke(huffmancode, "100000", 6)));
		assertEquals(63, (int) (method.invoke(huffmancode, "111111", 6)));
		assertEquals(-127, (int) (method.invoke(huffmancode, "0000000", 7)));
		assertEquals(-64, (int) (method.invoke(huffmancode, "0111111", 7)));
		assertEquals(64, (int) (method.invoke(huffmancode, "1000000", 7)));
		assertEquals(127, (int) (method.invoke(huffmancode, "1111111", 7)));
		assertEquals(-255, (int) (method.invoke(huffmancode, "00000000", 8)));
		assertEquals(-128, (int) (method.invoke(huffmancode, "01111111", 8)));
		assertEquals(128, (int) (method.invoke(huffmancode, "10000000", 8)));
		assertEquals(255, (int) (method.invoke(huffmancode, "11111111", 8)));
		assertEquals(-511, (int) (method.invoke(huffmancode, "000000000", 9)));
		assertEquals(-256, (int) (method.invoke(huffmancode, "011111111", 9)));
		assertEquals(256, (int) (method.invoke(huffmancode, "100000000", 9)));
		assertEquals(511, (int) (method.invoke(huffmancode, "111111111", 9)));
		assertEquals(-1023, (int) (method.invoke(huffmancode, "0000000000", 10)));
		assertEquals(-512, (int) (method.invoke(huffmancode, "0111111111", 10)));
		assertEquals(512, (int) (method.invoke(huffmancode, "1000000000", 10)));
		assertEquals(1023, (int) (method.invoke(huffmancode, "1111111111", 10)));
		assertEquals(-2047, (int) (method.invoke(huffmancode, "00000000000", 11)));
		assertEquals(-1024, (int) (method.invoke(huffmancode, "01111111111", 11)));
		assertEquals(1024, (int) (method.invoke(huffmancode, "10000000000", 11)));
		assertEquals(2047, (int) (method.invoke(huffmancode, "11111111111", 11)));
	}
	
	@Test
	public void testGetNextHuffmanDecodedValue() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method  = HuffmanDecode.class.getDeclaredMethod("getNextHuffmanDecodedValue", String[][].class);
		method.setAccessible(true);
		
		HuffmanDecode huffmancode = new HuffmanDecode();//null, null, null);
		huffmancode.data = "00101111";
		String[][] table = {{"00","0"},{"101","5"},{"111","18"}};
		
		assertEquals("0", (String)method.invoke(huffmancode, (Object)table));
		assertEquals("5", (String)method.invoke(huffmancode, (Object)table));
		assertEquals("18", (String)method.invoke(huffmancode,(Object) table));
	}
	
	@Test
	public void testDecodeDC() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method  = HuffmanDecode.class.getDeclaredMethod("decodeDC", String[][].class);
		method.setAccessible(true);		
		
		HuffmanDecode huffmancode = new HuffmanDecode();
		huffmancode.data = "00"+"101"+"1110"+"111"+"01111111111";
		String[][] table = {{"00","0"},{"101","4"},{"111","11"}};
		
		assertEquals(0, (int)method.invoke(huffmancode, (Object)table));
		assertEquals(14, (int)method.invoke(huffmancode, (Object)table));
		assertEquals(-1024, (int)method.invoke(huffmancode, (Object)table));

	}
	
	@Test
	public void testDeocdeAC() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method  = HuffmanDecode.class.getDeclaredMethod("decodeAC", String[][].class, DCTMatrix.class);
		method.setAccessible(true);
		HuffmanDecode huffmancode = new HuffmanDecode();
		huffmancode.data="101"+"101"+"11110"+"1110"+"1101"+"00";
		String[][] table = {{"00","0"},{"101","3"},{"1110","100"},{"11110","240"}};
		
		int[][] expect = new int[8][8];
		expect[0][1] = 5;
		expect[3][3] = 13;
		
		DCTMatrix actual = new DCTMatrix(Matrix.LUMINANCE, 8, 8);
		
		actual = (DCTMatrix) method.invoke(huffmancode, table, actual);
		assertArrayEquals(expect, actual.getMatrix());
	}
}
