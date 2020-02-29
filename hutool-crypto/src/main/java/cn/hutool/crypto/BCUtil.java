package cn.hutool.crypto;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;

/**
 * Bouncy Castle相关工具类封装
 *
 * @author looly
 * @since 4.5.0
 */
public class BCUtil {
	/**
	 * 编码压缩EC公钥（基于BouncyCastle）<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 *
	 * @param publicKey {@link PublicKey}，必须为org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
	 * @return 压缩得到的X
	 * @since 4.4.4
	 */
	public static byte[] encodeECPublicKey(PublicKey publicKey) {
		return ((BCECPublicKey) publicKey).getQ().getEncoded(true);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 *
	 * @param encode    压缩公钥
	 * @param curveName EC曲线名
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(String encode, String curveName) {
		return decodeECPoint(SecureUtil.decode(encode), curveName);
	}

	/**
	 * 解码恢复EC压缩公钥,支持Base64和Hex编码,（基于BouncyCastle）<br>
	 * 见：https://www.cnblogs.com/xinzhao/p/8963724.html
	 *
	 * @param encodeByte 压缩公钥
	 * @param curveName  EC曲线名，例如{@link KeyUtil#SM2_DEFAULT_CURVE}
	 * @return 公钥
	 * @since 4.4.4
	 */
	public static PublicKey decodeECPoint(byte[] encodeByte, String curveName) {
		final ECNamedCurveParameterSpec namedSpec = ECNamedCurveTable.getParameterSpec(curveName);
		final ECCurve curve = namedSpec.getCurve();
		final EllipticCurve ecCurve = new EllipticCurve(//
				new ECFieldFp(curve.getField().getCharacteristic()), //
				curve.getA().toBigInteger(), //
				curve.getB().toBigInteger());
		// 根据X恢复点Y
		final ECPoint point = ECPointUtil.decodePoint(ecCurve, encodeByte);

		// 根据曲线恢复公钥格式
		ECParameterSpec ecSpec = new ECNamedCurveSpec(curveName, curve, namedSpec.getG(), namedSpec.getN());

		final KeyFactory PubKeyGen = KeyUtil.getKeyFactory("EC");
		try {
			return PubKeyGen.generatePublic(new ECPublicKeySpec(point, ecSpec));
		} catch (GeneralSecurityException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 读取PEM格式的私钥
	 *
	 * @param pemStream pem流
	 * @return {@link PrivateKey}
	 * @since 4.5.2
	 */
	public static PrivateKey readPrivateKey(InputStream pemStream) {
		return (PrivateKey) readPemKey(pemStream);
	}

	/**
	 * 读取PEM格式的公钥
	 *
	 * @param pemStream pem流
	 * @return {@link PublicKey}
	 * @since 4.5.2
	 */
	public static PublicKey readPublicKey(InputStream pemStream) {
		return (PublicKey) readPemKey(pemStream);
	}

	/**
	 * 从pem文件中读取公钥或私钥<br>
	 * 根据类型返回{@link PublicKey} 或者 {@link PrivateKey}
	 *
	 * @param pemKeyStream pem流
	 * @return {@link Key}
	 * @since 4.5.2
	 * @deprecated 请使用{@link #readPemKey(InputStream)}
	 */
	@Deprecated
	public static Key readKey(InputStream pemKeyStream) {
		return readPemKey(pemKeyStream);
	}

	/**
	 * 从pem文件中读取公钥或私钥<br>
	 * 根据类型返回{@link PublicKey} 或者 {@link PrivateKey}
	 *
	 * @param keyStream pem流
	 * @return {@link Key}，null表示无法识别的密钥类型
	 * @since 5.1.6
	 */
	public static Key readPemKey(InputStream keyStream) {
		final PemObject object = readPemObject(keyStream);
		final String type = object.getType();
		if (StrUtil.isNotBlank(type)) {
			if (type.endsWith("PRIVATE KEY")) {
				return KeyUtil.generateRSAPrivateKey(object.getContent());
			} else if (type.endsWith("PUBLIC KEY")) {
				return KeyUtil.generateRSAPublicKey(object.getContent());
			} else if (type.endsWith("CERTIFICATE")) {
				return KeyUtil.readPublicKeyFromCert(IoUtil.toStream(object.getContent()));
			}
		}

		//表示无法识别的密钥类型
		return null;
	}

	/**
	 * 从pem文件中读取公钥或私钥
	 *
	 * @param keyStream pem流
	 * @return 密钥bytes
	 * @since 4.5.2
	 * @deprecated 使用{@link #readPem(InputStream)}
	 */
	@Deprecated
	public static byte[] readKeyBytes(InputStream keyStream) {
		return readPem(keyStream);
	}

	/**
	 * 从pem流中读取公钥或私钥
	 *
	 * @param keyStream pem流
	 * @return 密钥bytes
	 * @since 5.1.6
	 */
	public static byte[] readPem(InputStream keyStream) {
		PemObject pemObject = readPemObject(keyStream);
		if (null != pemObject) {
			return pemObject.getContent();
		}
		return null;
	}

	/**
	 * 读取pem文件中的信息，包括类型、头信息和密钥内容
	 *
	 * @param keyStream pem流
	 * @return {@link PemObject}
	 * @since 4.5.2
	 */
	public static PemObject readPemObject(InputStream keyStream) {
		return readPemObject(IoUtil.getUtf8Reader(keyStream));
	}

	/**
	 * 读取pem文件中的信息，包括类型、头信息和密钥内容
	 *
	 * @param reader pem Reader
	 * @return {@link PemObject}
	 * @since 5.1.6
	 */
	public static PemObject readPemObject(Reader reader) {
		PemReader pemReader = null;
		try {
			pemReader = new PemReader(reader);
			return pemReader.readPemObject();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(pemReader);
		}
	}

	/**
	 * 写出pem密钥（私钥、公钥、证书）
	 *
	 * @param type      密钥类型（私钥、公钥、证书）
	 * @param content   密钥内容
	 * @param keyStream pem流
	 * @since 5.1.6
	 */
	public static void writePemObject(String type, byte[] content, OutputStream keyStream) {
		writePemObject(new PemObject(type, content), keyStream);
	}

	/**
	 * 写出pem密钥（私钥、公钥、证书）
	 *
	 * @param pemObject pem对象，包括密钥和密钥类型等信息
	 * @param keyStream pem流
	 * @since 5.1.6
	 */
	public static void writePemObject(PemObjectGenerator pemObject, OutputStream keyStream) {
		PemWriter writer = null;
		try {
			writer = new PemWriter(IoUtil.getUtf8Writer(keyStream));
			writer.writeObject(pemObject);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(writer);
		}
	}
}
