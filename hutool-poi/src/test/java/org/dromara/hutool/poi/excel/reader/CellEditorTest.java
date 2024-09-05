/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.poi.excel.reader;

import org.dromara.hutool.poi.excel.ExcelUtil;
import org.dromara.hutool.poi.excel.cell.editors.CellEditor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.Assertions;

import java.io.Serializable;
import java.util.List;

public class CellEditorTest {

	@org.junit.jupiter.api.Test
	public void readTest(){
		final ExcelReader excelReader= ExcelUtil.getReader("cell_editor_test.xlsx");
		excelReader.getConfig().setCellEditor(new ExcelHandler());
		final List<Test> excelReaderObjects=excelReader.readAll(Test.class);

		Assertions.assertEquals("0", excelReaderObjects.get(0).getTest1());
		Assertions.assertEquals("b", excelReaderObjects.get(0).getTest2());
		Assertions.assertEquals("0", excelReaderObjects.get(1).getTest1());
		Assertions.assertEquals("b1", excelReaderObjects.get(1).getTest2());
		Assertions.assertEquals("0", excelReaderObjects.get(2).getTest1());
		Assertions.assertEquals("c2", excelReaderObjects.get(2).getTest2());
	}

	@AllArgsConstructor
	@Data
	public static class Test implements Serializable {
		private static final long serialVersionUID = 1L;

		private String test1;
		private String test2;
	}

	public static class ExcelHandler implements CellEditor {
		@ Override
		public Object edit(final Cell cell, Object o) {
			if (cell.getColumnIndex()==0 && cell.getRowIndex() != 0){
				o="0";
			}
			return o;
		}
	}
}
