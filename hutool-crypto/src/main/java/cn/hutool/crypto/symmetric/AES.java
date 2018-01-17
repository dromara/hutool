package cn.hutool.crypto.symmetric;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;

/**
 * AES加密算法实现<br>
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法<br>
 * 对于Java中AES的默认模式是：AES/ECB/PKCS5Padding，如果使用CryptoJS，请调整为：padding: CryptoJS.pad.Pkcs7
 * 
 * @author Looly
 * @since 3.0.8
 */
public class AES extends SymmetricCrypto {

	//------------------------------------------------------------------------- Constrctor start
	/**
	 * 构造，默认AES/ECB/PKCS5Padding，使用随机密钥
	 */
	public AES() {
		super(SymmetricAlgorithm.AES);
	}

	/**
	 * 构造，使用默认的AES/ECB/PKCS5Padding
	 * 
	 * @param key 密钥
	 */
	public AES(byte[] key) {
		super(SymmetricAlgorithm.AES, key);
	}

	/**
	 * 构造，使用随机密钥
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public AES(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}
	
	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public AES(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}
	
	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 * @since 3.3.0
	 */
	public AES(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public AES(Mode mode, Padding padding, SecretKey key, IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 */
	public AES(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(String mode, String padding, byte[] key) {
		this(mode, padding, SecureUtil.generateKey("AES", key), null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 * @param iv 加盐
	 */
	public AES(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding, SecureUtil.generateKey("AES", key), null == iv ? null : new IvParameterSpec(iv));
	}
	
	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 * @param iv 加盐
	 */
	public AES(String mode, String padding, SecretKey key, IvParameterSpec iv) {
		super(StrUtil.format("AES/{}/{}", mode, padding), key, iv);
	}
	//------------------------------------------------------------------------- Constrctor end

	/**
	 * 设置偏移向量
	 * 
	 * @param iv {@link IvParameterSpec}偏移向量
	 * @return 自身
	 */
	public AES setIv(IvParameterSpec iv) {
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
	public AES setIv(byte[] iv) {
		setIv(new IvParameterSpec(iv));
		return this;
	}

}
