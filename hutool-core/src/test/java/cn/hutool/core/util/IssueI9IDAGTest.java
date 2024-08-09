package cn.hutool.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class IssueI9IDAGTest {
	@Test
	@Disabled
	public void loopFilesTest() {
		final List<File> files = FileUtil.loopFiles("d:/m2_repo");
		files.forEach(Console::log);
	}
}
