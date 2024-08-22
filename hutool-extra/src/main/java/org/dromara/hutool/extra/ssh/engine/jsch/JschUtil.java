/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.extra.ssh.engine.jsch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.SshException;

/**
 * Jsch工具类<br>
 * Jsch是Java Secure Channel的缩写。JSch是一个SSH2的纯Java实现。<br>
 * 它允许你连接到一个SSH服务器，并且可以使用端口转发，X11转发，文件传输等。<br>
 *
 * @author Looly
 * @since 4.0.0
 */
public class JschUtil {

	/**
	 * 打开Session会话
	 * @param connector 连接信息
	 * @return {@link JschSession}
	 */
	public static Session openSession(final Connector connector){
		final JSch jsch = new JSch();
		final com.jcraft.jsch.Session session;
		try {
			session = jsch.getSession(connector.getUser(), connector.getHost(), connector.getPort());
			session.setTimeout((int) connector.getTimeout());
		} catch (final JSchException e) {
			throw new SshException(e);
		}

		session.setPassword(connector.getPassword());
		// 设置第一次登录的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");

		// 设置登录认证方式，跳过Kerberos身份验证
		session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");

		return session;
	}

	/**
	 * 打开Channel连接
	 *
	 * @param session     Session会话
	 * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
	 * @param timeout     连接超时时长，单位毫秒
	 * @return {@link Channel}
	 * @since 5.3.3
	 */
	public static Channel openChannel(final Session session, final ChannelType channelType, final long timeout) {
		final Channel channel = createChannel(session, channelType, timeout);
		try {
			channel.connect((int) Math.max(timeout, 0));
		} catch (final JSchException e) {
			throw new SshException(e);
		}
		return channel;
	}

	/**
	 * 创建Channel连接
	 *
	 * @param session     Session会话
	 * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
	 * @param timeout session超时时常，单位：毫秒
	 * @return {@link Channel}
	 * @since 4.5.2
	 */
	public static Channel createChannel(final Session session, final ChannelType channelType, final long timeout) {
		final Channel channel;
		try {
			if (false == session.isConnected()) {
				session.connect((int) timeout);
			}
			channel = session.openChannel(channelType.getValue());
		} catch (final JSchException e) {
			throw new SshException(e);
		}
		return channel;
	}

	/**
	 * 关闭SSH连接会话
	 *
	 * @param session SSH会话
	 */
	public static void close(final Session session) {
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	/**
	 * 关闭会话通道
	 *
	 * @param channel 会话通道
	 * @since 4.0.3
	 */
	public static void close(final Channel channel) {
		if (channel != null && channel.isConnected()) {
			channel.disconnect();
		}
	}
}
