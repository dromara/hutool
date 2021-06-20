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
 * {@link SSLContext}构建器
 *
 * @author Looly
 * @since 5.5.2
 */
public class SSLContextBuilder {

	/**
	 * Supports some version of SSL; may support other versions
	 */
	public static final String SSL = "SSL";
	/**
	 * Supports SSL version 2 or later; may support other versions
	 */
	public static final String SSLv2 = "SSLv2";
	/**
	 * Supports SSL version 3; may support other versions
	 */
	public static final String SSLv3 = "SSLv3";

	/**
	 * Supports some version of TLS; may support other versions
	 */
	public static final String TLS = "TLS";
	/**
	 * Supports RFC 2246: TLS version 1.0 ; may support other versions
	 */
	public static final String TLSv1 = "TLSv1";
	/**
	 * Supports RFC 4346: TLS version 1.1 ; may support other versions
	 */
	public static final String TLSv11 = "TLSv1.1";
	/**
	 * Supports RFC 5246: TLS version 1.2 ; may support other versions
	 */
	public static final String TLSv12 = "TLSv1.2";

	private String protocol = TLS;
	private KeyManager[] keyManagers;
	private TrustManager[] trustManagers = {new DefaultTrustManager()};
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
	public SSLContext buildQuietly() throws IORuntimeException{
		try {
			return build();
		} catch (GeneralSecurityException e) {
			throw new IORuntimeException(e);
		}
	}
}
