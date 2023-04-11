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

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/dromara/hutool/issues/3051
 */
public class Issue3051Test {
	@Test
	public void parseTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(new EmptyBean(),
			JSONConfig.create().setIgnoreError(true));

		Assert.assertEquals("{}", jsonObject.toString());
	}

	@Test
	public void parseTest2() {
		final JSONObject jsonObject = JSONUtil.parseObj(new EmptyBean());

		Assert.assertEquals("{}", jsonObject.toString());
	}

	@Data
	static class EmptyBean {

	}
}
