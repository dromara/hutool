package cn.hutool.core.lang.caller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CallerUtil} 单元测试
 * @author Looly
 *
 */
public class CallerTest {

	@Test
	public void getCallerTest() {
		Class<?> caller = CallerUtil.getCaller();
		Assertions.assertEquals(this.getClass(), caller);

		Class<?> caller0 = CallerUtil.getCaller(0);
		Assertions.assertEquals(CallerUtil.class, caller0);

		Class<?> caller1 = CallerUtil.getCaller(1);
		Assertions.assertEquals(this.getClass(), caller1);
	}

	@Test
	public void getCallerCallerTest() {
		Class<?> callerCaller = CallerTestClass.getCaller();
		Assertions.assertEquals(this.getClass(), callerCaller);
	}

	private static class CallerTestClass{
		public static Class<?> getCaller(){
			return CallerUtil.getCallerCaller();
		}
	}
}
