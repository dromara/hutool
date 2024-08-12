/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.crypto.openssl;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 加盐值魔数<br>
 * 用于在OpenSSL生成的密文中，提取加盐值等相关信息
 *
 * @author looly
 * @since 6.0.0
 */
public class SaltMagic {
	/**
	 * 加盐值长度
	 */
	public final static byte SALT_LEN = 8;
	/**
	 * OpenSSL's magic initial bytes.
	 */
	public static final byte[] SALTED_MAGIC = "Salted__".getBytes(StandardCharsets.US_ASCII);

	/**
	 * 获取魔术值和随机盐的长度：16（128位）
	 */
	public static final int MAGIC_SALT_LENGTH = SALTED_MAGIC.length + SALT_LEN;

	/**
	 * 获取去除头部盐的加密数据<br>
	 *
	 * @param encryptedData 密文
	 * @return 实际密文
	 */
	public static byte[] getData(final byte[] encryptedData) {
		if (ArrayUtil.startWith(encryptedData, SALTED_MAGIC)) {
			return Arrays.copyOfRange(encryptedData, SALTED_MAGIC.length + SALT_LEN, encryptedData.length);
		}
		return encryptedData;
	}

	/**
	 * 获取流中的加盐值<br>
	 * 不关闭流
	 *
	 * @param in 流
	 * @return salt
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] getSalt(final InputStream in) throws IORuntimeException {
		final byte[] headerBytes = new byte[SALTED_MAGIC.length];

		try{
			final int readHeaderSize = in.read(headerBytes);
			if (readHeaderSize < headerBytes.length ||
				!Arrays.equals(SALTED_MAGIC, headerBytes)) {
				throw new IORuntimeException("Unexpected magic header " + StrUtil.utf8Str(headerBytes));
			}

			final byte[] salt = new byte[SALT_LEN];
			final int readSaltSize = in.read(salt);
			if (readSaltSize < salt.length) {
				throw new IORuntimeException("Unexpected salt: " + StrUtil.utf8Str(salt));
			}
			return salt;
		} catch (final IOException e){
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取8位salt随机数<br>
	 *
	 * @param encryptedData 密文
	 * @return salt随机数
	 */
	public static byte[] getSalt(final byte[] encryptedData) {
		if (ArrayUtil.startWith(encryptedData, SALTED_MAGIC)) {
			return Arrays.copyOfRange(encryptedData, SALTED_MAGIC.length, MAGIC_SALT_LENGTH);
		}
		return null;
	}

	/**
	 * 为加密后的数据添加Magic头，生成的密文格式为：
	 * <pre>
	 *     Salted__[salt][data]
	 * </pre>
	 *
	 * @param data 数据
	 * @param salt 加盐值，必须8位，{@code null}表示返回原文
	 * @return 密文
	 */
	public static byte[] addMagic(final byte[] data, final byte[] salt) {
		if (null == salt) {
			return data;
		}
		Assert.isTrue(SALT_LEN == salt.length);
		return ByteUtil.concat(SALTED_MAGIC, salt, data);
	}

	/**
	 * 获取Magic头，生成的密文格式为：
	 * <pre>
	 *     Salted__[salt]
	 * </pre>
	 *
	 * @param salt 加盐值，必须8位，不能为{@code null}
	 * @return Magic头
	 */
	public static byte[] getSaltedMagic(final byte[] salt) {
		return ByteUtil.concat(SALTED_MAGIC, salt);
	}
}
