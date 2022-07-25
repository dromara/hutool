package cn.hutool.json;

import cn.hutool.core.comparator.CompareUtil;

import java.io.Serializable;
import java.util.Comparator;

/**
 * JSON配置项
 *
 * @author looly
 * @since 4.1.19
 */
public class JSONConfig implements Serializable {
	private static final long serialVersionUID = 119730355204738278L;

	/**
	 * 键排序规则，{@code null}表示不排序，不排序情况下，按照加入顺序排序
	 */
	private Comparator<String> keyComparator;
	/**
	 * 是否忽略转换过程中的异常
	 */
	private boolean ignoreError;
	/**
	 * 是否忽略键的大小写
	 */
	private boolean ignoreCase;
	/**
	 * 日期格式，null表示默认的时间戳
	 */
	private String dateFormat;
	/**
	 * 是否忽略null值
	 */
	private boolean ignoreNullValue = true;
	/**
	 * 是否支持transient关键字修饰和@Transient注解，如果支持，被修饰的字段或方法对应的字段将被忽略。
	 */
	private boolean transientSupport = true;

	/**
	 * 是否去除末尾多余0，例如如果为true,5.0返回5
	 */
	private boolean stripTrailingZeros = true;

	/**
	 * 是否检查重复key
	 */
	private boolean checkDuplicate;

	/**
	 * 创建默认的配置项
	 *
	 * @return JSONConfig
	 */
	public static JSONConfig create() {
		return new JSONConfig();
	}

	/**
	 * 是否有序，顺序按照加入顺序排序，只针对JSONObject有效
	 *
	 * @return 是否有序
	 * @deprecated 始终返回 {@code true}
	 */
	@Deprecated
	public boolean isOrder() {
		return true;
	}

	/**
	 * 设置是否有序，顺序按照加入顺序排序，只针对JSONObject有效
	 *
	 * @param order 是否有序
	 * @return this
	 * @deprecated 始终有序，无需设置
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public JSONConfig setOrder(boolean order) {
		return this;
	}

	/**
	 * 获取键排序规则<br>
	 * 键排序规则，{@code null}表示不排序，不排序情况下，按照加入顺序排序
	 *
	 * @return 键排序规则
	 * @since 5.7.21
	 */
	public Comparator<String> getKeyComparator() {
		return this.keyComparator;
	}

	/**
	 * 设置自然排序，即按照字母顺序排序
	 *
	 * @return this
	 * @since 5.7.21
	 */
	public JSONConfig setNatureKeyComparator() {
		return setKeyComparator(CompareUtil.naturalComparator());
	}

	/**
	 * 设置键排序规则<br>
	 * 键排序规则，{@code null}表示不排序，不排序情况下，按照加入顺序排序
	 *
	 * @param keyComparator 键排序规则
	 * @return this
	 * @since 5.7.21
	 */
	public JSONConfig setKeyComparator(Comparator<String> keyComparator) {
		this.keyComparator = keyComparator;
		return this;
	}

	/**
	 * 是否忽略转换过程中的异常
	 *
	 * @return 是否忽略转换过程中的异常
	 */
	public boolean isIgnoreError() {
		return ignoreError;
	}

	/**
	 * 设置是否忽略转换过程中的异常
	 *
	 * @param ignoreError 是否忽略转换过程中的异常
	 * @return this
	 */
	public JSONConfig setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
		return this;
	}

	/**
	 * 是否忽略键的大小写
	 *
	 * @return 是否忽略键的大小写
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * 设置是否忽略键的大小写
	 *
	 * @param ignoreCase 是否忽略键的大小写
	 * @return this
	 */
	public JSONConfig setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	/**
	 * 日期格式，null表示默认的时间戳
	 *
	 * @return 日期格式，null表示默认的时间戳
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * 设置日期格式，null表示默认的时间戳<br>
	 * 此方法设置的日期格式仅对转换为JSON字符串有效，对解析JSON为bean无效。
	 *
	 * @param dateFormat 日期格式，null表示默认的时间戳
	 * @return this
	 */
	public JSONConfig setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	/**
	 * 是否忽略null值
	 *
	 * @return 是否忽略null值
	 */
	public boolean isIgnoreNullValue() {
		return this.ignoreNullValue;
	}

	/**
	 * 设置是否忽略null值
	 *
	 * @param ignoreNullValue 是否忽略null值
	 * @return this
	 */
	public JSONConfig setIgnoreNullValue(boolean ignoreNullValue) {
		this.ignoreNullValue = ignoreNullValue;
		return this;
	}

	/**
	 * 是否支持transient关键字修饰和@Transient注解，如果支持，被修饰的字段或方法对应的字段将被忽略。
	 *
	 * @return 是否支持
	 * @since 5.4.2
	 */
	public boolean isTransientSupport() {
		return this.transientSupport;
	}

	/**
	 * 设置是否支持transient关键字修饰和@Transient注解，如果支持，被修饰的字段或方法对应的字段将被忽略。
	 *
	 * @param transientSupport 是否支持
	 * @return this
	 * @since 5.4.2
	 */
	public JSONConfig setTransientSupport(boolean transientSupport) {
		this.transientSupport = transientSupport;
		return this;
	}

	/**
	 * 是否去除末尾多余0，例如如果为true,5.0返回5
	 *
	 * @return 是否去除末尾多余0，例如如果为true,5.0返回5
	 * @since 5.6.2
	 */
	public boolean isStripTrailingZeros() {
		return stripTrailingZeros;
	}

	/**
	 * 设置是否去除末尾多余0，例如如果为true,5.0返回5
	 *
	 * @param stripTrailingZeros 是否去除末尾多余0，例如如果为true,5.0返回5
	 * @return this
	 * @since 5.6.2
	 */
	public JSONConfig setStripTrailingZeros(boolean stripTrailingZeros) {
		this.stripTrailingZeros = stripTrailingZeros;
		return this;
	}

	/**
	 * 是否检查多个相同的key
	 *
	 * @return 是否检查多个相同的key
	 * @since 5.8.5
	 */
	public boolean isCheckDuplicate() {
		return checkDuplicate;
	}

	/**
	 * 是否检查多个相同的key
	 *
	 * @param checkDuplicate 是否检查多个相同的key
	 * @return this
	 * @since 5.8.5
	 */
	public JSONConfig setCheckDuplicate(boolean checkDuplicate) {
		this.checkDuplicate = checkDuplicate;
		return this;
	}
}
