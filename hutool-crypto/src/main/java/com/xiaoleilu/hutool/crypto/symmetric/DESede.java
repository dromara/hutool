package com.xiaoleilu.hutool.crypto.symmetric;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.xiaoleilu.hutool.crypto.Mode;
import com.xiaoleilu.hutool.crypto.Padding;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * DESede是由DES对称加密算法改进后的一种对称加密算法。<br>
 * 使用 168 位的密钥对资料进行三次加密的一种机制；它通常（但非始终）提供极其强大的安全性。<br>
 * 如果三个 56 位的子元素都相同，则三重 DES 向后兼容 DES。<br>
 * Java中默认实现为：DESede/CBC/PKCS5Padding
 * 
 * @author Looly
 * @since 3.3.0
 */
public class DESede extends SymmetricCrypto {

	// ------------------------------------------------------------------------- Constrctor start
	/**
	 * 构造，默认DESede/CBC/PKCS5Padding，使用随机密钥
	 */
	public DESede() {
		super(SymmetricAlgorithm.DESede);
	}

	/**
	 * 构造，使用默认的DESede/CBC/PKCS5Padding
	 * 
	 * @param key 密钥
	 */
	public DESede(byte[] key) {
		super(SymmetricAlgorithm.DESede, key);
	}

	/**
	 * 构造，使用随机密钥
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public DESede(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 */
	public DESede(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DESede(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @since 3.3.0
	 */
	public DESede(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DESede(Mode mode, Padding padding, SecretKey key, IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 */
	public DESede(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度24位
	 */
	public DESede(String mode, String padding, byte[] key) {
		this(mode, padding, SecureUtil.generateKey("DES", key), null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度24位
	 * @param iv 加盐
	 */
	public DESede(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding, SecureUtil.generateKey("DES", key), null == iv ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥
	 */
	public DESede(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥
	 * @param iv 加盐
	 */
	public DESede(String mode, String padding, SecretKey key, IvParameterSpec iv) {
		super(StrUtil.format("DES/{}/{}", mode, padding), key, iv);
	}
	// ------------------------------------------------------------------------- Constrctor end

	/**
	 * 设置偏移向量
	 * 
	 * @param iv {@link IvParameterSpec}偏移向量
	 * @return 自身
	 */
	public DESede setIv(IvParameterSpec iv) {
		super.setParams(iv);
		return this;
	}

	/**
	 * 设置偏移向量
	 * 
	 * @param iv 偏移向量，加盐
	 * @return 自身
	 * @since 3.3.0
	 */
	public DESede setIv(byte[] iv) {
		setIv(new IvParameterSpec(iv));
		return this;
	}

}
