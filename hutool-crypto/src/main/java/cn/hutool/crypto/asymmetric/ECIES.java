package cn.hutool.crypto.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * ECIES（集成加密方案，elliptic curve integrate encrypt scheme）
 *
 * <p>
 * 详细介绍见：https://blog.csdn.net/baidu_26954729/article/details/90437344
 * 此算法必须引入Bouncy Castle库
 *
 * @author loolly
 * @since 5.3.10
 */
public class ECIES extends AsymmetricCrypto{

	/** 默认的ECIES算法 */
	private static final String ALGORITHM_ECIES = "ECIES";

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，生成新的私钥公钥对
	 */
	public ECIES() {
		super(ALGORITHM_ECIES);
	}

	/**
	 * 构造，生成新的私钥公钥对
	 *
	 * @param eciesAlgorithm 自定义ECIES算法，例如ECIESwithDESede/NONE/PKCS7Padding
	 */
	public ECIES(String eciesAlgorithm) {
		super(eciesAlgorithm);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 */
	public ECIES(String privateKeyStr, String publicKeyStr) {
		super(ALGORITHM_ECIES, privateKeyStr, publicKeyStr);
	}

	/**
	 * 构造<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param eciesAlgorithm 自定义ECIES算法，例如ECIESwithDESede/NONE/PKCS7Padding
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 * @since 4.5.8
	 */
	public ECIES(String eciesAlgorithm, String privateKeyStr, String publicKeyStr) {
		super(eciesAlgorithm, privateKeyStr, publicKeyStr);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public ECIES(byte[] privateKey, byte[] publicKey) {
		super(ALGORITHM_ECIES, privateKey, publicKey);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @since 3.1.1
	 */
	public ECIES(PrivateKey privateKey, PublicKey publicKey) {
		super(ALGORITHM_ECIES, privateKey, publicKey);
	}

	/**
	 * 构造 <br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param eciesAlgorithm 自定义ECIES算法，例如ECIESwithDESede/NONE/PKCS7Padding
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @since 4.5.8
	 */
	public ECIES(String eciesAlgorithm, PrivateKey privateKey, PublicKey publicKey) {
		super(eciesAlgorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end
}
