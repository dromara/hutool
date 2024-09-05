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

import org.dromara.hutool.core.annotation.Alias;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue867Test {

	@Test
	public void toBeanTest(){
		final String json = "{\"abc_1d\":\"123\",\"abc_d\":\"456\",\"abc_de\":\"789\"}";
		final Test02 bean = JSONUtil.toBean(JSONUtil.parseObj(json),Test02.class);
		Assertions.assertEquals("123", bean.getAbc1d());
		Assertions.assertEquals("456", bean.getAbcD());
		Assertions.assertEquals("789", bean.getAbcDe());
	}

	@Data
	static class Test02 {
		@Alias("abc_1d")
		private String abc1d;
		private String abcD;
		private String abcDe;
	}
}
