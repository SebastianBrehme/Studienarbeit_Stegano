package JPEG;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		FileParser x = new FileParser();
		x.setFilePath("D:\\dhbw\\Studienarbeit\\Bilder\\jpg\\huff_simple0.jpg");
		//x.setFilePath("D:\\dhbw\\Studienarbeit\\Bilder\\jpg\\oneBlack.jpg");
		//x.readFile();
		x.readFileBytes();
	}

}
