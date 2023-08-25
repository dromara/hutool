package cn.hutool.json;

import cn.hutool.core.lang.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/dromara/hutool/issues/I7M2GZ
 */
public class IssueI7M2GZTest {

	@Data
	@AllArgsConstructor
	public static class JSONBeanParserImpl implements JSONBeanParser<Object> {
		private String name;
		private Integer parsed;

		@Override
		public void parse(final Object object) {
			setName("new Object");
			setParsed(12);
		}
	}

	@Data
	public static class MyEntity<T> {
		private List<T> list;
	}

	@Test
	public void toListTest() {
		final List<JSONBeanParserImpl> list = new ArrayList<>();
		list.add(new JSONBeanParserImpl("Object1", 1));

		final MyEntity<JSONBeanParserImpl> entity = new MyEntity<>();
		entity.setList(list);

		final String json = JSONUtil.toJsonStr(entity);
		//Console.log(json);
		final MyEntity<JSONBeanParserImpl> result = JSONUtil.toBean(json, new TypeReference<MyEntity<JSONBeanParserImpl>>() {
		}, false);
		Assert.assertEquals("new Object", result.getList().get(0).getName());
		Assert.assertNotNull(result.getList().get(0).getParsed());
		Assert.assertEquals(Integer.valueOf(12), result.getList().get(0).getParsed());
	}
}
