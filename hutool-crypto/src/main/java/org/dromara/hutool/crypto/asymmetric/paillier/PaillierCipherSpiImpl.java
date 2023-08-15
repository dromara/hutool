/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.asymmetric.paillier;

import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.crypto.CryptoException;

import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Paillier算法加密器<br>
 * 来自：https://github.com/peterstefanov/paillier
 *
 * @author peterstefanov
 */
class PaillierCipherSpiImpl extends CipherSpi {

	protected SecureRandom random = RandomUtil.getSecureRandom();
	protected int stateMode;
	protected Key key;
	protected int plaintextSize;
	protected int ciphertextSize;
	protected byte[] dataBuffer;
	protected int lengthBuffer;


	// region ----- init
	/**
	 * PaillierCipher doesn't recognise any algorithm - specific initialisations
	 * so the algorithm specific engineInit() just calls the previous overloaded
	 * version of engineInit() (non-Javadoc)
	 */
	@Override
	protected void engineInit(final int mode, final Key key,
							  final AlgorithmParameterSpec params, final SecureRandom random)
		throws InvalidKeyException {
		engineInit(mode, key, random);
	}

	@Override
	protected void engineInit(final int mode, final Key key, final AlgorithmParameters params,
							  final SecureRandom random) throws InvalidKeyException {
		engineInit(mode, key, random);
	}

	/**
	 * Initialises this cipher with key and a source of randomness
	 */
	@Override
	protected void engineInit(final int mode, final Key key, final SecureRandom random) throws InvalidKeyException {
		checkKey(key, mode);

		stateMode = mode;
		this.key = key;
		if (null != random) {
			this.random = random;
		}
		final int modulusLength = ((PaillierKey) key).getN().bitLength();
		calculateBlockSizes(modulusLength);
	}
	// endregion

	@Override
	protected byte[] engineUpdate(final byte[] input, final int inputOffset, final int inputLen) {
		byte[] out = new byte[engineGetOutputSize(inputLen)];
		final int length = engineUpdate(input, inputOffset, inputLen, out, 0);

		if (length < out.length) {
			final byte[] shorter = new byte[length];
			System.arraycopy(out, 0, shorter, 0, length);
			out = shorter;
		}
		return out;
	}

	/**
	 * Creates a single input array from the buffered data and supplied input
	 * data. Calculates the location and the length of the last fractional block
	 * in the input data. Transforms all full blocks in the input data. Save the
	 * last fractional block in the internal buffer.
	 *
	 * @param input        - the input buffer
	 * @param inputOffset  - the offset in input where the input starts
	 * @param inputLen     - the input length
	 * @param output       - the buffer for the result
	 * @param outputOffset - the offset in output where the result is stored
	 * @return the number of bytes stored in output
	 */
	@Override
	protected int engineUpdate(final byte[] input, final int inputOffset, final int inputLen,
							   final byte[] output, final int outputOffset) {
		// create a single array of input data
		final int lengthBuffer = getDataBufferedLength();
		final byte[] totalIn = new byte[inputLen + lengthBuffer];
		readFromBufferAndReset(totalIn);
		System.arraycopy(input, inputOffset, totalIn, lengthBuffer, inputLen);

		// figure out the location of last fractional block
		final int blockSize = engineGetBlockSize();
		final int lastBlockSize = totalIn.length % blockSize;
		final int lastBlockOffset = totalIn.length - lastBlockSize;

		// step through the array
		int outputLength = 0;
		for (int i = 0; i < lastBlockOffset; i += blockSize)
			outputLength += engineTransformBlock(totalIn, i, blockSize,
				output, outputOffset + outputLength);

		// copy the reminder into dataBuffer
		addToBuffer(totalIn, lastBlockOffset, lastBlockSize);

		return outputLength;
	}

