package cn.hutool.core.lang;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.lang.caller.CallerUtil;

/**
 * {@link CallerUtil} 单元测试
 * @author Looly
 *
 */
public class CallerTest {
	
	@Test
	public void getCallerTest() {
		Class<?> caller = CallerUtil.getCaller();
		Assert.assertEquals(this.getClass(), caller);
		
		Class<?> caller0 = CallerUtil.getCaller(0);
		Assert.assertEquals(CallerUtil.class, caller0);
		
		Class<?> caller1 = CallerUtil.getCaller(1);
		Assert.assertEquals(this.getClass(), caller1);
		
		Class<?> callerCaller = CallerTestClass.getCaller();
		Assert.assertEquals(this.getClass(), callerCaller);
	}
	
	private static class CallerTestClass{
		public static Class<?> getCaller(){
			return CallerUtil.getCallerCaller();
		}
	}
}
