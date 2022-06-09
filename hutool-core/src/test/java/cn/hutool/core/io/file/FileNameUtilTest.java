package cn.hutool.core.io.file;

import org.junit.Assert;
import org.junit.Test;

public class FileNameUtilTest {

	@Test
	public void test(){
		String filename = "CON";
		Assert.assertTrue(FileNameUtil.matchInvalidFilename(filename));

		String filename2 = "CON9";
		Assert.assertFalse(FileNameUtil.matchInvalidFilename(filename2));

		String filename3 = "LPT8";
		Assert.assertTrue(FileNameUtil.matchInvalidFilename(filename3));

		String filename4 = "LPT8   ";
		Assert.assertTrue(FileNameUtil.matchInvalidFilename(filename4));

		String filename5 = "LPT88";
		Assert.assertFalse(FileNameUtil.matchInvalidFilename(filename5));

		String filename6 = "con";
		Assert.assertFalse(FileNameUtil.matchInvalidFilename(filename6));

		String filename7 = "abcde";
		Assert.assertFalse(FileNameUtil.matchInvalidFilename(filename7));
	}

}
