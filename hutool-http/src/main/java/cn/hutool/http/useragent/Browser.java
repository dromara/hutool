package cn.hutool.http.useragent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 浏览器对象
 * 
 * @author looly
 * @since 4.2.1
 */
public class Browser extends UserAgentInfo {

	/** 未知 */
	public static final Browser Unknown = new Browser(NameUnknown, null, null);
	/** 其它版本 */
	public static final String Other_Version = "[\\/ ]([\\d\\w\\.\\-]+)";

	/**
	 * 支持的浏览器类型
	 */
	public static final List<Browser> browers = CollUtil.newArrayList(//
			new Browser("MSEdge", "Edge|Edg", "(?:edge|Edg)\\/([\\d\\w\\.\\-]+)"), //
			new Browser("Chrome", "chrome", "chrome\\/([\\d\\w\\.\\-]+)"), //
			new Browser("Firefox", "firefox", Other_Version), //
			new Browser("IEMobile", "iemobile", Other_Version), //
			new Browser("Safari", "safari", "version\\/([\\d\\w\\.\\-]+)"), //
			new Browser("Opera", "opera", Other_Version), //
			new Browser("Konqueror", "konqueror", Other_Version), //
			new Browser("PS3", "playstation 3", "([\\d\\w\\.\\-]+)\\)\\s*$"), //
			new Browser("PSP", "playstation portable", "([\\d\\w\\.\\-]+)\\)?\\s*$"), //
			new Browser("Lotus", "lotus.notes", "Lotus-Notes\\/([\\w.]+)"), //
			new Browser("Thunderbird", "thunderbird", Other_Version), //
			new Browser("Netscape", "netscape", Other_Version), //
			new Browser("Seamonkey", "seamonkey", Other_Version), //
			new Browser("Outlook", "microsoft.outlook", Other_Version), //
			new Browser("Evolution", "evolution", Other_Version), //
			new Browser("MSIE", "msie", "msie ([\\d\\w\\.\\-]+)"), //
			new Browser("MSIE11", "rv:11", "rv:([\\d\\w\\.\\-]+)"), //
			new Browser("Gabble", "Gabble", "Gabble\\/([\\d\\w\\.\\-]+)"), //
			new Browser("Yammer Desktop", "AdobeAir", "([\\d\\w\\.\\-]+)\\/Yammer"), //
			new Browser("Yammer Mobile", "Yammer[\\s]+([\\d\\w\\.\\-]+)", "Yammer[\\s]+([\\d\\w\\.\\-]+)"), //
			new Browser("Apache HTTP Client", "Apache\\\\-HttpClient", "Apache\\-HttpClient\\/([\\d\\w\\.\\-]+)"), //
			new Browser("BlackBerry", "BlackBerry", "BlackBerry[\\d]+\\/([\\d\\w\\.\\-]+)")//
	);

	private Pattern versionPattern;


	/**
	 * 构造
	 * 
	 * @param name 浏览器名称
	 * @param regex 关键字或表达式
	 * @param versionRegex 匹配版本的正则
	 */
	public Browser(String name, String regex, String versionRegex) {
		super(name, regex);
		if (Other_Version.equals(versionRegex)) {
			versionRegex = name + versionRegex;
		}
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
		return ReUtil.getGroup1(this.versionPattern, userAgentString);
	}

	/**
	 * 是否移动浏览器
	 * 
	 * @return 是否移动浏览器
	 */
	public boolean isMobile() {
		return "PSP".equals(this.getName());
	}
}
