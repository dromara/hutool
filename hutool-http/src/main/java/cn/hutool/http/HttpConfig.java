package cn.hutool.http;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.SSLUtil;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Http配置项
 *
 * @author looly
 * @since 5.8.0
 */
public class HttpConfig {

	/**
	 * 创建默认Http配置信息
	 *
	 * @return HttpConfig
	 */
	public static HttpConfig create() {
		return new HttpConfig();
	}

	/**
	 * 默认连接超时
	 */
	int connectionTimeout = HttpGlobalConfig.getTimeout();
	/**
	 * 默认读取超时
	 */
	int readTimeout = HttpGlobalConfig.getTimeout();

	/**
	 * 是否禁用缓存
	 */
	boolean isDisableCache;

	/**
	 * 最大重定向次数
	 */
	int maxRedirectCount = HttpGlobalConfig.getMaxRedirectCount();

	/**
	 * 代理
	 */
	Proxy proxy;

	/**
	 * HostnameVerifier，用于HTTPS安全连接
	 */
	HostnameVerifier hostnameVerifier;
	/**
	 * SSLSocketFactory，用于HTTPS安全连接
	 */
	SSLSocketFactory ssf;

	/**
	 * Chuncked块大小，0或小于0表示不设置Chuncked模式
	 */
	int blockSize;

	/**
	 * 获取是否忽略响应读取时可能的EOF异常。<br>
	 * 在Http协议中，对于Transfer-Encoding: Chunked在正常情况下末尾会写入一个Length为0的的chunk标识完整结束。<br>
	 * 如果服务端未遵循这个规范或响应没有正常结束，会报EOF异常，此选项用于是否忽略这个异常。
	 */
	boolean ignoreEOFError = HttpGlobalConfig.isIgnoreEOFError();
	/**
	 * 获取是否忽略解码URL，包括URL中的Path部分和Param部分。<br>
	 * 在构建Http请求时，用户传入的URL可能有编码后和未编码的内容混合在一起，如果此参数为{@code true}，则会统一解码编码后的参数，<br>
	 * 按照RFC3986规范，在发送请求时，全部编码之。如果为{@code false}，则不会解码已经编码的内容，在请求时只编码需要编码的部分。
	 */
	boolean decodeUrl = HttpGlobalConfig.isDecodeUrl();

	/**
	 * 请求前的拦截器，用于在请求前重新编辑请求
	 */
	final HttpInterceptor.Chain<HttpRequest> requestInterceptors = GlobalInterceptor.INSTANCE.getCopiedRequestInterceptor();
	/**
	 * 响应后的拦截器，用于在响应后处理逻辑
	 */
	final HttpInterceptor.Chain<HttpResponse> responseInterceptors = GlobalInterceptor.INSTANCE.getCopiedResponseInterceptor();

	/**
	 * 重定向时是否使用拦截器
	 */
	boolean interceptorOnRedirect;

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
	public HttpConfig timeout(int milliseconds) {
		setConnectionTimeout(milliseconds);
		setReadTimeout(milliseconds);
		return this;
	}

	/**
	 * 设置连接超时，单位：毫秒
	 *
	 * @param milliseconds 超时毫秒数
	 * @return this
	 */
	public HttpConfig setConnectionTimeout(int milliseconds) {
		this.connectionTimeout = milliseconds;
		return this;
	}

	/**
	 * 设置连接超时，单位：毫秒
	 *
	 * @param milliseconds 超时毫秒数
	 * @return this
	 */
	public HttpConfig setReadTimeout(int milliseconds) {
		this.readTimeout = milliseconds;
		return this;
	}

	/**
	 * 禁用缓存
	 *
	 * @return this
	 */
	public HttpConfig disableCache() {
		this.isDisableCache = true;
		return this;
	}

	/**
	 * 设置最大重定向次数<br>
	 * 如果次数小于1则表示不重定向，大于等于1表示打开重定向
	 *
	 * @param maxRedirectCount 最大重定向次数
	 * @return this
	 */
	public HttpConfig setMaxRedirectCount(int maxRedirectCount) {
		this.maxRedirectCount = Math.max(maxRedirectCount, 0);
		return this;
	}

