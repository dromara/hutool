package cn.hutool.extra.mail;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 邮件账户对象
 *
 * @author Luxiaolei
 */
public class MailAccount implements Serializable {
	private static final long serialVersionUID = -6937313421815719204L;

	private static final String MAIL_PROTOCOL = "mail.transport.protocol";
	private static final String SMTP_HOST = "mail.smtp.host";
	private static final String SMTP_PORT = "mail.smtp.port";
	private static final String SMTP_AUTH = "mail.smtp.auth";
	private static final String SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
	private static final String SMTP_TIMEOUT = "mail.smtp.timeout";

	private static final String STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String SSL_ENABLE = "mail.smtp.ssl.enable";
	private static final String SOCKET_FACTORY = "mail.smtp.socketFactory.class";
	private static final String SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
	private static final String SOCKET_FACTORY_PORT = "smtp.socketFactory.port";

	private static final String MAIL_DEBUG = "mail.debug";
	private static final String SPLIT_LONG_PARAMS = "mail.mime.splitlongparameters";

	public static final String[] MAIL_SETTING_PATHS = new String[]{"config/mail.setting", "config/mailAccount.setting", "mail.setting"};

	/**
	 * SMTP服务器域名
	 */
	private String host;
	/**
	 * SMTP服务端口
	 */
	private Integer port;
	/**
	 * 是否需要用户名密码验证
	 */
	private Boolean auth;
	/**
	 * 用户名
	 */
	private String user;
	/**
	 * 密码
	 */
	private String pass;
	/**
	 * 发送方，遵循RFC-822标准
	 */
	private String from;

	/**
	 * 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
	 */
	private boolean debug;
	/**
	 * 编码用于编码邮件正文和发送人、收件人等中文
	 */
	private Charset charset = CharsetUtil.CHARSET_UTF_8;
	/**
	 * 对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）
	 */
	private boolean splitlongparameters;

	/**
	 * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
	 */
	private boolean starttlsEnable = false;
	/**
	 * 使用 SSL安全连接
	 */
	private Boolean sslEnable;
	/**
	 * 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
	 */
	private String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";
	/**
	 * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 */
	private boolean socketFactoryFallback;
	/**
	 * 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 */
	private int socketFactoryPort = 465;

	/**
	 * SMTP超时时长，单位毫秒，缺省值不超时
	 */
	private long timeout;
	/**
	 * Socket连接超时值，单位毫秒，缺省值不超时
	 */
	private long connectionTimeout;

	// -------------------------------------------------------------- Constructor start

	/**
	 * 构造,所有参数需自行定义或保持默认值
	 */
	public MailAccount() {
	}

	/**
	 * 构造
	 *
	 * @param settingPath 配置文件路径
	 */
	public MailAccount(String settingPath) {
		this(new Setting(settingPath));
	}

	/**
	 * 构造
	 *
	 * @param setting 配置文件
	 */
	public MailAccount(Setting setting) {
		setting.toBean(this);
	}

	// -------------------------------------------------------------- Constructor end

	/**
	 * 获得SMTP服务器域名
	 *
	 * @return SMTP服务器域名
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 设置SMTP服务器域名
	 *
	 * @param host SMTP服务器域名
	 * @return this
	 */
	public MailAccount setHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * 获得SMTP服务端口
	 *
	 * @return SMTP服务端口
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * 设置SMTP服务端口
	 *
	 * @param port SMTP服务端口
	 * @return this
	 */
	public MailAccount setPort(Integer port) {
		this.port = port;
		return this;
	}

	/**
	 * 是否需要用户名密码验证
	 *
	 * @return 是否需要用户名密码验证
	 */
	public Boolean isAuth() {
		return auth;
	}

	/**
	 * 设置是否需要用户名密码验证
	 *
	 * @param isAuth 是否需要用户名密码验证
	 * @return this
	 */
	public MailAccount setAuth(boolean isAuth) {
		this.auth = isAuth;
		return this;
	}

