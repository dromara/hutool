package cn.hutool.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.MultiFileResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.cookie.GlobalCookieManager;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;
import cn.hutool.json.JSON;
import cn.hutool.log.StaticLog;

/**
 * http请求类<br>
 * Http请求类用于构建Http请求并同步获取结果，此类通过CookieManager持有域名对应的Cookie值，再次请求时会自动附带Cookie信息
 * 
 * @author Looly
 */
public class HttpRequest extends HttpBase<HttpRequest> {

	private static final String BOUNDARY = "--------------------Hutool_" + RandomUtil.randomString(16);
	private static final byte[] BOUNDARY_END = StrUtil.format("--{}--\r\n", BOUNDARY).getBytes();
	private static final String CONTENT_DISPOSITION_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"\r\n\r\n";
	private static final String CONTENT_DISPOSITION_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"; filename=\"{}\"\r\n";

	private static final String CONTENT_TYPE_MULTIPART_PREFIX = "multipart/form-data; boundary=";
	private static final String CONTENT_TYPE_FILE_TEMPLATE = "Content-Type: {}\r\n\r\n";
	
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
	 * @since 4.1.0
	 * @see GlobalCookieManager#getCookieManager()
	 */
	public static CookieManager getCookieManager() {
		return GlobalCookieManager.getCookieManager();
	}

	/**
	 * 自定义{@link CookieManager}
	 * 
	 * @param customCookieManager 自定义的{@link CookieManager}
	 * @since 4.5.14
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 */
	public static void setCookieManager(CookieManager customCookieManager) {
		GlobalCookieManager.setCookieManager(customCookieManager);
	}

	/**
	 * 关闭Cookie
	 * 
	 * @since 4.1.9
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 */
	public static void closeCookie() {
		GlobalCookieManager.setCookieManager(null);
	}

	private String url;
	private URLStreamHandler urlHandler;
	private Method method = Method.GET;
	/** 默认连接超时 */
	private int connectionTimeout = HttpGlobalConfig.timeout;
	/** 默认读取超时 */
	private int readTimeout = HttpGlobalConfig.timeout;
	/** 存储表单数据 */
	private Map<String, Object> form;
	/** 文件表单对象，用于文件上传 */
	private Map<String, Resource> fileForm;
	/** Cookie */
	private String cookie;

	/** 连接对象 */
	private HttpConnection httpConnection;
	/** 是否禁用缓存 */
	private boolean isDisableCache;
	/** 是否对url中的参数进行编码 */
	private boolean encodeUrlParams;
	/** 是否是REST请求模式 */
	private boolean isRest;
	/** 重定向次数计数器，内部使用 */
	private int redirectCount;
	/** 最大重定向次数 */
	private int maxRedirectCount;
	/** 代理 */
	private Proxy proxy;

	/** HostnameVerifier，用于HTTPS安全连接 */
	private HostnameVerifier hostnameVerifier;
	/** SSLSocketFactory，用于HTTPS安全连接 */
	private SSLSocketFactory ssf;

	/**
	 * 构造
	 * 
	 * @param url URL
	 */
	public HttpRequest(String url) {
		Assert.notBlank(url, "Param [url] can not be blank !");
		this.url = URLUtil.normalize(url, true);
		// 给定一个默认头信息
		this.header(GlobalHeaders.INSTANCE.headers);
	}

	// ---------------------------------------------------------------- static Http Method start
	/**
	 * POST请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest post(String url) {
		return new HttpRequest(url).method(Method.POST);
	}

	/**
	 * GET请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest get(String url) {
		return new HttpRequest(url).method(Method.GET);
	}

	/**
	 * HEAD请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest head(String url) {
		return new HttpRequest(url).method(Method.HEAD);
	}

	/**
	 * OPTIONS请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest options(String url) {
		return new HttpRequest(url).method(Method.OPTIONS);
	}

	/**
	 * PUT请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest put(String url) {
		return new HttpRequest(url).method(Method.PUT);
	}

	/**
	 * PATCH请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 * @since 3.0.9
	 */
	public static HttpRequest patch(String url) {
		return new HttpRequest(url).method(Method.PATCH);
	}

