package cn.hutool.extra.ftp;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.NetUtil;
import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfiguration;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于 Apache FtpServer（http://mina.apache.org/ftpserver-project/）的FTP服务端简单封装。
 *
 * @author looly
 * @since 5.5.0
 */
public class SimpleFtpServer {

	/**
	 * 创建FTP服务器，调用{@link SimpleFtpServer#start()}启动即可
	 *
	 * @return SimpleFtpServer
	 */
	public static SimpleFtpServer create() {
		return new SimpleFtpServer();
	}

	FtpServerFactory serverFactory;
	ListenerFactory listenerFactory;

	/**
	 * 构造
	 */
	public SimpleFtpServer() {
		serverFactory = new FtpServerFactory();
		listenerFactory = new ListenerFactory();
	}

	/**
	 * 获取 {@link FtpServerFactory}，用于设置FTP服务器相关信息
	 *
	 * @return {@link FtpServerFactory}
	 */
	public FtpServerFactory getServerFactory() {
		return this.serverFactory;
	}

	/**
	 * 设置连接相关配置，使用ConnectionConfigFactory创建{@link ConnectionConfig}对象
	 *
	 * @param connectionConfig 连接配置
	 * @return this
	 */
	public SimpleFtpServer setConnectionConfig(ConnectionConfig connectionConfig) {
		this.serverFactory.setConnectionConfig(connectionConfig);
		return this;
	}

	/**
	 * 获取{@link ListenerFactory}，用于设置端口、用户、SSL等信息
	 *
	 * @return {@link ListenerFactory}
	 */
	public ListenerFactory getListenerFactory() {
		return this.listenerFactory;
	}

	/**
	 * 自定义默认端口，如果不设置，使用默认端口：21
	 *
	 * @param port 端口
	 * @return this
	 */
	public SimpleFtpServer setPort(int port) {
		Assert.isTrue(NetUtil.isValidPort(port), "Invalid port!");
		this.listenerFactory.setPort(port);
		return this;
	}

	/**
	 * 获取用户管理器，用于新增、查找和删除用户信息
	 *
	 * @return 用户管理器
	 */
	public UserManager getUserManager() {
		return this.serverFactory.getUserManager();
	}

	/**
	 * 增加FTP用户
	 *
	 * @param user FTP用户信息
	 * @return this
	 */
	public SimpleFtpServer addUser(User user) {
		try {
			getUserManager().save(user);
		} catch (org.apache.ftpserver.ftplet.FtpException e) {
			throw new FtpException(e);
		}
		return this;
	}

	/**
	 * 添加匿名用户
	 *
	 * @param homePath 用户路径，匿名用户对此路径有读写权限
	 * @return this
	 */
	public SimpleFtpServer addAnonymous(String homePath) {
		BaseUser user = new BaseUser();
		user.setName("anonymous");
		user.setHomeDirectory(homePath);
		List<Authority> authorities = new ArrayList<>();
		// 添加用户读写权限
		authorities.add(new WritePermission());
		user.setAuthorities(authorities);
		return addUser(user);
	}

	/**
	 * 删除用户
	 *
	 * @param userName 用户名
	 * @return this
	 */
	public SimpleFtpServer delUser(String userName) {
		try {
			getUserManager().delete(userName);
		} catch (org.apache.ftpserver.ftplet.FtpException e) {
			throw new FtpException(e);
		}
		return this;
	}

	/**
	 * 使用SSL安全连接，可以使用SslConfigurationFactory创建{@link SslConfiguration}
	 *
	 * @param ssl {@link SslConfiguration}
	 * @return this
	 */
	public SimpleFtpServer setSsl(SslConfiguration ssl) {
		this.listenerFactory.setSslConfiguration(ssl);
		listenerFactory.setImplicitSsl(true);
		return this;
	}

	/**
	 * 使用SSL安全连接
	 *
	 * @param keystoreFile 密钥文件
	 * @param password     密钥文件密码
	 * @return this
	 */
	public SimpleFtpServer setSsl(File keystoreFile, String password) {
		SslConfigurationFactory sslFactory = new SslConfigurationFactory();
		sslFactory.setKeystoreFile(keystoreFile);
		sslFactory.setKeystorePassword(password);
		return setSsl(sslFactory.createSslConfiguration());
	}

	/**
	 * 自定义用户管理器，一般用于使用配置文件配置用户信息
	 *
	 * @param userManager {@link UserManager}
	 * @return this
	 */
	public SimpleFtpServer setUserManager(UserManager userManager) {
		this.serverFactory.setUserManager(userManager);
		return this;
	}

	/**
	 * 自定义用户信息配置文件，此方法会重置用户管理器
	 *
	 * @param propertiesFile 配置文件
	 * @return this
	 */
	public SimpleFtpServer setUsersConfig(File propertiesFile) {
		final PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(propertiesFile);
		return this.setUserManager(userManagerFactory.createUserManager());
	}

	/**
	 * 增加FTP动作行为监听处理器，通过实现{@link Ftplet}，可以对用户的行为监听并执行相应动作
	 *
	 * @param name   名称
	 * @param ftplet {@link Ftplet}，用户自定义监听规则
	 * @return this
	 */
	public SimpleFtpServer addFtplet(String name, Ftplet ftplet) {
		this.serverFactory.getFtplets().put(name, ftplet);
		return this;
	}

	/**
	 * 启动FTP服务，阻塞当前线程
	 */
	public void start() {
		// 一个Listener对应一个监听端口
		// 可以创建多个监听，此处默认只监听一个
		serverFactory.addListener("default", listenerFactory.createListener());
		try {
			serverFactory.createServer().start();
		} catch (org.apache.ftpserver.ftplet.FtpException e) {
			throw new cn.hutool.extra.ftp.FtpException(e);
		}
	}
}
