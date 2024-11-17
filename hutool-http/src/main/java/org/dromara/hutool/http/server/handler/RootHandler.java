/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http.server.handler;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.http.meta.HttpStatus;
import org.dromara.hutool.http.server.ServerRequest;
import org.dromara.hutool.http.server.ServerResponse;

import java.io.File;
import java.util.List;

/**
 * 默认的处理器，通过解析用户传入的path，找到网页根目录下对应文件后返回
 *
 * @author looly
 * @since 5.2.6
 */
public class RootHandler implements HttpHandler {

	/**
	 * 默认主页文件名
	 */
	public static final String DEFAULT_INDEX_FILE_NAME = "index.html";

	private final File rootDir;
	private final List<String> indexFileNames;

	/**
	 * 构造
	 *
	 * @param rootDir 网页根目录
	 */
	public RootHandler(final String rootDir) {
		this(new File(rootDir));
	}

	/**
	 * 构造
	 *
	 * @param rootDir 网页根目录
	 */
	public RootHandler(final File rootDir) {
		this(rootDir, DEFAULT_INDEX_FILE_NAME);
	}

	/**
	 * 构造
	 *
	 * @param rootDir        网页根目录
	 * @param indexFileNames 主页文件名列表
	 */
	public RootHandler(final String rootDir, final String... indexFileNames) {
		this(new File(rootDir), indexFileNames);
	}

	/**
	 * 构造
	 *
	 * @param rootDir        网页根目录
	 * @param indexFileNames 主页文件名列表
	 * @since 5.4.0
	 */
	public RootHandler(final File rootDir, final String... indexFileNames) {
		this.rootDir = rootDir;
		this.indexFileNames = ListUtil.of(indexFileNames);
	}

	@Override
	public void handle(final ServerRequest request, final ServerResponse response) {
		final String path = request.getPath();

		File file = FileUtil.file(rootDir, path);
		if (file.exists()) {
			if (file.isDirectory()) {
				for (final String indexFileName : indexFileNames) {
					//默认读取主页
					file = FileUtil.file(file, indexFileName);
					if (file.exists() && file.isFile()) {
						response.write(file);
						return;
					}
				}
			} else{
				final String name = request.getParam("name");
				response.write(file, name);
				return;
			}
		}

		response.setStatus(HttpStatus.HTTP_NOT_FOUND);
		response.write("404 Not Found !");
	}
}
