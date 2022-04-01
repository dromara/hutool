package cn.hutool.crypto.digest.mac;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.Key;

/**
 * {@link CBCBlockCipherMac}实现的MAC算法，使用CBC Block方式
 *
 * @author looly
 * @since 5.8.0
 */
public class CBCBlockCipherMacEngine extends BCMacEngine {

	/**
	 * 构造
	 *
	 * @param digest        摘要算法，为{@link Digest} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 * @param iv            加盐
	 * @since 5.7.12
	 */
	public CBCBlockCipherMacEngine(BlockCipher digest, int macSizeInBits, Key key, byte[] iv) {
		this(digest, macSizeInBits, key.getEncoded(), iv);
	}

	/**
	 * 构造
	 *
	 * @param digest        摘要算法，为{@link Digest} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 * @param iv            加盐
	 */
	public CBCBlockCipherMacEngine(BlockCipher digest, int macSizeInBits, byte[] key, byte[] iv) {
		this(digest, macSizeInBits, new ParametersWithIV(new KeyParameter(key), iv));
	}

	/**
	 * 构造
	 *
	 * @param cipher        算法，为{@link BlockCipher} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 */
	public CBCBlockCipherMacEngine(BlockCipher cipher, int macSizeInBits, Key key) {
		this(cipher, macSizeInBits, key.getEncoded());
	}

	/**
	 * 构造
	 *
	 * @param cipher        算法，为{@link BlockCipher} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param key           密钥
	 */
	public CBCBlockCipherMacEngine(BlockCipher cipher, int macSizeInBits, byte[] key) {
		this(cipher, macSizeInBits, new KeyParameter(key));
	}

	/**
	 * 构造
	 *
	 * @param cipher        算法，为{@link BlockCipher} 的接口实现
	 * @param macSizeInBits mac结果的bits长度，必须为8的倍数
	 * @param params        参数，例如密钥可以用{@link KeyParameter}
	 */
	public CBCBlockCipherMacEngine(BlockCipher cipher, int macSizeInBits, CipherParameters params) {
		this(new CBCBlockCipherMac(cipher, macSizeInBits), params);
	}

	/**
	 * 构造
	 *
	 * @param mac    {@link CBCBlockCipherMac}
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 */
	public CBCBlockCipherMacEngine(CBCBlockCipherMac mac, CipherParameters params) {
		super(mac, params);
	}

	/**
	 * 初始化
	 *
	 * @param cipher {@link BlockCipher}
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @return this
	 * @see #init(Mac, CipherParameters)
	 */
	public CBCBlockCipherMacEngine init(BlockCipher cipher, CipherParameters params) {
		return (CBCBlockCipherMacEngine) init(new CBCBlockCipherMac(cipher), params);
	}
}
