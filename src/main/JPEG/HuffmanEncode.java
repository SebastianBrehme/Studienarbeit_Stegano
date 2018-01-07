package main.JPEG;

import java.util.ArrayList;
import java.util.List;

import main.Util.BitConverter;

public class HuffmanEncode {
	
	int listIndex;
	List<List<DCTMatrix>> decodedData;
	List<Integer> encodedData;
	ImageData imageData;
	HuffmanTable huffmanTable;
	
	public HuffmanEncode(List<List<DCTMatrix>> decodedData, HuffmanTable huffmanTable, ImageData imageData) {
		this.decodedData = decodedData;
		this.listIndex = 0;
		this.huffmanTable = huffmanTable;
		this.imageData = imageData;
		this.encodedData = new ArrayList<>();
	}
	
	public List<Integer> getEncodedData() {
		return this.encodedData;
	}
	
	public void encode()
	{
		this.listIndex = 0;
		int restart = 0xD0;
		
		for(listIndex = 0;listIndex<this.decodedData.size();listIndex++) {
			String encodedBinary = "";
			for (int i = 0; i < this.decodedData.get(listIndex).size(); i++)
			{
				encodedBinary += encodeDCValue(i);
				encodedBinary += encodeACValue(i);
			}
			int x = encodedBinary.length() % 8;
			for (int i = 0; i < 8-x; i++)
			{
				encodedBinary += "1";
			}
			
			List<Integer> temp = BitConverter.convertBitStringToIntegerList(encodedBinary);
			for(int i=0;i<temp.size()-1;i++) {
				if(temp.get(i) == 0xFF) {
					temp.add(i+1, 0x00);
				}
			}
			this.encodedData.addAll(temp);
			//if not the last list
			if(listIndex<this.decodedData.size()-1) {
				this.encodedData.add(0xFF);
				this.encodedData.add(restart);
				restart++;
				if(restart > 0xD7) {
					restart = 0xD0;
				}
			}
		}
	}

	private String encodeDCValue(int i) 
	{
		String encodedBits = "";
		int dcValue = ((DCTMatrix)this.decodedData.get(listIndex).get(i)).getValue(0);
		if (dcValue == 0)
		{
			encodedBits = this.getEncodedBitsDependingOnMatrixType(true, i, dcValue);
			return encodedBits;
		}
		
		String encodedDC = this.getEncodedDCACValue(dcValue);
		String bitsForLength = getEncodedBitsDependingOnMatrixType(true, i, encodedDC.length());
		if (bitsForLength == null) { 
			System.err.println("encodeDCValue: bitsForLength is null!");
			throw new RuntimeException("encodeDCValue: bitsForLength is null!");
			} //TODO react when error occurs
		else
		{
			encodedBits = bitsForLength + encodedDC;
		}
		return encodedBits;
	}
	
	private String encodeACValue(int i)
	{
		String acString = "";
		int zerorun = 0;
		int zrlrun = 0;
		for (int pos = 1; pos <= this.imageData.endSpectralSelection; pos++)
		{
			int acValue = ((DCTMatrix) this.decodedData.get(listIndex).get(i)).getValue(pos);
			if (acValue == 0)
			{
				zerorun++;
				if (zerorun == 16)
				{
					zrlrun++;
					String zrl = getEncodedBitsDependingOnMatrixType(false, i, 0xF0);
					if (zrl == null) {
						System.out.println("encodeACValue: zrl is null!");
						throw new RuntimeException("encodeACValue: zrl is null!");} //TODO react on error
					else
					{
						acString += zrl;
					}
					zerorun = 0;
				}
				
			}
			else
			{
				String encodedAC = this.getEncodedDCACValue(acValue);
				int v = zerorun * 16 + encodedAC.length();
				String x = getEncodedBitsDependingOnMatrixType(false, i, v);
				if (x == null) {
					System.out.println("encodeACValue: x is null!"); 
					throw new RuntimeException("encodeACValue: x is null!");
				} //TODO react on error
				else
				{
					acString += x + encodedAC;
				}
				zerorun = 0;
				zrlrun = 0;
			}
		}
		String endOfBlock = getEncodedBitsDependingOnMatrixType(false, i, 0);
		if (zrlrun > 0)
		{
			String zrlBits = getEncodedBitsDependingOnMatrixType(false, i, 0xF0);
			while (acString.endsWith(zrlBits))
			{
				acString = acString.substring(0, acString.length()-zrlBits.length());
			}
			acString += endOfBlock;
		}
		else
		{
			if (zerorun > 0)
			{
				acString += endOfBlock;
			}
		}
		return acString;
	}
	
	private String getEncodedDCACValue(int value)
	{
		int positivValue = Math.abs(value); 
		double log = Math.log10(positivValue) / Math.log10(2);
		int length = (int) Math.ceil(log);
		String bits = BitConverter.convertToBitString(positivValue, length);
		if (value > 0) { return bits; }
		else { return BitConverter.invertBitString(bits); }
	}
	
	private String getEncodedBitsDependingOnMatrixType(boolean DC, int i, int value)
	{
		String x = null;
		if (((DCTMatrix) this.decodedData.get(listIndex).get(i)).getMatrixType() == Matrix.LUMINANCE)
		{
			if (DC)
			{
				x = getHuffmanEncodedBits(this.huffmanTable.LuminanceDC, value);
			}
			else
			{
				x = getHuffmanEncodedBits(this.huffmanTable.LuminanceAC, value);
			}
		}
		else
		{
			if (DC)
			{
				x = getHuffmanEncodedBits(this.huffmanTable.ChrominanceDC, value);
			}
			else
			{
				x = getHuffmanEncodedBits(this.huffmanTable.ChrominanceAC, value);
			}
			
		}		
		return x;
	}
	
	private String getHuffmanEncodedBits(String[][] table, int value)
	{
		for (int i = 0; i < table.length; i++)
		{
			if (Integer.parseInt(table[i][1]) == value)
			{
				return table[i][0];
			}
		}
		return null;
	}
}
