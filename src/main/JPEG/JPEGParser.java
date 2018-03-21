package main.JPEG;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JPEGParser {

	String path;

	private HuffmanDecode decoder;
	FileParser fileparser;
	
	public JPEGParser(String path) {
		this.path = path;
	}

	public List<List<DCTMatrix>> readJpegFile() {
		try {
			fileparser = new FileParser();
			fileparser.setFilePath("TestImages" + File.separator + this.path);
			System.gc();
			fileparser.readFileBytes();
			System.gc();
			fileparser.createHuffmanTables();
			System.gc();
			decoder = fileparser.processImageData();
			System.gc();

			return decoder.getDecodedData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; //should never be reached;
	}

	
	public void writeJpegFile(List<List<DCTMatrix>> matrix) {
		HuffmanEncode huffencode = new HuffmanEncode(matrix, decoder.huffmanTable, decoder.imageData);
		huffencode.encode();
		try {
			fileparser.writeFileBytes(huffencode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
