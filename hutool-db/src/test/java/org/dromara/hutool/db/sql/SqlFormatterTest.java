package org.dromara.hutool.db.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SqlFormatterTest {

	@Test
	public void formatTest(){
		// issue#I3XS44@Gitee
		// 测试是否空指针错误
		final String sql = "(select 1 from dual) union all (select 1 from dual)";
		SqlFormatter.format(sql);
	}

	@Test
	public void issue3246Test() {
		final String sql = "select * from `order`";
		final String format = SqlFormatter.format(sql);
		Assertions.assertEquals(
			"select\n" +
			"        * \n" +
			"    from\n" +
			"        `order`", format);
	}
}
