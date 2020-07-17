package cn.hutool.core.io.unit;

import cn.hutool.core.util.StrUtil;

/**
 * 数据单位封装<p>
 * 此类来自于：Spring-framework
 *
 * <pre>
 *     BYTES      1B      2^0     1
 *     KILOBYTES  1KB     2^10    1,024
 *     MEGABYTES  1MB     2^20    1,048,576
 *     GIGABYTES  1GB     2^30    1,073,741,824
 *     TERABYTES  1TB     2^40    1,099,511,627,776
 * </pre>
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