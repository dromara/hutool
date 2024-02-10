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

package org.dromara.hutool.crypto.asymmetric;

import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.dromara.hutool.crypto.SignUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Set;

/**
 * 签名包装，{@link Signature} 包装类
 *
 * @author looly
 * @since 3.3.0
 */
public class Sign extends BaseAsymmetric<Sign> {
	private static final long serialVersionUID = 1L;

	/** 签名，用于签名和验证 */
	protected Signature signature;

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，创建新的私钥公钥对
	 *
	 * @param algorithm {@link SignAlgorithm}
	 */
	public Sign(final SignAlgorithm algorithm) {
		this(algorithm, null, (byte[]) null);
	}

	/**
	 * 构造，创建新的私钥公钥对
	 *
	 * @param algorithm 算法
	 */
	public Sign(final String algorithm) {
		this(algorithm, null, (byte[]) null);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm {@link SignAlgorithm}
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr 公钥Hex或Base64表示
	 */
	public Sign(final SignAlgorithm algorithm, final String privateKeyStr, final String publicKeyStr) {
		this(algorithm.getValue(), SecureUtil.decode(privateKeyStr), SecureUtil.decode(publicKeyStr));
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm {@link SignAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(final SignAlgorithm algorithm, final byte[] privateKey, final byte[] publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm {@link SignAlgorithm}
	 * @param keyPair 密钥对（包括公钥和私钥）
	 */
	public Sign(final SignAlgorithm algorithm, final KeyPair keyPair) {
		this(algorithm.getValue(), keyPair);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm {@link SignAlgorithm}
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(final SignAlgorithm algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		this(algorithm.getValue(), privateKey, publicKey);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm 非对称加密算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 */
	public Sign(final String algorithm, final String privateKeyBase64, final String publicKeyBase64) {
		this(algorithm, Base64.decode(privateKeyBase64), Base64.decode(publicKeyBase64));
	}

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(final String algorithm, final byte[] privateKey, final byte[] publicKey) {
		this(algorithm, //
				KeyUtil.generatePrivateKey(algorithm, privateKey), //
				KeyUtil.generatePublicKey(algorithm, publicKey)//
		);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm 算法，见{@link SignAlgorithm}
	 * @param keyPair 密钥对（包括公钥和私钥）
	 */
	public Sign(final String algorithm, final KeyPair keyPair) {
		this(algorithm, keyPair.getPrivate(), keyPair.getPublic());
	}

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public Sign(final String algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return this
	 */
	@Override
	public Sign init(final String algorithm, final PrivateKey privateKey, final PublicKey publicKey) {
		signature = SignUtil.createSignature(algorithm);
		super.init(algorithm, privateKey, publicKey);
		return this;
	}

	/**
	 * 设置签名的参数
	 *
	 * @param params {@link AlgorithmParameterSpec}
	 * @return this
	 * @since 4.6.5
	 */
	public Sign setParameter(final AlgorithmParameterSpec params) {
		try {
			this.signature.setParameter(params);
		} catch (final InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
		return this;
	}

	// --------------------------------------------------------------------------------- Sign and Verify
	/**
	 * 生成文件签名
	 *
	 * @param data 被签名数据
	 * @param charset 编码
	 * @return 签名
	 * @since 5.7.0
	 */
	public byte[] sign(final String data, final Charset charset) {
		return sign(ByteUtil.toBytes(data, charset));
	}

	/**
	 * 生成文件签名
	 *
	 * @param data 被签名数据
	 * @return 签名
	 * @since 5.7.0
	 */
	public byte[] sign(final String data) {
		return sign(data, CharsetUtil.UTF_8);
	}

	/**
	 * 生成文件签名，并转为16进制字符串
	 *
	 * @param data 被签名数据
	 * @param charset 编码
	 * @return 签名
	 * @since 5.7.0
	 */
	public String signHex(final String data, final Charset charset) {
		return HexUtil.encodeStr(sign(data, charset));
	}

	/**
	 * 生成文件签名
	 *
	 * @param data 被签名数据
	 * @return 签名
	 * @since 5.7.0
	 */
	public String signHex(final String data) {
		return signHex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 用私钥对信息生成数字签名
	 *
	 * @param data 加密数据
	 * @return 签名
	 */
	public byte[] sign(final byte[] data) {
		return sign(new ByteArrayInputStream(data), -1);
	}

	/**
	 * 生成签名，并转为16进制字符串<br>
	 *
	 * @param data 被签名数据
	 * @return 签名
	 * @since 5.7.0
	 */
	public String signHex(final byte[] data) {
		return HexUtil.encodeStr(sign(data));
	}

	/**
	 * 生成签名，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param data 被签名数据
	 * @return 签名
	 * @since 5.7.0
	 */
	public String signHex(final InputStream data) {
		return HexUtil.encodeStr(sign(data));
	}

	/**
	 * 生成签名，使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param data {@link InputStream} 数据流
	 * @return 签名bytes
	 * @since 5.7.0
	 */
	public byte[] sign(final InputStream data) {
		return sign(data, IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 生成签名，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 *
	 * @param data 被签名数据
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 签名
	 * @since 5.7.0
	 */
	public String digestHex(final InputStream data, final int bufferLength) {
		return HexUtil.encodeStr(sign(data, bufferLength));
	}

	/**
	 * 生成签名
	 *
	 * @param data {@link InputStream} 数据流
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 签名bytes
	 * @since 5.7.0
	 */
	public byte[] sign(final InputStream data, int bufferLength){
		if (bufferLength < 1) {
			bufferLength = IoUtil.DEFAULT_BUFFER_SIZE;
		}

		final byte[] buffer = new byte[bufferLength];
		lock.lock();
		try {
			signature.initSign(this.privateKey);
			final byte[] result;
			try {
				int read = data.read(buffer, 0, bufferLength);
				while (read > -1) {
					signature.update(buffer, 0, read);
					read = data.read(buffer, 0, bufferLength);
				}
				result = signature.sign();
			} catch (final Exception e) {
				throw new CryptoException(e);
			}
			return result;
		} catch (final Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 用公钥检验数字签名的合法性
	 *
	 * @param data 数据
	 * @param sign 签名
	 * @return 是否验证通过
	 */
	public boolean verify(final byte[] data, final byte[] sign) {
		lock.lock();
		try {
			signature.initVerify(this.publicKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (final Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获得签名对象
	 *
	 * @return {@link Signature}
	 */
	public Signature getSignature() {
		return signature;
	}

	/**
	 * 设置签名
	 *
	 * @param signature 签名对象 {@link Signature}
	 * @return 自身 {@link AsymmetricCrypto}
	 */
	public Sign setSignature(final Signature signature) {
		this.signature = signature;
		return this;
	}

	/**
	 * 设置{@link Certificate} 为PublicKey<br>
	 * 如果Certificate是X509Certificate，我们需要检查是否有密钥扩展
	 *
	 * @param certificate {@link Certificate}
	 * @return this
	 */
	public Sign setCertificate(final Certificate certificate) {
		// If the certificate is of type X509Certificate,
		// we should check whether it has a Key Usage
		// extension marked as critical.
		if (certificate instanceof X509Certificate) {
			// Check whether the cert has a key usage extension
			// marked as a critical extension.
			// The OID for KeyUsage extension is 2.5.29.15.
			final X509Certificate cert = (X509Certificate) certificate;
			final Set<String> critSet = cert.getCriticalExtensionOIDs();

			if (CollUtil.isNotEmpty(critSet) && critSet.contains("2.5.29.15")) {
				final boolean[] keyUsageInfo = cert.getKeyUsage();
				// keyUsageInfo[0] is for digitalSignature.
				if ((keyUsageInfo != null) && (keyUsageInfo[0] == false)) {
					throw new CryptoException("Wrong key usage");
				}
			}
		}
		this.publicKey = certificate.getPublicKey();
		return this;
	}
}
