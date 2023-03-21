package cn.hutool.extra.compress.extractor;

import cn.hutool.core.text.StrUtil;
import org.apache.commons.compress.archivers.ArchiveEntry;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.util.function.Predicate;

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
	default void extract(final File targetDir) {
		extract(targetDir, null);
	}

	/**
	 * 释放（解压）到指定目录，结束后自动关闭流，此方法只能调用一次
	 *
	 * @param targetDir 目标目录
	 * @param predicate 解压文件过滤器，用于指定需要释放的文件，{@code null}表示不过滤。{@link Predicate#test(Object)}为{@code true}时释放。
	 */
	void extract(File targetDir, Predicate<ArchiveEntry> predicate);

	/**
	 * 获取指定名称的文件流
	 *
	 * @param entryName entry名称
	 * @return 文件流，无文件返回{@code null}
	 */
	default InputStream get(final String entryName) {
		return getFirst((entry) -> StrUtil.equals(entryName, entry.getName()));
	}

	/**
	 * 获取满足指定过滤要求的压缩包内的第一个文件流
	 *
	 * @param predicate 用于指定需要释放的文件，null表示不过滤。当{@link Predicate#test(Object)}为{@code true}返回对应流。
	 * @return 满足过滤要求的第一个文件的流, 无满足条件的文件返回{@code null}
	 */
	InputStream getFirst(final Predicate<ArchiveEntry> predicate);

	/**
	 * 无异常关闭
	 */
	@Override
	void close();
}
