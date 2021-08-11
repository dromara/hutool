package cn.hutool.core.net;

import cn.hutool.core.io.IORuntimeException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * SSL(Secure Sockets Layer 安全套接字协议)相关工具封装
 *
 * @author looly
 * @since 5.5.2
 */
public class SSLUtil {

	/**
	 * 创建{@link SSLContext}，默认新人全部
	 *
	 * @param protocol     SSL协议，例如TLS等
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 * @since 5.7.8
	 */
	public static SSLContext createSSLContext(String protocol) throws IORuntimeException{
		return SSLContextBuilder.create().setProtocol(protocol).buildQuietly();
	}

	/**
	 * 创建{@link SSLContext}
	 *
	 * @param protocol     SSL协议，例如TLS等
	 * @param keyManager   密钥管理器,{@code null}表示无
	 * @param trustManager 信任管理器, {@code null}表示无
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager)
			throws IORuntimeException {
		return createSSLContext(protocol,
				keyManager == null ? null : new KeyManager[]{keyManager},
				trustManager == null ? null : new TrustManager[]{trustManager});
	}

	/**
	 * 创建和初始化{@link SSLContext}
	 *
	 * @param protocol      SSL协议，例如TLS等
	 * @param keyManagers   密钥管理器,{@code null}表示无
	 * @param trustManagers 信任管理器, {@code null}表示无
	 * @return {@link SSLContext}
	 * @throws IORuntimeException 包装 GeneralSecurityException异常
	 */
	public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) throws IORuntimeException {
		return SSLContextBuilder.create()
				.setProtocol(protocol)
				.setKeyManagers(keyManagers)
				.setTrustManagers(trustManagers).buildQuietly();
	}
}
