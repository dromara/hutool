package org.dromara.hutool.core.text;

import org.dromara.hutool.core.text.placeholder.StrMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class StrMatcherTest {

	@Test
	public void matcherTest(){
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州-已婚");
		Assertions.assertEquals("小明", match.get("name"));
		Assertions.assertEquals("19", match.get("age"));
		Assertions.assertEquals("男", match.get("gender"));
		Assertions.assertEquals("中国", match.get("country"));
		Assertions.assertEquals("河南", match.get("province"));
		Assertions.assertEquals("郑州", match.get("city"));
		Assertions.assertEquals("已婚", match.get("status"));
	}

	@Test
	public void matcherTest2(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州");
		Assertions.assertEquals(0, match.size());
	}

	@Test
	public void matcherTest3(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}经过${year}年");
		final Map<String, String> match = strMatcher.match("小明经过20年，成长为一个大人。");
		//Console.log(match);
		Assertions.assertEquals("小明", match.get("name"));
		Assertions.assertEquals("20", match.get("year"));
	}
}
