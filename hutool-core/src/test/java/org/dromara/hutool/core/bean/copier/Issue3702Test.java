/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
