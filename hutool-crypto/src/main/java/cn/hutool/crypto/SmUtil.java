package cn.hutool.crypto;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.digest.mac.BCHMacEngine;
import cn.hutool.crypto.digest.mac.MacEngine;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * SM国密算法工具类<br>
 * 此工具类依赖org.bouncycastle:bcprov-jdk15to18
 *
 * <p>封装包括：</p>
 * <ul>
 *     <li>SM2 椭圆曲线非对称加密和签名</li>
 *     <li>SM3 杂凑算法</li>
 *     <li>SM4 对称加密</li>
 * </ul>
 *
 * @author looly
 * @since 4.3.2
 */
public class SmUtil {

	private final static int RS_LEN = 32;
	/**
	 * SM2默认曲线
	 */
	public static final String SM2_CURVE_NAME = "sm2p256v1";
	/**
	 * SM2推荐曲线参数（来自https://github.com/ZZMarquis/gmhelper）
	 */
	public static final ECDomainParameters SM2_DOMAIN_PARAMS = BCUtil.toDomainParams(GMNamedCurves.getByName(SM2_CURVE_NAME));
	/**
	 * SM2国密算法公钥参数的Oid标识
 	 */
	public static final ASN1ObjectIdentifier ID_SM2_PUBLIC_KEY_PARAM = new ASN1ObjectIdentifier("1.2.156.10197.1.301");

	/**
	 * 创建SM2算法对象<br>
	 * 生成新的私钥公钥对
	 *
	 * @return {@link SM2}
	 */
	public static SM2 sm2() {
		return new SM2();
	}

	/**
	 * 创建SM2算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKeyStr 私钥Hex或Base64表示
	 * @param publicKeyStr  公钥Hex或Base64表示
	 * @return {@link SM2}
	 */
	public static SM2 sm2(String privateKeyStr, String publicKeyStr) {
		return new SM2(privateKeyStr, publicKeyStr);
	}

	/**
	 * 创建SM2算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥，必须使用PKCS#8规范
	 * @param publicKey  公钥，必须使用X509规范
	 * @return {@link SM2}
	 */
	public static SM2 sm2(byte[] privateKey, byte[] publicKey) {
		return new SM2(privateKey, publicKey);
	}

	/**
	 * 创建SM2算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return {@link SM2}
	 * @since 5.5.9
	 */
	public static SM2 sm2(PrivateKey privateKey, PublicKey publicKey) {
		return new SM2(privateKey, publicKey);
	}

	/**
	 * 创建SM2算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKeyParams 私钥参数
	 * @param publicKeyParams  公钥参数
	 * @return {@link SM2}
	 * @since 5.5.9
	 */
	public static SM2 sm2(ECPrivateKeyParameters privateKeyParams, ECPublicKeyParameters publicKeyParams) {
		return new SM2(privateKeyParams, publicKeyParams);
	}

	/**
	 * SM3加密<br>
	 * 例：<br>
	 * SM3加密：sm3().digest(data)<br>
	 * SM3加密并转为16进制字符串：sm3().digestHex(data)<br>
	 *
	 * @return {@link SM3}
	 */
	public static SM3 sm3() {
		return new SM3();
	}

	/**
	 * SM3加密，生成16进制SM3字符串<br>
	 *
	 * @param data 数据
	 * @return SM3字符串
	 */
	public static String sm3(String data) {
		return sm3().digestHex(data);
	}

	/**
	 * SM3加密，生成16进制SM3字符串<br>
	 *
	 * @param data 数据
	 * @return SM3字符串
	 */
	public static String sm3(InputStream data) {
		return sm3().digestHex(data);
	}

	/**
	 * SM3加密文件，生成16进制SM3字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return SM3字符串
	 */
	public static String sm3(File dataFile) {
		return sm3().digestHex(dataFile);
	}

	/**
	 * SM4加密，生成随机KEY。注意解密时必须使用相同 {@link SymmetricCrypto}对象或者使用相同KEY<br>
	 * 例：
	 *
	 * <pre>
	 * SM4加密：sm4().encrypt(data)
	 * SM4解密：sm4().decrypt(data)
	 * </pre>
	 *
	 * @return {@link SymmetricCrypto}
	 */
	public static SM4 sm4() {
		return new SM4();
	}

	/**
	 * SM4加密<br>
	 * 例：
	 *
	 * <pre>
	 * SM4加密：sm4(key).encrypt(data)
	 * SM4解密：sm4(key).decrypt(data)
	 * </pre>
	 *
	 * @param key 密钥
	 * @return {@link SM4}
	 */
	public static SM4 sm4(byte[] key) {
		return new SM4(key);
	}

