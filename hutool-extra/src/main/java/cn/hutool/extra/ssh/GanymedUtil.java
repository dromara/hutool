package cn.hutool.extra.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Ganymed-SSH2封装，见：http://www.ganymed.ethz.ch/ssh2/
 *
 * @author looly
 * @since 5.5.3
 */
public class GanymedUtil {

	/**
	 * 连接到服务器
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @return {@link Connection}
	 */
	public static Connection connect(String sshHost, int sshPort) {
		Connection conn = new Connection(sshHost, sshPort);
		try {
			conn.connect();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return conn;
	}

	/**
	 * 打开远程会话
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名，如果为null，默认root
	 * @param sshPass 密码
	 * @return {@link Session}
	 */
	public static Session openSession(String sshHost, int sshPort, String sshUser, String sshPass) {
		// 默认root用户
		if (StrUtil.isEmpty(sshUser)) {
			sshUser = "root";
		}

		final Connection connect = connect(sshHost, sshPort);
		try {
			connect.authenticateWithPassword(sshUser, sshPass);
			return connect.openSession();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 执行Shell命令（使用EXEC方式）
	 * <p>
	 * 此方法单次发送一个命令到服务端，不读取环境变量，执行结束后自动关闭Session，不会产生阻塞。
	 * </p>
	 *
	 * @param session   Session会话
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行返回结果
	 */
	public static String exec(Session session, String cmd, Charset charset, OutputStream errStream) {
		final String result;
		try {
			session.execCommand(cmd, charset.name());
			result = IoUtil.read(new StreamGobbler(session.getStdout()), charset);

			// 错误输出
			IoUtil.copy(new StreamGobbler(session.getStdout()), errStream);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			close(session);
		}
		return result;
	}

	/**
	 * 执行Shell命令
	 * <p>
	 * 此方法单次发送一个命令到服务端，自动读取环境变量，执行结束后自动关闭Session，不会产生阻塞。
	 * </p>
	 *
	 * @param session   Session会话
	 * @param cmd       命令
	 * @param charset   发送和读取内容的编码
	 * @param errStream 错误信息输出到的位置
	 * @return 执行返回结果
	 */
	public static String execByShell(Session session, String cmd, Charset charset, OutputStream errStream) {
		final String result;
		try {
			session.requestDumbPTY();
			IoUtil.write(session.getStdin(), charset, true, cmd);

			result = IoUtil.read(new StreamGobbler(session.getStdout()), charset);
			if(null != errStream){
				// 错误输出
				IoUtil.copy(new StreamGobbler(session.getStdout()), errStream);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			close(session);
		}
		return result;
	}

	/**
	 * 关闭会话
	 *
	 * @param session 会话通道
	 */
	public static void close(Session session) {
		if (session != null) {
			session.close();
		}
	}
}