	/**
	 * 设置域名验证器<br>
	 * 只针对HTTPS请求，如果不设置，不做验证，所有域名被信任
	 *
	 * @param hostnameVerifier HostnameVerifier
	 * @return this
	 */
	public HttpConfig setHostnameVerifier(HostnameVerifier hostnameVerifier) {
		// 验证域
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	/**
	 * 设置Http代理
	 *
	 * @param host 代理 主机
	 * @param port 代理 端口
	 * @return this
	 */
	public HttpConfig setHttpProxy(String host, int port) {
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
	public HttpConfig setProxy(Proxy proxy) {
		this.proxy = proxy;
		return this;
	}

	/**
	 * 设置SSLSocketFactory<br>
	 * 只针对HTTPS请求，如果不设置，使用默认的SSLSocketFactory<br>
	 * 默认SSLSocketFactory为：SSLSocketFactoryBuilder.create().build();
	 *
	 * @param ssf SSLScketFactory
	 * @return this
	 */
	public HttpConfig setSSLSocketFactory(SSLSocketFactory ssf) {
		this.ssf = ssf;
		return this;
	}

	/**
	 * 设置HTTPS安全连接协议，只针对HTTPS请求，可以使用的协议包括：<br>
	 * 此方法调用后{@link #setSSLSocketFactory(SSLSocketFactory)} 将被覆盖。
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
	 * @see SSLUtil#createSSLContext(String)
	 * @see #setSSLSocketFactory(SSLSocketFactory)
	 */
	public HttpConfig setSSLProtocol(String protocol) {
		Assert.notBlank(protocol, "protocol must be not blank!");
		setSSLSocketFactory(SSLUtil.createSSLContext(protocol).getSocketFactory());
		return this;
	}

	/**
	 * 采用流方式上传数据，无需本地缓存数据。<br>
	 * HttpUrlConnection默认是将所有数据读到本地缓存，然后再发送给服务器，这样上传大文件时就会导致内存溢出。
	 *
	 * @param blockSize 块大小（bytes数），0或小于0表示不设置Chuncked模式
	 * @return this
	 */
	public HttpConfig setBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	/**
	 * 设置是否忽略响应读取时可能的EOF异常。<br>
	 * 在Http协议中，对于Transfer-Encoding: Chunked在正常情况下末尾会写入一个Length为0的的chunk标识完整结束。<br>
	 * 如果服务端未遵循这个规范或响应没有正常结束，会报EOF异常，此选项用于是否忽略这个异常。
	 *
	 * @param ignoreEOFError 是否忽略响应读取时可能的EOF异常。
	 * @return this
	 * @since 5.7.20
	 */
	public HttpConfig setIgnoreEOFError(boolean ignoreEOFError) {
		this.ignoreEOFError = ignoreEOFError;
		return this;
	}

	/**
	 * 设置是否忽略解码URL，包括URL中的Path部分和Param部分。<br>
	 * 在构建Http请求时，用户传入的URL可能有编码后和未编码的内容混合在一起，如果此参数为{@code true}，则会统一解码编码后的参数，<br>
	 * 按照RFC3986规范，在发送请求时，全部编码之。如果为{@code false}，则不会解码已经编码的内容，在请求时只编码需要编码的部分。
	 *
	 * @param decodeUrl 是否忽略解码URL
	 * @return this
	 */
	public HttpConfig setDecodeUrl(boolean decodeUrl) {
		this.decodeUrl = decodeUrl;
		return this;
	}

	/**
	 * 设置拦截器，用于在请求前重新编辑请求
	 *
	 * @param interceptor 拦截器实现
	 * @return this
	 */
	public HttpConfig addRequestInterceptor(HttpInterceptor<HttpRequest> interceptor) {
		this.requestInterceptors.addChain(interceptor);
		return this;
	}

	/**
	 * 设置拦截器，用于在请求前重新编辑请求
	 *
	 * @param interceptor 拦截器实现
	 * @return this
	 */
	public HttpConfig addResponseInterceptor(HttpInterceptor<HttpResponse> interceptor) {
		this.responseInterceptors.addChain(interceptor);
		return this;
	}

	/**
	 * 重定向时是否使用拦截器
	 *
	 * @param interceptorOnRedirect 重定向时是否使用拦截器
	 * @return this
	 */
	public HttpConfig setInterceptorOnRedirect(boolean interceptorOnRedirect) {
		this.interceptorOnRedirect = interceptorOnRedirect;
		return this;
	}
}
