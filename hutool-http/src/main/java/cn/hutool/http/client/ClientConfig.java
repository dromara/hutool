package cn.hutool.http.client;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.ssl.SSLUtil;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.ssl.TrustAnySSLInfo;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Http客户端配置
 *
 * @author looly
 */
public class ClientConfig {

	/**
	 * 创建新的 ClientConfig
	 *
	 * @return ClientConfig
	 */
	public static ClientConfig of() {
		return new ClientConfig();
	}

	/**
	 * 默认连接超时
	 */
	private int connectionTimeout;
	/**
	 * 默认读取超时
	 */
	private int readTimeout;

	/**
	 * HostnameVerifier，用于HTTPS安全连接
	 */
	private HostnameVerifier hostnameVerifier;
	/**
	 * SSLSocketFactory，用于HTTPS安全连接
	 */
	private SSLSocketFactory socketFactory;
	/**
	 * 是否禁用缓存
	 */
	private boolean disableCache;
	/**
	 * 代理
	 */
	private Proxy proxy;

	/**
	 * 构造
	 */
	public ClientConfig() {
		connectionTimeout = HttpGlobalConfig.getTimeout();
		readTimeout = HttpGlobalConfig.getTimeout();
		hostnameVerifier = TrustAnySSLInfo.TRUST_ANY_HOSTNAME_VERIFIER;
		socketFactory = TrustAnySSLInfo.DEFAULT_SSF;
	}

	/**
	 * 设置超时，单位：毫秒<br>
	 * 超时包括：
	 *
	 * <pre>
	 * 1. 连接超时
	 * 2. 读取响应超时
	 * </pre>
	 *
	 * @param milliseconds 超时毫秒数
	 * @return this
	 * @see #setConnectionTimeout(int)
	 * @see #setReadTimeout(int)
	 */
	public ClientConfig setTimeout(final int milliseconds) {
		setConnectionTimeout(milliseconds);
		setReadTimeout(milliseconds);
		return this;
	}

	/**
	 * 获取连接超时，单位：毫秒
	 *
	 * @return 连接超时，单位：毫秒
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * 设置连接超时，单位：毫秒
	 *
	 * @param connectionTimeout 超时毫秒数
	 * @return this
	 */
	public ClientConfig setConnectionTimeout(final int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	/**
	 * 获取读取超时，单位：毫秒
	 *
	 * @return 读取超时，单位：毫秒
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * 设置读取超时，单位：毫秒
	 *
	 * @param readTimeout 读取超时，单位：毫秒
	 * @return this
	 */
	public ClientConfig setReadTimeout(final int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	/**
	 * 获取域名验证器
	 *
	 * @return 域名验证器
	 */
	public HostnameVerifier getHostnameVerifier() {
		return hostnameVerifier;
	}

	/**
	 * 设置域名验证器<br>
	 * 只针对HTTPS请求，如果不设置，不做验证，所有域名被信任
	 *
	 * @param hostnameVerifier HostnameVerifier
	 * @return this
	 */
	public ClientConfig setHostnameVerifier(final HostnameVerifier hostnameVerifier) {
		// 验证域
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	/**
	 * 获取SSLSocketFactory
	 *
	 * @return SSLSocketFactory
	 */
	public SSLSocketFactory getSocketFactory() {
		return socketFactory;
	}

	/**
	 * 设置SSLSocketFactory<br>
	 * 只针对HTTPS请求，如果不设置，使用默认的SSLSocketFactory<br>
	 * 默认SSLSocketFactory为：SSLSocketFactoryBuilder.create().build();
	 *
	 * @param ssf SSLScketFactory
	 * @return this
	 */
	public ClientConfig setSocketFactory(final SSLSocketFactory ssf) {
		this.socketFactory = ssf;
		return this;
	}

	/**
	 * 设置HTTPS安全连接协议，只针对HTTPS请求，可以使用的协议包括：<br>
	 * 此方法调用后{@link #setSocketFactory(SSLSocketFactory)} 将被覆盖。
	 *
	 * <pre>
	 * 1. TLSv1.2
	 * 2. TLSv1.1
	 * 3. SSLv3
	 * ...
	 * </pre>
	 *
	 * @param protocol 协议
	 * @return this
	 * @see SSLUtil#createTrustAnySSLContext(String)
	 * @see #setSocketFactory(SSLSocketFactory)
	 */
	public ClientConfig setSSLProtocol(final String protocol) {
		Assert.notBlank(protocol, "protocol must be not blank!");
		setSocketFactory(SSLUtil.createTrustAnySSLContext(protocol).getSocketFactory());
		return this;
	}

	/**
	 * 是否禁用缓存
	 *
	 * @return 是否禁用缓存
	 */
	public boolean isDisableCache() {
		return disableCache;
	}

	/**
	 * 设置是否禁用缓存
	 *
	 * @param disableCache 是否禁用缓存
	 */
	public void setDisableCache(final boolean disableCache) {
		this.disableCache = disableCache;
	}

	/**
	 * 获取代理
	 *
	 * @return 代理
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * 设置Http代理
	 *
	 * @param host 代理 主机
	 * @param port 代理 端口
	 * @return this
	 */
	public ClientConfig setHttpProxy(final String host, final int port) {
		final Proxy proxy = new Proxy(Proxy.Type.HTTP,
				new InetSocketAddress(host, port));
		return setProxy(proxy);
	}

	/**
	 * 设置代理
	 *
	 * @param proxy 代理 {@link Proxy}
	 * @return this
	 */
	public ClientConfig setProxy(final Proxy proxy) {
		this.proxy = proxy;
		return this;
	}
}
