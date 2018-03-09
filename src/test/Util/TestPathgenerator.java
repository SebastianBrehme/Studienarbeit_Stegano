package test.Util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SpringLayout.Constraints;

import org.junit.Before;
import org.junit.Test;

import main.JPEG.DCTMatrix;
import main.JPEG.Matrix;
import main.Util.DCTStream;
import main.Util.Pathgenerator;

public class TestPathgenerator {

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
	
	@Test
	public void testGeneratePath() {
		Pathgenerator generator = new Pathgenerator(new DCTStream(this.data), 12345678L);
		Iterator<Integer> actual = generator.generatePath().iterator();
		assertEquals(new Integer(2), actual.next());		
		assertEquals(new Integer(3), actual.next());
		assertEquals(new Integer(1), actual.next());
		assertEquals(new Integer(0), actual.next());

		generator = new Pathgenerator(new DCTStream(this.data), 7552345675345678765L);
		actual = generator.generatePath().iterator();
		assertEquals(new Integer(1), actual.next());		
		assertEquals(new Integer(2), actual.next());
		assertEquals(new Integer(3), actual.next());
		assertEquals(new Integer(0), actual.next());
		
		generator = new Pathgenerator(new DCTStream(this.data), 7559875675676678765L);
		actual = generator.generatePath().iterator();
		assertEquals(new Integer(2), actual.next());		
		assertEquals(new Integer(1), actual.next());
		assertEquals(new Integer(0), actual.next());
		assertEquals(new Integer(3), actual.next());
	}
}
