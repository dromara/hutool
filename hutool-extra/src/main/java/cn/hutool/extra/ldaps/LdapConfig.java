package cn.hutool.extra.ldaps;

/**
 * ldap登录配置
 *
 * @author zhaosheng
 */
public class LdapConfig {

	/**
	 * ldap服务地址
	 */
	private String host;

	/**
	 * 端口
	 */
	private int port;

	/**
	 * 协议
	 */
	private String protocol;

	/**
	 * 域
	 */
	private String domain;

	/**
	 * 默认使用非ldaps形式
	 */
	private Boolean isLdaps = false;

	/**
	 * 证书地址
	 */
	private String keystore;

	/**
	 * 用户名
	 */
	private String user;

	/**
	 * 密码
	 */
	private String password;

	public LdapConfig() {

	}

	public LdapConfig(String host, int port, String protocol, String domain, Boolean isLdaps) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.domain = domain;
		this.isLdaps = isLdaps;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Boolean getLdaps() {
		return isLdaps;
	}

	public void setLdaps(Boolean ldaps) {
		isLdaps = ldaps;
	}

	public String getKeystore() {
		return keystore;
	}

	public void setKeystore(String keystore) {
		this.keystore = keystore;
	}

	public String getUser() {
		return user + domain;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LdapConfig{" +
				"host='" + host + '\'' +
				", port=" + port +
				", protocol='" + protocol + '\'' +
				", domain='" + domain + '\'' +
				", isLdaps=" + isLdaps +
				", keystore='" + keystore + '\'' +
				", user='" + user + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
