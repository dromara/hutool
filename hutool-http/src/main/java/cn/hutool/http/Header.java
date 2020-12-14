package cn.hutool.http;

/**
 * Http 头域
 *
 * @author Looly
 */
public enum Header {

	//------------------------------------------------------------- 通用头域
	/**
	 * 提供验证头，例如：
	 * <pre>
	 * Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 */
	AUTHORIZATION("Authorization"),
	/**
	 * 提供给代理服务器的用于身份验证的凭证，例如：
	 * <pre>
	 * Proxy-Authorization: Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 */
	PROXY_AUTHORIZATION("Proxy-Authorization"),
	/**
	 * 提供日期和时间标志,说明报文是什么时间创建的
	 */
	DATE("Date"),
	/**
	 * 允许客户端和服务器指定与请求/响应连接有关的选项
	 */
	CONNECTION("Connection"),
	/**
	 * 给出发送端使用的MIME版本
	 */
	MIME_VERSION("MIME-Version"),
	/**
	 * 如果报文采用了分块传输编码(chunked transfer encoding) 方式,就可以用这个首部列出位于报文拖挂(trailer)部分的首部集合
	 */
	TRAILER("Trailer"),
	/**
	 * 告知接收端为了保证报文的可靠传输,对报文采用了什么编码方式
	 */
	TRANSFER_ENCODING("Transfer-Encoding"),
	/**
	 * 给出了发送端可能想要"升级"使用的新版本和协议
	 */
	UPGRADE("Upgrade"),
	/**
	 * 显示了报文经过的中间节点
	 */
	VIA("Via"),
	/**
	 * 指定请求和响应遵循的缓存机制
	 */
	CACHE_CONTROL("Cache-Control"),
	/**
	 * 用来包含实现特定的指令，最常用的是Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache- Control:no-cache相同
	 */
	PRAGMA("Pragma"),
	/**
	 * 请求表示提交内容类型或返回返回内容的MIME类型
	 */
	CONTENT_TYPE("Content-Type"),

	//------------------------------------------------------------- 请求头域
	/**
	 * 指定请求资源的Intenet主机和端口号，必须表示请求url的原始服务器或网关的位置。HTTP/1.1请求必须包含主机头域，否则系统会以400状态码返回
	 */
	HOST("Host"),
	/**
	 * 允许客户端指定请求uri的源资源地址，这可以允许服务器生成回退链表，可用来登陆、优化cache等。他也允许废除的或错误的连接由于维护的目的被 追踪。如果请求的uri没有自己的uri地址，Referer不能被发送。如果指定的是部分uri地址，则此地址应该是一个相对地址
	 */
	REFERER("Referer"),
	/**
	 * 指定请求的域
	 */
	ORIGIN("Origin"),
	/**
	 * HTTP客户端运行的浏览器类型的详细信息。通过该头部信息，web服务器可以判断到当前HTTP请求的客户端浏览器类别
	 */
	USER_AGENT("User-Agent"),
	/**
	 * 指定客户端能够接收的内容类型，内容类型中的先后次序表示客户端接收的先后次序
	 */
	ACCEPT("Accept"),
	/**
	 * 指定HTTP客户端浏览器用来展示返回信息所优先选择的语言
	 */
	ACCEPT_LANGUAGE("Accept-Language"),
	/**
	 * 指定客户端浏览器可以支持的web服务器返回内容压缩编码类型
	 */
	ACCEPT_ENCODING("Accept-Encoding"),
	/**
	 * 浏览器可以接受的字符编码集
	 */
	ACCEPT_CHARSET("Accept-Charset"),
	/**
	 * HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器
	 */
	COOKIE("Cookie"),
	/**
	 * 请求的内容长度
	 */
	CONTENT_LENGTH("Content-Length"),

	//------------------------------------------------------------- 响应头域
	/**
	 * 提供WWW验证响应头
	 */
	WWW_AUTHENTICATE("WWW-Authenticate"),
	/**
	 * Cookie
	 */
	SET_COOKIE("Set-Cookie"),
	/**
	 * Content-Encoding
	 */
	CONTENT_ENCODING("Content-Encoding"),
	/**
	 * Content-Disposition
	 */
	CONTENT_DISPOSITION("Content-Disposition"),
	/**
	 * ETag
	 */
	ETAG("ETag"),
	/**
	 * 重定向指示到的URL
	 */
	LOCATION("Location");

	private final String value;

	Header(String value) {
		this.value = value;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public String getValue(){
		return this.value;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
