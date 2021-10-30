package cn.hutool.poi.excel;

import cn.hutool.poi.excel.cell.CellEditor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Assert;

import java.io.Serializable;
import java.util.List;

public class CellEditorTest {

	@org.junit.Test
	public void readTest(){
		ExcelReader excelReader= ExcelUtil.getReader("cell_editor_test.xlsx");
		excelReader.setCellEditor(new ExcelHandler());
		List<Test> excelReaderObjects=excelReader.readAll(Test.class);

		Assert.assertEquals("0", excelReaderObjects.get(0).getTest1());
		Assert.assertEquals("b", excelReaderObjects.get(0).getTest2());
		Assert.assertEquals("0", excelReaderObjects.get(1).getTest1());
		Assert.assertEquals("b1", excelReaderObjects.get(1).getTest2());
		Assert.assertEquals("0", excelReaderObjects.get(2).getTest1());
		Assert.assertEquals("c2", excelReaderObjects.get(2).getTest2());
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
		public Object edit(Cell cell, Object o) {
			if (cell.getColumnIndex()==0 && cell.getRowIndex() != 0){
				o="0";
			}
			return o;
		}
	}
}
