/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.codec.hash;

import org.dromara.hutool.core.exception.HutoolException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Ketama算法，用于在一致性Hash中快速定位服务器位置
 *
 * @author looly
 * @since 5.7.20
 */
public class KetamaHash implements Hash64<byte[]>, Hash32<byte[]> {

	@Override
	public long hash64(final byte[] key) {
		final byte[] bKey = md5(key);
		return ((long) (bKey[3] & 0xFF) << 24)
				| ((long) (bKey[2] & 0xFF) << 16)
				| ((long) (bKey[1] & 0xFF) << 8)
				| (bKey[0] & 0xFF);
	}

	@Override
	public int hash32(final byte[] key) {
		return (int) (hash64(key) & 0xffffffffL);
	}

	@Override
	public Number encode(final byte[] key) {
		return hash64(key);
	}

	/**
	 * 计算MD5值，使用UTF-8编码
	 *
	 * @param key 被计算的键
	 * @return MD5值
	 */
	private static byte[] md5(final byte[] key) {
		final MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException e) {
			throw new HutoolException("MD5 algorithm not suooport!", e);
		}
		return md5.digest(key);
	}
}
