package test.JPEG;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import main.JPEG.ImageData;

public class TestImageData {
	
	@Test
	public void testSubstitudeFF00() {
		List<Integer> temp1 = Arrays.asList(0x55,0xFF,0x00,0x88);
		List<List<Integer>> raw = Arrays.asList(temp1);		
		List<Integer> temp2 = Arrays.asList(0x55, 0xFF, 0x88);
		List<List<Integer>> result = Arrays.asList(temp2);
		
		ImageData data = new ImageData(temp1);		
		raw = data.substitudeFF00(raw);
		assertEquals(result, raw);
		
		raw = data.substitudeFF00(raw);
		assertEquals(result, raw);
		
		temp1 = Arrays.asList(0x44, 0xFF, 0x00, 0xFF, 0x00, 0xFF, 0xD0, 0x22, 0xFF, 0x00);
		raw = Arrays.asList(temp1);		
		temp2 = Arrays.asList(0x44, 0xFF, 0xFF, 0xFF, 0xD0, 0x22, 0xFF);
		result = Arrays.asList(temp2);
		
		raw = data.substitudeFF00(raw);
		assertEquals(result, raw);
	}
	
	@Test
	public void testSplitRestartMarker() {
		List<Integer> raw = Arrays.asList(0x44,0xAB, 0xFF, 0xD0, 0x55, 0xFF, 0xD7, 0x99, 0xFF, 0xD8);
		ImageData data = new ImageData(raw);
		
		List<Integer> temp1 = Arrays.asList(0x44, 0xAB);
		List<Integer> temp2 = Arrays.asList(0x55);
		List<Integer> temp3 = Arrays.asList(0x99, 0xFF, 0xD8);
		List<List<Integer>> expected = Arrays.asList(temp1, temp2, temp3);
		
		List<List<Integer>> actual = data.splitRestartMarker(raw);
		
		assertEquals(expected, actual);		
	}
	
	@Test
	public void testconvertIntegerDataToBit() {
		List<Integer> temp1 = Arrays.asList(0x22, 0xFF);
		List<Integer> temp2 = Arrays.asList(0xD4, 0x82);
		List<List<Integer>> raw = Arrays.asList(temp1, temp2);
		
		ImageData data = new ImageData(temp1);
		List<String> expected = Arrays.asList("0010001011111111", "1101010010000010");
		List<String> actual = data.convertIntegerDataToBits(raw);
		assertEquals(expected, actual);
	}
}
