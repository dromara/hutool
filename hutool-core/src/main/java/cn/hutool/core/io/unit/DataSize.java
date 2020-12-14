package cn.hutool.core.io.unit;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据大小，可以将类似于'12MB'表示转换为bytes长度的数字
 * <p>
 * 此类来自于：Spring-framework
 *
 * <pre>
 *     byte        1B     1
 *     kilobyte    1KB    1,024
 *     megabyte    1MB    1,048,576
 *     gigabyte    1GB    1,073,741,824
 *     terabyte    1TB    1,099,511,627,776
 * </pre>
 *
 * @author Sam Brannen,Stephane Nicoll
 * @since 5.3.10
 */
public final class DataSize implements Comparable<DataSize> {

	/**
	 * The pattern for parsing.
	 */
	private static final Pattern PATTERN = Pattern.compile("^([+-]?\\d+(\\.\\d+)?)([a-zA-Z]{0,2})$");

	/**
	 * Bytes per Kilobyte(KB).
	 */
	private static final long BYTES_PER_KB = 1024;

	/**
	 * Bytes per Megabyte(MB).
	 */
	private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;

	/**
	 * Bytes per Gigabyte(GB).
	 */
	private static final long BYTES_PER_GB = BYTES_PER_MB * 1024;

	/**
	 * Bytes per Terabyte(TB).
	 */
	private static final long BYTES_PER_TB = BYTES_PER_GB * 1024;


	/**
	 * bytes长度
	 */
	private final long bytes;


	/**
	 * 构造
	 *
	 * @param bytes 长度
	 */
	private DataSize(long bytes) {
		this.bytes = bytes;
	}


	/**
	 * 获得对应bytes的DataSize
	 *
	 * @param bytes bytes大小，可正可负
	 * @return this
	 */
	public static DataSize ofBytes(long bytes) {
		return new DataSize(bytes);
	}

	/**
	 * 获得对应kilobytes的DataSize
	 *
	 * @param kilobytes kilobytes大小，可正可负
	 * @return a DataSize
	 */
	public static DataSize ofKilobytes(long kilobytes) {
		return new DataSize(Math.multiplyExact(kilobytes, BYTES_PER_KB));
	}

	/**
	 * 获得对应megabytes的DataSize
	 *
	 * @param megabytes megabytes大小，可正可负
	 * @return a DataSize
	 */
	public static DataSize ofMegabytes(long megabytes) {
		return new DataSize(Math.multiplyExact(megabytes, BYTES_PER_MB));
	}

	/**
	 * 获得对应gigabytes的DataSize
	 *
	 * @param gigabytes gigabytes大小，可正可负
	 * @return a DataSize
	 */
	public static DataSize ofGigabytes(long gigabytes) {
		return new DataSize(Math.multiplyExact(gigabytes, BYTES_PER_GB));
	}

	/**
	 * 获得对应terabytes的DataSize
	 *
	 * @param terabytes terabytes大小，可正可负
	 * @return a DataSize
	 */
	public static DataSize ofTerabytes(long terabytes) {
		return new DataSize(Math.multiplyExact(terabytes, BYTES_PER_TB));
	}

	/**
	 * 获得指定{@link DataUnit}对应的DataSize
	 *
	 * @param amount 大小
	 * @param unit 数据大小单位，null表示默认的BYTES
	 * @return DataSize
	 */
	public static DataSize of(long amount, DataUnit unit) {
		if(null == unit){
			unit = DataUnit.BYTES;
		}
		return new DataSize(Math.multiplyExact(amount, unit.size().toBytes()));
	}

	/**
	 * 获得指定{@link DataUnit}对应的DataSize
	 *
	 * @param amount 大小
	 * @param unit 数据大小单位，null表示默认的BYTES
	 * @return DataSize
	 * @since 5.4.5
	 */
	public static DataSize of(BigDecimal amount, DataUnit unit) {
		if(null == unit){
			unit = DataUnit.BYTES;
		}
		return new DataSize(amount.multiply(new BigDecimal(unit.size().toBytes())).longValue());
	}

