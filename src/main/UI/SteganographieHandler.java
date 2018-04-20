package main.UI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import main.JPEG.DCTMatrix;
import main.JPEG.JPEGParser;
import main.Util.EncryptedHidingWorkflow;
import main.crypto.SymmetricCryptography;
import main.steganography.ChangeEmbeder;
import main.steganography.DCTMatrixLSB;
import main.steganography.HiddenMessage;
import main.steganography.HiddenMessage.Type;

public class SteganographieHandler {

	public String imageInputPath;
	String outputFileSource;
	public String message = null;
	public String dataInputPath = null;
	String publicKey;
	
	String cryptoKey = null;
	public String stegoKey;
	
	String error;
	
	public SteganographieHandler() {
		error = "";
	}
	
	public boolean startSteganographie() {
		
		SymmetricCryptography crypto = null;
		
		if (cryptoKey == null) {
			crypto = new SymmetricCryptography();
			cryptoKey = new String(crypto.getKey());
		} 
	
		if (message != null) {
			EncryptedHidingWorkflow.hide(imageInputPath, cryptoKey, stegoKey, message);
		} else {
			EncryptedHidingWorkflow.hide(imageInputPath, cryptoKey, stegoKey, dataInputPath);
		}
		
		System.out.println("Hiding done!");
		return true;
	}
	
	public String retrieveDataOutOfImage(String filePath) {
		String strmsg = null;
		if (filePath == null) {
			strmsg = EncryptedHidingWorkflow.findMessage(imageInputPath,cryptoKey, stegoKey);
		} else {
			File foundFile = EncryptedHidingWorkflow.findFile(imageInputPath, cryptoKey, stegoKey, outputFileSource);
		}
		return strmsg;
	}
	
}
