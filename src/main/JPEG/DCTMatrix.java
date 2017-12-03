package main.JPEG;

public class DCTMatrix {
	private boolean luminance; // not used!

	private int[][] matrix;
	private int ACindex;

	private int width;
	private int height;

	public DCTMatrix(boolean lum, int width, int height) {
		this.luminance = lum;
		this.width = width;
		this.height = height;

		this.matrix = new int[height][width];
		this.ACindex = 0;
	}

	public int[][] getMatrix() {
		return this.matrix;
	}

	public void setDC(int value) {
		this.matrix[0][0] = value;
	}
	
	/*
	 *		0  1  2  3  4  5  6  7
	 *_____________________________
	 *0|	0  1  5  6  14 15 27 28
	 *1|	2  4  7  13 16 26 29 42
	 *2|	3  8  12 17 25 30 41 43
	 *3|	9  11 18 24 31 40 44 53
	 *4|	10 19 23 32 39 45 52 54
	 *5|	20 22 33 38 46 51 55 60
	 *6|	21 34 37 47 50 56 59 61
	 *7|	35 36 48 49 57 58 62 63 
	 * 
	 * 
	 * 
	 */
	public void setAC(int position, int value){
		//zigzag for a 8x8 matrix
		int[] zigzag = {0, 1, 8, 16, 9,	2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26, 33,	40,	48,	41, 34,	27,	20,	13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57, 50, 43, 36, 29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62, 63};
		int matrixPos = zigzag[position];
		matrix[matrixPos/8][matrixPos%8] = value;		
	}

	/*
	 * not working!!
	 */
	@Deprecated
	public void setAC(int value) {
		// implement zig-zag
		throw new UnsupportedOperationException("this method is not working!");
//		int i = 0;
//		int j = 1;
//		int k = 0;
//		boolean down = true;
//		while (k < this.ACindex) 
//		{
//			k++;
//			if (down) {
//				if ((j - 1) >= 0) {
//					j--;
//					if ((i + 1) < this.matrix.length) {
//						i++;
//					} else {
//						j += 2;
//						down = false;
//					}
//				} else {
//					if ((i + 1) < this.matrix.length) {
//						i++;
//					} else {
//						j++;
//					}
//				}
//			} else {
//				if ((j + 1) < this.matrix[0].length) {
//					if ((i - 1) >= 0) {
//						i--;
//					} else {
//						down = true;
//					}
//				} else {
//					if ((i + 1) < this.matrix.length) {
//						i++;
//						down = true;
//					}
//				}
//			}
//		}
//		this.matrix[i][j] = value;
//		this.ACindex++;
	}
}
