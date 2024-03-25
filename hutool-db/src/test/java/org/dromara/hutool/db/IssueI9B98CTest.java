package org.dromara.hutool.db;

import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.config.SettingConfigParser;
import org.dromara.hutool.setting.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI9B98CTest {

	@Test
	void settingConfigParserTest() {
		final SettingConfigParser settingConfigParser = SettingConfigParser.of(new Setting("config/issueI9B98C.setting"));
		final DbConfig config = settingConfigParser.parse(null);
		Assertions.assertEquals("jdbc:sqlite:test.db", config.getUrl());
		Assertions.assertEquals("true", config.getConnProps().getProperty("remarks"));
	}
}
