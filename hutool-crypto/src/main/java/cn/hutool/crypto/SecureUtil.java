package cn.hutool.crypto;

import java.io.File;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.DESede;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * 安全相关工具类<br>
 * 加密分为三种：<br>
 * 1、对称加密（symmetric），例如：AES、DES等<br>
 * 2、非对称加密（asymmetric），例如：RSA、DSA等<br>
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等<br>
 * 
 * @author xiaoleilu
 *
 */
public final class SecureUtil {
	
	/**
	 * 默认密钥字节数
	 * 
	 * <pre>
	 * RSA/DSA
	 * Default Keysize 1024
	 * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
	 * </pre>
	 */
	public static final int DEFAULT_KEY_SIZE = 1024;
	
	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 * 
	 * @param algorithm 算法，支持PBE算法
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm) {
		return generateKey(algorithm, -1);
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 * 
	 * @param algorithm 算法，支持PBE算法
	 * @param keySize 密钥长度
	 * @return {@link SecretKey}
	 * @since 3.1.2
	 */
	public static SecretKey generateKey(String algorithm, int keySize) {
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		
		if(keySize > 0) {
			keyGenerator.init(keySize);
		}
		return keyGenerator.generateKey();
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
	 * 
	 * @param algorithm 算法
	 * @param key 密钥，如果为{@code null} 自动生成随机密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm, byte[] key) {
		Assert.notBlank(algorithm, "Algorithm is blank!");
		SecretKey secretKey = null;
		if (algorithm.startsWith("PBE")) {
			// PBE密钥
			secretKey = generatePBEKey(algorithm, (null == key) ? null : StrUtil.str(key, CharsetUtil.CHARSET_UTF_8).toCharArray());
		} else if (algorithm.startsWith("DES")) {
			// DES密钥
			secretKey = generateDESKey(algorithm, key);
		} else {
			// 其它算法密钥
			secretKey = (null == key) ? generateKey(algorithm) : new SecretKeySpec(key, algorithm);
		}
		return secretKey;
	}

	/**
	 * 生成 {@link SecretKey}
	 * 
	 * @param algorithm DES算法，包括DES、DESede等
	 * @param key 密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateDESKey(String algorithm, byte[] key) {
		if (StrUtil.isBlank(algorithm) || false == algorithm.startsWith("DES")) {
			throw new CryptoException("Algorithm [{}] is not a DES algorithm!");
		}
		
		SecretKey secretKey = null;
		if (null == key) {
			secretKey = generateKey(algorithm);
		} else {
			KeySpec keySpec;
			try {
				if(algorithm.startsWith("DESede")) {
					//DESede兼容
					keySpec = new DESedeKeySpec(key);
				}else {
					keySpec = new DESKeySpec(key);
				}
			} catch (InvalidKeyException e) {
				throw new CryptoException(e);
			}
			secretKey = generateKey(algorithm, keySpec);
		}
		return secretKey;
	}

	/**
	 * 生成PBE {@link SecretKey}
	 * 
	 * @param algorithm PBE算法，包括：PBEWithMD5AndDES、PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40等
	 * @param key 密钥
	 * @return {@link SecretKey}
	 */
	public static SecretKey generatePBEKey(String algorithm, char[] key) {
		if (StrUtil.isBlank(algorithm) || false == algorithm.startsWith("PBE")) {
			throw new CryptoException("Algorithm [{}] is not a PBE algorithm!");
		}

		if (null == key) {
			key = RandomUtil.randomString(32).toCharArray();
		}
		PBEKeySpec keySpec = new PBEKeySpec(key);
		return generateKey(algorithm, keySpec);
	}

	/**
	 * 生成 {@link SecretKey}，仅用于对称加密和摘要算法
	 * 
	 * @param algorithm 算法
	 * @param keySpec {@link KeySpec}
	 * @return {@link SecretKey}
	 */
	public static SecretKey generateKey(String algorithm, KeySpec keySpec) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			return keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 生成私钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 * 
	 * @param algorithm 算法
	 * @param key 密钥
	 * @return 私钥 {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(String algorithm, byte[] key) {
		return generatePrivateKey(algorithm, new PKCS8EncodedKeySpec(key));
	}
	
	/**
	 * 生成私钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 * 
	 * @param algorithm 算法
	 * @param keySpec {@link KeySpec}
	 * @return 私钥 {@link PrivateKey}
	 * @since 3.1.1
	 */
	public static PrivateKey generatePrivateKey(String algorithm, KeySpec keySpec) {
		algorithm = getAlgorithmAfterWith(algorithm);
		try {
			return KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 生成私钥，仅用于非对称加密
	 * 
	 * @param keyStore {@link KeyStore}
	 * @param alias 别名
	 * @param password 密码
	 * @return 私钥 {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(KeyStore keyStore, String alias, char[] password) {
		try {
			return (PrivateKey) keyStore.getKey(alias, password);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 生成公钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 * 
	 * @param algorithm 算法
	 * @param key 密钥
	 * @return 公钥 {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(String algorithm, byte[] key) {
		return generatePublicKey(algorithm, new X509EncodedKeySpec(key));
	}
	
	/**
	 * 生成公钥，仅用于非对称加密<br>
	 * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
	 * 
	 * @param algorithm 算法
	 * @param keySpec {@link KeySpec}
	 * @return 公钥 {@link PublicKey}
	 * @since 3.1.1
	 */
	public static PublicKey generatePublicKey(String algorithm, KeySpec keySpec) {
		algorithm = getAlgorithmAfterWith(algorithm);
		try {
			return KeyFactory.getInstance(algorithm).generatePublic(keySpec);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 生成用于非对称加密的公钥和私钥，仅用于非对称加密<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 * 
	 * @param algorithm 非对称加密算法
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(String algorithm) {
		return generateKeyPair(algorithm, DEFAULT_KEY_SIZE, null);
	}
	
	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 * 
	 * @param algorithm 非对称加密算法
	 * @param keySize 密钥模（modulus ）长度
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize) {
		return generateKeyPair(algorithm, keySize, null);
	}

	/**
	 * 生成用于非对称加密的公钥和私钥<br>
	 * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
	 * 
	 * @param algorithm 非对称加密算法
	 * @param keySize 密钥模（modulus ）长度
	 * @param seed 种子
	 * @return {@link KeyPair}
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize, byte[] seed) {
		algorithm = getAlgorithmAfterWith(algorithm);
		if("EC".equalsIgnoreCase(algorithm) && (keySize <= 0 || keySize > 256)) {
			//对于EC算法，密钥长度有限制，在此使用默认256
			keySize = 256;
		}
		
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}

		if(keySize <= 0){
			keySize = DEFAULT_KEY_SIZE;
		}
		if (null != seed) {
			final SecureRandom random = new SecureRandom(seed);
			keyPairGen.initialize(keySize, random);
		} else {
			keyPairGen.initialize(keySize);
		}
		return keyPairGen.generateKeyPair();
	}
	
	/**
	 * 获取用于密钥生成的算法<br>
	 * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA，返回算法为EC
	 * 
	 * @param algorithm XXXwithXXX算法
	 * @return 算法
	 */
	public static String getAlgorithmAfterWith(String algorithm) {
		Assert.notNull(algorithm, "algorithm must be not null !");
		int indexOfWith = StrUtil.lastIndexOfIgnoreCase(algorithm, "with");
		if(indexOfWith > 0) {
			algorithm = StrUtil.subSuf(algorithm, indexOfWith + "with".length());
		}
		if("ECDSA".equalsIgnoreCase(algorithm)) {
			algorithm = "EC";
		}
		return algorithm;
	}

	/**
	 * 生成签名对象，仅用于非对称加密
	 * 
	 * @param asymmetricAlgorithm {@link AsymmetricAlgorithm} 非对称加密算法
	 * @param digestAlgorithm {@link DigestAlgorithm} 摘要算法
	 * @return {@link Signature}
	 */
	public static Signature generateSignature(AsymmetricAlgorithm asymmetricAlgorithm, DigestAlgorithm digestAlgorithm) {
		String digestPart = (null == digestAlgorithm) ? "NONE" : digestAlgorithm.name();
		String algorithm = StrUtil.format("{}with{}", digestPart, asymmetricAlgorithm.getValue());
		try {
			return Signature.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
	}
	
	/**
	 * 读取密钥库(Java Key Store，JKS) KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 * 
	 * @param in {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 */
	public static KeyStore readJKSKeyStore(InputStream in, char[] password){
		return readKeyStore("JKS", in, password);
	}
	
	/**
	 * 读取KeyStore文件<br>
	 * KeyStore文件用于数字证书的密钥对保存<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 * 
	 * @param type 类型
	 * @param in {@link InputStream} 如果想从文件读取.keystore文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 */
	public static KeyStore readKeyStore(String type, InputStream in, char[] password){
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance(type);
			keyStore.load(in, password);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
		return keyStore;
	}
	
	/**
	 * 读取X.509 Certification文件<br>
	 * Certification为证书文件<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 * 
	 * @param in {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 */
	public static Certificate readX509Certificate(InputStream in, char[] password){
		return readCertificate("X.509", in, password);
	}
	
	/**
	 * 读取Certification文件<br>
	 * Certification为证书文件<br>
	 * see: http://snowolf.iteye.com/blog/391931
	 * 
	 * @param type 类型
	 * @param in {@link InputStream} 如果想从文件读取.cer文件，使用 {@link FileUtil#getInputStream(java.io.File)} 读取
	 * @param password 密码
	 * @return {@link KeyStore}
	 */
	public static Certificate readCertificate(String type, InputStream in, char[] password){
		Certificate certificate;
		try {
			certificate = CertificateFactory.getInstance(type).generateCertificate(in);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
		return certificate;
	}
	
	/**
	 * 获得 Certification
	 * @param keyStore {@link KeyStore}
	 * @param alias 别名
	 * @return {@link Certificate}
	 */
	public static Certificate getCertificate(KeyStore keyStore, String alias){
		try {
			return keyStore.getCertificate(alias);
		} catch (Exception e) {
			throw new CryptoException(e);
		}
	}

	// ------------------------------------------------------------------- 对称加密算法

	/**
	 * AES加密，生成随机KEY。注意解密时必须使用相同 {@link AES}对象或者使用相同KEY<br>
	 * 例：
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
	 * <pre>
	 * MD5加密：md5().digest(data)
	 * MD5加密并转为16进制字符串：md5().digestHex(data)
	 * </pre>
	 * 
	 * @return {@link Digester}
	 */
	public static Digester md5() {
		return new Digester(DigestAlgorithm.MD5);
	}
	
	/**
	 * MD5加密，生成16进制MD5字符串<br>
	 * 
	 * @param data 数据
	 * @return MD5字符串
	 */
	public static String md5(String data) {
		return new Digester(DigestAlgorithm.MD5).digestHex(data);
	}
	
	/**
	 * MD5加密，生成16进制MD5字符串<br>
	 * 
	 * @param data 数据
	 * @return MD5字符串
	 */
	public static String md5(InputStream data) {
		return new Digester(DigestAlgorithm.MD5).digestHex(data);
	}
	
	/**
	 * MD5加密文件，生成16进制MD5字符串<br>
	 * 
	 * @param dataFile 被加密文件
	 * @return MD5字符串
	 */
	public static String md5(File dataFile) {
		return new Digester(DigestAlgorithm.MD5).digestHex(dataFile);
	}

	/**
	 * SHA1加密<br>
	 * 例：<br>
	 * 		SHA1加密：sha1().digest(data)<br>
	 * 		SHA1加密并转为16进制字符串：sha1().digestHex(data)<br>
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
	 * 创建HMac对象，调用digest方法可获得hmac值
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key 密钥，如果为<code>null</code>生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmac(HmacAlgorithm algorithm, String key){
		return new HMac(algorithm, StrUtil.utf8Bytes(key));
	}
	
	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key 密钥，如果为<code>null</code>生成随机密钥
	 * @return {@link HMac}
	 * @since 3.0.3
	 */
	public static HMac hmac(HmacAlgorithm algorithm, byte[] key){
		return new HMac(algorithm, key);
	}
	
	/**
	 * 创建HMac对象，调用digest方法可获得hmac值
	 * @param algorithm {@link HmacAlgorithm}
	 * @param key 密钥{@link SecretKey}，如果为<code>null</code>生成随机密钥
	 * @return {@link HMac}
	 * @since 3.0.3
	 */
	public static HMac hmac(HmacAlgorithm algorithm, SecretKey key){
		return new HMac(algorithm, key);
	}
	
	/**
	 * HmacMD5加密器<br>
	 * 例：<br>
	 * 		HmacMD5加密：hmacMd5(key).digest(data)<br>
	 * 		HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
	 * @param key 加密密钥，如果为<code>null</code>生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmacMd5(String key){
		return hmacMd5(StrUtil.utf8Bytes(key));
	}
	
	/**
	 * HmacMD5加密器<br>
	 * 例：<br>
	 * 		HmacMD5加密：hmacMd5(key).digest(data)<br>
	 * 		HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
	 * @param key 加密密钥，如果为<code>null</code>生成随机密钥
	 * @return {@link HMac}
	 */
	public static HMac hmacMd5(byte[] key){
		return new HMac(HmacAlgorithm.HmacMD5, key);
	}
	
	/**
	 * HmacMD5加密器，生成随机KEY<br>
	 * 例：<br>
	 * 		HmacMD5加密：hmacMd5().digest(data)<br>
	 * 		HmacMD5加密并转为16进制字符串：hmacMd5().digestHex(data)<br>
	 * @return {@link HMac}
	 */
	public static HMac hmacMd5(){
		return new HMac(HmacAlgorithm.HmacMD5);
	}
	
	/**
	 * HmacSHA1加密器<br>
	 * 例：<br>
	 * 		HmacSHA1加密：hmacSha1(key).digest(data)<br>
	 * 		HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
	 * @param key 加密密钥，如果为<code>null</code>生成随机密钥
	 * @return {@link HMac}
	 * @since 3.3.0
	 */
	public static HMac hmacSha1(String key){
		return hmacSha1(StrUtil.utf8Bytes(key));
	}
	
	/**
	 * HmacSHA1加密器<br>
	 * 例：<br>
	 * 		HmacSHA1加密：hmacSha1(key).digest(data)<br>
	 * 		HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
	 * @param key 加密密钥，如果为<code>null</code>生成随机密钥
	 * @return {@link HMac}
	 */
	public static HMac hmacSha1(byte[] key){
		return new HMac(HmacAlgorithm.HmacSHA1, key);
	}
	
	/**
	 * HmacSHA1加密器，生成随机KEY<br>
	 * 例：<br>
	 * 		HmacSHA1加密：hmacSha1().digest(data)<br>
	 * 		HmacSHA1加密并转为16进制字符串：hmacSha1().digestHex(data)<br>
	 * @return {@link HMac}
	 */
	public static HMac hmacSha1(){
		return new HMac(HmacAlgorithm.HmacSHA1);
	}
	
	// ------------------------------------------------------------------- 非称加密算法
	
	/**
	 * 创建RSA算法对象<br>
	 * 生成新的私钥公钥对
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa(){
		return new RSA();
	}
	
	/**
	 * 创建RSA算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa(String privateKeyBase64, String publicKeyBase64){
		return new RSA(privateKeyBase64, publicKeyBase64);
	}
	
	/**
	 * 创建RSA算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return {@link RSA}
	 * @since 3.0.5
	 */
	public static RSA rsa(byte[] privateKey, byte[] publicKey){
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
	public static Sign sign(SignAlgorithm algorithm){
		return new Sign(algorithm);
	}
	
	/**
	 * 创建签名算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param algorithm 签名算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(SignAlgorithm algorithm, String privateKeyBase64, String publicKeyBase64){
		return new Sign(algorithm, privateKeyBase64, publicKeyBase64);
	}
	
	/**
	 * 创建Sign算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 * @return {@link Sign}
	 * @since 3.3.0
	 */
	public static Sign sign(SignAlgorithm algorithm, byte[] privateKey, byte[] publicKey){
		return new Sign(algorithm, privateKey, publicKey);
	}
	
	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 * 
	 * @param crypto 对称加密算法
	 * @param params 参数
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(SymmetricCrypto crypto, Map<?, ?> params) {
		return signParams(crypto, params, StrUtil.EMPTY, StrUtil.EMPTY, true);
	}
	
	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 * 
	 * @param crypto 对称加密算法
	 * @param params 参数
	 * @param separator entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull 是否忽略null的键和值
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String separator, String keyValueSeparator, boolean isIgnoreNull) {
		if(MapUtil.isEmpty(params)) {
			return null;
		}
		String paramsStr = MapUtil.join(MapUtil.sort(params), separator, keyValueSeparator, isIgnoreNull);
		return crypto.encryptHex(paramsStr);
	}
	
	/**
	 * 对参数做md5签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 * 
	 * @param params 参数
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParamsMd5(Map<?, ?> params) {
		return signParams(DigestAlgorithm.MD5, params);
	}
	
	/**
	 * 对参数做Sha1签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 * 
	 * @param params 参数
	 * @return 签名
	 * @since 4.0.8
	 */
	public static String signParamsSha1(Map<?, ?> params) {
		return signParams(DigestAlgorithm.SHA1, params);
	}
	
	/**
	 * 对参数做Sha256签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 * 
	 * @param params 参数
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParamsSha256(Map<?, ?> params) {
		return signParams(DigestAlgorithm.SHA256, params);
	}
	
	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 * 
	 * @param digestAlgorithm 摘要算法
	 * @param params 参数
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params) {
		return signParams(digestAlgorithm, params, StrUtil.EMPTY, StrUtil.EMPTY, true);
	}
	
	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 * 
	 * @param digestAlgorithm 摘要算法
	 * @param params 参数
	 * @param separator entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull 是否忽略null的键和值
	 * @return 签名
	 * @since 4.0.1
	 */
	public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String separator, String keyValueSeparator, boolean isIgnoreNull) {
		if(MapUtil.isEmpty(params)) {
			return null;
		}
		final String paramsStr = MapUtil.join(MapUtil.sort(params), separator, keyValueSeparator, isIgnoreNull);
		return new Digester(digestAlgorithm).digestHex(paramsStr);
	}
	
	// ------------------------------------------------------------------- UUID
	/**
	 * 简化的UUID，去掉了横线
	 * 
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID() {
		return RandomUtil.simpleUUID();
	}
}
