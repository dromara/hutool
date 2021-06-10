package cn.hutool.jwt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.signers.JWTSigner;

import java.nio.charset.Charset;

/**
 * JSON Web Token (JWT)，基于JSON的开放标准（(RFC 7519)用于在网络应用环境间传递声明。<br>
 * <p>
 * 结构：xxxxx.yyyyy.zzzzz
 * <ul>
 *     <li>header：主要声明了JWT的签名算法</li>
 *     <li>payload：主要承载了各种声明并传递明文数据</li>
 *     <li>signture：拥有该部分的JWT被称为JWS，也就是签了名的JWS</li>
 * </ul>
 *
 * <p>
 * 详细介绍见；https://www.jianshu.com/p/576dbf44b2ae
 * </p>
 *
 * @author looly
 */
public class JWT {

	private final JWTHeader header;
	private final JWTPayload payload;

	private Charset charset;
	private JWTSigner signer;

	/**
	 * 构造
	 */
	public JWT() {
		this.header = new JWTHeader();
		this.payload = new JWTPayload();
		this.charset = CharsetUtil.CHARSET_UTF_8;
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return this
	 */
	public JWT setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 设置签名算法
	 *
	 * @param signer 签名算法
	 * @return this
	 */
	public JWT setSigner(JWTSigner signer) {
		this.signer = signer;
		return this;
	}

	/**
	 * 签名生成JWT字符串
	 *
	 * @return JWT字符串
	 */
	public String sign() {
		final String headerBase64 = Base64.encodeUrlSafe(this.header.getClaimsJson(), charset);
		final String payloadBase64 = Base64.encodeUrlSafe(this.payload.getClaimsJson(), charset);
		final String sign = signer.sign(headerBase64, payloadBase64);

		return StrUtil.format("{}.{}.{}", headerBase64, payloadBase64, sign);
	}
}
