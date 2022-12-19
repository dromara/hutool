package cn.hutool.core.date;

import java.time.LocalDate;
import java.util.List;

/**
 * 节假日和调休日配置类
 *
 * @author hhhhhxm
 */
public class DateConfig {
	/**
	 * 年份
	 */
	private Integer year;

	/**
	 * 假日
	 */
	private List<DateGap> holidayList;
	/**
	 * 调休日
	 */
	private List<DateGap> workDayList;

	public DateConfig() {
	}

	public DateConfig(Integer year, List<DateGap> holidayList, List<DateGap> workDayList) {
		this.year = year;
		this.holidayList = holidayList;
		this.workDayList = workDayList;
	}


	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<DateGap> getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(List<DateGap> holidayList) {
		this.holidayList = holidayList;
	}

	public List<DateGap> getWorkDayList() {
		return workDayList;
	}

	public void setWorkDayList(List<DateGap> workDayList) {
		this.workDayList = workDayList;
	}

	/**
	 * 日期间隙 用来描述开始日期和结束日期
	 *
	 * @author hhhhhxm
	 */
	public static class DateGap {
		/**
		 * 开始日期
		 */
		private LocalDate startDate;
		/**
		 * 结束日期
		 */
		private LocalDate endDate;

		public DateGap() {
		}

		public DateGap(LocalDate startDate, LocalDate endDate) {
			this.startDate = startDate;
			this.endDate = endDate;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}
	}
}
