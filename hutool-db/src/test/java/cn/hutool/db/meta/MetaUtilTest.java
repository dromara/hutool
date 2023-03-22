package cn.hutool.db.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.ds.DSFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * 元数据信息单元测试
 *
 * @author Looly
 *
 */
public class MetaUtilTest {
	DataSource ds = DSFactory.get("test");

	@Test
	public void getTablesTest() {
		final List<String> tables = MetaUtil.getTables(ds);
		Assert.assertEquals("user", tables.get(0));
	}

	@Test
	public void getTableMetaTest() {
		final Table table = MetaUtil.getTableMeta(ds, "user");
		Assert.assertEquals(CollectionUtil.newHashSet("id"), table.getPkNames());
	}

	@Test
	public void getColumnNamesTest() {
		final String[] names = MetaUtil.getColumnNames(ds, "user");
		Assert.assertArrayEquals(StrUtil.splitToArray("id,name,age,birthday,gender", ','), names);
	}

	@Test
	public void getTableIndexInfoTest() {
		final Table table = MetaUtil.getTableMeta(ds, "user_1");
		Assert.assertEquals(table.getIndexInfoList().size(), 2);
	}

	/**
	 * 表不存在抛出异常。
	 */
	@Test(expected = DbRuntimeException.class)
	public void getTableNotExistTest() {
		final Table table = MetaUtil.getTableMeta(ds, "user_not_exist");
		Assert.assertEquals(table.getIndexInfoList().size(), 2);
	}
}
