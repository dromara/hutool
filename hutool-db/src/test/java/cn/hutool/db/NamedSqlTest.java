package cn.hutool.db;

import cn.hutool.core.map.MapUtil;
import cn.hutool.db.sql.NamedSql;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamedSqlTest {

	@Test
	public void parseTest() {
		String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		//未指定参数原样输出
		Assertions.assertEquals("select * from table where id=@id and name = ? and nickName = ?", namedSql.getSql());
		Assertions.assertEquals("张三", namedSql.getParams()[0]);
		Assertions.assertEquals("小豆豆", namedSql.getParams()[1]);
	}

	@Test
	public void parseTest2() {
		String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.put("age", 12)
				.put("subName", "小豆豆")
				.put("id", null)
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals("select * from table where id=? and name = ? and nickName = ?", namedSql.getSql());
		//指定了null参数的依旧替换，参数值为null
		Assertions.assertNull(namedSql.getParams()[0]);
		Assertions.assertEquals("张三", namedSql.getParams()[1]);
		Assertions.assertEquals("小豆豆", namedSql.getParams()[2]);
	}

	@Test
	public void parseTest3() {
		// 测试连续变量名出现是否有问题
		String sql = "SELECT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as sysdate FROM dual";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseTest4() {
		// 测试postgre中形如data_value::numeric是否出错
		String sql = "select device_key, min(data_value::numeric) as data_value from device";

		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"张三")
				.build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals(sql, namedSql.getSql());
	}

	@Test
	public void parseInTest(){
		String sql = "select * from user where id in (:ids)";
		final HashMap<String, Object> paramMap = MapUtil.of("ids", new int[]{1, 2, 3});

		NamedSql namedSql = new NamedSql(sql, paramMap);
		Assertions.assertEquals("select * from user where id in (?,?,?)", namedSql.getSql());
		Assertions.assertEquals(1, namedSql.getParams()[0]);
		Assertions.assertEquals(2, namedSql.getParams()[1]);
		Assertions.assertEquals(3, namedSql.getParams()[2]);
	}

	@Test
	public void queryTest() throws SQLException {
		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"王五")
				.put("age1", 18).build();
		String sql = "select * from user where name = @name1 and age = @age1";

		List<Entity> query = Db.use().query(sql, paramMap);
		Assertions.assertEquals(1, query.size());

		// 采用传统方式查询是否能识别Map类型参数
		query = Db.use().query(sql, new Object[]{paramMap});
		Assertions.assertEquals(1, query.size());
	}
}
