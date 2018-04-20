package main.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Analysis.Histogram;
import main.Analysis.ImageCompare;
import main.Analysis.ImageCompare.DiffImageMode;
import main.JPEG.DCTMatrix;
import main.JPEG.FileParser;
import main.JPEG.HuffmanDecode;
import main.JPEG.HuffmanEncode;
import main.JPEG.JPEGParser;
import main.crypto.SymmetricCryptography;
import main.steganography.ChangeEmbeder;
import main.steganography.DCTMatrixLSB;
import main.steganography.HiddenMessage;
import main.steganography.HiddenMessage.Type;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String ckey = "0000111122223333";
		String skey = "stegokey";
		
		EncryptedHidingWorkflow.hide("TestImages\\cat.jpg", ckey, skey, "A very successfull workflow");
		System.out.println("hidden...try to recover...");
		String message = EncryptedHidingWorkflow.findMessage("TestImages\\cat_new.jpg",ckey, skey);
		System.out.println(message);

		
		File file = new File("Histogram.class");
		EncryptedHidingWorkflow.hide("TestImages\\cat.jpg", ckey, skey, file);
		System.out.println("hidden...try to recover...");
		File foundFile = EncryptedHidingWorkflow.findFile("TestImages\\cat_new.jpg", ckey, skey, "D:\\Programme\\Entwicklung\\workspaces\\workspaceStudienarbeit\\Source\\Studienarbeit_Stegano");
		System.out.println(foundFile.getName());
		
		//ImageCompare.compareImages("TestImages\\Work" + File.separator + "squareU.jpg",
		//		"TestImages" + File.separator + "squareU_new.jpg", DiffImageMode.AbsolutDiffImage, 2);

		System.out.println("Done");
	}
}