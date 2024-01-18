package com.simplesoft.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 암호화,복호화 Utils
 */
@Component
public class EncryptUtils {

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static String KEY = "bsdhsdudghksehddhksxhdgkqjeonsan";
	private static final int MAX_SECOND = 300; // 5분
	public static byte[] ivBytes = new byte[16];

	

	/**
	 * SHA-256 암호화
	 */
	public static byte[] sha256Encode(String msg) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(msg.getBytes());

			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * SHA-256 Byte to String
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	/**
	 * SHA-256 암호화
	 */
	public static String sha256Hashing(String msg) {
		return bytesToHex(sha256Encode(msg));
	}

	/**
	 * AES 복호화
	 */
	public static String decrypt(final String text) throws Exception {
		byte[] results;
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			byte[] keyBytes = new byte[16];
			byte[] getKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
			int len = getKeyBytes.length;
			if (len > keyBytes.length) {
				len = keyBytes.length;
			}
			System.arraycopy(getKeyBytes, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			results = cipher.doFinal(Base64.decodeBase64(text));

			return new String(results, StandardCharsets.UTF_8);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			// throw new Exception(e);
			return "-1";
		}
	}

	/**
	 * AES 암호화
	 */
	public static String encrypt(final String text) throws Exception {
		String results;
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			byte[] keyBytes = new byte[16];
			byte[] getKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
			int len = getKeyBytes.length;
			if (len > keyBytes.length) {
				len = keyBytes.length;
			}
			System.arraycopy(getKeyBytes, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			results = Base64.encodeBase64String(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new Exception(e);
		}
		return results;
	}

	/**
	 * 시간제한 복호화
	 */
	public static String decryptT(final String start, final String cipherText) throws Exception {
		if (!"".equals(start))
			gap(start);

		byte[] results;
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			byte[] keyBytes = new byte[16];
			byte[] getKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
			int len = getKeyBytes.length;
			if (len > keyBytes.length) {
				len = keyBytes.length;
			}
			System.arraycopy(getKeyBytes, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			results = cipher.doFinal(Base64.decodeBase64(cipherText));

			return new String(results, StandardCharsets.UTF_8);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			// throw new Exception(e);
			return "-1";
		}
	}

	/**
	 * 시간제한 암호화 함수
	 */
	public static String encryptT(final String start, final String plainText) throws Exception {
		if (!"".equals(start))
			gap(start);
		String results;
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			byte[] keyBytes = new byte[16];
			byte[] getKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
			int len = getKeyBytes.length;
			if (len > keyBytes.length) {
				len = keyBytes.length;
			}
			System.arraycopy(getKeyBytes, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			results = Base64.encodeBase64String(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new Exception(e);
		}
		return results;
	}

	/**
	 * 암호화 인증 유효시간 확인
	 */
	private static void gap(final String start) throws Exception {
		long end = System.currentTimeMillis() / 1000;

		if (end - Long.parseLong(start) > MAX_SECOND) {
			throw new Exception("인증 유효 시간이 경과 하였습니다.");
		}
	}

	/**
	 * AES 256 암호화
	 */
	public static String AES256_Encrypt(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, IOException, NoSuchProviderException {
		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
		cipher.init(1, newKey, ivSpec);
		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}

	/**
	 * AES 256 복호화
	 */
	public static String AES256_Decrypt(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, IOException, NoSuchProviderException {
		byte[] textBytes = Base64.decodeBase64(str);
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
		cipher.init(2, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}

	/**
	 * AES 256 복호화
	 */
	public static String AES256_Decrypt(String str, String aesKey) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException, NoSuchProviderException {

		if (StringUtils.ifNull(KEY, "").equals("")) {
			KEY = aesKey;
		}

		byte[] textBytes = Base64.decodeBase64(str);
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
		cipher.init(2, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}

	/**
	 * SHA512 암호화
	 */
	public static String SHA512_Encrypt(String str) throws NoSuchAlgorithmException {
		String tempEncrypt = "";
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(str.getBytes());
		byte[] mb = md.digest();
		for (int i = 0; i < mb.length; i++) {
			byte temp = mb[i];
			String s = Integer.toHexString(temp);
			while (s.length() < 2)
				s = "0" + s;
			s = s.substring(s.length() - 2);
			tempEncrypt = tempEncrypt + s;
		}
		return tempEncrypt;
	}

	/**
	 * 파일 암호화
	 */
	public static void encrypt(File source, File dest) throws Exception {
		crypt(Cipher.ENCRYPT_MODE, source, dest);
	}

	/**
	 * 파일 복호화
	 */
	public static void decrypt(File source, File dest) throws Exception {
		crypt(Cipher.DECRYPT_MODE, source, dest);
	}

	/**
	 * 파일 암/복호화
	 */
	private static void crypt(int mode, File source, File dest) throws Exception {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		byte[] keyBytes = new byte[16];
		byte[] getKeyBytes = KEY.getBytes(StandardCharsets.UTF_8);
		int len = getKeyBytes.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(getKeyBytes, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		InputStream input = null;
		OutputStream output = null;
		try {
			input = new BufferedInputStream(new FileInputStream(source));
			output = new BufferedOutputStream(new FileOutputStream(dest));
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = input.read(buffer)) != -1) {
				output.write(cipher.update(buffer, 0, read));
			}
			output.write(cipher.doFinal());
		} finally {
			if (output != null) {
				try {
					output.close();
					output.flush();
				} catch (IOException ie) {
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException ie) {
				}
			}
		}
	}
}
