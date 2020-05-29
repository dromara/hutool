package cn.hutool.extra.ftp;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * FTP配置项，提供FTP各种参数信息
 *
 * @author looly
 */
public class FtpConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	public static FtpConfig create(){
		return new FtpConfig();
	}

	/**
	 * 主机
	 */
	private String host;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 用户名
	 */
	private String user;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 编码
	 */
	private Charset charset;

	/**
	 * 连接超时时长，单位毫秒
	 */
	private long connectionTimeout;

	/**
	 * Socket连接超时时长，单位毫秒
	 */
	private long soTimeout;

	/**
	 * 构造
	 */
	public FtpConfig() {
	}

	/**
	 * 构造
	 *
	 * @param host 主机
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 * @param charset 编码
	 */
	public FtpConfig(String host, int port, String user, String password, Charset charset) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.charset = charset;
	}

	public String getHost() {
		return host;
	}

	public FtpConfig setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public FtpConfig setPort(int port) {
		this.port = port;
		return this;
	}

	public String getUser() {
		return user;
	}

	public FtpConfig setUser(String user) {
		this.user = user;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public FtpConfig setPassword(String password) {
		this.password = password;
		return this;
	}

	public Charset getCharset() {
		return charset;
	}

	public FtpConfig setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public FtpConfig setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public long getSoTimeout() {
		return soTimeout;
	}

	public FtpConfig setSoTimeout(long soTimeout) {
		this.soTimeout = soTimeout;
		return this;
	}
}
