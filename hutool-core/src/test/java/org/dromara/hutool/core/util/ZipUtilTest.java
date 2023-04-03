package org.dromara.hutool.core.util;

import org.dromara.hutool.core.compress.ZipReader;
import org.dromara.hutool.core.compress.ZipUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * {@link ZipUtil}单元测试
 * @author Looly
 *
 */
public class ZipUtilTest {

	@Test
	public void appendTest() throws IOException {
		final File appendFile = FileUtil.file("test-zip/addFile.txt");
		final File zipFile = FileUtil.file("test-zip/test.zip");

		// 用于测试完成后将被测试文件恢复
		final File tempZipFile = FileUtil.createTempFile(FileUtil.file("test-zip"));
		tempZipFile.deleteOnExit();
		FileUtil.copy(zipFile, tempZipFile, true);

		// test file add
		List<String> beforeNames = zipEntryNames(tempZipFile);
		ZipUtil.append(tempZipFile.toPath(), appendFile.toPath());
		List<String> afterNames = zipEntryNames(tempZipFile);

		// 确认增加了文件
		Assertions.assertEquals(beforeNames.size() + 1, afterNames.size());
		Assertions.assertTrue(afterNames.containsAll(beforeNames));
		Assertions.assertTrue(afterNames.contains(appendFile.getName()));

		// test dir add
		beforeNames = zipEntryNames(tempZipFile);
		final File addDirFile = FileUtil.file("test-zip/test-add");
		ZipUtil.append(tempZipFile.toPath(), addDirFile.toPath());
		afterNames = zipEntryNames(tempZipFile);

		// 确认增加了文件和目录，增加目录和目录下一个文件，故此处+2
		Assertions.assertEquals(beforeNames.size() + 2, afterNames.size());
		Assertions.assertTrue(afterNames.containsAll(beforeNames));
		Assertions.assertTrue(afterNames.contains(appendFile.getName()));

		// rollback
		Assertions.assertTrue(tempZipFile.delete(), String.format("delete temp file %s failed", tempZipFile.getCanonicalPath()));
	}

	/**
	 * 获取zip文件中所有一级文件/文件夹的name
	 *
	 * @param zipFile 待测试的zip文件
	 * @return zip文件中一级目录下的所有文件/文件夹名
	 */
	private List<String> zipEntryNames(final File zipFile) {
		final List<String> fileNames = new ArrayList<>();
		final ZipReader reader = ZipReader.of(zipFile, CharsetUtil.UTF_8);
		reader.read(zipEntry -> fileNames.add(zipEntry.getName()));
		reader.close();
		return fileNames;
	}

	@Test
	@Disabled
	public void zipDirTest() {
		ZipUtil.zip(new File("d:/test"));
	}

	@Test
	@Disabled
	public void unzipTest() {
		final File unzip = ZipUtil.unzip("f:/test/apache-maven-3.6.2.zip", "f:\\test");
		Console.log(unzip);
	}

	@Test
	@Disabled
	public void unzipTest2() {
		final File unzip = ZipUtil.unzip("f:/test/各种资源.zip", "f:/test/各种资源", CharsetUtil.GBK);
		Console.log(unzip);
	}

	@Test
	@Disabled
	public void unzipFromStreamTest() {
		final File unzip = ZipUtil.unzip(FileUtil.getInputStream("e:/test/hutool-core-5.1.0.jar"), FileUtil.file("e:/test/"), CharsetUtil.UTF_8);
		Console.log(unzip);
	}

	@Test
	@Disabled
	public void unzipChineseTest() {
		ZipUtil.unzip("d:/测试.zip");
	}

	@Test
	@Disabled
	public void unzipFileBytesTest() {
		final byte[] fileBytes = ZipUtil.unzipFileBytes(FileUtil.file("e:/02 电力相关设备及服务2-241-.zip"), CharsetUtil.GBK, "images/CE-EP-HY-MH01-ES-0001.jpg");
		Assertions.assertNotNull(fileBytes);
	}

