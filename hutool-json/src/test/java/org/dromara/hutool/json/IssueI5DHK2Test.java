/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

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
