package cn.hutool.poi.excel.cell;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 公式类型的值<br>
 *
 * <ul>
 *     <li>在Sax读取模式时，此对象用于接收单元格的公式以及公式结果值信息</li>
 *     <li>在写出模式时，用于定义写出的单元格类型为公式</li>
 * </ul>
 *
 * @author looly
 * @since 4.0.11
 */
public class FormulaCellValue implements CellValue<String>, CellSetter {

	/**
	 * 公式
	 */
	String formula;
	/**
	 * 结果，使用ExcelWriter时可以不用
	 */
	Object result;

	/**
	 * 构造
	 *
	 * @param formula 公式
	 */
	public FormulaCellValue(String formula) {
		this(formula, null);
	}

	/**
	 * 构造
	 *
	 * @param formula 公式
	 * @param result  结果
	 */
	public FormulaCellValue(String formula, Object result) {
		this.formula = formula;
		this.result = result;
	}

	@Override
	public String getValue() {
		return this.formula;
	}

	@Override
	public void setValue(Cell cell) {
		cell.setCellFormula(this.formula);
	}

	/**
	 * 获取结果
	 * @return 结果
	 */
	public Object getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return getResult().toString();
	}
}
