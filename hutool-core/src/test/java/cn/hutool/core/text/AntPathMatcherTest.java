package cn.hutool.core.text;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class AntPathMatcherTest {

	@Test
	public void matchesTest() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		boolean matched = antPathMatcher.match("/api/org/organization/{orgId}", "/api/org/organization/999");
		assertTrue(matched);
	}

	@Test
	public void matchesTest2() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();

		String pattern = "/**/*.xml*";
		String path = "/WEB-INF/web.xml";
		boolean isMatched = antPathMatcher.match(pattern, path);
		assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service";
		path = "org/codelabor/example/HelloWorldService";
		isMatched = antPathMatcher.match(pattern, path);
		assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service?";
		path = "org/codelabor/example/HelloWorldServices";
		isMatched = antPathMatcher.match(pattern, path);
		assertTrue(isMatched);
	}

	@Test
	public void matchesTest3(){
		AntPathMatcher pathMatcher = new AntPathMatcher();
		pathMatcher.setCachePatterns(true);
		pathMatcher.setCaseSensitive(true);
		pathMatcher.setPathSeparator("/");
		pathMatcher.setTrimTokens(true);

		assertTrue(pathMatcher.match("a", "a"));
		assertTrue(pathMatcher.match("a*", "ab"));
		assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/a"));
		assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/asdasd/a"));

		assertTrue(pathMatcher.match("*", "a"));
		assertTrue(pathMatcher.match("*/*", "a/a"));
	}

	/**
	 * AntPathMatcher默认路径分隔符为“/”，而在匹配文件路径时，需要注意Windows下路径分隔符为“\”，Linux下为“/”。靠谱写法如下两种方式：
	 * AntPathMatcher matcher = new AntPathMatcher(File.separator);
	 * AntPathMatcher matcher = new AntPathMatcher(System.getProperty("file.separator"));
	 */
	@Test
	public void matchesTest4() {
		AntPathMatcher pathMatcher = new AntPathMatcher();

		// 精确匹配
		assertTrue(pathMatcher.match("/test", "/test"));
		assertFalse(pathMatcher.match("test", "/test"));

		//测试通配符?
		assertTrue(pathMatcher.match("t?st", "test"));
		assertTrue(pathMatcher.match("te??", "test"));
		assertFalse(pathMatcher.match("tes?", "tes"));
		assertFalse(pathMatcher.match("tes?", "testt"));

		//测试通配符*
		assertTrue(pathMatcher.match("*", "test"));
		assertTrue(pathMatcher.match("test*", "test"));
		assertTrue(pathMatcher.match("test/*", "test/Test"));
		assertTrue(pathMatcher.match("*.*", "test."));
		assertTrue(pathMatcher.match("*.*", "test.test.test"));
		assertFalse(pathMatcher.match("test*", "test/")); //注意这里是false 因为路径不能用*匹配
		assertFalse(pathMatcher.match("test*", "test/t")); //这同理
		assertFalse(pathMatcher.match("test*aaa", "testblaaab")); //这个是false 因为最后一个b无法匹配了 前面都是能匹配成功的

		//测试通配符** 匹配多级URL
		assertTrue(pathMatcher.match("/*/**", "/testing/testing"));
		assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
		assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla")); //这里也是true哦
		assertFalse(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));

		assertFalse(pathMatcher.match("/????", "/bala/bla"));
		assertFalse(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));
		assertTrue(pathMatcher.match("/foo/bar/**", "/foo/bar"));

		//这个需要特别注意：{}里面的相当于Spring MVC里接受一个参数一样，所以任何东西都会匹配的
		assertTrue(pathMatcher.match("/{bla}.*", "/testing.html"));
		assertFalse(pathMatcher.match("/{bla}.htm", "/testing.html")); //这样就是false了
	}

	/**
	 * 测试 URI 模板变量提取
	 */
	@Test
	public void testExtractUriTemplateVariables() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		HashMap<String, String> map = (HashMap<String, String>) antPathMatcher.extractUriTemplateVariables("/api/org/organization/{orgId}",
				"/api/org" +
						"/organization" +
						"/999");
		assertEquals(1, map.size());
	}
}
