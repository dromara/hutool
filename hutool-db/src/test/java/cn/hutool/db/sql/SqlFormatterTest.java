package cn.hutool.db.sql;

import org.junit.Test;

public class SqlFormatterTest {

	@Test
	public void formatTest(){
		// issue#I3XS44@Gitee
		// 测试是否空指针错误
		String sql = "(select 1 from dual) union all (select 1 from dual)";
		SqlFormatter.format(sql);
	}

	@Test
	public void testKeyword() {
		String sql = "select * from `order`";
		String format = SqlFormatter.format(sql);
		System.out.println(format);
	}

	@Test
	public void testSqlBuilderFormat() {
		String sql = "SELECT `link_table_a`.`value_a` AS `link_table_a.value_a`,`link_table_a`.`id` AS `link_table_a.id`,`link_table_b`.`value_b` AS `link_table_b.value_b`,`link_table_c`.`id` AS `link_table_c.id`,`link_table_b`.`id` AS `link_table_b.id`,`link_table_c`.`value_c` AS `link_table_c.value_c` FROM `link_table_a` INNER JOIN `link_table_b` ON `link_table_a`.`table_b_id` = `link_table_b`.`id` INNER JOIN `link_table_c` ON `link_table_b`.`table_c_id` = `link_table_c`.`id`";
		String format = SqlBuilder.of(sql).format().build();
		System.out.println(format);
	}
}
