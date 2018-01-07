package main.JPEG;

import java.io.File;
import java.io.IOException;
import java.util.List;

import main.LSB.DCTMatrixImpl;
import main.LSB.DCTMatrixLSB;
import main.LSB.HiddenMessage;
import main.Util.BitConverter;

public class Main {

	public static void main(String[] args) throws IOException {		
		FileParser fileparser = new FileParser();
		
		//x.setFilePath("TestImages\\8auf8BK.jpg");
		fileparser.setFilePath("TestImages"+File.separator+"cat.jpg");
		//x.setFilePath("TestImages"+File.separator+"8auf8BK.jpg");
		//x.setFilePath("TestImages"+File.separator+"TestImage.jpg");
		//x.setFilePath("TestImages"+File.separator+"TestImage2.jpg");
		//x.setFilePath("TestImages"+File.separator+"TestImage3.jpg");
		//x.readFile();
		
		fileparser.readFileBytes();
		fileparser.createHuffmanTables();
		//fileparser.huffTables.printHuffmanTables();
		HuffmanDecode hc = fileparser.processImageData();
		//fileparser.printMatrix(hc);
		System.out.println("Done!");
		DCTMatrixLSB hider = new DCTMatrixImpl();
		
		List<List<DCTMatrix>> withMessage = hider.hideMessage(hc.getDecodedData(), new HiddenMessage());
		
		HuffmanEncode huffencode = new HuffmanEncode(withMessage, hc.huffmanTable, hc.imageData);
		huffencode.encode();
		fileparser.writeFileBytes(huffencode);
	}

}