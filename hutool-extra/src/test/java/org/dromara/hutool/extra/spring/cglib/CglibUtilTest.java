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

package org.dromara.hutool.extra.spring.cglib;

import lombok.Data;
import org.dromara.hutool.core.convert.ConvertUtil;
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
		CglibUtil.copy(bean, otherBean, (value, target, context) -> ConvertUtil.convertQuietly(target, value));
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
