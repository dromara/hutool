package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.copier.SrcToDestCopier;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * 文件拷贝器<br>
 * 支持以下几种情况：
 * <pre>
 * 1、文件复制到文件
 * 2、文件复制到目录
 * 3、目录复制到目录
 * 4、目录下的文件和目录复制到另一个目录
 * </pre>
 * 
 * @author Looly
 * @since 3.0.9
 */
public class FileCopier extends SrcToDestCopier<File, FileCopier>{
	private static final long serialVersionUID = 1L;
	
	/** 是否覆盖目标文件 */
	private boolean isOverride;
	/** 是否拷贝所有属性 */
	private boolean isCopyAttributes;
	/** 当拷贝来源是目录时是否只拷贝目录下的内容 */
	private boolean isCopyContentIfDir;
	/** 当拷贝来源是目录时是否只拷贝文件而忽略子目录 */
	private boolean isOnlyCopyFile;
	
	//-------------------------------------------------------------------------------------------------------- static method start
	/**
	 * 新建一个文件复制器
	 * @param srcPath 源文件路径（相对ClassPath路径或绝对路径）
	 * @param destPath 目标文件路径（相对ClassPath路径或绝对路径）
	 * @return {@link FileCopier}
	 */
	public static FileCopier create(String srcPath, String destPath) {
		return new FileCopier(FileUtil.file(srcPath), FileUtil.file(destPath));
	}
	
	/**
	 * 新建一个文件复制器
	 * @param src 源文件
	 * @param dest 目标文件
	 * @return {@link FileCopier}
	 */
	public static FileCopier create(File src, File dest) {
		return new FileCopier(src, dest);
	}
	//-------------------------------------------------------------------------------------------------------- static method end
	
	//-------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param src 源文件
	 * @param dest 目标文件
	 */
	public FileCopier(File src, File dest) {
		this.src = src;
		this.dest = dest;
	}
	//-------------------------------------------------------------------------------------------------------- Constructor end
	
	//-------------------------------------------------------------------------------------------------------- Getters and Setters start
	/**
	 * 是否覆盖目标文件
	 * @return 是否覆盖目标文件
	 */
	public boolean isOverride() {
		return isOverride;
	}
	/**
	 * 设置是否覆盖目标文件
	 * @param isOverride 是否覆盖目标文件
	 * @return this
	 */
	public FileCopier setOverride(boolean isOverride) {
		this.isOverride = isOverride;
		return this;
	}

	/**
	 * 是否拷贝所有属性
	 * @return 是否拷贝所有属性
	 */
	public boolean isCopyAttributes() {
		return isCopyAttributes;
	}
	/**
	 * 设置是否拷贝所有属性
	 * @param isCopyAttributes 是否拷贝所有属性
	 * @return this
	 */
	public FileCopier setCopyAttributes(boolean isCopyAttributes) {
		this.isCopyAttributes = isCopyAttributes;
		return this;
	}

	/**
	 * 当拷贝来源是目录时是否只拷贝目录下的内容
	 * @return 当拷贝来源是目录时是否只拷贝目录下的内容
	 */
	public boolean isCopyContentIfDir() {
		return isCopyContentIfDir;
	}
	
	/**
	 * 当拷贝来源是目录时是否只拷贝目录下的内容
	 * @param isCopyContentIfDir 是否只拷贝目录下的内容
	 * @return this
	 */
	public FileCopier setCopyContentIfDir(boolean isCopyContentIfDir) {
		this.isCopyContentIfDir = isCopyContentIfDir;
		return this;
	}
	
	/**
	 * 当拷贝来源是目录时是否只拷贝文件而忽略子目录
	 * 
	 * @return 当拷贝来源是目录时是否只拷贝文件而忽略子目录
	 * @since 4.1.5
	 */
	public boolean isOnlyCopyFile() {
		return isOnlyCopyFile;
	}

	/**
	 * 设置当拷贝来源是目录时是否只拷贝文件而忽略子目录
	 * 
	 * @param isOnlyCopyFile 当拷贝来源是目录时是否只拷贝文件而忽略子目录
	 * @return this
	 * @since 4.1.5
	 */
	public FileCopier setOnlyCopyFile(boolean isOnlyCopyFile) {
		this.isOnlyCopyFile = isOnlyCopyFile;
		return this;
	}
	//-------------------------------------------------------------------------------------------------------- Getters and Setters end

