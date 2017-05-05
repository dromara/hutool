package com.xiaoleilu.hutool.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaoleilu.hutool.log.dialect.commons.ApacheCommonsLogFactory;
import com.xiaoleilu.hutool.log.dialect.console.ConsoleLogFactory;
import com.xiaoleilu.hutool.log.dialect.jdk.JdkLogFactory;
import com.xiaoleilu.hutool.log.dialect.log4j.Log4jLogFactory;
import com.xiaoleilu.hutool.log.dialect.log4j2.Log4j2LogFactory;
import com.xiaoleilu.hutool.log.dialect.slf4j.Slf4jLogFactory;
import com.xiaoleilu.hutool.log.dialect.tinylog.TinyLogFactory;

/**
 * 日志工厂类
 * 
 * @see Slf4jLogFactory
 * @see Log4jLogFactory
 * @see Log4j2LogFactory
 * @see ApacheCommonsLogFactory
 * @see TinyLogFactory
 * @see JdkLogFactory
 * @see ConsoleLogFactory
 * 
 * @author Looly
 *
 */
public abstract class LogFactory {

	private String logFramworkName;
	protected Map<Object, Log> logCache;

	public LogFactory(String logFramworkName) {
		this.logFramworkName = logFramworkName;
		logCache = new ConcurrentHashMap<>();
	}
	
	/**
	 * 获得日志对象
	 * 
	 * @param name 日志对象名
	 * @return 日志对象
	 */
	public Log getLog(String name){
		Log log = logCache.get(name);
		if(null == log){
			log = createLog(name);
			logCache.put(name, log);
		}
		return log;
	}

	/**
	 * 获得日志对象
	 * 
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	public Log getLog(Class<?> clazz){
		Log log = logCache.get(clazz);
		if(null == log){
			log = createLog(clazz);
			logCache.put(clazz, log);
		}
		return log;
	}

	/**
	 * 创建日志对象
	 * 
	 * @param name 日志对象名
	 * @return 日志对象
	 */
	public abstract Log createLog(String name);

	/**
	 * 创建日志对象
	 * 
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	public abstract Log createLog(Class<?> clazz);

	/**
	 * 检查日志实现是否存在<br>
	 * 此方法仅用于检查所提供的日志相关类是否存在，当传入的日志类类不存在时抛出ClassNotFoundException<br>
	 * 此方法的作用是在detectLogFactory方法自动检测所用日志时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种日志的检测。
	 * 
	 * @param logClassName 日志实现相关类
	 */
	protected void checkLogExist(Object logClassName) {
		// 不做任何操作
	}

	// ------------------------------------------------------------------------- Static start
	private static volatile LogFactory currentLogFactory;
	private static final Object lock = new Object();

	/**
	 * @return 当前使用的日志工厂
	 */
	public static LogFactory getCurrentLogFactory() {
		if (null == currentLogFactory) {
			synchronized (lock) {
				if (null == currentLogFactory) {
					currentLogFactory = detectLogFactory();
				}
			}
		}
		return currentLogFactory;
	}

	/**
	 * 自定义日志实现
	 * 
	 * @see Slf4jLogFactory
	 * @see Log4jLogFactory
	 * @see Log4j2LogFactory
	 * @see ApacheCommonsLogFactory
	 * @see JdkLogFactory
	 * @see ConsoleLogFactory
	 * 
	 * @param logFactoryClass 日志工厂类
	 * @return 自定义的日志工厂类
	 */
	public static LogFactory setCurrentLogFactory(Class<? extends LogFactory> logFactoryClass) {
		try {
			return setCurrentLogFactory(logFactoryClass.newInstance());
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not instance LogFactory class!", e);
		}
	}

	/**
	 * 自定义日志实现
	 * 
	 * @see Slf4jLogFactory
	 * @see Log4jLogFactory
	 * @see Log4j2LogFactory
	 * @see ApacheCommonsLogFactory
	 * @see JdkLogFactory
	 * @see ConsoleLogFactory
	 * 
	 * @param logFactory 日志工厂类对象
	 * @return 自定义的日志工厂类
	 */
	public static LogFactory setCurrentLogFactory(LogFactory logFactory) {
		logFactory.getLog(LogFactory.class).debug("Custom Use [{}] Logger.", logFactory.logFramworkName);
		currentLogFactory = logFactory;
		return currentLogFactory;
	}

	/**
	 * 获得日志对象
	 * 
	 * @param name 日志对象名
	 * @return 日志对象
	 */
	public static Log get(String name) {
		return getCurrentLogFactory().getLog(name);
	}

	/**
	 * 获得日志对象
	 * 
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	public static Log get(Class<?> clazz) {
		return getCurrentLogFactory().getLog(clazz);
	}

	/**
	 * @return 获得调用者的日志
	 */
	public static Log get() {
		return get(new Exception().getStackTrace()[1].getClassName());
	}

	/**
	 * @return 获得调用者的调用者的日志（用于内部辗转调用）
	 */
	protected static Log indirectGet() {
		return get(new Exception().getStackTrace()[2].getClassName());
	}

	/**
	 * 决定日志实现
	 * 
	 * @see Slf4jLogFactory
	 * @see Log4jLogFactory
	 * @see Log4j2LogFactory
	 * @see ApacheCommonsLogFactory
	 * @see JdkLogFactory
	 * @see ConsoleLogFactory
	 * @return 日志实现类
	 */
	private static LogFactory detectLogFactory() {
		LogFactory logFactory;
		try {
			logFactory = new Slf4jLogFactory(true);
			logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
		} catch (NoClassDefFoundError e) {
			try {
				logFactory = new Log4jLogFactory();
				logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
			} catch (NoClassDefFoundError e2) {
				try {
					logFactory = new Log4j2LogFactory();
					logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
				} catch (NoClassDefFoundError e3) {
					try {
						logFactory = new ApacheCommonsLogFactory();
						logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
					} catch (NoClassDefFoundError e4) {
						try {
							logFactory = new TinyLogFactory();
							logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
						} catch (NoClassDefFoundError e5) {
							try {
								logFactory = new JdkLogFactory();
								logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
							} catch (NoClassDefFoundError e6) {
								logFactory = new ConsoleLogFactory();
								logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
							}
						}
					}
				}
			}
		}

		return logFactory;
	}
	// ------------------------------------------------------------------------- Static end
}
