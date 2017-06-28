package com.xiaoleilu.hutool.crypto.symmetric;

import javax.crypto.spec.IvParameterSpec;

import com.xiaoleilu.hutool.crypto.Mode;
import com.xiaoleilu.hutool.crypto.Padding;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * DES加密算法实现
 * @author Looly
 *@since 3.0.8
 */
public class DES extends SymmetricCrypto{
	
	/**
	 * 构造，默认DES/ECB/PKCS5Padding，使用随机密钥
	 */
	public DES() {
		super(SymmetricAlgorithm.DES);
	}
	
	/**
	 * 构造，使用默认的DES/ECB/PKCS5Padding
	 * @param key 密钥
	 */
	public DES(byte[] key) {
		super(SymmetricAlgorithm.DES, key);
	}
	
	/**
	 * 构造，使用随机密钥
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public DES(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}
	
	/**
	 * 构造
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 */
	public DES(Mode mode, Padding padding, byte[] key) {
		this(mode.name(), padding.name(), key);
	}
	
	/**
	 * 构造
	 * @param mode 模式
	 * @param padding 补码方式
	 */
	public DES(String mode, String padding) {
		this(mode, padding, null);
	}

	/**
	 * 构造
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，支持三种密钥长度：128、192、256位
	 */
	public DES(String mode, String padding, byte[] key) {
		super(StrUtil.format("DES/{}/{}", mode, padding), key);
	}
	
	/**
	 * 设置偏移向量
	 * @param iv {@link IvParameterSpec}偏移向量
	 * @return 自身
	 */
	public DES setIv(IvParameterSpec iv){
		super.setParams(iv);
		return this;
	}

}
