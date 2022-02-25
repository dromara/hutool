package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class AntPathMatcherTest {

	@Test
	public void matchesTest() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		boolean matched = antPathMatcher.match("/api/org/organization/{orgId}", "/api/org/organization/999");
		Assert.assertTrue(matched);
	}

	@Test
	public void matchesTest2() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();

		String pattern = "/**/*.xml*";
		String path = "/WEB-INF/web.xml";
		boolean isMatched = antPathMatcher.match(pattern, path);
		Assert.assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service";
		path = "org/codelabor/example/HelloWorldService";
		isMatched = antPathMatcher.match(pattern, path);
		Assert.assertTrue(isMatched);

		pattern = "org/codelabor/*/**/*Service?";
		path = "org/codelabor/example/HelloWorldServices";
		isMatched = antPathMatcher.match(pattern, path);
		Assert.assertTrue(isMatched);
	}

	@Test
	public void matchesTest3(){
		AntPathMatcher pathMatcher = new AntPathMatcher();
		pathMatcher.setCachePatterns(true);
		pathMatcher.setCaseSensitive(true);
		pathMatcher.setPathSeparator("/");
		pathMatcher.setTrimTokens(true);

		Assert.assertTrue(pathMatcher.match("a", "a"));
		Assert.assertTrue(pathMatcher.match("a*", "ab"));
		Assert.assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/a"));
		Assert.assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/asdasd/a"));

		Assert.assertTrue(pathMatcher.match("*", "a"));
		Assert.assertTrue(pathMatcher.match("*/*", "a/a"));
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
		Assert.assertTrue(pathMatcher.match("/test", "/test"));
		Assert.assertFalse(pathMatcher.match("test", "/test"));

		//测试通配符?
		Assert.assertTrue(pathMatcher.match("t?st", "test"));
		Assert.assertTrue(pathMatcher.match("te??", "test"));
		Assert.assertFalse(pathMatcher.match("tes?", "tes"));
		Assert.assertFalse(pathMatcher.match("tes?", "testt"));

		//测试通配符*
		Assert.assertTrue(pathMatcher.match("*", "test"));
		Assert.assertTrue(pathMatcher.match("test*", "test"));
		Assert.assertTrue(pathMatcher.match("test/*", "test/Test"));
		Assert.assertTrue(pathMatcher.match("*.*", "test."));
		Assert.assertTrue(pathMatcher.match("*.*", "test.test.test"));
		Assert.assertFalse(pathMatcher.match("test*", "test/")); //注意这里是false 因为路径不能用*匹配
		Assert.assertFalse(pathMatcher.match("test*", "test/t")); //这同理
		Assert.assertFalse(pathMatcher.match("test*aaa", "testblaaab")); //这个是false 因为最后一个b无法匹配了 前面都是能匹配成功的

		//测试通配符** 匹配多级URL
		Assert.assertTrue(pathMatcher.match("/*/**", "/testing/testing"));
		Assert.assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
		Assert.assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla")); //这里也是true哦
		Assert.assertFalse(pathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));

		Assert.assertFalse(pathMatcher.match("/????", "/bala/bla"));
		Assert.assertFalse(pathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

		Assert.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
		Assert.assertTrue(pathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
		Assert.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
		Assert.assertTrue(pathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));
		Assert.assertTrue(pathMatcher.match("/foo/bar/**", "/foo/bar"));

		//这个需要特别注意：{}里面的相当于Spring MVC里接受一个参数一样，所以任何东西都会匹配的
		Assert.assertTrue(pathMatcher.match("/{bla}.*", "/testing.html"));
		Assert.assertFalse(pathMatcher.match("/{bla}.htm", "/testing.html")); //这样就是false了
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
		Assert.assertEquals(1, map.size());
	}
}
