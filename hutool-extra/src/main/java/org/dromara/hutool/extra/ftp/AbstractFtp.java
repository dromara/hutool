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

package org.dromara.hutool.extra.ftp;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.util.List;

/**
 * 抽象FTP类，用于定义通用的FTP方法
 *
 * @author looly
 * @since 4.1.14
 */
public abstract class AbstractFtp implements Ftp {

	protected FtpConfig ftpConfig;

	@Override
	public FtpConfig getConfig() {
		return this.ftpConfig;
	}

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	protected AbstractFtp(final FtpConfig config) {
		this.ftpConfig = config;
	}

	@Override
	public boolean exist(final String path) {
		if (StrUtil.isBlank(path)) {
			return false;
		}
		// 目录验证
		if (isDir(path)) {
			return true;
		}
		if (CharUtil.isFileSeparator(path.charAt(path.length() - 1))) {
			return false;
		}

		final String fileName = FileNameUtil.getName(path);
		if (StrUtil.DOT.equals(fileName) || StrUtil.DOUBLE_DOT.equals(fileName)) {
			return false;
		}

		// 文件验证
		final String dir = StrUtil.defaultIfEmpty(StrUtil.removeSuffix(path, fileName), StrUtil.DOT);
		// issue#I7CSQ9 检查父目录为目录且是否存在
		if (!isDir(dir)) {
			return false;
		}
		final List<String> names;
		try {
			names = ls(dir);
		} catch (final FtpException ignore) {
			return false;
		}
		return containsIgnoreCase(names, fileName);
	}

	@Override
	public void mkDirs(final String dir) {
		final String[] dirs = StrUtil.trim(dir).split("[\\\\/]+");

		final String now = pwd();
		if (dirs.length > 0 && StrUtil.isEmpty(dirs[0])) {
			//首位为空，表示以/开头
			this.cd(StrUtil.SLASH);
		}
		for (final String s : dirs) {
			if (StrUtil.isNotEmpty(s)) {
				boolean exist = true;
				try {
					if (!cd(s)) {
						exist = false;
					}
				} catch (final FtpException e) {
					exist = false;
				}
				if (!exist) {
					//目录不存在时创建
					mkdir(s);
					cd(s);
				}
			}
		}
		// 切换回工作目录
		cd(now);
	}

	/**
	 * 下载文件-避免未完成的文件<br>
	 * 来自：<a href="https://gitee.com/dromara/hutool/pulls/407">https://gitee.com/dromara/hutool/pulls/407</a><br>
	 * 此方法原理是先在目标文件同级目录下创建临时文件，下载之，等下载完毕后重命名，避免因下载错误导致的文件不完整。
	 *
	 * @param path           文件路径
	 * @param outFile        输出文件或目录
	 * @param tempFileSuffix 临时文件后缀，默认".temp"
	 * @since 5.7.12
	 */
	public void download(final String path, File outFile, String tempFileSuffix) {
		if (StrUtil.isBlank(tempFileSuffix)) {
			tempFileSuffix = ".temp";
		} else {
			tempFileSuffix = StrUtil.addPrefixIfNot(tempFileSuffix, StrUtil.DOT);
		}

		// 目标文件真实名称
		final String fileName = outFile.isDirectory() ? FileNameUtil.getName(path) : outFile.getName();
		// 临时文件名称
		final String tempFileName = fileName + tempFileSuffix;

		// 临时文件
		outFile = new File(outFile.isDirectory() ? outFile : outFile.getParentFile(), tempFileName);
		try {
			download(path, outFile);
			// 重命名下载好的临时文件
			FileUtil.rename(outFile, fileName, true);
		} catch (final Throwable e) {
			// 异常则删除临时文件
			FileUtil.del(outFile);
			throw new FtpException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 是否包含指定字符串，忽略大小写
	 *
	 * @param names      文件或目录名列表
	 * @param nameToFind 要查找的文件或目录名
	 * @return 是否包含
	 */
	private static boolean containsIgnoreCase(final List<String> names, final String nameToFind) {
		if (StrUtil.isEmpty(nameToFind)) {
			return false;
		}
		return CollUtil.contains(names, nameToFind::equalsIgnoreCase);
	}
	// ---------------------------------------------------------------------------------------------------------------------------------------- Private method end
}
