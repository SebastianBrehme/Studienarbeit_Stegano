package main.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class AsymmetricCryptography {
	private KeyPair key;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public AsymmetricCryptography() {
		this.key = this.generateAsymmetricKey();
		this.privateKey = key.getPrivate();
		this.publicKey = key.getPublic();
	}
	
	public AsymmetricCryptography(KeyPair key) {
		this.key = key;
		this.privateKey = key.getPrivate();
		this.publicKey = key.getPublic();
	}
	
	/*
	public AsymmetricCryptography(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	public AsymmetricCryptography(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	*/
	
	public AsymmetricCryptography(byte[] key, boolean publicKey) {
		X509EncodedKeySpec k = new X509EncodedKeySpec(key);
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			
			if(publicKey) {
				this.publicKey = kf.generatePublic(k);
			} else {
				this.privateKey = kf.generatePrivate(k);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getPublicKey() {
		return this.publicKey.getEncoded();
	}
	
	public byte[] getPrivateKey() {
		return this.privateKey.getEncoded();
	}
	
	private KeyPair generateAsymmetricKey() {
		int keySize = 1024;
		KeyPairGenerator gen;
		try {
			gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(keySize);
			return gen.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] encrypt(byte[] message) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
			return cipher.doFinal(message);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public byte[] decrypt(byte[] message) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
			return cipher.doFinal(message);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
}
