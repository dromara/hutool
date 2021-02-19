package cn.hutool.db.ds;

import cn.hutool.log.StaticLog;

/**
 * 全局的数据源工厂<br>
 * 一般情况下，一个应用默认只使用一种数据库连接池，因此维护一个全局的数据源工厂类减少判断连接池类型造成的性能浪费
 *
 * @author looly
 * @since 4.0.2
 */
public class GlobalDSFactory {

	private static volatile DSFactory factory;
	private static final Object lock = new Object();

	/*
	 * 设置在JVM关闭时关闭所有数据库连接
	 */
	static {
		// JVM关闭时关闭所有连接池
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (null != factory) {
					factory.destroy();
					StaticLog.debug("DataSource: [{}] destroyed.", factory.dataSourceName);
					factory = null;
				}
			}
		});
	}

	/**
	 * 获取默认的数据源工厂，读取默认数据库配置文件<br>
	 * 此处使用懒加载模式，在第一次调用此方法时才创建默认数据源工厂<br>
	 * 如果想自定义全局的数据源工厂，请在第一次调用此方法前调用{@link #set(DSFactory)} 方法自行定义
	 *
	 * @return 当前使用的数据源工厂
	 */
	public static DSFactory get() {
		if (null == factory) {
			synchronized (lock) {
				if (null == factory) {
					factory = DSFactory.create(null);
				}
			}
		}
		return factory;
	}

	/**
	 * 设置全局的数据源工厂<br>
	 * 在项目中存在多个连接池库的情况下，我们希望使用低优先级的库时使用此方法自定义之<br>
	 * 重新定义全局的数据源工厂此方法可在以下两种情况下调用：
	 *
	 * <pre>
	 * 1. 在get方法调用前调用此方法来自定义全局的数据源工厂
	 * 2. 替换已存在的全局数据源工厂，当已存在时会自动关闭
	 * </pre>
	 *
	 * @param customDSFactory 自定义数据源工厂
	 * @return 自定义的数据源工厂
	 */
	public static DSFactory set(DSFactory customDSFactory) {
		synchronized (lock) {
			if (null != factory) {
				if (factory.equals(customDSFactory)) {
					return factory;// 数据源工厂不变时返回原数据源工厂
				}
				// 自定义数据源工厂前关闭之前的数据源
				factory.destroy();
			}

			StaticLog.debug("Custom use [{}] DataSource.", customDSFactory.dataSourceName);
			factory = customDSFactory;
		}
		return factory;
	}
}
