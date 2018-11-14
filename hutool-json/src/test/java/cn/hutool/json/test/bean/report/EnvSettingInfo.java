package cn.hutool.json.test.bean.report;

import java.util.Collection;

/**
 * 测试环境信息
 * @author xuwangcheng
 * @version 20181012
 *
 */
public class EnvSettingInfo {
	
	public static boolean DEV_MODE = true;
	
	private boolean remoteMode;
	
	private String hubRemoteUrl;
	
	private String reportFolder = "/report";
	private String screenshotFolder = "/screenshot";;
	
	private String elementFolder = "/config/element/";
	private String suiteFolder = "/config/suite/";
	
	private String chromeDriverPath = "/src/main/resources/chromedriver.exe";
	private String ieDriverPath = "/src/main/resources/IEDriverServer.exe";
	private String operaDriverPath = "/src/main/resources/operadriver.exe";
	private String firefoxDriverPath = "/src/main/resources/geckodriver.exe";
	
	private Double defaultSleepSeconds;
	
	private Integer elementLocationRetryCount;
	private Double elementLocationTimeouts;
	
	/**
	 * 收件人列表
	 */
	private Collection<String> tos;
	/**
	 * 抄送人列表
	 */
	private Collection<String> ccs;
	/**
	 * 密送人列表
	 */
	private Collection<String> bccs;
	
	/**
	 * 是否可以开启定时任务
	 */
	private boolean cronEnabled = false;
	
	/**
	 * 定时执行：suite文件
	 */
	private String cronSuite;
	
	/**
	 * 定时执行：cron表达式，支持linux crontab格式(5位)和Quartz的cron格式(6位)
	 */
	private String cronExpression;
	
	/**
	 * 存储测试报告数据的轻量级数据库，路径
	 */
	private String sqlitePath;
	
	public EnvSettingInfo() {
		super();
	}

	public void setSqlitePath(String sqlitePath) {
		this.sqlitePath = sqlitePath;
	}
	
	public String getSqlitePath() {
		return sqlitePath;
	}
	
	public void setCronEnabled(boolean cronEnabled) {
		this.cronEnabled = cronEnabled;
	}
	
	public boolean isCronEnabled() {
		return cronEnabled;
	}
	
	public String getCronSuite() {
		return cronSuite;
	}

	public void setCronSuite(String cronSuite) {
		this.cronSuite = cronSuite;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getElementLocationRetryCount() {
		return elementLocationRetryCount;
	}

	public void setElementLocationRetryCount(Integer elementLocationRetryCount) {
		this.elementLocationRetryCount = elementLocationRetryCount;
	}

	public Double getElementLocationTimeouts() {
		return elementLocationTimeouts;
	}

	public void setElementLocationTimeouts(Double elementLocationTimeouts) {
		this.elementLocationTimeouts = elementLocationTimeouts;
	}

	public String getElementFolder() {
		return elementFolder;
	}

	public void setElementFolder(String elementFolder) {
		this.elementFolder = elementFolder;
	}

	public String getSuiteFolder() {
		return suiteFolder;
	}

	public void setSuiteFolder(String suiteFolder) {
		this.suiteFolder = suiteFolder;
	}

	public boolean isRemoteMode() {
		return remoteMode;
	}

	public void setRemoteMode(boolean remoteMode) {
		this.remoteMode = remoteMode;
	}

	public String getHubRemoteUrl() {
		return hubRemoteUrl;
	}

	public void setHubRemoteUrl(String hubRemoteUrl) {
		this.hubRemoteUrl = hubRemoteUrl;
	}

	public String getReportFolder() {
		return reportFolder;
	}

	public void setReportFolder(String reportFolder) {
		this.reportFolder = reportFolder;
	}

	public String getScreenshotFolder() {
		return screenshotFolder;
	}

	public void setScreenshotFolder(String screenshotFolder) {
		this.screenshotFolder = screenshotFolder;
	}

	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

	public String getIeDriverPath() {
		return ieDriverPath;
	}

	public void setIeDriverPath(String ieDriverPath) {
		this.ieDriverPath = ieDriverPath;
	}

	public String getOperaDriverPath() {
		return operaDriverPath;
	}

	public void setOperaDriverPath(String operaDriverPath) {
		this.operaDriverPath = operaDriverPath;
	}

	public String getFirefoxDriverPath() {
		return firefoxDriverPath;
	}

	public void setFirefoxDriverPath(String firefoxDriverPath) {
		this.firefoxDriverPath = firefoxDriverPath;
	}

	public Double getDefaultSleepSeconds() {
		return defaultSleepSeconds;
	}

	public void setDefaultSleepSeconds(Double defaultSleepSeconds) {
		this.defaultSleepSeconds = defaultSleepSeconds;
	}

	public Collection<String> getTos() {
		return tos;
	}

	public void setTos(Collection<String> tos) {
		this.tos = tos;
	}

	public Collection<String> getCcs() {
		return ccs;
	}

	public void setCcs(Collection<String> ccs) {
		this.ccs = ccs;
	}

	public Collection<String> getBccs() {
		return bccs;
	}

	public void setBccs(Collection<String> bccs) {
		this.bccs = bccs;
	}

	@Override
	public String toString() {
		return "EnvSettingInfo [remoteMode=" + remoteMode + ", hubRemoteUrl=" + hubRemoteUrl + ", reportFolder="
				+ reportFolder + ", screenshotFolder=" + screenshotFolder + ", elementFolder=" + elementFolder
				+ ", suiteFolder=" + suiteFolder + ", chromeDriverPath=" + chromeDriverPath + ", ieDriverPath="
				+ ieDriverPath + ", operaDriverPath=" + operaDriverPath + ", firefoxDriverPath=" + firefoxDriverPath
				+ ", defaultSleepSeconds=" + defaultSleepSeconds + ", elementLocationRetryCount="
				+ elementLocationRetryCount + ", elementLocationTimeouts=" + elementLocationTimeouts + ", mailAccount="
				+ 1 + ", tos=" + tos + ", ccs=" + ccs + ", bccs=" + bccs + ", cronEnabled=" + cronEnabled
				+ ", cronSuite=" + cronSuite + ", cronExpression=" + cronExpression + "]";
	}
}
