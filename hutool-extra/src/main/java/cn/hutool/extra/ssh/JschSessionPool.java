package cn.hutool.extra.ssh;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jcraft.jsch.Session;

public enum JschSessionPool {
	INSTANCE;

	/** SSH会话池，key：host，value：Session对象 */
	private Map<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();

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
