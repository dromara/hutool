package cn.hutool.db;

import cn.hutool.core.lang.Console;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import org.junit.Ignore;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

public class IssueI9BANETest {
	@Test
	@Ignore
	public void metaTest() throws SQLException {
		final Db db = Db.use("orcl");
		db.find(Entity.create("\"1234\""));

		final DataSource ds = DSFactory.get("orcl");
		final Table tableMeta = MetaUtil.getTableMeta(ds, null, null, "\"1234\"");
		Console.log("remarks: " + tableMeta.getComment());
		Console.log("pks: " + tableMeta.getPkNames());
		Console.log("columns: " + tableMeta.getColumns());
		Console.log("index: " + tableMeta.getIndexInfoList());
	}
}