	/**
	 * Calls the second overloaded version of the same method.
	 */
	@Override
	protected byte[] engineDoFinal(final byte[] input, final int inputOffset, final int inputLen) {
		final byte[] out = new byte[engineGetOutputSize(inputLen)];
		final int length = engineDoFinal(input, inputOffset, inputLen, out, 0);

		if (length < out.length) {
			final byte[] smaller = new byte[length];
			System.arraycopy(out, 0, smaller, 0, length);
			return smaller;
		}
		return out;
	}

	/**
	 * Encrypts or decrypts data in a single-part operation, or finishes a
	 * multiple-part operation.
	 * <p>
	 * Creates a single input array from the buffered data and supplied input
	 * data. Finds the location and the size of the last partial or full block
	 * in engineUpdate(),just interested in last partial block.. Transforms each
	 * full blocks in the input array by calling engineTransformBlock().
	 * Transform the final partial or full block by calling
	 * engineTransformBlockFinal().
	 *
	 * @param input        - the input buffer
	 * @param inputOffset  - the offset in input where the input starts
	 * @param inputLen     - the input length
	 * @param output       - the buffer for the result
	 * @param outputOffset - the offset in output where the result is stored
	 * @return the number of bytes stored in output
	 */
	@Override
	protected int engineDoFinal(final byte[] input, final int inputOffset, final int inputLen,
								final byte[] output, final int outputOffset) {

		// Create a single array of input data
		final int lengthBuffer = getDataBufferedLength();
		final byte[] totalIn = new byte[inputLen + lengthBuffer];
		readFromBufferAndReset(totalIn);
		if (inputLen > 0)
			System.arraycopy(input, inputOffset, totalIn, lengthBuffer,
				inputLen);

		// Find the location of the last partial or full block.
		final int blockSize = engineGetBlockSize();
		int lastBlockSize = totalIn.length % blockSize;
		if (lastBlockSize == 0 && totalIn.length > 0)
			lastBlockSize = blockSize;
		final int lastBlockOffset = totalIn.length - lastBlockSize;

		// Step through the array
		int outputLength = 0;
		for (int i = 0; i < lastBlockOffset; i += blockSize)
			outputLength += engineTransformBlock(totalIn, i, blockSize,
				output, outputOffset + outputLength);

		// Transform the final partial or full block
		outputLength += engineTransformBlockFinal(totalIn, lastBlockOffset,
			lastBlockSize, output, outputOffset + outputLength);

		return outputLength;

	}

	/**
	 * engineGetBlockSize() returns the appropriate size , based on cipher.
	 *
	 * @return plaintextSize - the block size(in bytes).
	 */
	@Override
	protected int engineGetBlockSize() {
		if (stateMode == Cipher.DECRYPT_MODE)
			return ciphertextSize;
		else
			return plaintextSize;
	}

	/**
	 * Based on the state of the cipher, figure out how many input blocks are
	 * represented by inputLen. Then the number of output bytes need to be
	 * calculated.
	 *
	 * @param inputLen the input length (in bytes)
	 * @return outLength - the required output buffered size (in bytes)
	 */
	@Override
	protected int engineGetOutputSize(final int inputLen) {
		final int inBlocks;
		final int outLength;
		if (stateMode == Cipher.ENCRYPT_MODE) {
			inBlocks = (inputLen + getDataBufferedLength() + plaintextSize - 1)
				/ plaintextSize;
			outLength = inBlocks * ciphertextSize;
		} else {
			inBlocks = (inputLen + getDataBufferedLength() + plaintextSize - 1)
				/ ciphertextSize;
			outLength = inBlocks * plaintextSize;
		}
		return outLength;
	}

	@Override
	protected final void engineSetMode(final String mode) throws NoSuchAlgorithmException {
		throw new NoSuchAlgorithmException("Paillier supports no modes.");
	}

	@Override
	protected final void engineSetPadding(final String padding) throws NoSuchPaddingException {
		throw new NoSuchPaddingException("Paillier supports no padding.");
	}

