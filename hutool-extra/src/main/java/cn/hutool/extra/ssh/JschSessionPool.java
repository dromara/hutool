package cn.hutool.extra.ssh;

import cn.hutool.core.cache.SimpleCache;
import cn.hutool.core.text.StrUtil;
import com.jcraft.jsch.Session;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Jsch会话池
 *
 * @author looly
 */
public enum JschSessionPool {
	INSTANCE;

	/**
	 * SSH会话池，key：host，value：Session对象
	 */
	private final SimpleCache<String, Session> cache = new SimpleCache<>(new HashMap<>());

	/**
	 * 获取Session，不存在返回null
	 *
	 * @param key 键
	 * @return Session
	 */
	public Session get(final String key) {
		return cache.get(key);
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
	public Session getSession(final String sshHost, final int sshPort, final String sshUser, final String sshPass) {
		final String key = StrUtil.format("{}@{}:{}", sshUser, sshHost, sshPort);
		return this.cache.get(key, Session::isConnected, ()-> JschUtil.openSession(sshHost, sshPort, sshUser, sshPass));
	}

	/**
	 * 获得一个SSH跳板机会话，重用已经使用的会话
	 *
	 * @param sshHost    跳板机主机
	 * @param sshPort    跳板机端口
	 * @param sshUser    跳板机用户名
	 * @param prvkey     跳板机私钥路径
	 * @param passphrase 跳板机私钥密码
	 * @return SSH会话
	 */
	public Session getSession(final String sshHost, final int sshPort, final String sshUser, final String prvkey, final byte[] passphrase) {
		final String key = StrUtil.format("{}@{}:{}", sshUser, sshHost, sshPort);
		return this.cache.get(key, Session::isConnected, ()->JschUtil.openSession(sshHost, sshPort, sshUser, prvkey, passphrase));
	}

	/**
	 * 加入Session
	 *
	 * @param key     键
	 * @param session Session
	 */
	public void put(final String key, final Session session) {
		this.cache.put(key, session);
	}

	/**
	 * 关闭SSH连接会话
	 *
	 * @param key 主机，格式为user@host:port
	 */
	public void close(final String key) {
		final Session session = get(key);
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
		this.cache.remove(key);
	}

	/**
	 * 移除指定Session
	 *
	 * @param session Session会话
	 * @since 4.1.15
	 */
	public void remove(final Session session) {
		if (null != session) {
			final Iterator<Entry<String, Session>> iterator = this.cache.iterator();
			Entry<String, Session> entry;
			while (iterator.hasNext()) {
				entry = iterator.next();
				if (session.equals(entry.getValue())) {
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
		Session session;
		for (final Entry<String, Session> entry : this.cache) {
			session = entry.getValue();
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
		cache.clear();
	}
}
