package cn.hutool.extra.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.LocalPortGenerater;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * JSch是Java Secure Channel的缩写。JSch是一个SSH2的纯Java实现。它允许你连接到一个SSH服务器，并且可以使用端口转发，X11转发，文件传输等。<br>
 * 
 * @author Looly
 * @since 4.0.0
 */
public class JschUtil {

	/** 不使用SSH的值 */
	public final static String SSH_NONE = "none";

	/** 本地端口生成器 */
	private static final LocalPortGenerater portGenerater = new LocalPortGenerater(10000);

	/**
	 * 生成一个本地端口，用于远程端口映射
	 * 
	 * @return 未被使用的本地端口
	 */
	public static int generateLocalPort() {
		return portGenerater.generate();
	}
	
	/**
	 * 获得一个SSH跳板机会话，重用已经使用的会话
	 * 
	 * @param sshHost 跳板机主机
	 * @param sshPort 跳板机端口
	 * @param sshUser 跳板机用户名
	 * @param sshPass 跳板机密码
	 * @return SSH会话
	 */
	public static Session getSession(String sshHost, int sshPort, String sshUser, String sshPass) {
		return JschSessionPool.INSTANCE.getSession(sshHost, sshPort, sshUser, sshPass);
	}

	/**
	 * 打开一个新的SSH跳板机会话
	 * 
	 * @param sshHost 跳板机主机
	 * @param sshPort 跳板机端口
	 * @param sshUser 跳板机用户名
	 * @param sshPass 跳板机密码
	 * @return SSH会话
	 */
	public static Session openSession(String sshHost, int sshPort, String sshUser, String sshPass) {
		if (StrUtil.isEmpty(sshHost) || sshPort < 0 || StrUtil.isEmpty(sshUser) || StrUtil.isEmpty(sshPass)) {
			return null;
		}

		Session session;
		try {
			session = new JSch().getSession(sshUser, sshHost, sshPort);
			session.setPassword(sshPass);
			//设置第一次登陆的时候提示，可选值：(ask | yes | no) 
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return session;
	}

	/**
	 * 绑定端口到本地。 一个会话可绑定多个端口
	 * 
	 * @param session 需要绑定端口的SSH会话
	 * @param remoteHost 远程主机
	 * @param remotePort 远程端口
	 * @param localPort 本地端口
	 * @return 成功与否
	 * @throws JschRuntimeException 端口绑定失败异常
	 */
	public static boolean bindPort(Session session, String remoteHost, int remotePort, int localPort) throws JschRuntimeException {
		if (session != null && session.isConnected()) {
			try {
				session.setPortForwardingL(localPort, remoteHost, remotePort);
			} catch (JSchException e) {
				throw new JschRuntimeException(e, "From [{}] mapping to [{}] error！", remoteHost, localPort);
			}
			return true;
		}
		return false;
	}

	/**
	 * 解除端口映射
	 * 
	 * @param session 需要解除端口映射的SSH会话
	 * @param localPort 需要解除的本地端口
	 * @return 解除成功与否
	 */
	public static boolean unBindPort(Session session, int localPort) {
		try {
			session.delPortForwardingL(localPort);
			return true;
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
	}

	/**
	 * 打开SSH会话，并绑定远程端口到本地的一个随机端口
	 * 
	 * @param sshConn SSH连接信息对象
	 * @param remoteHost 远程主机
	 * @param remotePort 远程端口
	 * @return 映射后的本地端口
	 * @throws JschRuntimeException 连接异常
	 */
	public static int openAndBindPortToLocal(Connector sshConn, String remoteHost, int remotePort) throws JschRuntimeException {
		final Session session = openSession(sshConn.getHost(), sshConn.getPort(), sshConn.getUser(), sshConn.getPassword());
		if (session == null) {
			throw new JschRuntimeException("Error to create SSH Session！");
		}
		final int localPort = generateLocalPort();
		bindPort(session, remoteHost, remotePort, localPort);
		return localPort;
	}

	/**
	 * 打开SFTP连接
	 * 
	 * @param session Session会话
	 * @return {@link ChannelSftp}
	 * @since 4.0.3
	 */
	public static ChannelSftp openSftp(Session session) {
		Channel channel;
		try {
			channel = session.openChannel("sftp");
			channel.connect();
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return (ChannelSftp) channel;
	}

	/**
	 * 创建Sftp
	 * 
	 * @param sshHost 远程主机
	 * @param sshPort 远程主机端口
	 * @param sshUser 远程主机用户名
	 * @param sshPass 远程主机密码
	 * @return {@link Sftp}
	 * @since 4.0.3
	 */
	public static Sftp createSftp(String sshHost, int sshPort, String sshUser, String sshPass) {
		return new Sftp(sshHost, sshPort, sshUser, sshPass);
	}

	/**
	 * 创建Sftp
	 * 
	 * @param session SSH会话
	 * @return {@link Sftp}
	 * @since 4.0.5
	 */
	public static Sftp createSftp(Session session) {
		return new Sftp(session);
	}

	/**
	 * 打开Shell连接
	 * 
	 * @param session Session会话
	 * @return {@link ChannelShell}
	 * @since 4.0.3
	 */
	public static ChannelShell openShell(Session session) {
		Channel channel;
		try {
			channel = session.openChannel("shell");
			channel.connect();
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return (ChannelShell) channel;
	}

	/**
	 * 打开Exec连接<br>
	 * 获取ChannelExec后首先
	 * 
	 * @param session Session会话
	 * @param cmd 命令
	 * @param charset 发送和读取内容的编码
	 * @return {@link ChannelExec}
	 * @since 4.0.3
	 */
	public static String exec(Session session, String cmd, Charset charset) {
		if (null == charset) {
			charset = CharsetUtil.CHARSET_UTF_8;
		}
		ChannelExec channel;
		try {
			channel = (ChannelExec) session.openChannel("exec");
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}

		channel.setCommand(StrUtil.bytes(cmd, charset));
		channel.setInputStream(null);
		channel.setErrStream(System.err);
		InputStream in = null;
		try {
			channel.connect();// 执行命令 等待执行结束
			in = channel.getInputStream();
			return IoUtil.read(in, CharsetUtil.CHARSET_UTF_8);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		} finally {
			IoUtil.close(in);
			close(channel);
		}
	}

	/**
	 * 关闭SSH连接会话
	 * 
	 * @param session SSH会话
	 */
	public static void close(Session session) {
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
		JschSessionPool.INSTANCE.remove(session);
	}

	/**
	 * 关闭会话通道
	 * 
	 * @param channel 会话通道
	 * @since 4.0.3
	 */
	public static void close(Channel channel) {
		if (channel != null && channel.isConnected()) {
			channel.disconnect();
		}
	}

	/**
	 * 关闭SSH连接会话
	 * 
	 * @param key 主机，格式为user@host:port
	 */
	public static void close(String key) {
		JschSessionPool.INSTANCE.close(key);
	}

	/**
	 * 关闭所有SSH连接会话
	 */
	public static void closeAll() {
		JschSessionPool.INSTANCE.closeAll();
	}

}
