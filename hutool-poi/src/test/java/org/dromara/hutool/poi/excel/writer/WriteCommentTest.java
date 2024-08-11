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

import org.dromara.hutool.poi.excel.ExcelUtil;
import org.dromara.hutool.poi.excel.SimpleClientAnchor;
import org.junit.jupiter.api.Test;

public class WriteCommentTest {
	@Test
	void writeCommentTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/comments.xlsx");
		final SimpleClientAnchor clientAnchor = new SimpleClientAnchor(0, 0, 1, 1);
		ExcelDrawingUtil.drawingCellComment(writer.getOrCreateCell(5, 6), clientAnchor, "测试内容");
		writer.close();
	}
}
