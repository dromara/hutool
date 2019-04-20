package cn.hutool.core.date;

import java.util.Calendar;

import cn.hutool.core.util.ArrayUtil;

/**
 * 日期修改器<br>
 * 用于实现自定义某个日期字段的调整，包括：
 * 
 * <pre>
 * 1. 获取指定字段的起始时间
 * 2. 获取指定字段的四舍五入时间
 * 3. 获取指定字段的结束时间
 * </pre>
 * 
 * @author looly
 *
 */
public class DateModifier {

	/** 忽略的字段 */
	private static final int[] ignoreFields = new int[] { //
			Calendar.HOUR, //
			Calendar.AM_PM, //
			Calendar.DAY_OF_WEEK, //
			Calendar.DAY_OF_YEAR, //
			Calendar.WEEK_OF_YEAR//
	};

	/**
	 * 修改日期
	 * 
	 * @param calendar {@link Calendar}
	 * @param dateField 日期字段，既保留到哪个日期字段
	 * @param modifyType 修改类型，包括舍去、四舍五入、进一等
	 * @return 修改后的{@link Calendar}
	 */
	public static Calendar modify(Calendar calendar, int dateField, ModifyType modifyType) {
		// 上下午特殊处理
		if (Calendar.AM_PM == dateField) {
			boolean isAM = DateUtil.isAM(calendar);
			switch (modifyType) {
			case TRUNCATE:
				calendar.set(Calendar.HOUR_OF_DAY, isAM ? 0 : 12);
				break;
			case CEILING:
				calendar.set(Calendar.HOUR_OF_DAY, isAM ? 11 : 23);
				break;
			case ROUND:
				int min = isAM ? 0 : 12;
				int max = isAM ? 11 : 23;
				int href = (max - min) / 2 + 1;
				int value = calendar.get(Calendar.HOUR_OF_DAY);
				calendar.set(Calendar.HOUR_OF_DAY, (value < href) ? min : max);
				break;
			}
		}

		// 当用户指定了无关字段时，降级字段
		if (ArrayUtil.contains(ignoreFields, dateField)) {
			return modify(calendar, dateField + 1, modifyType);
		}

		for (int i = Calendar.MILLISECOND; i > dateField; i--) {
			if (ArrayUtil.contains(ignoreFields, i) || Calendar.WEEK_OF_MONTH == i) {
				// 忽略无关字段（WEEK_OF_MONTH）始终不做修改
				continue;
			}

			if (Calendar.WEEK_OF_MONTH == dateField) {
				// 在星期模式下，月的处理忽略之
				if (Calendar.DAY_OF_MONTH == i) {
					continue;
				} else if (Calendar.DAY_OF_WEEK_IN_MONTH == i) {
					// 星期模式下，星期几统一用DAY_OF_WEEK处理
					i = Calendar.DAY_OF_WEEK;
				}
			} else if (Calendar.DAY_OF_WEEK_IN_MONTH == i) {
				// 非星期模式下，星期处理忽略之
				// 由于DAY_OF_WEEK忽略，自动降级到DAY_OF_WEEK_IN_MONTH
				continue;
			}

			modifyField(calendar, i, modifyType);
		}
		return calendar;
	}

	// -------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 修改日期字段值
	 * 
	 * @param calendar {@link Calendar}
	 * @param field 字段，见{@link Calendar}
	 * @param modifyType {@link ModifyType}
	 */
	private static void modifyField(Calendar calendar, int field, ModifyType modifyType) {
		// Console.log("# {} {}", DateField.of(field), calendar.getActualMinimum(field));
		switch (modifyType) {
		case TRUNCATE:
			calendar.set(field, DateUtil.getBeginValue(calendar, field));
			break;
		case CEILING:
			calendar.set(field, DateUtil.getEndValue(calendar, field));
			break;
		case ROUND:
			int min = DateUtil.getBeginValue(calendar, field);
			int max = DateUtil.getEndValue(calendar, field);
			int href;
			if (Calendar.DAY_OF_WEEK == field) {
				// 星期特殊处理，假设周一是第一天，中间的为周四
				href = (min + 3) % 7;
			} else {
				href = (max - min) / 2 + 1;
			}
			int value = calendar.get(field);
			calendar.set(field, (value < href) ? min : max);
			break;
		}
	}
	// -------------------------------------------------------------------------------------------------- Private method end

	/**
	 * 修改类型
	 * 
	 * @author looly
	 *
	 */
	public static enum ModifyType {
		/**
		 * 取指定日期短的起始值.
		 */
		TRUNCATE,

		/**
		 * 指定日期属性按照四舍五入处理
		 */
		ROUND,

		/**
		 * 指定日期属性按照进一法处理
		 */
		CEILING
	}
}
