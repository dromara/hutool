package cn.hutool.db;

import cn.hutool.core.map.MapUtil;
import cn.hutool.db.sql.NamedSql;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class NamedSqlTest {

	@Test
	public void parseTest() {
		String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";

		Map<String, Object> paramMap = MapUtil.builder("name1", (Object)"张三").put("age", 12).put("subName", "小豆豆").build();

		NamedSql namedSql = new NamedSql(sql, paramMap);
		//未指定参数原样输出
		Assert.assertEquals("select * from table where id=@id and name = ? and nickName = ?", namedSql.getSql());
		Assert.assertEquals("张三", namedSql.getParams()[0]);
		Assert.assertEquals("小豆豆", namedSql.getParams()[1]);
	}
	
	@Test
	public void parseTest2() {
		String sql = "select * from table where id=@id and name = @name1 and nickName = :subName";
		
		Map<String, Object> paramMap = MapUtil.builder("name1", (Object)"张三").put("age", 12).put("subName", "小豆豆").put("id", null).build();
		
		NamedSql namedSql = new NamedSql(sql, paramMap);
		Assert.assertEquals("select * from table where id=? and name = ? and nickName = ?", namedSql.getSql());
		//指定了null参数的依旧替换，参数值为null
		Assert.assertNull(namedSql.getParams()[0]);
		Assert.assertEquals("张三", namedSql.getParams()[1]);
		Assert.assertEquals("小豆豆", namedSql.getParams()[2]);
	}

	@Test
	public void queryTest() throws SQLException {
		Map<String, Object> paramMap = MapUtil
				.builder("name1", (Object)"王五")
				.put("age1", 18).build();
		String sql = "select * from user where name = @name1 and age = @age1";

		List<Entity> query = Db.use().query(sql, paramMap);
		Assert.assertEquals(1, query.size());

		// 采用传统方式查询是否能识别Map类型参数
		query = Db.use().query(sql, new Object[]{paramMap});
		Assert.assertEquals(1, query.size());
	}
}
