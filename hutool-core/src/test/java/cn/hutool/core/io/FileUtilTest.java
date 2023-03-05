package cn.hutool.core.io;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.LineSeparator;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.SystemUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * {@link FileUtil} 单元测试类
 *
 * @author Looly
 */
public class FileUtilTest {

	@Test(expected = IllegalArgumentException.class)
	public void fileTest() {
		final File file = FileUtil.file("d:/aaa", "bbb");
		Assert.assertNotNull(file);

		// 构建目录中出现非子目录抛出异常
		FileUtil.file(file, "../ccc");

		FileUtil.file("E:/");
	}

	@Test
	public void getAbsolutePathTest() {
		final String absolutePath = FileUtil.getAbsolutePath("LICENSE-junit.txt");
		Assert.assertNotNull(absolutePath);
		final String absolutePath2 = FileUtil.getAbsolutePath(absolutePath);
		Assert.assertNotNull(absolutePath2);
		Assert.assertEquals(absolutePath, absolutePath2);

		String path = FileUtil.getAbsolutePath("中文.xml");
		Assert.assertTrue(path.contains("中文.xml"));

		path = FileUtil.getAbsolutePath("d:");
		Assert.assertEquals("d:", path);
	}

	@Test
	@Ignore
	public void touchTest() {
		FileUtil.touch("d:\\tea\\a.jpg");
	}

	@Test
	@Ignore
	public void renameTest() {
		FileUtil.rename(FileUtil.file("d:/test/3.jpg"), "2.jpg", false);
	}

	@Test
	@Ignore
	public void renameTest2() {
		FileUtil.move(FileUtil.file("d:/test/a"), FileUtil.file("d:/test/b"), false);
	}

	@Test
	public void copyTest() {
		final File srcFile = FileUtil.file("hutool.jpg");
		final File destFile = FileUtil.file("hutool.copy.jpg");

		FileUtil.copy(srcFile, destFile, true);

		Assert.assertTrue(destFile.exists());
		Assert.assertEquals(srcFile.length(), destFile.length());
	}

	@Test
	@Ignore
	public void copyDirTest() {
		final File srcFile = FileUtil.file("D:\\test");
		final File destFile = FileUtil.file("E:\\");

		FileUtil.copy(srcFile, destFile, true);
	}

	@Test
	@Ignore
	public void moveDirTest() {
		final File srcFile = FileUtil.file("E:\\test2");
		final File destFile = FileUtil.file("D:\\");

		FileUtil.move(srcFile, destFile, true);
	}

	@Test
	public void equalsTest() {
		// 源文件和目标文件都不存在
		final File srcFile = FileUtil.file("d:/hutool.jpg");
		final File destFile = FileUtil.file("d:/hutool.jpg");

		final boolean equals = FileUtil.equals(srcFile, destFile);
		Assert.assertTrue(equals);

		// 源文件存在，目标文件不存在
		final File srcFile1 = FileUtil.file("hutool.jpg");
		final File destFile1 = FileUtil.file("d:/hutool.jpg");

		final boolean notEquals = FileUtil.equals(srcFile1, destFile1);
		Assert.assertFalse(notEquals);
	}

	@Test
	@Ignore
	public void convertLineSeparatorTest() {
		FileUtil.convertLineSeparator(FileUtil.file("d:/aaa.txt"), CharsetUtil.UTF_8, LineSeparator.WINDOWS);
	}

	@Test
	public void normalizeHomePathTest() {
		final String home = SystemUtil.getUserHomePath().replace('\\', '/');
		Assert.assertEquals(home + "/bar/", FileUtil.normalize("~/foo/../bar/"));
	}

	@Test
	public void normalizeHomePathTest2() {
		final String home = SystemUtil.getUserHomePath().replace('\\', '/');
		// 多个~应该只替换开头的
		Assert.assertEquals(home + "/~bar/", FileUtil.normalize("~/foo/../~bar/"));
	}

	@Test
	public void normalizeClassPathTest() {
		Assert.assertEquals("", FileUtil.normalize("classpath:"));
	}

	@Test
	public void normalizeClassPathTest2() {
		Assert.assertEquals("../a/b.csv", FileUtil.normalize("../a/b.csv"));
		Assert.assertEquals("../../../a/b.csv", FileUtil.normalize("../../../a/b.csv"));
	}

	@Test
	public void doubleNormalizeTest() {
		final String normalize = FileUtil.normalize("/aa/b:/c");
		final String normalize2 = FileUtil.normalize(normalize);
		Assert.assertEquals("/aa/b:/c", normalize);
		Assert.assertEquals(normalize, normalize2);
	}

