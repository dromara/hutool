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
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class IssueI8NMP7Test {

	@Test
	public void toBeanTest() {
		final String jsonString = "{\"enableTime\":\"1702262524444\"}";
		final DemoModel bean = JSONUtil.toBean(jsonString, JSONConfig.of().setDateFormat("#SSS"), DemoModel.class);
		Assertions.assertNotNull(bean.getEnableTime());
	}

	@Data
	@ToString
	static class DemoModel{
		private Date enableTime;
	}

}
