package test.Util;

import static org.junit.Assert.*;

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

}
