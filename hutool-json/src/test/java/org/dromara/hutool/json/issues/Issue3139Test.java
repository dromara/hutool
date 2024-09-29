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

package org.dromara.hutool.json.issues;

import lombok.Data;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.json.serializer.JSONDeserializer;
import org.dromara.hutool.json.serializer.TypeAdapterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue3139Test {
	@Test
	public void toBeanTest() {
		final String xml = "<r>\n" +
			"  <c>\n" +
			"     <s>1</s>\n" +
			"     <p>str</p>\n" +
			"  </c>\n" +
			"</r>";

		final JSONObject jsonObject = JSONUtil.parseObj(xml);
		Assertions.assertEquals("{\"r\":{\"c\":{\"s\":1,\"p\":\"str\"}}}", jsonObject.toString());

		final JSONObject r = jsonObject.getJSONObject("r");
		Assertions.assertEquals("{\"c\":{\"s\":1,\"p\":\"str\"}}", r.toString());

		// R中的c为List，但是JSON中为JSONObject，默认会遍历键值对，此处需要自定义序列化
		TypeAdapterManager.getInstance().register(R.class, (JSONDeserializer<R>) (json, deserializeType) -> {
			final R r1 = new R();
			// c作为一个独立对象放入List中
			r1.setC(ListUtil.of((C) json.asJSONObject().get("c", C.class)));
			return r1;
		});

		final R bean = r.toBean(R.class);
		Assertions.assertNotNull(bean);

		final List<C> c = bean.getC();
		Assertions.assertEquals(1, c.size());
		Assertions.assertEquals("1", c.get(0).getS());
		Assertions.assertEquals("str", c.get(0).getP());
	}

	@Data
	static class C {
		String s;
		String p;
	}

	@Data
	static class R {
		List<C> c;
	}
}
