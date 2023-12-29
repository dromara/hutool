/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.ftp;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.net.NetUtil;
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
 * 基于 Apache FtpServer（http://apache.apache.org/ftpserver-project/）的FTP服务端简单封装。
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
	public static SimpleFtpServer of() {
		return new SimpleFtpServer();
	}

	private final FtpServerFactory serverFactory;
	private final ListenerFactory listenerFactory;

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
	public SimpleFtpServer setConnectionConfig(final ConnectionConfig connectionConfig) {
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
	public SimpleFtpServer setPort(final int port) {
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
	public SimpleFtpServer addUser(final User user) {
		try {
			getUserManager().save(user);
		} catch (final org.apache.ftpserver.ftplet.FtpException e) {
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
	public SimpleFtpServer addAnonymous(final String homePath) {
		final BaseUser user = new BaseUser();
		user.setName("anonymous");
		user.setHomeDirectory(homePath);
		final List<Authority> authorities = new ArrayList<>();
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
	public SimpleFtpServer delUser(final String userName) {
		try {
			getUserManager().delete(userName);
		} catch (final org.apache.ftpserver.ftplet.FtpException e) {
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
	public SimpleFtpServer setSsl(final SslConfiguration ssl) {
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
	public SimpleFtpServer setSsl(final File keystoreFile, final String password) {
		final SslConfigurationFactory sslFactory = new SslConfigurationFactory();
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
	public SimpleFtpServer setUserManager(final UserManager userManager) {
		this.serverFactory.setUserManager(userManager);
		return this;
	}

	/**
	 * 自定义用户信息配置文件，此方法会重置用户管理器
	 *
	 * @param propertiesFile 配置文件
	 * @return this
	 */
	public SimpleFtpServer setUsersConfig(final File propertiesFile) {
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
	public SimpleFtpServer addFtplet(final String name, final Ftplet ftplet) {
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
		} catch (final org.apache.ftpserver.ftplet.FtpException e) {
			throw new FtpException(e);
		}
	}
}
