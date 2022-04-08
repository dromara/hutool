package cn.hutool.json.test.bean;

import lombok.Data;

@Data
public class TokenAuthResponse {
	private String token;
	private String userId;
}
