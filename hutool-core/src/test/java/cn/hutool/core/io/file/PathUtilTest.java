package cn.hutool.core.io.file;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PathUtilTest {

	@Test
	@Ignore
	public void copyFileTest(){
		PathUtil.copyFile(
				Paths.get("d:/test/1595232240113.jpg"),
				Paths.get("d:/test/1595232240113_copy.jpg"),
				StandardCopyOption.COPY_ATTRIBUTES,
				StandardCopyOption.REPLACE_EXISTING
				);
	}
}
