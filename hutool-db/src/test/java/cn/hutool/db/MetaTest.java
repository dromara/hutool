package cn.hutool.db;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;

/**
 * 元数据信息单元测试
 * 
 * @author Looly
 *
 */
public class MetaTest {
	DataSource ds = DSFactory.get("test");

	@Test
	public void getTablesTest() {
		List<String> tables = MetaUtil.getTables(ds);
		Assert.assertEquals("user", tables.get(0));
	}

	@Test
	public void getTableMetaTest() {

		Table table = MetaUtil.getTableMeta(ds, "user");
		Assert.assertEquals(CollectionUtil.newHashSet("id"), table.getPkNames());
	}
}
