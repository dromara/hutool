package org.dromara.hutool.db;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.db.ds.DSUtil;
import org.dromara.hutool.db.ds.DSWrapper;
import org.dromara.hutool.db.meta.MetaUtil;
import org.dromara.hutool.db.meta.Table;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI9BANETest {
	@Test
	@Disabled
	void metaTest() {
		final Db db = Db.of("orcl");
		db.find(Entity.of("\"1234\""));

		final DSWrapper ds = DSUtil.getDS("orcl");
		final Table tableMeta = MetaUtil.getTableMeta(ds, null, null, "\"1234\"");
		Console.log(tableMeta.getIndexInfoList());
	}
}