	/**
	 * DELETE请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest delete(String url) {
		return new HttpRequest(url).method(Method.DELETE);
	}

	/**
	 * TRACE请求
	 * 
	 * @param url URL
	 * @return HttpRequest
	 */
	public static HttpRequest trace(String url) {
		return new HttpRequest(url).method(Method.TRACE);
	}
	// ---------------------------------------------------------------- static Http Method end

	/**
	 * 获取请求URL
	 * 
	 * @return URL字符串
	 * @since 4.1.8
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置URL
	 * 
	 * @param url url字符串
	 * @since 4.1.8
	 */
	public HttpRequest setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 设置{@link URLStreamHandler}
	 * <p>
	 * 部分环境下需要单独设置此项，例如当 WebLogic Server 实例充当 SSL 客户端角色（它会尝试通过 SSL 连接到其他服务器或应用程序）时，<br>
	 * 它会验证 SSL 服务器在数字证书中返回的主机名是否与用于连接 SSL 服务器的 URL 主机名相匹配。如果主机名不匹配，则删除此连接。<br>
	 * 因此weblogic不支持https的sni协议的主机名验证，此时需要将此值设置为sun.net.www.protocol.https.Handler对象。
	 * <p>
	 * 相关issue见：https://gitee.com/loolly/hutool/issues/IMD1X
	 * 
	 * @param urlHandler {@link URLStreamHandler}
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
	 * 获取{@link HttpConnection}
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
		if (Method.PATCH == method) {
			this.method = Method.POST;
			this.header("X-HTTP-Method-Override", "PATCH");
		} else {
			this.method = method;
		}
		
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
			return !httpVersion.equalsIgnoreCase(HTTP_1_0);
		}

		return !connection.equalsIgnoreCase("close");
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
	 * @since 3.1.1
	 */
	public HttpRequest cookie(HttpCookie... cookies) {
		if (ArrayUtil.isEmpty(cookies)) {
			return disableCookie();
		}
		return cookie(ArrayUtil.join(cookies, ";"));
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
	 * @param name 名
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
		} else if (value instanceof Resource) {
			// 自定义流上传
			return this.form(name, (Resource) value);
		} else if (this.form == null) {
			this.form = new LinkedHashMap<>();
		}

		String strValue;
		if (value instanceof List) {
			// 列表对象
			strValue = CollectionUtil.join((List<?>) value, ",");
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

		form.put(name, strValue);
		return this;
	}

	/**
	 * 设置表单数据
	 * 
	 * @param name 名
	 * @param value 值
	 * @param parameters 参数对，奇数为名，偶数为值
	 * @return this
	 * 
	 */
	public HttpRequest form(String name, Object value, Object... parameters) {
		form(name, value);

		for (int i = 0; i < parameters.length; i += 2) {
			name = parameters[i].toString();
			form(name, parameters[i + 1]);
		}
		return this;
	}

	/**
	 * 设置map类型表单数据
	 * 
	 * @param formMap 表单内容
	 * @return this
	 * 
	 */
	public HttpRequest form(Map<String, Object> formMap) {
		if (MapUtil.isNotEmpty(formMap)) {
			for (Map.Entry<String, Object> entry : formMap.entrySet()) {
				form(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	/**
	 * 文件表单项<br>
	 * 一旦有文件加入，表单变为multipart/form-data
	 * 
	 * @param name 名
	 * @param files 需要上传的文件
	 * @return this
	 */
	public HttpRequest form(String name, File... files) {
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
	 * @param name 名
	 * @param file 需要上传的文件
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
	 * @param name 名
	 * @param fileBytes 需要上传的文件
	 * @param fileName 文件名
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
	 * @param name 名
	 * @param resource 数据源，文件可以使用{@link FileResource}包装使用
	 * @return this
	 * @since 4.0.9
	 */
	public HttpRequest form(String name, Resource resource) {
		if (null != resource) {
			if (false == isKeepAlive()) {
				keepAlive(true);
			}

			if (null == this.fileForm) {
				fileForm = new HashMap<>();
			}
			// 文件对象
			this.fileForm.put(name, resource);
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
		return this.fileForm;
	}
	// ---------------------------------------------------------------- Form end

	// ---------------------------------------------------------------- Body start
	/**
	 * 设置内容主体
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
	 * @param body 请求体
	 * @param contentType 请求体类型，{@code null}表示自动判断类型
	 * @return this
	 */
	public HttpRequest body(String body, String contentType) {
		body(StrUtil.bytes(body, this.charset));
		this.form = null; // 当使用body时，停止form的使用
		contentLength((null != body ? body.length() : 0));

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
		}
		return this;
	}

	/**
	 * 设置JSON内容主体<br>
	 * 设置默认的Content-Type为 application/json 需在此方法调用前使用charset方法设置编码，否则使用默认编码UTF-8
	 * 
	 * @param json JSON请求体
	 * @return this
	 */
	public HttpRequest body(JSON json) {
		return this.body(json.toString());
	}

	/**
	 * 设置主体字节码<br>
	 * 需在此方法调用前使用charset方法设置编码，否则使用默认编码UTF-8
	 * 
	 * @param bodyBytes 主体
	 * @return this
	 */
	public HttpRequest body(byte[] bodyBytes) {
		this.bodyBytes = bodyBytes;
		return this;
	}
	// ---------------------------------------------------------------- Body end

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
		setConnectionTimeout(milliseconds);
		setReadTimeout(milliseconds);
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
		this.connectionTimeout = milliseconds;
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
		this.readTimeout = milliseconds;
		return this;
	}

	/**
	 * 禁用缓存
	 * 
	 * @return this
	 */
	public HttpRequest disableCache() {
		this.isDisableCache = true;
		return this;
	}

	/**
	 * 是否对URL中的参数进行编码
	 * 
	 * @param isEncodeUrlParams 是否对URL中的参数进行编码
	 * @return this
	 * @since 4.4.1
	 */
	public HttpRequest setEncodeUrlParams(boolean isEncodeUrlParams) {
		this.encodeUrlParams = isEncodeUrlParams;
		return this;
	}

	/**
	 * 设置是否打开重定向，如果打开默认重定向次数为2<br>
	 * 此方法效果与{@link #setMaxRedirectCount(int)} 一致
	 * 
	 * @param isFollowRedirects 是否打开重定向
	 * @return this
	 */
	public HttpRequest setFollowRedirects(boolean isFollowRedirects) {
		return setMaxRedirectCount(isFollowRedirects ? 2 : 0);
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
		if (maxRedirectCount > 0) {
			this.maxRedirectCount = maxRedirectCount;
		} else {
			this.maxRedirectCount = 0;
		}
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
		// 验证域
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	/**
	 * 设置代理
	 * 
	 * @param proxy 代理 {@link Proxy}
	 * @return this
	 */
	public HttpRequest setProxy(Proxy proxy) {
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
	public HttpRequest setSSLSocketFactory(SSLSocketFactory ssf) {
		this.ssf = ssf;
		return this;
	}

	/**
	 * 设置HTTPS安全连接协议，只针对HTTPS请求，可以使用的协议包括：<br>
	 * 
	 * <pre>
	 * 1. TLSv1.2
	 * 2. TLSv1.1
	 * 3. SSLv3
	 * ...
	 * </pre>
	 * 
	 * @see SSLSocketFactoryBuilder
	 * @param protocol 协议
	 * @return this
	 */
	public HttpRequest setSSLProtocol(String protocol) {
		if (null == this.ssf) {
			try {
				this.ssf = SSLSocketFactoryBuilder.create().setProtocol(protocol).build();
			} catch (Exception e) {
				throw new HttpException(e);
			}
		}
		return this;
	}

	/**
	 * 设置是否rest模式
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
	 * 执行Reuqest请求
	 * 
	 * @return this
	 */
	public HttpResponse execute() {
		return this.execute(false);
	}

	/**
	 * 异步请求<br>
	 * 异步请求后获取的{@link HttpResponse} 为异步模式，此时此对象持有Http链接（http链接并不会关闭），直调用获取内容方法为止
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
		// 初始化URL
		urlWithParamIfGet();
		// 编码URL
		if (this.encodeUrlParams) {
			this.url = HttpUtil.encodeParams(this.url, this.charset);
		}
		// 初始化 connection
		initConnecton();

		// 发送请求
		send();

		// 手动实现重定向
		HttpResponse httpResponse = sendRedirectIfPosible();

		// 获取响应
		if (null == httpResponse) {
			httpResponse = new HttpResponse(this.httpConnection, this.charset, isAsync, isIgnoreResponseBody());
		}
		return httpResponse;
	}

	/**
	 * 简单验证
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @return HttpRequest
	 */
	public HttpRequest basicAuth(String username, String password) {
		final String data = username.concat(":").concat(password);
		final String base64 = Base64.encode(data, charset);

		header("Authorization", "Basic " + base64, true);

		return this;
	}

	// ---------------------------------------------------------------- Private method start
	/**
	 * 初始化网络连接
	 */
	private void initConnecton() {
		if(null != this.httpConnection) {
			// 执行下次请求时自动关闭上次请求（常用于转发）
			this.httpConnection.disconnectQuietly();
		}
		
		this.httpConnection = HttpConnection.create(URLUtil.toUrlForHttp(this.url, this.urlHandler), this.proxy)//
				.setMethod(this.method)//
				.setHttpsInfo(this.hostnameVerifier, this.ssf)//
				.setConnectTimeout(this.connectionTimeout)//
				.setReadTimeout(this.readTimeout)//
				// 自定义Cookie
				.setCookie(this.cookie)
				// 定义转发
				.setInstanceFollowRedirects(this.maxRedirectCount > 0 ? true : false)
				// 覆盖默认Header
				.header(this.headers, true);
		
		// 读取全局Cookie信息并附带到请求中
		GlobalCookieManager.add(this.httpConnection);

		// 是否禁用缓存
		if (this.isDisableCache) {
			this.httpConnection.disableCache();
		}
	}

	/**
	 * 对于GET请求将参数加到URL中<br>
	 * 此处不对URL中的特殊字符做单独编码
	 */
	private void urlWithParamIfGet() {
		if (Method.GET.equals(method) && false == this.isRest) {
			// 优先使用body形式的参数，不存在使用form
			if (ArrayUtil.isNotEmpty(this.bodyBytes)) {
				this.url = HttpUtil.urlWithForm(this.url, StrUtil.str(this.bodyBytes, this.charset), this.charset, false);
			} else {
				this.url = HttpUtil.urlWithForm(this.url, this.form, this.charset, false);
			}
		}
	}

	/**
	 * 调用转发，如果需要转发返回转发结果，否则返回<code>null</code>
	 * 
	 * @return {@link HttpResponse}，无转发返回 <code>null</code>
	 */
	private HttpResponse sendRedirectIfPosible() {
		if (this.maxRedirectCount < 1) {
			// 不重定向
			return null;
		}

		// 手动实现重定向
		if (this.httpConnection.getHttpURLConnection().getInstanceFollowRedirects()) {
			int responseCode;
			try {
				responseCode = httpConnection.responseCode();
			} catch (IOException e) {
				// 错误时静默关闭连接
				this.httpConnection.disconnectQuietly();
				throw new HttpException(e);
			}
			if (responseCode != HttpURLConnection.HTTP_OK) {
				if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
					this.url = httpConnection.header(Header.LOCATION);
					if (redirectCount < this.maxRedirectCount) {
						redirectCount++;
						return execute();
					} else {
						StaticLog.warn("URL [{}] redirect count more than {} !", this.url, this.maxRedirectCount);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 发送数据流
	 * 
	 * @throws IOException
	 */
	private void send() throws HttpException {
		try {
			if (Method.POST.equals(this.method) || Method.PUT.equals(this.method) || Method.DELETE.equals(this.method) || this.isRest) {
				if (CollectionUtil.isEmpty(this.fileForm)) {
					sendFormUrlEncoded();// 普通表单
				} else {
					sendMultipart(); // 文件上传表单
				}
			} else {
				this.httpConnection.connect();
			}
		} catch (IOException e) {
			// 异常时关闭连接
			this.httpConnection.disconnectQuietly();
			throw new HttpException(e);
		}
	}

	/**
	 * 发送普通表单<br>
	 * 发送数据后自动关闭输出流
	 * 
	 * @throws IOException
	 */
	private void sendFormUrlEncoded() throws IOException {
		if (StrUtil.isBlank(this.header(Header.CONTENT_TYPE))) {
			// 如果未自定义Content-Type，使用默认的application/x-www-form-urlencoded
			this.httpConnection.header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.toString(this.charset), true);
		}

		// Write的时候会优先使用body中的内容，write时自动关闭OutputStream
		if (ArrayUtil.isNotEmpty(this.bodyBytes)) {
			IoUtil.write(this.httpConnection.getOutputStream(), true, this.bodyBytes);
		} else {
			final String content = HttpUtil.toParams(this.form, this.charset);
			IoUtil.write(this.httpConnection.getOutputStream(), this.charset, true, content);
		}
	}

	/**
	 * 发送多组件请求（例如包含文件的表单）<br>
	 * 发送数据后自动关闭输出流
	 * 
	 * @throws IOException
	 */
	private void sendMultipart() throws IOException {
		setMultipart();// 设置表单类型为Multipart

		try(OutputStream out = this.httpConnection.getOutputStream()) {
			writeFileForm(out);
			writeForm(out);
			formEnd(out);
		} catch (IOException e) {
			throw e;
		}
	}

	// 普通字符串数据
	/**
	 * 发送普通表单内容
	 * 
	 * @param out 输出流
	 * @throws IOException
	 */
	private void writeForm(OutputStream out) throws IOException {
		if (CollectionUtil.isNotEmpty(this.form)) {
			StringBuilder builder = StrUtil.builder();
			for (Entry<String, Object> entry : this.form.entrySet()) {
				builder.append("--").append(BOUNDARY).append(StrUtil.CRLF);
				builder.append(StrUtil.format(CONTENT_DISPOSITION_TEMPLATE, entry.getKey()));
				builder.append(entry.getValue()).append(StrUtil.CRLF);
			}
			IoUtil.write(out, this.charset, false, builder);
		}
	}

	/**
	 * 发送文件对象表单
	 * 
	 * @param out 输出流
	 * @throws IOException
	 */
	private void writeFileForm(OutputStream out) throws IOException {
		for (Entry<String, Resource> entry : this.fileForm.entrySet()) {
			appendPart(entry.getKey(), entry.getValue(), out);
		}
	}

	/**
	 * 添加Multipart表单的数据项
	 * 
	 * @param formFieldName 表单名
	 * @param resource 资源，可以是文件等
	 * @param out Http流
	 * @since 4.1.0
	 */
	private void appendPart(String formFieldName, Resource resource, OutputStream out) {
		if (resource instanceof MultiResource) {
			// 多资源
			for (Resource subResource : (MultiResource) resource) {
				appendPart(formFieldName, subResource, out);
			}
		} else {
			// 普通资源
			final StringBuilder builder = StrUtil.builder().append("--").append(BOUNDARY).append(StrUtil.CRLF);
			final String fileName = resource.getName();
			builder.append(StrUtil.format(CONTENT_DISPOSITION_FILE_TEMPLATE, formFieldName, ObjectUtil.defaultIfNull(fileName, formFieldName)));
			builder.append(StrUtil.format(CONTENT_TYPE_FILE_TEMPLATE, HttpUtil.getMimeType(fileName)));
			IoUtil.write(out, this.charset, false, builder);
			InputStream in = null;
			try {
				in = resource.getStream();
				IoUtil.copy(in, out);
			} finally {
				IoUtil.close(in);
			}
			IoUtil.write(out, this.charset, false, StrUtil.CRLF);
		}

	}

	// 添加结尾数据
	/**
	 * 上传表单结束
	 * 
	 * @param out 输出流
	 * @throws IOException
	 */
	private void formEnd(OutputStream out) throws IOException {
		out.write(BOUNDARY_END);
		out.flush();
	}

	/**
	 * 设置表单类型为Multipart（文件上传）
	 * 
	 * @return HttpConnection
	 */
	private void setMultipart() {
		this.httpConnection.header(Header.CONTENT_TYPE, CONTENT_TYPE_MULTIPART_PREFIX + BOUNDARY, true);
	}

	/**
	 * 是否忽略读取响应body部分<br>
	 * HEAD、CONNECT、OPTIONS、TRACE方法将不读取响应体
	 * 
	 * @return 是否需要忽略响应body部分
	 * @since 3.1.2
	 */
	private boolean isIgnoreResponseBody() {
		if (Method.HEAD == this.method || Method.CONNECT == this.method || Method.OPTIONS == this.method || Method.TRACE == this.method) {
			return true;
		}
		return false;
	}
	// ---------------------------------------------------------------- Private method end

}
