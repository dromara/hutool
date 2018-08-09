package cn.hutool.db.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.map.MapUtil;
import cn.hutool.db.sql.NamedSql;

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
}
