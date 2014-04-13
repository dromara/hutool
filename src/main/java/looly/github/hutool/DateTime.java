package looly.github.hutool;

import java.util.Date;

/**
 * 封装java.util.Date
 * @author xiaoleilu
 *
 */
public class DateTime extends Date{
	private static final long serialVersionUID = -5395712593979185936L;
	
	/**
	 * 转换JDK date为 DateTime
	 * @param date JDK Date
	 * @return DateTime
	 */
	public static DateTime parse(Date date) {
		return new DateTime(date);
	}
	
	public DateTime() {
		super();
	}
	
	public DateTime(Date date) {
		this(date.getTime());
	}
	
	public DateTime(long timeMillis) {
		super(timeMillis);
	}
	
	@Override
	public String toString() {
		return DateUtil.formatDateTime(this);
	}
}
