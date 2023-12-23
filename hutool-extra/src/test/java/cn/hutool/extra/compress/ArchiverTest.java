package cn.hutool.extra.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.compress.archiver.StreamArchiver;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

@SuppressWarnings("resource")
public class ArchiverTest {

	@Test
	@Ignore
	public void zipTest(){
		final File file = FileUtil.file("d:/test/compress/test.zip");
		StreamArchiver.create(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.ZIP, file)
				.add(FileUtil.file("d:/Java"), (f)->{
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Ignore
	public void tarTest(){
		final File file = FileUtil.file("d:/test/compress/test.tar");
		StreamArchiver.create(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.TAR, file)
				.add(FileUtil.file("d:/Java"), (f)->{
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Ignore
	public void cpioTest(){
		final File file = FileUtil.file("d:/test/compress/test.cpio");
		StreamArchiver.create(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.CPIO, file)
				.add(FileUtil.file("d:/Java"), (f)->{
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Ignore
	public void sevenZTest(){
		final File file = FileUtil.file("d:/test/compress/test.7z");
		CompressUtil.createArchiver(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.SEVEN_Z, file)
				.add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f)->{
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Ignore
	public void tgzTest(){
		final File file = FileUtil.file("d:/test/compress/test.tgz");
		CompressUtil.createArchiver(CharsetUtil.CHARSET_UTF_8, "tgz", file)
				.add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f)->{
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	/**
	 * Add: D:\disk-all
	 * Add: D:\disk-all\els-app
	 * Add: D:\disk-all\els-app\db-backup
	 * Add: D:\disk-all\els-app\新建 文本文档.txt
	 * Add: D:\disk-all\新建 文本文档.txt
	 * Add: D:\disk-all\新建文件夹
	 */
	@Test
	@Ignore
	public void emptyTest(){
		final File file = FileUtil.file("d:/disk-all.tgz");
		CompressUtil.createArchiver(CharsetUtil.CHARSET_UTF_8, "tgz", file)
			.add(FileUtil.file("D:\\disk-all"), (f)->{
				Console.log("Add: {}", f.getPath());
				return true;
			})
			.finish().close();
	}

	@Test
	@Ignore
	public void emptyZTest(){
		final File file = FileUtil.file("d:/disk-all.7z");
		CompressUtil.createArchiver(CharsetUtil.CHARSET_UTF_8, "7z", file)
			.add(FileUtil.file("D:\\disk-all"), (f)->{
				Console.log("Add: {}", f.getPath());
				return true;
			})
			.finish().close();
	}
}
