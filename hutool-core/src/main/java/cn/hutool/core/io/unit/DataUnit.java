package cn.hutool.core.io.unit;

import cn.hutool.core.util.StrUtil;

/**
 * 数据单位封装<p>
 * 此类来自于：Spring-framework
 *
 * <p>
 * <table border="1">
 * <tr><th>名称</th><th>数据大小</th><th>Power&nbsp;of&nbsp;2</th><th>bytes表示</th></tr>
 * <tr><td>{@link #BYTES}</td><td>1B</td><td>2^0</td><td>1</td></tr>
 * <tr><td>{@link #KILOBYTES}</td><td>1KB</td><td>2^10</td><td>1,024</td></tr>
 * <tr><td>{@link #MEGABYTES}</td><td>1MB</td><td>2^20</td><td>1,048,576</td></tr>
 * <tr><td>{@link #GIGABYTES}</td><td>1GB</td><td>2^30</td><td>1,073,741,824</td></tr>
 * <tr><td>{@link #TERABYTES}</td><td>1TB</td><td>2^40</td><td>1,099,511,627,776</td></tr>
 * </table>
 *
 * @author Sam Brannen，Stephane Nicoll
 * @since 5.3.10
 */
public enum DataUnit {

	/**
	 * Bytes, 后缀表示为： {@code B}.
	 */
	BYTES("B", DataSize.ofBytes(1)),

	/**
	 * Kilobytes, 后缀表示为： {@code KB}.
	 */
	KILOBYTES("KB", DataSize.ofKilobytes(1)),

	/**
	 * Megabytes, 后缀表示为： {@code MB}.
	 */
	MEGABYTES("MB", DataSize.ofMegabytes(1)),

	/**
	 * Gigabytes, 后缀表示为： {@code GB}.
	 */
	GIGABYTES("GB", DataSize.ofGigabytes(1)),

	/**
	 * Terabytes, 后缀表示为： {@code TB}.
	 */
	TERABYTES("TB", DataSize.ofTerabytes(1));

	public static final String[] UNIT_NAMES = new String[]{"B", "kB", "MB", "GB", "TB", "EB"};

	private final String suffix;

	private final DataSize size;


	DataUnit(String suffix, DataSize size) {
		this.suffix = suffix;
		this.size = size;
	}

	DataSize size() {
		return this.size;
	}

	/**
	 * 通过后缀返回对应的 {@link DataUnit}
	 *
	 * @param suffix 单位后缀
	 * @return 匹配到的{@link DataUnit}
	 * @throws IllegalArgumentException 后缀无法识别报错
	 */
	public static DataUnit fromSuffix(String suffix) {
		for (DataUnit candidate : values()) {
			// 支持类似于 3MB，3M，3m等写法
			if (StrUtil.startWithIgnoreCase(candidate.suffix, suffix)) {
				return candidate;
			}
		}
		throw new IllegalArgumentException("Unknown data unit suffix '" + suffix + "'");
	}

}