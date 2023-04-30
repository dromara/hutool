package cn.hutool.core.io.file;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.visitor.MoveVisitor;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.nio.file.*;

/**
 * 文件移动封装
 *
 * @author looly
 * @since 5.8.14
 */
public class PathMover {

	/**
	 * 创建文件或目录移动器
	 *
	 * @param src        源文件或目录
	 * @param target     目标文件或目录
	 * @param isOverride 是否覆盖目标文件
	 * @return {@code PathMover}
	 */
	public static PathMover of(final Path src, final Path target, final boolean isOverride) {
		return of(src, target, isOverride ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
	}

	/**
	 * 创建文件或目录移动器
	 *
	 * @param src     源文件或目录
	 * @param target  目标文件或目录
	 * @param options 移动参数
	 * @return {@code PathMover}
	 */
	public static PathMover of(final Path src, final Path target, final CopyOption[] options) {
		return new PathMover(src, target, options);
	}

	private final Path src;
	private final Path target;
	private final CopyOption[] options;

	/**
	 * 构造
	 *
	 * @param src     源文件或目录，不能为{@code null}且必须存在
	 * @param target  目标文件或目录
	 * @param options 移动参数
	 */
	public PathMover(final Path src, final Path target, final CopyOption[] options) {
		Assert.notNull(target, "Src path must be not null !");
		if(false == PathUtil.exists(src, false)){
			throw new IllegalArgumentException("Src path is not exist!");
		}
		this.src = src;
		this.target = Assert.notNull(target, "Target path must be not null !");
		this.options = ObjUtil.defaultIfNull(options, new CopyOption[]{});;
	}

	/**
	 * 移动文件或目录到目标中，例如：
	 * <ul>
	 *     <li>如果src和target为同一文件或目录，直接返回target。</li>
	 *     <li>如果src为文件，target为目录，则移动到目标目录下，存在同名文件则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为文件，则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为不存在的路径，则重命名源文件到目标指定的文件，如move("/a/b", "/c/d"), d不存在，则b变成d。</li>
	 *     <li>如果src为目录，target为文件，抛出{@link IllegalArgumentException}</li>
	 *     <li>如果src为目录，target为目录，则将源目录及其内容移动到目标路径目录中，如move("/a/b", "/c/d")，结果为"/c/d/b"</li>
	 *     <li>如果src为目录，target为不存在的路径，则重命名src到target，如move("/a/b", "/c/d")，结果为"/c/d/"，相当于b重命名为d</li>
	 * </ul>
	 *
	 * @return 目标文件Path
	 */
	public Path move() {
		final Path src = this.src;
		Path target = this.target;
		final CopyOption[] options = this.options;

		if (PathUtil.isSub(src, target)) {
			if(Files.exists(target) && PathUtil.equals(src, target)){
				// issue#2845，当用户传入目标路径与源路径一致时，直接返回，否则会导致删除风险。
				return target;
			}

			// 当用户将文件夹拷贝到其子文件夹时，报错
			throw new IllegalArgumentException(StrUtil.format("Target [{}] is sub path of src [{}]!", target, src));
		}

		if (PathUtil.isDirectory(target)) {
			// 创建子路径的情况，1是目标是目录，需要移动到目录下，2是目标不能存在，自动创建目录
			target = target.resolve(src.getFileName());
		}

		// 自动创建目标的父目录
		PathUtil.mkParentDirs(target);
		try {
			return Files.move(src, target, options);
		} catch (final IOException e) {
			if (e instanceof FileAlreadyExistsException) {
				// 目标文件已存在，直接抛出异常
				// issue#I4QV0L@Gitee
				throw new IORuntimeException(e);
			}
			// 移动失败，可能是跨分区移动导致的，采用递归移动方式
			walkMove(src, target, options);
			// 移动后删除空目录
			PathUtil.del(src);
			return target;
		}
	}

	/**
	 * 移动文件或目录内容到目标中，例如：
	 * <ul>
	 *     <li>如果src为文件，target为目录，则移动到目标目录下，存在同名文件则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为文件，则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为不存在的路径，则重命名源文件到目标指定的文件，如moveContent("/a/b", "/c/d"), d不存在，则b变成d。</li>
	 *     <li>如果src为目录，target为文件，抛出{@link IllegalArgumentException}</li>
	 *     <li>如果src为目录，target为目录，则将源目录下的内容移动到目标路径目录中，源目录不删除。</li>
	 *     <li>如果src为目录，target为不存在的路径，则创建目标路径为目录，将源目录下的内容移动到目标路径目录中，源目录不删除。</li>
	 * </ul>
	 *
	 * @return 目标文件Path
	 */
	public Path moveContent() {
		final Path src = this.src;
		if (PathUtil.isExistsAndNotDirectory(target, false)) {
			// 文件移动调用move方法
			return move();
		}

		final Path target = this.target;
		if (PathUtil.isExistsAndNotDirectory(target, false)) {
			// 目标不能为文件
			throw new IllegalArgumentException("Can not move dir content to a file");
		}

		// issue#2893 target 不存在导致NoSuchFileException
		if (PathUtil.equals(src, target)) {
			// issue#2845，当用户传入目标路径与源路径一致时，直接返回，否则会导致删除风险。
			return target;
		}

		final CopyOption[] options = this.options;

		// 移动失败，可能是跨分区移动导致的，采用递归移动方式
		walkMove(src, target, options);
		return target;
	}

	/**
	 * 递归移动
	 *
	 * @param src     源目录
	 * @param target  目标目录
	 * @param options 移动参数
	 */
	private static void walkMove(final Path src, final Path target, final CopyOption... options) {
		try {
			// 移动源目录下的内容而不删除目录
			Files.walkFileTree(src, new MoveVisitor(src, target, options));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
