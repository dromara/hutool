package com.xiaoleilu.hutool.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.http.ssl.SSLSocketFactoryBuilder;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.json.JSON;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.util.ThreadUtil;

/**
 * http请求类<br>
 * Http请求类用于构建Http请求并同步获取结果，此类通过 {@link CookiePool}持有域名对应的Cookie值，再次请求时会自动附带Cookie信息
 * 
 * @author Looly
 */
public class HttpRequest extends HttpBase<HttpRequest> {
	private static final String BOUNDARY = "--------------------Hutool_" + RandomUtil.randomString(16);
	private static final byte[] BOUNDARY_END = StrUtil.format("--{}--\r\n", BOUNDARY).getBytes();
	private static final String CONTENT_DISPOSITION_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"\r\n\r\n";
	private static final String CONTENT_DISPOSITION_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"; filename=\"{}\"\r\n";
	
	private static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED_PREFIX = "application/x-www-form-urlencoded;charset=";
	private static final String CONTENT_TYPE_MULTIPART_PREFIX = "multipart/form-data; boundary=";
	private static final String CONTENT_TYPE_FILE_TEMPLATE = "Content-Type: {}\r\n\r\n";

	private String url = "";
	private Method method = Method.GET;
	/** 默认超时 */
	private int timeout = -1;
	/** 存储表单数据 */
	private Map<String, Object> form;
	/** 文件表单对象，用于文件上传 */
	private Map<String, File> fileForm;
	/** 文件表单对象，用于文件上传 */
	private String cookie;

