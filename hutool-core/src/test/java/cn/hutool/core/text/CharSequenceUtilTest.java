package cn.hutool.core.text;

import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

public class CharSequenceUtilTest {

	@Test
	public void replaceTest() {
		String actual = CharSequenceUtil.replace("SSM15930297701BeryAllen", Pattern.compile("[0-9]"), matcher -> "");
		Assert.assertEquals("SSMBeryAllen", actual);
	}

	@Test
	public void replaceTest2(){
		// https://gitee.com/dromara/hutool/issues/I4M16G
		String replace = "#{A}";
		String result = CharSequenceUtil.replace(replace, "#{AAAAAAA}", "1");
		Assert.assertEquals(replace, result);
	}

	@Test
	public void replaceByStrTest(){
		String replace = "SSM15930297701BeryAllen";
		String result = CharSequenceUtil.replace(replace, 5, 12, "***");
		Assert.assertEquals("SSM15***01BeryAllen", result);
	}

	@Test
	public void addPrefixIfNotTest(){
		String str = "hutool";
		String result = CharSequenceUtil.addPrefixIfNot(str, "hu");
		Assert.assertEquals(str, result);

		result = CharSequenceUtil.addPrefixIfNot(str, "Good");
		Assert.assertEquals("Good" + str, result);
	}

	@Test
	public void addSuffixIfNotTest(){
		String str = "hutool";
		String result = CharSequenceUtil.addSuffixIfNot(str, "tool");
		Assert.assertEquals(str, result);

		result = CharSequenceUtil.addSuffixIfNot(str, " is Good");
		Assert.assertEquals( str + " is Good", result);

		// https://gitee.com/dromara/hutool/issues/I4NS0F
		result = CharSequenceUtil.addSuffixIfNot("", "/");
		Assert.assertEquals( "/", result);
	}

	@Test
	public void normalizeTest(){
		// https://blog.csdn.net/oscar999/article/details/105326270

		String str1 = "\u00C1";
		String str2 = "\u0041\u0301";

		Assert.assertNotEquals(str1, str2);

		str1 = CharSequenceUtil.normalize(str1);
		str2 = CharSequenceUtil.normalize(str2);
		Assert.assertEquals(str1, str2);
	}

	@Test
	public void indexOfTest(){
		int index = CharSequenceUtil.indexOf("abc123", '1');
		Assert.assertEquals(3, index);
		index = CharSequenceUtil.indexOf("abc123", '3');
		Assert.assertEquals(5, index);
		index = CharSequenceUtil.indexOf("abc123", 'a');
		Assert.assertEquals(0, index);
	}

	@Test
	public void indexOfTest2(){
		int index = CharSequenceUtil.indexOf("abc123", '1', 0, 3);
		Assert.assertEquals(-1, index);

		index = CharSequenceUtil.indexOf("abc123", 'b', 0, 3);
		Assert.assertEquals(1, index);
	}

	@Test
	public void subPreGbkTest(){
		// https://gitee.com/dromara/hutool/issues/I4JO2E
		String s = "华硕K42Intel酷睿i31代2G以下独立显卡不含机械硬盘固态硬盘120GB-192GB4GB-6GB";

		String v = CharSequenceUtil.subPreGbk(s, 40, false);
		Assert.assertEquals(39, v.getBytes(CharsetUtil.CHARSET_GBK).length);

		v = CharSequenceUtil.subPreGbk(s, 40, true);
		Assert.assertEquals(41, v.getBytes(CharsetUtil.CHARSET_GBK).length);
	}

	@Test
	public void startWithTest(){
		// https://gitee.com/dromara/hutool/issues/I4MV7Q
		Assert.assertFalse(CharSequenceUtil.startWith("123", "123", false, true));
		Assert.assertFalse(CharSequenceUtil.startWith(null, null, false, true));
	}
}
