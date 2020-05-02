package cn.hutool.crypto.digest.mac;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.CryptoException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.IOException;
import java.io.InputStream;

/**
 * BouncyCastle的HMAC算法实现引擎，使用{@link Mac} 实现摘要<br>
 * 当引入BouncyCastle库时自动使用其作为Provider
 * 
 * @author Looly
 * @since 4.5.13
 */
public class BCHMacEngine implements MacEngine {

	private Mac mac;

	// ------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param digest 摘要算法，为{@link Digest} 的接口实现
	 * @param key 密钥
	 * @since 4.5.13
	 */
	public BCHMacEngine(Digest digest, byte[] key) {
		this(digest, new KeyParameter(key));
	}

	/**
	 * 构造
	 * 
	 * @param digest 摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @since 4.5.13
	 */
	public BCHMacEngine(Digest digest, CipherParameters params) {
		init(digest, params);
	}
	// ------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 初始化
	 * 
	 * @param digest 摘要算法
	 * @param params 参数，例如密钥可以用{@link KeyParameter}
	 * @return this
	 */
	public BCHMacEngine init(Digest digest, CipherParameters params) {
		mac = new HMac(digest);
		mac.init(params);
		return this;
	}

	@Override
	public byte[] digest(InputStream data, int bufferLength) {
		if (bufferLength < 1) {
			bufferLength = IoUtil.DEFAULT_BUFFER_SIZE;
		}
		final byte[] buffer = new byte[bufferLength];

		byte[] result;
		try {
			int read = data.read(buffer, 0, bufferLength);

			while (read > -1) {
				mac.update(buffer, 0, read);
				read = data.read(buffer, 0, bufferLength);
			}
			result = new byte[this.mac.getMacSize()];
			mac.doFinal(result, 0);
		} catch (IOException e) {
			throw new CryptoException(e);
		} finally {
			mac.reset();
		}
		return result;
	}

	/**
	 * 获得 {@link Mac}
	 * 
	 * @return {@link Mac}
	 */
	public Mac getMac() {
		return mac;
	}

	@Override
	public int getMacLength() {
		return mac.getMacSize();
	}

	@Override
	public String getAlgorithm() {
		return this.mac.getAlgorithmName();
	}
}
