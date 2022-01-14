package cn.hutool.core.lang.hash;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.StrUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Ketama算法，用于在一致性Hash中快速定位服务器位置
 *
 * @author looly
 * @since 5.7.20
 */
public class KetamaHash implements Hash64<String>, Hash32<String> {

	@Override
	public long hash64(String key) {
		byte[] bKey = md5(key);
		return ((long) (bKey[3] & 0xFF) << 24)
				| ((long) (bKey[2] & 0xFF) << 16)
				| ((long) (bKey[1] & 0xFF) << 8)
				| (bKey[0] & 0xFF);
	}

	@Override
	public int hash32(String key) {
		return (int) (hash64(key) & 0xffffffffL);
	}

	@Override
	public Number hash(String key) {
		return hash64(key);
	}

	/**
	 * 计算MD5值，使用UTF-8编码
	 *
	 * @param key 被计算的键
	 * @return MD5值
	 */
	private static byte[] md5(String key) {
		final MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new UtilException("MD5 algorithm not suooport!", e);
		}
		return md5.digest(StrUtil.utf8Bytes(key));
	}
}
