package cn.hutool.core.text;

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

	// ------------------------------------------------------------------------ remove
}
