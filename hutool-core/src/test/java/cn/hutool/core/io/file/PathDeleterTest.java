package cn.hutool.core.io.file;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class PathDeleterTest {
	@Test
	@Disabled
	public void delFileTest() {
		FileUtil.touch("d:/test/exist.txt");
		PathUtil.del(Paths.get("d:/test/exist.txt"));
	}

	@Test
	@Disabled
	public void delDirTest() {
		PathUtil.del(Paths.get("d:/test/dir1"));
	}

	@Test
	@Disabled
	public void cleanDirTest() {
		PathUtil.clean(Paths.get("d:/test/dir1"));
	}
}
