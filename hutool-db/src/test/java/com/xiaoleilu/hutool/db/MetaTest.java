package com.xiaoleilu.hutool.db;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.db.meta.Table;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 元数据信息单元测试
 * @author Looly
 *
 */
public class MetaTest {
	DataSource ds = DSFactory.get();
	
	@Test
	public void getTablesTest(){
		List<String> tables = DbUtil.getTables(ds);
		Assert.assertEquals("user", tables.get(1));
	}
	
	@Test
	public void getTableMetaTest(){
		
		Table table = DbUtil.getTableMeta(ds, "user");
		Assert.assertEquals(CollectionUtil.newHashSet("id"), table.getPkNames());
	}
}
