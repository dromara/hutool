package cn.hutool.crypto;

import java.io.File;
import java.io.InputStream;

import org.bouncycastle.crypto.params.ECDomainParameters;

import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * SM国密算法工具类<br>
 * 此工具类依赖org.bouncycastle:bcpkix-jdk15on
 * 
 * @author looly
 * @since 4.3.2
 */
public class SmUtil {

	private static String SM3 = "SM3";
	private static String SM4 = "SM4";

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
	 * @param publicKeyStr 公钥Hex或Base64表示
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
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return {@link SM2}
	 */
	public static SM2 sm2(byte[] privateKey, byte[] publicKey) {
		return new SM2(privateKey, publicKey);
	}

	/**
	 * SM3加密<br>
	 * 例：<br>
	 * SM3加密：sm3().digest(data)<br>
	 * SM3加密并转为16进制字符串：sm3().digestHex(data)<br>
	 * 
	 * @return {@link Digester}
	 */
	public static Digester sm3() {
		return new Digester(SM3);
	}

	/**
	 * SM3加密，生成16进制SM3字符串<br>
	 * 
	 * @param data 数据
	 * @return SM3字符串
	 */
	public static String sm3(String data) {
		return new Digester(SM3).digestHex(data);
	}

	/**
	 * SM3加密，生成16进制SM3字符串<br>
	 * 
	 * @param data 数据
	 * @return SM3字符串
	 */
	public static String sm3(InputStream data) {
		return new Digester(SM3).digestHex(data);
	}

	/**
	 * SM3加密文件，生成16进制SM3字符串<br>
	 * 
	 * @param dataFile 被加密文件
	 * @return SM3字符串
	 */
	public static String sm3(File dataFile) {
		return new Digester(SM3).digestHex(dataFile);
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
	public static SymmetricCrypto sm4() {
		return new SymmetricCrypto(SM4);
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
	 * @return {@link SymmetricCrypto}
	 */
	public static SymmetricCrypto sm4(byte[] key) {
		return new SymmetricCrypto(SM4, key);
	}

	/**
	 * bc加解密使用旧标c1||c2||c3，此方法在加密后调用，将结果转化为c1||c3||c2
	 * 
	 * @param c1c2c3 加密后的bytes，顺序为C1C2C3
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
	 * @param 加密后的bytes，顺序为C1C3C2
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
}
