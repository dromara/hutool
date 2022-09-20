package cn.hutool.core.codec.hash;

import cn.hutool.core.exceptions.UtilException;

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
			throw new UtilException("MD5 algorithm not suooport!", e);
		}
		return md5.digest(key);
	}
}
