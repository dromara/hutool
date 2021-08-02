package cn.hutool.core.io.file;

import org.junit.Assert;
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

	@Test
	@Ignore
	public void copyTest(){
		PathUtil.copy(
				Paths.get("d:/Red2_LYY"),
				Paths.get("d:/test/aaa/aaa.txt")
		);
	}

	@Test
	@Ignore
	public void copyContentTest(){
		PathUtil.copyContent(
				Paths.get("d:/Red2_LYY"),
				Paths.get("d:/test/aaa/")
		);
	}

	@Test
	@Ignore
	public void moveTest(){
		PathUtil.move(Paths.get("d:/lombok.jar"), Paths.get("d:/test/"), false);
	}

	@Test
	@Ignore
	public void moveDirTest(){
		PathUtil.move(Paths.get("c:\\aaa"), Paths.get("d:/test/looly"), false);
	}

	@Test
	@Ignore
	public void delDirTest(){
		PathUtil.del(Paths.get("d:/test/looly"));
	}

	@Test
	@Ignore
	public void getMimeTypeTest(){
		String mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.jpg"));
		Assert.assertEquals("image/jpeg", mimeType);

		mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.mov"));
		Assert.assertEquals("video/quicktime", mimeType);
	}
}
