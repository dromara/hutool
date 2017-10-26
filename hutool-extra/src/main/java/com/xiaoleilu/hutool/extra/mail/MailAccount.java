package com.xiaoleilu.hutool.extra.mail;

import java.io.Serializable;
import java.util.Properties;

import com.xiaoleilu.hutool.setting.Setting;

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

	public static final String MAIL_SETTING_PATH = "config/mailAccount.setting";

	private String host;
	private String port = "25";
	private boolean auth;
	private String user;
	private String pass;
	private String from;

	//-------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public MailAccount() {
	}

	/**
	 * 构造
	 * @param settingPath 配置文件路径
	 */
	public MailAccount(String settingPath) {
		this(new Setting(settingPath));
	}

	/**
	 * 构造
	 * @param setting 配置文件
	 */
	public MailAccount(Setting setting) {
		setting.toBean(this);
	}
	//-------------------------------------------------------------- Constructor end

	public String getHost() {
		return host;
	}

	public MailAccount setHost(String host) {
		this.host = host;
		return this;
	}

	public String getPort() {
		return port;
	}

	public MailAccount setPort(String port) {
		this.port = port;
		return this;
	}

	public boolean isAuth() {
		return auth;
	}

	public MailAccount setAuth(boolean isAuth) {
		this.auth = isAuth;
		return this;
	}

	public String getUser() {
		return user;
	}

	public MailAccount setUser(String user) {
		this.user = user;
		return this;
	}

	public String getPass() {
		return pass;
	}

	public MailAccount setPass(String pass) {
		this.pass = pass;
		return this;
	}

	public String getFrom() {
		return from;
	}

	public MailAccount setFrom(String from) {
		this.from = from;
		return this;
	}

	@Override
	public String toString() {
		return "MailAccount [host=" + host + ", port=" + port + ", isAuth=" + auth + ", user=" + user + ", pass=******, from=" + from + "]";
	}
	
	/**
	 * 获得SMTP相关信息
	 * @return {@link Properties}
	 */
	public Properties getSmtpProps() {
		final Properties p = new Properties();
		p.put(SMTP_HOST, this.host);
		p.put(SMTP_PORT, this.port);
		p.put(SMTP_AUTH, this.auth);
		
		return p;
	}
}
