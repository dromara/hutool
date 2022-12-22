package cn.hutool.crypto;

import cn.hutool.core.io.IORuntimeException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.X509TrustedCertificateBlock;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 基于bcpkix封装的Openssl相关工具，包括密钥转换、Pem密钥文件读取等<br>
 * 注意此工具需要引入org.bouncycastle:bcpkix-jdk15to18
 *
 * @author changhr2013, looly
 * @since 5.8.5
 */
public class OpensslKeyUtil {

	private static final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter().setProvider(GlobalBouncyCastleProvider.INSTANCE.getProvider());

	/**
	 * 转换{@link PrivateKeyInfo}为{@link PrivateKey}
	 *
	 * @param privateKeyInfo {@link PrivateKeyInfo}
	 * @return {@link PrivateKey}
	 * @throws CryptoException {@link PEMException}包装
	 */
	public static PrivateKey getPrivateKey(final PrivateKeyInfo privateKeyInfo) throws CryptoException {
		try {
			return pemKeyConverter.getPrivateKey(privateKeyInfo);
		} catch (final PEMException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 转换{@link SubjectPublicKeyInfo}为{@link PublicKey}
	 *
	 * @param publicKeyInfo {@link SubjectPublicKeyInfo}
	 * @return {@link PublicKey}
	 * @throws CryptoException {@link PEMException}包装
	 */
	public static PublicKey getPublicKey(final SubjectPublicKeyInfo publicKeyInfo) throws CryptoException {
		try {
			return pemKeyConverter.getPublicKey(publicKeyInfo);
		} catch (final PEMException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 转换{@link PEMKeyPair}为{@link KeyPair}
	 *
	 * @param keyPair {@link PEMKeyPair}
	 * @return {@link KeyPair}
	 * @throws CryptoException {@link PEMException}包装
	 */
	public static KeyPair getKeyPair(final PEMKeyPair keyPair) throws CryptoException {
		try {
			return pemKeyConverter.getKeyPair(keyPair);
		} catch (final PEMException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 从pem文件中读取公钥或私钥<br>
	 * 根据类型返回 {@link PublicKey} 或者 {@link PrivateKey}
	 *
	 * @param keyStream pem 流
	 * @param password  私钥密码
	 * @return {@link Key}，null 表示无法识别的密钥类型
	 * @since 5.8.5
	 */
	public static Key readPemKey(final InputStream keyStream, final char[] password) {
		try (final PEMParser pemParser = new PEMParser(new InputStreamReader(keyStream))) {
			return readPemKeyFromKeyObject(pemParser.readObject(), password);
		} catch (final IOException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 解密{@link PKCS8EncryptedPrivateKeyInfo}为{@link PrivateKeyInfo}
	 *
	 * @param pkcs8Info {@link PKCS8EncryptedPrivateKeyInfo}
	 * @param password  密码
	 * @return {@link PrivateKeyInfo}
	 * @throws CryptoException OperatorCreationException和PKCSException包装
	 */
	public static PrivateKeyInfo decrypt(final PKCS8EncryptedPrivateKeyInfo pkcs8Info, final char[] password) throws CryptoException {
		final InputDecryptorProvider decryptProvider;
		try {
			decryptProvider = new JceOpenSSLPKCS8DecryptorProviderBuilder().setProvider(GlobalBouncyCastleProvider.INSTANCE.getProvider()).build(password);
			return pkcs8Info.decryptPrivateKeyInfo(decryptProvider);
		} catch (final OperatorCreationException | PKCSException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 解密{@link PEMEncryptedKeyPair}为{@link PEMKeyPair}
	 *
	 * @param pemEncryptedKeyPair {@link PKCS8EncryptedPrivateKeyInfo}
	 * @param password            密码
	 * @return {@link PEMKeyPair}
	 * @throws IORuntimeException IOException包装
	 */
	public static PEMKeyPair decrypt(final PEMEncryptedKeyPair pemEncryptedKeyPair, final char[] password) throws IORuntimeException {
		final PEMDecryptorProvider decryptProvider;
		try {
			decryptProvider = new JcePEMDecryptorProviderBuilder().setProvider(GlobalBouncyCastleProvider.INSTANCE.getProvider()).build(password);
			return pemEncryptedKeyPair.decryptKeyPair(decryptProvider);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 读取Pem文件中的密钥，密钥支持包括：<br>
	 * <ul>
	 *     <li>{@link PrivateKeyInfo}</li>
	 *     <li>{@link PEMKeyPair}，默认读取私钥</li>
	 *     <li>{@link PKCS8EncryptedPrivateKeyInfo}</li>
	 *     <li>{@link PEMEncryptedKeyPair}，默认读取私钥</li>
	 *     <li>{@link X509CertificateHolder}</li>
	 *     <li>{@link X509TrustedCertificateBlock}</li>
	 *     <li>{@link PKCS10CertificationRequest}</li>
	 * </ul>
	 *
	 * @param keyObject 密钥内容对象
	 * @param password  密码（部分加密的pem使用）
	 * @return {@link Key}
	 * @throws CryptoException 读取异常或不支持的类型
	 */
	private static Key readPemKeyFromKeyObject(final Object keyObject, final char[] password) throws CryptoException {
		if (keyObject instanceof PrivateKeyInfo) {
			// PrivateKeyInfo
			return getPrivateKey((PrivateKeyInfo) keyObject);
		} else if (keyObject instanceof PEMKeyPair) {
			// PemKeyPair
			return getKeyPair((PEMKeyPair) keyObject).getPrivate();
		} else if (keyObject instanceof PKCS8EncryptedPrivateKeyInfo) {
			// Encrypted PrivateKeyInfo
			return getPrivateKey(decrypt((PKCS8EncryptedPrivateKeyInfo) keyObject, password));
		} else if (keyObject instanceof PEMEncryptedKeyPair) {
			// Encrypted PemKeyPair
			return getPrivateKey(decrypt((PEMEncryptedKeyPair) keyObject, password).getPrivateKeyInfo());
		} else if (keyObject instanceof SubjectPublicKeyInfo) {
			// SubjectPublicKeyInfo
			return getPublicKey((SubjectPublicKeyInfo) keyObject);
		} else if (keyObject instanceof X509CertificateHolder) {
			// X509 Certificate
			return getPublicKey(((X509CertificateHolder) keyObject).getSubjectPublicKeyInfo());
		} else if (keyObject instanceof X509TrustedCertificateBlock) {
			// X509 Trusted Certificate
			return getPublicKey(((X509TrustedCertificateBlock) keyObject).getCertificateHolder().getSubjectPublicKeyInfo());
		} else if (keyObject instanceof PKCS10CertificationRequest) {
			// PKCS#10 CSR
			return getPublicKey(((PKCS10CertificationRequest) keyObject).getSubjectPublicKeyInfo());
		} else {
			// 表示无法识别的密钥类型
			throw new CryptoException("Unsupported key object type: {}", keyObject.getClass());
		}
	}
}
