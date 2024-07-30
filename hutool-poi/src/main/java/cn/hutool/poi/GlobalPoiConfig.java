package cn.hutool.poi;

import org.apache.poi.openxml4j.util.ZipSecureFile;

/**
 * POI的全局设置
 *
 * @author Looly
 * @see ZipSecureFile
 * @since 5.8.30
 */
public class GlobalPoiConfig {

	/**
	 * 设置解压时的最小压缩比例<br>
	 * 为了避免`Zip Bomb`，POI中设置了最小压缩比例，这个比例为：
	 * <pre>
	 *     压缩后的大小/解压后的大小
	 * </pre>
	 * <p>
	 * POI的默认值是0.01（即最小压缩到1%），如果文档中的文件压缩比例小于这个值，就会报错。<br>
	 * 如果文件中确实存在高压缩比的文件，可以通过这个全局方法自定义比例，从而避免错误。
	 *
	 * @param ratio 解压后的文件大小与原始文件大小的最小比率，小于等于0表示不检查
	 */
	public static void setMinInflateRatio(final double ratio) {
		ZipSecureFile.setMinInflateRatio(ratio);
	}

	/**
	 * 设置单个Zip文件中最大文件大小，默认为4GB，即32位zip格式的最大值。
	 *
	 * @param maxEntrySize 单个Zip文件中最大文件大小，必须大于0
	 */
	public static void setMaxEntrySize(final long maxEntrySize) {
		ZipSecureFile.setMaxEntrySize(maxEntrySize);
	}

	/**
	 * 设置解压前文本的最大字符数，超过抛出异常。
	 *
	 * @param maxTextSize 文本的最大字符数
	 * @throws IllegalArgumentException for negative maxTextSize
	 */
	public static void setMaxTextSize(final long maxTextSize) {
		ZipSecureFile.setMaxTextSize(maxTextSize);
	}
}
