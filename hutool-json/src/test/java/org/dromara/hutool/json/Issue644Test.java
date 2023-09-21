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

import org.dromara.hutool.core.date.TimeUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * 问题反馈对象中有JDK8日期对象时转换失败，5.0.7修复
 */
public class Issue644Test {

	@Test
	public void toBeanTest(){
		final BeanWithDate beanWithDate = new BeanWithDate();
		beanWithDate.setDate(LocalDateTime.now());

		final JSONObject jsonObject = JSONUtil.parseObj(beanWithDate);

		BeanWithDate beanWithDate2 = JSONUtil.toBean(jsonObject, BeanWithDate.class);
		Assertions.assertEquals(TimeUtil.formatNormal(beanWithDate.getDate()),
				TimeUtil.formatNormal(beanWithDate2.getDate()));

		beanWithDate2 = JSONUtil.toBean(jsonObject.toString(), BeanWithDate.class);
		Assertions.assertEquals(TimeUtil.formatNormal(beanWithDate.getDate()),
				TimeUtil.formatNormal(beanWithDate2.getDate()));
	}

	@Data
	static class BeanWithDate{
		private LocalDateTime date;
	}
}
