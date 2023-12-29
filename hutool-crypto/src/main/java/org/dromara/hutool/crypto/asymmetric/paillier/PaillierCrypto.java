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

package org.dromara.hutool.crypto.asymmetric.paillier;

import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.asymmetric.AbstractAsymmetricCrypto;
import org.dromara.hutool.crypto.asymmetric.KeyType;

import javax.crypto.Cipher;
import java.security.*;

/**
 * 同态加密算法Paillier<br>
 * 来自：https://github.com/peterstefanov/paillier<br>
 * 来自：https://github.com/dromara/hutool/pull/3131
 * <p>
 * 加法同态，存在有效算法+，E(x+y)=E(x)+E(y)或者 x+y=D(E(x)+E(y))成立，并且不泄漏 x 和 y。
 * 乘法同态，存在有效算法*，E(x×y)=E(x)*E(y)或者 xy=D(E(x)*E(y))成立，并且不泄漏 x 和 y。
 * <p>
 * 方案安全性可以归约到判定性合数剩余假设（Decisional Composite Residuosity Assumption, DCRA），即给定一个合数n和整数z，判定z是否在n^2下是否是n次剩余是困难的。
 * 这个假设经过了几十年的充分研究，到目前为止还没有多项式时间的算法可以攻破，所以Paillier加密方案的安全性被认为相当可靠。
 * <p>
 * 字符串文本加解密相互配对,此时无法使用同态加法和同态乘法
 * 数值类型不可使用字符串加解密
 * <p>
 * 公钥加密和同态加法/同态乘法运算
 * 私钥解密
 *
 * @author Revers, peterstefanov
 **/
public class PaillierCrypto extends AbstractAsymmetricCrypto<PaillierCrypto> {
	private static final long serialVersionUID = 1L;

	private final PaillierCipherSpiImpl spi;

	/**
	 * 构造，使用随机密钥对
	 */
	public PaillierCrypto() {
		this(PaillierKeyPairGenerator.of().generateKeyPair());
	}

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param keyPair 密钥对
	 */
	public PaillierCrypto(final KeyPair keyPair) {
		this(keyPair.getPrivate(), keyPair.getPublic());
	}

	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 */
	public PaillierCrypto(final PrivateKey privateKey, final PublicKey publicKey) {
		super(PaillierKey.ALGORITHM_NAME, privateKey, publicKey);
		this.spi = new PaillierCipherSpiImpl();
	}

	@Override
	public byte[] encrypt(final byte[] data, final KeyType keyType) {
		final Key key = getKeyByType(keyType);
		lock.lock();
		try {
			initMode(Cipher.ENCRYPT_MODE, key);
			return doFinal(data, 0, data.length);
		} catch (final Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public byte[] decrypt(final byte[] bytes, final KeyType keyType) {
		final Key key = getKeyByType(keyType);
		lock.lock();
		try {
			initMode(Cipher.DECRYPT_MODE, key);
			return doFinal(bytes, 0, bytes.length);
		} catch (final Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 初始化模式
	 * <ul>
	 *     <li>加密模式下，使用{@link Cipher#ENCRYPT_MODE}，密钥使用公钥</li>
	 *     <li>解密模式下，使用{@link Cipher#DECRYPT_MODE}，密钥使用私钥</li>
	 * </ul>
	 *
	 * @param mode 模式，可选{@link Cipher#ENCRYPT_MODE}或{@link Cipher#DECRYPT_MODE}
	 * @param key  公钥或私钥
	 * @return this
	 * @throws InvalidKeyException 密钥错误
	 */
	public PaillierCrypto initMode(final int mode, final Key key) throws InvalidKeyException {
		this.spi.engineInit(mode, key, null);
		return this;
	}

	/**
	 * 执行加密或解密数据
	 *
	 * @param input       数据
	 * @param inputOffset 开始位置
	 * @param inputLen    处理长度
	 * @return 执行后的数据
	 */
	public byte[] doFinal(final byte[] input, final int inputOffset, final int inputLen) {
		return this.spi.engineDoFinal(input, inputOffset, inputLen);
	}
}
