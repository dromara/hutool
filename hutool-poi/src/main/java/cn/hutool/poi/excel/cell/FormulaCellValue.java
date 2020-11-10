package cn.hutool.poi.excel.cell;

/**
 * 公式类型的值
 *
 * @author looly
 * @since 4.0.11
 */
public class FormulaCellValue implements CellValue<String> {

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
