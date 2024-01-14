/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.db.sql.NamedSql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamedSqlTest {

	@Test
	public void parseTest() {
		final String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		//未指定参数原样输出
		Assertions.assertEquals("select * from table where id=@id and name = ? and nickName = ?", namedSql.getSql());
		Assertions.assertEquals("张三", namedSql.getParamArray()[0]);
		Assertions.assertEquals("小豆豆", namedSql.getParamArray()[1]);
	}

	@Test
	public void parseTest2() {
		final String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.put("id", null)
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals("select * from table where id=? and name = ? and nickName = ?", namedSql.getSql());
		//指定了null参数的依旧替换，参数值为null
		Assertions.assertNull(namedSql.getParamArray()[0]);
		Assertions.assertEquals("张三", namedSql.getParamArray()[1]);
		Assertions.assertEquals("小豆豆", namedSql.getParamArray()[2]);
	}

	@Test
	public void parseTest3() {
		// 测试连续变量名出现是否有问题
		final String sql = "SELECT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as sysdate FROM dual";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseTest4() {
		// 测试postgre中形如data_value::numeric是否出错
		final String sql = "select device_key, min(data_value::numeric) as data_value from device";

		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseInTest(){
		final String sql = "select * from user where id in (:ids)";
		final HashMap<String, Object> paramMap = MapUtil.of("ids", new int[]{1, 2, 3});

		final NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals("select * from user where id in (?,?,?)", namedSql.getSql());
		Assertions.assertEquals(1, namedSql.getParamArray()[0]);
		Assertions.assertEquals(2, namedSql.getParamArray()[1]);
		Assertions.assertEquals(3, namedSql.getParamArray()[2]);
	}

	@Test
	public void queryTest() {
		final Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"王五")
				.put("age1", 18).build();
		final String sql = "select * from user where name = @name1 and age = @age1";

		List<Entity> query = Db.of().query(sql, paramMap);
		Assertions.assertEquals(1, query.size());

		// 采用传统方式查询是否能识别Map类型参数
		query = Db.of().query(sql, new Object[]{paramMap});
		Assertions.assertEquals(1, query.size());
	}
}
