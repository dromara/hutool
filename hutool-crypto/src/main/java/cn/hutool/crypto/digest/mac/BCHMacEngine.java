package cn.hutool.crypto.digest.mac;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * BouncyCastle的HMAC算法实现引擎，使用{@link Mac} 实现摘要<br>
 * 当引入BouncyCastle库时自动使用其作为Provider
 *
 * @author Looly
 * @since 4.5.13
 */
public class BCHMacEngine extends BCMacEngine {

	// ------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param digest 摘要算法，为{@link Digest} 的接口实现
	 * @param key    密钥
	 * @param iv     加盐
	 * @since 5.7.12
	 */
	public BCHMacEngine(Digest digest, byte[] key, byte[] iv) {
		this(digest, new ParametersWithIV(new KeyParameter(key), iv));
	}

	/**
	 * 构造
	 *
	 * @param digest 摘要算法，为{@link Digest} 的接口实现
	 * @param key    密钥
	 * @since 4.5.13
	 */
	public BCHMacEngine(Digest digest, byte[] key) {
		this(digest, new KeyParameter(key));
	}

	/**
	 * 构造
	 *
	 * @param digest 摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @since 4.5.13
	 */
	public BCHMacEngine(Digest digest, CipherParameters params) {
		this(new HMac(digest), params);
	}

	/**
	 * 构造
	 *
	 * @param mac {@link HMac}
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @since 5.8.0
	 */
	public BCHMacEngine(HMac mac, CipherParameters params) {
		super(mac, params);
	}
	// ------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 初始化
	 *
	 * @param digest 摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @return this
	 * @see #init(Mac, CipherParameters)
	 */
	public BCHMacEngine init(Digest digest, CipherParameters params) {
		return (BCHMacEngine) init(new HMac(digest), params);
	}
}
