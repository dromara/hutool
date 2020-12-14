package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * JDK8中新加入的java.time包对象解析转换器<br>
 * 支持的对象包括：
 *
 * <pre>
 * java.time.Instant
 * java.time.LocalDateTime
 * java.time.LocalDate
 * java.time.LocalTime
 * java.time.ZonedDateTime
 * java.time.OffsetDateTime
 * java.time.OffsetTime
 * </pre>
 *
 * @author looly
 * @since 5.0.0
 */
public class TemporalAccessorConverter extends AbstractConverter<TemporalAccessor> {
	private static final long serialVersionUID = 1L;

	private final Class<?> targetType;
	/**
	 * 日期格式化
	 */
	private String format;

	/**
	 * 构造
	 *
	 * @param targetType 目标类型
	 */
	public TemporalAccessorConverter(Class<?> targetType) {
		this(targetType, null);
	}

	/**
	 * 构造
	 *
	 * @param targetType 目标类型
	 * @param format     日期格式
	 */
	public TemporalAccessorConverter(Class<?> targetType, String format) {
		this.targetType = targetType;
		this.format = format;
	}

	/**
	 * 获取日期格式
	 *
	 * @return 设置日期格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置日期格式
	 *
	 * @param format 日期格式
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	protected TemporalAccessor convertInternal(Object value) {
		if (value instanceof Long) {
			return parseFromLong((Long) value);
		} else if (value instanceof TemporalAccessor) {
			return parseFromTemporalAccessor((TemporalAccessor) value);
		} else if (value instanceof Date) {
			final DateTime dateTime = DateUtil.date((Date) value);
			return parseFromInstant(dateTime.toInstant(), dateTime.getZoneId());
		}else if (value instanceof Calendar) {
			final Calendar calendar = (Calendar) value;
			return parseFromInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
		} else {
			return parseFromCharSequence(convertToStr(value));
		}
	}

	/**
	 * 通过反射从字符串转java.time中的对象
	 *
	 * @param value 字符串值
	 * @return 日期对象
	 */
	private TemporalAccessor parseFromCharSequence(CharSequence value) {
		if(StrUtil.isBlank(value)){
			return null;
		}

		final Instant instant;
		ZoneId zoneId;
		if (null != this.format) {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.format);
			instant = formatter.parse(value, Instant::from);
			zoneId = formatter.getZone();
		} else {
			final DateTime dateTime = DateUtil.parse(value);
			instant = Objects.requireNonNull(dateTime).toInstant();
			zoneId = dateTime.getZoneId();
		}
		return parseFromInstant(instant, zoneId);
	}

	/**
	 * 将Long型时间戳转换为java.time中的对象
	 *
	 * @param time 时间戳
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromLong(Long time) {
		return parseFromInstant(Instant.ofEpochMilli(time), null);
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param temporalAccessor TemporalAccessor对象
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromTemporalAccessor(TemporalAccessor temporalAccessor) {
		TemporalAccessor result = null;
		if(temporalAccessor instanceof LocalDateTime){
			result = parseFromLocalDateTime((LocalDateTime) temporalAccessor);
		} else if(temporalAccessor instanceof ZonedDateTime){
			result = parseFromZonedDateTime((ZonedDateTime) temporalAccessor);
		}

		if(null == result){
			result = parseFromInstant(DateUtil.toInstant(temporalAccessor), null);
		}

		return result;
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param localDateTime {@link LocalDateTime}对象
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromLocalDateTime(LocalDateTime localDateTime) {
		if(Instant.class.equals(this.targetType)){
			return DateUtil.toInstant(localDateTime);
		}
		if(LocalDate.class.equals(this.targetType)){
			return localDateTime.toLocalDate();
		}
		if(LocalTime.class.equals(this.targetType)){
			return localDateTime.toLocalTime();
		}
		if(ZonedDateTime.class.equals(this.targetType)){
			return localDateTime.atZone(ZoneId.systemDefault());
		}
		if(OffsetDateTime.class.equals(this.targetType)){
			return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
		}
		if(OffsetTime.class.equals(this.targetType)){
			return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime();
		}

		return null;
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param zonedDateTime {@link ZonedDateTime}对象
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromZonedDateTime(ZonedDateTime zonedDateTime) {
		if(Instant.class.equals(this.targetType)){
			return DateUtil.toInstant(zonedDateTime);
		}
		if(LocalDateTime.class.equals(this.targetType)){
			return zonedDateTime.toLocalDateTime();
		}
		if(LocalDate.class.equals(this.targetType)){
			return zonedDateTime.toLocalDate();
		}
		if(LocalTime.class.equals(this.targetType)){
			return zonedDateTime.toLocalTime();
		}
		if(OffsetDateTime.class.equals(this.targetType)){
			return zonedDateTime.toOffsetDateTime();
		}
		if(OffsetTime.class.equals(this.targetType)){
			return zonedDateTime.toOffsetDateTime().toOffsetTime();
		}

		return null;
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param instant {@link Instant}对象
	 * @param zoneId 时区ID，null表示当前系统默认的时区
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromInstant(Instant instant, ZoneId zoneId) {
		if(Instant.class.equals(this.targetType)){
			return instant;
		}

		zoneId = ObjectUtil.defaultIfNull(zoneId, ZoneId.systemDefault());

		TemporalAccessor result = null;
		if (LocalDateTime.class.equals(this.targetType)) {
			result = LocalDateTime.ofInstant(instant, zoneId);
		} else if (LocalDate.class.equals(this.targetType)) {
			result = instant.atZone(zoneId).toLocalDate();
		} else if (LocalTime.class.equals(this.targetType)) {
			result = instant.atZone(zoneId).toLocalTime();
		} else if (ZonedDateTime.class.equals(this.targetType)) {
			result = instant.atZone(zoneId);
		} else if (OffsetDateTime.class.equals(this.targetType)) {
			result = OffsetDateTime.ofInstant(instant, zoneId);
		} else if (OffsetTime.class.equals(this.targetType)) {
			result = OffsetTime.ofInstant(instant, zoneId);
		}
		return result;
	}
}
