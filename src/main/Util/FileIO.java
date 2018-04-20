package test.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileIO {

	public static byte[] readFile(String path) throws IOException {
		return FileIO.readFile(new File(path));
	}
	
	public static byte[] readFile(File file) throws IOException {
		
		FileInputStream fstream = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		int length = fstream.read(data);
		if(length != file.length()) {
			System.err.println("Hidden File Read Error - length difference");
		}
		System.out.println(length);
		//System.out.println(Arrays.toString(data));
		fstream.close();
		return data;
	}
	
	public static void writeFile(String path, byte[] data) throws IOException {
		FileIO.writeFile(new File(path), data);
	}
	
	public static void writeFile(File file, byte[] data) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fstream = new FileOutputStream(file);
		fstream.write(data);
		fstream.flush();
		fstream.close();		
	}
	
}
