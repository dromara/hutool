package com.xiaoleilu.hutool.db.ds;

import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.StrUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * Jedis数据源
 * 
 * @author looly
 *
 */
public class RedisDs {
	/** 默认配置文件 */
	public final static String REDIS_CONFIG_PATH = "config/redis.setting";

	private Setting setting;
	private JedisPool pool;

	/**
	 * 初始化Jedis客户端
	 * @param group Redis服务器信息分组
	 * @return this
	 */
	synchronized public RedisDs init(String group) {
		if (null == setting) {
			setting = new Setting(REDIS_CONFIG_PATH, true);
		}

		final JedisPoolConfig config = new JedisPoolConfig();
		// 共用配置
		setting.toBean(config);
		if(StrUtil.isNotBlank(group)) {
			// 特有配置
			setting.toBean(group, config);
		}

		this.pool = new JedisPool(config,
				// 地址
				setting.getStr("host", group, Protocol.DEFAULT_HOST),
				// 端口
				setting.getInt("port", group, Protocol.DEFAULT_PORT),
				// 连接超时
				setting.getInt("connectionTimeout", group, setting.getInt("timeout", group, Protocol.DEFAULT_TIMEOUT)),
				// 读取数据超时
				setting.getInt("soTimeout", group, setting.getInt("timeout", group, Protocol.DEFAULT_TIMEOUT)),
				// 密码
				setting.getStr("password", group, null),
				// 数据库序号
				setting.getInt("database", group, Protocol.DEFAULT_DATABASE),
				// 客户端名
				setting.getStr("clientName", group, "Hutool"),
				// 是否使用SSL
				setting.getBool("ssl", group, false),
				// SSL相关，使用默认
				null, null, null);

		return this;
	}
	
	/**
	 * 从资源池中获取{@link Jedis}
	 * @return {@link Jedis}
	 */
	public Jedis get() {
		return this.pool.getResource();
	}
}
