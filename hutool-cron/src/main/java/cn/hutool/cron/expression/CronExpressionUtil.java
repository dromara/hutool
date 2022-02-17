package cn.hutool.cron.expression;

import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.pattern.CronPatternUtil;

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
	 *
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
	 *
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


	/**
	 * 对比测试
	 * @param args
	 */
	public static void main(String[] args) {
		//耗时约5秒并且只查到当年，可能返回null
		Date date = CronPatternUtil.nextDateAfter(new CronPattern("* 12 6 6 2 ?"), new Date(), true);

		//无耗时
		Date nextTime = CronExpressionUtil.getNextTime("* 12 6 6 2 ?", new Date());
	}

}