	/**
	 * This implementation runs just in Electronic Codebook(ECB) mode -
	 * "each block is encrypted separately of other blocks" , so this method
	 * returns null.
	 */
	@Override
	protected byte[] engineGetIV() {
		return null;
	}

	@Override
	protected AlgorithmParameters engineGetParameters() {
		return null;
	}

	// region ----- private methods

	/**
	 * 检查密钥有效性
	 *
	 * @param key  密钥
	 * @param mode 模式
	 * @throws InvalidKeyException 无效KEY异常
	 */
	private void checkKey(final Key key, final int mode) throws InvalidKeyException {
		if (mode == Cipher.ENCRYPT_MODE) {
			if (!(key instanceof PaillierPublicKey)) {
				throw new InvalidKeyException("I didn't get a PaillierPublicKey.");
			}
		} else if (mode == Cipher.DECRYPT_MODE) {
			if (!(key instanceof PaillierPrivateKey)) {
				throw new InvalidKeyException(
					"I didn't get a PaillierPrivateKey. ");
			}
		} else {
			throw new IllegalArgumentException("Bad mode: " + mode);
		}
	}

	/**
	 * This helper method returns an array of bytes that is only as long as it
	 * needs to be , ignoring the sign of the number.
	 *
	 * @param big BigInteger
	 * @return an array of bytes
	 */
	private byte[] getBytes(final BigInteger big) {
		final byte[] bigBytes = big.toByteArray();
		if ((big.bitLength() % 8) != 0) {
			return bigBytes;
		} else {
			final byte[] smallerBytes = new byte[big.bitLength() / 8];
			System.arraycopy(bigBytes, 1, smallerBytes, 0, smallerBytes.length);
			return smallerBytes;
		}
	}

	/**
	 * Calculates the size of the plaintext block and a ciphertext block, based
	 * on the size of the key used to initialise the cipher.
	 *
	 * @param modulusLength - n = p*q
	 */
	private void calculateBlockSizes(final int modulusLength) {
		plaintextSize = (modulusLength - 1) / 8;
		ciphertextSize = ((modulusLength + 7) / 8) * 2;

	}

	/**
	 * This method may be passed less than a full block.
	 *
	 * @param input        - byte[]
	 * @param inputOffset  -int
	 * @param inputLenth   -int
	 * @param output       -byte[]
	 * @param outputOffset -int
	 * @return The number of bytes written.
	 */
	private int engineTransformBlockFinal(final byte[] input, final int inputOffset,
										  final int inputLenth, final byte[] output, final int outputOffset) {
		if (inputLenth == 0) {
			return 0;
		}
		return engineTransformBlock(input, inputOffset, inputLenth, output, outputOffset);
	}

	/**
	 * This method is called whenever a full block needs to be
	 * encrypted/decrypted. The input block is contained in input.The
	 * transformed block is written into output starting at otputOfset. The
	 * number of bytes written is returned.
	 *
	 * @param input        - byte[]
	 * @param inputOffset  -int
	 * @param inputLenth   -int
	 * @param output       -byte[]
	 * @param outputOffset -int
	 * @return The number of bytes written.
	 */
	private int engineTransformBlock(final byte[] input, final int inputOffset,
									 final int inputLenth, final byte[] output, final int outputOffset) {
		if (stateMode == Cipher.ENCRYPT_MODE) {
			return encryptBlock(input, inputOffset, inputLenth, output, outputOffset);
		} else if (stateMode == Cipher.DECRYPT_MODE) {
			return decryptBlock(input, inputOffset, output, outputOffset);
		}
		return 0;
	}

