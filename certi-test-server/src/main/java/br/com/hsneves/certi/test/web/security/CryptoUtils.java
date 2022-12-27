package br.com.hsneves.certi.test.web.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import br.com.hsneves.certi.test.exceptions.CertiTestRuntimeException;

/**
 * 
 * Funções utilitárias para criptografia.
 * 
 * @author Henrique Neves
 *
 */
public class CryptoUtils {

	/**
	 * Valores mínimos recomendados por PKCS#5.
	 */
	private static final int ITERATION_COUNT = 1000;

	/**
	 * 256-bits para AES-256, 128-bits para AES-128, etc
	 */
	private static final int KEY_LENGTH = 256;

	/**
	 * Algoritmo de criptografia PBKDF2.
	 */
	private static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";

	/**
	 * O nome da transformação para criar um cipher.
	 */
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	/**
	 * Comprimento do salt depende do comprimento da chave (256 / 8 = 32).
	 */
	private static final int PKCS5_SALT_LENGTH = 32;

	private static final String DELIMITER = "]";

	private static final SecureRandom random = new SecureRandom();

	/**
	 * Algoritmo de criptografia
	 */
	private static final String AES_ALGORITHM = "AES";

	/**
	 * Senha para a chave de criptografia
	 */
	private static final String CRYPTO_PASSWORD = "&W7rkf?qTZS-Wzr_GdmgjCRM?yTBtsUg";

	/**
	 * Criptografar texto com padrão {@link CRYPTO_PASSWORD}
	 * 
	 * @param text Text to be encrypted.
	 * @return Encrypted String
	 */
	public static String encrypt(String text) {
		return encrypt(text, CRYPTO_PASSWORD);
	}

	/**
	 * Função principal para descriptografar um texto criptografado com base na criptografia de senha.
	 * 
	 * @param plaintext texto para ser encriptado
	 * @param password  senha da chave de criptografia
	 * @return string encriptada
	 */
	private static String encrypt(String plaintext, String password) {
		byte[] salt = generateSalt();
		SecretKey key = deriveKey(password, salt);

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			byte[] iv = generateIv(cipher.getBlockSize());
			IvParameterSpec ivParams = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
			byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

			if (salt != null) {
				return String.format("%s%s%s%s%s", toBase64(salt), DELIMITER, toBase64(iv), DELIMITER, toBase64(cipherText));
			}
			return String.format("%s%s%s", toBase64(iv), DELIMITER, toBase64(cipherText));
		} catch (GeneralSecurityException e) {
			throw new CertiTestRuntimeException(e);
		}
	}

	/**
	 * Descriptografar texto com padrão {@link CRYPTO_PASSWORD}
	 * 
	 * @param text texto criptografado
	 * @return string descriptografada
	 */
	public static String decrypt(String text) {
		return decrypt(text, CRYPTO_PASSWORD);
	}

	/**
	 * Descriptografar
	 * 
	 * @param encryptedText texto criptografado
	 * @param password senha para a chave de criptografia.
	 * @return string descriptografada
	 */
	private static String decrypt(String encryptedText, String password) {
		String[] fields = encryptedText.split(DELIMITER);
		if (fields.length != 3) {
			throw new IllegalArgumentException("Invalid encrypted text format");
		}
		byte[] salt = fromBase64(fields[0]);
		byte[] iv = fromBase64(fields[1]);
		byte[] cipherBytes = fromBase64(fields[2]);
		SecretKey key = deriveKey(password, salt);

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec ivParams = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
			byte[] plaintext = cipher.doFinal(cipherBytes);
			return new String(plaintext, StandardCharsets.UTF_8);
		} catch (GeneralSecurityException e) {
			throw new CertiTestRuntimeException(e);
		}
	}

	/**
	 * Salt são dados aleatórios usados como uma entrada adicional para uma função unidirecional que "mistura" uma senha ou frase secreta.
	 * 
	 * @return Generated salt
	 */
	private static byte[] generateSalt() {
		byte[] b = new byte[PKCS5_SALT_LENGTH];
		random.nextBytes(b);
		return b;
	}

	/**
	 * Vetor de inicialização - para obter segurança semântica, não permite que um invasor infira relacionamentos entre segmentos da mensagem criptografada.
	 *
	 * @param length
	 * @return
	 */
	private static byte[] generateIv(int length) {
		byte[] b = new byte[length];
		random.nextBytes(b);
		return b;
	}

	/**
	 * Derive the key from the password
	 * 
	 * @param password
	 * @param salt
	 * @return SecretKey
	 */
	private static SecretKey deriveKey(String password, byte[] salt) {
		try {
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBKDF2_DERIVATION_ALGORITHM);
			byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
			return new SecretKeySpec(keyBytes, AES_ALGORITHM);
		} catch (GeneralSecurityException e) {
			throw new CertiTestRuntimeException(e);
		}
	}

	/**
	 * Encode base64 byte array to String
	 * 
	 * @param bytes Base64 array
	 * @return Encoded Base64 String
	 */
	private static String toBase64(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	/**
	 * Decode a base64 String to a byte array
	 * 
	 * @param base64 Base64 String
	 * @return Decoded Base64 byte array
	 */
	private static byte[] fromBase64(String base64) {
		return Base64.decodeBase64(base64);
	}

}