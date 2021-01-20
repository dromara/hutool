package cn.hutool.extra.ssh;

/**
 * 连接者对象，提供一些连接的基本信息
 * 
 * @author Luxiaolei
 *
 */
public class Connector {
	private String host;
	private int port;
	private String user;
	private String password;
	private String group;

	// ----------------------------------------------------------------------- 构造 start
	public Connector() {
	}

	/**
	 * 构造
	 * 
	 * @param user 用户名
	 * @param password 密码
	 * @param group 组
	 */
	public Connector(String user, String password, String group) {
		this.user = user;
		this.password = password;
		this.group = group;
	}

	/**
	 * 构造
	 * 
	 * @param host 主机名
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 */
	public Connector(String host, int port, String user, String password) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	// ----------------------------------------------------------------------- 构造 end

	/**
	 * 获得主机名
	 * 
	 * @return 主机名
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 设定主机名
	 * 
	 * @param host 主机名
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 获得端口号
	 * 
	 * @return 端口号
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设定端口号
	 * 
	 * @param port 端口号
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 获得用户名
	 * 
	 * @return 用户名
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 设定用户名
	 * 
	 * @param name 用户名
	 */
	public void setUser(String name) {
		this.user = name;
	}

	/**
	 * 获得密码
	 * 
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设定密码
	 * 
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获得用户组名
	 * 
	 * @return 用户组
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * 设定用户组名
	 * 
	 * @param group 用户组
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * toString方法仅用于测试显示
	 */
	@Override
	public String toString() {
		return "Connector [host=" + host + ", port=" + port + ", user=" + user + ", password=" + password + "]";
	}
}
