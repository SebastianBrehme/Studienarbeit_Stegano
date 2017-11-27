package main.JPEG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HuffmanTable {

	
	public int[][] codes;
	
	String[][] LuminanceDC;
	String[][] LuminanceAC;
	String[][] ChrominanceDC;
	String[][] ChrominanceAC;
	int LuminanceACIndex;
	int LuminanceDCIndex;
	int ChrominanceACIndex;
	int ChrominanceDCIndex;
	
	public HuffmanTable(Object[] content)
	{
		this.initTables(content);
	}
	
	public HuffmanTable(Object[] content0, Object[] content1, Object[] content2, Object[] content3)
	{
		this.initTable(content0);
		this.initTable(content1);
		this.initTable(content2);
		this.initTable(content3);
	}
	
	private void initTables(Object[] table)
	{
		//length 
		int length = (int)table[0] * (16*16) + (int)table[1];
		
		int index = 2;
		//loop
		while (index < length)
		{
			List content = new ArrayList<>();
			
			content.add(table[index]);
			int l = 0;
			for (int i = index + 1; i < index + 17; i++)
			{
				l += (int) table[i];
				content.add(table[i]);
			}
			for (int i = 0; i < l; i++)
			{
				content.add(table[index + i + 17]);
			}
			int fulllength = l + 19;
			if (fulllength > 255)
			{
				content.add(0, fulllength / 256);
				content.add(1, fulllength % 256);
			}
			else
			{
				content.add(0, 0);
				content.add(1, fulllength);
			}
			index += l + 17;
			initTable(content.toArray());
		}
	}
	
	private void initTable(Object[] table)
	{
		//length 
		int length = (int)table[0] * (16*16) + (int)table[1];
		
		//code count for each length
		int[] counts = new int[16];
		counts[0] = (int) table[3];
		counts[1] = (int) table[4];
		counts[2] = (int) table[5];
		counts[3] = (int) table[6];
		counts[4] = (int) table[7];
		counts[5] = (int) table[8];
		counts[6] = (int) table[9];
		counts[7] = (int) table[10];
		counts[8] = (int) table[11];
		counts[9] = (int) table[12];
		counts[10] = (int) table[13];
		counts[11] = (int) table[14];
		counts[12] = (int) table[15];
		counts[13] = (int) table[16];
		counts[14] = (int) table[17];
		counts[15] = (int) table[18];
		//codes sorted by their length
		int tableIndex = 19;
		this.codes = new int[16][];
		int codecount = 0;
		for (int i = 0; i < 16; i++)
		{
			codes[i] = new int[counts[i] + 1];
			codes[i][0] = counts[i];
			codecount += counts[i];
			for (int j = 1; j < counts[i] + 1; j++)
			{
				codes[i][j] = (int) table[tableIndex++];
			}
		}
		
		//DHT class and ID
		int cl = 0;
		int id = 0;
		int table2 = (int) table[2];
		if (table2 == 0)
		{
			cl = 0;
			id = 0;
			this.LuminanceDC = new String[codecount][2];
			this.LuminanceDCIndex = 0;
		}
		else if (table2 == 1)
		{
			cl = 0;
			id = 1;
			this.ChrominanceDC = new String[codecount][2];
			this.ChrominanceDCIndex = 0;
		}
		else if (table2 == 16)
		{
			cl = 1;
			id = 0;
			this.LuminanceAC = new String[codecount][2];
			this.LuminanceACIndex = 0;
		}
		else if (table2 == 17)
		{
			cl = 1;
			id = 1;
			this.ChrominanceAC = new String[codecount][2];
			this.ChrominanceACIndex = 0;
		}
		//create bits for each code
		TreeNode tree = new TreeNode(null, 0, this, table2, 0);
		
	}
	
	public void setCode(int table, String code, String bits)
	{
		if (table == 0)
		{
			this.LuminanceDC[this.LuminanceDCIndex][0] = bits;
			this.LuminanceDC[this.LuminanceDCIndex][1] = code;
			this.LuminanceDCIndex++;
		}
		else if (table == 1)
		{
			this.ChrominanceDC[this.ChrominanceDCIndex][0] = bits;
			this.ChrominanceDC[this.ChrominanceDCIndex][1] = code;
			this.ChrominanceDCIndex++;
		}
		else if (table == 16)
		{
			this.LuminanceAC[this.LuminanceACIndex][0] = bits;
			this.LuminanceAC[this.LuminanceACIndex][1] = code;
			this.LuminanceACIndex++;
		}
		else if (table == 17)
		{
			this.ChrominanceAC[this.ChrominanceACIndex][0] = bits;
			this.ChrominanceAC[this.ChrominanceACIndex][1] = code;
			this.ChrominanceACIndex++;
		}
	}
	
	public void printHuffmanTables()
	{
		System.out.println("Luminance DC:");
		for (int i = 0; i < this.LuminanceDC.length; i++)
		{
			System.out.println(this.LuminanceDC[i][0] + "|" + this.LuminanceDC[i][1]);
		}
		System.out.println("Luminance AC:");
		for (int i = 0; i < this.LuminanceAC.length; i++)
		{
			System.out.println(this.LuminanceAC[i][0] + "|" + this.LuminanceAC[i][1]);
		}
		System.out.println("Chrominance DC:");
		for (int i = 0; i < this.ChrominanceDC.length; i++)
		{
			System.out.println(this.ChrominanceDC[i][0] + "|" + this.ChrominanceDC[i][1]);
		}
		System.out.println("Chrominance AC:");
		for (int i = 0; i < this.ChrominanceAC.length; i++)
		{
			System.out.println(this.ChrominanceAC[i][0] + "|" + this.ChrominanceAC[i][1]);
		}
	}
}

class TreeNode
{
	TreeNode parent;
	TreeNode child0;
	TreeNode child1;
	
	TreeNode(TreeNode parent, int length, HuffmanTable hufftable, int table, int bits)
	{
		this.parent = parent;
		if (length < 17)
		{
			if(length > 0)
			{
				int codecount = hufftable.codes[length-1][0];
				if (codecount > 0)
				{
					int k = hufftable.codes[length-1].length - hufftable.codes[length-1][0];
					String codetopass = Integer.toString(hufftable.codes[length-1][k]);
					String bitstopass = convertToString(length, bits);
					hufftable.setCode(table, codetopass, bitstopass);
					hufftable.codes[length-1][0] -= 1;
				}
				else
				{
					this.child0 = new TreeNode(this, length+1, hufftable, table, bits*2);
					this.child1 = new TreeNode(this, length+1, hufftable, table, bits*2 + 1);
				}
			}
			else
			{
				this.child0 = new TreeNode(this, length+1, hufftable, table, bits*2);
				this.child1 = new TreeNode(this, length+1, hufftable, table, bits*2 + 1);
			}
		}
	}
	
	private String convertToString(int length, int bits)
	{
		StringBuffer x = new StringBuffer();
		while (bits > 0)
		{
			int k = bits % 2;
			x.insert(0, Integer.toString(k));
			
			bits = bits / 2;
		}
		while (length > x.length())
		{
			x.insert(0, "0");
		}
		
		return x.toString();
	}
}
