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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IssueI9HQQETest {

	@Test
	public void toBeanTest(){
		final String json = "{\"currentDate\":\"18/04/2024\", \"currentDateTime\":\"18/04/2024\"}";
		final JSONConfig jsonConfig = new JSONConfig();
		jsonConfig.setDateFormat("dd/MM/yyyy");

		final OldJSONObject entries = JSONUtil.parseObj(json, jsonConfig);

		final MMHBo mmh = entries.toBean(MMHBo.class);
		Assertions.assertNotNull(mmh.getCurrentDate());
		Assertions.assertNotNull(mmh.getCurrentDateTime());
	}

	@Data
	public static class MMHBo {
		/**
		 * 当前时间
		 */
		private LocalDate currentDate;
		private LocalDateTime currentDateTime;
	}
}
