package cn.hutool.core.text;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class StrMatcherTest {

	@Test
	public void matcherTest(){
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州-已婚");
		Console.log(match);
	}

	@Test
	public void matcherTest2(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州");
		Assert.assertEquals(0, match.size());
	}

	@Test
	public void matcherTest3(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}经过${year}年");
		final Map<String, String> match = strMatcher.match("小明经过20年，成长为一个大人。");
		Console.log(match);
		Assert.assertEquals("小明", match.get("name"));
		Assert.assertEquals("20", match.get("year"));
	}
}
