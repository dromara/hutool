package com.xiaoleilu.hutool.extra.mail;

import java.io.Serializable;
import java.util.Properties;

import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 邮件账户对象
 * 
 * @author Luxiaolei
 *
 */
public class MailAccount implements Serializable {
	private static final long serialVersionUID = -6937313421815719204L;

	private static final String SMTP_HOST = "mail.smtp.host";
	private static final String SMTP_PORT = "mail.smtp.port";
	private static final String SMTP_AUTH = "mail.smtp.auth";
	
	private static final String STARTTTLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String SOCKEY_FACTORY = "mail.smtp.socketFactory.class";
	private static final String SOCKEY_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
	private static final String SOCKEY_FACTORY_PORT = "smtp.socketFactory.port";
	

	public static final String MAIL_SETTING_PATH = "config/mailAccount.setting";

	/** SMTP服务器域名 */
	private String host;
	/** SMTP服务端口 */
	private Integer port;
	/** 是否需要用户名密码验证 */
	private Boolean auth;
	/** 用户名 */
	private String user;
	/** 密码 */
	private String pass;
	/** 发送方，遵循RFC-822标准 */
	private String from;
	
	//SSL
	/** 使用 STARTTLS安全连接 */
	private boolean startttlsEnable = false;
	/** 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字 */
	private String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";
	/** 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true */
	private boolean socketFactoryFallback;
	/** 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口 */
	private int socketFactoryPort = 465;

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
	 * @return SMTP服务器域名
	 */
	public String getHost() {
		return host;
	}
	/**
	 * 设置SMTP服务器域名
	 * @param host SMTP服务器域名
	 * @return this
	 */
	public MailAccount setHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * 获得SMTP服务端口
	 * @return SMTP服务端口
	 */
	public Integer getPort() {
		return port;
	}
	/**
	 * 设置SMTP服务端口
	 * @param port SMTP服务端口
	 * @return this
	 */
	public MailAccount setPort(Integer port) {
		this.port = port;
		return this;
	}

	/**
	 * 是否需要用户名密码验证
	 * @return 是否需要用户名密码验证
	 */
	public Boolean isAuth() {
		return auth;
	}
	/**
	 * 设置是否需要用户名密码验证
	 * @param isAuth 是否需要用户名密码验证
	 * @return this
	 */
	public MailAccount setAuth(boolean isAuth) {
		this.auth = isAuth;
		return this;
	}

	/**
	 * 获取用户名
	 * @return 用户名
	 */
	public String getUser() {
		return user;
	}
	/**
	 * 设置用户名
	 * @param user 用户名
	 * @return this
	 */
	public MailAccount setUser(String user) {
		this.user = user;
		return this;
	}

	/**
	 * 获取密码
	 * @return 密码
	 */
	public String getPass() {
		return pass;
	}
	/**
	 * 设置密码
	 * @param pass 密码
	 * @return this
	 */
	public MailAccount setPass(String pass) {
		this.pass = pass;
		return this;
	}

	/**
	 * 获取发送方，遵循RFC-822标准
	 * @return 发送方，遵循RFC-822标准
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * 设置发送方，遵循RFC-822标准<br>
	 * 发件人可以是以下形式：
	 * <pre>
	 * 1. user@xxx.xx
	 * 2.  name <user@xxx.xx>
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
	 * 是否使用 STARTTLS安全连接
	 * @return 是否使用 STARTTLS安全连接
	 */
	public boolean isStartttlsEnable() {
		return startttlsEnable;
	}
	/**
	 * 设置是否使用STARTTLS安全连接
	 * @param startttlsEnable 是否使用STARTTLS安全连接
	 * @return this
	 */
	public MailAccount setStartttlsEnable(boolean startttlsEnable) {
		this.startttlsEnable = startttlsEnable;
		return this;
	}

	/**
	 * 获取指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
	 * @return 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
	 */
	public String getSocketFactoryClass() {
		return socketFactoryClass;
	}
	/**
	 * 设置指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
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
	 * @return 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 */
	public boolean isSocketFactoryFallback() {
		return socketFactoryFallback;
	}
	/**
	 * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 * @param socketFactoryFallback 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 * @return this
	 */
	public MailAccount setSocketFactoryFallback(boolean socketFactoryFallback) {
		this.socketFactoryFallback = socketFactoryFallback;
		return this;
	}

	/**
	 * 获取指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 * @return 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 */
	public int getSocketFactoryPort() {
		return socketFactoryPort;
	}
	/**
	 * 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 * @param socketFactoryPort 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
	 * @return this
	 */
	public MailAccount setSocketFactoryPort(int socketFactoryPort) {
		this.socketFactoryPort = socketFactoryPort;
		return this;
	}

	/**
	 * 获得SMTP相关信息
	 * 
	 * @return {@link Properties}
	 */
	public Properties getSmtpProps() {
		final Properties p = new Properties();
		p.put(SMTP_HOST, this.host);
		p.put(SMTP_PORT, this.port);
		p.put(SMTP_AUTH, this.auth);

		// SSL
		if(startttlsEnable) {
			p.put(STARTTTLS_ENABLE, startttlsEnable);
			p.put(SOCKEY_FACTORY, socketFactoryClass);
			p.put(SOCKEY_FACTORY_FALLBACK, socketFactoryFallback);
			p.put(SOCKEY_FACTORY_PORT, socketFactoryPort);
		}
		
		return p;
	}
	
	/**
	 * 如果某些值为null，使用默认值
	 * 
	 * @return this
	 */
	public MailAccount defaultIfEmpty() {
		if(StrUtil.isBlank(this.host)) {
			//如果SMTP地址为空，默认使用smtp.<发件人邮箱后缀>
			this.host = StrUtil.format("smtp.{}", StrUtil.subSuf(this.from, this.from.indexOf('@')+1));
		}
		if(StrUtil.isBlank(user)) {
			//如果发件人为空，默认为发件人邮箱前缀
			this.user = StrUtil.subPre(this.from, this.from.indexOf('@'));
		}
		if(null == this.auth) {
			//如果密码非空白，则使用认证模式
			this.auth = (false == StrUtil.isBlank(this.pass));
		}
		if(null == this.port) {
			//端口在SSL状态下默认与socketFactoryPort一致，非SSL状态下默认为25
			this.port = this.startttlsEnable ? this.socketFactoryPort : 25;
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		return "MailAccount [host=" + host + ", port=" + port + ", auth=" + auth + ", user=" + user + ", pass=" + (StrUtil.isEmpty(this.pass) ? "" : "******") + ", from=" + from + ", startttlsEnable=" + startttlsEnable
				+ ", socketFactoryClass=" + socketFactoryClass + ", socketFactoryFallback=" + socketFactoryFallback + ", socketFactoryPort=" + socketFactoryPort + "]";
	}
}
