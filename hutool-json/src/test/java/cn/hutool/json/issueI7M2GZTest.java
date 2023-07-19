package cn.hutool.json;

import cn.hutool.core.lang.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/dromara/hutool/issues/I7M2GZ
 */
public class issueI7M2GZTest {

	public static class JSONBeanParserImpl implements JSONBeanParser {
		private String name;
		private Boolean isParsed;

		public Boolean getParsed() {
			return isParsed;
		}

		public void setParsed(Boolean parsed) {
			isParsed = parsed;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public JSONBeanParserImpl() {
		}

		public JSONBeanParserImpl(String name) {
			this.name = name;
		}

		@Override
		public void parse(Object object) {
			setParsed(true);
		}
	}

	public static class MyEntity<T> {
		private List<T> list;

		public List<T> getList() {
			return list;
		}

		public void setList(List<T> list) {
			this.list = list;
		}
	}

	@Test
	public void toListTest() {
		List<JSONBeanParserImpl> list = new ArrayList<>();
		list.add(new JSONBeanParserImpl("Object1"));

		MyEntity<JSONBeanParserImpl> entity = new MyEntity<>();
		entity.setList(list);
		String json = JSONUtil.toJsonStr(entity);
		MyEntity<JSONBeanParserImpl> result = JSONUtil.toBean(json, new TypeReference<MyEntity<JSONBeanParserImpl>>() {
		}, false);
		Assert.assertEquals("Object1", result.getList().get(0).getName());
		Assert.assertNotNull(result.getList().get(0).getParsed());
	}
}
