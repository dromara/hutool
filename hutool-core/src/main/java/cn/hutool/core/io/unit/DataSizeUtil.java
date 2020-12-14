package cn.hutool.core.io.unit;

import java.text.DecimalFormat;

/**
 * 数据大小工具类
 *
 * @author looly
 * @since 5.3.10
 */
public class DataSizeUtil {

	/**
	 * 解析数据大小字符串，转换为bytes大小
	 *
	 * @param text 数据大小字符串，类似于：12KB, 5MB等
	 * @return bytes大小
	 */
	public static long parse(String text) {
		return DataSize.parse(text).toBytes();
	}

	/**
	 * 可读的文件大小<br>
	 * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	 *
	 * @param size Long类型大小
	 * @return 大小
	 */
	public static String format(long size) {
		if (size <= 0) {
			return "0";
		}
		int digitGroups = Math.min(DataUnit.UNIT_NAMES.length-1, (int) (Math.log10(size) / Math.log10(1024)));
		return new DecimalFormat("#,##0.##")
				.format(size / Math.pow(1024, digitGroups)) + " " + DataUnit.UNIT_NAMES[digitGroups];
	}
}