	/**
	 * Perform actual encryption ,creates single array and updates the result
	 * after the encryption.
	 *
	 * @param input        - the input in bytes
	 * @param inputOffset  - the offset in input where the input starts
	 * @param inputLenth   - the input length
	 * @param output       - the buffer for the result
	 * @param outputOffset - the offset in output where the result is stored
	 * @return the number of bytes stored in output
	 * @throws CryptoException throws if Plaintext m is not in Z_n , m should be less then n
	 */
	private int encryptBlock(final byte[] input, final int inputOffset, final int inputLenth,
							 final byte[] output, final int outputOffset) throws CryptoException {
		final byte[] messageBytes = new byte[plaintextSize];
		final int inLenth = Math.min(plaintextSize, inputLenth);
		System.arraycopy(input, inputOffset, messageBytes, 0, inLenth);
		final BigInteger m = new BigInteger(1, messageBytes);

		// get the public key in order to encrypt
		final PaillierPublicKey key = (PaillierPublicKey) this.key;
		final BigInteger g = key.getG();
		final BigInteger n = key.getN();
		final BigInteger nsquare = key.getNSquare();
		final BigInteger r = key.generateRandomRinZn(random);

		if (m.compareTo(BigInteger.ZERO) < 0 || m.compareTo(n) >= 0) {
			throw new CryptoException(
				"PaillierCipher.encryptBlock :Plaintext m is not in Z_n , m should be less then n");
		}
		final BigInteger c = (g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)))
			.mod(nsquare);

		final byte[] cBytes = getBytes(c);
		System.arraycopy(cBytes, 0, output, outputOffset + ciphertextSize
			- cBytes.length, cBytes.length);

		return ciphertextSize;
	}

	/**
	 * Perform actual decryption ,creates single array for the output and updates
	 * the result after the decryption.
	 *
	 * @param input        - the input in bytes
	 * @param inputOffset  - the offset in input where the input starts
	 * @param output       - the buffer for the result
	 * @param outputOffset - the offset in output where the result is stored
	 * @return the number of bytes stored in output
	 */
	private int decryptBlock(final byte[] input, final int inputOffset,
							 final byte[] output, final int outputOffset) {
		final PaillierPrivateKey key = (PaillierPrivateKey) this.key;
		final BigInteger u = key.getU();
		final BigInteger lambda = key.getLambda();
		final BigInteger n = key.getN();
		final BigInteger nsquare = key.getNSquare();

		// extract c
		final byte[] cBytes = new byte[ciphertextSize];
		System.arraycopy(input, inputOffset, cBytes, 0, ciphertextSize);
		final BigInteger c = new BigInteger(1, cBytes);
		// calculate the message
		final BigInteger m = c.modPow(lambda, nsquare).subtract(BigInteger.ONE)
			.divide(n).multiply(u).mod(n);

		final byte[] messageBytes = getBytes(m);
		final int gatedLength = Math.min(messageBytes.length, plaintextSize);
		System.arraycopy(messageBytes, 0, output, outputOffset + plaintextSize
			- gatedLength, gatedLength);
		return plaintextSize;
	}

	/**
	 * Retrieved buffered data. The data will be copied into the supplied array
	 * and the internal buffer is reset - by setting lengthBuffer to 0
	 */
	private void readFromBufferAndReset(final byte[] output) {
		checkBuffer();
		System.arraycopy(dataBuffer, 0, output, 0, lengthBuffer);
		lengthBuffer = 0;
	}

	/**
	 * Returns the length of the data stored in the buffer.
	 *
	 * @return lengthBuffer
	 */
	private int getDataBufferedLength() {
		checkBuffer();
		return lengthBuffer;
	}

	/**
	 * Checks to see if the buffer exists. If not , or if it is not the same
	 * length as the block size, a new buffer created.
	 */
	private void checkBuffer() {
		if (dataBuffer == null || dataBuffer.length != engineGetBlockSize()) {
			dataBuffer = new byte[engineGetBlockSize()];
			lengthBuffer = 0;
		}
	}

	/**
	 * Adds the specified data to the internal buffer.
	 */
	private void addToBuffer(final byte[] input, final int offset, final int length) {
		checkBuffer();
		System.arraycopy(input, offset, dataBuffer, lengthBuffer, length);
		lengthBuffer += length;
	}
	// endregion
}
