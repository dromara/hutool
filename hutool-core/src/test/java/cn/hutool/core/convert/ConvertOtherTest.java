package cn.hutool.core.convert;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;

/**
 * 其它转换
 * @author Looly
 *
 */
public class ConvertOtherTest {
	@Test
	public void hexTest() {
		String a = "我是一个小小的可爱的字符串";
		String hex = Convert.toHex(a, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("e68891e698afe4b880e4b8aae5b08fe5b08fe79a84e58fafe788b1e79a84e5ad97e7aca6e4b8b2", hex);

		String raw = Convert.hexToStr(hex, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(a, raw);
	}

	@Test
	public void unicodeTest() {
		String a = "我是一个小小的可爱的字符串";

		String unicode = Convert.strToUnicode(a);
		Assert.assertEquals("\\u6211\\u662f\\u4e00\\u4e2a\\u5c0f\\u5c0f\\u7684\\u53ef\\u7231\\u7684\\u5b57\\u7b26\\u4e32", unicode);

		String raw = Convert.unicodeToStr(unicode);
		Assert.assertEquals(raw, a);

		// 针对有特殊空白符的Unicode
		String str = "你 好";
		String unicode2 = Convert.strToUnicode(str);
		Assert.assertEquals("\\u4f60\\u00a0\\u597d", unicode2);

		String str2 = Convert.unicodeToStr(unicode2);
		Assert.assertEquals(str, str2);
	}

	@Test
	public void convertCharsetTest() {
		String a = "我不是乱码";
		// 转换后result为乱码
		String result = Convert.convertCharset(a, CharsetUtil.UTF_8, CharsetUtil.ISO_8859_1);
		String raw = Convert.convertCharset(result, CharsetUtil.ISO_8859_1, "UTF-8");
		Assert.assertEquals(raw, a);
	}

	@Test
	public void convertTimeTest() {
		long a = 4535345;
		long minutes = Convert.convertTime(a, TimeUnit.MILLISECONDS, TimeUnit.MINUTES);
		Assert.assertEquals(75, minutes);
	}

	@Test
	public void digitToChineseTest() {
		double a = 67556.32;
		String digitUppercase = Convert.digitToChinese(a);
		Assert.assertEquals("陆万柒仟伍佰伍拾陆元叁角贰分", digitUppercase);
		
		a = 1024.00;
		digitUppercase = Convert.digitToChinese(a);
		Assert.assertEquals("壹仟零贰拾肆元整", digitUppercase);
		
		a = 1024;
		digitUppercase = Convert.digitToChinese(a);
		Assert.assertEquals("壹仟零贰拾肆元整", digitUppercase);
	}

	@Test
	public void wrapUnwrapTest() {
		// 去包装
		Class<?> wrapClass = Integer.class;
		Class<?> unWraped = Convert.unWrap(wrapClass);
		Assert.assertEquals(int.class, unWraped);

		// 包装
		Class<?> primitiveClass = long.class;
		Class<?> wraped = Convert.wrap(primitiveClass);
		Assert.assertEquals(Long.class, wraped);
	}
}
