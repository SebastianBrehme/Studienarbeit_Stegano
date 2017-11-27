package test.Util;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Util.BitConverter;

public class TestBitConverter {

	@Test
	public void testConvertToBitString() {
		assertEquals("0", BitConverter.convertToBitString(0));
		assertEquals("1", BitConverter.convertToBitString(1));
		assertEquals("1010", BitConverter.convertToBitString(10));
		assertEquals("110010", BitConverter.convertToBitString(50));
		assertEquals("1100100", BitConverter.convertToBitString(100));
		assertEquals("10010110", BitConverter.convertToBitString(150));
		assertEquals("11001000", BitConverter.convertToBitString(200));
		assertEquals("11100110", BitConverter.convertToBitString(230));
		assertEquals("11111111", BitConverter.convertToBitString(255));
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
