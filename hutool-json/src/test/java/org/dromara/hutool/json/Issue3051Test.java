/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3051Test {

	@Test
	public void parseTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(new EmptyBean(),
			JSONConfig.of().setIgnoreError(true));

		Assertions.assertEquals("{}", jsonObject.toString());
	}

	@Test
	public void parseTest2() {
		Assertions.assertThrows(JSONException.class, ()->{
			final JSONObject jsonObject = JSONUtil.parseObj(new EmptyBean());
			Assertions.assertEquals("{}", jsonObject.toString());
		});
	}

	@Data
	static class EmptyBean {

	}
}
