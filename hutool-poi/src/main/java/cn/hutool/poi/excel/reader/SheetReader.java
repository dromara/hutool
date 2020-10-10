package cn.hutool.poi.excel.reader;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excel {@link Sheet}读取接口，通过实现此接口，将{@link Sheet}中的数据读取为不同类型。
 *
 * @param <T> 读取的数据类型
 */
@FunctionalInterface
public interface SheetReader<T> {

	/**
	 * 读取数据
	 *
	 * @param sheet {@link Sheet}
	 * @return 读取结果
	 */
	T read(Sheet sheet);
}
