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

package org.dromara.hutool.json;

import lombok.Data;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3504Test {
	@Test
	public void test3504() {
		final JsonBean jsonBean = new JsonBean();
		jsonBean.setName("test");
		jsonBean.setClasses(new Class[]{String.class});
		final String huToolJsonStr = JSONUtil.toJsonStr(jsonBean);
		Console.log(huToolJsonStr);
		final JsonBean bean = JSONUtil.toBean(huToolJsonStr, JsonBean.class);
		Assertions.assertNotNull(bean);
		Assertions.assertEquals("test", bean.getName());
	}

	@Data
	public static class JsonBean {
		private String name;
		private Class<?>[] classes;
	}
}
