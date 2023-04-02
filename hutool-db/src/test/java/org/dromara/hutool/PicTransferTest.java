package org.dromara.hutool;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.text.StrUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PicTransferTest {

	@Test
	@Disabled
	public void findTest() {
		Db.of().find(
				ListUtil.view("NAME", "TYPE", "GROUP", "PIC"),
				Entity.of("PIC_INFO").set("TYPE", 1),
				rs -> {
					while(rs.next()){
						save(rs);
					}
					return null;
				}
		);
	}

	private static void save(final ResultSet rs) throws SQLException{
		final String destDir = "d:/test/pic";
		final String path = StrUtil.format("{}/{}-{}.jpg", destDir, rs.getString("NAME"), rs.getString("GROUP"));
		FileUtil.writeFromStream(rs.getBlob("PIC").getBinaryStream(), path);
	}
}
