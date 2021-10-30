package cn.hutool.core.compress;

import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Zip文件拷贝的FileVisitor实现，zip中追加文件，此类非线程安全<br>
 * 此类在遍历源目录并复制过程中会自动创建目标目录中不存在的上级目录。
 *
 * @author looly
 * @since 5.7.15
 */
public class ZipCopyVisitor extends SimpleFileVisitor<Path> {

	/**
	 * 源Path，或基准路径，用于计算被拷贝文件的相对路径
	 */
	private final Path source;
	private final FileSystem fileSystem;
	private final CopyOption[] copyOptions;

	/**
	 * 构造
	 *
	 * @param source 源Path，或基准路径，用于计算被拷贝文件的相对路径
	 * @param fileSystem 目标Zip文件
	 * @param copyOptions 拷贝选项，如跳过已存在等
	 */
	public ZipCopyVisitor(Path source, FileSystem fileSystem, CopyOption... copyOptions) {
		this.source = source;
		this.fileSystem = fileSystem;
		this.copyOptions = copyOptions;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		final Path targetDir = resolveTarget(dir);
		if(StrUtil.isNotEmpty(targetDir.toString())){
			// 在目标的Zip文件中的相对位置创建目录
			try {
				Files.copy(dir, targetDir, copyOptions);
			} catch (FileAlreadyExistsException e) {
				if (false == Files.isDirectory(targetDir)) {
					throw e;
				}
			}
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		// 如果目标存在，无论目录还是文件都抛出FileAlreadyExistsException异常，此处不做特别处理
		Files.copy(file, resolveTarget(file), copyOptions);

		return FileVisitResult.CONTINUE;
	}

	/**
	 * 根据源文件或目录路径，拼接生成目标的文件或目录路径<br>
	 * 原理是首先截取源路径，得到相对路径，再和目标路径拼接
	 *
	 * <p>
	 * 如：源路径是 /opt/test/，需要拷贝的文件是 /opt/test/a/a.txt，得到相对路径 a/a.txt<br>
	 * 目标路径是/home/，则得到最终目标路径是 /home/a/a.txt
	 * </p>
	 *
	 * @param file 需要拷贝的文件或目录Path
	 * @return 目标Path
	 */
	private Path resolveTarget(Path file) {
		return fileSystem.getPath(source.relativize(file).toString());
	}
}
