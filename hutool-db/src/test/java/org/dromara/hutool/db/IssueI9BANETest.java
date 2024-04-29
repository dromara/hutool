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
		final DSWrapper ds = DSUtil.getDS("orcl");
		final Table tableMeta = MetaUtil.getTableMeta(ds, null, null, "\"1234\"");
		Console.log("remarks: " + tableMeta.getRemarks());
		Console.log("pks: " + tableMeta.getPkNames());
		Console.log("columns: " + tableMeta.getColumns());
		Console.log("index: " + tableMeta.getIndexInfoList());
	}
}
