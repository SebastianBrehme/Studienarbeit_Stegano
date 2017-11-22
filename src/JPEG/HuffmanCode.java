package JPEG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HuffmanCode {
	
	private HuffmanTable huff;
	private String data;
	private List decodedData;
	private int ss;
	private int se;
	private int height;
	private int width;

	public HuffmanCode(HuffmanTable huffmanTable, String encodedData, int startspectralselection, int endspectralselection /*, int width, int height*/)
	{
		this.huff = huffmanTable;
		this.data = encodedData;
		this.ss = startspectralselection;
		this.se = endspectralselection;
		//this.height = height;
		//this.width = width;
		
		this.decodedData = new ArrayList<>();
	}
	
	public List getDecodedData()
	{
		return this.decodedData;
	}
	
	private void reduceData(int beginIndex, int endIndex)
	{
		this.data = this.data.substring(beginIndex, endIndex);
	}
	
	public void decode()
	{
		int matrixwidth = 8;
		int matrixheight = 8;
		int hsum = 0;
		int wsum = 0;
		boolean done = false;
		while (!done)
		{
			DCTMatrix luminanceMatrix = new DCTMatrix(true, matrixwidth, matrixheight);
			int dcvalue = this.decodeDC(this.huff.LuminanceDC);
			luminanceMatrix.setDC(dcvalue);
			this.decodeAC(this.huff.LuminanceAC, luminanceMatrix);
			this.decodedData.add(luminanceMatrix);
			
			DCTMatrix chrominanceMatrixCb = new DCTMatrix(false, matrixwidth ,matrixheight);
			dcvalue = this.decodeDC(this.huff.ChrominanceDC);
			chrominanceMatrixCb.setDC(dcvalue);
			this.decodeAC(this.huff.ChrominanceAC, chrominanceMatrixCb);
			this.decodedData.add(chrominanceMatrixCb);
			
			DCTMatrix chrominanceMatrixCr = new DCTMatrix(false, matrixwidth, matrixheight);
			dcvalue = this.decodeDC(this.huff.ChrominanceDC);
			chrominanceMatrixCr.setDC(dcvalue);
			this.decodeAC(this.huff.ChrominanceAC, chrominanceMatrixCr);
			this.decodedData.add(chrominanceMatrixCr);
			
			hsum += matrixheight;
			wsum += matrixwidth;
			if (this.data.length() < 8)
			{
				done = true;
			}
		}
	}
	
	private int decodeDC(String[][] table)
	{
		String c = this.getCode(table);
		if (c == null) { /*error*/ } 
		else 
		{
			int code = Integer.parseInt(c);
			String bits = this.data.substring(0, code-1);
			this.reduceData(code, this.data.length()-1);
			
			int dcvalue = this.getDCValue(bits, code);
			return dcvalue;
		} 
		return 0;
	}
	
	private void decodeAC(String[][] table, DCTMatrix matrix)
	{
		for (int i = 1; i <= this.se; i++)
		{
			String code = this.getCode(table);
			if (code == null) { /*error*/ } 
			else 
			{
				int c = Integer.parseInt(code);
				if (c == 0)
				{
					return;
				}
				else
				{
					matrix.setAC(c);
				}
			}
		}
		
		
	}
	
	private int getDCValue(String bits, int length)
	{
		double x = Math.pow(2, length);
		int min = ((int)x-1) * (-1);
		int highestnegativ = (int) x / (-2);
		
		int c = 0;
		for (int i = 0; i < bits.length(); i++)
		{
			double mult = Math.pow(2, i);
			if (bits.charAt(bits.length()-i-1) == '1')
			{
				c += (mult);
			}
		}
		
		int k = min + c;
		if (k <= highestnegativ)
		{
			return k;
		}
		else
		{
			int temp = k - highestnegativ;
			k = highestnegativ * (-1) + (temp-1);
			return k;
		}
	}
	
	private String getCode(String[][] table)
	{
		System.out.println(this.data);
		try {
			int k = System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < table.length; i++)
		{
			if (this.data.startsWith(table[i][0]))
			{
				this.reduceData(table[i][0].length(), this.data.length()-1);
				return table[i][1];
			}
		}
		return null;
	}
}

