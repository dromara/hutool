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

package org.dromara.hutool.json.writer;

import lombok.Data;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValueWriterManagerTest {

	@BeforeEach
	public void init(){
		ValueWriterManager.getInstance().register(new ValueWriter() {
			@Override
			public void write(final JSONWriter writer, final Object value) {
				writer.writeRaw(String.valueOf(((CustomSubBean)value).getId()));
			}

			@Override
			public boolean test(final Object value) {
				return value instanceof CustomSubBean;
			}
		});
	}

	@Test
	public void customWriteTest(){
		final CustomSubBean customBean = new CustomSubBean();
		customBean.setId(12);
		customBean.setName("aaa");
		final String s = JSONUtil.toJsonStr(customBean);
		Assertions.assertEquals("12", s);
	}

	@Test
	public void customWriteSubTest(){
		final CustomSubBean customSubBean = new CustomSubBean();
		customSubBean.setId(12);
		customSubBean.setName("aaa");
		final CustomBean customBean = new CustomBean();
		customBean.setId(1);
		customBean.setSub(customSubBean);

		final String s = JSONUtil.toJsonStr(customBean);
		Assertions.assertEquals("{\"id\":1,\"sub\":12}", s);

		// 自定义转换
		final JSONConfig jsonConfig = JSONConfig.of();
		final Converter converter = jsonConfig.getConverter();
		jsonConfig.setConverter((targetType, value) -> {
			if(targetType == CustomSubBean.class){
				final CustomSubBean subBean = new CustomSubBean();
				subBean.setId((Integer) value);
				return subBean;
			}
			return converter.convert(targetType, value);
		});
		final CustomBean customBean1 = JSONUtil.parseObj(s, jsonConfig).toBean(CustomBean.class);
		Assertions.assertEquals(12, customBean1.getSub().getId());
	}

	@Data
	static class CustomSubBean {
		private int id;
		private String name;
	}

	@Data
	static class CustomBean{
		private int id;
		private CustomSubBean sub;
	}
}
