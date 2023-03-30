package cn.hutool.extra.compress;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.compress.archiver.StreamArchiver;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

@SuppressWarnings("resource")
public class ArchiverTest {

	@Test
	@Disabled
	public void zipTest() {
		final File file = FileUtil.file("d:/test/compress/test.zip");
		StreamArchiver.of(CharsetUtil.UTF_8, ArchiveStreamFactory.ZIP, file)
				.add(FileUtil.file("d:/Java"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void tarTest() {
		final File file = FileUtil.file("d:/test/compress/test.tar");
		StreamArchiver.of(CharsetUtil.UTF_8, ArchiveStreamFactory.TAR, file)
				.add(FileUtil.file("d:/Java"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void cpioTest() {
		final File file = FileUtil.file("d:/test/compress/test.cpio");
		StreamArchiver.of(CharsetUtil.UTF_8, ArchiveStreamFactory.CPIO, file)
				.add(FileUtil.file("d:/Java"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void sevenZTest() {
		final File file = FileUtil.file("d:/test/compress/test.7z");
		CompressUtil.createArchiver(CharsetUtil.UTF_8, ArchiveStreamFactory.SEVEN_Z, file)
				.add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void tgzTest() {
		final File file = FileUtil.file("d:/test/compress/test.tgz");
		CompressUtil.createArchiver(CharsetUtil.UTF_8, "tgz", file)
				.add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}
}
