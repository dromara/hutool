package org.dromara.hutool.core.bean.copier;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * setFieldValueEditor编辑后的值理应继续判断ignoreNullValue
 */
public class Issue3702Test {
	@Test
	void mapToMapTest() {
		final Map<String,String> map= new HashMap<>();
		map.put("a","");
		map.put("b","b");
		map.put("c","c");
		map.put("d","d");

		final Map<String,String> map2= new HashMap<>();
		map2.put("a","a1");
		map2.put("b","b1");
		map2.put("c","c1");
		map2.put("d","d1");

		final CopyOptions option= CopyOptions.of()
			.setIgnoreNullValue(true)
			.setIgnoreError(true)
			.setFieldEditor((entry)->{
				if(ObjUtil.equals(entry.getValue(), "")){
					entry.setValue(null);
				}
				return entry;
			});
		BeanUtil.copyProperties(map,map2,option);
		Assertions.assertEquals("{a=a1, b=b, c=c, d=d}", map2.toString());
	}
}
