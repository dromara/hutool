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
	public void getTablesTest() {
		final List<String> tables = MetaUtil.getTables(ds);
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
}
