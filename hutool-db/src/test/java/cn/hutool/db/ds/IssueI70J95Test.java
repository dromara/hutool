/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.db.ds;

import cn.hutool.setting.Setting;
import org.junit.Test;

public class IssueI70J95Test {

	@Test
	public void getDataSourceTest() {
		final String dbSourceName = "group_db1";

		final Setting dbSetting = new Setting();
		dbSetting.setByGroup("url", dbSourceName, "jdbc:sqlite:test.db");
		dbSetting.setByGroup("username", dbSourceName, "****");
		dbSetting.setByGroup("password", dbSourceName, "***");
		DSFactory.create(dbSetting).getDataSource(dbSourceName);
	}
}
