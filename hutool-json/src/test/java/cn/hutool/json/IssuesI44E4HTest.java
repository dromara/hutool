package cn.hutool.json;

import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试自定义反序列化
 */
public class IssuesI44E4HTest {

	@Test
	public void deserializerTest(){
		GlobalSerializeMapping.put(TestDto.class, (JSONDeserializer<TestDto>) json -> {
			final TestDto testDto = new TestDto();
			testDto.setMd(new AcBizModuleMd("name1", ((JSONObject)json).getStr("md")));
			return testDto;
		});

		String jsonStr = "{\"md\":\"value1\"}";
		final TestDto testDto = JSONUtil.toBean(jsonStr, TestDto.class);
		Assert.assertEquals("value1", testDto.getMd().getValue());
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class AcBizModuleMd {
		private String name;
		private String value;
		// 值列表
		public static final AcBizModuleMd Value1 = new AcBizModuleMd("value1", "name1");
		public static final AcBizModuleMd Value2 = new AcBizModuleMd("value2", "name2");
		public static final AcBizModuleMd Value3 = new AcBizModuleMd("value3", "name3");
	}

	@Getter
	@Setter
	public static class TestDto {
		private AcBizModuleMd md;
	}
}


