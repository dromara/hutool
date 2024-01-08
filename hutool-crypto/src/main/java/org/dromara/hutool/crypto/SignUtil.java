/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import org.dromara.hutool.crypto.asymmetric.Sign;
import org.dromara.hutool.crypto.asymmetric.SignAlgorithm;
import org.dromara.hutool.crypto.digest.DigestAlgorithm;
import org.dromara.hutool.crypto.digest.Digester;
import org.dromara.hutool.crypto.provider.GlobalProviderFactory;
import org.dromara.hutool.crypto.symmetric.SymmetricCrypto;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Signature;
import java.util.Map;

/**
 * 签名工具类<br>
 * 封装包括：
 * <ul>
 *     <li>非对称签名，签名算法支持见{@link SignAlgorithm}</li>
 *     <li>对称签名，支持Map类型参数排序后签名</li>
 *     <li>摘要签名，支持Map类型参数排序后签名，签名方法见：{@link DigestAlgorithm}</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.20
 */
public class SignUtil {

	/**
	 * 生成签名对象，仅用于非对称加密
	 *
	 * @param asymmetricAlgorithm {@link AsymmetricAlgorithm} 非对称加密算法
	 * @param digestAlgorithm     {@link DigestAlgorithm} 摘要算法
	 * @return {@link Signature}
	 */
	public static Signature createSignature(final AsymmetricAlgorithm asymmetricAlgorithm, final DigestAlgorithm digestAlgorithm) {
		return createSignature(SecureUtil.generateAlgorithm(asymmetricAlgorithm, digestAlgorithm));
	}

	/**
	 * 创建{@link Signature}签名对象
	 *
	 * @param algorithm 算法
	 * @return {@link Signature}
	 * @since 5.7.0
	 */
	public static Signature createSignature(final String algorithm) {
		final Provider provider = GlobalProviderFactory.getProvider();

		final Signature signature;
		try {
			signature = (null == provider) ? Signature.getInstance(algorithm) : Signature.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}

		return signature;
	}

	/**
	 * 创建签名算法对象<br>
	 * 生成新的私钥公钥对
	 *
	 * @param algorithm 签名算法
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(final SignAlgorithm algorithm) {
		return new Sign(algorithm);
	}

	/**
	 * 创建签名算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm        签名算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64  公钥Base64
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(final SignAlgorithm algorithm, final String privateKeyBase64, final String publicKeyBase64) {
		return new Sign(algorithm, privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 创建Sign算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm  算法枚举
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(final SignAlgorithm algorithm, final byte[] privateKey, final byte[] publicKey) {
		return new Sign(algorithm, privateKey, publicKey);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param crypto      对称加密算法
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(final SymmetricCrypto crypto, final Map<?, ?> params, final String... otherParams) {
		return signParams(crypto, params, StrUtil.EMPTY, StrUtil.EMPTY, true, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 *
	 * @param crypto            对称加密算法
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(final SymmetricCrypto crypto, final Map<?, ?> params, final String separator,
									final String keyValueSeparator, final boolean isIgnoreNull, final String... otherParams) {
		return crypto.encryptHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
	}

	/**
	 * 对参数做md5签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParamsMd5(final Map<?, ?> params, final String... otherParams) {
		return signParams(DigestAlgorithm.MD5, params, otherParams);
	}

	/**
	 * 对参数做Sha1签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.8
	 */
	public static String signParamsSha1(final Map<?, ?> params, final String... otherParams) {
		return signParams(DigestAlgorithm.SHA1, params, otherParams);
	}

	/**
	 * 对参数做Sha256签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParamsSha256(final Map<?, ?> params, final String... otherParams) {
		return signParams(DigestAlgorithm.SHA256, params, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param digestAlgorithm 摘要算法
	 * @param params          参数
	 * @param otherParams     其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(final DigestAlgorithm digestAlgorithm, final Map<?, ?> params, final String... otherParams) {
		return signParams(digestAlgorithm, params, StrUtil.EMPTY, StrUtil.EMPTY, true, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 *
	 * @param digestAlgorithm   摘要算法
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(final DigestAlgorithm digestAlgorithm, final Map<?, ?> params, final String separator,
									final String keyValueSeparator, final boolean isIgnoreNull, final String... otherParams) {
		return new Digester(digestAlgorithm).digestHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
	}
}
