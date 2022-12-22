package cn.hutool.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.MultiFileResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.TableMap;
import cn.hutool.core.net.SSLUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.body.BytesBody;
import cn.hutool.http.body.FormUrlEncodedBody;
import cn.hutool.http.body.MultipartBody;
import cn.hutool.http.body.RequestBody;
import cn.hutool.http.cookie.GlobalCookieManager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * http请求类<br>
 * Http请求类用于构建Http请求并同步获取结果，此类通过CookieManager持有域名对应的Cookie值，再次请求时会自动附带Cookie信息
 *
 * @author Looly
 */
public class HttpRequest extends HttpBase<HttpRequest> {

	// ---------------------------------------------------------------- static Http Method start

	/**
	 * POST请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest post(String url) {
		return of(url).method(Method.POST);
	}

	/**
	 * GET请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest get(String url) {
		return of(url).method(Method.GET);
	}

	/**
	 * HEAD请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest head(String url) {
		return of(url).method(Method.HEAD);
	}

	/**
	 * OPTIONS请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest options(String url) {
		return of(url).method(Method.OPTIONS);
	}

	/**
	 * PUT请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest put(String url) {
		return of(url).method(Method.PUT);
	}

	/**
	 * PATCH请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 * @since 3.0.9
	 */
	public static HttpRequest patch(String url) {
		return of(url).method(Method.PATCH);
	}

	/**
	 * DELETE请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest delete(String url) {
		return of(url).method(Method.DELETE);
	}

	/**
	 * TRACE请求
	 *
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest trace(String url) {
		return of(url).method(Method.TRACE);
	}

	/**
	 * 构建一个HTTP请求<br>
	 * 对于传入的URL，可以自定义是否解码已经编码的内容，设置见{@link HttpGlobalConfig#setDecodeUrl(boolean)}<br>
	 * 在构建Http请求时，用户传入的URL可能有编码后和未编码的内容混合在一起，如果{@link HttpGlobalConfig#isDecodeUrl()}为{@code true}，则会统一解码编码后的参数，<br>
	 * 按照RFC3986规范，在发送请求时，全部编码之。如果为{@code false}，则不会解码已经编码的内容，在请求时只编码需要编码的部分。
	 *
	 * @param url URL链接，默认自动编码URL中的参数等信息
	 * @return HttpRequest
	 * @since 5.7.18
	 */
	public static HttpRequest of(String url) {
		return of(url, HttpGlobalConfig.isDecodeUrl() ? DEFAULT_CHARSET : null);
	}

	/**
	 * 构建一个HTTP请求<br>
	 * 对于传入的URL，可以自定义是否解码已经编码的内容。<br>
	 * 在构建Http请求时，用户传入的URL可能有编码后和未编码的内容混合在一起，如果charset参数不为{@code null}，则会统一解码编码后的参数，<br>
	 * 按照RFC3986规范，在发送请求时，全部编码之。如果为{@code false}，则不会解码已经编码的内容，在请求时只编码需要编码的部分。
	 *
	 * @param url     URL链接
	 * @param charset 编码，如果为{@code null}不自动解码编码URL
	 * @return HttpRequest
	 * @since 5.7.18
	 */
	public static HttpRequest of(String url, Charset charset) {
		return of(UrlBuilder.ofHttp(url, charset));
	}

	/**
	 * 构建一个HTTP请求<br>
	 *
	 * @param url {@link UrlBuilder}
	 * @return HttpRequest
	 * @since 5.8.0
	 */
	public static HttpRequest of(UrlBuilder url) {
		return new HttpRequest(url);
	}

	/**
	 * 设置全局默认的连接和读取超时时长
	 *
	 * @param customTimeout 超时时长
	 * @see HttpGlobalConfig#setTimeout(int)
	 * @since 4.6.2
	 */
	public static void setGlobalTimeout(int customTimeout) {
		HttpGlobalConfig.setTimeout(customTimeout);
	}

	/**
	 * 获取Cookie管理器，用于自定义Cookie管理
	 *
	 * @return {@link CookieManager}
	 * @see GlobalCookieManager#getCookieManager()
	 * @since 4.1.0
	 */
	public static CookieManager getCookieManager() {
		return GlobalCookieManager.getCookieManager();
	}

	/**
	 * 自定义{@link CookieManager}
	 *
	 * @param customCookieManager 自定义的{@link CookieManager}
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 * @since 4.5.14
	 */
	public static void setCookieManager(CookieManager customCookieManager) {
		GlobalCookieManager.setCookieManager(customCookieManager);
	}

	/**
	 * 关闭Cookie
	 *
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 * @since 4.1.9
	 */
	public static void closeCookie() {
		GlobalCookieManager.setCookieManager(null);
	}
	// ---------------------------------------------------------------- static Http Method end

