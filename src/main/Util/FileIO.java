package main.Util;

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

	public static byte[] readFile(File file) {

		try {
			FileInputStream fstream = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			int length = fstream.read(data);
			if (length != file.length()) {
				System.err.println("Hidden File Read Error - length difference");
			}
			fstream.close();
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static void writeFile(String path, byte[] data) throws IOException {
		FileIO.writeFile(new File(path), data);
	}

	public static void writeFile(File file, byte[] data) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fstream = new FileOutputStream(file);
			fstream.write(data);
			fstream.flush();
			fstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
