package test.Util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import main.Util.BitConverter;

public class TestBitConverter {

	@Test
	public void testConvertToBitString() {
		assertEquals("00000000", BitConverter.convertToBitString(0));
		assertEquals("00000001", BitConverter.convertToBitString(1));
		assertEquals("00001010", BitConverter.convertToBitString(10));
		assertEquals("00110010", BitConverter.convertToBitString(50));
		assertEquals("01100100", BitConverter.convertToBitString(100));
		assertEquals("10010110", BitConverter.convertToBitString(150));
		assertEquals("11001000", BitConverter.convertToBitString(200));
		assertEquals("11100110", BitConverter.convertToBitString(230));
		assertEquals("11111111", BitConverter.convertToBitString(255));
	}
	
	@Test
	public void testConvertSignedToBitString() {
		assertEquals("00000000", BitConverter.convertSignedToBitString(0));
		assertEquals("00000001", BitConverter.convertSignedToBitString(1));
		assertEquals("00001010", BitConverter.convertSignedToBitString(10));
		assertEquals("00110010", BitConverter.convertSignedToBitString(50));
		assertEquals("01100100", BitConverter.convertSignedToBitString(100));
		
		assertEquals("10010110", BitConverter.convertSignedToBitString(-106));
		assertEquals("11001000", BitConverter.convertSignedToBitString(-56));
		assertEquals("11100110", BitConverter.convertSignedToBitString(-26));
		assertEquals("11111111", BitConverter.convertSignedToBitString(-1));
	}
	
	@Test
	public void testConvertToBitStringWithLength() {
		assertEquals("0", BitConverter.convertToBitString(0,1));
		assertEquals("00001", BitConverter.convertToBitString(1,5));
		assertEquals("0000001010", BitConverter.convertToBitString(10,10));
		assertEquals("00110010", BitConverter.convertToBitString(50,8));
		assertEquals("1100100", BitConverter.convertToBitString(100,5));
		assertEquals("010010110", BitConverter.convertToBitString(150,9));
	}
	
	@Test
	public void testgetLowerBits() {
		assertEquals(1, BitConverter.getLowerBits(0x11));
		assertEquals(2, BitConverter.getLowerBits(0x12));
		assertEquals(9, BitConverter.getLowerBits(0xF9));
		assertEquals(7, BitConverter.getLowerBits(0xF7));
		assertEquals(11, BitConverter.getLowerBits(0x1B));
		assertEquals(15, BitConverter.getLowerBits(0x1F));
	}
	
	@Test
	public void testgetHigherBits() {
		assertEquals(1, BitConverter.getHigherBits(0x14));
		assertEquals(2, BitConverter.getHigherBits(0x23));
		assertEquals(15, BitConverter.getHigherBits(0xF9));
		assertEquals(14, BitConverter.getHigherBits(0xE7));
		assertEquals(8, BitConverter.getHigherBits(0x8B));
		assertEquals(7, BitConverter.getHigherBits(0x7F));
	}
	
	@Test
	public void testinvertBitString() {
		assertEquals("1", BitConverter.invertBitString("0"));
		assertEquals("0", BitConverter.invertBitString("1"));
		assertEquals("101011", BitConverter.invertBitString("010100"));
		assertEquals("00110101", BitConverter.invertBitString("11001010"));
		assertEquals("100010100111", BitConverter.invertBitString("011101011000"));
		assertEquals("00110110", BitConverter.invertBitString("11001001"));
	}
	
	@Test
	public void testconvertBitStringToIntegerList() {
		List<Integer> values = new ArrayList<>();
		values.add(255);
		values.add(20);
		assertEquals(values, BitConverter.convertBitStringToIntegerList("1111111100010100"));
		values.add(179);
		values.add(42);
		assertEquals(values, BitConverter.convertBitStringToIntegerList("11111111000101001011001100101010"));
	}

}
