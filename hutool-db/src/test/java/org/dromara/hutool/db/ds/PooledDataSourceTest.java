package org.dromara.hutool.db.ds;

import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.config.SettingConfigParser;
import org.dromara.hutool.db.ds.pooled.PooledDSFactory;
import org.dromara.hutool.setting.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class PooledDataSourceTest {
	@Test
	void getConnTest() {
		final DSPool dsPool = new DSPool(
			new SettingConfigParser(new Setting("config/db.setting")),
			new PooledDSFactory());

		final DSWrapper test = dsPool.getDataSource("test");
		Assertions.assertEquals("org.dromara.hutool.db.ds.pooled.PooledDataSource", test.getRaw().getClass().getName());
		for (int i = 0; i < 1000; i++) {
			try {
				test.getConnection().close();
			} catch (final SQLException e) {
				throw new DbException(e);
			}
		}

	}
}
