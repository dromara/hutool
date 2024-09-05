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

package org.dromara.hutool.poi.excel.reader;

import lombok.Data;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * issue#1729@Github<br>
 * 日期为空时返回""而非null，因此会导致日期等字段的转换错误，此处转bean时忽略错误。
 */
public class Issue1729Test {

	@Test
	public void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("UserProjectDO.xlsx");
		final List<UserProjectDO> read = reader.read(0, 1, UserProjectDO.class);
		Assertions.assertEquals("aa", read.get(0).getProjectName());
		Assertions.assertNull(read.get(0).getEndTrainTime());
		Assertions.assertEquals("2020-02-02", read.get(0).getEndTestTime().toString());
	}

	@Data
	public static class UserProjectDO {
		private String projectName;
		private java.sql.Date endTrainTime;
		private java.sql.Date endTestTime;
	}
}
