package cn.hutool.crypto.digest.mac;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;

/**
 * 默认的HMAC算法实现引擎，使用{@link Mac} 实现摘要<br>
 * 当引入BouncyCastle库时自动使用其作为Provider
 * 
 * @author Looly
 *@since 4.5.13
 */
public class DefaultHMacEngine implements MacEngine {

	private Mac mac;
	
	// ------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param algorithm 算法
	 * @param key 密钥
	 * @since 4.5.13
	 */
	public DefaultHMacEngine(String algorithm, byte[] key) {
		init(algorithm, key);
	}
	
	/**
	 * 构造
	 * @param algorithm 算法
	 * @param key 密钥
	 * @since 4.5.13
	 */
	public DefaultHMacEngine(String algorithm, Key key) {
		init(algorithm, key);
	}
	// ------------------------------------------------------------------------------------------- Constructor end
	
	/**
	 * 初始化
	 * @param algorithm 算法
	 * @param key 密钥
	 * @return this
	 */
	public DefaultHMacEngine init(String algorithm, byte[] key){
		return init(algorithm, (null == key) ? null : new SecretKeySpec(key, algorithm));
	}
	
	/**
	 * 初始化
	 * @param algorithm 算法
	 * @param key 密钥 {@link SecretKey}
	 * @return this
	 * @throws CryptoException Cause by IOException
	 */
	public DefaultHMacEngine init(String algorithm, Key key){
		try {
			mac = SecureUtil.createMac(algorithm);
			if(null == key){
				key = SecureUtil.generateKey(algorithm);
			}
			mac.init(key);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
		return this;
	}

	@Override
	public byte[] digest(InputStream data, int bufferLength) {
		if (bufferLength < 1) {
			bufferLength = IoUtil.DEFAULT_BUFFER_SIZE;
		}
		byte[] buffer = new byte[bufferLength];

		byte[] result;
		try {
			int read = data.read(buffer, 0, bufferLength);

			while (read > -1) {
				mac.update(buffer, 0, read);
				read = data.read(buffer, 0, bufferLength);
			}
			result = mac.doFinal();
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
		return mac.getMacLength();
	}

	@Override
	public String getAlgorithm() {
		return this.mac.getAlgorithm();
	}
}
