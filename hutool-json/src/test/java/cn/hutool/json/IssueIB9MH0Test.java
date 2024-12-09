package cn.hutool.json;

import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONObjectSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIB9MH0Test {

	@Test
	void parseTest() {
		GlobalSerializeMapping.put(TabTypeEnum.class, (JSONObjectSerializer<TabTypeEnum>) (json, bean) -> json.set("code", bean.getCode())
			.set("title", bean.getTitle()));
		final JSON parse = JSONUtil.parse(TabTypeEnum._01);
		Assertions.assertEquals("{\"code\":\"tab_people_home\",\"title\":\"首页\"}", parse.toString());
	}

	public enum TabTypeEnum  {
		_01("tab_people_home","首页"),
		_02("tab_people_hospital","医院");

		private String code;
		private String title;

		TabTypeEnum(String code, String title) {
			this.code = code;
			this.title = title;
		}

		public String getCode() {
			return code;
		}

		public String getTitle() {
			return title;
		}
	}
}
