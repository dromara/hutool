package cn.hutool.core.text;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class StrMatcherTest {

	@Test
	public void matcherTest(){
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州-已婚");
		assertEquals("小明", match.get("name"));
		assertEquals("19", match.get("age"));
		assertEquals("男", match.get("gender"));
		assertEquals("中国", match.get("country"));
		assertEquals("河南", match.get("province"));
		assertEquals("郑州", match.get("city"));
		assertEquals("已婚", match.get("status"));
	}

	@Test
	public void matcherTest2(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州");
		assertEquals(0, match.size());
	}

	@Test
	public void matcherTest3(){
		// 当有无匹配项的时候，按照全不匹配对待
		final StrMatcher strMatcher = new StrMatcher("${name}经过${year}年");
		final Map<String, String> match = strMatcher.match("小明经过20年，成长为一个大人。");
		//Console.log(match);
		assertEquals("小明", match.get("name"));
		assertEquals("20", match.get("year"));
	}
}
