package cn.hutool.http.ssl;

import cn.hutool.core.text.StrUtil;

import javax.net.ssl.SSLSocketFactory;

/**
 * 新任所有SSL配置
 *
 * @author looly
 */
public class TrustAnySSLInfo {
	/**
	 * 默认信任全部的域名校验器
	 */
	public static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER;
	/**
	 * 默认的SSLSocketFactory，区分安卓
	 */
	public static final SSLSocketFactory DEFAULT_SSF;

	static {
		TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
		if (StrUtil.equalsIgnoreCase("dalvik", System.getProperty("java.vm.name"))) {
			// 兼容android低版本SSL连接
			DEFAULT_SSF = new AndroidSupportSSLFactory();
		} else {
			DEFAULT_SSF = new DefaultSSLFactory();
		}
	}
}
