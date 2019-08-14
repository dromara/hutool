package cn.hutool.extra.ssh;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.jcraft.jsch.Session;

import cn.hutool.core.util.StrUtil;

/**
 * Jsch会话池
 * 
 * @author looly
 *
 */
public enum JschSessionPool {
	INSTANCE;

	/** SSH会话池，key：host，value：Session对象 */
	private Map<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();
	/** 锁 */
	private static final Object lock = new Object();

	/**
	 * 获取Session，不存在返回null
	 * 
	 * @param key 键
	 * @return Session
	 */
	public Session get(String key) {
		return sessionPool.get(key);
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
	public Session getSession(String sshHost, int sshPort, String sshUser, String sshPass) {
		final String key = StrUtil.format("{}@{}:{}", sshUser, sshHost, sshPort);
		Session session = get(key);
		if (null == session || false == session.isConnected()) {
			synchronized (lock) {
				session = get(key);
				if (null == session || false == session.isConnected()) {
					session = JschUtil.openSession(sshHost, sshPort, sshUser, sshPass);
					put(key, session);
				}
			}
		}
		return session;
	}

	/**
	 * 加入Session
	 * 
	 * @param key 键
	 * @param session Session
	 */
	public void put(String key, Session session) {
		this.sessionPool.put(key, session);
	}

	/**
	 * 关闭SSH连接会话
	 * 
	 * @param key 主机，格式为user@host:port
	 */
	public void close(String key) {
		Session session = sessionPool.get(key);
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
		sessionPool.remove(key);
	}
	
	/**
	 * 移除指定Session
	 * 
	 * @param session Session会话
	 * @since 4.1.15
	 */
	public void remove(Session session) {
		if(null != session) {
			final Iterator<Entry<String, Session>> iterator = this.sessionPool.entrySet().iterator();
			Entry<String, Session> entry;
			while(iterator.hasNext()) {
				entry = iterator.next();
				if(session.equals(entry.getValue())) {
					iterator.remove();
					break;
				}
			}
		}
	}

	/**
	 * 关闭所有SSH连接会话
	 */
	public void closeAll() {
		Collection<Session> sessions = sessionPool.values();
		for (Session session : sessions) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
		sessionPool.clear();
	}
}
