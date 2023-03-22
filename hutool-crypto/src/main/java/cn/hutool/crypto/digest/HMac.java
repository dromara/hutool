package cn.hutool.crypto.digest;

import cn.hutool.crypto.digest.mac.Mac;
import cn.hutool.crypto.digest.mac.MacEngine;
import cn.hutool.crypto.digest.mac.MacEngineFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * HMAC摘要算法<br>
 * HMAC，全称为“Hash Message Authentication Code”，中文名“散列消息鉴别码”<br>
 * 主要是利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。<br>
 * 一般的，消息鉴别码用于验证传输于两个共 同享有一个密钥的单位之间的消息。<br>
 * HMAC 可以与任何迭代散列函数捆绑使用。MD5 和 SHA-1 就是这种散列函数。HMAC 还可以使用一个用于计算和确认消息鉴别值的密钥。<br>
 * 注意：此对象实例化后为非线程安全！
 *
 * @author Looly
 */
public class HMac extends Mac {
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，自动生成密钥
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 */
	public HMac(HmacAlgorithm algorithm) {
		this(algorithm, (Key) null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key       密钥
	 */
	public HMac(HmacAlgorithm algorithm, byte[] key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key       密钥
	 */
	public HMac(HmacAlgorithm algorithm, Key key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public HMac(String algorithm, byte[] key) {
		this(algorithm, new SecretKeySpec(key, algorithm));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public HMac(String algorithm, Key key) {
		this(algorithm, key, null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param spec      {@link AlgorithmParameterSpec}
	 * @since 5.6.12
	 */
	public HMac(String algorithm, Key key, AlgorithmParameterSpec spec) {
		this(MacEngineFactory.createEngine(algorithm, key, spec));
	}

	/**
	 * 构造
	 *
	 * @param engine MAC算法实现引擎
	 * @since 4.5.13
	 */
	public HMac(MacEngine engine) {
		super(engine);
	}
	// ------------------------------------------------------------------------------------------- Constructor end
}
