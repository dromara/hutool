package cn.hutool.extra.compress.archiver;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;

import java.io.Closeable;
import java.io.File;

/**
 * 数据归档封装，归档即将几个文件或目录打成一个压缩包
 *
 * @author looly
 */
public interface Archiver extends Closeable {

	/**
	 * 将文件或目录加入归档，目录采取递归读取方式按照层级加入
	 *
	 * @param file 文件或目录
	 * @return this
	 */
	default Archiver add(File file) {
		return add(file, null);
	}

	/**
	 * 将文件或目录加入归档，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，当{@link Filter#accept(Object)}为true时加入。
	 * @return this
	 */
	default Archiver add(File file, Filter<File> filter) {
		return add(file, StrUtil.SLASH, filter);
	}

	/**
	 * 将文件或目录加入归档包，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param path   文件或目录的初始路径，null表示位于根路径
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，当{@link Filter#accept(Object)}为true时加入。
	 * @return this
	 */
	Archiver add(File file, String path, Filter<File> filter);

	/**
	 * 结束已经增加的文件归档，此方法不会关闭归档流，可以继续添加文件
	 *
	 * @return this
	 */
	Archiver finish();

	/**
	 * 无异常关闭
	 */
	@Override
	void close();
}
