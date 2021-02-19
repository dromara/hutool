package cn.hutool.core.text.csv;

/**
 * CSV的行处理器，实现此接口用于按照行处理数据
 *
 * @author Looly
 * @since 5.0.4
 */
@FunctionalInterface
public interface CsvRowHandler {

	/**
	 * 处理行数据
	 *
	 * @param row 行数据
	 */
	void handle(CsvRow row);
}
