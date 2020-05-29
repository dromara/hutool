package cn.hutool.extra.ssh;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.LocalPortGenerater;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

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
	 * 不使用SSH的值
	 */
	public final static String SSH_NONE = "none";

	/**
	 * 本地端口生成器
	 */
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
	 * 获得一个SSH会话，重用已经使用的会话
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @return SSH会话
	 */
	public static Session getSession(String sshHost, int sshPort, String sshUser, String sshPass) {
		return JschSessionPool.INSTANCE.getSession(sshHost, sshPort, sshUser, sshPass);
	}

	/**
	 * 获得一个SSH会话，重用已经使用的会话
	 *
	 * @param sshHost        主机
	 * @param sshPort        端口
	 * @param sshUser        用户名
	 * @param privateKeyPath 私钥路径
	 * @param passphrase     私钥密码
	 * @return SSH会话
	 */
	public static Session getSession(String sshHost, int sshPort, String sshUser, String privateKeyPath, byte[] passphrase) {
		return JschSessionPool.INSTANCE.getSession(sshHost, sshPort, sshUser, privateKeyPath, passphrase);
	}

	/**
	 * 打开一个新的SSH会话
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @return SSH会话
	 */
	public static Session openSession(String sshHost, int sshPort, String sshUser, String sshPass) {
		return openSession(sshHost, sshPort, sshUser, sshPass, 0);
	}

	/**
	 * 打开一个新的SSH会话
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @param timeout Socket连接超时时长，单位毫秒
	 * @return SSH会话
	 * @since 5.3.3
	 */
	public static Session openSession(String sshHost, int sshPort, String sshUser, String sshPass, int timeout) {
		final Session session = createSession(sshHost, sshPort, sshUser, sshPass);
		try {
			session.connect(timeout);
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return session;
	}

	/**
	 * 打开一个新的SSH会话
	 *
	 * @param sshHost        主机
	 * @param sshPort        端口
	 * @param sshUser        用户名
	 * @param privateKeyPath 私钥的路径
	 * @param passphrase     私钥文件的密码，可以为null
	 * @return SSH会话
	 */
	public static Session openSession(String sshHost, int sshPort, String sshUser, String privateKeyPath, byte[] passphrase) {
		final Session session = createSession(sshHost, sshPort, sshUser, privateKeyPath, passphrase);
		try {
			session.connect();
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return session;
	}

	/**
	 * 新建一个新的SSH会话，此方法并不打开会话（既不调用connect方法）
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名，如果为null，默认root
	 * @param sshPass 密码
	 * @return SSH会话
	 * @since 4.5.2
	 */
	public static Session createSession(String sshHost, int sshPort, String sshUser, String sshPass) {
		final JSch jsch = new JSch();
		final Session session = createSession(jsch, sshHost, sshPort, sshUser);

		if (StrUtil.isNotEmpty(sshPass)) {
			session.setPassword(sshPass);
		}

		return session;
	}

	/**
	 * 新建一个新的SSH会话，此方法并不打开会话（既不调用connect方法）
	 *
	 * @param sshHost        主机
	 * @param sshPort        端口
	 * @param sshUser        用户名，如果为null，默认root
	 * @param privateKeyPath 私钥的路径
	 * @param passphrase     私钥文件的密码，可以为null
	 * @return SSH会话
	 * @since 5.0.0
	 */
	public static Session createSession(String sshHost, int sshPort, String sshUser, String privateKeyPath, byte[] passphrase) {
		Assert.notEmpty(privateKeyPath, "PrivateKey Path must be not empty!");

		final JSch jsch = new JSch();
		try {
			jsch.addIdentity(privateKeyPath, passphrase);
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}

		return createSession(jsch, sshHost, sshPort, sshUser);
	}

	/**
	 * 创建一个SSH会话，重用已经使用的会话
	 *
	 * @param jsch    {@link JSch}
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名，如果为null，默认root
	 * @return {@link Session}
	 * @since 5.0.3
	 */
	public static Session createSession(JSch jsch, String sshHost, int sshPort, String sshUser) {
		Assert.notEmpty(sshHost, "SSH Host must be not empty!");
		Assert.isTrue(sshPort > 0, "SSH port must be > 0");

		// 默认root用户
		if (StrUtil.isEmpty(sshUser)) {
			sshUser = "root";
		}

		if (null == jsch) {
			jsch = new JSch();
		}

		Session session;
		try {
			session = jsch.getSession(sshUser, sshHost, sshPort);
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}

		// 设置第一次登录的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");

		return session;
	}

	/**
	 * 绑定端口到本地。 一个会话可绑定多个端口
	 *
	 * @param session    需要绑定端口的SSH会话
	 * @param remoteHost 远程主机
	 * @param remotePort 远程端口
	 * @param localPort  本地端口
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
	 * @param session   需要解除端口映射的SSH会话
	 * @param localPort 需要解除的本地端口
	 * @return 解除成功与否
	 */
	public static boolean unBindPort(Session session, int localPort) {
		try {
			session.delPortForwardingL(localPort);
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return true;
	}

	/**
	 * 打开SSH会话，并绑定远程端口到本地的一个随机端口
	 *
	 * @param sshConn    SSH连接信息对象
	 * @param remoteHost 远程主机
	 * @param remotePort 远程端口
	 * @return 映射后的本地端口
	 * @throws JschRuntimeException 连接异常
	 */
	public static int openAndBindPortToLocal(Connector sshConn, String remoteHost, int remotePort) throws JschRuntimeException {
		final Session session = openSession(sshConn.getHost(), sshConn.getPort(), sshConn.getUser(), sshConn.getPassword());
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
		return openSftp(session, 0);
	}

	/**
	 * 打开SFTP连接
	 *
	 * @param session Session会话
	 * @param timeout 连接超时时长，单位毫秒
	 * @return {@link ChannelSftp}
	 * @since 5.3.3
	 */
	public static ChannelSftp openSftp(Session session, int timeout) {
		return (ChannelSftp) openChannel(session, ChannelType.SFTP, timeout);
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
		return (ChannelShell) openChannel(session, ChannelType.SHELL);
	}

	/**
	 * 打开Channel连接
	 *
	 * @param session     Session会话
	 * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
	 * @return {@link Channel}
	 * @since 4.5.2
	 */
	public static Channel openChannel(Session session, ChannelType channelType) {
		return openChannel(session, channelType, 0);
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
	public static Channel openChannel(Session session, ChannelType channelType, int timeout) {
		final Channel channel = createChannel(session, channelType);
		try {
			channel.connect(Math.max(timeout, 0));
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return channel;
	}

	/**
	 * 创建Channel连接
	 *
	 * @param session     Session会话
	 * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
	 * @return {@link Channel}
	 * @since 4.5.2
	 */
	public static Channel createChannel(Session session, ChannelType channelType) {
		Channel channel;
		try {
			if (false == session.isConnected()) {
				session.connect();
			}
			channel = session.openChannel(channelType.getValue());
		} catch (JSchException e) {
			throw new JschRuntimeException(e);
		}
		return channel;
	}

	/**
	 * 执行Shell命令
	 *
	 * @param session Session会话
	 * @param cmd     命令
	 * @param charset 发送和读取内容的编码
	 * @return {@link ChannelExec}
	 * @since 4.0.3
	 */
	public static String exec(Session session, String cmd, Charset charset) {
		return exec(session, cmd, charset, System.err);
	}

	/**
	 * 执行Shell命令（使用EXEC方式）
	 * <p>
	 * 此方法单次发送一个命令到服务端，不读取环境变量，执行结束后自动关闭channel，不会产生阻塞。
	 * </p>
	 *
	 * @param session   Session会话
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return {@link ChannelExec}
	 * @since 4.3.1
	 */
	public static String exec(Session session, String cmd, Charset charset, OutputStream errStream) {
		if (null == charset) {
			charset = CharsetUtil.CHARSET_UTF_8;
		}
		final ChannelExec channel = (ChannelExec) createChannel(session, ChannelType.EXEC);
		channel.setCommand(StrUtil.bytes(cmd, charset));
		channel.setInputStream(null);
		channel.setErrStream(errStream);
		InputStream in = null;
		try {
			channel.connect();
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
	 * 执行Shell命令
	 * <p>
	 * 此方法单次发送一个命令到服务端，自动读取环境变量，执行结束后自动关闭channel，不会产生阻塞。<br>
	 * 此方法返回数据中可能
	 * </p>
	 *
	 * @param session Session会话
	 * @param cmd     命令
	 * @param charset 发送和读取内容的编码
	 * @return {@link ChannelExec}
	 * @since 5.2.5
	 */
	public static String execByShell(Session session, String cmd, Charset charset) {
		final ChannelShell shell = openShell(session);
		// 开始连接
		shell.setPty(true);
		OutputStream out = null;
		InputStream in = null;
		final StringBuilder result = StrUtil.builder();
		try {
			out = shell.getOutputStream();
			in = shell.getInputStream();

			out.write(StrUtil.bytes(cmd, charset));
			out.flush();

			while (in.available() > 0) {
				result.append(IoUtil.read(in, charset));
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(out);
			IoUtil.close(in);
			close(shell);
		}
		return result.toString();
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
