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

package org.dromara.hutool.json.writer;

import lombok.Data;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3541Test {
	@Test
	void writeNumberJSTest() {
		final Demo demo = new Demo();
		// 超出长度
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		final String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.JS));
		Assertions.assertEquals("{\"id\":\"1227690722069581409\",\"name\":\"hutool\"}", jsonStr);

		// 未超出长度
		demo.setId(1227690722069581L);
		final String jsonStr2 = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.JS));
		Assertions.assertEquals("{\"id\":1227690722069581,\"name\":\"hutool\"}", jsonStr2);
	}

	@Test
	void writeNumberStringTest() {
		final Demo demo = new Demo();
		// 超出长度
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		final String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.STRING));
		Assertions.assertEquals("{\"id\":\"1227690722069581409\",\"name\":\"hutool\"}", jsonStr);

		// 未超出长度
		demo.setId(1227690722069581L);
		final String jsonStr2 = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.STRING));
		Assertions.assertEquals("{\"id\":\"1227690722069581\",\"name\":\"hutool\"}", jsonStr2);
	}

	@Test
	void writeNumberNormalTest() {
		final Demo demo = new Demo();
		// 超出长度
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		final String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.NORMAL));
		Assertions.assertEquals("{\"id\":1227690722069581409,\"name\":\"hutool\"}", jsonStr);

		// 未超出长度
		demo.setId(1227690722069581L);
		final String jsonStr2 = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.NORMAL));
		Assertions.assertEquals("{\"id\":1227690722069581,\"name\":\"hutool\"}", jsonStr2);
	}

	@Data
	public static class Demo {
		private Long id;
		private String name;
	}
}
