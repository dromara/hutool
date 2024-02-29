package cn.hutool.core.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class Issue3497Test {
	@Test
	public void mapToMapTest() {
		final Map<String, String> aB = MapUtil.builder("a_b", "1").build();
		final Map<?, ?> bean = BeanUtil.toBean(aB, Map.class, CopyOptions.create().setFieldNameEditor(StrUtil::toCamelCase));
		Assert.assertEquals(bean.toString(), "{aB=1}");
	}
}
