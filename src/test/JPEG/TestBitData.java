package test.JPEG;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.JPEG.BitData;

public class TestBitData {
	
	List<List<Integer>> data;

	@Before
	public void setUp() throws Exception {
		data = new ArrayList<List<Integer>>();
		List<Integer> tmp = new ArrayList<Integer>();
		tmp.add(0xFF);
		tmp.add(0x00);
		data.add(tmp);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBitData() {
		BitData b_data = new BitData(data);
		assertTrue(b_data.startsWithString("1111111100000000"));
	}

	@Test
	public void testReduce() {
		BitData b_data = new BitData(data);
		b_data.reduce(8, -1);
		boolean result = b_data.startsWithString("0000");
		assertTrue(result);
	}

	@Test
	public void testlessThan8() {
		BitData b_data = new BitData(data);
		assertFalse(b_data.lessThan8());
		b_data.reduce(8, -1);
		assertTrue(b_data.lessThan8());
		b_data.reduce(1, -1);
		assertTrue(b_data.lessThan8());
	}

	@Test
	public void testCutPartAsString() {
		BitData b_data = new BitData(data);
		String result = b_data.cutPartAsString(4, 12);
		assertEquals("11110000",result);
	}

	@Test
	public void testNextList() {
		
	}

	@Test
	public void testStartsWithString() {
		BitData b_data = new BitData(data);
		boolean result = b_data.startsWithString("111");
		assertTrue(result);		
	}

}
