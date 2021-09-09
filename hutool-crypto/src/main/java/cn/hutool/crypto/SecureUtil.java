package cn.hutool.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.DESede;
import cn.hutool.crypto.symmetric.PBKDF2;
import cn.hutool.crypto.symmetric.RC4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.crypto.symmetric.ZUC;
import cn.hutool.crypto.symmetric.fpe.FPE;
import org.bouncycastle.crypto.AlphabetMapper;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Map;

/**
 * 安全相关工具类<br>
 * 加密分为三种：<br>
 * 1、对称加密（symmetric），例如：AES、DES等<br>
 * 2、非对称加密（asymmetric），例如：RSA、DSA等<br>
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等<br>
 *
 * @author Looly, Gsealy
 */
public class SecureUtil {

	/**
	 * 默认密钥字节数
	 *
	 * <pre>
	 * RSA/DSA
	 * Default Keysize 1024
	 * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
	 * </pre>
	 */
	public static final int DEFAULT_KEY_SIZE = KeyUtil.DEFAULT_KEY_SIZE;

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 *
	 * @param algorithm 算法，支持PBE算法
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm) {
		return KeyUtil.generateKey(algorithm);
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 *
	 * @param algorithm 算法，支持PBE算法
	 * @param keySize   密钥长度
	 * @return {@link SecretKey}
	 * @since 3.1.2
	 */
	public static SecretKey generateKey(String algorithm, int keySize) {
		return KeyUtil.generateKey(algorithm, keySize);
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 *
	 * @param algorithm 算法
	 * @param key       密钥，如果为{@code null} 自动生成随机密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm, byte[] key) {
		return KeyUtil.generateKey(algorithm, key);
	}

	/**
	 * 生成 {@link SecretKey}
	 *
	 * @param algorithm DES算法，包括DES、DESede等
	 * @param key       密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateDESKey(String algorithm, byte[] key) {
		return KeyUtil.generateDESKey(algorithm, key);
	}

	/**
	 * 生成PBE {@link SecretKey}
	 *
	 * @param algorithm PBE算法，包括：PBEWithMD5AndDES、PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40等
	 * @param key       密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generatePBEKey(String algorithm, char[] key) {
		return KeyUtil.generatePBEKey(algorithm, key);
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法
	 *
	 * @param algorithm 算法
	 * @param keySpec   {@link KeySpec}
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm, KeySpec keySpec) {
		return KeyUtil.generateKey(algorithm, keySpec);
	}

	/**
	 * 生成私钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @return 私钥 {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(String algorithm, byte[] key) {
		return KeyUtil.generatePrivateKey(algorithm, key);
	}

	/**
	 * 生成私钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 *
	 * @param algorithm 算法
	 * @param keySpec   {@link KeySpec}
	 * @return 私钥 {@link PrivateKey}
	 * @since 3.1.1
	 */
	public static PrivateKey generatePrivateKey(String algorithm, KeySpec keySpec) {
		return KeyUtil.generatePrivateKey(algorithm, keySpec);
	}

	/**
	 * 生成私钥，仅用于非对称加密
	 *
	 * @param keyStore {@link KeyStore}
	 * @param alias    别名
	 * @param password 密码
	 * @return 私钥 {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(KeyStore keyStore, String alias, char[] password) {
		return KeyUtil.generatePrivateKey(keyStore, alias, password);
	}

	/**
	 * 生成公钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @return 公钥 {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(String algorithm, byte[] key) {
		return KeyUtil.generatePublicKey(algorithm, key);
	}

	/**
	 * 生成公钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 *
	 * @param algorithm 算法
	 * @param keySpec   {@link KeySpec}
	 * @return 公钥 {@link PublicKey}
	 * @since 3.1.1
	 */
	public static PublicKey generatePublicKey(String algorithm, KeySpec keySpec) {
		return KeyUtil.generatePublicKey(algorithm, keySpec);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥，仅用于非对称加密<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 *
	 * @param algorithm 非对称加密算法
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(String algorithm) {
		return KeyUtil.generateKeyPair(algorithm);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 *
	 * @param algorithm 非对称加密算法
	 * @param keySize   密钥模（modulus ）长度
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize) {
		return KeyUtil.generateKeyPair(algorithm, keySize);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 *
	 * @param algorithm 非对称加密算法
	 * @param keySize   密钥模（modulus ）长度
	 * @param seed      种子
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize, byte[] seed) {
		return KeyUtil.generateKeyPair(algorithm, keySize, seed);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 *
	 * @param algorithm 非对称加密算法
	 * @param params    {@link AlgorithmParameterSpec}
	 * @return {@link KeyPair}
	 * @since 4.3.3
	 */
	public static KeyPair generateKeyPair(String algorithm, AlgorithmParameterSpec params) {
		return KeyUtil.generateKeyPair(algorithm, params);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 *
	 * @param algorithm 非对称加密算法
	 * @param seed      种子
	 * @param params    {@link AlgorithmParameterSpec}
	 * @return {@link KeyPair}
	 * @since 4.3.3
	 */
	public static KeyPair generateKeyPair(String algorithm, byte[] seed, AlgorithmParameterSpec params) {
		return KeyUtil.generateKeyPair(algorithm, seed, params);
	}

	/**
	 * 获取用于密钥生成的算法<br>
	 * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA或SM2，返回算法为EC
	 *
	 * @param algorithm XXXwithXXX算法
	 * @return 算法
	 */
	public static String getAlgorithmAfterWith(String algorithm) {
		return KeyUtil.getAlgorithmAfterWith(algorithm);
	}

	/**
	 * 生成算法，格式为XXXwithXXX
	 *
	 * @param asymmetricAlgorithm 非对称算法
	 * @param digestAlgorithm     摘要算法
	 * @return 算法
	 * @since 4.4.1
	 */
	public static String generateAlgorithm(AsymmetricAlgorithm asymmetricAlgorithm, DigestAlgorithm digestAlgorithm) {
		final String digestPart = (null == digestAlgorithm) ? "NONE" : digestAlgorithm.name();
		return StrUtil.format("{}with{}", digestPart, asymmetricAlgorithm.getValue());
	}

	/**
	 * 生成签名对象，仅用于非对称加密
	 *
	 * @param asymmetricAlgorithm {@link AsymmetricAlgorithm} 非对称加密算法
	 * @param digestAlgorithm     {@link DigestAlgorithm} 摘要算法
	 * @return {@link Signature}
	 */
	public static Signature generateSignature(AsymmetricAlgorithm asymmetricAlgorithm, DigestAlgorithm digestAlgorithm) {
		try {
			return Signature.getInstance(generateAlgorithm(asymmetricAlgorithm, digestAlgorithm));
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 读取密钥库(Java Key Store，JKS) KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 *
	 * @param in       {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 */
	public static KeyStore readJKSKeyStore(InputStream in, char[] password) {
		return KeyUtil.readJKSKeyStore(in, password);
	}

	/**
	 * 读取KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 *
	 * @param type     类型
	 * @param in       {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 */
	public static KeyStore readKeyStore(String type, InputStream in, char[] password) {
		return KeyUtil.readKeyStore(type, in, password);
	}

	/**
	 * 读取X.509 Certification文件<br>
	 * Certification为证书文件<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 *
	 * @param in       {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @param alias    别名
	 * @return {@link KeyStore}
	 * @since 4.4.1
	 */
	public static Certificate readX509Certificate(InputStream in, char[] password, String alias) {
		return KeyUtil.readX509Certificate(in, password, alias);
	}

	/**
	 * 读取X.509 Certification文件<br>
	 * Certification为证书文件<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 *
	 * @param in {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @return {@link KeyStore}
	 * @since 4.4.1
	 */
	public static Certificate readX509Certificate(InputStream in) {
		return KeyUtil.readX509Certificate(in);
	}

	/**
	 * 读取Certification文件<br>
	 * Certification为证书文件<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 *
	 * @param type     类型，例如X.509
	 * @param in       {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @param alias    别名
	 * @return {@link KeyStore}
	 * @since 4.4.1
	 */
	public static Certificate readCertificate(String type, InputStream in, char[] password, String alias) {
		return KeyUtil.readCertificate(type, in, password, alias);
	}

	/**
	 * 读取Certification文件<br>
	 * Certification为证书文件<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 *
	 * @param type 类型，例如X.509
	 * @param in   {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @return {@link Certificate}
	 */
	public static Certificate readCertificate(String type, InputStream in) {
		return KeyUtil.readCertificate(type, in);
	}

	/**
	 * 获得 Certification
	 *
	 * @param keyStore {@link KeyStore}
	 * @param alias    别名
	 * @return {@link Certificate}
	 */
	public static Certificate getCertificate(KeyStore keyStore, String alias) {
		return KeyUtil.getCertificate(keyStore, alias);
	}

	// ------------------------------------------------------------------- 对称加密算法

	/**
	 * AES加密，生成随机KEY。注意解密时必须使用相同 {@link AES}对象或者使用相同KEY<br>
	 * 例：
	 *
	 * <pre>
	 * AES加密：aes().encrypt(data)
	 * AES解密：aes().decrypt(data)
	 * </pre>
	 *
	 * @return {@link AES}
	 */
	public static AES aes() {
		return new AES();
	}

	/**
	 * AES加密<br>
	 * 例：
	 *
	 * <pre>
	 * AES加密：aes(key).encrypt(data)
	 * AES解密：aes(key).decrypt(data)
	 * </pre>
	 *
	 * @param key 密钥
	 * @return {@link SymmetricCrypto}
	 */
	public static AES aes(byte[] key) {
		return new AES(key);
	}

	/**
	 * DES加密，生成随机KEY。注意解密时必须使用相同 {@link DES}对象或者使用相同KEY<br>
	 * 例：
	 *
	 * <pre>
	 * DES加密：des().encrypt(data)
	 * DES解密：des().decrypt(data)
	 * </pre>
	 *
	 * @return {@link DES}
	 */
	public static DES des() {
		return new DES();
	}

	/**
	 * DES加密<br>
	 * 例：
	 *
	 * <pre>
	 * DES加密：des(key).encrypt(data)
	 * DES解密：des(key).decrypt(data)
	 * </pre>
	 *
	 * @param key 密钥
	 * @return {@link DES}
	 */
	public static DES des(byte[] key) {
		return new DES(key);
	}

	/**
	 * DESede加密（又名3DES、TripleDES），生成随机KEY。注意解密时必须使用相同 {@link DESede}对象或者使用相同KEY<br>
	 * Java中默认实现为：DESede/ECB/PKCS5Padding<br>
	 * 例：
	 *
	 * <pre>
	 * DESede加密：desede().encrypt(data)
	 * DESede解密：desede().decrypt(data)
	 * </pre>
	 *
	 * @return {@link DESede}
	 * @since 3.3.0
	 */
	public static DESede desede() {
		return new DESede();
	}

	/**
	 * DESede加密（又名3DES、TripleDES）<br>
	 * Java中默认实现为：DESede/ECB/PKCS5Padding<br>
	 * 例：
	 *
	 * <pre>
	 * DESede加密：desede(key).encrypt(data)
	 * DESede解密：desede(key).decrypt(data)
	 * </pre>
	 *
	 * @param key 密钥
	 * @return {@link DESede}
	 * @since 3.3.0
	 */
	public static DESede desede(byte[] key) {
		return new DESede(key);
	}

	// ------------------------------------------------------------------- 摘要算法

	/**
	 * MD5加密<br>
	 * 例：
	 *
	 * <pre>
	 * MD5加密：md5().digest(data)
	 * MD5加密并转为16进制字符串：md5().digestHex(data)
	 * </pre>
	 *
	 * @return {@link Digester}
	 */
	public static MD5 md5() {
		return new MD5();
	}

	/**
	 * MD5加密，生成16进制MD5字符串<br>
	 *
	 * @param data 数据
	 * @return MD5字符串
	 */
	public static String md5(String data) {
		return new MD5().digestHex(data);
	}

	/**
	 * MD5加密，生成16进制MD5字符串<br>
	 *
	 * @param data 数据
	 * @return MD5字符串
	 */
	public static String md5(InputStream data) {
		return new MD5().digestHex(data);
	}

	/**
	 * MD5加密文件，生成16进制MD5字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return MD5字符串
	 */
	public static String md5(File dataFile) {
		return new MD5().digestHex(dataFile);
	}

	/**
	 * SHA1加密<br>
	 * 例：<br>
	 * SHA1加密：sha1().digest(data)<br>
	 * SHA1加密并转为16进制字符串：sha1().digestHex(data)<br>
	 *
	 * @return {@link Digester}
	 */
	public static Digester sha1() {
		return new Digester(DigestAlgorithm.SHA1);
	}

	/**
	 * SHA1加密，生成16进制SHA1字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1字符串
	 */
	public static String sha1(String data) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * SHA1加密，生成16进制SHA1字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1字符串
	 */
	public static String sha1(InputStream data) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(data);
	}

	/**
	 * SHA1加密文件，生成16进制SHA1字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return SHA1字符串
	 */
	public static String sha1(File dataFile) {
		return new Digester(DigestAlgorithm.SHA1).digestHex(dataFile);
	}

	/**
	 * SHA256加密<br>
	 * 例：<br>
	 * SHA256加密：sha256().digest(data)<br>
	 * SHA256加密并转为16进制字符串：sha256().digestHex(data)<br>
	 *
	 * @return {@link Digester}
	 * @since 4.3.2
	 */
	public static Digester sha256() {
		return new Digester(DigestAlgorithm.SHA256);
	}

	/**
	 * SHA256加密，生成16进制SHA256字符串<br>
	 *
	 * @param data 数据
	 * @return SHA256字符串
	 * @since 4.3.2
	 */
	public static String sha256(String data) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * SHA256加密，生成16进制SHA256字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1字符串
	 * @since 4.3.2
	 */
	public static String sha256(InputStream data) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(data);
	}

	/**
	 * SHA256加密文件，生成16进制SHA256字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return SHA256字符串
	 * @since 4.3.2
	 */
	public static String sha256(File dataFile) {
		return new Digester(DigestAlgorithm.SHA256).digestHex(dataFile);
	}

	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 *
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key       密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmac(HmacAlgorithm algorithm, String key) {
		return new HMac(algorithm, StrUtil.utf8Bytes(key));
	}

	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 *
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key       密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.0.3
	 */
	public static HMac hmac(HmacAlgorithm algorithm, byte[] key) {
		return new HMac(algorithm, key);
	}

	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 *
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key       密钥{@link SecretKey}，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.0.3
	 */
	public static HMac hmac(HmacAlgorithm algorithm, SecretKey key) {
		return new HMac(algorithm, key);
	}

	/**
	 * HmacMD5加密器<br>
	 * 例：<br>
	 * HmacMD5加密：hmacMd5(key).digest(data)<br>
	 * HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmacMd5(String key) {
		return hmacMd5(StrUtil.utf8Bytes(key));
	}

	/**
	 * HmacMD5加密器<br>
	 * 例：<br>
	 * HmacMD5加密：hmacMd5(key).digest(data)<br>
	 * HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 */
	public static HMac hmacMd5(byte[] key) {
		return new HMac(HmacAlgorithm.HmacMD5, key);
	}

	/**
	 * HmacMD5加密器，生成随机KEY<br>
	 * 例：<br>
	 * HmacMD5加密：hmacMd5().digest(data)<br>
	 * HmacMD5加密并转为16进制字符串：hmacMd5().digestHex(data)<br>
	 *
	 * @return {@link HMac}
	 */
	public static HMac hmacMd5() {
		return new HMac(HmacAlgorithm.HmacMD5);
	}

	/**
	 * HmacSHA1加密器<br>
	 * 例：<br>
	 * HmacSHA1加密：hmacSha1(key).digest(data)<br>
	 * HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmacSha1(String key) {
		return hmacSha1(StrUtil.utf8Bytes(key));
	}

	/**
	 * HmacSHA1加密器<br>
	 * 例：<br>
	 * HmacSHA1加密：hmacSha1(key).digest(data)<br>
	 * HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 */
	public static HMac hmacSha1(byte[] key) {
		return new HMac(HmacAlgorithm.HmacSHA1, key);
	}

	/**
	 * HmacSHA1加密器，生成随机KEY<br>
	 * 例：<br>
	 * HmacSHA1加密：hmacSha1().digest(data)<br>
	 * HmacSHA1加密并转为16进制字符串：hmacSha1().digestHex(data)<br>
	 *
	 * @return {@link HMac}
	 */
	public static HMac hmacSha1() {
		return new HMac(HmacAlgorithm.HmacSHA1);
	}

	/**
	 * HmacSHA256加密器<br>
	 * 例：<br>
	 * HmacSHA256加密：hmacSha256(key).digest(data)<br>
	 * HmacSHA256加密并转为16进制字符串：hmacSha256(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 5.6.0
	 */
	public static HMac hmacSha256(String key) {
		return hmacSha256(StrUtil.utf8Bytes(key));
	}

	/**
	 * HmacSHA256加密器<br>
	 * 例：<br>
	 * HmacSHA256加密：hmacSha256(key).digest(data)<br>
	 * HmacSHA256加密并转为16进制字符串：hmacSha256(key).digestHex(data)<br>
	 *
	 * @param key 加密密钥，如果为{@code null}生成随机密钥
	 * @return {@link HMac}
	 * @since 5.6.0
	 */
	public static HMac hmacSha256(byte[] key) {
		return new HMac(HmacAlgorithm.HmacSHA256, key);
	}

	/**
	 * HmacSHA256加密器，生成随机KEY<br>
	 * 例：<br>
	 * HmacSHA256加密：hmacSha256().digest(data)<br>
	 * HmacSHA256加密并转为16进制字符串：hmacSha256().digestHex(data)<br>
	 *
	 * @return {@link HMac}
	 * @since 5.6.0
	 */
	public static HMac hmacSha256() {
		return new HMac(HmacAlgorithm.HmacSHA256);
	}

	// ------------------------------------------------------------------- 非称加密算法

	/**
	 * 创建RSA算法对象<br>
	 * 生成新的私钥公钥对
	 *
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa() {
		return new RSA();
	}

	/**
	 * 创建RSA算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64  公钥Base64
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa(String privateKeyBase64, String publicKeyBase64) {
		return new RSA(privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 创建RSA算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa(byte[] privateKey, byte[] publicKey) {
		return new RSA(privateKey, publicKey);
	}

	/**
	 * 创建签名算法对象<br>
	 * 生成新的私钥公钥对
	 *
	 * @param algorithm 签名算法
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(SignAlgorithm algorithm) {
		return new Sign(algorithm);
	}

	/**
	 * 创建签名算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm        签名算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64  公钥Base64
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(SignAlgorithm algorithm, String privateKeyBase64, String publicKeyBase64) {
		return new Sign(algorithm, privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 创建Sign算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm  算法枚举
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(SignAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
		return new Sign(algorithm, privateKey, publicKey);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param crypto      对称加密算法
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String... otherParams) {
		return signParams(crypto, params, StrUtil.EMPTY, StrUtil.EMPTY, true, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 *
	 * @param crypto            对称加密算法
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String separator,
									String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
		return crypto.encryptHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
	}

	/**
	 * 对参数做md5签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParamsMd5(Map<?, ?> params, String... otherParams) {
		return signParams(DigestAlgorithm.MD5, params, otherParams);
	}

	/**
	 * 对参数做Sha1签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.8
	 */
	public static String signParamsSha1(Map<?, ?> params, String... otherParams) {
		return signParams(DigestAlgorithm.SHA1, params, otherParams);
	}

	/**
	 * 对参数做Sha256签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParamsSha256(Map<?, ?> params, String... otherParams) {
		return signParams(DigestAlgorithm.SHA256, params, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param digestAlgorithm 摘要算法
	 * @param params          参数
	 * @param otherParams     其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String... otherParams) {
		return signParams(digestAlgorithm, params, StrUtil.EMPTY, StrUtil.EMPTY, true, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 *
	 * @param digestAlgorithm   摘要算法
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String separator,
									String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
		return new Digester(digestAlgorithm).digestHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
	}

	/**
	 * 增加加密解密的算法提供者，默认优先使用，例如：
	 *
	 * <pre>
	 * addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	 * </pre>
	 *
	 * @param provider 算法提供者
	 * @since 4.1.22
	 */
	public static void addProvider(Provider provider) {
		Security.insertProviderAt(provider, 0);
	}

	/**
	 * 解码字符串密钥，可支持的编码如下：
	 *
	 * <pre>
	 * 1. Hex（16进制）编码
	 * 1. Base64编码
	 * </pre>
	 *
	 * @param key 被解码的密钥字符串
	 * @return 密钥
	 * @since 4.3.3
	 */
	public static byte[] decode(String key) {
		return Validator.isHex(key) ? HexUtil.decodeHex(key) : Base64.decode(key);
	}

	/**
	 * 创建{@link Cipher}
	 *
	 * @param algorithm 算法
	 * @return {@link Cipher}
	 * @since 4.5.2
	 */
	public static Cipher createCipher(String algorithm) {
		final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

		Cipher cipher;
		try {
			cipher = (null == provider) ? Cipher.getInstance(algorithm) : Cipher.getInstance(algorithm, provider);
		} catch (Exception e) {
			throw new CryptoException(e);
		}

		return cipher;
	}

	/**
	 * 创建{@link MessageDigest}
	 *
	 * @param algorithm 算法
	 * @return {@link MessageDigest}
	 * @since 4.5.2
	 */
	public static MessageDigest createMessageDigest(String algorithm) {
		final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

		MessageDigest messageDigest;
		try {
			messageDigest = (null == provider) ? MessageDigest.getInstance(algorithm) : MessageDigest.getInstance(algorithm, provider);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}

		return messageDigest;
	}

	/**
	 * 创建{@link Mac}
	 *
	 * @param algorithm 算法
	 * @return {@link Mac}
	 * @since 4.5.13
	 */
	public static Mac createMac(String algorithm) {
		final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

		Mac mac;
		try {
			mac = (null == provider) ? Mac.getInstance(algorithm) : Mac.getInstance(algorithm, provider);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}

		return mac;
	}

	/**
	 * 创建{@link Signature}
	 *
	 * @param algorithm 算法
	 * @return {@link Signature}
	 * @since 5.7.0
	 */
	public static Signature createSignature(String algorithm) {
		final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

		Signature signature;
		try {
			signature = (null == provider) ? Signature.getInstance(algorithm) : Signature.getInstance(algorithm, provider);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}

		return signature;
	}

	/**
	 * RC4算法
	 *
	 * @param key 密钥
	 * @return {@link RC4}
	 */
	public static RC4 rc4(String key) {
		return new RC4(key);
	}

	/**
	 * 强制关闭Bouncy Castle库的使用，全局有效
	 *
	 * @since 4.5.2
	 */
	public static void disableBouncyCastle() {
		GlobalBouncyCastleProvider.setUseBouncyCastle(false);
	}

	/**
	 * PBKDF2加密密码
	 *
	 * @param password 密码
	 * @param salt     盐
	 * @return 盐，一般为16位
	 * @since 5.6.0
	 */
	public static String pbkdf2(char[] password, byte[] salt) {
		return new PBKDF2().encryptHex(password, salt);
	}

	/**
	 * FPE(Format Preserving Encryption)实现，支持FF1和FF3-1模式。
	 *
	 * @param mode   FPE模式枚举，可选FF1或FF3-1
	 * @param key    密钥，{@code null}表示随机密钥，长度必须是16bit、24bit或32bit
	 * @param mapper Alphabet字典映射，被加密的字符范围和这个映射必须一致，例如手机号、银行卡号等字段可以采用数字字母字典表
	 * @param tweak  Tweak是为了解决因局部加密而导致结果冲突问题，通常情况下将数据的不可变部分作为Tweak
	 * @return {@link FPE}
	 * @since 5.7.12
	 */
	public static FPE fpe(FPE.FPEMode mode, byte[] key, AlphabetMapper mapper, byte[] tweak) {
		return new FPE(mode, key, mapper, tweak);
	}

	/**
	 * 祖冲之算法集（ZUC-128算法）实现，基于BouncyCastle实现。
	 *
	 * @param key 密钥
	 * @param iv  加盐，长度16bytes，{@code null}是随机加盐
	 * @return {@link ZUC}
	 * @since 5.7.12
	 */
	public static ZUC zuc128(byte[] key, byte[] iv) {
		return new ZUC(ZUC.ZUCAlgorithm.ZUC_128, key, iv);
	}

	/**
	 * 祖冲之算法集（ZUC-256算法）实现，基于BouncyCastle实现。
	 *
	 * @param key 密钥
	 * @param iv  加盐，长度25bytes，{@code null}是随机加盐
	 * @return {@link ZUC}
	 * @since 5.7.12
	 */
	public static ZUC zuc256(byte[] key, byte[] iv) {
		return new ZUC(ZUC.ZUCAlgorithm.ZUC_256, key, iv);
	}
}
