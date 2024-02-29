package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Issue3497Test {

	@Test
	public void setFieldEditorTest() {
		final Map<String, String> aB = MapUtil.builder("a_b", "1").build();
		final Map<?, ?> bean = BeanUtil.toBean(aB, Map.class, CopyOptions.of().setFieldEditor((entry)->{
			entry.setKey(StrUtil.toCamelCase(entry.getKey()));
			return entry;
		}));
		Assertions.assertEquals(bean.toString(), "{aB=1}");
	}

}