	private HttpConfig config = HttpConfig.create();
	private UrlBuilder url;
	private URLStreamHandler urlHandler;
	private Method method = Method.GET;
	/**
	 * 连接对象
	 */
	private HttpConnection httpConnection;

	/**
	 * 存储表单数据
	 */
	private Map<String, Object> form;
	/**
	 * Cookie
	 */
	private String cookie;
	/**
	 * 是否为Multipart表单
	 */
	private boolean isMultiPart;
	/**
	 * 是否是REST请求模式
	 */
	private boolean isRest;
	/**
	 * 重定向次数计数器，内部使用
	 */
	private int redirectCount;

	/**
	 * 构造，URL编码默认使用UTF-8
	 *
	 * @param url URL
	 * @deprecated 请使用 {@link #of(String)}
	 */
	@Deprecated
	public HttpRequest(String url) {
		this(UrlBuilder.ofHttp(url));
	}

	/**
	 * 构造
	 *
	 * @param url {@link UrlBuilder}
	 */
	public HttpRequest(UrlBuilder url) {
		this.url = Assert.notNull(url, "URL must be not null!");
		// 给定默认URL编码
		final Charset charset = url.getCharset();
		if (null != charset) {
			this.charset(charset);
		}
		// 给定一个默认头信息
		this.header(GlobalHeaders.INSTANCE.headers);
	}

	/**
	 * 获取请求URL
	 *
	 * @return URL字符串
	 * @since 4.1.8
	 */
	public String getUrl() {
		return url.toString();
	}

	/**
	 * 设置URL
	 *
	 * @param url url字符串
	 * @return this
	 * @since 4.1.8
	 */
	public HttpRequest setUrl(String url) {
		return setUrl(UrlBuilder.ofHttp(url, this.charset));
	}

	/**
	 * 设置URL
	 *
	 * @param urlBuilder url字符串
	 * @return this
	 * @since 5.3.1
	 */
	public HttpRequest setUrl(UrlBuilder urlBuilder) {
		this.url = urlBuilder;
		return this;
	}

	/**
	 * 设置{@link URLStreamHandler}
	 * <p>
	 * 部分环境下需要单独设置此项，例如当 WebLogic Server 实例充当 SSL 客户端角色（它会尝试通过 SSL 连接到其他服务器或应用程序）时，<br>
	 * 它会验证 SSL 服务器在数字证书中返回的主机名是否与用于连接 SSL 服务器的 URL 主机名相匹配。如果主机名不匹配，则删除此连接。<br>
	 * 因此weblogic不支持https的sni协议的主机名验证，此时需要将此值设置为sun.net.www.protocol.https.Handler对象。
	 * <p>
	 * 相关issue见：<a href="https://gitee.com/dromara/hutool/issues/IMD1X">https://gitee.com/dromara/hutool/issues/IMD1X</a>
	 *
	 * @param urlHandler {@link URLStreamHandler}
	 * @return this
	 * @since 4.1.9
	 */
	public HttpRequest setUrlHandler(URLStreamHandler urlHandler) {
		this.urlHandler = urlHandler;
		return this;
	}

	/**
	 * 获取Http请求方法
	 *
	 * @return {@link Method}
	 * @since 4.1.8
	 */
	public Method getMethod() {
		return this.method;
	}

	/**
	 * 设置请求方法
	 *
	 * @param method HTTP方法
	 * @return HttpRequest
	 * @see #method(Method)
	 * @since 4.1.8
	 */
	public HttpRequest setMethod(Method method) {
		return method(method);
	}

	/**
	 * 获取{@link HttpConnection}<br>
	 * 在{@link #execute()} 执行前此对象为null
	 *
	 * @return {@link HttpConnection}
	 * @since 4.2.2
	 */
	public HttpConnection getConnection() {
		return this.httpConnection;
	}

	/**
	 * 设置请求方法
	 *
	 * @param method HTTP方法
	 * @return HttpRequest
	 */
	public HttpRequest method(Method method) {
		this.method = method;
		return this;
	}

	// ---------------------------------------------------------------- Http Request Header start

	/**
	 * 设置contentType
	 *
	 * @param contentType contentType
	 * @return HttpRequest
	 */
	public HttpRequest contentType(String contentType) {
		header(Header.CONTENT_TYPE, contentType);
		return this;
	}

	/**
	 * 设置是否为长连接
	 *
	 * @param isKeepAlive 是否长连接
	 * @return HttpRequest
	 */
	public HttpRequest keepAlive(boolean isKeepAlive) {
		header(Header.CONNECTION, isKeepAlive ? "Keep-Alive" : "Close");
		return this;
	}