	/**
	 * 执行拷贝<br>
	 * 拷贝规则为：
	 * <pre>
	 * 1、源为文件，目标为已存在目录，则拷贝到目录下，文件名不变
	 * 2、源为文件，目标为不存在路径，则目标以文件对待（自动创建父级目录）比如：/dest/aaa，如果aaa不存在，则aaa被当作文件名
	 * 3、源为文件，目标是一个已存在的文件，则当{@link #setOverride(boolean)}设为true时会被覆盖，默认不覆盖
	 * 4、源为目录，目标为已存在目录，当{@link #setCopyContentIfDir(boolean)}为true时，只拷贝目录中的内容到目标目录中，否则整个源目录连同其目录拷贝到目标目录中
	 * 5、源为目录，目标为不存在路径，则自动创建目标为新目录，然后按照规则4复制
	 * 6、源为目录，目标为文件，抛出IO异常
	 * 7、源路径和目标路径相同时，抛出IO异常
	 * </pre>
	 * 
	 * @return 拷贝后目标的文件或目录
	 * @throws IORuntimeException IO异常
	 */
	@Override
	public File copy() throws IORuntimeException{
		final File src = this.src;
		final File dest = this.dest;
		// check
		Assert.notNull(src, "Source File is null !");
		if (false == src.exists()) {
			throw new IORuntimeException("File not exist: " + src);
		}
		Assert.notNull(dest, "Destination File or directiory is null !");
		if (FileUtil.equals(src, dest)) {
			throw new IORuntimeException("Files '{}' and '{}' are equal", src, dest);
		}

		if (src.isDirectory()) {// 复制目录
			if(dest.exists() && false == dest.isDirectory()) {
				//源为目录，目标为文件，抛出IO异常
				throw new IORuntimeException("Src is a directory but dest is a file!");
			}
			if(FileUtil.isSub(src, dest)) {
				throw new IORuntimeException("Dest is a sub directory of src !");
			}
			
			final File subDest = isCopyContentIfDir ? dest : FileUtil.mkdir(FileUtil.file(dest, src.getName()));
			internalCopyDirContent(src, subDest);
		} else {// 复制文件
			internalCopyFile(src, dest);
		}
		return dest;
	}

	//----------------------------------------------------------------------------------------- Private method start
	/**
	 * 拷贝目录内容，只用于内部，不做任何安全检查<br>
	 * 拷贝内容的意思为源目录下的所有文件和目录拷贝到另一个目录下，而不拷贝源目录本身
	 * 
	 * @param src 源目录
	 * @param dest 目标目录
	 * @throws IORuntimeException IO异常
	 */
	private void internalCopyDirContent(File src, File dest) throws IORuntimeException {
		if (null != copyFilter && false == copyFilter.accept(src)) {
			//被过滤的目录跳过
			return;
		}

		if (false == dest.exists()) {
			//目标为不存在路径，创建为目录
			//noinspection ResultOfMethodCallIgnored
			dest.mkdirs();
		} else if (false == dest.isDirectory()) {
			throw new IORuntimeException(StrUtil.format("Src [{}] is a directory but dest [{}] is a file!", src.getPath(), dest.getPath()));
		}
		
		final String[] files = src.list();
		if(ArrayUtil.isNotEmpty(files)){
			File srcFile;
			File destFile;
			for (String file : files) {
				srcFile = new File(src, file);
				destFile = this.isOnlyCopyFile ? dest : new File(dest, file);
				// 递归复制
				if (srcFile.isDirectory()) {
					internalCopyDirContent(srcFile, destFile);
				} else {
					internalCopyFile(srcFile, destFile);
				}
			}
		}
	}

	/**
	 * 拷贝文件，只用于内部，不做任何安全检查<br>
	 * 情况如下：
	 * <pre>
	 * 1、如果目标是一个不存在的路径，则目标以文件对待（自动创建父级目录）比如：/dest/aaa，如果aaa不存在，则aaa被当作文件名
	 * 2、如果目标是一个已存在的目录，则文件拷贝到此目录下，文件名与原文件名一致
	 * </pre>
	 * 
	 * @param src 源文件，必须为文件
	 * @param dest 目标文件，如果非覆盖模式必须为目录
	 * @throws IORuntimeException IO异常
	 */
	private void internalCopyFile(File src, File dest) throws IORuntimeException {
		if (null != copyFilter && false == copyFilter.accept(src)) {
			//被过滤的文件跳过
			return;
		}
		
		// 如果已经存在目标文件，切为不覆盖模式，跳过之
		if (dest.exists()) {
			if(dest.isDirectory()) {
				//目标为目录，目录下创建同名文件
				dest = new File(dest, src.getName());
			}
			
			if(dest.exists() && false == isOverride) {
				//非覆盖模式跳过
				return;
			}
		}else {
			//路径不存在则创建父目录
			//noinspection ResultOfMethodCallIgnored
			dest.getParentFile().mkdirs();
		}
		
		final ArrayList<CopyOption> optionList = new ArrayList<>(2);
		if(isOverride) {
			optionList.add(StandardCopyOption.REPLACE_EXISTING);
		}
		if(isCopyAttributes) {
			optionList.add(StandardCopyOption.COPY_ATTRIBUTES);
		}
		
		try {
			Files.copy(src.toPath(), dest.toPath(), optionList.toArray(new CopyOption[0]));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	//----------------------------------------------------------------------------------------- Private method end
}
