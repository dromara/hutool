package com.xiaoleilu.hutool.io;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.io.file.LineSeparator;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * {@link FileUtil} 单元测试类
 * 
 * @author Looly
 */
public class FileUtilTest {
	
	@Test
	public void getAbsolutePathTest(){
		String absolutePath = FileUtil.getAbsolutePath("LICENSE-junit.txt");
		String absolutePath2 = FileUtil.getAbsolutePath(absolutePath);
		Assert.assertNotNull(absolutePath2);
		Assert.assertEquals(absolutePath, absolutePath2);
	}
	
	@Test
	public void getAbsolutePathTest2() {
		String path = FileUtil.getAbsolutePath("中文.xml");
		Assert.assertTrue(path.contains("中文.xml"));
	}
	
	@Test
	public void getAbsolutePathTest3() {
		String path = FileUtil.getAbsolutePath("../../LICENSE-junit.txt");
		Console.log(path);
	}
	
	@Test
	@Ignore
	public void touchTest() {
		FileUtil.touch("d:\\tea\\a.jpg");
	}
	
	@Test
	public void delTest() {
		//删除一个不存在的文件，应返回false
		boolean result = FileUtil.del("e:/Hutool_test_3434543533409843.txt");
		Assert.assertFalse(result);
	}
	
	@Test
	@Ignore
	public void renameTest() {
		FileUtil.rename(FileUtil.file("hutool.jpg"), "b.png", false, false);
	}

	@Test
	public void copyTest() throws Exception {
		File srcFile = FileUtil.file("hutool.jpg");
		File destFile = FileUtil.file("hutool.copy.jpg");

		FileUtil.copy(srcFile, destFile, true);
		
		Assert.assertTrue(destFile.exists());
		Assert.assertEquals(srcFile.length(), destFile.length());
	}
	
	@Test
	public void equlasTest() {
		//源文件和目标文件都不存在
		File srcFile = FileUtil.file("d:/hutool.jpg");
		File destFile = FileUtil.file("d:/hutool.jpg");
		
		boolean equals = FileUtil.equals(srcFile, destFile);
		Assert.assertTrue(equals);
		
		//源文件存在，目标文件不存在
		File srcFile1 = FileUtil.file("hutool.jpg");
		File destFile1 = FileUtil.file("d:/hutool.jpg");
		
		boolean notEquals = FileUtil.equals(srcFile1, destFile1);
		Assert.assertFalse(notEquals);
	}
	
	@Test
	@Ignore
	public void convertLineSeparatorTest() {
		FileUtil.convertLineSeparator(FileUtil.file("d:/aaa.txt"), CharsetUtil.CHARSET_UTF_8, LineSeparator.WINDOWS);
	}
	
	@Test
	public void normalizeTest() {
		Assert.assertEquals("/foo/", FileUtil.normalize("/foo//"));
		Assert.assertEquals("/foo/", FileUtil.normalize("/foo/./"));
		Assert.assertEquals("/bar", FileUtil.normalize("/foo/../bar"));
		Assert.assertEquals("/bar/", FileUtil.normalize("/foo/../bar/"));
		Assert.assertEquals("/baz", FileUtil.normalize("/foo/../bar/../baz"));
		Assert.assertEquals("/", FileUtil.normalize("/../"));
		Assert.assertEquals("foo", FileUtil.normalize("foo/bar/.."));
		Assert.assertEquals("bar", FileUtil.normalize("foo/../../bar"));
		Assert.assertEquals("bar", FileUtil.normalize("foo/../bar"));
		Assert.assertEquals("/server/bar", FileUtil.normalize("//server/foo/../bar"));
		Assert.assertEquals("/bar", FileUtil.normalize("//server/../bar"));
		Assert.assertEquals("C:/bar", FileUtil.normalize("C:\\foo\\..\\bar"));
		Assert.assertEquals("C:/bar", FileUtil.normalize("C:\\..\\bar"));
		Assert.assertEquals("~/bar/", FileUtil.normalize("~/foo/../bar/"));
		Assert.assertEquals("bar", FileUtil.normalize("~/../bar"));
		Assert.assertEquals("bar", FileUtil.normalize("../../bar"));
		Assert.assertEquals("C:/bar", FileUtil.normalize("/C:/bar"));
	}
	
	@Test
	public void normalizeClassPathTest() {
		Assert.assertEquals("", FileUtil.normalize("classpath:"));
	}
	
	@Test
	public void doubleNormalizeTest() {
		String normalize = FileUtil.normalize("/aa/b:/c");
		String normalize2 = FileUtil.normalize(normalize);
		Assert.assertEquals("/aa/b:/c", normalize);
		Assert.assertEquals(normalize, normalize2);
	}
	
	@Test
	public void subPathTest() {
		Path path = Paths.get("d:/aaa/bbb/ccc/ddd/eee/fff");
		
		Path subPath = FileUtil.subPath(path, 5, 4);
		Assert.assertEquals("eee", subPath.toString());
		subPath = FileUtil.subPath(path, 0, 1);
		Assert.assertEquals("aaa", subPath.toString());
		subPath = FileUtil.subPath(path, 1, 0);
		Assert.assertEquals("aaa", subPath.toString());
		
		//负数
		subPath = FileUtil.subPath(path, -1, 0);
		Assert.assertEquals("aaa/bbb/ccc/ddd/eee", subPath.toString().replace('\\', '/'));
		subPath = FileUtil.subPath(path, -1, Integer.MAX_VALUE);
		Assert.assertEquals("fff", subPath.toString());
		subPath = FileUtil.subPath(path, -1, path.getNameCount());
		Assert.assertEquals("fff", subPath.toString());
		subPath = FileUtil.subPath(path, -2, -3);
		Assert.assertEquals("ddd", subPath.toString());
	}
	
	@Test
	public void getPathEle() {
		Path path = Paths.get("d:/aaa/bbb/ccc/ddd/eee/fff");
		
		Path ele = FileUtil.getPathEle(path, -1);
		Assert.assertEquals("fff", ele.toString());
		ele = FileUtil.getPathEle(path, 0);
		Assert.assertEquals("aaa", ele.toString());
		ele = FileUtil.getPathEle(path, -5);
		Assert.assertEquals("bbb", ele.toString());
		ele = FileUtil.getPathEle(path, -6);
		Assert.assertEquals("aaa", ele.toString());
	}
	
	@Test
	public void listFileNamesTest() {
		List<String> names = FileUtil.listFileNames("classpath:");
		Assert.assertTrue(names.contains("hutool.jpg"));
		
		names = FileUtil.listFileNames("");
		Assert.assertTrue(names.contains("hutool.jpg"));
		
		names = FileUtil.listFileNames(".");
		Assert.assertTrue(names.contains("hutool.jpg"));
	}
}
