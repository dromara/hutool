package cn.hutool.core.lang.intern;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InternUtilTest {

	/**
	 * 检查规范字符串是否相同
	 */
	@SuppressWarnings("StringOperationCanBeSimplified")
	@Test
	public void weakTest(){
		final Intern<String> intern = InternUtil.ofWeak();
		final String a1 = RandomUtil.randomString(RandomUtil.randomInt(100));
		final String a2 = new String(a1);

		Assertions.assertNotSame(a1, a2);

		Assertions.assertSame(intern.intern(a1), intern.intern(a2));
	}

}