	/**
	 * 获取用户名
	 *
	 * @return 用户名
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 设置用户名
	 *
	 * @param user 用户名
	 * @return this
	 */
	public MailAccount setUser(String user) {
		this.user = user;
		return this;
	}

	/**
	 * 获取密码
	 *
	 * @return 密码
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * 设置密码
	 *
	 * @param pass 密码
	 * @return this
	 */
	public MailAccount setPass(String pass) {
		this.pass = pass;
		return this;
	}

	/**
	 * 获取发送方，遵循RFC-822标准
	 *
	 * @return 发送方，遵循RFC-822标准
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * 设置发送方，遵循RFC-822标准<br>
	 * 发件人可以是以下形式：
	 *
	 * <pre>
	 * 1. user@xxx.xx
	 * 2.  name &lt;user@xxx.xx&gt;
	 * </pre>
	 *
	 * @param from 发送方，遵循RFC-822标准
	 * @return this
	 */
	public MailAccount setFrom(String from) {
		this.from = from;
		return this;
	}

	/**
	 * 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
	 *
	 * @return 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
	 * @since 4.0.2
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * 设置是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
	 *
	 * @param debug 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
	 * @return this
	 * @since 4.0.2
	 */
	public MailAccount setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	/**
	 * 获取字符集编码
	 *
	 * @return 编码
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置字符集编码
	 *
	 * @param charset 字符集编码
	 * @return this
	 */
	public MailAccount setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）
	 *
	 * @return 对于超长参数是否切分为多份
	 */
	public boolean isSplitlongparameters() {
		return splitlongparameters;
	}

	/**
	 * 设置对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）
	 *
	 * @param splitlongparameters 对于超长参数是否切分为多份
	 */
	public void setSplitlongparameters(boolean splitlongparameters) {
		this.splitlongparameters = splitlongparameters;
	}

	/**
	 * 是否使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
	 *
	 * @return 是否使用 STARTTLS安全连接
	 */
	public boolean isStarttlsEnable() {
		return this.starttlsEnable;
	}

	/**
	 * 设置是否使用STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
	 *
	 * @param startttlsEnable 是否使用STARTTLS安全连接
	 * @return this
	 */
	public MailAccount setStarttlsEnable(boolean startttlsEnable) {
		this.starttlsEnable = startttlsEnable;
		return this;
	}

	/**
	 * 是否使用 SSL安全连接
	 *
	 * @return 是否使用 SSL安全连接
	 */
	public Boolean isSslEnable() {
		return this.sslEnable;
	}

	/**
	 * 设置是否使用SSL安全连接
	 *
	 * @param sslEnable 是否使用SSL安全连接
	 * @return this
	 */
	public MailAccount setSslEnable(Boolean sslEnable) {
		this.sslEnable = sslEnable;
		return this;
	}

	/**
	 * 获取指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
	 *
	 * @return 指定实现javax.net.SocketFactory接口的类的名称, 这个类将被用于创建SMTP的套接字
	 */
	public String getSocketFactoryClass() {
		return socketFactoryClass;
	}

	/**
	 * 设置指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
	 *
	 * @param socketFactoryClass 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
	 * @return this
	 */
	public MailAccount setSocketFactoryClass(String socketFactoryClass) {
		this.socketFactoryClass = socketFactoryClass;
		return this;
	}

	/**
	 * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 *
	 * @return 如果设置为true, 未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 */
	public boolean isSocketFactoryFallback() {
		return socketFactoryFallback;
	}

	/**
	 * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 *
	 * @param socketFactoryFallback 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 * @return this
	 */
	public MailAccount setSocketFactoryFallback(boolean socketFactoryFallback) {
		this.socketFactoryFallback = socketFactoryFallback;
		return this;
	}

	/**
	 * 获取指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 *
	 * @return 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 */
	public int getSocketFactoryPort() {
		return socketFactoryPort;
	}

	/**
	 * 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 *
	 * @param socketFactoryPort 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 * @return this
	 */
	public MailAccount setSocketFactoryPort(int socketFactoryPort) {
		this.socketFactoryPort = socketFactoryPort;
		return this;
	}

