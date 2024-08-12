/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.extra.compress.archiver;

import java.io.Closeable;
import java.io.File;
import java.util.function.Function;
import java.util.function.Predicate;

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
	default Archiver add(final File file) {
		return add(file, null);
	}

	/**
	 * 将文件或目录加入归档，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param predicate 文件过滤器，指定哪些文件或目录可以加入，{@link Predicate#test(Object)}为{@code true}时加入，null表示全部加入
	 * @return this
	 */
	default Archiver add(final File file, final Predicate<File> predicate) {
		return add(file, null, predicate);
	}

	/**
	 * 将文件或目录加入归档包，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param path   文件或目录的初始路径，null表示位于根路径
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，{@link Predicate#test(Object)}为{@code true}保留，null表示全部加入
	 * @return this
	 */
	default Archiver add(final File file, final String path, final Predicate<File> filter){
		return add(file, path, Function.identity(), filter);
	}

	/**
	 * 将文件或目录加入归档包，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param path   文件或目录的初始路径，null表示位于根路径
	 * @param fileNameEditor 文件名编辑器
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，{@link Predicate#test(Object)}为{@code true}保留，null表示全部加入
	 * @return this
	 * @since 6.0.0
	 */
	Archiver add(File file, String path, Function<String, String> fileNameEditor, Predicate<File> filter);

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
