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
