package org.dromara.hutool.json.issues;

import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.json.serializer.JSONSerializer;
import org.dromara.hutool.json.serializer.TypeAdapterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIB9MH0Test {
	@Test
	void parseTest() {
		// 自定义序列化
		TypeAdapterManager.getInstance().register(TabTypeEnum.class, (JSONSerializer<TabTypeEnum>) (bean, context) ->
			context.getOrCreateObj().putValue("code", bean.getCode()).putValue("title", bean.getTitle()));

		final JSON parse = JSONUtil.parse(TabTypeEnum._01);
		Assertions.assertEquals("{\"code\":\"tab_people_home\",\"title\":\"首页\"}", parse.toString());
	}

	public enum TabTypeEnum  {
		_01("tab_people_home","首页"),
		_02("tab_people_hospital","医院");

		private String code;
		private String title;

		TabTypeEnum(final String code, final String title) {
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
