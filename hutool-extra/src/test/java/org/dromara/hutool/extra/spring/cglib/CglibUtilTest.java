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

package org.dromara.hutool.extra.spring.cglib;

import lombok.Data;
import org.dromara.hutool.core.convert.Convert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

public class CglibUtilTest {

	@Test
	@EnabledForJreRange(max = JRE.JAVA_8)
	public void copyTest() {
		final SampleBean bean = new SampleBean();
		OtherSampleBean otherBean = new OtherSampleBean();
		bean.setValue("Hello world");
		bean.setValue2("123");

		CglibUtil.copy(bean, otherBean);
		Assertions.assertEquals("Hello world", otherBean.getValue());
		// 无定义转换器，转换失败
		Assertions.assertEquals(0, otherBean.getValue2());

		final OtherSampleBean otherBean2 = CglibUtil.copy(bean, OtherSampleBean.class);
		Assertions.assertEquals("Hello world", otherBean2.getValue());
		// 无定义转换器，转换失败
		Assertions.assertEquals(0, otherBean.getValue2());

		otherBean = new OtherSampleBean();
		//自定义转换器
		CglibUtil.copy(bean, otherBean, (value, target, context) -> Convert.convertQuietly(target, value));
		Assertions.assertEquals("Hello world", otherBean.getValue());
		Assertions.assertEquals(123, otherBean.getValue2());
	}

	@Data
	public static class SampleBean {
		private String value;
		private String value2;
	}

	@Data
	public static class OtherSampleBean {
		private String value;
		private int value2;
	}
}
