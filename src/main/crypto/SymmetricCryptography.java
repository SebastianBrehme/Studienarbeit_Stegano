package main.crypto;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricCryptography {

	private byte[] key;
	
	public SymmetricCryptography(){
		this.key = this.generateSymmetricKey().getEncoded();
	}
	
	public SymmetricCryptography(SecretKey key) {
		this.key = key.getEncoded();
	}
	
	public SymmetricCryptography(byte[] key) {
		this.key = key;
	}
	
	private SecretKey generateSymmetricKey() {
		try {
			int keySize = 128;
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(keySize);
			return gen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] getKey() {
		return this.key;
	}
	
	public String getMessageString(byte[] message) {
		java.util.Base64.Encoder enc = java.util.Base64.getEncoder();
		return new String(enc.encode(message));
	}
	
	public byte[] encrypt(byte[] message) {
		SecretKeySpec keySpec = new SecretKeySpec(this.key, "AES");
		
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			return cipher.doFinal(message);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	
	public byte[] decrypt(byte[] message) {
		SecretKeySpec keySpec = new SecretKeySpec(this.key, "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			return cipher.doFinal(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
