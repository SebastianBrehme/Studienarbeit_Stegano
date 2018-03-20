package test.crypto;

import org.junit.Test;

import main.crypto.AsymmetricCryptography;
import main.crypto.SymmetricCryptography;

import static org.junit.Assert.*;

public class TestHybridCryptography {

	@Test
	public void testHybridCrypthography() {
		//Receiver
		AsymmetricCryptography asymCr = new AsymmetricCryptography();
		byte[] publicKey = asymCr.getPublicKey();
		
		
		//Sender
		SymmetricCryptography symCr = new SymmetricCryptography();
		String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer in hendrerit diam. Proin viverra elementum tincidunt."
					+ "Maecenas pharetra augue nec semper feugiat. Mauris hendrerit justo vehicula lectus rhoncus ornare."; 
		byte[] cipher = symCr.encrypt(message.getBytes());
		
		byte[] symmetricKey = symCr.getKey();
		AsymmetricCryptography encryptKey = new AsymmetricCryptography(publicKey, true);
		byte[] cipherKey = encryptKey.encrypt(symmetricKey);
		
		
		//Receiver
		byte[] decryptedKey = asymCr.decrypt(cipherKey);
		SymmetricCryptography decrypt = new SymmetricCryptography(decryptedKey);
		byte[] decryptedMessage = decrypt.decrypt(cipher);
		
		assertArrayEquals(symmetricKey, decryptedKey);
		assertArrayEquals(message.getBytes(), decryptedMessage);
	}
}
