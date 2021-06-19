package cn.hutool.jwt;

import java.util.Map;

/**
 * JWT载荷信息<br>
 * 载荷就是存放有效信息的地方。这个名字像是特指飞机上承载的货品，这些有效信息包含三个部分:
 *
 * <ul>
 *     <li>标准中注册的声明</li>
 *     <li>公共的声明</li>
 *     <li>私有的声明</li>
 * </ul>
 * <p>
 * 详细介绍见：https://www.jianshu.com/p/576dbf44b2ae
 *
 * @author looly
 * @since 5.7.0
 */
public class JWTPayload extends Claims implements RegisteredPayload<JWTPayload>{
	private static final long serialVersionUID = 1L;

	/**
	 * 增加自定义JWT认证载荷信息
	 *
	 * @param payloadClaims 载荷信息
	 * @return this
	 */
	public JWTPayload addPayloads(Map<String, ?> payloadClaims) {
		putAll(payloadClaims);
		return this;
	}

	@Override
	public JWTPayload setPayload(String name, Object value) {
		setClaim(name, value);
		return this;
	}
}