	/**
	 * @return 获取是否为长连接
	 */
	public boolean isKeepAlive() {
		String connection = header(Header.CONNECTION);
		if (connection == null) {
			return false == HTTP_1_0.equalsIgnoreCase(httpVersion);
		}

		return false == "close".equalsIgnoreCase(connection);
	}

	/**
	 * 获取内容长度
	 *
	 * @return String
	 */
	public String contentLength() {
		return header(Header.CONTENT_LENGTH);
	}

	/**
	 * 设置内容长度
	 *
	 * @param value 长度
	 * @return HttpRequest
	 */
	public HttpRequest contentLength(int value) {
		header(Header.CONTENT_LENGTH, String.valueOf(value));
		return this;
	}

	/**
	 * 设置Cookie<br>
	 * 自定义Cookie后会覆盖Hutool的默认Cookie行为
	 *
	 * @param cookies Cookie值数组，如果为{@code null}则设置无效，使用默认Cookie行为
	 * @return this
	 * @since 5.4.1
	 */
	public HttpRequest cookie(Collection<HttpCookie> cookies) {
		return cookie(CollUtil.isEmpty(cookies) ? null : cookies.toArray(new HttpCookie[0]));
	}

	/**
	 * 设置Cookie<br>
	 * 自定义Cookie后会覆盖Hutool的默认Cookie行为
	 *
	 * @param cookies Cookie值数组，如果为{@code null}则设置无效，使用默认Cookie行为
	 * @return this
	 * @since 3.1.1
	 */
	public HttpRequest cookie(HttpCookie... cookies) {
		if (ArrayUtil.isEmpty(cookies)) {
			return disableCookie();
		}
		// 名称/值对之间用分号和空格 ('; ')
		// https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Cookie
		return cookie(ArrayUtil.join(cookies, "; "));
	}

	/**
	 * 设置Cookie<br>
	 * 自定义Cookie后会覆盖Hutool的默认Cookie行为
	 *
	 * @param cookie Cookie值，如果为{@code null}则设置无效，使用默认Cookie行为
	 * @return this
	 * @since 3.0.7
	 */
	public HttpRequest cookie(String cookie) {
		this.cookie = cookie;
		return this;
	}

	/**
	 * 禁用默认Cookie行为，此方法调用后会将Cookie置为空。<br>
	 * 如果想重新启用Cookie，请调用：{@link #cookie(String)}方法自定义Cookie。<br>
	 * 如果想启动默认的Cookie行为（自动回填服务器传回的Cookie），则调用{@link #enableDefaultCookie()}
	 *
	 * @return this
	 * @since 3.0.7
	 */
	public HttpRequest disableCookie() {
		return cookie(StrUtil.EMPTY);
	}

	/**
	 * 打开默认的Cookie行为（自动回填服务器传回的Cookie）
	 *
	 * @return this
	 */
	public HttpRequest enableDefaultCookie() {
		return cookie((String) null);
	}
	// ---------------------------------------------------------------- Http Request Header end

	// ---------------------------------------------------------------- Form start

	/**
	 * 设置表单数据<br>
	 *
	 * @param name  名
	 * @param value 值
	 * @return this
	 */
	public HttpRequest form(String name, Object value) {
		if (StrUtil.isBlank(name) || ObjectUtil.isNull(value)) {
			return this; // 忽略非法的form表单项内容;
		}

		// 停用body
		this.bodyBytes = null;

		if (value instanceof File) {
			// 文件上传
			return this.form(name, (File) value);
		}

		if (value instanceof Resource) {
			return form(name, (Resource) value);
		}

		// 普通值
		String strValue;
		if (value instanceof Iterable) {
			// 列表对象
			strValue = CollUtil.join((Iterable<?>) value, ",");
		} else if (ArrayUtil.isArray(value)) {
			if (File.class == ArrayUtil.getComponentType(value)) {
				// 多文件
				return this.form(name, (File[]) value);
			}
			// 数组对象
			strValue = ArrayUtil.join((Object[]) value, ",");
		} else {
			// 其他对象一律转换为字符串
			strValue = Convert.toStr(value, null);
		}

		return putToForm(name, strValue);
	}

	/**
	 * 设置表单数据
	 *
	 * @param name       名
	 * @param value      值
	 * @param parameters 参数对，奇数为名，偶数为值
	 * @return this
	 */
	public HttpRequest form(String name, Object value, Object... parameters) {
		form(name, value);

		for (int i = 0; i < parameters.length; i += 2) {
			form(parameters[i].toString(), parameters[i + 1]);
		}
		return this;
	}

