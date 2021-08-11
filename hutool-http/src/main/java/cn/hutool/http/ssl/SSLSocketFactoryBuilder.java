package cn.hutool.http.ssl;

import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.core.net.SSLProtocols;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * SSLSocketFactory构建器
 *
 * @author Looly
 * @see SSLContextBuilder
 * @deprecated 请使用 {@link SSLContextBuilder}
 */
@Deprecated
public class SSLSocketFactoryBuilder implements SSLProtocols {

	SSLContextBuilder sslContextBuilder;

	/**
	 * 构造
	 */
	public SSLSocketFactoryBuilder() {
		this.sslContextBuilder = SSLContextBuilder.create();
	}

	/**
	 * 创建 SSLSocketFactoryBuilder
	 *
	 * @return SSLSocketFactoryBuilder
	 */
	public static SSLSocketFactoryBuilder create() {
		return new SSLSocketFactoryBuilder();
	}

	/**
	 * 设置协议
	 *
	 * @param protocol 协议
	 * @return 自身
	 */
	public SSLSocketFactoryBuilder setProtocol(String protocol) {
		this.sslContextBuilder.setProtocol(protocol);
		return this;
	}

	/**
	 * 设置信任信息
	 *
	 * @param trustManagers TrustManager列表
	 * @return 自身
	 */
	public SSLSocketFactoryBuilder setTrustManagers(TrustManager... trustManagers) {
		this.sslContextBuilder.setTrustManagers(trustManagers);
		return this;
	}

	/**
	 * 设置 JSSE key managers
	 *
	 * @param keyManagers JSSE key managers
	 * @return 自身
	 */
	public SSLSocketFactoryBuilder setKeyManagers(KeyManager... keyManagers) {
		this.sslContextBuilder.setKeyManagers(keyManagers);
		return this;
	}

	/**
	 * 设置 SecureRandom
	 *
	 * @param secureRandom SecureRandom
	 * @return 自己
	 */
	public SSLSocketFactoryBuilder setSecureRandom(SecureRandom secureRandom) {
		this.sslContextBuilder.setSecureRandom(secureRandom);
		return this;
	}

	/**
	 * 构建SSLSocketFactory
	 *
	 * @return SSLSocketFactory
	 * @throws NoSuchAlgorithmException 无此算法
	 * @throws KeyManagementException   Key管理异常
	 */
	public SSLSocketFactory build() throws NoSuchAlgorithmException, KeyManagementException {
		return this.sslContextBuilder.build().getSocketFactory();
	}
}
