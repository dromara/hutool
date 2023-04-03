/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.template;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * 抽象模板接口
 *
 * @author looly
 */
public interface Template {

	/**
	 * 将模板与绑定参数融合后输出到Writer
	 *
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param writer     输出
	 */
	void render(Map<?, ?> bindingMap, Writer writer);

	/**
	 * 将模板与绑定参数融合后输出到流
	 *
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param out        输出
	 */
	void render(Map<?, ?> bindingMap, OutputStream out);

	/**
	 * 写出到文件
	 *
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param file       输出到的文件
	 */
	default void render(final Map<?, ?> bindingMap, final File file) {
		BufferedOutputStream out = null;
		try {
			out = FileUtil.getOutputStream(file);
			this.render(bindingMap, out);
		} finally {
			IoUtil.closeQuietly(out);
		}
	}

	/**
	 * 将模板与绑定参数融合后返回为字符串
	 *
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @return 融合后的内容
	 */
	default String render(final Map<?, ?> bindingMap) {
		final StringWriter writer = new StringWriter();
		render(bindingMap, writer);
		return writer.toString();
	}
}
