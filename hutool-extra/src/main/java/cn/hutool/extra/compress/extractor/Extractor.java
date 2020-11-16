package cn.hutool.extra.compress.extractor;

import cn.hutool.core.lang.Filter;
import org.apache.commons.compress.archivers.ArchiveEntry;

import java.io.Closeable;
import java.io.File;

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
	default void extract(File targetDir){
		extract(targetDir, null);
	}

	/**
	 * 释放（解压）到指定目录，结束后自动关闭流，此方法只能调用一次
	 *
	 * @param targetDir 目标目录
	 * @param filter    解压文件过滤器，用于指定需要释放的文件，null表示不过滤。当{@link Filter#accept(Object)}为true时释放。
	 */
	void extract(File targetDir, Filter<ArchiveEntry> filter);

	/**
	 * 无异常关闭
	 */
	@Override
	void close();
}
