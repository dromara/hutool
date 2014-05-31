package hutool;

import java.sql.SQLException;
import java.util.List;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.SqlRunner;
import com.xiaoleilu.hutool.db.dialect.DialectFactory;
import com.xiaoleilu.hutool.db.ds.DruidDS;
import com.xiaoleilu.hutool.db.handler.EntityHandler;


public class Test2 {
	public static void main(String[] args) throws SQLException {
		SqlRunner runner = DbUtil.newSqlRunner(DruidDS.getDataSource("test"), DialectFactory.newDialect(DialectFactory.DRIVER_MYSQL));
		Entity e = Entity.create("gs_epg")
//				.add("domain", "tv.cntv.new")
//				.add("source_id", 1)
				.add("site_id", 1);
//				.add("channel", 1)
//				.add("entity_id", 1)
//				.add("title", "aaaa")
//				.add("play_time", "bbb")
//				.add("index", 1);
//		Entity where = Entity.create("gs_epg").add("playdate", "2014-05-26");
		List<Entity> list = runner.page(null, e, 0, 5, new EntityHandler());
		for (Entity entity : list) {
			System.out.println(entity);
		}
	}
}
