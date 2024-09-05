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

package org.dromara.hutool.poi.csv;

import lombok.Data;
import org.dromara.hutool.core.annotation.Alias;
import org.dromara.hutool.core.io.file.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class IssueI91VF1Test {
	@Test
	public void csvReadTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<DeviceVO> read = reader.read(FileUtil.getUtf8Reader("issueI91VF1.csv"), true, DeviceVO.class);
		final DeviceVO deviceVO = read.get(0);
		Assertions.assertEquals("192.168.1.1", deviceVO.getDeviceIp());
		Assertions.assertEquals("admin", deviceVO.getUsername());
		Assertions.assertEquals("123", deviceVO.getPassword());
	}

	@Data
	static class DeviceVO {
		@Alias("主机")
		private String deviceIp;
		@Alias("用户名")
		private String username;
		@Alias("密码")
		private String password;
	}
}