	/**
	 * 设置map类型表单数据
	 *
	 * @param formMap 表单内容
	 * @return this
	 */
	public HttpRequest form(Map<String, Object> formMap) {
		if (MapUtil.isNotEmpty(formMap)) {
			formMap.forEach(this::form);
		}
		return this;
	}

	/**
	 * 设置map&lt;String, String&gt;类型表单数据
	 *
	 * @param formMapStr 表单内容
	 * @return this
	 * @since 5.6.7
	 */
	public HttpRequest formStr(Map<String, String> formMapStr) {
		if (MapUtil.isNotEmpty(formMapStr)) {
			formMapStr.forEach(this::form);
		}
		return this;
	}

	/**
	 * 文件表单项<br>
	 * 一旦有文件加入，表单变为multipart/form-data
	 *
	 * @param name  名
	 * @param files 需要上传的文件，为空跳过
	 * @return this
	 */
	public HttpRequest form(String name, File... files) {
		if (ArrayUtil.isEmpty(files)) {
			return this;
		}
		if (1 == files.length) {
			final File file = files[0];
			return form(name, file, file.getName());
		}
		return form(name, new MultiFileResource(files));
	}

	/**
	 * 文件表单项<br>
	 * 一旦有文件加入，表单变为multipart/form-data
	 *
	 * @param name 名
	 * @param file 需要上传的文件
	 * @return this
	 */
	public HttpRequest form(String name, File file) {
		return form(name, file, file.getName());
	}

	/**
	 * 文件表单项<br>
	 * 一旦有文件加入，表单变为multipart/form-data
	 *
	 * @param name     名
	 * @param file     需要上传的文件
	 * @param fileName 文件名，为空使用文件默认的文件名
	 * @return this
	 */
	public HttpRequest form(String name, File file, String fileName) {
		if (null != file) {
			form(name, new FileResource(file, fileName));
		}
		return this;
	}

	/**
	 * 文件byte[]表单项<br>
	 * 一旦有文件加入，表单变为multipart/form-data
	 *
	 * @param name      名
	 * @param fileBytes 需要上传的文件
	 * @param fileName  文件名
	 * @return this
	 * @since 4.1.0
	 */
	public HttpRequest form(String name, byte[] fileBytes, String fileName) {
		if (null != fileBytes) {
			form(name, new BytesResource(fileBytes, fileName));
		}
		return this;
	}

	/**
	 * 文件表单项<br>
	 * 一旦有文件加入，表单变为multipart/form-data
	 *
	 * @param name     名
	 * @param resource 数据源，文件可以使用{@link FileResource}包装使用
	 * @return this
	 * @since 4.0.9
	 */
	public HttpRequest form(String name, Resource resource) {
		if (null != resource) {
			if (false == isKeepAlive()) {
				keepAlive(true);
			}

			this.isMultiPart = true;
			return putToForm(name, resource);
		}
		return this;
	}

	/**
	 * 获取表单数据
	 *
	 * @return 表单Map
	 */
	public Map<String, Object> form() {
		return this.form;
	}

	/**
	 * 获取文件表单数据
	 *
	 * @return 文件表单Map
	 * @since 3.3.0
	 */
	public Map<String, Resource> fileForm() {
		final Map<String, Resource> result = MapUtil.newHashMap();
		this.form.forEach((key, value) -> {
			if (value instanceof Resource) {
				result.put(key, (Resource) value);
			}
		});
		return result;
	}
	// ---------------------------------------------------------------- Form end

	// ---------------------------------------------------------------- Body start

	/**
	 * 设置内容主体<br>
	 * 请求体body参数支持两种类型：
	 *
	 * <pre>
	 * 1. 标准参数，例如 a=1&amp;b=2 这种格式
	 * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，Hutool会自动绑定其对应的Content-Type
	 * </pre>
	 *
	 * @param body 请求体
	 * @return this
	 */
	public HttpRequest body(String body) {
		return this.body(body, null);
	}

	/**
	 * 设置内容主体<br>
	 * 请求体body参数支持两种类型：
	 *
	 * <pre>
	 * 1. 标准参数，例如 a=1&amp;b=2 这种格式
	 * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，Hutool会自动绑定其对应的Content-Type
	 * </pre>
	 *
	 * @param body        请求体
	 * @param contentType 请求体类型，{@code null}表示自动判断类型
	 * @return this
	 */
	public HttpRequest body(String body, String contentType) {
		byte[] bytes = StrUtil.bytes(body, this.charset);
		body(bytes);
		this.form = null; // 当使用body时，停止form的使用

		if (null != contentType) {
			// Content-Type自定义设置
			this.contentType(contentType);
		} else {
			// 在用户未自定义的情况下自动根据内容判断
			contentType = HttpUtil.getContentTypeByRequestBody(body);
			if (null != contentType && ContentType.isDefault(this.header(Header.CONTENT_TYPE))) {
				if (null != this.charset) {
					// 附加编码信息
					contentType = ContentType.build(contentType, this.charset);
				}
				this.contentType(contentType);
			}
		}

		// 判断是否为rest请求
		if (StrUtil.containsAnyIgnoreCase(contentType, "json", "xml")) {
			this.isRest = true;
			contentLength(bytes.length);
		}
		return this;
	}

