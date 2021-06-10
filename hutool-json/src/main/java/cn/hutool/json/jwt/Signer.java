package cn.hutool.json.jwt;

public interface Signer {

	String sign(String header, String payload);
}
