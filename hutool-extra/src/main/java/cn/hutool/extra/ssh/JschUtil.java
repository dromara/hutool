package cn.hutool.extra.ssh;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * SSH安全连接相关工具类 此工具类用于维护一个到跳板机的通道，并将跳板机可访问的服务器端口映射到本地使用
 * 
 * @author Looly
 * @since 4.0.0
 */
public class JschUtil {

	/*--------------------------常量 start-------------------------------*/
	/** 不使用SSH的值 */
	public final static String SSH_NONE = "none";
	/*--------------------------常量 start-------------------------------*/

	/*--------------------------私有属性 start-------------------------------*/
	/** SSH会话池，key：host，value：Session对象 */
	private static Map<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();
	/** 备选的本地端口 */
	private volatile static AtomicInteger alternativePort = new AtomicInteger(10000);
	/** 锁 */
	private static final Object lock = new Object();
	/*--------------------------私有属性 start-------------------------------*/

	/**
	 * 生成一个本地端口，用于远程端口映射
	 * 
	 * @return 未被使用的本地端口
	 */
	public static int generateLocalPort() {
		int validPort = alternativePort.get();
		// 获取可用端口
		while (false == NetUtil.isUsableLocalPort(validPort)) {
			validPort = alternativePort.incrementAndGet();
		}
		return validPort;
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
		final String key = StrUtil.format("{}@{}:{}", sshUser, sshHost, sshPort);
		Session session = sessionPool.get(key);
		if(null == session) {
			synchronized (lock) {
				session = sessionPool.get(key);
				if(null == session || false == session.isConnected()) {
					session = openSession(sshHost, sshPort, sshUser, sshPass);
					sessionPool.put(key, session);
				}
			}
		}
		return session;
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
	public static boolean bindPort(Session session, String remoteHost, int remotePort, int localPort) throws JschRuntimeException{
		if (session != null && session.isConnected()) {
			try {
				session.setPortForwardingL(localPort, remoteHost, remotePort);
			} catch (JSchException e) {
				throw new JschRuntimeException("From [" + remoteHost + "] Mapping to [" + localPort + "] error！", e);
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
	 * 关闭SSH连接会话
	 * 
	 * @param session SSH会话
	 */
	public static void close(Session session) {
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	/**
	 * 关闭SSH连接会话
	 * 
	 * @param key 主机，格式为user@host:port
	 */
	public static void close(String key) {
		Session session = sessionPool.get(key);
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
		sessionPool.remove(key);
	}

	/**
	 * 关闭所有SSH连接会话
	 */
	public static void closeAll() {
		Collection<Session> sessions = sessionPool.values();
		for (Session session : sessions) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
		sessionPool.clear();
	}

}
