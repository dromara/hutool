package cn.hutool.http.useragent;

import java.util.regex.Pattern;

/**
 * User-agent信息
 * 
 * @author looly
 * @since 4.2.1
 */
public class UserAgentInfo {
	private String name;
	private Pattern pattern;

	public UserAgentInfo(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
