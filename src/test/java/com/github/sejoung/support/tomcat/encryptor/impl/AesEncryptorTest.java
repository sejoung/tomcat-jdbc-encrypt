package com.github.sejoung.support.tomcat.encryptor.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import junit.framework.TestCase;

public class AesEncryptorTest extends TestCase {

	public void testEncrypt()
			throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		AesEncryptor ae = new AesEncryptor("test");
		String encryptedString = ae.encrypt("java");
		String decryptedString = ae.decrypt(encryptedString);
		System.out.println(decryptedString + ": " + encryptedString);
	}

	public void testDecrypt() {
	}
}