package main.JPEG;

import java.util.ArrayList;
import java.util.List;

public class HuffmanCode {
	
	private HuffmanTable huffmanTable;
	private String data;
	private List<DCTMatrix> decodedData;
	private int startSpectralSelection; //not used
	private int endSpectralSelection; //not used
	private int height;//not used
	private int width;//not used

	public HuffmanCode(HuffmanTable huffmanTable, String encodedData, int startspectralselection, int endspectralselection /*, int width, int height*/)
	{
		this.huffmanTable = huffmanTable;
		this.data = encodedData;
		this.startSpectralSelection = startspectralselection;
		this.endSpectralSelection = endspectralselection;
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
			int dcvalue = this.decodeDC(this.huffmanTable.LuminanceDC);
			luminanceMatrix.setDC(dcvalue);
			this.decodeAC(this.huffmanTable.LuminanceAC, luminanceMatrix);
			this.decodedData.add(luminanceMatrix);
			
			DCTMatrix chrominanceMatrixCb = new DCTMatrix(false, matrixwidth ,matrixheight);
			dcvalue = this.decodeDC(this.huffmanTable.ChrominanceDC);
			chrominanceMatrixCb.setDC(dcvalue);
			this.decodeAC(this.huffmanTable.ChrominanceAC, chrominanceMatrixCb);
			this.decodedData.add(chrominanceMatrixCb);
			
			DCTMatrix chrominanceMatrixCr = new DCTMatrix(false, matrixwidth, matrixheight);
			dcvalue = this.decodeDC(this.huffmanTable.ChrominanceDC);
			chrominanceMatrixCr.setDC(dcvalue);
			this.decodeAC(this.huffmanTable.ChrominanceAC, chrominanceMatrixCr);
			this.decodedData.add(chrominanceMatrixCr);
			
			hsum += matrixheight;
			wsum += matrixwidth;
			if (this.data.length() < 8)
			{
				done = true;
			}
		}
	}
	//the method to call
	private int decodeDC(String[][] table)
	{
		String c = this.getCode(table);
		if (c == null) { /*error*/ } //TODO react when error occurs
		else 
		{
			int code = Integer.parseInt(c);
			if (code > 0)
			{
				String bits = this.data.substring(0, code);
				this.reduceData(code, this.data.length()); //the additional bits
				
				int dcvalue = this.getDCValue(bits, code); //the actual DC Value
				return dcvalue;
			}
			else
			{
				return 0;
			}
		} 
		return 0;
	}
	
	private void decodeAC(String[][] table, DCTMatrix matrix)
	{
		for (int i = 1; i <= this.endSpectralSelection; i++)
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
	
	//gets the real DC value of the additional bits
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
	
	//returns the value thats is represented by the huffmancode the data starts with
	private String getCode(String[][] table)
	{
		System.out.println("Data: " + this.data);
		for (int i = 0; i < table.length; i++)
		{
			System.out.println(table[i][0]);
			if (this.data.startsWith(table[i][0]))
			{
				this.reduceData(table[i][0].length(), this.data.length());
				return table[i][1];
			}
		}
		return null;
	}
}

