package cn.hutool.core.lang.caller;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link CallerUtil} 单元测试
 * @author Looly
 *
 */
public class CallerTest {

	@Test
	public void getCallerTest() {
		final Class<?> caller = CallerUtil.getCaller();
		Assert.assertEquals(this.getClass(), caller);

		final Class<?> caller0 = CallerUtil.getCaller(0);
		Assert.assertEquals(CallerUtil.class, caller0);

		final Class<?> caller1 = CallerUtil.getCaller(1);
		Assert.assertEquals(this.getClass(), caller1);
	}

	@Test
	public void getCallerCallerTest() {
		final Class<?> callerCaller = CallerTestClass.getCaller();
		Assert.assertEquals(this.getClass(), callerCaller);
	}

	private static class CallerTestClass{
		public static Class<?> getCaller(){
			return CallerUtil.getCallerCaller();
		}
	}
}