	/**
	 * bc加解密使用旧标c1||c2||c3，此方法在加密后调用，将结果转化为c1||c3||c2
	 *
	 * @param c1c2c3             加密后的bytes，顺序为C1C2C3
	 * @param ecDomainParameters {@link ECDomainParameters}
	 * @return 加密后的bytes，顺序为C1C3C2
	 */
	public static byte[] changeC1C2C3ToC1C3C2(byte[] c1c2c3, ECDomainParameters ecDomainParameters) {
		// sm2p256v1的这个固定65。可看GMNamedCurves、ECCurve代码。
		final int c1Len = (ecDomainParameters.getCurve().getFieldSize() + 7) / 8 * 2 + 1;
		final int c3Len = 32; // new SM3Digest().getDigestSize();
		byte[] result = new byte[c1c2c3.length];
		System.arraycopy(c1c2c3, 0, result, 0, c1Len); // c1
		System.arraycopy(c1c2c3, c1c2c3.length - c3Len, result, c1Len, c3Len); // c3
		System.arraycopy(c1c2c3, c1Len, result, c1Len + c3Len, c1c2c3.length - c1Len - c3Len); // c2
		return result;
	}

	/**
	 * bc加解密使用旧标c1||c3||c2，此方法在解密前调用，将密文转化为c1||c2||c3再去解密
	 *
	 * @param c1c3c2             加密后的bytes，顺序为C1C3C2
	 * @param ecDomainParameters {@link ECDomainParameters}
	 * @return c1c2c3 加密后的bytes，顺序为C1C2C3
	 */
	public static byte[] changeC1C3C2ToC1C2C3(byte[] c1c3c2, ECDomainParameters ecDomainParameters) {
		// sm2p256v1的这个固定65。可看GMNamedCurves、ECCurve代码。
		final int c1Len = (ecDomainParameters.getCurve().getFieldSize() + 7) / 8 * 2 + 1;
		final int c3Len = 32; // new SM3Digest().getDigestSize();
		byte[] result = new byte[c1c3c2.length];
		System.arraycopy(c1c3c2, 0, result, 0, c1Len); // c1: 0->65
		System.arraycopy(c1c3c2, c1Len + c3Len, result, c1Len, c1c3c2.length - c1Len - c3Len); // c2
		System.arraycopy(c1c3c2, c1Len, result, c1c3c2.length - c3Len, c3Len); // c3
		return result;
	}

	/**
	 * BC的SM3withSM2签名得到的结果的rs是asn1格式的，这个方法转化成直接拼接r||s
	 *
	 * @param rsDer rs in asn1 format
	 * @return sign result in plain byte array
	 * @since 4.5.0
	 */
	public static byte[] rsAsn1ToPlain(byte[] rsDer) {
		final BigInteger[] decode;
		try {
			decode = StandardDSAEncoding.INSTANCE.decode(SM2_DOMAIN_PARAMS.getN(), rsDer);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		final byte[] r = bigIntToFixedLengthBytes(decode[0]);
		final byte[] s = bigIntToFixedLengthBytes(decode[1]);

		return ArrayUtil.addAll(r, s);
	}

	/**
	 * BC的SM3withSM2验签需要的rs是asn1格式的，这个方法将直接拼接r||s的字节数组转化成asn1格式
	 *
	 * @param sign in plain byte array
	 * @return rs result in asn1 format
	 * @since 4.5.0
	 */
	public static byte[] rsPlainToAsn1(byte[] sign) {
		if (sign.length != RS_LEN * 2) {
			throw new CryptoException("err rs. ");
		}
		BigInteger r = new BigInteger(1, Arrays.copyOfRange(sign, 0, RS_LEN));
		BigInteger s = new BigInteger(1, Arrays.copyOfRange(sign, RS_LEN, RS_LEN * 2));
		try {
			return StandardDSAEncoding.INSTANCE.encode(SM2_DOMAIN_PARAMS.getN(), r, s);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建HmacSM3算法的{@link MacEngine}
	 *
	 * @param key 密钥
	 * @return {@link MacEngine}
	 * @since 4.5.13
	 */
	public static MacEngine createHmacSm3Engine(byte[] key) {
		return new BCHMacEngine(new SM3Digest(), key);
	}

	/**
	 * HmacSM3算法实现
	 *
	 * @param key 密钥
	 * @return {@link HMac} 对象，调用digestXXX即可
	 * @since 4.5.13
	 */
	public static HMac hmacSm3(byte[] key) {
		return new HMac(HmacAlgorithm.HmacSM3, key);
	}

	// -------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * BigInteger转固定长度bytes
	 *
	 * @param rOrS {@link BigInteger}
	 * @return 固定长度bytes
	 * @since 4.5.0
	 */
	private static byte[] bigIntToFixedLengthBytes(BigInteger rOrS) {
		// for sm2p256v1, n is 00fffffffeffffffffffffffffffffffff7203df6b21c6052b53bbf40939d54123,
		// r and s are the result of mod n, so they should be less than n and have length<=32
		byte[] rs = rOrS.toByteArray();
		if (rs.length == RS_LEN) {
			return rs;
		} else if (rs.length == RS_LEN + 1 && rs[0] == 0) {
			return Arrays.copyOfRange(rs, 1, RS_LEN + 1);
		} else if (rs.length < RS_LEN) {
			byte[] result = new byte[RS_LEN];
			Arrays.fill(result, (byte) 0);
			System.arraycopy(rs, 0, result, RS_LEN - rs.length, rs.length);
			return result;
		} else {
			throw new CryptoException("Error rs: {}", Hex.toHexString(rs));
		}
	}
	// -------------------------------------------------------------------------------------------------------- Private method end
}
