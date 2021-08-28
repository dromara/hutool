package cn.hutool.core.net;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * {@link SSLContext}构建器，可以自定义：<br>
 * <ul>
 *     <li>协议（protocol），默认TLS</li>
 *     <li>{@link KeyManager}，默认空</li>
 *     <li>{@link TrustManager}，默认{@link DefaultTrustManager}，即信任全部</li>
 *     <li>{@link SecureRandom}</li>
 * </ul>
 *
 * 构建后可获得{@link SSLContext}，通过调用{@link SSLContext#getSocketFactory()}获取{@link javax.net.ssl.SSLSocketFactory}
 *
 * @author Looly
 * @since 5.5.2
 */
public class SSLContextBuilder implements SSLProtocols {

	private String protocol = TLS;
	private KeyManager[] keyManagers;
	private TrustManager[] trustManagers = {DefaultTrustManager.INSTANCE};
	private SecureRandom secureRandom = new SecureRandom();


	/**
	 * 创建 SSLContextBuilder
	 *
	 * @return SSLContextBuilder
	 */
	public static SSLContextBuilder create() {
		return new SSLContextBuilder();
	}

	/**
	 * 设置协议。例如TLS等
	 *
	 * @param protocol 协议
	 * @return 自身
	 */
	public SSLContextBuilder setProtocol(String protocol) {
		if (StrUtil.isNotBlank(protocol)) {
			this.protocol = protocol;
		}
		return this;
	}

	/**
	 * 设置信任信息
	 *
	 * @param trustManagers TrustManager列表
	 * @return 自身
	 */
	public SSLContextBuilder setTrustManagers(TrustManager... trustManagers) {
		if (ArrayUtil.isNotEmpty(trustManagers)) {
			this.trustManagers = trustManagers;
		}
		return this;
	}

	/**
	 * 设置 JSSE key managers
	 *
	 * @param keyManagers JSSE key managers
	 * @return 自身
	 */
	public SSLContextBuilder setKeyManagers(KeyManager... keyManagers) {
		if (ArrayUtil.isNotEmpty(keyManagers)) {
			this.keyManagers = keyManagers;
		}
		return this;
	}

	/**
	 * 设置 SecureRandom
	 *
	 * @param secureRandom SecureRandom
	 * @return 自己
	 */
	public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
		if (null != secureRandom) {
			this.secureRandom = secureRandom;
		}
		return this;
	}

	/**
	 * 构建{@link SSLContext}
	 *
	 * @return {@link SSLContext}
	 * @throws NoSuchAlgorithmException 无此算法
	 * @throws KeyManagementException   Key管理异常
	 */
	public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance(protocol);
		sslContext.init(this.keyManagers, this.trustManagers, this.secureRandom);
		return sslContext;
	}

	/**
	 * 构建{@link SSLContext}
	 *
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public SSLContext buildQuietly() throws IORuntimeException {
		try {
			return build();
		} catch (GeneralSecurityException e) {
			throw new IORuntimeException(e);
		}
	}
}
