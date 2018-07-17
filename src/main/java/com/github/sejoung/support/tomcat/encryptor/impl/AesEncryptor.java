package com.github.sejoung.support.tomcat.encryptor.impl;

import com.github.sejoung.support.tomcat.encryptor.Encryptor;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES 암호화
 *
 */
public class AesEncryptor implements Encryptor {
	private static final String ALGORITHM = "AES";
	private static final String defaultSecretKey = "killers";
	private Key secretKeySpec;

	public AesEncryptor() {
		this(null);
	}

	/**
	 * 단축키
	 * @param secretKey
	 */
	public AesEncryptor(String secretKey) {
		this.secretKeySpec = generateKey(secretKey);
	}

	public String encrypt(String plainText) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(1, this.secretKeySpec);
		return asHexString(cipher.doFinal(plainText.getBytes("UTF-8")));
	}

	public String decrypt(String encryptedString) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(2, this.secretKeySpec);
		return new String(cipher.doFinal(toByteArray(encryptedString)));
	}

	private Key generateKey(String secretKey) {
		if (secretKey == null) {
			secretKey = defaultSecretKey;
		}
		try {
			byte[] key = secretKey.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);

			KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
			kgen.init(128);

			return new SecretKeySpec(key, ALGORITHM);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final String asHexString(byte[] buf) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		for (int i = 0; i < buf.length; i++) {
			if ((buf[i] & 0xFF) < 16) {
				strbuf.append("0");
			}
			strbuf.append(Long.toString(buf[i] & 0xFF, 16));
		}
		return strbuf.toString();
	}

	private final byte[] toByteArray(String hexString) {
		int arrLength = hexString.length() >> 1;
		byte[] buf = new byte[arrLength];
		for (int ii = 0; ii < arrLength; ii++) {
			int index = ii << 1;
			String l_digit = hexString.substring(index, index + 2);
			buf[ii] = ((byte) Integer.parseInt(l_digit, 16));
		}
		return buf;
	}

	public static void main(String[] args) throws Exception {
		if ((args.length == 1) || (args.length == 2)) {
			String secretKey = args.length == 2 ? args[1] : null;
			System.out.println(args[0] + ": " + new AesEncryptor(secretKey).encrypt(args[0]));
		} else {
			System.out.println("USAGE: java Encryptor string-to-encrypt [secretKey]");
		}
	}
}
