package cn.hutool.core.io.file;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;

public class PathDeleterTest {
	@Test
	@Ignore
	public void delFileTest() {
		FileUtil.touch("d:/test/exist.txt");
		PathUtil.del(Paths.get("d:/test/exist.txt"));
	}

	@Test
	@Ignore
	public void delDirTest() {
		PathUtil.del(Paths.get("d:/test/dir1"));
	}

	@Test
	@Ignore
	public void cleanDirTest() {
		PathUtil.clean(Paths.get("d:/test/dir1"));
	}
}
