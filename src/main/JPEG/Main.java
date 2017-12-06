package main.JPEG;

import java.io.File;
import java.io.IOException;

import main.Util.BitConverter;

public class Main {

	public static void main(String[] args) throws IOException {
		FileParser x = new FileParser();
		
		//x.setFilePath("TestImages\\8auf8BK.jpg");
		x.setFilePath("TestImages"+File.separator+"huff_simple0.jpg");
		//x.setFilePath("TestImages"+File.separator+"8auf8BK.jpg");
		//x.setFilePath("TestImages"+File.separator+"TestImage.jpg");
		//x.setFilePath("TestImages"+File.separator+"TestImage2.jpg");
		//x.setFilePath("TestImages"+File.separator+"TestImage3.jpg");
		//x.readFile();
		x.readFileBytes();
		x.createHuffmanTables();
		x.huffTables.printHuffmanTables();
		HuffmanCode hc = x.processImageData();
		x.printMatrix(hc);
		System.out.println("Done!");
	}

}
