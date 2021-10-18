package cn.hutool.core.io.file.visitor;

import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.nio.zipfs.ZipFileSystem;
import com.sun.nio.zipfs.ZipPath;

import java.io.File;
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

	private final Path source;
	private final Path target;
	private boolean isTargetCreated;
	private final boolean isZipFile;
	private String dirRoot = null;
	private final CopyOption[] copyOptions;

	/**
	 * 构造
	 *
	 * @param source 源Path
	 * @param target 目标Path
	 * @param copyOptions 拷贝选项，如跳过已存在等
	 */
	public CopyVisitor(Path source, Path target, CopyOption... copyOptions) {
		if(PathUtil.exists(target, false) && false == PathUtil.isDirectory(target)){
			throw new IllegalArgumentException("Target must be a directory");
		}
		this.source = source;
		this.target = target;
		this.isZipFile = target instanceof ZipPath;
		this.copyOptions = copyOptions;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		final Path targetDir;
		if (isZipFile) {
			ZipPath zipPath = (ZipPath) target;
			ZipFileSystem fileSystem = zipPath.getFileSystem();
			if (dirRoot == null) {
				targetDir = fileSystem.getPath(dir.getFileName().toString());
				dirRoot = dir.getFileName().toString() + File.separator;
			} else {
				targetDir = fileSystem.getPath(dirRoot, StrUtil.subAfter(dir.toString(), dirRoot, false));
			}
		} else {
			initTarget();
			// 将当前目录相对于源路径转换为相对于目标路径
			targetDir = target.resolve(source.relativize(dir));
		}
		try {
			Files.copy(dir, targetDir, copyOptions);
		} catch (FileAlreadyExistsException e) {
			if (false == Files.isDirectory(targetDir))
				throw e;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		if (isZipFile) {
			if (dirRoot == null) {
				Files.copy(file, target, copyOptions);
			} else {
				ZipPath zipPath = (ZipPath) target;
				Files.copy(file, zipPath.getFileSystem().getPath(dirRoot, StrUtil.subAfter(file.toString(), dirRoot, false)), copyOptions);
			}
		} else {
			initTarget();
			Files.copy(file, target.resolve(source.relativize(file)), copyOptions);
		}
		return FileVisitResult.CONTINUE;
	}

	/**
	 * 初始化目标文件或目录
	 */
	private void initTarget(){
		if(false == this.isTargetCreated){
			PathUtil.mkdir(this.target);
			this.isTargetCreated = true;
		}
	}
}
