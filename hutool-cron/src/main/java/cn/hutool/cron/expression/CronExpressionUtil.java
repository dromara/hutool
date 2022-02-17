package cn.hutool.cron.expression;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author apach
 */
public class CronExpressionUtil {


	/**
	 * 获取cron指定日期后的第一个匹配日期
	 * @param cron
	 * @param start
	 * @return
	 */
	public static Date getNextTime(String cron, Date start) {
		try {
			return new CronExpression(cron).getNextValidTimeAfter(start);
		} catch (ParseException e) {
			return null;
		}
	}


	/**
	 * 获取cron指定日期后的n个匹配日期
	 * @param cron
	 * @param start
	 * @param count
	 * @return
	 */
	public static List<Date> getNextTime(String cron, Date start, Integer count) {
		ArrayList<Date> result = new ArrayList<>();

		CronExpression cronExpression = null;
		try {
			cronExpression = new CronExpression(cron);
		} catch (ParseException e) {
			return result;
		}
		for (int i = 0; i < count; i++) {
			start = cronExpression.getNextValidTimeAfter(start);
			result.add(start);
		}

		return result;
	}

}
