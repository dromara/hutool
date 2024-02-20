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
