package cn.hutool.db;

import cn.hutool.core.lang.Console;
import cn.hutool.db.sql.Query;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Oracle操作单元测试
 * 
 * @author looly
 *
 */
public class OracleTest {

	@Test
	public void oraclePageSqlTest() {
		Page page = new Page(0, 10);
		Entity where = Entity.create("PMCPERFORMANCEINFO").set("yearPI", "2017");
		final Query query = new Query(SqlUtil.buildConditions(where), where.getTableName());
		query.setPage(page);

		SqlBuilder find = SqlBuilder.create(null).query(query).orderBy(page.getOrders());
		final int[] startEnd = page.getStartEnd();
		SqlBuilder builder = SqlBuilder.create(null).append("SELECT * FROM ( SELECT row_.*, rownum rownum_ from ( ")//
				.append(find)//
				.append(" ) row_ where rownum <= ").append(startEnd[1])//
				.append(") table_alias")//
				.append(" where table_alias.rownum_ >= ").append(startEnd[0]);//

		String ok = "SELECT * FROM "//
				+ "( SELECT row_.*, rownum rownum_ from ( SELECT * FROM PMCPERFORMANCEINFO WHERE yearPI = ? ) row_ "//
				+ "where rownum <= 10) table_alias where table_alias.rownum_ >= 0";//
		Assert.assertEquals(ok, builder.toString());
	}

	@Test
	@Ignore
	public void insertTest() throws SQLException {
		for (int id = 100; id < 200; id++) {
			Db.use("orcl").insert(Entity.create("T_USER")//
					.set("ID", id)//
					.set("name", "测试用户" + id)//
					.set("TEXT", "描述" + id)//
					.set("TEST1", "t" + id)//
			);
		}
	}

	@Test
	@Ignore
	public void pageTest() throws SQLException {
		PageResult<Entity> result = Db.use("orcl").page(Entity.create("T_USER"), new Page(2, 10));
		for (Entity entity : result) {
			Console.log(entity.get("ID"));
		}
	}

}
