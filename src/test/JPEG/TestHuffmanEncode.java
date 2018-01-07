package test.JPEG;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import main.JPEG.HuffmanDecode;
import main.JPEG.HuffmanEncode;

public class TestHuffmanEncode {
	
	@Test
	public void testGetEncodedDCACValue() throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Method method = HuffmanEncode.class.getDeclaredMethod("getEncodedDCACValue", int.class);
		method.setAccessible(true);
		HuffmanEncode huffmancode = new HuffmanEncode(null, null, null);// "", 0, 63);

		assertEquals("0", (method.invoke(huffmancode, -1)));
		assertEquals("1", (method.invoke(huffmancode, 1)));
		assertEquals("00", (method.invoke(huffmancode, -3)));
		assertEquals("01", (method.invoke(huffmancode, -2)));
		assertEquals("10", (method.invoke(huffmancode, 2)));
		assertEquals("11", (method.invoke(huffmancode, 3)));
		assertEquals("000", (method.invoke(huffmancode, -7)));
		assertEquals("011", (method.invoke(huffmancode, -4)));
		assertEquals("100", (method.invoke(huffmancode, 4)));
		assertEquals("111", (method.invoke(huffmancode, 7)));
		assertEquals("0000", (method.invoke(huffmancode, -15)));
		assertEquals("0111", (method.invoke(huffmancode, -8)));
		assertEquals("1000", (method.invoke(huffmancode, 8)));
		assertEquals("1111", (method.invoke(huffmancode, 15)));
		assertEquals("00000", (method.invoke(huffmancode, -31)));
		assertEquals("01111", (method.invoke(huffmancode, -16)));
		assertEquals("10000", (method.invoke(huffmancode, 16)));
		assertEquals("11111", (method.invoke(huffmancode, 31)));
		assertEquals("000000", (method.invoke(huffmancode, -63)));
		assertEquals("011111", (method.invoke(huffmancode, -32)));
		assertEquals("100000", (method.invoke(huffmancode, 32)));
		assertEquals("111111", (method.invoke(huffmancode, 63)));
		assertEquals("0000000", (method.invoke(huffmancode, -127)));
		assertEquals("0111111", (method.invoke(huffmancode, -64)));
		assertEquals("1000000", (method.invoke(huffmancode, 64)));
		assertEquals("1111111", (method.invoke(huffmancode, 127)));
		assertEquals("00000000", (method.invoke(huffmancode, -255)));
		assertEquals("01111111", (method.invoke(huffmancode, -128)));
		assertEquals("10000000", (method.invoke(huffmancode, 128)));
		assertEquals("11111111", (method.invoke(huffmancode, 255)));
		assertEquals("000000000", (method.invoke(huffmancode, -511)));
		assertEquals("011111111", (method.invoke(huffmancode, -256)));
		assertEquals("100000000", (method.invoke(huffmancode, 256)));
		assertEquals("111111111", (method.invoke(huffmancode, 511)));
		assertEquals("0000000000", (method.invoke(huffmancode, -1023)));
		assertEquals("0111111111", (method.invoke(huffmancode, -512)));
		assertEquals("1000000000", (method.invoke(huffmancode, 512)));
		assertEquals("1111111111", (method.invoke(huffmancode, 1023)));
		assertEquals("00000000000", (method.invoke(huffmancode, -2047)));
		assertEquals("01111111111", (method.invoke(huffmancode, -1024)));
		assertEquals("10000000000", (method.invoke(huffmancode, 1024)));
		assertEquals("11111111111", (method.invoke(huffmancode, 2047)));
	}

}