	/**
	 * 设置SMTP超时时长，单位毫秒，缺省值不超时
	 *
	 * @param timeout SMTP超时时长，单位毫秒，缺省值不超时
	 * @return this
	 * @since 4.1.17
	 */
	public MailAccount setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * 设置Socket连接超时值，单位毫秒，缺省值不超时
	 *
	 * @param connectionTimeout Socket连接超时值，单位毫秒，缺省值不超时
	 * @return this
	 * @since 4.1.17
	 */
	public MailAccount setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	/**
	 * 获得SMTP相关信息
	 *
	 * @return {@link Properties}
	 */
	public Properties getSmtpProps() {
		//全局系统参数
		System.setProperty(SPLIT_LONG_PARAMS, String.valueOf(this.splitlongparameters));

		final Properties p = new Properties();
		p.put(MAIL_PROTOCOL, "smtp");
		p.put(SMTP_HOST, this.host);
		p.put(SMTP_PORT, String.valueOf(this.port));
		p.put(SMTP_AUTH, String.valueOf(this.auth));
		if (this.timeout > 0) {
			p.put(SMTP_TIMEOUT, String.valueOf(this.timeout));
		}
		if (this.connectionTimeout > 0) {
			p.put(SMTP_CONNECTION_TIMEOUT, String.valueOf(this.connectionTimeout));
		}

		p.put(MAIL_DEBUG, String.valueOf(this.debug));

		if (this.starttlsEnable) {
			//STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
			p.put(STARTTLS_ENABLE, "true");

			if (null == this.sslEnable) {
				//为了兼容旧版本，当用户没有此项配置时，按照starttlsEnable开启状态时对待
				this.sslEnable = true;
			}
		}

		// SSL
		if (null != this.sslEnable && this.sslEnable) {
			p.put(SSL_ENABLE, "true");
			p.put(SOCKET_FACTORY, socketFactoryClass);
			p.put(SOCKET_FACTORY_FALLBACK, String.valueOf(this.socketFactoryFallback));
			p.put(SOCKET_FACTORY_PORT, String.valueOf(this.socketFactoryPort));
		}

		return p;
	}

	/**
	 * 如果某些值为null，使用默认值
	 *
	 * @return this
	 */
	public MailAccount defaultIfEmpty() {
		// 去掉发件人的姓名部分
		final String fromAddress = InternalMailUtil.parseFirstAddress(this.from, this.charset).getAddress();

		if (StrUtil.isBlank(this.host)) {
			// 如果SMTP地址为空，默认使用smtp.<发件人邮箱后缀>
			this.host = StrUtil.format("smtp.{}", StrUtil.subSuf(fromAddress, fromAddress.indexOf('@') + 1));
		}
		if (StrUtil.isBlank(user)) {
			// 如果用户名为空，默认为发件人邮箱前缀
			this.user = StrUtil.subPre(fromAddress, fromAddress.indexOf('@'));
		}
		if (null == this.auth) {
			// 如果密码非空白，则使用认证模式
			this.auth = (false == StrUtil.isBlank(this.pass));
		}
		if (null == this.port) {
			// 端口在SSL状态下默认与socketFactoryPort一致，非SSL状态下默认为25
			this.port = (null != this.sslEnable && this.sslEnable) ? this.socketFactoryPort : 25;
		}
		if (null == this.charset) {
			// 默认UTF-8编码
			this.charset = CharsetUtil.CHARSET_UTF_8;
		}

		return this;
	}

	@Override
	public String toString() {
		return "MailAccount [host=" + host + ", port=" + port + ", auth=" + auth + ", user=" + user + ", pass=" + (StrUtil.isEmpty(this.pass) ? "" : "******") + ", from=" + from + ", startttlsEnable="
				+ starttlsEnable + ", socketFactoryClass=" + socketFactoryClass + ", socketFactoryFallback=" + socketFactoryFallback + ", socketFactoryPort=" + socketFactoryPort + "]";
	}
}
