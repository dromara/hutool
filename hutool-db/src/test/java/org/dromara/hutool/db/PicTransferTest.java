/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.db;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
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
		final String destDir = "d:/test/shape";
		final String path = StrUtil.format("{}/{}-{}.jpg", destDir, rs.getString("NAME"), rs.getString("GROUP"));
		FileUtil.copy(rs.getBlob("PIC").getBinaryStream(), FileUtil.file(path));
	}
}
