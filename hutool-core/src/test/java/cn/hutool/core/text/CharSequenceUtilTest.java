package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

public class CharSequenceUtilTest {

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
	public void startWithNumberTest() throws Exception {
		String var1 = "123str";
		String var2 = "180公斤";
		String var3 = "str";
		String var4 = "身高180";
		Assert.assertTrue(CharSequenceUtil.startWithNumber(var1));
		Assert.assertTrue(CharSequenceUtil.startWithNumber(var2));
		Assert.assertFalse(CharSequenceUtil.startWithNumber(var3));
		Assert.assertFalse(CharSequenceUtil.startWithNumber(var4));
	}

	@Test
	public void startWithGeneralTest() throws Exception {
		String var1 = "str";
		String var2 = "123";
		String var3 = "_str";
		String var4 = "身高180";
		Assert.assertTrue(CharSequenceUtil.startWithGeneral(var1));
		Assert.assertTrue(CharSequenceUtil.startWithGeneral(var2));
		Assert.assertTrue(CharSequenceUtil.startWithGeneral(var3));
		Assert.assertFalse(CharSequenceUtil.startWithGeneral(var4));
	}

	@Test
	public void startWithWordTest() throws Exception {
		String var1 = "str";
		String var2 = "123";
		String var3 = "_str";
		String var4 = "身高180";
		Assert.assertTrue(CharSequenceUtil.startWithWord(var1));
		Assert.assertFalse(CharSequenceUtil.startWithWord(var2));
		Assert.assertFalse(CharSequenceUtil.startWithWord(var3));
		Assert.assertFalse(CharSequenceUtil.startWithWord(var4));
	}

	@Test
	public void startWithChineseTest() throws Exception {
		String var1 = "str";
		String var2 = "_str";
		String var3 = "123";
		String var4 = "身高180";
		Assert.assertFalse(CharSequenceUtil.startWithChinese(var1));
		Assert.assertFalse(CharSequenceUtil.startWithChinese(var2));
		Assert.assertFalse(CharSequenceUtil.startWithChinese(var3));
		Assert.assertTrue(CharSequenceUtil.startWithChinese(var4));
	}

	@Test
	public void endWithNumberTest() throws Exception {
		String var1 = "str123";
		String var2 = "身高180";
		String var3 = "str";
		String var4 = "180公斤";
		Assert.assertTrue(CharSequenceUtil.endWithNumber(var1));
		Assert.assertTrue(CharSequenceUtil.endWithNumber(var2));
		Assert.assertFalse(CharSequenceUtil.endWithNumber(var3));
		Assert.assertFalse(CharSequenceUtil.endWithNumber(var4));
	}

	@Test
	public void endWithGeneralTest() throws Exception {
		String var1 = "str";
		String var2 = "123";
		String var3 = "str_";
		String var4 = "180公斤";
		Assert.assertTrue(CharSequenceUtil.endWithGeneral(var1));
		Assert.assertTrue(CharSequenceUtil.endWithGeneral(var2));
		Assert.assertTrue(CharSequenceUtil.endWithGeneral(var3));
		Assert.assertFalse(CharSequenceUtil.endWithGeneral(var4));
	}

	@Test
	public void endWithWordTest() throws Exception {
		String var1 = "str";
		String var2 = "_str";
		String var3 = "123";
		String var4 = "身高180";
		Assert.assertTrue(CharSequenceUtil.endWithWord(var1));
		Assert.assertTrue(CharSequenceUtil.endWithWord(var2));
		Assert.assertFalse(CharSequenceUtil.endWithWord(var3));
		Assert.assertFalse(CharSequenceUtil.endWithWord(var4));
	}

	@Test
	public void endWithChineseTest() throws Exception {
		String var1 = "str";
		String var2 = "_str";
		String var3 = "123";
		String var4 = "180公斤";
		Assert.assertFalse(CharSequenceUtil.endWithChinese(var1));
		Assert.assertFalse(CharSequenceUtil.endWithChinese(var2));
		Assert.assertFalse(CharSequenceUtil.endWithChinese(var3));
		Assert.assertTrue(CharSequenceUtil.endWithChinese(var4));
	}

	// ------------------------------------------------------------------------ remove

	@Test
	public void removeNumbersTest(){
		String var1 = "";
		String var2 = "str";
		String var3 = "身高180";
		String var4 = "身高180体重180";
		Assert.assertEquals("", CharSequenceUtil.removeNumbers(var1));
		Assert.assertEquals("str", CharSequenceUtil.removeNumbers(var2));
		Assert.assertEquals("身高", CharSequenceUtil.removeNumbers(var3));
		Assert.assertEquals("身高体重", CharSequenceUtil.removeNumbers(var4));
	}

	@Test
	public void removeChineseTest(){
		String var1 = "";
		String var2 = "str";
		String var3 = "身高180";
		String var4 = "身高180体重180cm";
		Assert.assertEquals("", CharSequenceUtil.removeChinese(var1));
		Assert.assertEquals("str", CharSequenceUtil.removeChinese(var2));
		Assert.assertEquals("180", CharSequenceUtil.removeChinese(var3));
		Assert.assertEquals("180180cm", CharSequenceUtil.removeChinese(var4));
	}

}
