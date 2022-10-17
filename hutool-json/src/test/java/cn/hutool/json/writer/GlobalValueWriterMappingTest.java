package cn.hutool.json.writer;

import cn.hutool.core.convert.Converter;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GlobalValueWriterMappingTest {

	@Before
	public void init(){
		GlobalValueWriterMapping.put(CustomSubBean.class, (JSONValueWriter<CustomSubBean>) (writer, value) -> {
			writer.writeRaw(String.valueOf(value.getId()));
		});
	}

	@Test
	public void customWriteTest(){
		final CustomSubBean customBean = new CustomSubBean();
		customBean.setId(12);
		customBean.setName("aaa");
		final String s = JSONUtil.toJsonStr(customBean);
		Assert.assertEquals("12", s);
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
		Assert.assertEquals("{\"id\":1,\"sub\":12}", s);

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
		Assert.assertEquals(12, customBean1.getSub().getId());
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
