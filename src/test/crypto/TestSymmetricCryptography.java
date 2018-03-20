package test.crypto;

import static org.junit.Assert.*;

import java.util.Base64.Encoder;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import org.junit.Test;

import main.crypto.SymmetricCryptography;

public class TestSymmetricCryptography {

	@Test
	public void testSymmetricCryptography() {
		SymmetricCryptography sc = new SymmetricCryptography();
		String message0 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer in hendrerit diam. Proin viverra elementum tincidunt."; 
		String message1 = "Nulla vitae aliquet diam. Quisque vestibulum lobortis bibendum. Mauris molestie diam mauris, vel rhoncus orci bibendum vitae. Nam aliquam augue ante, sit amet auctor neque vehicula at."; 
		String message2 = "Phasellus tellus est, congue eget enim in, placerat elementum mauris. Aliquam semper ligula sapien, id dignissim leo congue quis."; 
		String message3 = "Praesent blandit leo et risus vestibulum, gravida sollicitudin ex ultrices. Aliquam erat volutpat. Vestibulum sit amet tincidunt massa. Donec euismod erat eget urna sollicitudin luctus. Vestibulum mollis, dui ut dictum porta, magna leo placerat velit, non vulputate velit diam sed turpis."; 
		String message4 = "Duis vel dui pretium, porttitor elit ac, tempus libero. Praesent laoreet nunc sed odio tincidunt, rhoncus v"; 
		String message5 = "TeXH7rTwTQEnhCROSewBCWMnlJZxCxctyKj562M3WLZah4SGqRVGPC95w4kq7HZBOPZw1t3LFiYdTLufhnQzRKwVFpiqeOeszWkpGZ2O68JvuxfNW0ZVMxLVT2gjY8entfOinTuGgOGCXOFNcI7b85eJRrCN0xDYMVpaPdiFtyV44GVmpvouyUlKg9fV1GN0bwfkaihg"; 

		byte[] cipher0 = sc.encrypt(message0.getBytes());
		byte[] cipher1 = sc.encrypt(message1.getBytes());
		byte[] cipher2 = sc.encrypt(message2.getBytes());
		byte[] cipher3 = sc.encrypt(message3.getBytes());
		byte[] cipher4 = sc.encrypt(message4.getBytes());
		byte[] cipher5 = sc.encrypt(message5.getBytes());
		
		assertArrayEquals(message0.getBytes(), sc.decrypt(cipher0));
		assertArrayEquals(message1.getBytes(), sc.decrypt(cipher1));
		assertArrayEquals(message2.getBytes(), sc.decrypt(cipher2));
		assertArrayEquals(message3.getBytes(), sc.decrypt(cipher3));
		assertArrayEquals(message4.getBytes(), sc.decrypt(cipher4));
		assertArrayEquals(message5.getBytes(), sc.decrypt(cipher5));
	}
	
	@Test
	public void testSymmetricCryptography2() {		
		SymmetricCryptography sc = new SymmetricCryptography();
		String message = "Maecenas pharetra augue nec semper feugiat. Mauris hendrerit justo vehicula lectus rhoncus ornare.";
		
		byte[] cipher = sc.encrypt(message.getBytes());
		byte[] key = sc.getKey();
		
		SymmetricCryptography dec = new SymmetricCryptography(key);
		
		assertArrayEquals(message.getBytes(), dec.decrypt(cipher));
	}
}
