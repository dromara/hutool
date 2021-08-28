package cn.hutool.http.useragent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 系统对象
 *
 * @author looly
 * @since 4.2.1
 */
public class OS extends UserAgentInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * 未知
	 */
	public static final OS Unknown = new OS(NameUnknown, null);

	/**
	 * 支持的引擎类型
	 */
	public static final List<OS> oses = CollUtil.newArrayList(//
			new OS("Windows 10 or Windows Server 2016", "windows nt 10\\.0", "windows nt (10\\.0)"),//
			new OS("Windows 8.1 or Winsows Server 2012R2", "windows nt 6\\.3", "windows nt (6\\.3)"),//
			new OS("Windows 8 or Winsows Server 2012", "windows nt 6\\.2", "windows nt (6\\.2)"),//
			new OS("Windows Vista", "windows nt 6\\.0", "windows nt (6\\.0)"), //
			new OS("Windows 7 or Windows Server 2008R2", "windows nt 6\\.1", "windows nt (6\\.1)"), //
			new OS("Windows 2003", "windows nt 5\\.2", "windows nt (5\\.2)"), //
			new OS("Windows XP", "windows nt 5\\.1", "windows nt (5\\.1)"), //
			new OS("Windows 2000", "windows nt 5\\.0", "windows nt (5\\.0)"), //
			new OS("Windows Phone", "windows (ce|phone|mobile)( os)?", "windows (?:ce|phone|mobile) (\\d+([._]\\d+)*)"), //
			new OS("Windows", "windows"), //
			new OS("OSX", "os x (\\d+)[._](\\d+)", "os x (\\d+([._]\\d+)*)"), //
			new OS("Android", "Android", "Android (\\d+([._]\\d+)*)"),//
			new OS("Linux", "linux"), //
			new OS("Wii", "wii", "wii libnup/(\\d+([._]\\d+)*)"), //
			new OS("PS3", "playstation 3", "playstation 3; (\\d+([._]\\d+)*)"), //
			new OS("PSP", "playstation portable", "Portable\\); (\\d+([._]\\d+)*)"), //
			new OS("iPad", "\\(iPad.*os (\\d+)[._](\\d+)", "\\(iPad.*os (\\d+([._]\\d+)*)"), //
			new OS("iPhone", "\\(iPhone.*os (\\d+)[._](\\d+)", "\\(iPhone.*os (\\d+([._]\\d+)*)"), //
			new OS("YPod", "iPod touch[\\s\\;]+iPhone.*os (\\d+)[._](\\d+)", "iPod touch[\\s\\;]+iPhone.*os (\\d+([._]\\d+)*)"), //
			new OS("YPad", "iPad[\\s\\;]+iPhone.*os (\\d+)[._](\\d+)", "iPad[\\s\\;]+iPhone.*os (\\d+([._]\\d+)*)"), //
			new OS("YPhone", "iPhone[\\s\\;]+iPhone.*os (\\d+)[._](\\d+)", "iPhone[\\s\\;]+iPhone.*os (\\d+([._]\\d+)*)"), //
			new OS("Symbian", "symbian(os)?"), //
			new OS("Darwin", "Darwin\\/([\\d\\w\\.\\-]+)", "Darwin\\/([\\d\\w\\.\\-]+)"), //
			new OS("Adobe Air", "AdobeAir\\/([\\d\\w\\.\\-]+)", "AdobeAir\\/([\\d\\w\\.\\-]+)"), //
			new OS("Java", "Java[\\s]+([\\d\\w\\.\\-]+)", "Java[\\s]+([\\d\\w\\.\\-]+)")//
	);

	/**
	 * 添加自定义的系统类型
	 *
	 * @param name         浏览器名称
	 * @param regex        关键字或表达式
	 * @param versionRegex 匹配版本的正则
	 * @since 5.7.4
	 */
	synchronized public static void addCustomOs(String name, String regex, String versionRegex) {
		oses.add(new OS(name, regex, versionRegex));
	}

	private Pattern versionPattern;

	/**
	 * 构造
	 *
	 * @param name  系统名称
	 * @param regex 关键字或表达式
	 */
	public OS(String name, String regex) {
		this(name, regex, null);
	}

	/**
	 * 构造
	 *
	 * @param name         系统名称
	 * @param regex        关键字或表达式
	 * @param versionRegex 版本正则表达式
	 * @since 5.7.4
	 */
	public OS(String name, String regex, String versionRegex) {
		super(name, regex);
		if (null != versionRegex) {
			this.versionPattern = Pattern.compile(versionRegex, Pattern.CASE_INSENSITIVE);
		}
	}

	/**
	 * 获取浏览器版本
	 *
	 * @param userAgentString User-Agent字符串
	 * @return 版本
	 */
	public String getVersion(String userAgentString) {
		if(isUnknown() || null == this.versionPattern){
			// 无版本信息
			return null;
		}
		return ReUtil.getGroup1(this.versionPattern, userAgentString);
	}
}
