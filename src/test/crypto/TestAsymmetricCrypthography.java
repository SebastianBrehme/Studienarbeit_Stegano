package test.crypto;

import org.junit.Test;
import static org.junit.Assert.*;

import main.crypto.AsymmetricCryptography;

public class TestAsymmetricCrypthography {

	@Test
	public void testAsymmetricCrypthography() {
		AsymmetricCryptography cr = new AsymmetricCryptography();
		String message0 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer in hendrerit diam. Proin viverra elementum tincid"; 
		String message1 = "Nulla vitae aliquet diam. Quisque vestibulum lobortis bibendum. Mauris molestie diam mauris, vel rhoncus orci "; 
		String message2 = "Phasellus tellus est, congue eget enim in, placerat elementum mauris. Aliquam semper ligula sapien, id."; 
		String message3 = "Praesent blandit leo et risus vestibulum, gravida sollicitudin ex ultrices. Aliquam erat volutpat. Vestibulum ."; 
		String message4 = "Duis vel dui pretium, porttitor elit ac, tempus libero. Praesent laoreet nunc sed odio tincidunt, rhoncus "; 
		String message5 = "TeXH7rTwTQEnhCROSewBCWMnlJZxCxctyKj562M3WLZah4SGqRVGPC95w4kq7HZBOPZw1t3LFiYdTLufhnQzRKwVFpiqeOeszWkpGZ2O68Jvux"; 

		byte[] cipher0 = cr.encrypt(message0.getBytes());
		byte[] cipher1 = cr.encrypt(message1.getBytes());
		byte[] cipher2 = cr.encrypt(message2.getBytes());
		byte[] cipher3 = cr.encrypt(message3.getBytes());
		byte[] cipher4 = cr.encrypt(message4.getBytes());
		byte[] cipher5 = cr.encrypt(message5.getBytes());
		
		assertArrayEquals(message0.getBytes(), cr.decrypt(cipher0));
		assertArrayEquals(message1.getBytes(), cr.decrypt(cipher1));
		assertArrayEquals(message2.getBytes(), cr.decrypt(cipher2));
		assertArrayEquals(message3.getBytes(), cr.decrypt(cipher3));
		assertArrayEquals(message4.getBytes(), cr.decrypt(cipher4));
		assertArrayEquals(message5.getBytes(), cr.decrypt(cipher5));
	}
	
	@Test
	public void testAsymmetricCrypthography2() {
		AsymmetricCryptography cr = new AsymmetricCryptography();
		String message = "Maecenas pharetra augue nec semper feugiat. Mauris hendrerit justo vehicula lectus rhoncus ornare.";
		
		byte[] key = cr.getPublicKey();
		
		AsymmetricCryptography encr = new AsymmetricCryptography(key, true);
		byte[] cipher = encr.encrypt(message.getBytes());
		
		assertArrayEquals(message.getBytes(), cr.decrypt(cipher));
	}
}
