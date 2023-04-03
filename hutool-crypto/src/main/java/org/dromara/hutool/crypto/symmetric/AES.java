/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.Mode;
import org.dromara.hutool.crypto.Padding;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES加密算法实现<br>
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法<br>
 * 对于Java中AES的默认模式是：AES/ECB/PKCS5Padding，如果使用CryptoJS，请调整为：padding: CryptoJS.pad.Pkcs7
 *
 * <p>
 * 相关概念说明：
 * <pre>
 * mode:    加密算法模式，是用来描述加密算法（此处特指分组密码，不包括流密码，）在加密时对明文分组的模式，它代表了不同的分组方式
 * padding: 补码方式是在分组密码中，当明文长度不是分组长度的整数倍时，需要在最后一个分组中填充一些数据使其凑满一个分组的长度。
 * iv:      在对明文分组加密时，会将明文分组与前一个密文分组进行XOR运算（即异或运算），但是加密第一个明文分组时不存在“前一个密文分组”，
 *          因此需要事先准备一个与分组长度相等的比特序列来代替，这个比特序列就是偏移量。
 * </pre>
 * <p>
 * 相关概念见：https://blog.csdn.net/OrangeJack/article/details/82913804
 *
 * @author Looly
 * @since 3.0.8
 */
public class AES extends SymmetricCrypto {
	private static final long serialVersionUID = 1L;

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
	public AES(final byte[] key) {
		super(SymmetricAlgorithm.AES, key);
	}

	/**
	 * 构造，使用默认的AES/ECB/PKCS5Padding
	 *
	 * @param key 密钥
	 * @since 5.5.2
	 */
	public AES(final SecretKey key) {
		super(SymmetricAlgorithm.AES, key);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public AES(final Mode mode, final Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(final Mode mode, final Padding padding, final byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      偏移向量，加盐
	 * @since 3.3.0
	 */
	public AES(final Mode mode, final Padding padding, final byte[] key, final byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @since 3.3.0
	 */
	public AES(final Mode mode, final Padding padding, final SecretKey key) {
		this(mode, padding, key, (IvParameterSpec) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      偏移向量，加盐
	 * @since 4.6.7
	 */
	public AES(final Mode mode, final Padding padding, final SecretKey key, final byte[] iv) {
		this(mode, padding, key, ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode       模式{@link Mode}
	 * @param padding    {@link Padding}补码方式
	 * @param key        密钥，支持三种密钥长度：128、192、256位
	 * @param paramsSpec 算法参数，例如加盐等
	 * @since 3.3.0
	 */
	public AES(final Mode mode, final Padding padding, final SecretKey key, final AlgorithmParameterSpec paramsSpec) {
		this(mode.name(), padding.name(), key, paramsSpec);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 */
	public AES(final String mode, final String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(final String mode, final String padding, final byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      加盐
	 */
	public AES(final String mode, final String padding, final byte[] key, final byte[] iv) {
		this(mode, padding,//
				KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key),//
				ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(final String mode, final String padding, final SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode       模式
	 * @param padding    补码方式
	 * @param key        密钥，支持三种密钥长度：128、192、256位
	 * @param paramsSpec 算法参数，例如加盐等
	 */
	public AES(final String mode, final String padding, final SecretKey key, final AlgorithmParameterSpec paramsSpec) {
		super(StrUtil.format("AES/{}/{}", mode, padding), key, paramsSpec);
	}
	//------------------------------------------------------------------------- Constrctor end
}
