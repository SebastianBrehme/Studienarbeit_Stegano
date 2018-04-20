package main.Util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import main.JPEG.DCTMatrix;
import main.JPEG.JPEGParser;
import main.crypto.SymmetricCryptography;
import main.steganography.ChangeEmbeder;
import main.steganography.DCTMatrixLSB;
import main.steganography.HiddenMessage;
import main.steganography.HiddenMessage.Type;

public class EncryptedHidingWorkflow {
	
	public static void hide(String imagePath, String cryptoKey, String stegoKey, String message) {
		JPEGParser parser = new JPEGParser(imagePath);
		DCTMatrixLSB hider = new ChangeEmbeder();
		SymmetricCryptography crypto = new SymmetricCryptography(cryptoKey.getBytes());
		
		
		List<List<DCTMatrix>> dct_data = parser.readJpegFile();		
		byte[] data = crypto.encrypt(message.getBytes());

		HiddenMessage hiddenmessage = new HiddenMessage(data, Type.TEXT);//, "histo.class");// Type.TEXT);
		dct_data = hider.hideMessageWithKey(dct_data, hiddenmessage, stegoKey);

		parser.writeJpegFile(dct_data);
	}
	
	public static String findMessage(String imagePath, String cryptoKey, String stegoKey) {
		JPEGParser parser = new JPEGParser(imagePath);
		DCTMatrixLSB finder = new ChangeEmbeder();
		SymmetricCryptography crypto = new SymmetricCryptography(cryptoKey.getBytes());
		
		List<List<DCTMatrix>> dct_data = parser.readJpegFile();		
		
		HiddenMessage hiddenMessage = finder.recoverMessageWithKey(dct_data,stegoKey);
		
		byte data[] = crypto.decrypt(hiddenMessage.getData());
		
		File toDelete = new File(imagePath.substring(0, imagePath.lastIndexOf(".")) + "_new.jpg");
		toDelete.delete();
		
		return new String(data);
	}
	
	public static void hide(String imagePath, String cryptoKey, String stegoKey, File file) {
		JPEGParser parser = new JPEGParser(imagePath);
		DCTMatrixLSB hider = new ChangeEmbeder();		
		SymmetricCryptography crypto = new SymmetricCryptography(cryptoKey.getBytes());
		
		List<List<DCTMatrix>> dct_data = parser.readJpegFile();		
		
		byte[] data = FileIO.readFile(file);
		data = crypto.encrypt(data);
		byte[] filename = crypto.encrypt(file.getName().getBytes());
		HiddenMessage hiddenmessage = new HiddenMessage(data, filename);
		dct_data = hider.hideMessageWithKey(dct_data, hiddenmessage, stegoKey);
		
		parser.writeJpegFile(dct_data);
	}
	
	public static File findFile(String imagePath, String cryptoKey, String stegoKey, String filePath) {
		JPEGParser parser = new JPEGParser(imagePath);
		DCTMatrixLSB hider = new ChangeEmbeder();		
		SymmetricCryptography crypto = new SymmetricCryptography(cryptoKey.getBytes());		
		
		List<List<DCTMatrix>> dct_data = parser.readJpegFile();		
		HiddenMessage hiddenMessage = hider.recoverMessageWithKey(dct_data, stegoKey);
		byte[] data = crypto.decrypt(hiddenMessage.getData());
		byte[] fileName = crypto.decrypt(hiddenMessage.filename);
		File file = new File(filePath+File.separator+new String(fileName));
		FileIO.writeFile(file, data);
		
		File toDelete = new File(imagePath.substring(0, imagePath.lastIndexOf(".")) + "_new.jpg");
		toDelete.delete();
		
		return file;
	}
}
