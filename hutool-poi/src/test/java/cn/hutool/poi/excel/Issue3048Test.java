package cn.hutool.poi.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/dromara/hutool/issues/3048
 * Excel导出javaBean中有BigDecimal类型精度流失
 *
 */
public class Issue3048Test {
	@Test
	@Ignore
	public void excelOutPutBeanListToExcel(){
		List<TestBean> excelExportList = new ArrayList<>();
		excelExportList.add(new TestBean("1", new BigDecimal("1.22")));
		excelExportList.add(new TestBean("2", new BigDecimal("2.342")));
		excelExportList.add(new TestBean("3", new BigDecimal("1.2346453453534534543545")));
		ExcelWriter excelWriter = ExcelUtil.getWriter(true);
		//excelWriter.setNumberAutoPrecision(true);
		excelWriter.write(excelExportList, true);
		excelWriter.getStyleSet().getCellStyleForNumber().setDataFormat((short) 0);
		excelWriter.flush(new File("d:/test/test.xlsx"));
		excelWriter.close();
	}

	@Data
	@AllArgsConstructor
	static class TestBean{
		private String testKey;
		private BigDecimal testValue;
	}
}
