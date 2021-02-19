package cn.hutool.extra.template;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;

/**
 * Thymeleaf单元测试
 * 
 * @author looly
 *
 */
public class ThymeleafTest {

	@Test
	public void thymeleafEngineTest() {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "a");

		Map<String, Object> map2 = new HashMap<>();
		map2.put("name", "b");

		// 日期测试
		Map<String, Object> map3 = new HashMap<>();
		map3.put("name", DateUtil.parse("2019-01-01"));

		List<Map<String, Object>> list = new ArrayList<>();
		list.add(map1);
		list.add(map2);
		list.add(map3);

		// 字符串模板
		TemplateEngine engine = new ThymeleafEngine(new TemplateConfig());
		Template template = engine.getTemplate("<h3 th:each=\"item : ${list}\" th:text=\"${item.name}\"></h3>");
		String render = template.render(Dict.create().set("list", list));
		Assert.assertEquals("<h3>a</h3><h3>b</h3><h3>2019-01-01 00:00:00</h3>", render);
	}

	@Test
	public void thymeleafEngineTest2() {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "a");

		Map<String, Object> map2 = new HashMap<>();
		map2.put("name", "b");

		// 日期测试
		Map<String, Object> map3 = new HashMap<>();
		map3.put("name", DateUtil.parse("2019-01-01"));

		List<Map<String, Object>> list = new ArrayList<>();
		list.add(map1);
		list.add(map2);
		list.add(map3);

		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("list", list);

		 hutoolApi(map);
		thymeleaf(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void thymeleaf(Map map) {
		org.thymeleaf.TemplateEngine templateEngine = new org.thymeleaf.TemplateEngine();
		StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
		templateEngine.addTemplateResolver(stringTemplateResolver);

		StringWriter writer = new StringWriter();
		Context context = new Context(Locale.getDefault(), map);
		templateEngine.process("<h3 th:each=\"item : ${list}\" th:text=\"${item.name}\"></h3>", context, writer);

		Assert.assertEquals("<h3>a</h3><h3>b</h3><h3>2019-01-01 00:00:00</h3>", writer.toString());
	}

	@SuppressWarnings("rawtypes")
	private static void hutoolApi(Map map) {

		// 字符串模板
		TemplateEngine engine = new ThymeleafEngine(new TemplateConfig());
		Template template = engine.getTemplate("<h3 th:each=\"item : ${list}\" th:text=\"${item.name}\"></h3>");
		// "<h3 th:text=\"${nestMap.nestKey}\"></h3>"
		String render = template.render(map);
		Assert.assertEquals("<h3>a</h3><h3>b</h3><h3>2019-01-01 00:00:00</h3>", render);
	}
}
