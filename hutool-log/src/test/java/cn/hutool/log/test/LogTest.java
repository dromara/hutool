package cn.hutool.log.test;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.level.Level;

/**
 * 日志门面单元测试
 * @author Looly
 *
 */
public class LogTest {
	
	@Test
	public void logTest(){
		Log log = LogFactory.get();
		
		// 自动选择日志实现
		log.debug("This is {} log", Level.DEBUG);
		log.info("This is {} log", Level.INFO);
		log.warn("This is {} log", Level.WARN);
		
//		Exception e = new Exception("test Exception");
//		log.error(e, "This is {} log", Level.ERROR);
	}
	
	/**
	 * 兼容slf4j日志消息格式测试，即第二个参数是异常对象时正常输出异常信息
	 */
	@Test
	@Ignore
	public void logWithExceptionTest() {
		Log log = LogFactory.get();
		Exception e = new Exception("test Exception");
		log.error("我是错误消息", e);
	}

	@Test
	public void logNullTest(){
		final Log log = Log.get();
		log.debug(null);
		log.info(null);
		log.warn(null);
	}
}
