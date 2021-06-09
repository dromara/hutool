package cn.hutool.core.codec;

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
		String a = "伦家是一个非常长的字符串66";
		String encode = Base62.encode(a);
		Assert.assertEquals("17vKU8W4JMG8dQF8lk9VNnkdMOeWn4rJMva6F0XsLrrT53iKBnqo", encode);

		String decodeStr = Base62.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}
}
