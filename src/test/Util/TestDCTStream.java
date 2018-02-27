package test.Util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.JPEG.DCTMatrix;
import main.JPEG.Matrix;
import main.Util.DCTStream;

public class TestDCTStream {

	List<List<DCTMatrix>> data;
	
	@Before
	public void setUp() throws Exception {
		DCTMatrix one = new DCTMatrix(Matrix.LUMINANCE, 8,8);
		DCTMatrix two = new DCTMatrix(Matrix.LUMINANCE, 8,8);
		DCTMatrix three = new DCTMatrix(Matrix.LUMINANCE, 8,8);
		DCTMatrix four = new DCTMatrix(Matrix.LUMINANCE, 8,8);
		for(int i=0;i<64;i++) {
			one.setValue(i, 10);
			two.setValue(i, 0);
			three.setValue(i, -5);
			four.setValue(i, 5);
		}
		List<DCTMatrix> list1 = new ArrayList<DCTMatrix>();
		list1.add(one); list1.add(two);
		List<DCTMatrix> list2 = new ArrayList<DCTMatrix>();
		list2.add(three); list2.add(four);
		data = new ArrayList<List<DCTMatrix>>();
		data.add(list1); data.add(list2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDCTStream() {
		DCTStream stream = new DCTStream(this.data);
		assertEquals(stream.size, 64*4);
	}

	@Test
	public void testGetValueAt() {
		DCTStream stream = new DCTStream(this.data);
		for(int i=0;i<stream.size;i++) {
			if(i<64) {
				assertEquals(10, stream.getValueAt(i));
			}
			if(i>=64 && i<128) {
				assertEquals(0, stream.getValueAt(i));
			}
			if(i>=128 && i<192) {
				assertEquals(-5, stream.getValueAt(i));
			}
			if(i>=192) {
				assertEquals(5, stream.getValueAt(i));
			}
		}
	}

	@Test
	public void testSetValueAt() {
		DCTStream stream = new DCTStream(this.data);
		int testValues[] = {5,9,18,31,43,59,70,112,180,0,255};
		for(int temp:testValues) {
			stream.setValueAt(temp, temp);
			assertEquals(temp, stream.getValueAt(temp));
		}
	}

}