	@Test
	public void gzipTest() {
		final String data = "我是一个需要压缩的很长很长的字符串";
		final byte[] bytes = ByteUtil.toUtf8Bytes(data);
		final byte[] gzip = ZipUtil.gzip(bytes);

		//保证gzip长度正常
		Assertions.assertEquals(68, gzip.length);

		final byte[] unGzip = ZipUtil.unGzip(gzip);
		//保证正常还原
		Assertions.assertEquals(data, StrUtil.utf8Str(unGzip));
	}

	@Test
	public void zlibTest() {
		final String data = "我是一个需要压缩的很长很长的字符串";
		final byte[] bytes = ByteUtil.toUtf8Bytes(data);
		byte[] gzip = ZipUtil.zlib(bytes, 0);

		//保证zlib长度正常
		Assertions.assertEquals(62, gzip.length);
		final byte[] unGzip = ZipUtil.unZlib(gzip);
		//保证正常还原
		Assertions.assertEquals(data, StrUtil.utf8Str(unGzip));

		gzip = ZipUtil.zlib(bytes, 9);
		//保证zlib长度正常
		Assertions.assertEquals(56, gzip.length);
		final byte[] unGzip2 = ZipUtil.unZlib(gzip);
		//保证正常还原
		Assertions.assertEquals(data, StrUtil.utf8Str(unGzip2));
	}

	@Test
	@Disabled
	public void zipStreamTest(){
		//https://github.com/dromara/hutool/issues/944
		final String dir = "d:/test";
		final String zip = "d:/test.zip";
		try (final OutputStream out = Files.newOutputStream(Paths.get(zip))){
			//实际应用中, out 为 HttpServletResponse.getOutputStream
			ZipUtil.zip(out, Charset.defaultCharset(), false, null, new File(dir));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Test
	@Disabled
	public void zipStreamTest2(){
		// https://github.com/dromara/hutool/issues/944
		final String file1 = "d:/test/a.txt";
		final String file2 = "d:/test/a.txt";
		final String file3 = "d:/test/asn1.key";

		final String zip = "d:/test/test2.zip";
		//实际应用中, out 为 HttpServletResponse.getOutputStream
		ZipUtil.zip(FileUtil.getOutputStream(zip), Charset.defaultCharset(), false, null,
				new File(file1),
				new File(file2),
				new File(file3)
		);
	}

	@Test
	@Disabled
	public void zipToStreamTest(){
		final String zip = "d:/test/testToStream.zip";
		final OutputStream out = FileUtil.getOutputStream(zip);
		ZipUtil.zip(out, new String[]{"sm1_alias.txt"},
				new InputStream[]{FileUtil.getInputStream("d:/test/sm4_1.txt")});
	}

	@Test
	@Disabled
	public void zipMultiFileTest(){
		final File[] dd={FileUtil.file("d:\\test\\qr_a.jpg")
				,FileUtil.file("d:\\test\\qr_b.jpg")};

		ZipUtil.zip(FileUtil.file("d:\\test\\qr.zip"),false,dd);
	}

	@Test
	@Disabled
	public void sizeUnzip() throws IOException {
		final String zipPath = "F:\\BaiduNetdiskDownload\\demo.zip";
		final String outPath = "F:\\BaiduNetdiskDownload\\test";
		final ZipFile zipFile = new ZipFile(zipPath, Charset.forName("GBK"));
		final File file = new File(outPath);
		// 限制解压文件大小为637KB
		final long size = 637*1024L;

		// 限制解压文件大小为636KB
		// long size = 636*1024L;
		ZipUtil.unzip(zipFile, file, size);
	}

	@Test
	@Disabled
	public void unzipTest3() {
		// https://github.com/dromara/hutool/issues/3018
		ZipUtil.unzip("d:/test/default.zip", "d:/test/");
	}
}
