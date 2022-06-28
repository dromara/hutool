package cn.hutool.core.text;

import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * @title: PlaceholderUtil测试类
 * @author: trifolium.wang
 * @date: 2022/6/28
 * @modified :
 */
public class PlaceholderUtilTest {

	private final String str = "{name:\"${name}\", generate:\"${generate}\"}";

	@Test
	public void extractParamTest() {
		Collection<String> params = PlaceholderUtil.extractParam(str, "${", "}");
		Console.log(params);
		Assert.assertEquals("[name, generate]", params.toString());
	}

	@Test
	public void replaceParamTest() {
		String paramStr = PlaceholderUtil.replaceParam(str,
				MapUtil.builder("name", "张三").put("generate", "男").build(), "${", "}");
		Console.log(paramStr);
		Assert.assertEquals("{name:\"张三\", generate:\"男\"}", paramStr);
	}

}
