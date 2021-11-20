package cn.hutool.crypto;

import javax.crypto.Cipher;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * {@link Cipher}包装类，提供初始化模式等额外方法<br>
 * 包装之后可提供自定义或默认的：
 * <ul>
 *     <li>{@link AlgorithmParameterSpec}</li>
 *     <li>{@link SecureRandom}</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.17
 */
public class CipherWrapper {

	private final Cipher cipher;
	/**
	 * 算法参数
	 */
	private AlgorithmParameterSpec params;
	/**
	 * 随机数生成器，可自定义随机数种子
	 */
	private SecureRandom random;

	/**
	 * 构造
	 *
	 * @param algorithm 算法名称
	 */
	public CipherWrapper(String algorithm) {
		this(SecureUtil.createCipher(algorithm));
	}

	/**
	 * 构造
	 *
	 * @param cipher {@link Cipher}
	 */
	public CipherWrapper(Cipher cipher) {
		this.cipher = cipher;
	}

	/**
	 * 获取{@link AlgorithmParameterSpec}<br>
	 * 在某些算法中，需要特别的参数，例如在ECIES中，此处为IESParameterSpec
	 *
	 * @return {@link AlgorithmParameterSpec}
	 */
	public AlgorithmParameterSpec getParams() {
		return this.params;
	}

	/**
	 * 设置 {@link AlgorithmParameterSpec}，通常用于加盐或偏移向量
	 *
	 * @param params {@link AlgorithmParameterSpec}
	 * @return this
	 */
	public CipherWrapper setParams(AlgorithmParameterSpec params) {
		this.params = params;
		return this;
	}

	/**
	 * 设置随机数生成器，可自定义随机数种子
	 *
	 * @param random 随机数生成器，可自定义随机数种子
	 * @return this
	 */
	public CipherWrapper setRandom(SecureRandom random) {
		this.random = random;
		return this;
	}

	/**
	 * 获取被包装的{@link Cipher}
	 *
	 * @return {@link Cipher}
	 */
	public Cipher getCipher() {
		return this.cipher;
	}

	/**
	 * 初始化{@link Cipher}为加密或者解密模式
	 *
	 * @param mode 模式，见{@link Cipher#ENCRYPT_MODE} 或 {@link Cipher#DECRYPT_MODE}
	 * @param key  密钥
	 * @return this
	 * @throws InvalidKeyException                无效key
	 * @throws InvalidAlgorithmParameterException 无效算法
	 */
	public CipherWrapper initMode(int mode, Key key)
			throws InvalidKeyException, InvalidAlgorithmParameterException {
		final Cipher cipher = this.cipher;
		final AlgorithmParameterSpec params = this.params;
		final SecureRandom random = this.random;
		if (null != params) {
			if (null != random) {
				cipher.init(mode, key, params, random);
			} else {
				cipher.init(mode, key, params);
			}
		} else {
			if (null != random) {
				cipher.init(mode, key, random);
			} else {
				cipher.init(mode, key);
			}
		}
		return this;
	}
}
