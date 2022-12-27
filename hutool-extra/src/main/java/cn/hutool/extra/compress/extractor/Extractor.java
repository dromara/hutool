package cn.hutool.extra.compress.extractor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.compress.archivers.ArchiveEntry;

import java.io.Closeable;
import java.io.File;
import java.util.List;

/**
 * 归档数据解包封装，用于将zip、tar等包解包为文件
 *
 * @author looly
 * @since 5.5.0
 */
public interface Extractor extends Closeable {

	/**
	 * 释放（解压）到指定目录，结束后自动关闭流，此方法只能调用一次
	 *
	 * @param targetDir 目标目录
	 */
	default void extract(File targetDir) {
		extract(targetDir, null);
	}

	/**
	 * 释放（解压）到指定目录，结束后自动关闭流，此方法只能调用一次
	 *
	 * @param targetDir 目标目录
	 * @param filter    解压文件过滤器，用于指定需要释放的文件，{@code null}表示不过滤。当{@link Filter#accept(Object)}为true时释放。
	 */
	default void extract(File targetDir, Filter<ArchiveEntry> filter) {
		this.extract(targetDir, 0, filter);
	}

	/**
	 * 释放（解压）到指定目录，结束后自动关闭流，此方法只能调用一次
	 *
	 * @param targetDir       目标目录
	 * @param stripComponents 清除(剥离)压缩包里面的 n 级文件夹名
	 */
	default void extract(File targetDir, int stripComponents) {
		this.extract(targetDir, stripComponents, null);
	}

	/**
	 * 释放（解压）到指定目录，结束后自动关闭流，此方法只能调用一次
	 *
	 * @param targetDir       目标目录
	 * @param stripComponents 清除(剥离)压缩包里面的 n 级文件夹名
	 * @param filter          解压文件过滤器，用于指定需要释放的文件，{@code null}表示不过滤。当{@link Filter#accept(Object)}为true时释放。
	 */
	void extract(File targetDir, int stripComponents, Filter<ArchiveEntry> filter);

	/**
	 * 剥离名称
	 *
	 * @param name            文件名
	 * @param stripComponents 剥离层级
	 * @return 剥离后的文件名
	 */
	default String stripName(String name, int stripComponents) {
		if (stripComponents <= 0) {
			return name;
		}
		List<String> nameList = StrUtil.splitTrim(name, StrUtil.SLASH);
		int size = nameList.size();
		if (size > stripComponents) {
			nameList = CollUtil.sub(nameList, stripComponents, size);
			return CollUtil.join(nameList, StrUtil.SLASH);
		}
		return null;
	}

	/**
	 * 无异常关闭
	 */
	@Override
	void close();
}
