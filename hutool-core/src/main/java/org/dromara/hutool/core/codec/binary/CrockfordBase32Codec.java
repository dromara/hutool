/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.codec.binary;

import org.dromara.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Crockford`s Base32实现<br>
 * 来自：https://gist.github.com/markov/5206312
 *
 * <p>Provides Base32 encoding and decoding as defined by <a href="http://www.ietf.org/rfc/rfc4648.txt">RFC 4648</a>.
 * However it uses a custom alphabet first coined by Douglas Crockford. Only addition to the alphabet is that 'u' and
 * 'U' characters decode as if they were 'V' to improve mistakes by human input.
 * </p>
 *
 * <p>
 * This class operates directly on byte streams, and not character streams.
 * </p>
 *
 * @version $Id: Base32.java 1382498 2012-09-09 13:41:55Z sebb $
 * @see <a href="http://www.ietf.org/rfc/rfc4648.txt">RFC 4648</a>
 * @see <a href="http://www.crockford.com/wrmg/base32.html">Douglas Crockford's Base32 Encoding</a>
 * @since 1.5
 */
public class CrockfordBase32Codec {

	/**
	 * Mask used to extract 8 bits, used in decoding bytes
	 */
	protected static final int MASK_8BITS = 0xff;
	private static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
	private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
	/**
	 * Defines the default buffer size - currently {@value}
	 * - must be large enough for at least one encoded block+separator
	 */
	private static final int DEFAULT_BUFFER_SIZE = 8192;
	/**
	 * Mask used to extract 5 bits, used when encoding Base32 bytes
	 */
	private static final int MASK_5BITS = 0x1f;
	/**
	 * BASE32 characters are 5 bits in length.
	 * They are formed by taking a block of five octets to form a 40-bit string,
	 * which is converted into eight BASE32 characters.
	 */
	private static final int BITS_PER_ENCODED_BYTE = 5;
	private static final int BYTES_PER_ENCODED_BLOCK = 8;
	private static final int BYTES_PER_UNENCODED_BLOCK = 5;
	private static final byte PAD = '=';
	/**
	 * This array is a lookup table that translates 5-bit positive integer index values into their "Base32 Alphabet"
	 * equivalents as specified in Table 3 of RFC 2045.
	 */
	private static final byte[] ENCODE_TABLE = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M',
		'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
	};
	/**
	 * Convenience variable to help us determine when our buffer is going to run out of room and needs resizing.
	 * <code>decodeSize = {@link #BYTES_PER_ENCODED_BLOCK} - 1 + lineSeparator.length;</code>
	 */
	private final int decodeSize;
	/**
	 * Convenience variable to help us determine when our buffer is going to run out of room and needs resizing.
	 * <code>encodeSize = {@link #BYTES_PER_ENCODED_BLOCK} + lineSeparator.length;</code>
	 */
	private final int encodeSize;
	/**
	 * Wheather this encoder should use a padding character at the end of encoded Strings.
	 */
	private final boolean usePaddingCharacter;
	/**
	 * Buffer for streaming.
	 */
	protected byte[] buffer;
	/**
	 * Position where next character should be written in the buffer.
	 */
	protected int pos;
	/**
	 * Boolean flag to indicate the EOF has been reached. Once EOF has been reached, this object becomes useless,
	 * and must be thrown away.
	 */
	protected boolean eof;
	/**
	 * Writes to the buffer only occur after every 3/5 reads when encoding, and every 4/8 reads when decoding.
	 * This variable helps track that.
	 */
	protected int modulus;
	/**
	 * Place holder for the bytes we're dealing with for our based logic.
	 * Bitwise operations store and extract the encoding or decoding from this variable.
	 */
	private long bitWorkArea;

	/**
	 * 构造
	 */
	public CrockfordBase32Codec() {
		this(false);
	}

	/**
	 * Creates a Base32 codec used for decoding and encoding.
	 * <p>
	 * When encoding the line length is 0 (no chunking).
	 * </p>
	 *
	 * @param usePaddingCharacter 是否填充字符
	 */
	public CrockfordBase32Codec(final boolean usePaddingCharacter) {
		this.usePaddingCharacter = usePaddingCharacter;
		this.encodeSize = BYTES_PER_ENCODED_BLOCK;
		this.decodeSize = this.encodeSize - 1;
	}

	/**
	 * Checks if a byte value is whitespace or not.
	 * Whitespace is taken to mean: space, tab, CR, LF
	 *
	 * @param byteToCheck the byte to check
	 * @return true if byte is whitespace, false otherwise
	 */
	protected static boolean isWhiteSpace(final byte byteToCheck) {
		switch (byteToCheck) {
			case ' ':
			case '\n':
			case '\r':
			case '\t':
				return true;
			default:
				return false;
		}
	}

	/**
	 * Tests a given String to see if it contains only valid characters within the alphabet.
	 * The method treats whitespace and PAD as valid.
	 *
	 * @param base32 String to test
	 * @return {@code true} if all characters in the String are valid characters in the alphabet or if
	 * the String is empty; {@code false}, otherwise
	 * @see #isInAlphabet(byte[], boolean)
	 */
	public static boolean isInAlphabet(final String base32) {
		return isInAlphabet(base32.getBytes(DEFAULT_CHARSET), true);
	}

	/**
	 * Tests a given byte array to see if it contains only valid characters within the alphabet.
	 * The method optionally treats whitespace and pad as valid.
	 *
	 * @param arrayOctet byte array to test
	 * @param allowWSPad if {@code true}, then whitespace and PAD are also allowed
	 * @return {@code true} if all bytes are valid characters in the alphabet or if the byte array is empty;
	 * {@code false}, otherwise
	 */
	public static boolean isInAlphabet(final byte[] arrayOctet, final boolean allowWSPad) {
		for (final byte b : arrayOctet) {
			if (!isInAlphabet(b) &&
				(!allowWSPad || (b != PAD) && !isWhiteSpace(b))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns whether the {@code octet} is in the Base32 alphabet.
	 *
	 * @param octet The value to test
	 * @return {@code true} if the value is defined in the Base32 alphabet {@code false} otherwise.
	 */
	public static boolean isInAlphabet(final byte octet) {
		return decode(octet) != -1;
	}

	/**
	 * Returns the amount of buffered data available for reading.
	 *
	 * @return The amount of buffered data available for reading.
	 */
	int available() {  // package protected for access from I/O streams
		return buffer != null ? pos : 0;
	}

	/**
	 * Increases our buffer by the {@link #DEFAULT_BUFFER_RESIZE_FACTOR}.
	 */
	private void resizeBuffer() {
		if (buffer == null) {
			buffer = new byte[DEFAULT_BUFFER_SIZE];
			pos = 0;
		} else {
			final byte[] b = new byte[buffer.length * DEFAULT_BUFFER_RESIZE_FACTOR];
			System.arraycopy(buffer, 0, b, 0, buffer.length);
			buffer = b;
		}
	}

	/**
	 * Ensure that the buffer has room for {@code size} bytes
	 *
	 * @param size minimum spare space required
	 */
	protected void ensureBufferSize(final int size) {
		if ((buffer == null) || (buffer.length < pos + size)) {
			resizeBuffer();
		}
	}

	/**
	 * Extracts buffered data into the provided byte[] array, starting at position bPos,
	 * up to a maximum of bAvail bytes. Returns how many bytes were actually extracted.
	 *
	 * @param b byte[] array to extract the buffered data into.
	 * @return The number of bytes successfully extracted into the provided byte[] array.
	 */
	int readResults(final byte[] b) {  // package protected for access from I/O streams
		if (buffer != null) {
			final int len = available();
			System.arraycopy(buffer, 0, b, 0, len);
			buffer = null; // so hasData() will return false, and this method can return -1
			return len;
		}
		return eof ? -1 : 0;
	}

	/**
	 * Resets this object to its initial newly constructed state.
	 */
	private void reset() {
		buffer = null;
		pos = 0;
		modulus = 0;
		eof = false;
	}

	/**
	 * Encodes a String containing characters in the Base32 alphabet.
	 *
	 * @param pArray A String containing Base32 character data
	 * @return A String containing only Base32 character data
	 */
	public String encodeToString(final String pArray) {
		return encodeToString(pArray.getBytes(DEFAULT_CHARSET));
	}

	/**
	 * Encodes a byte[] containing binary data, into a String containing characters in the Base-N alphabet.
	 *
	 * @param pArray a byte array containing binary data
	 * @return A String containing only Base32 character data
	 */
	public String encodeToString(final byte[] pArray) {
		return new String(encode(pArray), DEFAULT_CHARSET);
	}

	/**
	 * Encodes a String containing characters in the Base32 alphabet.
	 *
	 * @param pArray A String containing Base32 character data
	 * @return A UTF-8 decoded String
	 */
	public String decodeToString(final String pArray) {
		return decodeToString(pArray.getBytes(DEFAULT_CHARSET));
	}

	/**
	 * Decodes a byte[] containing binary data, into a String containing UTF-8 decoded String.
	 *
	 * @param pArray a byte array containing binary data
	 * @return A UTF-8 decoded String
	 */
	public String decodeToString(final byte[] pArray) {
		return new String(decode(pArray), DEFAULT_CHARSET);
	}

	/**
	 * Decodes a String containing characters in the Base-N alphabet.
	 *
	 * @param pArray A String containing Base-N character data
	 * @return a byte array containing binary data
	 */
	public byte[] decode(final String pArray) {
		return decode(pArray.getBytes(DEFAULT_CHARSET));
	}

	/**
	 * Encodes a String containing characters in the Base32 alphabet.
	 *
	 * @param pArray A String containing Base-N character data
	 * @return a byte array containing binary data
	 */
	public byte[] encode(final String pArray) {
		return encode(pArray.getBytes(DEFAULT_CHARSET));
	}

	/**
	 * Decodes a byte[] containing characters in the Base-N alphabet.
	 *
	 * @param pArray A byte array containing Base-N character data
	 * @return a byte array containing binary data
	 */
	public byte[] decode(final byte[] pArray) {
		reset();
		if (pArray == null || pArray.length == 0) {
			return pArray;
		}
		decode(pArray, 0, pArray.length);
		decode(pArray, 0, -1); // Notify decoder of EOF.
		final byte[] result = new byte[pos];
		readResults(result);
		return result;
	}

	// The static final fields above are used for the original static byte[] methods on Base32.
	// The private member fields below are used with the new streaming approach, which requires
	// some state be preserved between calls of encode() and decode().

	/**
	 * Encodes a byte[] containing binary data, into a byte[] containing characters in the alphabet.
	 *
	 * @param pArray a byte array containing binary data
	 * @return A byte array containing only the basen alphabetic character data
	 */
	public byte[] encode(final byte[] pArray) {
		reset();
		if (pArray == null || pArray.length == 0) {
			return pArray;
		}
		encode(pArray, 0, pArray.length);
		encode(pArray, 0, -1); // Notify encoder of EOF.
		final byte[] buf = new byte[pos];
		readResults(buf);
		return buf;
	}

	/**
	 * Calculates the amount of space needed to encode the supplied array.
	 *
	 * @param pArray byte[] array which will later be encoded
	 * @return amount of space needed to encode the supplied array.
	 * Returns a long since a max-len array will require > Integer.MAX_VALUE
	 */
	public long getEncodedLength(final byte[] pArray) {
		// Calculate non-chunked size - rounded up to allow for padding
		// cast to long is needed to avoid possibility of overflow
		return ((pArray.length + BYTES_PER_UNENCODED_BLOCK - 1) / BYTES_PER_UNENCODED_BLOCK) * (long) BYTES_PER_ENCODED_BLOCK;
	}

	/**
	 * <p>
	 * Decodes all of the provided data, starting at inPos, for inAvail bytes. Should be called at least twice: once
	 * with the data to decode, and once with inAvail set to "-1" to alert decoder that EOF has been reached. The "-1"
	 * call is not necessary when decoding, but it doesn't hurt, either.
	 * </p>
	 * <p>
	 * Ignores all non-Base32 characters. This is how chunked (e.g. 76 character) data is handled, since CR and LF are
	 * silently ignored, but has implications for other bytes, too. This method subscribes to the garbage-in,
	 * garbage-out philosophy: it will not check the provided data for validity.
	 * </p>
	 *
	 * @param in      byte[] array of ascii data to Base32 decode.
	 * @param inPos   Position to start reading data from.
	 * @param inAvail Amount of bytes available from input for encoding.Output is written to {@link #buffer} as 8-bit octets, using {@link #pos} as the buffer position
	 */
	void decode(final byte[] in, int inPos, final int inAvail) { // package protected for access from I/O streams
		if (eof) {
			return;
		}
		if (inAvail < 0) {
			eof = true;
		}
		for (int i = 0; i < inAvail; i++) {
			final byte b = in[inPos++];
			if (b == PAD) {
				// We're done.
				eof = true;
				break;
			} else {
				ensureBufferSize(decodeSize);
				if (isInAlphabet(b)) {
					final int result = decode(b);
					modulus = (modulus + 1) % BYTES_PER_ENCODED_BLOCK;
					bitWorkArea = (bitWorkArea << BITS_PER_ENCODED_BYTE) + result; // collect decoded bytes
					if (modulus == 0) { // we can output the 5 bytes
						buffer[pos++] = (byte) ((bitWorkArea >> 32) & MASK_8BITS);
						buffer[pos++] = (byte) ((bitWorkArea >> 24) & MASK_8BITS);
						buffer[pos++] = (byte) ((bitWorkArea >> 16) & MASK_8BITS);
						buffer[pos++] = (byte) ((bitWorkArea >> 8) & MASK_8BITS);
						buffer[pos++] = (byte) (bitWorkArea & MASK_8BITS);
					}
				}
			}
		}

		// Two forms of EOF as far as Base32 decoder is concerned: actual
		// EOF (-1) and first time '=' character is encountered in stream.
		// This approach makes the '=' padding characters completely optional.
		if (eof && modulus >= 2) { // if modulus < 2, nothing to do
			ensureBufferSize(decodeSize);

			//  we ignore partial bytes, i.e. only multiples of 8 count
			switch (modulus) {
				case 2: // 10 bits, drop 2 and output one byte
					buffer[pos++] = (byte) ((bitWorkArea >> 2) & MASK_8BITS);
					break;
				case 3: // 15 bits, drop 7 and output 1 byte
					buffer[pos++] = (byte) ((bitWorkArea >> 7) & MASK_8BITS);
					break;
				case 4: // 20 bits = 2*8 + 4
					bitWorkArea = bitWorkArea >> 4; // drop 4 bits
					buffer[pos++] = (byte) ((bitWorkArea >> 8) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea) & MASK_8BITS);
					break;
				case 5: // 25bits = 3*8 + 1
					bitWorkArea = bitWorkArea >> 1;
					buffer[pos++] = (byte) ((bitWorkArea >> 16) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea >> 8) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea) & MASK_8BITS);
					break;
				case 6: // 30bits = 3*8 + 6
					bitWorkArea = bitWorkArea >> 6;
					buffer[pos++] = (byte) ((bitWorkArea >> 16) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea >> 8) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea) & MASK_8BITS);
					break;
				case 7: // 35 = 4*8 +3
					bitWorkArea = bitWorkArea >> 3;
					buffer[pos++] = (byte) ((bitWorkArea >> 24) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea >> 16) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea >> 8) & MASK_8BITS);
					buffer[pos++] = (byte) ((bitWorkArea) & MASK_8BITS);
					break;
			}
		}
	}

	/**
	 * <p>
	 * Encodes all of the provided data, starting at inPos, for inAvail bytes. Must be called at least twice: once with
	 * the data to encode, and once with inAvail set to "-1" to alert encoder that EOF has been reached, so flush last
	 * remaining bytes (if not multiple of 5).
	 * </p>
	 *
	 * @param in      byte[] array of binary data to Base32 encode.
	 * @param inPos   Position to start reading data from.
	 * @param inAvail Amount of bytes available from input for encoding.
	 */
	void encode(final byte[] in, int inPos, final int inAvail) { // package protected for access from I/O streams
		if (eof) {
			return;
		}
		// inAvail < 0 is how we're informed of EOF in the underlying data we're
		// encoding.
		if (inAvail < 0) {
			eof = true;
			if (0 == modulus) {
				return; // no leftovers to process
			}
			ensureBufferSize(encodeSize);
			final int savedPos = pos;
			switch (modulus) { // % 5
				case 1: // Only 1 octet; take top 5 bits then remainder
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 3) & MASK_5BITS]; // 8-1*5 = 3
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea << 2) & MASK_5BITS]; // 5-3=2
					if (usePaddingCharacter) {
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
					}
					break;

				case 2: // 2 octets = 16 bits to use
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 11) & MASK_5BITS]; // 16-1*5 = 11
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 6) & MASK_5BITS]; // 16-2*5 = 6
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 1) & MASK_5BITS]; // 16-3*5 = 1
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea << 4) & MASK_5BITS]; // 5-1 = 4
					if (usePaddingCharacter) {
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
					}
					break;
				case 3: // 3 octets = 24 bits to use
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 19) & MASK_5BITS]; // 24-1*5 = 19
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 14) & MASK_5BITS]; // 24-2*5 = 14
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 9) & MASK_5BITS]; // 24-3*5 = 9
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 4) & MASK_5BITS]; // 24-4*5 = 4
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea << 1) & MASK_5BITS]; // 5-4 = 1
					if (usePaddingCharacter) {
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
						buffer[pos++] = PAD;
					}
					break;
				case 4: // 4 octets = 32 bits to use
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 27) & MASK_5BITS]; // 32-1*5 = 27
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 22) & MASK_5BITS]; // 32-2*5 = 22
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 17) & MASK_5BITS]; // 32-3*5 = 17
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 12) & MASK_5BITS]; // 32-4*5 = 12
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 7) & MASK_5BITS]; // 32-5*5 =  7
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 2) & MASK_5BITS]; // 32-6*5 =  2
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea << 3) & MASK_5BITS]; // 5-2 = 3
					if (usePaddingCharacter) {
						buffer[pos++] = PAD;
					}
					break;
			}
		} else {
			for (int i = 0; i < inAvail; i++) {
				ensureBufferSize(encodeSize);
				modulus = (modulus + 1) % BYTES_PER_UNENCODED_BLOCK;
				int b = in[inPos++];
				if (b < 0) {
					b += 256;
				}
				bitWorkArea = (bitWorkArea << 8) + b; // BITS_PER_BYTE
				if (0 == modulus) { // we have enough bytes to create our output
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 35) & MASK_5BITS];
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 30) & MASK_5BITS];
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 25) & MASK_5BITS];
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 20) & MASK_5BITS];
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 15) & MASK_5BITS];
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 10) & MASK_5BITS];
					buffer[pos++] = ENCODE_TABLE[(int) (bitWorkArea >> 5) & MASK_5BITS];
					buffer[pos++] = ENCODE_TABLE[(int) bitWorkArea & MASK_5BITS];
				}
			}
		}
	}

	/**
	 * 写出Crockford`s Base32值 到buffer指定位置
	 *
	 * @param buffer buffer
	 * @param value  值
	 * @param count  字符数量
	 * @param offset 开始位置
	 */
	public static void writeCrockford(final char[] buffer, final long value, final int count, final int offset) {
		for (int i = 0; i < count; i++) {
			final int index = (int) ((value >>> ((count - i - 1) * BITS_PER_ENCODED_BYTE)) & MASK_5BITS);
			buffer[offset + i] = (char) ENCODE_TABLE[index];
		}
	}

	/**
	 * 追加Crockford`s Base32值 到buffer指定位置
	 *
	 * @param builder {@link StringBuilder}
	 * @param value   值
	 * @param count   字符数量
	 */
	public static void appendCrockford(final StringBuilder builder, final long value, final int count) {
		for (int i = count - 1; i >= 0; i--) {
			final int index = (int) ((value >>> (i * BITS_PER_ENCODED_BYTE)) & MASK_5BITS);
			builder.append(ENCODE_TABLE[index]);
		}
	}

	/**
	 * 解析Crockford`s Base32值
	 *
	 * @param input Crockford`s Base32值
	 * @return ID值
	 */
	public static long parseCrockford(final String input) {
		Objects.requireNonNull(input, "input must not be null!");
		final int length = input.length();
		if (length > 12) {
			throw new IllegalArgumentException("input length must not exceed 12 but was " + length + "!");
		}

		long result = 0;
		for (int i = 0; i < length; i++) {
			final char current = input.charAt(i);
			final byte value = decode((byte) current);
			if (value < 0) {
				throw new IllegalArgumentException("Illegal character '" + current + "'!");
			}
			result |= ((long) value) << ((length - 1 - i) * BITS_PER_ENCODED_BYTE);
		}
		return result;
	}

	private static byte decode(final byte octet) {
		switch (octet) {
			case '0':
			case 'O':
			case 'o':
				return 0;

			case '1':
			case 'I':
			case 'i':
			case 'L':
			case 'l':
				return 1;

			case '2':
				return 2;
			case '3':
				return 3;
			case '4':
				return 4;
			case '5':
				return 5;
			case '6':
				return 6;
			case '7':
				return 7;
			case '8':
				return 8;
			case '9':
				return 9;

			case 'A':
			case 'a':
				return 10;

			case 'B':
			case 'b':
				return 11;

			case 'C':
			case 'c':
				return 12;

			case 'D':
			case 'd':
				return 13;

			case 'E':
			case 'e':
				return 14;

			case 'F':
			case 'f':
				return 15;

			case 'G':
			case 'g':
				return 16;

			case 'H':
			case 'h':
				return 17;

			case 'J':
			case 'j':
				return 18;

			case 'K':
			case 'k':
				return 19;

			case 'M':
			case 'm':
				return 20;

			case 'N':
			case 'n':
				return 21;

			case 'P':
			case 'p':
				return 22;

			case 'Q':
			case 'q':
				return 23;

			case 'R':
			case 'r':
				return 24;

			case 'S':
			case 's':
				return 25;

			case 'T':
			case 't':
				return 26;

			case 'U':
			case 'u':
			case 'V':
			case 'v':
				return 27;

			case 'W':
			case 'w':
				return 28;

			case 'X':
			case 'x':
				return 29;

			case 'Y':
			case 'y':
				return 30;

			case 'Z':
			case 'z':
				return 31;

			default:
				return -1;
		}
	}
}
