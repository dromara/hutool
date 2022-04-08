package cn.hutool.json.test.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAuthWarp extends UUMap<TokenAuthResponse> {
	private static final long serialVersionUID = 1L;

	private String targetUrl;
	private String success;
}
