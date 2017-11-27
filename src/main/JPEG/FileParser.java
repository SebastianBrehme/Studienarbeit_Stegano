package main.JPEG;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import main.Util.BitConverter;

public class FileParser {
	private String filepath = "";
	
	private int status;	// 0 = INITIAL, 2 = read FF, 3 = EOF
	
	private Object[][] huffmantables;
	
	private HuffmanTable huffTables;
	
	private List<Integer> imageData;
	
	public FileParser()
	{
		this.status = 0;
		huffmantables = new Object[4][];
	}
	
	public void setFilePath(String file)
	{
		this.filepath = file;
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
			int c;
			
			int huffTablesIndex = 0;
			
			while ((c = in.read()) != -1)
			{
				if (c == 255)	//0xFF
				{
					this.status = 2;
				}
				else if (c == 196) //0xC4
				{
					if (status == 2)
					{
						List x = this.readHuffmanTable(in);
						this.huffmantables[huffTablesIndex++] = x.toArray();
					}
					else
					{
						this.status = 0;
					}
				}
				else if (c == 218)	//0xDA
				{
					if (status == 2)
					{
						this.imageData = this.readImageData(in);
					}
					else
					{
						this.status = 0;
				
					}
				}
				else
				{
					this.status = 0;
				}
				
				if (this.status == 3)
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
		}
		
		this.createHuffmanTables();
		this.huffTables.printHuffmanTables();
		HuffmanCode hc = this.processImageData();
		this.printMatrix(hc);
	}
	
	private List readHuffmanTable(FileInputStream in) throws IOException
	{
		List x = new ArrayList<>();
		int huf;
		while ((huf = in.read()) != -1)
		{
			if (huf != 255) //0xFF
			{
				x.add(huf);
			}
			else
			{
				this.status = 2;
				return x;
			}
		}
		this.status = 3;
		return x;
	}
	
	private List<Integer> readImageData(FileInputStream in) throws IOException
	{
		List x = new ArrayList<>();
		int d;
		while ((d = in.read()) != -1)
		{
			if (d == 255)	//0xFF
			{
				int temp = in.read();
				if (temp == 217)	//0xD9
				{ 
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
	
	private void createHuffmanTables()
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

	private HuffmanCode processImageData()
	{
		int[] data = this.imageData.stream().mapToInt(i->i).toArray();
		int index = 0;
		int first = data[index++]; //should be 0x00
		int second = data[index++]; //should be 0x0C
		int numberofcomponents = data[index++];
		int[][] components = new int[numberofcomponents][3];
		for (int i = 0; i < numberofcomponents; i++)
		{
			components[i][0] = data[index++];	//component id
			int temp = data[index++];
			components[i][1] = temp / 2;							//DC Table Number
			components[i][2] = temp % 2;							//AC Table Number
		}
		int startSpectralSelection = data[index++]; //start of spectral selection
		int endSpectralSelection = data[index++]; //end of spectral selection
		int temp = data[index++];
		int ah = temp / 2;
		int al = temp % 2;
		
		StringBuilder bitStringData = new StringBuilder();
		for (int i = index; i < data.length; i++)
		{
			//parse to binary
			int value = data[index++];
						
			bitStringData.append(BitConverter.convertToBitString(value));
			
			// skip "stuff bit"
			if (value == 255 && data[index] == 0)	//0xFF 0x00
			{
				index++;
				i++;
			}
		}
		
		HuffmanCode hc = new HuffmanCode(this.huffTables, bitStringData.toString(), startSpectralSelection, endSpectralSelection);
		hc.decode();
		return hc;
	} 
	
	private void printMatrix(HuffmanCode hc)
	{
		List m = hc.getDecodedData();
		for(int k = 0; k < m.size(); k++)
		{
			System.out.println("MAtrix:");
			int[][] matrix = ((DCTMatrix) m.get(k)).getMatrix();
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
}