	/**
	 * 设置主体字节码<br>
	 * 需在此方法调用前使用charset方法设置编码，否则使用默认编码UTF-8
	 *
	 * @param bodyBytes 主体
	 * @return this
	 */
	public HttpRequest body(byte[] bodyBytes) {
		if (null != bodyBytes) {
			this.bodyBytes = bodyBytes;
		}
		return this;
	}
	// ---------------------------------------------------------------- Body end

	/**
	 * 将新的配置加入<br>
	 * 注意加入的配置可能被修改
	 *
	 * @param config 配置
	 * @return this
	 */
	public HttpRequest setConfig(HttpConfig config) {
		this.config = config;
		return this;
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
	public HttpRequest timeout(int milliseconds) {
		config.timeout(milliseconds);
		return this;
	}

	/**
	 * 设置连接超时，单位：毫秒
	 *
	 * @param milliseconds 超时毫秒数
	 * @return this
	 * @since 4.5.6
	 */
	public HttpRequest setConnectionTimeout(int milliseconds) {
		config.setConnectionTimeout(milliseconds);
		return this;
	}

	/**
	 * 设置连接超时，单位：毫秒
	 *
	 * @param milliseconds 超时毫秒数
	 * @return this
	 * @since 4.5.6
	 */
	public HttpRequest setReadTimeout(int milliseconds) {
		config.setReadTimeout(milliseconds);
		return this;
	}

	/**
	 * 禁用缓存
	 *
	 * @return this
	 */
	public HttpRequest disableCache() {
		config.disableCache();
		return this;
	}

	/**
	 * 设置是否打开重定向，如果打开默认重定向次数为2<br>
	 * 此方法效果与{@link #setMaxRedirectCount(int)} 一致
	 *
	 * <p>
	 * 需要注意的是，当设置为{@code true}时，如果全局重定向次数非0，直接复用，否则设置默认2次。<br>
	 * 当设置为{@code false}时，无论全局是否设置次数，都设置为0。<br>
	 * 不调用此方法的情况下，使用全局默认的次数。
	 * </p>
	 *
	 * @param isFollowRedirects 是否打开重定向
	 * @return this
	 */
	public HttpRequest setFollowRedirects(boolean isFollowRedirects) {
		if (isFollowRedirects) {
			if (config.maxRedirectCount <= 0) {
				// 默认两次跳转
				return setMaxRedirectCount(2);
			}
		} else {
			// 手动强制关闭重定向，此时不受全局重定向设置影响
			if (config.maxRedirectCount < 0) {
				return setMaxRedirectCount(0);
			}
		}
		return this;
	}

	/**
	 * 设置最大重定向次数<br>
	 * 如果次数小于1则表示不重定向，大于等于1表示打开重定向
	 *
	 * @param maxRedirectCount 最大重定向次数
	 * @return this
	 * @since 3.3.0
	 */
	public HttpRequest setMaxRedirectCount(int maxRedirectCount) {
		config.setMaxRedirectCount(maxRedirectCount);
		return this;
	}

	/**
	 * 设置域名验证器<br>
	 * 只针对HTTPS请求，如果不设置，不做验证，所有域名被信任
	 *
	 * @param hostnameVerifier HostnameVerifier
	 * @return this
	 */
	public HttpRequest setHostnameVerifier(HostnameVerifier hostnameVerifier) {
		config.setHostnameVerifier(hostnameVerifier);
		return this;
	}

	/**
	 * 设置Http代理
	 *
	 * @param host 代理 主机
	 * @param port 代理 端口
	 * @return this
	 * @since 5.4.5
	 */
	public HttpRequest setHttpProxy(String host, int port) {
		config.setHttpProxy(host, port);
		return this;
	}

	/**
	 * 设置代理
	 *
	 * @param proxy 代理 {@link Proxy}
	 * @return this
	 */
	public HttpRequest setProxy(Proxy proxy) {
		config.setProxy(proxy);
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
	public HttpRequest setSSLSocketFactory(SSLSocketFactory ssf) {
		config.setSSLSocketFactory(ssf);
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
	public HttpRequest setSSLProtocol(String protocol) {
		config.setSSLProtocol(protocol);
		return this;
	}

	/**
	 * 设置是否rest模式<br>
	 * rest模式下get请求不会把参数附加到URL之后
	 *
	 * @param isRest 是否rest模式
	 * @return this
	 * @since 4.5.0
	 */
	public HttpRequest setRest(boolean isRest) {
		this.isRest = isRest;
		return this;
	}

	/**
	 * 采用流方式上传数据，无需本地缓存数据。<br>
	 * HttpUrlConnection默认是将所有数据读到本地缓存，然后再发送给服务器，这样上传大文件时就会导致内存溢出。
	 *
	 * @param blockSize 块大小（bytes数），0或小于0表示不设置Chuncked模式
	 * @return this
	 * @since 4.6.5
	 */
	public HttpRequest setChunkedStreamingMode(int blockSize) {
		config.setBlockSize(blockSize);
		return this;
	}

	/**
	 * 设置拦截器，用于在请求前重新编辑请求
	 *
	 * @param interceptor 拦截器实现
	 * @return this
	 * @see #addRequestInterceptor(HttpInterceptor)
	 * @since 5.7.16
	 */
	public HttpRequest addInterceptor(HttpInterceptor<HttpRequest> interceptor) {
		return addRequestInterceptor(interceptor);
	}

	/**
	 * 设置拦截器，用于在请求前重新编辑请求
	 *
	 * @param interceptor 拦截器实现
	 * @return this
	 * @since 5.8.0
	 */
	public HttpRequest addRequestInterceptor(HttpInterceptor<HttpRequest> interceptor) {
		config.addRequestInterceptor(interceptor);
		return this;
	}

	/**
	 * 设置拦截器，用于在请求前重新编辑请求
	 *
	 * @param interceptor 拦截器实现
	 * @return this
	 * @since 5.8.0
	 */
	public HttpRequest addResponseInterceptor(HttpInterceptor<HttpResponse> interceptor) {
		config.addResponseInterceptor(interceptor);
		return this;
	}

	/**
	 * 执行Reuqest请求
	 *
	 * @return this
	 */
	public HttpResponse execute() {
		return this.execute(false);
	}

	/**
	 * 异步请求<br>
	 * 异步请求后获取的{@link HttpResponse} 为异步模式，执行完此方法后发送请求到服务器，但是并不立即读取响应内容。<br>
	 * 此时保持Http连接不关闭，直调用获取内容方法为止。
	 *
	 * <p>
	 * 一般执行完execute之后会把响应内容全部读出来放在一个 byte数组里，如果你响应的内容太多内存就爆了，此法是发送完请求不直接读响应内容，等有需要的时候读。
	 *
	 * @return 异步对象，使用get方法获取HttpResponse对象
	 */
	public HttpResponse executeAsync() {
		return this.execute(true);
	}

	/**
	 * 执行Reuqest请求
	 *
	 * @param isAsync 是否异步
	 * @return this
	 */
	public HttpResponse execute(boolean isAsync) {
		return doExecute(isAsync, config.requestInterceptors, config.responseInterceptors);
	}

	/**
	 * 执行Request请求后，对响应内容后续处理<br>
	 * 处理结束后关闭连接
	 *
	 * @param consumer 响应内容处理函数
	 * @since 5.7.8
	 */
	public void then(Consumer<HttpResponse> consumer) {
		try (final HttpResponse response = execute(true)) {
			consumer.accept(response);
		}
	}

	/**
	 * 执行Request请求后，对响应内容后续处理<br>
	 * 处理结束后关闭连接
	 *
	 * @param <T>      处理结果类型
	 * @param function 响应内容处理函数
	 * @return 处理结果
	 * @since 5.8.5
	 */
	public <T> T thenFunction(Function<HttpResponse, T> function) {
		try (final HttpResponse response = execute(true)) {
			return function.apply(response);
		}
	}

	/**
	 * 简单验证，生成的头信息类似于：
	 * <pre>
	 * Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return this
	 */
	public HttpRequest basicAuth(String username, String password) {
		return auth(HttpUtil.buildBasicAuth(username, password, charset));
	}

	/**
	 * 简单代理验证，生成的头信息类似于：
	 * <pre>
	 * Proxy-Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return this
	 * @since 5.4.6
	 */
	public HttpRequest basicProxyAuth(String username, String password) {
		return proxyAuth(HttpUtil.buildBasicAuth(username, password, charset));
	}

	/**
	 * 令牌验证，生成的头类似于："Authorization: Bearer XXXXX"，一般用于JWT
	 *
	 * @param token 令牌内容
	 * @return HttpRequest
	 * @since 5.5.3
	 */
	public HttpRequest bearerAuth(String token) {
		return auth("Bearer " + token);
	}

	/**
	 * 验证，简单插入Authorization头
	 *
	 * @param content 验证内容
	 * @return HttpRequest
	 * @since 5.2.4
	 */
	public HttpRequest auth(String content) {
		header(Header.AUTHORIZATION, content, true);
		return this;
	}

	/**
	 * 验证，简单插入Authorization头
	 *
	 * @param content 验证内容
	 * @return HttpRequest
	 * @since 5.4.6
	 */
	public HttpRequest proxyAuth(String content) {
		header(Header.PROXY_AUTHORIZATION, content, true);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = StrUtil.builder();
		sb.append("Request Url: ").append(this.url.setCharset(this.charset)).append(StrUtil.CRLF);
		sb.append(super.toString());
		return sb.toString();
	}

	// ---------------------------------------------------------------- Private method start

	/**
	 * 执行Reuqest请求
	 *
	 * @param isAsync              是否异步
	 * @param requestInterceptors  请求拦截器列表
	 * @param responseInterceptors 响应拦截器列表
	 * @return this
	 */
	private HttpResponse doExecute(boolean isAsync, HttpInterceptor.Chain<HttpRequest> requestInterceptors,
								   HttpInterceptor.Chain<HttpResponse> responseInterceptors) {
		if (null != requestInterceptors) {
			for (HttpInterceptor<HttpRequest> interceptor : requestInterceptors) {
				interceptor.process(this);
			}
		}

		// 初始化URL
		urlWithParamIfGet();
		// 初始化 connection
		initConnection();
		// 发送请求
		send();

		// 手动实现重定向
		HttpResponse httpResponse = sendRedirectIfPossible(isAsync);

		// 获取响应
		if (null == httpResponse) {
			httpResponse = new HttpResponse(this.httpConnection, this.config, this.charset, isAsync, isIgnoreResponseBody());
		}

		// 拦截响应
		if (null != responseInterceptors) {
			for (HttpInterceptor<HttpResponse> interceptor : responseInterceptors) {
				interceptor.process(httpResponse);
			}
		}

		return httpResponse;
	}

	/**
	 * 初始化网络连接
	 */
	private void initConnection() {
		if (null != this.httpConnection) {
			// 执行下次请求时自动关闭上次请求（常用于转发）
			this.httpConnection.disconnectQuietly();
		}

		this.httpConnection = HttpConnection
				// issue#I50NHQ
				// 在生成正式URL前，设置自定义编码
				.create(this.url.setCharset(this.charset).toURL(this.urlHandler), config.proxy)//
				.setConnectTimeout(config.connectionTimeout)//
				.setReadTimeout(config.readTimeout)//
				.setMethod(this.method)//
				.setHttpsInfo(config.hostnameVerifier, config.ssf)//
				// 关闭JDK自动转发，采用手动转发方式
				.setInstanceFollowRedirects(false)
				// 流方式上传数据
				.setChunkedStreamingMode(config.blockSize)
				// 覆盖默认Header
				.header(this.headers, true);

		if (null != this.cookie) {
			// 当用户自定义Cookie时，全局Cookie自动失效
			this.httpConnection.setCookie(this.cookie);
		} else {
			// 读取全局Cookie信息并附带到请求中
			GlobalCookieManager.add(this.httpConnection);
		}

		// 是否禁用缓存
		if (config.isDisableCache) {
			this.httpConnection.disableCache();
		}
	}

	/**
	 * 对于GET请求将参数加到URL中<br>
	 * 此处不对URL中的特殊字符做单独编码<br>
	 * 对于非rest的GET请求，且处于重定向时，参数丢弃
	 */
	private void urlWithParamIfGet() {
		if (Method.GET.equals(method) && false == this.isRest && this.redirectCount <= 0) {
			UrlQuery query = this.url.getQuery();
			if (null == query) {
				query = new UrlQuery();
				this.url.setQuery(query);
			}

			// 优先使用body形式的参数，不存在使用form
			if (ArrayUtil.isNotEmpty(this.bodyBytes)) {
				query.parse(StrUtil.str(this.bodyBytes, this.charset), this.charset);
			} else {
				query.addAll(this.form);
			}
		}
	}

	/**
	 * 调用转发，如果需要转发返回转发结果，否则返回{@code null}
	 *
	 * @param isAsync 是否异步
	 * @return {@link HttpResponse}，无转发返回 {@code null}
	 */
	private HttpResponse sendRedirectIfPossible(boolean isAsync) {
		// 手动实现重定向
		if (config.maxRedirectCount > 0) {
			int responseCode;
			try {
				responseCode = httpConnection.responseCode();
			} catch (IOException e) {
				// 错误时静默关闭连接
				this.httpConnection.disconnectQuietly();
				throw new HttpException(e);
			}

			if (responseCode != HttpURLConnection.HTTP_OK) {
				if (HttpStatus.isRedirected(responseCode)) {
					final UrlBuilder redirectUrl;
					String location = httpConnection.header(Header.LOCATION);
					if (false == HttpUtil.isHttp(location) && false == HttpUtil.isHttps(location)) {
						// issue#I5TPSY
						// location可能为相对路径
						if (false == location.startsWith("/")) {
							location = StrUtil.addSuffixIfNot(this.url.getPathStr(), "/") + location;
						}
						redirectUrl = UrlBuilder.of(this.url.getScheme(), this.url.getHost(), this.url.getPort()
								, location, null, null, this.charset);
					} else {
						redirectUrl = UrlBuilder.ofHttpWithoutEncode(location);
					}
					setUrl(redirectUrl);
					if (redirectCount < config.maxRedirectCount) {
						redirectCount++;
						// 重定向不再走过滤器
						return doExecute(isAsync, config.interceptorOnRedirect ? config.requestInterceptors : null,
								config.interceptorOnRedirect ? config.responseInterceptors : null);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 发送数据流
	 *
	 * @throws IORuntimeException IO异常
	 */
	private void send() throws IORuntimeException {
		try {
			if (Method.POST.equals(this.method) //
					|| Method.PUT.equals(this.method) //
					|| Method.DELETE.equals(this.method) //
					|| this.isRest) {
				if (isMultipart()) {
					sendMultipart(); // 文件上传表单
				} else {
					sendFormUrlEncoded();// 普通表单
				}
			} else {
				this.httpConnection.connect();
			}
		} catch (IOException e) {
			// 异常时关闭连接
			this.httpConnection.disconnectQuietly();
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 发送普通表单<br>
	 * 发送数据后自动关闭输出流
	 *
	 * @throws IOException IO异常
	 */
	private void sendFormUrlEncoded() throws IOException {
		if (StrUtil.isBlank(this.header(Header.CONTENT_TYPE))) {
			// 如果未自定义Content-Type，使用默认的application/x-www-form-urlencoded
			this.httpConnection.header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.toString(this.charset), true);
		}

		// Write的时候会优先使用body中的内容，write时自动关闭OutputStream
		RequestBody body;
		if (ArrayUtil.isNotEmpty(this.bodyBytes)) {
			body = BytesBody.create(this.bodyBytes);
		} else {
			body = FormUrlEncodedBody.create(this.form, this.charset);
		}
		body.writeClose(this.httpConnection.getOutputStream());
	}

	/**
	 * 发送多组件请求（例如包含文件的表单）<br>
	 * 发送数据后自动关闭输出流
	 *
	 * @throws IOException IO异常
	 */
	private void sendMultipart() throws IOException {
		final MultipartBody multipartBody = MultipartBody.create(this.form, this.charset);
		//设置表单类型为Multipart（文件上传）
		this.httpConnection.header(Header.CONTENT_TYPE, multipartBody.getContentType(), true);
		multipartBody.writeClose(this.httpConnection.getOutputStream());
	}

	/**
	 * 是否忽略读取响应body部分<br>
	 * HEAD、CONNECT、OPTIONS、TRACE方法将不读取响应体
	 *
	 * @return 是否需要忽略响应body部分
	 * @since 3.1.2
	 */
	private boolean isIgnoreResponseBody() {
		return Method.HEAD == this.method //
				|| Method.CONNECT == this.method //
				|| Method.OPTIONS == this.method //
				|| Method.TRACE == this.method;
	}

	/**
	 * 判断是否为multipart/form-data表单，条件如下：
	 *
	 * <pre>
	 *     1. 存在资源对象（fileForm非空）
	 *     2. 用户自定义头为multipart/form-data开头
	 * </pre>
	 *
	 * @return 是否为multipart/form-data表单
	 * @since 5.3.5
	 */
	private boolean isMultipart() {
		if (this.isMultiPart) {
			return true;
		}

		final String contentType = header(Header.CONTENT_TYPE);
		return StrUtil.isNotEmpty(contentType) &&
				contentType.startsWith(ContentType.MULTIPART.getValue());
	}

	/**
	 * 将参数加入到form中，如果form为空，新建之。
	 *
	 * @param name  表单属性名
	 * @param value 属性值
	 * @return this
	 */
	private HttpRequest putToForm(String name, Object value) {
		if (null == name || null == value) {
			return this;
		}
		if (null == this.form) {
			this.form = new TableMap<>(16);
		}
		this.form.put(name, value);
		return this;
	}
	// ---------------------------------------------------------------- Private method end

}
