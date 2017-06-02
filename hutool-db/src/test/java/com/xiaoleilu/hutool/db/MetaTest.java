package com.xiaoleilu.hutool.db;
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
	
	@Test
	public void getTableMetaTest(){
		DataSource ds = DSFactory.get();
		Table table = DbUtil.getTableMeta(ds, "user");
		Assert.assertEquals(CollectionUtil.newHashSet("id"), table.getPkNames());
	}
}
