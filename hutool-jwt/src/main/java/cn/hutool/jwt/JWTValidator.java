package cn.hutool.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.NoneJWTSigner;

import java.util.Date;

/**
 * JWT数据校验器，用于校验包括：
 * <ul>
 *     <li>算法是否一致</li>
 *     <li>算法签名是否正确</li>
 *     <li>字段值是否有效（例如时间未过期等）</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.2
 */
public class JWTValidator {

	private final JWT jwt;

	/**
	 * 创建JWT验证器
	 *
	 * @param token JWT Token
	 * @return {@link JWTValidator}
	 */
	public static JWTValidator of(String token) {
		return new JWTValidator(JWT.of(token));
	}

	/**
	 * 创建JWT验证器
	 *
	 * @param jwt JWT对象
	 * @return {@link JWTValidator}
	 */
	public static JWTValidator of(JWT jwt) {
		return new JWTValidator(jwt);
	}

	/**
	 * 构造
	 *
	 * @param jwt JWT对象
	 */
	public JWTValidator(JWT jwt) {
		this.jwt = jwt;
	}

	/**
	 * 验证算法，使用JWT对象自带的{@link JWTSigner}
	 *
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateAlgorithm() throws ValidateException {
		return validateAlgorithm(null);
	}

	/**
	 * 验证算法，使用自定义的{@link JWTSigner}
	 *
	 * @param signer 用于验证算法的签名器
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateAlgorithm(JWTSigner signer) throws ValidateException {
		validateAlgorithm(this.jwt, signer);
		return this;
	}

	/**
	 * 检查JWT的以下三两个时间：
	 *
	 * <ul>
	 *     <li>{@link JWTPayload#NOT_BEFORE}：被检查时间必须晚于生效时间</li>
	 *     <li>{@link JWTPayload#EXPIRES_AT}：被检查时间必须早于失效时间</li>
	 *     <li>{@link JWTPayload#ISSUED_AT}：签发时间必须早于失效时间</li>
	 * </ul>
	 * <p>
	 * 如果某个时间没有设置，则不检查（表示无限制）
	 *
	 * @param dateToCheck 被检查的时间，一般为当前时间
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateDate(Date dateToCheck) throws ValidateException {
		validateDate(this.jwt.getPayload(), dateToCheck);
		return this;
	}

	/**
	 * 验证算法
	 *
	 * @param jwt    {@link JWT}对象
	 * @param signer 用于验证的签名器
	 * @throws ValidateException 验证异常
	 */
	private static void validateAlgorithm(JWT jwt, JWTSigner signer) throws ValidateException {
		final String algorithmId = jwt.getAlgorithm();
		if (null == signer) {
			signer = jwt.getSigner();
		}

		if (StrUtil.isEmpty(algorithmId)) {
			// 可能无签名
			if (null == signer || signer instanceof NoneJWTSigner) {
				return;
			}
			throw new ValidateException("No algorithm defined in header!");
		}

		if (null == signer) {
			throw new IllegalArgumentException("No Signer for validate algorithm!");
		}

		final String algorithmIdInSigner = signer.getAlgorithmId();
		if (false == StrUtil.equals(algorithmId, algorithmIdInSigner)) {
			throw new ValidateException("Algorithm [{}] defined in header doesn't match to [{}]!"
					, algorithmId, algorithmIdInSigner);
		}

		// 通过算法验证签名是否正确
		if (false == jwt.verify(signer)) {
			throw new ValidateException("Signature verification failed!");
		}
	}

	/**
	 * 检查JWT的以下三两个时间：
	 *
	 * <ul>
	 *     <li>{@link JWTPayload#NOT_BEFORE}：被检查时间必须晚于生效时间</li>
	 *     <li>{@link JWTPayload#EXPIRES_AT}：被检查时间必须早于失效时间</li>
	 *     <li>{@link JWTPayload#ISSUED_AT}：签发时间必须早于失效时间</li>
	 * </ul>
	 * <p>
	 * 如果某个时间没有设置，则不检查（表示无限制）
	 *
	 * @param payload     {@link JWTPayload}
	 * @param dateToCheck 被检查的时间，一般为当前时间
	 * @throws ValidateException 验证异常
	 */
	private static void validateDate(JWTPayload payload, Date dateToCheck) throws ValidateException {
		if (null == dateToCheck) {
			// 默认当前时间
			dateToCheck = DateUtil.date();
		}

		// 检查生效时间（被检查时间必须晚于生效时间）
		final Date notBefore = payload.getClaimsJson().getDate(JWTPayload.NOT_BEFORE);
		if (null != notBefore && dateToCheck.before(notBefore)) {
			throw new ValidateException("Current date [{}] is before 'nbf' [{}]",
					dateToCheck, DateUtil.date(notBefore));
		}

		// 检查失效时间（被检查时间必须早于失效时间）
		final Date expiresAt = payload.getClaimsJson().getDate(JWTPayload.EXPIRES_AT);
		if (null != expiresAt && dateToCheck.after(expiresAt)) {
			throw new ValidateException("Current date [{}] is after 'exp' [{}]",
					dateToCheck, DateUtil.date(expiresAt));
		}

		// 检查签发时间（被检查时间必须晚于签发时间）
		final Date issueAt = payload.getClaimsJson().getDate(JWTPayload.ISSUED_AT);
		if (null != issueAt && dateToCheck.before(issueAt)) {
			throw new ValidateException("Current date [{}] is before 'iat' [{}]",
					dateToCheck, DateUtil.date(issueAt));
		}
	}
}
