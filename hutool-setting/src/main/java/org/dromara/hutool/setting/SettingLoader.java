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

package org.dromara.hutool.setting;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.LineReader;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.util.SystemUtil;
import org.dromara.hutool.log.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Setting文件加载器
 *
 * @author Looly
 */
public class SettingLoader {
	private static final Log log = Log.get();

	/**
	 * 注释符号（当有此符号在行首，表示此行为注释）
	 */
	private final static char COMMENT_FLAG_PRE = '#';

	/**
	 * 本设置对象的字符集
	 */
	private final Charset charset;
	/**
	 * 是否使用变量
	 */
	private final boolean isUseVariable;
	/**
	 * 赋值分隔符（用于分隔键值对）
	 */
	private char assignFlag = '=';
	/**
	 * 变量名称的正则
	 */
	private String varRegex = "\\$\\{(.*?)\\}";
	/**
	 * 值编辑器
	 */
	private ValueEditor valueEditor;

	/**
	 * 构造
	 *
	 * @param charset       编码
	 * @param isUseVariable 是否使用变量
	 */
	public SettingLoader(final Charset charset, final boolean isUseVariable) {
		this.charset = charset;
		this.isUseVariable = isUseVariable;
	}

	// region ----- setXXX

	/**
	 * 设置变量的正则<br>
	 * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
	 *
	 * @param regex 正则
	 * @return this
	 */
	public SettingLoader setVarRegex(final String regex) {
		this.varRegex = regex;
		return this;
	}

	/**
	 * 赋值分隔符（用于分隔键值对）
	 *
	 * @param assignFlag 正则
	 * @return this
	 * @since 4.6.5
	 */
	public SettingLoader setAssignFlag(final char assignFlag) {
		this.assignFlag = assignFlag;
		return this;
	}

	/**
	 * 设置值编辑器，用于在获取值后编辑返回值，例如解密等<br>
	 * 编辑器函数接受一个参数，此参数为待编辑的值，函数返回编辑后的值<br>
	 * 注意：此函数调用在变量替换前
	 *
	 * @param valueEditor 编辑器函数
	 * @return this
	 * @since 6.0.0
	 */
	public SettingLoader setValueEditor(final ValueEditor valueEditor) {
		this.valueEditor = valueEditor;
		return this;
	}
	// endregion

	// region ----- load

	/**
	 * 加载设置文件
	 *
	 * @param resource 配置文件URL
	 * @return 加载是否成功
	 */
	public GroupedMap load(final Resource resource) {
		if (resource == null) {
			throw new NullPointerException("Null setting url define!");
		}
		log.debug("Load setting file [{}]", resource);
		InputStream settingStream = null;
		try {
			settingStream = resource.getStream();
			return load(settingStream);
		} catch (final Exception e) {
			log.error(e, "Load setting error!");
			// 加载错误跳过，返回空的map
			return new GroupedMap();
		} finally {
			IoUtil.closeQuietly(settingStream);
		}
	}

	/**
	 * 加载设置文件。 此方法不会关闭流对象
	 *
	 * @param settingStream 文件流
	 * @return {@link GroupedMap}
	 * @throws IOException IO异常
	 */
	synchronized public GroupedMap load(final InputStream settingStream) throws IOException {
		final GroupedMap groupedMap = new GroupedMap();
		LineReader reader = null;
		try {
			reader = new LineReader(settingStream, this.charset);
			// 分组
			String group = null;

			String line;
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				line = StrUtil.trim(line);
				// 跳过注释行和空行
				if (StrUtil.isBlank(line) || StrUtil.startWith(line, COMMENT_FLAG_PRE)) {
					continue;
				}

				// 记录分组名
				if (StrUtil.isWrap(line, CharUtil.BRACKET_START, CharUtil.BRACKET_END)) {
					group = StrUtil.trim(line.substring(1, line.length() - 1));
					continue;
				}

				final String[] keyValue = SplitUtil.split(line, String.valueOf(this.assignFlag), 2, true, false)
					.toArray(new String[0]);
				// 跳过不符合键值规范的行
				if (keyValue.length < 2) {
					continue;
				}

				final String key = StrUtil.trim(keyValue[0]);
				String value = keyValue[1];
				if (null != this.valueEditor) {
					value = this.valueEditor.edit(group, key, value);
				}

				// 替换值中的所有变量变量（变量必须是此行之前定义的变量，否则无法找到）
				if (this.isUseVariable) {
					value = replaceVar(groupedMap, group, value);
				}
				groupedMap.put(group, key, value);
			}
		} finally {
			IoUtil.closeQuietly(reader);
		}

