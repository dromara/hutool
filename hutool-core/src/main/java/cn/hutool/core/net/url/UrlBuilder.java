package cn.hutool.core.net.url;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;

/**
 * URL 生成器，格式形如：
 * <pre>
 * [scheme:]scheme-specific-part[#fragment]
 * [scheme:][//authority][path][?query][#fragment]
 * [scheme:][//host:port][path][?query][#fragment]
 * </pre>
 *
 * @author looly
 * @see <a href="https://en.wikipedia.org/wiki/Uniform_Resource_Identifier">Uniform Resource Identifier</a>
 * @since 5.3.1
 */
public final class UrlBuilder implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_SCHEME = "http";

	/**
	 * 协议，例如http
	 */
	private String scheme;
	/**
	 * 主机，例如127.0.0.1
	 */
	private String host;
	/**
	 * 端口，默认-1
	 */
	private int port = -1;
	/**
	 * 路径，例如/aa/bb/cc
	 */
	private UrlPath path;
	/**
	 * 查询语句，例如a=1&amp;b=2
	 */
	private UrlQuery query;
	/**
	 * 标识符，例如#后边的部分
	 */
	private String fragment;

	/**
	 * 编码，用于URLEncode和URLDecode
	 */
	private Charset charset;

	/**
	 * 使用URI构建UrlBuilder
	 *
	 * @param uri     URI
	 * @param charset 编码，用于URLEncode和URLDecode
	 * @return UrlBuilder
	 */
	public static UrlBuilder of(URI uri, Charset charset) {
		return of(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getRawQuery(), uri.getFragment(), charset);
	}

	/**
	 * 使用URL字符串构建UrlBuilder，当传入的URL没有协议时，按照http协议对待。
	 *
	 * @param httpUrl URL字符串
	 * @param charset 编码，用于URLEncode和URLDecode
	 * @return UrlBuilder
	 */
	public static UrlBuilder ofHttp(String httpUrl, Charset charset) {
		Assert.notBlank(httpUrl, "Http url must be not blank!");

		final int sepIndex = httpUrl.indexOf("://");
		if (sepIndex < 0) {
			httpUrl = "http://" + httpUrl.trim();
		}
		return of(httpUrl, charset);
	}

	/**
	 * 使用URL字符串构建UrlBuilder
	 *
	 * @param url     URL字符串
	 * @param charset 编码，用于URLEncode和URLDecode
	 * @return UrlBuilder
	 */
	public static UrlBuilder of(String url, Charset charset) {
		Assert.notBlank(url, "Url must be not blank!");
		return of(URLUtil.url(url.trim()), charset);
	}

	/**
	 * 使用URL构建UrlBuilder
	 *
	 * @param url     URL
	 * @param charset 编码，用于URLEncode和URLDecode
	 * @return UrlBuilder
	 */
	public static UrlBuilder of(URL url, Charset charset) {
		return of(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef(), charset);
	}

	/**
	 * 构建UrlBuilder
	 *
	 * @param scheme   协议，默认http
	 * @param host     主机，例如127.0.0.1
	 * @param port     端口，-1表示默认端口
	 * @param path     路径，例如/aa/bb/cc
	 * @param query    查询，例如a=1&amp;b=2
	 * @param fragment 标识符例如#后边的部分
	 * @param charset  编码，用于URLEncode和URLDecode
	 * @return UrlBuilder
	 */
	public static UrlBuilder of(String scheme, String host, int port, String path, String query, String fragment, Charset charset) {
		return of(scheme, host, port, UrlPath.of(path, charset), UrlQuery.of(query, charset), fragment, charset);
	}

	/**
	 * 构建UrlBuilder
	 *
	 * @param scheme   协议，默认http
	 * @param host     主机，例如127.0.0.1
	 * @param port     端口，-1表示默认端口
	 * @param path     路径，例如/aa/bb/cc
	 * @param query    查询，例如a=1&amp;b=2
	 * @param fragment 标识符例如#后边的部分
	 * @param charset  编码，用于URLEncode和URLDecode
	 * @return UrlBuilder
	 */
	public static UrlBuilder of(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment, Charset charset) {
		return new UrlBuilder(scheme, host, port, path, query, fragment, charset);
	}

	/**
	 * 创建空的UrlBuilder
	 *
	 * @return UrlBuilder
	 */
	public static UrlBuilder create() {
		return new UrlBuilder();
	}

	/**
	 * 构造
	 */
	public UrlBuilder() {
		this.charset = CharsetUtil.CHARSET_UTF_8;
	}

	/**
	 * 构造
	 *
	 * @param scheme   协议，默认http
	 * @param host     主机，例如127.0.0.1
	 * @param port     端口，-1表示默认端口
	 * @param path     路径，例如/aa/bb/cc
	 * @param query    查询，例如a=1&amp;b=2
	 * @param fragment 标识符例如#后边的部分
	 * @param charset  编码，用于URLEncode和URLDecode
	 */
	public UrlBuilder(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment, Charset charset) {
		this.charset = charset;
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
		this.query = query;
		this.setFragment(fragment);
	}

	/**
	 * 获取协议，例如http
	 *
	 * @return 协议，例如http
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * 获取协议，例如http，如果用户未定义协议，使用默认的http协议
	 *
	 * @return 协议，例如http
	 */
	public String getSchemeWithDefault() {
		return StrUtil.emptyToDefault(this.scheme, DEFAULT_SCHEME);
	}

	/**
	 * 设置协议，例如http
	 *
	 * @param scheme 协议，例如http
	 * @return this
	 */
	public UrlBuilder setScheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * 获取 主机，例如127.0.0.1
	 *
	 * @return 主机，例如127.0.0.1
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 设置主机，例如127.0.0.1
	 *
	 * @param host 主机，例如127.0.0.1
	 * @return this
	 */
	public UrlBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * 获取端口，默认-1
	 *
	 * @return 端口，默认-1
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设置端口，默认-1
	 *
	 * @param port 端口，默认-1
	 * @return this
	 */
	public UrlBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * 获得authority部分
	 *
	 * @return authority部分
	 */
	public String getAuthority() {
		return (port < 0) ? host : host + ":" + port;
	}

	/**
	 * 获取路径，例如/aa/bb/cc
	 *
	 * @return 路径，例如/aa/bb/cc
	 */
	public UrlPath getPath() {
		return path;
	}

	/**
	 * 获得路径，例如/aa/bb/cc
	 *
	 * @return 路径，例如/aa/bb/cc
	 */
	public String getPathStr() {
		return null == this.path ? StrUtil.SLASH : this.path.build(charset);
	}

	/**
	 * 设置路径，例如/aa/bb/cc，将覆盖之前所有的path相关设置
	 *
	 * @param path 路径，例如/aa/bb/cc
	 * @return this
	 */
	public UrlBuilder setPath(UrlPath path) {
		this.path = path;
		return this;
	}

	/**
	 * 增加路径节点
	 *
	 * @param segment 路径节点
	 * @return this
	 */
	public UrlBuilder addPath(String segment) {
		if (StrUtil.isBlank(segment)) {
			return this;
		}
		if (null == this.path) {
			this.path = new UrlPath();
		}
		this.path.add(segment);
		return this;
	}

	/**
	 * 追加path节点
	 *
	 * @param segment path节点
	 * @return this
	 */
	public UrlBuilder appendPath(CharSequence segment) {
		if (StrUtil.isEmpty(segment)) {
			return this;
		}

		if (this.path == null) {
			this.path = new UrlPath();
		}
		this.path.add(segment);
		return this;
	}

	/**
	 * 获取查询语句，例如a=1&amp;b=2
	 *
	 * @return 查询语句，例如a=1&amp;b=2
	 */
	public UrlQuery getQuery() {
		return query;
	}

	/**
	 * 获取查询语句，例如a=1&amp;b=2
	 *
	 * @return 查询语句，例如a=1&amp;b=2
	 */
	public String getQueryStr() {
		return null == this.query ? null : this.query.build(this.charset);
	}

	/**
	 * 设置查询语句，例如a=1&amp;b=2，将覆盖之前所有的query相关设置
	 *
	 * @param query 查询语句，例如a=1&amp;b=2
	 * @return this
	 */
	public UrlBuilder setQuery(UrlQuery query) {
		this.query = query;
		return this;
	}

	/**
	 * 添加查询项，支持重复键
	 *
	 * @param key   键
	 * @param value 值
	 * @return this
	 */
	public UrlBuilder addQuery(String key, String value) {
		if (StrUtil.isEmpty(key)) {
			return this;
		}

		if (this.query == null) {
			this.query = new UrlQuery();
		}
		this.query.add(key, value);
		return this;
	}

	/**
	 * 获取标识符，#后边的部分
	 *
	 * @return 标识符，例如#后边的部分
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * 获取标识符，#后边的部分
	 *
	 * @return 标识符，例如#后边的部分
	 */
	public String getFragmentEncoded() {
		return URLUtil.encodeAll(this.fragment, this.charset);
	}

	/**
	 * 设置标识符，例如#后边的部分
	 *
	 * @param fragment 标识符，例如#后边的部分
	 * @return this
	 */
	public UrlBuilder setFragment(String fragment) {
		if (StrUtil.isEmpty(fragment)) {
			this.fragment = null;
		}
		this.fragment = StrUtil.removePrefix(fragment, "#");
		return this;
	}

	/**
	 * 获取编码，用于URLEncode和URLDecode
	 *
	 * @return 编码
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置编码，用于URLEncode和URLDecode
	 *
	 * @param charset 编码
	 * @return this
	 */
	public UrlBuilder setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 创建URL字符串
	 *
	 * @return URL字符串
	 */
	public String build() {
		return toURL().toString();
	}

	/**
	 * 转换为{@link URL} 对象
	 *
	 * @return {@link URL}
	 */
	public URL toURL() {
		return toURL(null);
	}

	/**
	 * 转换为{@link URL} 对象
	 *
	 * @param handler {@link URLStreamHandler}，null表示默认
	 * @return {@link URL}
	 */
	public URL toURL(URLStreamHandler handler) {
		final StringBuilder fileBuilder = new StringBuilder();

		// path
		fileBuilder.append(StrUtil.blankToDefault(getPathStr(), StrUtil.SLASH));

		// query
		final String query = getQueryStr();
		if (StrUtil.isNotBlank(query)) {
			fileBuilder.append('?').append(query);
		}

		// fragment
		if (StrUtil.isNotBlank(this.fragment)) {
			fileBuilder.append('#').append(getFragmentEncoded());
		}

		try {
			return new URL(getSchemeWithDefault(), host, port, fileBuilder.toString(), handler);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * 转换为URI
	 *
	 * @return URI
	 */
	public URI toURI() {
		try {
			return new URI(
					getSchemeWithDefault(),
					getAuthority(),
					getPathStr(),
					getQueryStr(),
					getFragmentEncoded());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return build();
	}

}