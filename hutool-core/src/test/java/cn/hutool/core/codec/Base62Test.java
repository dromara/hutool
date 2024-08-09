package cn.hutool.core.codec;

import cn.hutool.core.util.RandomUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Base62单元测试
 *
 * @author looly
 *
 */
public class Base62Test {

	@Test
	public void encodeAndDecodeTest() {
		String a = "伦家是一个非常长的字符串66";
		String encode = Base62.encode(a);
		assertEquals("17vKU8W4JMG8dQF8lk9VNnkdMOeWn4rJMva6F0XsLrrT53iKBnqo", encode);

		String decodeStr = Base62.decodeStr(encode);
		assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedTest() {
		String a = "伦家是一个非常长的字符串66";
		String encode = Base62.encodeInverted(a);
		assertEquals("17Vku8w4jmg8Dqf8LK9vnNKDmoEwN4RjmVA6f0xSlRRt53IkbNQO", encode);

		String decodeStr = Base62.decodeStrInverted(encode);
		assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeRandomTest() {
		String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		String encode = Base62.encode(a);
		String decodeStr = Base62.decodeStr(encode);
		assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedRandomTest() {
		String a = RandomUtil.randomString(RandomUtil.randomInt(1000));
		String encode = Base62.encodeInverted(a);
		String decodeStr = Base62.decodeStrInverted(encode);
		assertEquals(a, decodeStr);
	}
}
