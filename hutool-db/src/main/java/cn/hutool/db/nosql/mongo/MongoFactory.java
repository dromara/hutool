package cn.hutool.db.nosql.mongo;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.setting.Setting;

/**
 * MongoDB工厂类，用于创建
 * @author looly
 *
 */
public class MongoFactory {
	
	/** 各分组做组合key的时候分隔符 */
	private final static String GROUP_SEPRATER = ",";
	
	/** 数据源池 */
	private static Map<String, MongoDS> dsMap = new ConcurrentHashMap<>();

	// JVM关闭前关闭MongoDB连接
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				MongoFactory.closeAll();
			}
		});
	}

	// ------------------------------------------------------------------------ Get DS start
	/**
	 * 获取MongoDB数据源<br>
	 * 
	 * @param host 主机
	 * @param port 端口
	 * @return MongoDB连接
	 */
	public static MongoDS getDS(String host, int port) {
		final String key = host + ":" + port;
		MongoDS ds = dsMap.get(key);
		if (null == ds) {
			// 没有在池中加入之
			ds = new MongoDS(host, port);
			dsMap.put(key, ds);
		}

		return ds;
	}

	/**
	 * 获取MongoDB数据源<br>
	 * 多个分组名对应的连接组成集群
	 * 
	 * @param groups 分组列表
	 * @return MongoDB连接
	 */
	public static MongoDS getDS(String... groups) {
		final String key = ArrayUtil.join(groups, GROUP_SEPRATER);
		MongoDS ds = dsMap.get(key);
		if (null == ds) {
			// 没有在池中加入之
			ds = new MongoDS(groups);
			dsMap.put(key, ds);
		}

		return ds;
	}

	/**
	 * 获取MongoDB数据源<br>
	 * 
	 * @param groups 分组列表
	 * @return MongoDB连接
	 */
	public static MongoDS getDS(Collection<String> groups) {
		return getDS(groups.toArray(new String[groups.size()]));
	}

	/**
	 * 获取MongoDB数据源<br>
	 * 
	 * @param setting 设定文件
	 * @param groups 分组列表
	 * @return MongoDB连接
	 */
	public static MongoDS getDS(Setting setting, String... groups) {
		final String key = setting.getSettingPath() + GROUP_SEPRATER + ArrayUtil.join(groups, GROUP_SEPRATER);
		MongoDS ds = dsMap.get(key);
		if (null == ds) {
			// 没有在池中加入之
			ds = new MongoDS(setting, groups);
			dsMap.put(key, ds);
		}

		return ds;
	}

	/**
	 * 获取MongoDB数据源<br>
	 * 
	 * @param setting 配置文件
	 * @param groups 分组列表
	 * @return MongoDB连接
	 */
	public static MongoDS getDS(Setting setting, Collection<String> groups) {
		return getDS(setting, groups.toArray(new String[groups.size()]));
	}
	// ------------------------------------------------------------------------ Get DS ends
	
	/**
	 * 关闭全部连接
	 */
	public static void closeAll() {
		if(CollectionUtil.isNotEmpty(dsMap)){
			for(MongoDS ds : dsMap.values()) {
				ds.close();
			}
			dsMap.clear();
		}
	}
}
