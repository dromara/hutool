package org.dromara.hutool.writer;

import org.dromara.hutool.JSONConfig;
import org.dromara.hutool.JSONUtil;
import org.dromara.hutool.convert.Converter;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GlobalValueWriterMappingTest {

	@BeforeEach
	public void init(){
		GlobalValueWriterMapping.put(CustomSubBean.class,
				(JSONValueWriter<CustomSubBean>) (writer, value) -> writer.writeRaw(String.valueOf(value.getId())));
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
