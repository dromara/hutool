package cn.hutool.core.net;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * 默认信任管理器，默认信任所有客户端和服务端证书
 *
 * @author Looly
 * @since 5.5.7
 */
public class DefaultTrustManager implements X509TrustManager {

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
}
