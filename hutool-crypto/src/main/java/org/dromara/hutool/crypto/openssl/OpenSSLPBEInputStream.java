/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.openssl;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.text.StrUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/*
source: http://stackoverflow.com/questions/11783062/how-to-decrypt-an-encrypted-file-in-java-with-openssl-with-aes
*/
public class OpenSSLPBEInputStream extends InputStream {

	protected final static int READ_BLOCK_SIZE = 64 * 1024;

	private final Cipher cipher;
	private final InputStream inStream;

	private final byte[] bufferCipher = new byte[READ_BLOCK_SIZE];
	private byte[] bufferClear = null;

	private int index = Integer.MAX_VALUE;
	private int maxIndex = 0;

	public OpenSSLPBEInputStream(final InputStream streamIn,
								 final String algorithm,
								 final int iterationCount,
								 final char[] password) throws IOException {

		this.inStream = streamIn;

		readHeader();
		final byte[] salt = readSalt();

		try {
			this.cipher = OpenSSLPBECommon.initializeCipher(password, salt, Cipher.DECRYPT_MODE, algorithm, iterationCount);
		} catch (final Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public int available() throws IOException {
		return inStream.available();
	}

	@Override
	public int read() throws IOException {
		if (maxIndex < 0) {
			return -1;
		}
		if (index > maxIndex) {
			index = 0;
			final int read = inStream.read(bufferCipher);

			if (read != -1) {
				bufferClear = cipher.update(bufferCipher, 0, read);
			}

			if (read == -1 || bufferClear == null || bufferClear.length == 0) {
				try {
					bufferClear = cipher.doFinal();
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					bufferClear = null;
				}
			}

			maxIndex = bufferClear == null ? -1 : bufferClear.length - 1;

			if (maxIndex < 0) {
				return -1;
			}
		}

		return bufferClear[index++] & 0xff;
	}

	private void readHeader() throws IOException {
		final byte[] headerBytes = new byte[OpenSSLPBECommon.SALTED_MAGIC.length];
		inStream.read(headerBytes);

		if (!Arrays.equals(OpenSSLPBECommon.SALTED_MAGIC, headerBytes)) {
			throw new IOException("unexpected file header " + StrUtil.utf8Str(headerBytes));
		}
	}

	private byte[] readSalt() throws IOException {
		final byte[] salt = new byte[OpenSSLPBECommon.SALT_SIZE_BYTES];
		inStream.read(salt);
		return salt;
	}
}
