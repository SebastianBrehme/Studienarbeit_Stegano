package test.steganography;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import main.JPEG.DCTMatrix;
import main.JPEG.Matrix;
import main.crypto.SymmetricCryptography;
import main.steganography.ChangeEmbeder;
import main.steganography.HiddenMessage;
import main.steganography.HiddenMessage.Type;

@RunWith(Parameterized.class)
public class TestChangeEmbeder {
	
	ChangeEmbeder embeder;
	final String crypto_key;// = "meinsuperKeyHIER";
	final String stego_key;// = "super anderer key";
	final String strmsg;// = "tolle Nachricht #super! 5+5*3-0/8, >|<,_-";
	HiddenMessage hiddenmsg;
	List<List<DCTMatrix>> imageData;
	
	@Parameters
	public static List<Object[]> data(){
		return Arrays.asList(new Object[][] {
			{"meinsuperKeyHIER","super hprf34EOUf key",""},
			{"meinsuperKeyHIER","super hprf34EOUf key","tolle Nachricht #super! 5+5*3-0/8, >|<,_-"},
			{"1a1rkxuw56H6Q4xS","DGu68JPT6b anderer key","tolle Nachricht #super! 5+5*3-0/8, >|<,_-WFuM6oU80Zvgpe88Ysq8"},
			{"xWsQtp7UiW9pDGHE","superbgHtuzgS7R anderer sydRtDGt7d","tolle 1aWrHniT1f2FfulQksTvNachricht #super! 5+5*3-0/8, >|<,_-"},
			{"eoqOb3Z2O2EKKWHc","super andt5W9E60ngUerer key","tV3pxS3M442mizv8iDz4yolle Nachricht #super! 5+5*3-0/8, >|<,_-"},
			{"tJMp9pCoeRHKMw7M","sSrNHkmFYGbuper anderer key","tolle Nachricht #super! 5iypscP4bckNZweydLxOr+5*3-0/8, >|<,_-"},
			{"5lPx2Qx4x0vn66UJ","super anderer keydDP7CJgCV4","tolle Nachricht #super! 5+5*3-0/8, >JcXzt0qEmw1SqJQAOFdu|<,_-"},
			{"JOPJeZBn1IRXXGPO","super anderer key","tolle Nachricht #sogdsmXCwD1ClaqJUP3e0uper! 5+5*3-0/8, >|<,_-"},
			{"PAZSCFvWmg2PXHwa","super anderer prYG0qFwSWkey","tolle Nachnu0IdrDE04RqO7DS7Stlricht #super! 5+5*3-0/8, >|<,_-"},
			{"uT8ielqtRtUqZfoR","super and5sVlCaFzD8erer key","tolle Nachricht #super! 5+5*3-0/8, >|<,_WjvHg9NrxpbCB2mN9jcp-"},
			{"tVn9fXRuMvlUWW6Q","super annkwyZ4XYYLderer key","tolle NachrifQKJ3RDuLkwJBMcQL1vAcht #super! 5+5*3-0/8, >|<,_-"},
			{"z7vQeLsa85auGAU1","super andereZWdsrxknWsr key","tolle Nachricht #super! 5+5GliCkk7kXsocZfvwqbgc*3-0/8, >|<,_-"},
			{"aNniwTsklvo2fEPp","supeAEHSPXzSnBr anderer key","tolle NaEHqieh6SV38sHTlFPSe1chricht #super! 5+5*3-0/8, >|<,_-"},
			{"57AHNglORRqNQPH3","super andAEHSPXzSnBerer key","tolle NachricBkCKHctKf3ce4KqDW26Xht #super! 5+5*3-0/8, >|<,_-"},
			{"eGVpyULOFEpxN0SQ","su","tolle Nachricht1oaljluuQzgOKsUZqA9G #super! 5+5*3-0/8, >|<,_-"},
			{"ykuimd3APvoHlhE8","super andVFZAq7HBmMerer key","tolle Nachricht #super! 5+5*3-0/8, >|<z9dxdGUzzqV6fbrqw0Pb,_-"},
			{"oQqT2h2PcUjwtHkh","super anderer key","tolle NacheFzXcgywT39afh9T6TxNricht #super! 5+5*3-0/8, >|<,_-"},
			{"SRdiQK9Z2awsu3uM","super anderer key","tolle Nachricht #super!Kc202y92UTdMP4COUk4k 5+5*3-0/8, >|<,_-"},
			{"HGIY4cyfbBtfQ8ZZ","supegYqsx11iUgr anderer key","tolle Nachricht #super! 5+5*3-069HhTHdCw5sWJ1jwBtc0/8, >|<,_-"},
			{"oOOGZWmisWQTI1zn","super hhOZL726HmrM6Hqanderer key","tolle NachqNhl0FnfADFFtF45Tadwricht #super! 5+5*3-0/8, >|<,_-"},
			{"ZbCrsgJpAebqUTJe","super anhhOZL726HmrM6Hqderer key","tolle Nachricht #super! 5+5*3-0/8, VPOSLjwbX1IaKtXYUH4y>|<,_-"},
			{"d9m7jU0sSHDATblM","super RLVrlvvKAjHUS6x key","tolla8TwzcEJGlN4GRC0VZBte Nachricht #super! 5+5*3-0/8, >|<,_-"}
		});
	}	
	
	public TestChangeEmbeder(String cryptoKey, String stegoKey, String msg) {
		this.crypto_key = cryptoKey;
		this.stego_key = stegoKey;
		this.strmsg = msg;
	}

	@Before
	public void setUp() throws Exception {
		imageData = generateMatrixData();
		embeder = new ChangeEmbeder();	
		hiddenmsg = new HiddenMessage(strmsg.getBytes(), Type.TEXT);
	}

	@After
	public void tearDown() throws Exception {
		embeder = null;
		imageData = null;
		hiddenmsg = null;
	}

	@Test
	public void testChangeEmbederWithKey() {		
		HiddenMessage msg = this.encrypt(hiddenmsg);
		List<List<DCTMatrix>> work = embeder.hideMessageWithKey(imageData, msg, this.stego_key);
		HiddenMessage plainMsg = embeder.recoverMessageWithKey(work, this.stego_key);
		plainMsg = this.decrypt(plainMsg);
		assertEquals(hiddenmsg, plainMsg);
	}	
	
	private HiddenMessage encrypt(HiddenMessage msg) {
		SymmetricCryptography crypto = new SymmetricCryptography(this.crypto_key.getBytes());
		byte[] data = crypto.encrypt(msg.getData());
		return new HiddenMessage(data, msg.getType());
	}
	
	private HiddenMessage decrypt(HiddenMessage msg) {
		SymmetricCryptography crypto = new SymmetricCryptography(this.crypto_key.getBytes());
		byte[] data = crypto.decrypt(msg.getData());
		return new HiddenMessage(data, msg.getType());
	}
	
	
	private List<List<DCTMatrix>> generateMatrixData(){
		List<List<DCTMatrix>> result = new ArrayList<List<DCTMatrix>>();
		for(int i=0;i<4;i++) {
			List<DCTMatrix> temp = new ArrayList<DCTMatrix>();
			for(int j=0;j<4;j++) {
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
