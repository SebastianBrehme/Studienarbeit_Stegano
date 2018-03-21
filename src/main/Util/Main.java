package main.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import test.Util.FileIO;

public class Main {

	public static void main(String[] args) throws IOException {
		//byte[] data = FileIO.readFile("deutsch.txt");
		//FileIO.writeFile("test2.txt", data);
		//System.out.println("Started");
		
		Main.hidemymessage();
		Main.getmymessage();

//		char zeichen = '.';
//		int versuche = 0;
//		int fail = 0;
//
//		List<List<DCTMatrix>> withMessage = null;
//
//		FileParser fileparser = new FileParser();
//
//		fileparser.setFilePath("TestImages" + File.separator + "cat.jpg");
//		System.gc();
//		fileparser.readFileBytes();
//		System.gc();
//		fileparser.createHuffmanTables();
//		System.gc();
//		HuffmanDecode hc = fileparser.processImageData();
//		System.gc();
//		// System.out.println("Done Processing!");
//		
//		File keyList = new File("deutsch - Kopie.txt");
//		BufferedReader reader = new BufferedReader(new FileReader(keyList));
//		String key = reader.readLine();
//		// System.out.println(msg);
//		// String key = "Abfuellfilialen";
//		Histogram.createHistogram(hc.getDecodedData());
//		while (key != null) {
//			DCTMatrixLSB hider = new ChangeEmbeder();
//			HiddenMessage msg = new HiddenMessage(key.getBytes());
//			List<List<DCTMatrix>> work = hc.getDecodedData();//Main.cloneList(hc.getDecodedData());
//			versuche++;
//			// System.out.println(key);
//			
//			withMessage = hider.hideMessageWithKey(work, msg, "test");
//			HiddenMessage hdmsg = new HiddenMessage();
//			
//			if (withMessage != null) {
//				hdmsg = hider.recoverMessageWithKey(withMessage, "test");
//			}
//
//			// System.out.println("Nachrichten sind gleich? - "+hdmsg.equals(msg));
//			if (!hdmsg.equals(msg)) {
//				 System.err.println("FEHLER");
//				 System.out.println("Original: " + msg);
//				 System.out.println("Entdeckte Nachricht: " + hdmsg);
//				System.out.println("Key: " + key);
//				fail++;
//				System.out.printf("Versuche %d; Erfolg: %d, Fail: %d; Quote %f\n", versuche, versuche - fail, fail,
//						(double) (versuche - fail) / versuche);
//				// System.exit(8);
//			}
//			key = reader.readLine();
//
//			try {
//				char first = key.toLowerCase().charAt(0);
//				if (first != zeichen) {
//					System.out.println(first);
//					zeichen = first;
//				}
//			} catch (Exception e) {
//
//			}
//			 key = null;
//		}
//
//		System.out.printf("Versuche %d; Erfolg: %d, Fail: %d; Quote %f\n", versuche, versuche - fail, fail,
//				(double) (versuche - fail) / versuche);

		// Histogram.createHistogram(withMessage);
		// HiddenMessage uncover = hider.recoverMessage(withMessage);
		// System.out.println(uncover);

		// System.out.println("message Embedded - bevor encoding");
		// HuffmanEncode huffencode = new HuffmanEncode(withMessage, hc.huffmanTable,
		// hc.imageData);
		// huffencode.encode();
		// System.gc();
		// fileparser.writeFileBytes(huffencode);
		// System.gc();

		// ImageCompare.compareImages("TestImages" + File.separator + "cat.jpg",
		// "TestImages" + File.separator + "cat_new.jpg",
		// DiffImageMode.AbsolutDiffImage, 2);

		System.out.println("Write Done");
	}
	
	public static void hidemymessage() throws IOException {
		JPEGParser parser = new JPEGParser("cat.jpg");
		List<List<DCTMatrix>> dct_data = parser.readJpegFile();
	
		
		DCTMatrixLSB hider = new ChangeEmbeder();
		
		String strmsg = "meine total geheime Nachricht";
		System.out.println(strmsg);
		SymmetricCryptography crypto = new SymmetricCryptography("meinsuperkeyhier".getBytes());
		byte[] data = crypto.encrypt(strmsg.getBytes());
		
		HiddenMessage msg = new HiddenMessage(data, Type.TEXT);
		List<List<DCTMatrix>> work = dct_data;//Main.cloneList(hc.getDecodedData())
		work = hider.hideMessageWithKey(work, msg, "test");
		
		System.out.println("message Embedded - bevor encoding");		
		parser.writeJpegFile(work);		
		System.out.println("Write Done");
	}
	
	public static void getmymessage() throws IOException {		
		JPEGParser parser = new JPEGParser("cat_new.jpg");
		List<List<DCTMatrix>> data = parser.readJpegFile();
		
		DCTMatrixLSB hider = new ChangeEmbeder();
		HiddenMessage msg = hider.recoverMessageWithKey(data, "test");
		SymmetricCryptography crypto = new SymmetricCryptography("meinsuperkeyhier".getBytes());
		String strmsg = new String(crypto.decrypt(msg.getData()));
		System.out.println(strmsg);
		System.out.println("Finish");
		System.exit(0);
	}

	public static List<List<DCTMatrix>> cloneList(List<List<DCTMatrix>> org) {
		List<List<DCTMatrix>> clone = new ArrayList<List<DCTMatrix>>();
		for (List<DCTMatrix> temp : org) {
			List<DCTMatrix> tclone = new ArrayList<DCTMatrix>();
			for (DCTMatrix mt : temp) {
				tclone.add(mt.clone());
			}
			clone.add(tclone);
		}
		return clone;
	}

	public static void waitTill2() {
		System.out.println("...press 2...");
		try {
			while (System.in.read() != 50) {
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println("!!!");
	}

}