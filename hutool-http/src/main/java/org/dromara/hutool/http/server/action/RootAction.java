/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.server.action;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.http.server.HttpServerRequest;
import org.dromara.hutool.http.server.HttpServerResponse;

import java.io.File;
import java.util.List;

/**
 * 默认的处理器，通过解析用户传入的path，找到网页根目录下对应文件后返回
 *
 * @author looly
 * @since 5.2.6
 */
public class RootAction implements Action {

	public static final String DEFAULT_INDEX_FILE_NAME = "index.html";

	private final File rootDir;
	private final List<String> indexFileNames;

	/**
	 * 构造
	 *
	 * @param rootDir 网页根目录
	 */
	public RootAction(final String rootDir) {
		this(new File(rootDir));
	}

	/**
	 * 构造
	 *
	 * @param rootDir 网页根目录
	 */
	public RootAction(final File rootDir) {
		this(rootDir, DEFAULT_INDEX_FILE_NAME);
	}

	/**
	 * 构造
	 *
	 * @param rootDir        网页根目录
	 * @param indexFileNames 主页文件名列表
	 */
	public RootAction(final String rootDir, final String... indexFileNames) {
		this(new File(rootDir), indexFileNames);
	}

	/**
	 * 构造
	 *
	 * @param rootDir        网页根目录
	 * @param indexFileNames 主页文件名列表
	 * @since 5.4.0
	 */
	public RootAction(final File rootDir, final String... indexFileNames) {
		this.rootDir = rootDir;
		this.indexFileNames = ListUtil.of(indexFileNames);
	}

	@Override
	public void doAction(final HttpServerRequest request, final HttpServerResponse response) {
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

		response.send404("404 Not Found !");
	}
}
