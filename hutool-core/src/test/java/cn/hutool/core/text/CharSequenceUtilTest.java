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

	// ------------------------------------------------------------------------ remove
}