	@Test
	public void subPathTest() {
		final Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

		Path subPath = FileUtil.subPath(path, 5, 4);
		Assert.assertEquals("eee", subPath.toString());
		subPath = FileUtil.subPath(path, 0, 1);
		Assert.assertEquals("aaa", subPath.toString());
		subPath = FileUtil.subPath(path, 1, 0);
		Assert.assertEquals("aaa", subPath.toString());

		// 负数
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
	public void subPathTest2() {
		String subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb/ccc/");
		Assert.assertEquals("ccc/", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc/");
		Assert.assertEquals("ccc/", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc/test.txt");
		Assert.assertEquals("ccc/test.txt", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb/ccc");
		Assert.assertEquals("ccc", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc");
		Assert.assertEquals("ccc", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb");
		Assert.assertEquals("", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb");
		Assert.assertEquals("", subPath);
	}

	@Test
	public void getPathEle() {
		final Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

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

	@Test
	@Ignore
	public void listFileNamesInJarTest() {
		final List<String> names = FileUtil.listFileNames("d:/test/hutool-core-5.1.0.jar!/cn/hutool/core/util ");
		for (final String name : names) {
			Console.log(name);
		}
	}

	@Test
	@Ignore
	public void listFileNamesTest2() {
		final List<String> names = FileUtil.listFileNames("D:\\m2_repo\\commons-cli\\commons-cli\\1.0\\commons-cli-1.0.jar!org/apache/commons/cli/");
		for (final String string : names) {
			Console.log(string);
		}
	}

	@Test
	@Ignore
	public void loopFilesTest() {
		final List<File> files = FileUtil.loopFiles("d:/");
		for (final File file : files) {
			Console.log(file.getPath());
		}
	}

	@Test
	@Ignore
	public void loopFilesTest2() {
		FileUtil.loopFiles("").forEach(Console::log);
	}

	@Test
	@Ignore
	public void loopFilesWithDepthTest() {
		final List<File> files = FileUtil.loopFiles(FileUtil.file("d:/m2_repo"), 2, null);
		for (final File file : files) {
			Console.log(file.getPath());
		}
	}

	@Test
	public void getParentTest() {
		// 只在Windows下测试
		if (FileUtil.isWindows()) {
			File parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 0);
			Assert.assertEquals(FileUtil.file("d:\\aaa\\bbb\\cc\\ddd"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 1);
			Assert.assertEquals(FileUtil.file("d:\\aaa\\bbb\\cc"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 2);
			Assert.assertEquals(FileUtil.file("d:\\aaa\\bbb"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 4);
			Assert.assertEquals(FileUtil.file("d:\\"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 5);
			Assert.assertNull(parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 10);
			Assert.assertNull(parent);
		}
	}

	@Test
	public void lastIndexOfSeparatorTest() {
		final String dir = "d:\\aaa\\bbb\\cc\\ddd";
		final int index = FileUtil.lastIndexOfSeparator(dir);
		Assert.assertEquals(13, index);

		final String file = "ddd.jpg";
		final int index2 = FileUtil.lastIndexOfSeparator(file);
		Assert.assertEquals(-1, index2);
	}

	@Test
	public void getNameTest() {
		String path = "d:\\aaa\\bbb\\cc\\ddd\\";
		String name = FileNameUtil.getName(path);
		Assert.assertEquals("ddd", name);

		path = "d:\\aaa\\bbb\\cc\\ddd.jpg";
		name = FileNameUtil.getName(path);
		Assert.assertEquals("ddd.jpg", name);
	}

	@Test
	public void mainNameTest() {
		String path = "d:\\aaa\\bbb\\cc\\ddd\\";
		String mainName = FileNameUtil.mainName(path);
		Assert.assertEquals("ddd", mainName);

		path = "d:\\aaa\\bbb\\cc\\ddd";
		mainName = FileNameUtil.mainName(path);
		Assert.assertEquals("ddd", mainName);

		path = "d:\\aaa\\bbb\\cc\\ddd.jpg";
		mainName = FileNameUtil.mainName(path);
		Assert.assertEquals("ddd", mainName);
	}

	@Test
	public void extNameTest() {
		String path =  FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd\\" : "~/Desktop/hutool/ddd/";
		String mainName = FileNameUtil.extName(path);
		Assert.assertEquals("", mainName);

		path =  FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd" : "~/Desktop/hutool/ddd";
		mainName = FileNameUtil.extName(path);
		Assert.assertEquals("", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd.jpg" : "~/Desktop/hutool/ddd.jpg";
		mainName = FileNameUtil.extName(path);
		Assert.assertEquals("jpg", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.xlsx" : "~/Desktop/hutool/fff.xlsx";
		mainName = FileNameUtil.extName(path);
		Assert.assertEquals("xlsx", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.gz" : "~/Desktop/hutool/fff.tar.gz";
		mainName = FileNameUtil.extName(path);
		Assert.assertEquals("tar.gz", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.Z" : "~/Desktop/hutool/fff.tar.Z";
		mainName = FileNameUtil.extName(path);
		Assert.assertEquals("tar.Z", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.bz2" : "~/Desktop/hutool/fff.tar.bz2";
		mainName = FileNameUtil.extName(path);
		Assert.assertEquals("tar.bz2", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.xz" : "~/Desktop/hutool/fff.tar.xz";
		mainName = FileNameUtil.extName(path);
		Assert.assertEquals("tar.xz", mainName);
	}

	@Test
	public void getWebRootTest() {
		final File webRoot = FileUtil.getWebRoot();
		Assert.assertNotNull(webRoot);
		Assert.assertEquals("hutool-core", webRoot.getName());
	}

	@Test
	public void getMimeTypeTest() {
		String mimeType = FileUtil.getMimeType("test2Write.jpg");
		Assert.assertEquals("image/jpeg", mimeType);

		mimeType = FileUtil.getMimeType("test2Write.html");
		Assert.assertEquals("text/html", mimeType);

		mimeType = FileUtil.getMimeType("main.css");
		Assert.assertEquals("text/css", mimeType);

		mimeType = FileUtil.getMimeType("test.js");
		Assert.assertEquals("application/x-javascript", mimeType);

		// office03
		mimeType = FileUtil.getMimeType("test.doc");
		Assert.assertEquals("application/msword", mimeType);
		mimeType = FileUtil.getMimeType("test.xls");
		Assert.assertEquals("application/vnd.ms-excel", mimeType);
		mimeType = FileUtil.getMimeType("test.ppt");
		Assert.assertEquals("application/vnd.ms-powerpoint", mimeType);

		// office07+
		mimeType = FileUtil.getMimeType("test.docx");
		Assert.assertEquals("application/vnd.openxmlformats-officedocument.wordprocessingml.document", mimeType);
		mimeType = FileUtil.getMimeType("test.xlsx");
		Assert.assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", mimeType);
		mimeType = FileUtil.getMimeType("test.pptx");
		Assert.assertEquals("application/vnd.openxmlformats-officedocument.presentationml.presentation", mimeType);

		// pr#2617@Github
		mimeType = FileUtil.getMimeType("test.wgt");
		Assert.assertEquals("application/widget", mimeType);
	}

	@Test
	public void isSubTest() {
		final File file = new File("d:/test");
		final File file2 = new File("d:/test2/aaa");
		Assert.assertFalse(FileUtil.isSub(file, file2));
	}

	@Test
	public void isSubRelativeTest() {
		final File file = new File("..");
		final File file2 = new File(".");
		Assert.assertTrue(FileUtil.isSub(file, file2));
	}

	@Test
	@Ignore
	public void appendLinesTest(){
		final List<String> list = ListUtil.of("a", "b", "c");
		FileUtil.appendLines(list, FileUtil.file("d:/test/appendLines.txt"), CharsetUtil.UTF_8);
	}

	@Test
	@Ignore
	public void createTempFileTest(){
		final File nullDirTempFile = FileUtil.createTempFile();
		Assert.assertTrue(nullDirTempFile.exists());

		final File suffixDirTempFile = FileUtil.createTempFile(".xlsx",true);
		Assert.assertEquals("xlsx", FileNameUtil.getSuffix(suffixDirTempFile));

		final File prefixDirTempFile = FileUtil.createTempFile("prefix",".xlsx",true);
		Assert.assertTrue(FileNameUtil.getPrefix(prefixDirTempFile).startsWith("prefix"));
	}

	@Test
	@Ignore
	public void getTotalLinesTest() {
		// 千万行秒级内返回
		final int totalLines = FileUtil.getTotalLines(FileUtil.file(""));
		Assert.assertEquals(10000000, totalLines);
	}

	@Test
	public void isAbsolutePathTest(){
		String path = "d:/test\\aaa.txt";
		Assert.assertTrue(FileUtil.isAbsolutePath(path));

		path = "test\\aaa.txt";
		Assert.assertFalse(FileUtil.isAbsolutePath(path));
	}
}
