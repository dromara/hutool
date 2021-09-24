package cn.hutool.db;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PicTransferTest {

	@Test
	@Ignore
	public void findTest() throws SQLException {
		Db.use().find(
				ListUtil.of("NAME", "TYPE", "GROUP", "PIC"),
				Entity.create("PIC_INFO").set("TYPE", 1),
				rs -> {
					while(rs.next()){
						save(rs);
					}
					return null;
				}
		);
	}

	private static void save(ResultSet rs) throws SQLException{
		String destDir = "d:/test/pic";
		String path = StrUtil.format("{}/{}-{}.jpg", destDir, rs.getString("NAME"), rs.getString("GROUP"));
		FileUtil.writeFromStream(rs.getBlob("PIC").getBinaryStream(), path);
	}
}