	/**
	 * 获取指定数据大小文本对应的DataSize对象，如果无单位指定，默认获取{@link DataUnit#BYTES}
	 * <p>
	 * 例如：
	 * <pre>
	 * "12KB" -- parses as "12 kilobytes"
	 * "5MB"  -- parses as "5 megabytes"
	 * "20"   -- parses as "20 bytes"
	 * </pre>
	 *
	 * @param text the text to parse
	 * @return the parsed DataSize
	 * @see #parse(CharSequence, DataUnit)
	 */
	public static DataSize parse(CharSequence text) {
		return parse(text, null);
	}

	/**
	 * Obtain a DataSize from a text string such as {@code 12MB} using
	 * the specified default {@link DataUnit} if no unit is specified.
	 * <p>
	 * The string starts with a number followed optionally by a unit matching one of the
	 * supported {@linkplain DataUnit suffixes}.
	 * <p>
	 * Examples:
	 * <pre>
	 * "12KB" -- parses as "12 kilobytes"
	 * "5MB"  -- parses as "5 megabytes"
	 * "20"   -- parses as "20 kilobytes" (where the {@code defaultUnit} is {@link DataUnit#KILOBYTES})
	 * </pre>
	 *
	 * @param text the text to parse
	 * @param defaultUnit 默认的数据单位
	 * @return the parsed DataSize
	 */
	public static DataSize parse(CharSequence text, DataUnit defaultUnit) {
		Assert.notNull(text, "Text must not be null");
		try {
			final Matcher matcher = PATTERN.matcher(text);
			Assert.state(matcher.matches(), "Does not match data size pattern");

			final DataUnit unit = determineDataUnit(matcher.group(3), defaultUnit);
			return DataSize.of(new BigDecimal(matcher.group(1)), unit);
		} catch (Exception ex) {
			throw new IllegalArgumentException("'" + text + "' is not a valid data size", ex);
		}
	}

	/**
	 * 决定数据单位，后缀不识别时使用默认单位
	 * @param suffix 后缀
	 * @param defaultUnit 默认单位
	 * @return {@link DataUnit}
	 */
	private static DataUnit determineDataUnit(String suffix, DataUnit defaultUnit) {
		DataUnit defaultUnitToUse = (defaultUnit != null ? defaultUnit : DataUnit.BYTES);
		return (StrUtil.isNotEmpty(suffix) ? DataUnit.fromSuffix(suffix) : defaultUnitToUse);
	}

	/**
	 * 是否为负数，不包括0
	 *
	 * @return 负数返回true，否则false
	 */
	public boolean isNegative() {
		return this.bytes < 0;
	}

	/**
	 * 返回bytes大小
	 *
	 * @return bytes大小
	 */
	public long toBytes() {
		return this.bytes;
	}

	/**
	 * 返回KB大小
	 *
	 * @return KB大小
	 */
	public long toKilobytes() {
		return this.bytes / BYTES_PER_KB;
	}

	/**
	 * 返回MB大小
	 *
	 * @return MB大小
	 */
	public long toMegabytes() {
		return this.bytes / BYTES_PER_MB;
	}

	/**
	 * 返回GB大小
	 *
	 * @return GB大小
	 *
	 */
	public long toGigabytes() {
		return this.bytes / BYTES_PER_GB;
	}

	/**
	 * 返回TB大小
	 *
	 * @return TB大小
	 */
	public long toTerabytes() {
		return this.bytes / BYTES_PER_TB;
	}

	@Override
	public int compareTo(DataSize other) {
		return Long.compare(this.bytes, other.bytes);
	}

	@Override
	public String toString() {
		return String.format("%dB", this.bytes);
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		DataSize otherSize = (DataSize) other;
		return (this.bytes == otherSize.bytes);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(this.bytes);
	}

}
