package cn.hutool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class AntPathMatcherTest {

	@Test
	public void matchesTest() {
		final AntPathMatcher antPathMatcher = new AntPathMatcher();
		final boolean matched = antPathMatcher.match("/api/org/organization/{orgId}", "/api/org/organization/999");
		Assertions.assertTrue(matched);
	}

	@Test
	public void matchesTest2() {
		final AntPathMatcher antPathMatcher = new AntPathMatcher();

		String pattern = "/**/*.xml*";
		String path = "/WEB-INF/web.xml";
		boolean isMatched = antPathMatcher.match(pattern, path);
		Assertions.assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service";
		path = "org/codelabor/example/HelloWorldService";
		isMatched = antPathMatcher.match(pattern, path);
		Assertions.assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service?";
		path = "org/codelabor/example/HelloWorldServices";
		isMatched = antPathMatcher.match(pattern, path);
		Assertions.assertTrue(isMatched);
	}

	@Test
	public void matchesTest3(){
		final AntPathMatcher pathMatcher = new AntPathMatcher();
		pathMatcher.setCachePatterns(true);
		pathMatcher.setCaseSensitive(true);
		pathMatcher.setPathSeparator("/");
		pathMatcher.setTrimTokens(true);

		Assertions.assertTrue(pathMatcher.match("a", "a"));
		Assertions.assertTrue(pathMatcher.match("a*", "ab"));
		Assertions.assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/a"));
		Assertions.assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/asdasd/a"));

		Assertions.assertTrue(pathMatcher.match("*", "a"));
		Assertions.assertTrue(pathMatcher.match("*/*", "a/a"));
	}

	/**
	 * AntPathMatcher默认路径分隔符为“/”，而在匹配文件路径时，需要注意Windows下路径分隔符为“\”，Linux下为“/”。靠谱写法如下两种方式：
	 * AntPathMatcher matcher = new AntPathMatcher(File.separator);
	 * AntPathMatcher matcher = new AntPathMatcher(System.getProperty("file.separator"));
	 */
	@Test
	public void matchesTest4() {
		final AntPathMatcher pathMatcher = new AntPathMatcher();

		// 精确匹配
		Assertions.assertTrue(pathMatcher.match("/test", "/test"));
		Assertions.assertFalse(pathMatcher.match("test", "/test"));

		//测试通配符?
		Assertions.assertTrue(pathMatcher.match("t?st", "test"));
		Assertions.assertTrue(pathMatcher.match("te??", "test"));
		Assertions.assertFalse(pathMatcher.match("tes?", "tes"));
		Assertions.assertFalse(pathMatcher.match("tes?", "testt"));

		//测试通配符*
		Assertions.assertTrue(pathMatcher.match("*", "test"));
		Assertions.assertTrue(pathMatcher.match("test*", "test"));
		Assertions.assertTrue(pathMatcher.match("test/*", "test/Test"));
		Assertions.assertTrue(pathMatcher.match("*.*", "test."));
		Assertions.assertTrue(pathMatcher.match("*.*", "test.test.test"));
		Assertions.assertFalse(pathMatcher.match("test*", "test/")); //注意这里是false 因为路径不能用*匹配
		Assertions.assertFalse(pathMatcher.match("test*", "test/t")); //这同理
		Assertions.assertFalse(pathMatcher.match("test*aaa", "testblaaab")); //这个是false 因为最后一个b无法匹配了 前面都是能匹配成功的

		//测试通配符** 匹配多级URL
		Assertions.assertTrue(pathMatcher.match("/*/**", "/testing/testing"));
		Assertions.assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
		Assertions.assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla")); //这里也是true哦
		Assertions.assertFalse(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));

		Assertions.assertFalse(pathMatcher.match("/????", "/bala/bla"));
		Assertions.assertFalse(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

		Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		Assertions.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));
		Assertions.assertTrue(pathMatcher.match("/foo/bar/**", "/foo/bar"));

		//这个需要特别注意：{}里面的相当于Spring MVC里接受一个参数一样，所以任何东西都会匹配的
		Assertions.assertTrue(pathMatcher.match("/{bla}.*", "/testing.html"));
		Assertions.assertFalse(pathMatcher.match("/{bla}.htm", "/testing.html")); //这样就是false了
	}

	/**
	 * 测试 URI 模板变量提取
	 */
	@Test
	public void testExtractUriTemplateVariables() {
		final AntPathMatcher antPathMatcher = new AntPathMatcher();
		final HashMap<String, String> map = (HashMap<String, String>) antPathMatcher.extractUriTemplateVariables("/api/org/organization/{orgId}",
				"/api/org" +
						"/organization" +
						"/999");
		Assertions.assertEquals(1, map.size());
	}
}
