package org.dromara.hutool.setting;

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIB1I8PTest {
	@Test
	void loadTest() {
		final SettingLoader loader = new SettingLoader(CharsetUtil.UTF_8, true);
		loader.setValueEditor((group, key, value)->{
			if("pass".equals(key)){
				return "pass" + value;
			}
			return value;
		});
		final Setting setting = new Setting(ResourceUtil.getResource("test.setting"), loader);
		Assertions.assertEquals("pass123456", setting.getStrByGroup("pass", "demo"));
	}
}
