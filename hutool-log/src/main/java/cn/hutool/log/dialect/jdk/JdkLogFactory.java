package cn.hutool.log.dialect.jdk;

import java.io.InputStream;
import java.util.logging.LogManager;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * JDK日志工厂类
 *  <a href="http://java.sun.com/javase/6/docs/technotes/guides/logging/index.html">java.util.logging</a> log.
 * @author Looly
 *
 */
public class JdkLogFactory extends LogFactory{
	
	public JdkLogFactory() {
		super("JDK Logging");
		readConfig();
	}

	@Override
	public Log createLog(String name) {
		return new JdkLog(name);
	}

	@Override
	public Log createLog(Class<?> clazz) {
		return new JdkLog(clazz);
	}
	
	/**
	 * 读取ClassPath下的logging.properties配置文件
	 */
	private void readConfig() {
		//避免循环引用，Log初始化的时候不使用相关工具类
		InputStream in = ResourceUtil.getStreamSafe("logging.properties");
		if(null == in){
			System.err.println("[WARN] Can not find [logging.properties], use [%JRE_HOME%/lib/logging.properties] as default!");
			return;
		}
		
		try {
			LogManager.getLogManager().readConfiguration(in);
		} catch (Exception e) {
			Console.error(e, "Read [logging.properties] from classpath error!");
			try {
				LogManager.getLogManager().readConfiguration();
			} catch (Exception e1) {
				Console.error(e, "Read [logging.properties] from [%JRE_HOME%/lib/logging.properties] error!");
			}
		} finally {
			IoUtil.close(in);
		}
	}
}
