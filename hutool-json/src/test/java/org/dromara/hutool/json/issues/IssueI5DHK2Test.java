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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI5DHK2Test {

	@Test
	public void toBeanTest(){
		final String jsonStr = "{\n" +
				"    \"punished_parties\": [{\n" +
				"        \"properties\": {\n" +
				"            \"employment_informations\": [{\n" +
				"                \"employer_name\": \"张三皮包公司\"\n" +
				"            }]\n" +
				"        }\n" +
				"    }]\n" +
				"}";

		final JSONObject json = JSONUtil.parseObj(jsonStr);
		final String exployerName = json
				.getJSONArray("punished_parties")
				.getJSONObject(0)
				.getJSONObject("properties")
				.getJSONArray("employment_informations")
				.getJSONObject(0).getStr("employer_name");
		Assertions.assertEquals("张三皮包公司", exployerName);


		final Punished punished = JSONUtil.toBean(json, Punished.class);
		Assertions.assertEquals("张三皮包公司", punished.getPunished_parties()[0].getProperties().getEmployment_informations()[0].getEmployer_name());
	}

	@Data
	private static class Punished{
		private Party[] punished_parties;
	}

	@Data
	private static class Party{
		private Properties properties;
	}

	@Data
	private static class Properties{
		private EmploymentInformation[] employment_informations;
	}

	@Data
	private static class EmploymentInformation {
		private String employer_name;
	}
}
