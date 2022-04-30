package cn.hutool.core.codec;

import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Base62单元测试
 *
 * @author looly
 *
 */
public class Base62Test {

	@Test
	public void encodeAndDecodeTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base62.encode(a);
		Assert.assertEquals("17vKU8W4JMG8dQF8lk9VNnkdMOeWn4rJMva6F0XsLrrT53iKBnqo", encode);

		final String decodeStr = Base62.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base62.encodeInverted(a);
		Assert.assertEquals("17Vku8w4jmg8Dqf8LK9vnNKDmoEwN4RjmVA6f0xSlRRt53IkbNQO", encode);

		final String decodeStr = Base62.decodeStrInverted(encode);
		Assert.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeRandomTest() {
		final String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		final String encode = Base62.encode(a);
		final String decodeStr = Base62.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedRandomTest() {
		final String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		final String encode = Base62.encodeInverted(a);
		final String decodeStr = Base62.decodeStrInverted(encode);
		Assert.assertEquals(a, decodeStr);
	}
}
