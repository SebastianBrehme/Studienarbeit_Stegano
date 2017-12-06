package main.JPEG;

import java.util.ArrayList;
import java.util.List;

import main.Util.BitConverter;

public class HuffmanCode {
	
	private StartOfFrameMarker sofMarker;
	private HuffmanTable huffmanTable;
	private String data;
	private List<DCTMatrix> decodedData;
	private int startSpectralSelection; //not used
	private int endSpectralSelection; //not used
	private int height;//not used
	private int width;//not used

	public HuffmanCode(StartOfFrameMarker sofMarker, HuffmanTable huffmanTable, String encodedData, int startspectralselection, int endspectralselection /*, int width, int height*/)
	{
		this.sofMarker = sofMarker;
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
			for (int i = 0; i < this.sofMarker.getNumberOfComponents(); i++)
			{
				for (int horizontal = 0; horizontal < this.sofMarker.getHorizontalFactors()[i]; horizontal++)
				{
					for (int vertical = 0; vertical < this.sofMarker.getVerticalFactors()[i]; vertical++)
					{
						if (i == 0)
						{
							DCTMatrix luminanceMatrix = new DCTMatrix(true, matrixwidth, matrixheight);
							int dcvalue = this.decodeDC(this.huffmanTable.LuminanceDC);
							luminanceMatrix.setDC(dcvalue);
							this.decodeAC(this.huffmanTable.LuminanceAC, luminanceMatrix);
							this.decodedData.add(luminanceMatrix);
						}
						else if (i == 1)
						{
							DCTMatrix chrominanceMatrixCb = new DCTMatrix(false, matrixwidth ,matrixheight);
							int dcvalue = this.decodeDC(this.huffmanTable.ChrominanceDC);
							chrominanceMatrixCb.setDC(dcvalue);
							this.decodeAC(this.huffmanTable.ChrominanceAC, chrominanceMatrixCb);
							this.decodedData.add(chrominanceMatrixCb);
						}
						else if(i == 2)
						{
							DCTMatrix chrominanceMatrixCr = new DCTMatrix(false, matrixwidth, matrixheight);
							int dcvalue = this.decodeDC(this.huffmanTable.ChrominanceDC);
							chrominanceMatrixCr.setDC(dcvalue);
							this.decodeAC(this.huffmanTable.ChrominanceAC, chrominanceMatrixCr);
							this.decodedData.add(chrominanceMatrixCr);
						}
					}
				}
			}
			
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
		String c = this.getNextHuffmanDecodedValue(table);
		if (c == null) { /*error*/ } //TODO react when error occurs
		else 
		{
			int code = Integer.parseInt(c);
			if (code > 0)
			{
				String bits = this.data.substring(0, code);
				this.reduceData(code, this.data.length()); //remove the additional bits from the data stream
				
				int dcvalue = this.getDecodedDCACValue(bits, code); //the actual DC Value
				return dcvalue;
			}
			else
			{
				return 0;
			}
		} 
		return 0;
	}
	
	private DCTMatrix decodeAC(String[][] table, DCTMatrix matrix)
	{
		for (int position = 1; position <= this.endSpectralSelection; position++)
		{
			String code_s = this.getNextHuffmanDecodedValue(table);
			if (code_s == null) { /*error*/ } //TODO react when error occurs
			else 
			{
				int code = Integer.parseInt(code_s);
				if(code == 0) //remaining values are zero
				{
					return matrix;
				}				
				else if(code == 0xF0) //next 16 values are zero
				{
					for(int j=0;j<16;j++) {
						matrix.setAC(position, 0);
						position++;
					}
					//to avoid to jump over one position; 
					//the for loop does the last position++; 
					//positon has to stay on the last inserted value
					position--;
				}
				else
				{
					int zeroRun = BitConverter.getHigherBits(code);
					int magnitude = BitConverter.getLowerBits(code);					
					
					String bits = this.data.substring(0, magnitude);
					this.reduceData(magnitude, this.data.length());
					
					int value = this.getDecodedDCACValue(bits, magnitude);					
					
					for(int j = 0;j<zeroRun;j++) {
						matrix.setAC(position, 0);
						position++;
					}
					
					matrix.setAC(position,value);
				}
			}
		}
		return matrix;		
	}
	
	
	/* 
	 * gets the real DC/AC value of the additional bits
	 * for AC 0 Value is not possible
	 * 
	 * DC Code	Size	Additional Bits									DC Value
	 * 00		0	 					  									  0
	 * 01		1						0	1							   -1	1
	 * 02		2					00,01	10,11					    -3,-2	2,3
	 * 03		3		  000,001,010,011	100,101,110,111		  -7,-6,-5,-4	4,5,6,7
	 * 04		4			0000,...,0111	1000,...,1111		   -15,...,-8	8,...,15
	 * 05		5			  0 0000,...	...,1 1111			  -31,...,-16	16,...,31
	 * 06		6			 00 0000,...	...,11 1111			  -63,...,-32	32,...,63
	 * 07		7		    000 0000,...	...,111 1111		 -127,...,-64	64,...,127
	 * 08		8		   0000 0000,...	...,1111 1111		-255,...,-128	128,...,255
	 * 09		9		 0 0000 0000,...	...,1 1111 1111		-511,...,-256	256,...,511
	 * 0A		10		00 0000 0000,...	...,11 1111 1111   -1023,...,-512	512,...,1023
	 * 0B		11		000 0000 0000,...	...,111 1111 1111 -2047,...,-1024	1024,...,2047
	 *
	 *@param bits - the bits in the data stream
	 *@param length - the lengths of the bits
	 *@return DC Value
	 */
	private int getDecodedDCACValue(String bits, int length)
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
	
	//returns the next huffman encoded value in the data stream
	private String getNextHuffmanDecodedValue(String[][] table)
	{
		//System.out.println("Data: " + this.data);
		for (int i = 0; i < table.length; i++)
		{
			//System.out.println("Trying: "+table[i][0]);
			if (this.data.startsWith(table[i][0]))
			{
				this.reduceData(table[i][0].length(), this.data.length());
				return table[i][1];
			}
		}
		return null;
	}
}

