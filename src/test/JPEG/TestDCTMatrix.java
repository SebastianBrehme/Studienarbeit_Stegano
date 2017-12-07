package test.JPEG;

import static org.junit.Assert.*;

import org.junit.Test;

import main.JPEG.DCTMatrix;
import main.JPEG.Matrix;

public class TestDCTMatrix {

	@Test
	public void testGetMatrix() {
		DCTMatrix matrix = new DCTMatrix(Matrix.LUMINANCE, 8, 8);
		int[][] expect = new int[8][8];
		assertArrayEquals(expect, matrix.getMatrix());
	}

	@Test
	public void testSetDC() {
		DCTMatrix matrix = new DCTMatrix(Matrix.CB, 8, 8);
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
		DCTMatrix matrix = new DCTMatrix(Matrix.CR, 8, 8);
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
	
	@Test
	public void testgetMatrixType() {
		DCTMatrix matrix1 = new DCTMatrix(Matrix.LUMINANCE, 8, 8);
		assertEquals(Matrix.LUMINANCE, matrix1.getMatrixType());
		
		DCTMatrix matrix2 = new DCTMatrix(Matrix.CB, 8, 8);
		assertEquals(Matrix.CB, matrix2.getMatrixType());
		
		DCTMatrix matrix3 = new DCTMatrix(Matrix.CR, 8, 8);
		assertEquals(Matrix.CR, matrix3.getMatrixType());
	}
	
	@Test
	public void testgetValue() {
		DCTMatrix matrix = new DCTMatrix(Matrix.CR, 8, 8);
		
		matrix.setAC(1,1);
		matrix.setAC(2,2);
		matrix.setAC(3,-3);
		matrix.setAC(4,-4);
		matrix.setAC(15,15);
		matrix.setAC(56,56);
		matrix.setAC(63,-63);

		assertEquals(1, matrix.getValue(1));
		assertEquals(2, matrix.getValue(2));
		assertEquals(-3, matrix.getValue(3));
		assertEquals(-4, matrix.getValue(4));
		assertEquals(15, matrix.getValue(15));
		assertEquals(56, matrix.getValue(56));
		assertEquals(-63, matrix.getValue(63));
	}

}
