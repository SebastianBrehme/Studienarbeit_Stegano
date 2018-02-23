package main.JPEG;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
	
	enum Status{
		INITIAL,
		READFF,
		EOF
	}
	private Status status;	
	
	private String filepath = "";
	
	private String outFilepath = "NewFile.jpg";
	
	private StartOfFrameMarker sofMarker;
	
	private Object[][] huffmantables;
	
	public HuffmanTable huffTables;
	
	private ImageData imageData;
	
	private FileOutputStream out = null;
	
	public FileParser()
	{
		this.status = Status.INITIAL;
		huffmantables = new Object[4][];
	}
	
	public void setFilePath(String file)
	{
		this.filepath = file;
		this.outFilepath = file.substring(0, file.lastIndexOf(".")) + "_new.jpg";
	}
	
	public String getFilePath()
	{
		return this.filepath;
	}
	
	public void readFileBytes() throws IOException
	{
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(this.filepath);
			out = new FileOutputStream(this.outFilepath);
			int c;
			
			int huffTablesIndex = 0;
			while ((c = in.read()) != -1)
			{
				out.write(c);
				
				if (c == 255)	//0xFF
				{
					this.status = Status.READFF;
				}
				else if (c == 192) //0xC0 Start of Frame Marker
				{
					if (this.status == Status.READFF)
					{
						List<Integer> x = this.readNextSegment(in);
						this.sofMarker = new StartOfFrameMarker(x);
					}
				}
				else if (c == 196) //0xC4 DHT - Huffmantable
				{
					if (this.status == Status.READFF)
					{
						List<Integer> x = this.readNextSegment(in);
						this.huffmantables[huffTablesIndex++] = x.toArray();
					}
					else
					{
						this.status = Status.INITIAL;
					}
				}
				else if (c == 218)	//0xDA SOS - Start of Scan
				{
					if (this.status == Status.READFF)
					{
						this.imageData = new ImageData(this.readImageData(in));
					}
					else
					{
						this.status = Status.INITIAL;
				
					}
				}
				else if(c>= 0xE0 && c<=0xEF) {
					if(this.status == Status.READFF) {
						skipAPPData(in);
					}
				}
				else
				{
					this.status = Status.INITIAL;
				}
				
				if (this.status == Status.EOF)
				{
					break;
				}
			}
			
		}
		finally
		{
			if (in != null)
			{
				in.close();
			}
			if (out != null)
			{
				out.flush();
			}
		}
		
		
	}
	
	private void skipAPPData(FileInputStream in) throws IOException {
		int firstLength = in.read();
		int secondLength = in.read();
		int totalLength = firstLength *256 + secondLength;
		out.write(firstLength);
		out.write(secondLength);
		for(int i=0;i<totalLength-2;i++) {
			int c = in.read();
			out.write(c);
		}
	}
	
	private List<Integer> readNextSegment(FileInputStream in) throws IOException
	{
		List<Integer> x = new ArrayList<>();
		int nextValue;
		while ((nextValue = in.read()) != -1)
		{
			out.write(nextValue);
			if (nextValue != 255)
			{
				x.add(nextValue);
			}
			else
			{
				this.status = Status.READFF;
				return x;
			}
		}
		this.status = Status.EOF;
		return x;
	}
	
	private List<Integer> readImageData(FileInputStream in) throws IOException
	{
		List<Integer> integerList = new ArrayList<>();
		int dataByte;
		while ((dataByte = in.read()) != -1)
		{
			if (dataByte == 255)	//0xFF
			{
				int temp = in.read();
				if (temp == 217)	//0xD9
				{ 
					this.status = Status.EOF;
					return integerList;
				}
				else
				{
					integerList.add(dataByte);
					integerList.add(temp);
				}
			}
			else
			{
				integerList.add(dataByte);
			}
		}
		return integerList;
	}
	
	public void createHuffmanTables()
	{
		if (this.huffmantables[1] == null)
		{
			this.huffTables = new HuffmanTable(this.huffmantables[0]);
		}
		else
		{
			this.huffTables = new HuffmanTable(this.huffmantables[0], this.huffmantables[1], this.huffmantables[2], this.huffmantables[3]);
		}
	}

	public HuffmanDecode processImageData()
	{
		this.imageData.sortRawData();
		HuffmanDecode hc = new HuffmanDecode(this.sofMarker, this.huffTables, this.imageData);
		hc.decode();
		return hc;
	} 
	
	public void printMatrix(HuffmanDecode hc)
	{
		List<List<DCTMatrix>> data = hc.getDecodedData();
		for(List<DCTMatrix> temp : data) {
			for(DCTMatrix matrix : temp) {
				System.out.println(matrix);
			}
		}
	}
	
	public void writeFileBytes(HuffmanEncode hc) throws IOException
	{
		String f = this.filepath.substring(0, this.filepath.lastIndexOf('.'));
		
		int index = 0;
		//write SOS Header
		out.write(this.imageData.firstLength);	//for size
		out.write(this.imageData.secoundLength);	//for size
		
		int numberofcomponents = this.imageData.components.length;
		out.write(numberofcomponents);
		for (int i = 0; i < numberofcomponents; i++)
		{
			out.write(this.imageData.components[i].ID); //component id
			out.write(this.imageData.components[i].DC*16+this.imageData.components[i].AC); // DC/AC Table numbers
		}
		out.write(this.imageData.startSpectralSelection); //start of spectral selection
		out.write(this.imageData.endSpectralSelection); //end of spectral selection
		out.write(this.imageData.successivApproximation); // two 4 bit fields
		
		//Write content of picture
		for (int i = 0; i < hc.getEncodedData().size(); i++)
		{
			int value = (int)hc.getEncodedData().get(i); 
			out.write(value);
		}
		//write end of SOS
		out.write(0xFF);
		out.write(0xD9);
			
		out.flush();
		out.close();
	}
}

