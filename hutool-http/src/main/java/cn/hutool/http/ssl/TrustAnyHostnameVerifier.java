package cn.hutool.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * https 域名校验，信任所有域名
 *
 * @author Looly
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(final String hostname, final SSLSession session) {
		return true;// 直接返回true
	}
}
