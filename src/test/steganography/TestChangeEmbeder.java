package test.steganography;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.JPEG.DCTMatrix;
import main.JPEG.Matrix;
import main.crypto.SymmetricCryptography;
import main.steganography.ChangeEmbeder;
import main.steganography.HiddenMessage;
import main.steganography.HiddenMessage.Type;

public class TestChangeEmbeder {
	
	ChangeEmbeder embeder;
	final String crypto_key = "meinsuperKeyHIER";
	final String stego_key = "super anderer key";
	final String strmsg = "tolle Nachricht #super! 5+5*3-0/8, >|<,_-";
	HiddenMessage msg;
	List<List<DCTMatrix>> imageData;

	@Before
	public void setUp() throws Exception {
		imageData = generateMatrixData();
		embeder = new ChangeEmbeder();	
		msg = new HiddenMessage(strmsg.getBytes(), Type.TEXT);
	}

	@After
	public void tearDown() throws Exception {
		embeder = null;
		imageData = null;
		msg = null;
	}

	@Test
	public void testChangeEmbederWithKey() {
		
		msg = this.encrypt(msg);
		List<List<DCTMatrix>> work = embeder.hideMessageWithKey(imageData, msg, this.stego_key);
		HiddenMessage plainMsg = embeder.recoverMessageWithKey(work, this.stego_key);
		plainMsg = this.encrypt(plainMsg);
		assertEquals(msg, plainMsg);
	}	
	
	private HiddenMessage encrypt(HiddenMessage msg) {
		SymmetricCryptography crypto = new SymmetricCryptography(this.crypto_key.getBytes());
		byte[] data = crypto.encrypt(strmsg.getBytes());
		return new HiddenMessage(data, msg.getType());
	}
	
	private HiddenMessage decrypt(HiddenMessage msg) {
		SymmetricCryptography crypto = new SymmetricCryptography(this.crypto_key.getBytes());
		byte[] data = crypto.decrypt(strmsg.getBytes());
		return new HiddenMessage(data, msg.getType());
	}
	
	
	private List<List<DCTMatrix>> generateMatrixData(){
		List<List<DCTMatrix>> result = new ArrayList<List<DCTMatrix>>();
		for(int i=0;i<3;i++) {
			List<DCTMatrix> temp = new ArrayList<DCTMatrix>();
			for(int j=0;j<3;j++) {
								temp.add(generateMatrix());
			}
			result.add(temp);
		}
		return result;
	}
	
	private DCTMatrix generateMatrix() {
		DCTMatrix matrix = new DCTMatrix(Matrix.LUMINANCE, 8,8);
		for(int i=0;i<64;i++) {
			matrix.setValue(i, i);
		}
		return matrix;
	}

}
