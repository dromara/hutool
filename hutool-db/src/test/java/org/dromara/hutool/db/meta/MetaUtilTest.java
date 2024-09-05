/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.db.meta;

import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.db.ds.DSUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * 元数据信息单元测试
 *
 * @author Looly
 *
 */
public class MetaUtilTest {
	final DataSource ds = DSUtil.getDS("test");

	@Test
	public void getTableNamesTest() {
		final List<String> tables = MetaUtil.getTableNames(ds);
		Assertions.assertTrue(tables.contains("user"));
	}

	@Test
	public void getTableMetaTest() {
		final Table table = MetaUtil.getTableMeta(ds, "user");
		Assertions.assertEquals(SetUtil.of("id"), table.getPkNames());
	}

	@Test
	public void getColumnNamesTest() {
		final String[] names = MetaUtil.getColumnNames(ds, "user");
		Assertions.assertArrayEquals(SplitUtil.splitToArray("id,name,age,birthday,gender", StrUtil.COMMA), names);
	}

	@Test
	public void getTableIndexInfoTest() {
		final Table table = MetaUtil.getTableMeta(ds, "user_1");
		Assertions.assertEquals(table.getIndexInfoList().size(), 2);
	}

	/**
	 * 增加 列顺序字段
	 */
	@Test
	public void getTableColumnTest() {
		final Table table = MetaUtil.getTableMeta(ds, "user");
		System.out.println(table.getColumns());
		Assertions.assertEquals(SetUtil.of("id"), table.getPkNames());
	}
}
