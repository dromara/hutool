package cn.hutool.db.nosql.redis;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.Closeable;

/**
 * Jedis数据源
 * 
 * @author looly
 * @since 3.2.3
 */
public class RedisDS implements Closeable{
	/** 默认配置文件 */
	public final static String REDIS_CONFIG_PATH = "config/redis.setting";

	/** 配置文件 */
	private Setting setting;
	/** Jedis连接池 */
	private JedisPool pool;

	// --------------------------------------------------------------------------------- Static method start
	/**
	 * 创建RedisDS，使用默认配置文件，默认分组
	 * 
	 * @return {@link RedisDS}
	 */
	public static RedisDS create() {
		return new RedisDS();
	}

	/**
	 * 创建RedisDS，使用默认配置文件
	 * 
	 * @param group 配置文件中配置分组
	 * @return {@link RedisDS}
	 */
	public static RedisDS create(String group) {
		return new RedisDS(group);
	}

	/**
	 * 创建RedisDS
	 * 
	 * @param setting 配置文件
	 * @param group 配置文件中配置分组
	 * @return {@link RedisDS}
	 */
	public static RedisDS create(Setting setting, String group) {
		return new RedisDS(setting, group);
	}
	// --------------------------------------------------------------------------------- Static method end

	/**
	 * 构造，使用默认配置文件，默认分组
	 */
	public RedisDS() {
		this(null, null);
	}

	/**
	 * 构造，使用默认配置文件
	 * 
	 * @param group 配置文件中配置分组
	 */
	public RedisDS(String group) {
		this(null, group);
	}

	/**
	 * 构造
	 * 
	 * @param setting 配置文件
	 * @param group 配置文件中配置分组
	 */
	public RedisDS(Setting setting, String group) {
		this.setting = setting;
		init(group);
	}

	/**
	 * 初始化Jedis客户端
	 * 
	 * @param group Redis服务器信息分组
	 * @return this
	 */
	public RedisDS init(String group) {
		if (null == setting) {
			setting = new Setting(REDIS_CONFIG_PATH, true);
		}

		final JedisPoolConfig config = new JedisPoolConfig();
		// 共用配置
		setting.toBean(config);
		if (StrUtil.isNotBlank(group)) {
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
	 * 
	 * @return {@link Jedis}
	 */
	public Jedis getJedis() {
		return this.pool.getResource();
	}

	/**
	 * 从Redis中获取值
	 * 
	 * @param key 键
	 * @return 值
	 */
	public String getStr(String key) {
		try (Jedis jedis = getJedis()) {
			return jedis.get(key);
		}
	}

	/**
	 * 从Redis中获取值
	 * 
	 * @param key 键
	 * @param value 值
	 * @return 状态码
	 */
	public String setStr(String key, String value) {
		try (Jedis jedis = getJedis()) {
			return jedis.set(key, value);
		}
	}
	
	/**
	 * 从Redis中删除多个值
	 * 
	 * @param keys 需要删除值对应的键列表
	 * @return 删除个数，0表示无key可删除
	 */
	public Long del(String... keys) {
		try (Jedis jedis = getJedis()) {
			return jedis.del(keys);
		}
	}

	@Override
	public void close() {
		IoUtil.close(pool);
	}
}
