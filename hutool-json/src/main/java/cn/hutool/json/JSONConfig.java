package cn.hutool.json;

import java.io.Serializable;

/**
 * JSON配置项
 *
 * @author looly
 * @since 4.1.19
 */
public class JSONConfig implements Serializable {
	private static final long serialVersionUID = 119730355204738278L;

	/**
	 * 是否有序，顺序按照加入顺序排序
	 */
	private boolean order;
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
	 * 创建默认的配置项
	 *
	 * @return JSONConfig
	 */
	public static JSONConfig create() {
		return new JSONConfig();
	}

	/**
	 * 是否有序，顺序按照加入顺序排序
	 *
	 * @return 是否有序
	 */
	public boolean isOrder() {
		return order;
	}

	/**
	 * 设置是否有序，顺序按照加入顺序排序
	 *
	 * @param order 是否有序
	 * @return this
	 */
	public JSONConfig setOrder(boolean order) {
		this.order = order;
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
	 * 设置日期格式，null表示默认的时间戳
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
	 * 是否忽略transient关键字修饰的字段
	 *
	 * @return 是否忽略transient关键字修饰的字段
	 * @since 5.3.11
	 * @deprecated 此方法名称有二义性，请使用{@link #isTransientSupport()}
	 */
	@Deprecated
	public boolean isIgnoreTransient() {
		return isTransientSupport();
	}

	/**
	 * 设置是否忽略transient关键字修饰的字段
	 *
	 * @param ignoreTransient 是否忽略transient关键字修饰的字段
	 * @return this
	 * @since 5.3.11
	 * @deprecated 此方法名称有二义性，请使用{@link #setTransientSupport(boolean)}
	 */
	@Deprecated
	public JSONConfig setIgnoreTransient(boolean ignoreTransient) {
		return setTransientSupport(ignoreTransient);
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
}