	/** 连接对象 */
	private HttpConnection httpConnection;
	/** 是否禁用缓存 */
	private boolean isDisableCache;
	/** 是否允许重定向 */
	private Boolean isFollowRedirects;
	/** 重定向次数 */
	private int redirectCount;
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
		this.url = url;
	}

	// ---------------------------------------------------------------- Http Method start
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
	// ---------------------------------------------------------------- Http Method end

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
	 * @param cookie Cookie值，如果为{@code null}则设置无效，使用默认Cookie行为
	 * @return this
	 * @since 3.0.7
	 */
	public HttpRequest cookie(String cookie){
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
	public HttpRequest disableCookie(){
		return cookie(StrUtil.EMPTY);
	}
	
	/**
	 * 打开默认的Cookie行为（自动回填服务器传回的Cookie）
	 * @return this
	 */
	public HttpRequest enableDefaultCookie(){
		return cookie(null);
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
		if(StrUtil.isBlank(name) || ObjectUtil.isNull(value)){
			return this;	//忽略非法的form表单项内容;
		}
		
		// 停用body
		this.body = null;

		if (value instanceof File) {
			return this.form(name, (File) value);
		} else if (this.form == null) {
			form = new HashMap<String, Object>();
		}

		String strValue;
		if (value instanceof List) {
			// 列表对象
			strValue = CollectionUtil.join((List<?>) value, ",");
		} else if (ArrayUtil.isArray(value)) {
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
		for (Map.Entry<String, Object> entry : formMap.entrySet()) {
			form(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * 文件表单项<br>
	 * 一旦有文件加入，表单变为multipart/form-data
	 * 
	 * @param name 名
	 * @param file 文件
	 * @return this
	 */
	public HttpRequest form(String name, File file) {
		if (null == file) {
			return this;
		}

		if (false == isKeepAlive()) {
			keepAlive(true);
		}

		if (this.fileForm == null) {
			fileForm = new HashMap<String, File>();
		}
		// 文件对象
		this.fileForm.put(name, file);
		return this;
	}

	/**
	 * 获取表单数据
	 * 
	 * @return 表单Map
	 */
	public Map<String, Object> form() {
		return form;
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
		this.body = body;
		this.form = null; // 当使用body时，废弃form的使用
		contentLength(body.length());
		return this;
	}
	
	/**
	 * 设置内容主体
	 * 
	 * @param body 请求体
	 * @param contentType 请求体类型
	 * @return this
	 */
	public HttpRequest body(String body, String contentType) {
		this.body(body);
		this.contentType(contentType);
		return this;
	}
	
	/**
	 * 设置JSON内容主体<br>
	 * 设置默认的Content-Type为 application/json
	 * 需在此方法调用前使用charset方法设置编码，否则使用默认编码UTF-8
	 * 
	 * @param json JSON请求体
	 * @return this
	 */
	public HttpRequest body(JSON json) {
		this.body(json.toString());
		
		String contentTypeJson = "application/json";
		if(StrUtil.isNotBlank(this.charset)){
			contentTypeJson = StrUtil.format("{};charset={}", contentTypeJson, this.charset);
		}
		this.contentType(contentTypeJson);
		
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
		return body(StrUtil.str(bodyBytes, this.charset));
	}
	// ---------------------------------------------------------------- Body end

	/**
	 * 设置超时
	 * 
	 * @param milliseconds 超时毫秒数
	 * @return this
	 */
	public HttpRequest timeout(int milliseconds) {
		this.timeout = milliseconds;
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
	 * 设置是否打开重定向
	 * @param isFollowRedirects 是否打开重定向
	 * @return this
	 */
	public HttpRequest setFollowRedirects(Boolean isFollowRedirects) {
		this.isFollowRedirects = isFollowRedirects;
		return this;
	}
	
	/**
	 * 设置域名验证器<br>
	 * 只针对HTTPS请求，如果不设置，不做验证，所有域名被信任
	 * 
	 * @param hostnameVerifier HostnameVerifier
	 */
	public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
		// 验证域
		this.hostnameVerifier = hostnameVerifier;
	}
	
	/**
	 * 设置代理
	 * @param proxy 代理 {@link Proxy}
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
	
	/**
	 * 设置SSLSocketFactory<br>
	 * 只针对HTTPS请求，如果不设置，使用默认的SSLSocketFactory<br>
	 * 默认SSLSocketFactory为：SSLSocketFactoryBuilder.create().build();
	 * 
	 * @param ssf SSLScketFactory
	 * @return this
	 */
	public HttpRequest setSSLSocketFactory(SSLSocketFactory ssf){
		this.ssf = ssf;
		return this;
	}
	
	/**
	 * 设置HTTPS安全连接协议，只针对HTTPS请求<br>
	 * @see SSLSocketFactoryBuilder
	 * @param protocol 协议
	 * @return this
	 */
	public HttpRequest setSSLProtocol(String protocol){
		if(null == this.ssf){
			try {
				this.ssf = SSLSocketFactoryBuilder.create().setProtocol(protocol).build();
			} catch (Exception e) {
				throw new HttpException(e);
			}
		}
		return this;
	}

	/**
	 * 执行Reuqest请求
	 * 
	 * @return HttpResponse
	 */
	public HttpResponse execute() {
		//初始化URL
		urlWithParamIfGet();
		// 初始化 connection
		initConnecton();

		// 发送请求
		send();
		
		//手动实现重定向
		HttpResponse httpResponse = sendRedirectIfPosible();

		// 获取响应
		if(null == httpResponse){
			httpResponse = HttpResponse.readResponse(httpConnection);
		}

		this.httpConnection.disconnect();
		return httpResponse;
	}
	
	/**
	 * 异步请求
	 * @return 异步对象，使用get方法获取HttpResponse对象
	 */
	public Future<HttpResponse> asyncExecute(){
		return ThreadUtil.execAsync(new Callable<HttpResponse>(){
			@Override
			public HttpResponse call() throws Exception {
				return execute();
			}
		});
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
	private void initConnecton(){
		// 初始化 connection
		this.httpConnection = HttpConnection
				.create(this.url, this.method, this.hostnameVerifier, this.ssf, this.timeout, this.proxy)
				.header(this.headers, true); // 覆盖默认Header
		
		//自定义Cookie
		if(null != this.cookie){
			this.httpConnection.setCookie(this.cookie);
		}
		
		//是否禁用缓存
		if(this.isDisableCache){
			this.httpConnection.disableCache();
		}
		
		if(null != isFollowRedirects){
			//如果自定义了转发，则设置，否则使用默认
			this.httpConnection.setInstanceFollowRedirects(isFollowRedirects);
		}
	}
	
	/**
	 * 对于GET请求将参数加到URL中
	 */
	private void urlWithParamIfGet(){
		if (Method.GET.equals(method)) {
			// 优先使用body形式的参数，不存在使用form
			if (StrUtil.isNotBlank(this.body)) {
				this.url = HttpUtil.urlWithForm(this.url, this.body);
			} else {
				this.url = HttpUtil.urlWithForm(this.url, this.form);
			}
		}
	}
	
	/**
	 * 调用转发，如果需要转发返回转发结果，否则返回<code>null</code>
	 * @return {@link HttpResponse}，无转发返回 <code>null</code>
	 */
	private HttpResponse sendRedirectIfPosible(){
		//手动实现重定向
		if(this.httpConnection.getHttpURLConnection().getInstanceFollowRedirects()){
			int responseCode;
			try {
				responseCode = httpConnection.responseCode();
			} catch (IOException e) {
				throw new HttpException(e);
			}
			if(responseCode != HttpURLConnection.HTTP_OK){
				if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER){
					this.url = httpConnection.header(Header.LOCATION);
					if(redirectCount < 2){
						redirectCount++;
						return execute();
					}else{
						StaticLog.warn("URL [{}] redirect count more than two !", this.url);
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
			if (Method.POST.equals(method) || Method.PUT.equals(method)) {
				if (CollectionUtil.isEmpty(fileForm)) {
					sendFormUrlEncoded();//普通表单
				} else {
					sendMltipart();	//文件上传表单
				}
			}else{
				this.httpConnection.connect();
			}
		} catch (IOException e) {
			throw new HttpException(e.getMessage(), e);
		}
	}
	
	/**
	 * 发送普通表单
	 * @throws IOException
	 */
	private void sendFormUrlEncoded() throws IOException{
		if(StrUtil.isBlank(this.header(Header.CONTENT_TYPE))){
			//如果未自定义Content-Type，使用默认的application/x-www-form-urlencoded
			this.httpConnection.header(Header.CONTENT_TYPE, CONTENT_TYPE_X_WWW_FORM_URLENCODED_PREFIX + this.charset, true);
		}
		
		// Write的时候会优先使用body中的内容，write时自动关闭OutputStream
		String content;
		if (StrUtil.isNotBlank(this.body)) {
			content = this.body;
		} else {
			content = HttpUtil.toParams(this.form, this.charset);
		}
		IoUtil.write(this.httpConnection.getOutputStream(), this.charset, true, content);
	}

	/**
	 * 发送多组件请求（例如包含文件的表单）
	 * 
	 * @throws IOException
	 */
	private void sendMltipart() throws IOException {
		setMultipart();//设置表单类型为Multipart
		
		final OutputStream out = this.httpConnection.getOutputStream();
		try {
			writeFileForm(out);
			writeForm(out);
			formEnd(out);
		} catch (IOException e) {
			throw e;
		}finally{
			IoUtil.close(out);
		}
	}

	// 普通字符串数据
	/**
	 * 发送普通表单内容
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
	 * @param out 输出流
	 * @throws IOException
	 */
	private void writeFileForm(OutputStream out) throws IOException {
		File file;
		for (Entry<String, File> entry : this.fileForm.entrySet()) {
			file = entry.getValue();
			StringBuilder builder = StrUtil.builder().append("--").append(BOUNDARY).append(StrUtil.CRLF);
			builder.append(StrUtil.format(CONTENT_DISPOSITION_FILE_TEMPLATE, entry.getKey(), file.getName()));
			builder.append(StrUtil.format(CONTENT_TYPE_FILE_TEMPLATE, HttpUtil.getMimeType(file.getName())));
			IoUtil.write(out, this.charset, false, builder);
			FileUtil.writeToStream(file, out);
			IoUtil.write(out, this.charset, false, StrUtil.CRLF);
		}
	}

	// 添加结尾数据
	/**
	 * 上传表单结束
	 * @param out 输出流
	 * @throws IOException
	 */
	private void formEnd(OutputStream out) throws IOException {
		out.write(BOUNDARY_END);
		out.flush();
	}
	
	/**
	 * 设置表单类型为Multipart（文件上传）
	 * @return HttpConnection
	 */
	private void setMultipart(){
		this.httpConnection.header(Header.CONTENT_TYPE, CONTENT_TYPE_MULTIPART_PREFIX + BOUNDARY, true);
	}
	// ---------------------------------------------------------------- Private method end

}
