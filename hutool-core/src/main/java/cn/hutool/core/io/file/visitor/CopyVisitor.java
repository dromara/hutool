package cn.hutool.core.io.file.visitor;

import cn.hutool.core.io.file.PathUtil;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件拷贝的FileVisitor实现，用于递归遍历拷贝目录，此类非线程安全<br>
 * 此类在遍历源目录并复制过程中会自动创建目标目录中不存在的上级目录。
 *
 * @author looly
 * @since 5.5.1
 */
public class CopyVisitor extends SimpleFileVisitor<Path> {

	/**
	 * 源Path，或基准路径，用于计算被拷贝文件的相对路径
	 */
	private final Path source;
	private final Path target;
	private final CopyOption[] copyOptions;

	/**
	 * 标记目标目录是否创建，省略每次判断目标是否存在
	 */
	private boolean isTargetCreated;

	/**
	 * 构造
	 *
	 * @param source      源Path，或基准路径，用于计算被拷贝文件的相对路径
	 * @param target      目标Path
	 * @param copyOptions 拷贝选项，如跳过已存在等
	 */
	public CopyVisitor(Path source, Path target, CopyOption... copyOptions) {
		if (PathUtil.exists(target, false) && false == PathUtil.isDirectory(target)) {
			throw new IllegalArgumentException("Target must be a directory");
		}
		this.source = source;
		this.target = target;
		this.copyOptions = copyOptions;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		initTargetDir();
		// 将当前目录相对于源路径转换为相对于目标路径
		final Path targetDir = resolveTarget(dir);

		// 在目录不存在的情况下，copy方法会创建新目录
		try {
			Files.copy(dir, targetDir, copyOptions);
		} catch (FileAlreadyExistsException e) {
			if (false == Files.isDirectory(targetDir)) {
				// 目标文件存在抛出异常，目录忽略
				throw e;
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		initTargetDir();
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
		return target.resolve(source.relativize(file));
	}

	/**
	 * 初始化目标文件或目录
	 */
	private void initTargetDir() {
		if (false == this.isTargetCreated) {
			PathUtil.mkdir(this.target);
			this.isTargetCreated = true;
		}
	}
}
