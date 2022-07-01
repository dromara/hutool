package cn.hutool.json.test.bean;

import cn.hutool.core.annotation.JsonDateFormat;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 测试json时间格式化
 *
 *
 * @author duhanmin
 */
public class JsonDateFormatBean {
	@JsonDateFormat("yyyy-mm-dd hh:mm:ss")
	private java.util.Date date1;
	@JsonDateFormat("yyyy-mm-dd hh:mm:ss")
	private java.sql.Date date2;
	@JsonDateFormat("yyyy-mm-dd hh:mm:ss")
	private java.sql.Time date3;
	@JsonDateFormat("yyyy-mm-dd hh:mm:ss")
	private java.sql.Timestamp date4;

	private java.util.Date date11;
	private java.sql.Date date21;
	private java.sql.Time date31;
	private java.sql.Timestamp date41;

	public void setDate1(Date date1) {
		this.date1 = date1;
		this.date11 = date1;
	}

	public void setDate2(java.sql.Date date2) {
		this.date2 = date2;
		this.date21 = date2;
	}

	public void setDate3(Time date3) {
		this.date3 = date3;
		this.date31 = date3;
	}

	public void setDate4(Timestamp date4) {
		this.date4 = date4;
		this.date41 = date4;
	}

	public Date getDate1() {
		return date1;
	}

	public java.sql.Date getDate2() {
		return date2;
	}

	public Time getDate3() {
		return date3;
	}

	public Timestamp getDate4() {
		return date4;
	}

	public Date getDate11() {
		return date11;
	}

	public java.sql.Date getDate21() {
		return date21;
	}

	public Time getDate31() {
		return date31;
	}

	public Timestamp getDate41() {
		return date41;
	}
}
