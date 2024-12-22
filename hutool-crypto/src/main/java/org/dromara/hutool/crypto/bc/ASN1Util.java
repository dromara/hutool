/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.crypto.bc;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.crypto.CryptoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ASN.1 – Abstract Syntax Notation dot one，抽象记法1 工具类。<br>
 * ASN.1描述了一种对数据进行表示、编码、传输和解码的数据格式。它的编码格式包括DER、BER、DL等<br>
 *
 * @author looly
 * @since 5.7.10
 */
public class ASN1Util {

	/**
	 * 编码为DER格式
	 *
	 * @param elements ASN.1元素
	 * @return 编码后的bytes
	 */
	public static byte[] encodeDer(final ASN1Encodable... elements) {
		return encode(ASN1Encoding.DER, elements);
	}

	/**
	 * 编码为指定ASN1格式
	 *
	 * @param asn1Encoding 编码格式，见{@link ASN1Encoding}，可选DER、BER或DL
	 * @param elements     ASN.1元素
	 * @return 编码后的bytes
	 */
	public static byte[] encode(final String asn1Encoding, final ASN1Encodable... elements) {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		encodeTo(asn1Encoding, out, elements);
		return out.toByteArray();
	}

	/**
	 * 编码为指定ASN1格式
	 *
	 * @param asn1Encoding 编码格式，见{@link ASN1Encoding}，可选DER、BER或DL
	 * @param out          输出流
	 * @param elements     ASN.1元素
	 */
	public static void encodeTo(final String asn1Encoding, final OutputStream out, final ASN1Encodable... elements) {
		final ASN1Sequence sequence;
		switch (asn1Encoding) {
			case ASN1Encoding.DER:
				sequence = new DERSequence(elements);
				break;
			case ASN1Encoding.BER:
				sequence = new BERSequence(elements);
				break;
			case ASN1Encoding.DL:
				sequence = new DLSequence(elements);
				break;
			default:
				throw new CryptoException("Unsupported ASN1 encoding: {}", asn1Encoding);
		}
		try {
			sequence.encodeTo(out);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 读取ASN.1数据流为{@link ASN1Object}
	 *
	 * @param in ASN.1数据
	 * @return {@link ASN1Object}
	 */
	public static ASN1Object decode(final InputStream in) {
		final ASN1InputStream asn1In = new ASN1InputStream(in);
		try {
			return asn1In.readObject();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取ASN1格式的导出格式，一般用于调试
	 *
	 * @param in ASN.1数据
	 * @return {@link ASN1Object}的字符串表示形式
	 * @see ASN1Dump#dumpAsString(Object)
	 */
	public static String getDumpStr(final InputStream in) {
		return ASN1Dump.dumpAsString(decode(in));
	}

	/**
	 * 生成X500Name信息
	 *
	 * @param C  Country Name (国家代号),eg: CN
	 * @param ST State or Province Name (洲或者省份),eg: Beijing
	 * @param L  Locality Name (城市名),eg: Beijing
	 * @param O  Organization Name (可以是公司名称),
	 * @param OU Organizational Unit Name (可以是单位部门名称)
	 * @param CN Common Name (服务器ip或者域名),eg: 192.168.30.71 or www.baidu.com
	 * @return X500Name
	 */
	public static X500Name createX500Name(final String C, final String ST, final String L,
										  final String O, final String OU, final String CN) {
		return new X500NameBuilder()
			.addRDN(BCStyle.C, C)
			.addRDN(BCStyle.ST, ST)
			.addRDN(BCStyle.L, L)
			.addRDN(BCStyle.O, O)
			.addRDN(BCStyle.OU, OU)
			.addRDN(BCStyle.CN, CN)
			.build();
	}
}
