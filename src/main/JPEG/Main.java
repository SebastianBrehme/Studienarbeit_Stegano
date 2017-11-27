package main.JPEG;

import java.io.File;
import java.io.IOException;

import main.Util.BitConverter;

public class Main {

	public static void main(String[] args) throws IOException {
		FileParser x = new FileParser();
		
		//x.setFilePath("TestImages\\8auf8BK.jpg");
		x.setFilePath("TestImages\\huff_simple0.jpg");
		//x.setFilePath("D:\\dhbw\\Studienarbeit\\Bilder\\jpg\\oneBlack.jpg");
		//x.readFile();
		x.readFileBytes();
	}

}
