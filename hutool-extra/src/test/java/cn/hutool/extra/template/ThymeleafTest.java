package cn.hutool.extra.template;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.Dict;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.StringWriter;
import java.util.*;

/**
 * Thymeleaf单元测试
 *
 * @author looly
 *
 */
public class ThymeleafTest {

	/**
	 * <a href="https://github.com/dromara/hutool/issues/2530">...</a>
	 * 自定义操作原始引擎
	 */
	@Test
	@Disabled
	public void addDialectTest(){
		final TemplateEngine engine = TemplateUtil.createEngine();
		if(engine instanceof ThymeleafEngine){
			final org.thymeleaf.TemplateEngine rawEngine = ((ThymeleafEngine) engine).getRawEngine();
			rawEngine.addDialect(new StandardDialect());
		}
	}

	@Test
	public void thymeleafEngineTest() {
		final Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "a");

		final Map<String, Object> map2 = new HashMap<>();
		map2.put("name", "b");

		// 日期测试
		final Map<String, Object> map3 = new HashMap<>();
		map3.put("name", DateUtil.parse("2019-01-01"));

		final List<Map<String, Object>> list = new ArrayList<>();
		list.add(map1);
		list.add(map2);
		list.add(map3);

		// 字符串模板
		final TemplateEngine engine = new ThymeleafEngine(new TemplateConfig());
		final Template template = engine.getTemplate("<h3 th:each=\"item : ${list}\" th:text=\"${item.name}\"></h3>");
		final String render = template.render(Dict.of().set("list", list));
		Assertions.assertEquals("<h3>a</h3><h3>b</h3><h3>2019-01-01 00:00:00</h3>", render);
	}

	@Test
	public void thymeleafEngineTest2() {
		final Map<String, Object> map1 = new HashMap<>();
		map1.put("name", "a");

		final Map<String, Object> map2 = new HashMap<>();
		map2.put("name", "b");

		// 日期测试
		final Map<String, Object> map3 = new HashMap<>();
		map3.put("name", DateUtil.parse("2019-01-01"));

		final List<Map<String, Object>> list = new ArrayList<>();
		list.add(map1);
		list.add(map2);
		list.add(map3);

		final LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("list", list);

		 hutoolApi(map);
		thymeleaf(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void thymeleaf(final Map map) {
		final org.thymeleaf.TemplateEngine templateEngine = new org.thymeleaf.TemplateEngine();
		final StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
		templateEngine.addTemplateResolver(stringTemplateResolver);

		final StringWriter writer = new StringWriter();
		final Context context = new Context(Locale.getDefault(), map);
		templateEngine.process("<h3 th:each=\"item : ${list}\" th:text=\"${item.name}\"></h3>", context, writer);

		Assertions.assertEquals("<h3>a</h3><h3>b</h3><h3>2019-01-01 00:00:00</h3>", writer.toString());
	}

	@SuppressWarnings("rawtypes")
	private static void hutoolApi(final Map map) {

		// 字符串模板
		final TemplateEngine engine = new ThymeleafEngine(new TemplateConfig());
		final Template template = engine.getTemplate("<h3 th:each=\"item : ${list}\" th:text=\"${item.name}\"></h3>");
		// "<h3 th:text=\"${nestMap.nestKey}\"></h3>"
		final String render = template.render(map);
		Assertions.assertEquals("<h3>a</h3><h3>b</h3><h3>2019-01-01 00:00:00</h3>", render);
	}
}
