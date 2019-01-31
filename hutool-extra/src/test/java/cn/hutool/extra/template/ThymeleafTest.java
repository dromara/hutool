package cn.hutool.extra.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

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

		Map<String, Object> map3 = new HashMap<>();
		map3.put("name", "c");

		List<Map<String, Object>> list = new ArrayList<>();
		list.add(map1);
		list.add(map2);
		list.add(map3);

		// 字符串模板
		TemplateEngine engine = new ThymeleafEngine(new TemplateConfig());
		Template template = engine.getTemplate("<h3 th:each=\"item : ${list}\" th:text=\"${item.name}\"></h3>");
		String render = template.render(Dict.create().set("list", list));
		Assert.assertEquals("<h3>a</h3><h3>b</h3><h3>c</h3>", render);
	}
}