		return groupedMap;
	}
	// endregion

	// region ----- store

	/**
	 * 持久化当前设置，会覆盖掉之前的设置<br>
	 * 持久化会不会保留之前的分组
	 *
	 * @param groupedMap   分组map
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(final GroupedMap groupedMap, final String absolutePath) {
		store(groupedMap, FileUtil.touch(absolutePath));
	}

	/**
	 * 持久化当前设置，会覆盖掉之前的设置<br>
	 * 持久化会不会保留之前的分组
	 *
	 * @param groupedMap 分组map
	 * @param file       设置文件
	 */
	public void store(final GroupedMap groupedMap, final File file) {
		Assert.notNull(file, "File to store must be not null !");
		log.debug("Store Setting to [{}]...", file.getAbsolutePath());
		PrintWriter writer = null;
		try {
			writer = FileUtil.getPrintWriter(file, charset, false);
			store(groupedMap, writer);
		} finally {
			IoUtil.closeQuietly(writer);
		}
	}

	/**
	 * 存储到Writer
	 *
	 * @param groupedMap 分组Map
	 * @param writer     Writer
	 */
	synchronized private void store(final GroupedMap groupedMap, final PrintWriter writer) {
		for (final Entry<String, LinkedHashMap<String, String>> groupEntry : groupedMap.entrySet()) {
			writer.println(StrUtil.format("{}{}{}", CharUtil.BRACKET_START, groupEntry.getKey(), CharUtil.BRACKET_END));
			for (final Entry<String, String> entry : groupEntry.getValue().entrySet()) {
				writer.println(StrUtil.format("{} {} {}", entry.getKey(), this.assignFlag, entry.getValue()));
			}
		}
	}
	// endregion

	// ----------------------------------------------------------------------------------- Private method start

	/**
	 * 替换给定值中的变量标识
	 *
	 * @param group 所在分组
	 * @param value 值
	 * @return 替换后的字符串
	 */
	private String replaceVar(final GroupedMap groupedMap, final String group, String value) {
		// 找到所有变量标识
		final Set<String> vars = ReUtil.findAll(varRegex, value, 0, new HashSet<>());
		String key;
		for (final String var : vars) {
			key = ReUtil.get(varRegex, var, 1);
			if (StrUtil.isNotBlank(key)) {
				// 本分组中查找变量名对应的值
				String varValue = groupedMap.get(group, key);
				// 跨分组查找
				if (null == varValue) {
					final List<String> groupAndKey = SplitUtil.split(key, StrUtil.DOT, 2, true, false);
					if (groupAndKey.size() > 1) {
						varValue = groupedMap.get(groupAndKey.get(0), groupAndKey.get(1));
					}
				}
				// 系统参数和环境变量中查找
				if (null == varValue) {
					varValue = SystemUtil.get(key);
				}

				if (null != varValue) {
					// 替换标识
					value = value.replace(var, varValue);
				}
			}
		}
		return value;
	}
	// ----------------------------------------------------------------------------------- Private method end

	/**
	 * 值编辑器，用于在加载配置文件时对值进行编辑，例如解密等<br>
	 * 此接口用于在加载配置文件时，编辑值，例如解密等，从而加载出明文的配置值
	 *
	 */
	@FunctionalInterface
	public interface ValueEditor {
		/**
		 * 编辑值
		 *
		 * @param group 分组
		 * @param key   键
		 * @param value 值
		 * @return 编辑后的值
		 */
		String edit(String group, String key, String value);
	}
}
