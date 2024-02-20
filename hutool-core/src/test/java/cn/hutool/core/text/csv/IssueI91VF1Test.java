package cn.hutool.core.text.csv;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.io.FileUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class IssueI91VF1Test {
	@Test
	public void csvReadTest() {
		final CsvReader reader = CsvUtil.getReader();
		final List<DeviceVO> read = reader.read(FileUtil.getUtf8Reader("issueI91VF1.csv"), DeviceVO.class);
		final DeviceVO deviceVO = read.get(0);
		Assert.assertEquals("192.168.1.1", deviceVO.getDeviceIp());
		Assert.assertEquals("admin", deviceVO.getUsername());
		Assert.assertEquals("123", deviceVO.getPassword());
	}

	@Data
	static class DeviceVO{
		@Alias("主机")
		private String deviceIp;
		@Alias("用户名")
		private String username;
		@Alias("密码")
		private String password;
	}
}
