package cn.hutool.http.useragent;

/**
 * User-Agent信息对象
 * 
 * @author looly
 * @since 4.2.1
 */
public class UserAgent {

	/** 是否为移动平台 */
	private boolean mobile;
	/** 浏览器类型 */
	private Browser browser;
	/** 平台类型 */
	private Platform platform;
	/** 系统类型 */
	private OS os;
	/** 引擎类型 */
	private Engine engine;
	/** 浏览器版本 */
	private String version;
	/** 引擎版本 */
	private String engineVersion;

	/**
	 * 是否为移动平台
	 * 
	 * @return 是否为移动平台
	 */
	public boolean isMobile() {
		return mobile;
	}

	/**
	 * 设置是否为移动平台
	 * 
	 * @param mobile 是否为移动平台
	 */
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取浏览器类型
	 * 
	 * @return 浏览器类型
	 */
	public Browser getBrowser() {
		return browser;
	}

	/**
	 * 设置浏览器类型
	 * 
	 * @param browser 浏览器类型
	 */
	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	/**
	 * 获取平台类型
	 * 
	 * @return 平台类型
	 */
	public Platform getPlatform() {
		return platform;
	}

	/**
	 * 设置平台类型
	 * 
	 * @param platform 平台类型
	 */
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	/**
	 * 获取系统类型
	 * 
	 * @return 系统类型
	 */
	public OS getOs() {
		return os;
	}

	/**
	 * 设置系统类型
	 * 
	 * @param os 系统类型
	 */
	public void setOs(OS os) {
		this.os = os;
	}

	/**
	 * 获取引擎类型
	 * 
	 * @return 引擎类型
	 */
	public Engine getEngine() {
		return engine;
	}

	/**
	 * 设置引擎类型
	 * 
	 * @param engine 引擎类型
	 */
	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	/**
	 * 获取浏览器版本
	 * 
	 * @return 浏览器版本
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置浏览器版本
	 * 
	 * @param version 浏览器版本
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取引擎版本
	 * 
	 * @return 引擎版本
	 */
	public String getEngineVersion() {
		return engineVersion;
	}

	/**
	 * 设置引擎版本
	 * 
	 * @param engineVersion 引擎版本
	 */
	public void setEngineVersion(String engineVersion) {
		this.engineVersion = engineVersion;
	}

}
