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

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.json.test.bean.ResultBean;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 测试在bean转换时使用BeanConverter，默认忽略转换失败的字段。
 * 现阶段Converter的问题在于，无法更细粒度的控制转换失败的范围，例如Bean的一个字段为List，
 * list任意一个item转换失败都会导致这个list为null。
 * <p>
 * TODO 需要在Converter中添加ConvertOption，用于更细粒度的控制转换规则
 */
public class Issue1200Test {

	@Test
	@Disabled
	public void toBeanTest(){
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.readUtf8Str("issue1200.json"));
		Console.log(jsonObject);

		final ResultBean resultBean = jsonObject.toBean(ResultBean.class);
		Console.log(resultBean);
	}
}
