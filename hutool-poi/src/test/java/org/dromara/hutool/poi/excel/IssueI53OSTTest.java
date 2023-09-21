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

package org.dromara.hutool.poi.excel;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sax方式读取合并单元格，只有第一个单元格有值，其余为null
 */
public class IssueI53OSTTest {

	@Test
	@Disabled
	public void readTest(){
		final Map<String, Object> result = new HashMap<>();
		final List<Object> header = new ArrayList<>();

		ExcelUtil.readBySax("d:/test/sax_merge.xlsx", -1, (sheetIndex, rowIndex, rowCells) -> {
			if(rowIndex == 0){
				header.addAll(rowCells);
				return;
			}
			for (int i = 0; i < rowCells.size(); i++) {
				result.put((String) header.get(i), rowCells.get(i));
			}
			Console.log(result);
			result.clear();
		});
	}
}
