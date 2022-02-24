package cn.hutool.core.codec;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * @author lin
 * Inspired from https://github.com/adamcaudill/Base58Check/blob/master/src/Base58Check/Base58CheckEncoding.cs
 * Base58工具类，提供Base58的编码和解码方案<br>
 * @since 5.7.22
 */
public class Base58 {


	private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
	private static final char[] ALPHABET_ARRAY = ALPHABET.toCharArray();
	private static final BigInteger BASE_SIZE = BigInteger.valueOf(ALPHABET_ARRAY.length);
	private static final int CHECKSUM_SIZE = 4;

	// -------------------------------------------------------------------- encode

	/**
	 * Base58编码
	 *
	 * @param data 被编码的数组，添加校验和。
	 * @return 编码后的字符串
	 * @since 5.7.22
	 */
	public static String encode(byte[] data) throws NoSuchAlgorithmException {
		return encodePlain(addChecksum(data));
	}

	/**
	 * Base58编码
	 *
	 * @param data 被编码的数据，不带校验和。
	 * @return 编码后的字符串
	 * @since 5.7.22
	 */
	public static String encodePlain(byte[] data) {
		BigInteger intData;
		try {
			intData = new BigInteger(1, data);
		} catch (NumberFormatException e) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		while (intData.compareTo(BigInteger.ZERO) > 0) {
			BigInteger[] quotientAndRemainder = intData.divideAndRemainder(BASE_SIZE);
			BigInteger quotient = quotientAndRemainder[0];
			BigInteger remainder = quotientAndRemainder[1];
			intData = quotient;
			result.insert(0, ALPHABET_ARRAY[remainder.intValue()]);
		}
		for (int i = 0; i < data.length && data[i] == 0; i++) {
			result.insert(0, '1');
		}
		return result.toString();
	}
	// -------------------------------------------------------------------- decode

	/**
	 * Base58编码
	 *
	 * @param encoded 被解码的base58字符串
	 * @return 解码后的bytes
	 * @since 5.7.22
	 */
	public static byte[] decode(String encoded) throws NoSuchAlgorithmException {
		byte[] valueWithChecksum = decodePlain(encoded);
		byte[] value = verifyAndRemoveChecksum(valueWithChecksum);
		if (value == null) {
			throw new IllegalArgumentException("Base58 checksum is invalid");
		}
		return value;
	}

	/**
	 * Base58编码
	 *
	 * @param encoded 被解码的base58字符串
	 * @return 解码后的bytes
	 * @since 5.7.22
	 */
	public static byte[] decodePlain(String encoded) {
		if (encoded.length() == 0) {
			return new byte[0];
		}
		BigInteger intData = BigInteger.ZERO;
		int leadingZeros = 0;
		for (int i = 0; i < encoded.length(); i++) {
			char current = encoded.charAt(i);
			int digit = ALPHABET.indexOf(current);
			if (digit == -1) {
				throw new IllegalArgumentException(String.format("Invalid Base58 character `%c` at position %d", current, i));
			}
			intData = (intData.multiply(BASE_SIZE)).add(BigInteger.valueOf(digit));
		}

		for (int i = 0; i < encoded.length(); i++) {
			char current = encoded.charAt(i);
			if (current == '1') {
				leadingZeros++;
			} else {
				break;
			}
		}
		byte[] bytesData;
		if (intData.equals(BigInteger.ZERO)) {
			bytesData = new byte[0];
		} else {
			bytesData = intData.toByteArray();
		}
		//Should we cut the sign byte ? - https://bitcoinj.googlecode.com/git-history/216deb2d35d1a128a7f617b91f2ca35438aae546/lib/src/com/google/bitcoin/core/Base58.java
		boolean stripSignByte = bytesData.length > 1 && bytesData[0] == 0 && bytesData[1] < 0;
		byte[] decoded = new byte[bytesData.length - (stripSignByte ? 1 : 0) + leadingZeros];
		System.arraycopy(bytesData, stripSignByte ? 1 : 0, decoded, leadingZeros, decoded.length - leadingZeros);
		return decoded;
	}

	private static byte[] verifyAndRemoveChecksum(byte[] data) throws NoSuchAlgorithmException {
		byte[] value = Arrays.copyOfRange(data, 0, data.length - CHECKSUM_SIZE);
		byte[] checksum = Arrays.copyOfRange(data, data.length - CHECKSUM_SIZE, data.length);
		byte[] expectedChecksum = getChecksum(value);
		return Arrays.equals(checksum, expectedChecksum) ? value : null;
	}

	private static byte[] addChecksum(byte[] data) throws NoSuchAlgorithmException {
		byte[] checksum = getChecksum(data);
		byte[] result = new byte[data.length + checksum.length];
		System.arraycopy(data, 0, result, 0, data.length);
		System.arraycopy(checksum, 0, result, data.length, checksum.length);
		return result;
	}

	private static byte[] getChecksum(byte[] data) throws NoSuchAlgorithmException {
		byte[] hash = hash256(data);
		hash = hash256(hash);
		return Arrays.copyOfRange(hash, 0, CHECKSUM_SIZE);
	}

	private static byte[] hash256(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data);
		return md.digest();
	}

}
