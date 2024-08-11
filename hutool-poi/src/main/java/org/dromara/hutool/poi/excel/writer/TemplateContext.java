/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.poi.excel.SheetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 模板上下文，记录了模板中变量所在的Cell
 *
 * @author Looly
 * @since 6.0.0
 */
public class TemplateContext {

	/**
	 * 变量正则
	 * <ol>
	 *     <li>变量名只能包含字母、数字、下划线、$符号、.符号，不能以数字开头</li>
	 *     <li>变量以 { 开始，以 } 结束</li>
	 *     <li>\{表示转义，非变量符号</li>
	 *     <li>.开头的变量表示列表，.出现在中间，表示表达式子对象</li>
	 * </ol>
	 */
	private static final Pattern VAR_PATTERN = Pattern.compile("(?<!\\\\)\\{([.$_a-zA-Z]+\\d*[.$_a-zA-Z]*)}");
	private static final Pattern ESCAPE_VAR_PATTERN = Pattern.compile("\\\\\\{([.$_a-zA-Z]+\\d*[.$_a-zA-Z]*)\\\\}");

	// 存储变量对应单元格的映射
	private final Map<String, Cell> varMap = new HashMap<>();

	/**
	 * 构造
	 *
	 * @param templateSheet 模板sheet
	 */
	public TemplateContext(final Sheet templateSheet) {
		init(templateSheet);
	}

	/**
	 * 获取变量对应的单元格，列表变量以.开头
	 *
	 * @param varName 变量名
	 * @return 单元格
	 */
	public Cell getCell(final String varName) {
		return varMap.get(varName);
	}

	/**
	 * 初始化，提取变量及位置，并将转义的变量回填
	 *
	 * @param templateSheet 模板sheet
	 */
	private void init(final Sheet templateSheet) {
		SheetUtil.walk(templateSheet, (cell, ctx) -> {
			if (CellType.STRING == cell.getCellType()) {
				// 只读取字符串类型的单元格
				final String cellValue = cell.getStringCellValue();

				// 字符串中可能有多个变量
				final List<String> vars = ReUtil.findAllGroup1(VAR_PATTERN, cellValue);
				if (CollUtil.isNotEmpty(vars)) {
					// 模板变量
					for (final String var : vars) {
						varMap.put(var, cell);
					}
				}

				// 替换转义的变量
				final String str = ReUtil.replaceAll(cellValue, ESCAPE_VAR_PATTERN, (matcher) -> "{" + matcher.group(1) + "}");
				if (!StrUtil.equals(cellValue, str)) {
					cell.setCellValue(str);
				}
			}
		});
	}
}
