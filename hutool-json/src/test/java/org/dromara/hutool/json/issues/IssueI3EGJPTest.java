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
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI3EGJPTest {

	@Test
	public void hutoolMapToBean() {
		final JSONObject paramJson = new JSONObject();
		paramJson.putValue("is_booleana", "1");
		paramJson.putValue("is_booleanb", true);
		final ConvertDO convertDO = paramJson.toBean(ConvertDO.class);

		Assertions.assertTrue(convertDO.isBooleana());
		Assertions.assertTrue(convertDO.getIsBooleanb());
	}

	@Data
	public static class ConvertDO {
		private boolean isBooleana;
		private Boolean isBooleanb;
	}
}
