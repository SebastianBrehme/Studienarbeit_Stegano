package main.JPEG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import main.Util.BitConverter;

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
	
	private List<Integer> imageData;
	
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
						this.imageData = this.readImageData(in);
					}
					else
					{
						this.status = Status.INITIAL;
				
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
		List<Integer> x = new ArrayList<>();
		int d;
		while ((d = in.read()) != -1)
		{
			if (d == 255)	//0xFF
			{
				int temp = in.read();
				if (temp == 217)	//0xD9
				{ 
					this.status = Status.EOF;
					return x;
				}
				else
				{
					x.add(d);
					x.add(temp);
				}
			}
			else
			{
				x.add(d);
			}
		}
		return x;
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

	public HuffmanCode processImageData()
	{
		int[] data = this.imageData.stream().mapToInt(i->i).toArray();
		int index = 0;
		int first = data[index++]; //should be 0x00 - for size
		int second = data[index++]; //should be 0x0C - for size
		
		int numberofcomponents = data[index++];
		int[][] components = new int[numberofcomponents][3];
		for (int i = 0; i < numberofcomponents; i++)
		{
			components[i][0] = data[index++];	//component id
			int temp = data[index++];
			components[i][1] = BitConverter.getHigherBits(temp); //DC Table Number
			components[i][2] = BitConverter.getLowerBits(temp);	//AC Table Number
		}
		//Only for progressiv mode, currently not used
		int startSpectralSelection = data[index++]; //start of spectral selection
		int endSpectralSelection = data[index++]; //end of spectral selection
		int successivApproximation = data[index++]; //two 4 bit fields
		
		
		StringBuilder bitStringData = new StringBuilder();
		for (int i = index; i < data.length; i++)
		{
			//parse to binary
			int value = data[index++];
						
			bitStringData.append(BitConverter.convertToBitString(value));
			
			// skip "stuff bit"
			if (value == 255 && data[index] == 0)	//0xFF 0x00
			{
				index++; //sicher???
				i++;
			}
		}
		
		HuffmanCode hc = new HuffmanCode(this.sofMarker, this.huffTables, bitStringData.toString(), startSpectralSelection, endSpectralSelection);
		hc.decode();
		return hc;
	} 
	
	public void printMatrix(HuffmanCode hc)
	{
		Object[] m = hc.getDecodedData();
		for(int k = 0; k < m.length; k++)
		{
			System.out.println("Matrix:");
			int[][] matrix = ((DCTMatrix) m[k]).getMatrix();
			for (int i = 0; i < matrix.length; i++)
			{
				for (int j = 0; j < matrix[0].length; j++)
				{
					System.out.print(matrix[i][j] + " ");
				}
				System.out.println("");
			}
		}
	}
	
	public void writeFileBytes(HuffmanCode hc) throws IOException
	{
		String f = this.filepath.substring(0, this.filepath.lastIndexOf('.'));
		
		int index = 0;
		//write SOS Header
		out.write(this.imageData.get(index++));	//for size
		out.write(this.imageData.get(index++));	//for size
		
		int numberofcomponents = this.imageData.get(index++);
		out.write(numberofcomponents);
		for (int i = 0; i < numberofcomponents; i++)
		{
			out.write(this.imageData.get(index++)); //component id
			out.write(this.imageData.get(index++)); // DC/AC Table numbers
		}
		out.write(this.imageData.get(index++)); //start of spectral selection
		out.write(this.imageData.get(index++)); //end of spectral selection
		out.write(this.imageData.get(index++)); // two 4 bit fields
		
		//Write content of picture
		for (int i = 0; i < hc.getEncodedData().size(); i++)
		{
			int value = (int)hc.getEncodedData().get(i); 
			out.write(value);
			
			if (value == 255)
			{
				out.write(0x00);
			}
		}
		//write end of SOS
		out.write(0xFF);
		out.write(0xD9);
			
		out.flush();
		out.close();
		
		System.out.println("Write Done");
	}
}

