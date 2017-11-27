package main.JPEG;

public class DCTMatrix
{
	private boolean luminance;
	
	private int[][] matrix;
	private int ACindex;
	
	private int width;
	private int height;
	
	public DCTMatrix(boolean lum, int width, int height)
	{
		this.luminance = lum;
		this.width = width;
		this.height = height;
		
		this.matrix = new int[height][width];
		this.ACindex = 0;
	}
	
	public int[][] getMatrix()
	{
		return this.matrix;
	}
	
	public void setDC(int value)
	{
		this.matrix[0][0] = value;
	}
	
	public void setAC(int value)
	{
		//implement zig-zag 
		int i = 0;
		int j = 1;
		int k = 0;
		boolean down = true;
		while (k < this.ACindex)
		{
			if (down)
			{
				if ((j-1) >= 0)
				{
					j--;
					if ((i + 1) < this.matrix.length)
					{
						i++;
					}
					else
					{
						j += 2;
						down = false;
					}
				}
				else
				{
					if ((i + 1) < this.matrix.length)
					{
						i++;
					}
					else
					{
						j++;
					}
				}
			}
			else
			{
				if ((j + 1) < this.matrix[0].length)
				{
					if ((i - 1) >= 0)
					{
						i--;
					}
					else
					{
						down = true;
					}
				}
				else
				{
					if ((i + 1) < this.matrix.length)
					{
						i++;
						down = true;
					}
				}
			}
		}
		this.matrix[i][j] = value;
		this.ACindex++;
	}
}

