package cn.hutool.json.test.bean;

public class TokenAuthWarp extends UUMap<TokenAuthResponse> {
	private static final long serialVersionUID = 1L;

	private String targetUrl;
	private String success;

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
}
