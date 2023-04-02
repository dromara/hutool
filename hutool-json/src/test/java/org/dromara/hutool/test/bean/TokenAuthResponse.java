package org.dromara.hutool.test.bean;

import lombok.Data;

@Data
public class TokenAuthResponse {
	private String token;
	private String userId;
}
