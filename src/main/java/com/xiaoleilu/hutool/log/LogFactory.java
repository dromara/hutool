package com.xiaoleilu.hutool.log;

import java.util.Arrays;
import java.util.List;

import com.xiaoleilu.hutool.ClassUtil;
import com.xiaoleilu.hutool.log.dialect.ApacheCommonsLog;
import com.xiaoleilu.hutool.log.dialect.JdkLog;
import com.xiaoleilu.hutool.log.dialect.Log4j2Log;
import com.xiaoleilu.hutool.log.dialect.Log4jLog;
import com.xiaoleilu.hutool.log.dialect.Slf4jLog;

/**
 * 日志工厂类
 * @author Looly
 *
 */
public class LogFactory {
	
	private static final Class<? extends AbstractLog> currentLogClass = detectLog();
	
	/**
	 * 决定日志实现
	 * @return 日志实现类
	 */
	public static Class<? extends AbstractLog> detectLog(){
		List<Class<? extends AbstractLog>> logClassList = Arrays.asList(
				Slf4jLog.class,
				Log4jLog.class, 
				Log4j2Log.class, 
				ApacheCommonsLog.class, 
				JdkLog.class
		);
		
		for (Class<? extends AbstractLog> clazz : logClassList) {
			try {
				clazz.getConstructor(Class.class).newInstance(LogFactory.class).info("Use Log Framework: [{}]", clazz.getSimpleName());
				return clazz;
			} catch (Error | Exception e) {
				continue;
			}
		}
		return JdkLog.class;
	}
	
	/**
	 * @return 当前使用的日志系统
	 */
	public static Class<? extends AbstractLog> getCurrentLogClass(){
		return currentLogClass;
	}
	
	/**
	 * 获得日志对象
	 * @param name 日志对象名
	 * @return 日志对象
	 */
	public static Log getLog(String name){
		return ClassUtil.newInstance(currentLogClass, name);
	}
	
	/**
	 * 获得日志对象
	 * @param clazz 日志对应类
	 * @return 日志对象
	 */
	public static Log getLog(Class<?> clazz){
		return ClassUtil.newInstance(currentLogClass, clazz);
	}
	
	/**
	 * @return 获得日志，自动判定日志发出者
	 */
	public static Log get() {
		return getLog(new Exception().getStackTrace()[2].getClassName());
	}
}
