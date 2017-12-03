package test.JPEG;

import static org.junit.Assert.*;

import org.junit.Test;

import main.JPEG.DCTMatrix;

public class TestDCTMatrix {

	@Test
	public void testGetMatrix() {
		DCTMatrix matrix = new DCTMatrix(true/*not used*/, 8, 8);
		int[][] expect = new int[8][8];
		assertArrayEquals(expect, matrix.getMatrix());
	}

	@Test
	public void testSetDC() {
		DCTMatrix matrix = new DCTMatrix(true/*not used*/, 8, 8);
		int[][] expect = new int[8][8];
		
		matrix.setDC(50);
		expect[0][0] = 50;		
		assertArrayEquals(expect, matrix.getMatrix());
		matrix.setDC(0);
		expect[0][0] = 0;		
		assertArrayEquals(expect, matrix.getMatrix());
		matrix.setDC(-150);
		expect[0][0] = -150;		
		assertArrayEquals(expect, matrix.getMatrix());
	}

	@Test//(timeout = 1000)
	public void testSetAC() {
		DCTMatrix matrix = new DCTMatrix(true/*not used*/, 8, 8);
		int[][] expect = new int[8][8];
		
		matrix.setAC(1,1);
		expect[0][1] = 1;
		assertArrayEquals(expect, matrix.getMatrix());
		
		matrix.setAC(2,2);
		expect[1][0] = 2;
		assertArrayEquals(expect, matrix.getMatrix());
		
		matrix.setAC(3,-3);
		expect[2][0] = -3;
		assertArrayEquals(expect, matrix.getMatrix());
		
		matrix.setAC(4,-4);
		expect[1][1] = -4;
		assertArrayEquals(expect, matrix.getMatrix());
		
		assertArrayEquals(expect, matrix.getMatrix());
		matrix.setAC(15,15);
		expect[0][5] = 15;
		assertArrayEquals(expect, matrix.getMatrix());
		
		assertArrayEquals(expect, matrix.getMatrix());
		matrix.setAC(56,56);
		expect[6][5] = 56;
		assertArrayEquals(expect, matrix.getMatrix());
		
		assertArrayEquals(expect, matrix.getMatrix());
		matrix.setAC(63,-63);
		expect[7][7] = -63;
		assertArrayEquals(expect, matrix.getMatrix());
	}

}
