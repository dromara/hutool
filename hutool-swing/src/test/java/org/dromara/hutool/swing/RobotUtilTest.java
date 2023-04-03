package org.dromara.hutool.swing;

import org.dromara.hutool.core.io.file.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class RobotUtilTest {

	@Test
	@Disabled
	public void captureScreenTest() {
		RobotUtil.captureScreen(FileUtil.file("e:/screen.jpg"));
	}
}
