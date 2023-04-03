/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * PEM(Privacy Enhanced Mail)格式相关工具类。（基于Bouncy Castle）
 *
 * <p>
 * PEM一般为文本格式，以 -----BEGIN... 开头，以 -----END... 结尾，中间的内容是 BASE64 编码。
 * <p>
 * 这种格式可以保存证书和私钥，有时我们也把PEM格式的私钥的后缀改为 .key 以区别证书与私钥。
 *
 * @author looly
 * @since 5.1.6
 */
public class PemUtil {

	/**
	 * 读取PEM格式的私钥
	 *
	 * @param pemStream pem流
	 * @return {@link PrivateKey}
	 * @since 4.5.2
	 */
	public static PrivateKey readPemPrivateKey(final InputStream pemStream) {
		return (PrivateKey) readPemKey(pemStream);
	}

	/**
	 * 读取PEM格式的公钥
	 *
	 * @param pemStream pem流
	 * @return {@link PublicKey}
	 * @since 4.5.2
	 */
	public static PublicKey readPemPublicKey(final InputStream pemStream) {
		return (PublicKey) readPemKey(pemStream);
	}

	/**
	 * 从pem文件中读取公钥或私钥<br>
	 * 根据类型返回 {@link PublicKey} 或者 {@link PrivateKey}
	 *
	 * @param keyStream pem流
	 * @return {@link Key}，null表示无法识别的密钥类型
	 * @since 5.1.6
	 */
	public static Key readPemKey(final InputStream keyStream) {
		final PemObject object = readPemObject(keyStream);
		final String type = object.getType();
		if (StrUtil.isNotBlank(type)) {
			//private
			if (type.endsWith("EC PRIVATE KEY")) {
				try {
					// 尝试PKCS#8
					return KeyUtil.generatePrivateKey("EC", object.getContent());
				} catch (final Exception e) {
					// 尝试PKCS#1
					return KeyUtil.generatePrivateKey("EC", ECKeyUtil.createOpenSSHPrivateKeySpec(object.getContent()));
				}
			}
			if (type.endsWith("PRIVATE KEY")) {
				return KeyUtil.generateRSAPrivateKey(object.getContent());
			}

			// public
			if (type.endsWith("EC PUBLIC KEY")) {
				try {
					// 尝试DER
					return KeyUtil.generatePublicKey("EC", object.getContent());
				} catch (Exception e) {
					// 尝试PKCS#1
					return KeyUtil.generatePublicKey("EC", ECKeyUtil.createOpenSSHPublicKeySpec(object.getContent()));
				}
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
	 * 从pem流中读取公钥或私钥
	 *
	 * @param keyStream pem流
	 * @return 密钥bytes
	 * @since 5.1.6
	 */
	public static byte[] readPem(final InputStream keyStream) {
		final PemObject pemObject = readPemObject(keyStream);
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
	public static PemObject readPemObject(final InputStream keyStream) {
		return readPemObject(IoUtil.toUtf8Reader(keyStream));
	}

	/**
	 * 读取pem文件中的信息，包括类型、头信息和密钥内容
	 *
	 * @param reader pem Reader
	 * @return {@link PemObject}
	 * @since 5.1.6
	 */
	public static PemObject readPemObject(final Reader reader) {
		PemReader pemReader = null;
		try {
			pemReader = new PemReader(reader);
			return pemReader.readPemObject();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.closeQuietly(pemReader);
		}
	}

	/**
	 * 将私钥或公钥转换为PEM格式的字符串
	 * @param type 密钥类型（私钥、公钥、证书）
	 * @param content 密钥内容
	 * @return PEM内容
	 * @since 5.5.9
	 */
	public static String toPem(final String type, final byte[] content) {
		final StringWriter stringWriter = new StringWriter();
		writePemObject(type, content, stringWriter);
		return stringWriter.toString();
	}

	/**
	 * 写出pem密钥（私钥、公钥、证书）
	 *
	 * @param type      密钥类型（私钥、公钥、证书）
	 * @param content   密钥内容，需为PKCS#1格式
	 * @param keyStream pem流
	 * @since 5.1.6
	 */
	public static void writePemObject(final String type, final byte[] content, final OutputStream keyStream) {
		writePemObject(new PemObject(type, content), keyStream);
	}

	/**
	 * 写出pem密钥（私钥、公钥、证书）
	 *
	 * @param type    密钥类型（私钥、公钥、证书）
	 * @param content 密钥内容，需为PKCS#1格式
	 * @param writer  pemWriter
	 * @since 5.5.9
	 */
	public static void writePemObject(final String type, final byte[] content, final Writer writer) {
		writePemObject(new PemObject(type, content), writer);
	}

	/**
	 * 写出pem密钥（私钥、公钥、证书）
	 *
	 * @param pemObject pem对象，包括密钥和密钥类型等信息
	 * @param keyStream pem流
	 * @since 5.1.6
	 */
	public static void writePemObject(final PemObjectGenerator pemObject, final OutputStream keyStream) {
		writePemObject(pemObject, IoUtil.toUtf8Writer(keyStream));
	}

	/**
	 * 写出pem密钥（私钥、公钥、证书）
	 *
	 * @param pemObject pem对象，包括密钥和密钥类型等信息
	 * @param writer    pemWriter
	 * @since 5.5.9
	 */
	public static void writePemObject(final PemObjectGenerator pemObject, final Writer writer) {
		final PemWriter pemWriter = new PemWriter(writer);
		try {
			pemWriter.writeObject(pemObject);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.closeQuietly(pemWriter);
		}
	}
}
