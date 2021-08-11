package cn.hutool.core.net;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * 默认信任管理器，默认信任所有客户端和服务端证书<br>
 * 继承{@link X509ExtendedTrustManager}的原因见：https://blog.csdn.net/ghaohao/article/details/79454913
 *
 * @author Looly
 * @since 5.5.7
 */
public class DefaultTrustManager extends X509ExtendedTrustManager {

	/**
	 * 默认的全局单例默认信任管理器，，默认信任所有客户端和服务端证书
	 * @since 5.7.8
	 */
	public static DefaultTrustManager INSTANCE = new DefaultTrustManager();

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) {
	}

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
	}

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
	}
}
