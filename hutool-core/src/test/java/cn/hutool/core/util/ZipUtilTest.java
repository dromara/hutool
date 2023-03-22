package cn.hutool.core.util;

import cn.hutool.core.compress.ZipReader;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import static cn.hutool.core.util.ZipUtil.unzip;

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
		Assert.assertEquals(beforeNames.size() + 1, afterNames.size());
		Assert.assertTrue(afterNames.containsAll(beforeNames));
		Assert.assertTrue(afterNames.contains(appendFile.getName()));

		// test dir add
		beforeNames = zipEntryNames(tempZipFile);
		final File addDirFile = FileUtil.file("test-zip/test-add");
		ZipUtil.append(tempZipFile.toPath(), addDirFile.toPath());
		afterNames = zipEntryNames(tempZipFile);

		// 确认增加了文件和目录，增加目录和目录下一个文件，故此处+2
		Assert.assertEquals(beforeNames.size() + 2, afterNames.size());
		Assert.assertTrue(afterNames.containsAll(beforeNames));
		Assert.assertTrue(afterNames.contains(appendFile.getName()));

		// rollback
		Assert.assertTrue(String.format("delete temp file %s failed", tempZipFile.getCanonicalPath()), tempZipFile.delete());
	}

	/**
	 * 获取zip文件中所有一级文件/文件夹的name
	 *
	 * @param zipFile 待测试的zip文件
	 * @return zip文件中一级目录下的所有文件/文件夹名
	 */
	private List<String> zipEntryNames(final File zipFile) {
		final List<String> fileNames = new ArrayList<>();
		final ZipReader reader = ZipReader.of(zipFile, CharsetUtil.CHARSET_UTF_8);
		reader.read(zipEntry -> fileNames.add(zipEntry.getName()));
		reader.close();
		return fileNames;
	}

	@Test
	@Ignore
	public void zipDirTest() {
		ZipUtil.zip(new File("d:/test"));
	}

	@Test
	@Ignore
	public void unzipTest() {
		final File unzip = ZipUtil.unzip("d:/test/hutool.zip", "d:\\test", CharsetUtil.CHARSET_GBK);
		Console.log(unzip);
	}

	@Test
	@Ignore
	public void unzipTest2() {
		final File unzip = ZipUtil.unzip("f:/test/各种资源.zip", "f:/test/各种资源", CharsetUtil.CHARSET_GBK);
		Console.log(unzip);
	}

	@Test
	@Ignore
	public void unzipFromStreamTest() {
		final File unzip = ZipUtil.unzip(FileUtil.getInputStream("e:/test/hutool-core-5.1.0.jar"), FileUtil.file("e:/test/"), CharsetUtil.CHARSET_UTF_8);
		Console.log(unzip);
	}

	@Test
	@Ignore
	public void unzipChineseTest() {
		ZipUtil.unzip("d:/测试.zip");
	}

	@Test
	@Ignore
	public void unzipFileBytesTest() {
		final byte[] fileBytes = ZipUtil.unzipFileBytes(FileUtil.file("e:/02 电力相关设备及服务2-241-.zip"), CharsetUtil.CHARSET_GBK, "images/CE-EP-HY-MH01-ES-0001.jpg");
		Assert.assertNotNull(fileBytes);
	}

	@Test
	public void gzipTest() {
		final String data = "我是一个需要压缩的很长很长的字符串";
		final byte[] bytes = StrUtil.utf8Bytes(data);
		final byte[] gzip = ZipUtil.gzip(bytes);

		//保证gzip长度正常
		Assert.assertEquals(68, gzip.length);

		final byte[] unGzip = ZipUtil.unGzip(gzip);
		//保证正常还原
		Assert.assertEquals(data, StrUtil.utf8Str(unGzip));
	}

	@Test
	public void zlibTest() {
		final String data = "我是一个需要压缩的很长很长的字符串";
		final byte[] bytes = StrUtil.utf8Bytes(data);
		byte[] gzip = ZipUtil.zlib(bytes, 0);

		//保证zlib长度正常
		Assert.assertEquals(62, gzip.length);
		final byte[] unGzip = ZipUtil.unZlib(gzip);
		//保证正常还原
		Assert.assertEquals(data, StrUtil.utf8Str(unGzip));

		gzip = ZipUtil.zlib(bytes, 9);
		//保证zlib长度正常
		Assert.assertEquals(56, gzip.length);
		final byte[] unGzip2 = ZipUtil.unZlib(gzip);
		//保证正常还原
		Assert.assertEquals(data, StrUtil.utf8Str(unGzip2));
	}

	@Test
	@Ignore
	public void zipStreamTest(){
		//https://github.com/dromara/hutool/issues/944
		final String dir = "d:/test";
		final String zip = "d:/test.zip";
		//noinspection IOStreamConstructor
		try (final OutputStream out = new FileOutputStream(zip)){
			//实际应用中, out 为 HttpServletResponse.getOutputStream
			ZipUtil.zip(out, Charset.defaultCharset(), false, null, new File(dir));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Test
	@Ignore
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
	@Ignore
	public void zipToStreamTest(){
		final String zip = "d:/test/testToStream.zip";
		final OutputStream out = FileUtil.getOutputStream(zip);
		ZipUtil.zip(out, new String[]{"sm1_alias.txt"},
				new InputStream[]{FileUtil.getInputStream("d:/test/sm4_1.txt")});
	}

	@Test
	@Ignore
	public void zipMultiFileTest(){
		final File[] dd={FileUtil.file("d:\\test\\qr_a.jpg")
				,FileUtil.file("d:\\test\\qr_b.jpg")};

		ZipUtil.zip(FileUtil.file("d:\\test\\qr.zip"),false,dd);
	}

	@Test
	@Ignore
	public void sizeUnzipTest() throws IOException {
		final String zipPath = "e:\\hutool\\demo.zip";
		final String outPath = "e:\\hutool\\test";
		final ZipFile zipFile = new ZipFile(zipPath, Charset.forName("GBK"));
		final File file = new File(outPath);
		// 限制解压文件大小为637KB
		final long size = 637*1024L;

		// 限制解压文件大小为636KB
		// long size = 636*1024L;
		unzip(zipFile, file, size);
	}
}
