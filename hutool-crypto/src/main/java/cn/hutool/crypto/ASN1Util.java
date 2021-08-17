package cn.hutool.crypto;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IORuntimeException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.util.ASN1Dump;

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
	public static byte[] encodeDer(ASN1Encodable... elements) {
		return encode(ASN1Encoding.DER, elements);
	}

	/**
	 * 编码为指定ASN1格式
	 *
	 * @param asn1Encoding 编码格式，见{@link ASN1Encoding}，可选DER、BER或DL
	 * @param elements     ASN.1元素
	 * @return 编码后的bytes
	 */
	public static byte[] encode(String asn1Encoding, ASN1Encodable... elements) {
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
	public static void encodeTo(String asn1Encoding, OutputStream out, ASN1Encodable... elements) {
		ASN1Sequence sequence;
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
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 读取ASN.1数据流为{@link ASN1Object}
	 *
	 * @param in ASN.1数据
	 * @return {@link ASN1Object}
	 */
	public static ASN1Object decode(InputStream in) {
		final ASN1InputStream asn1In = new ASN1InputStream(in);
		try {
			return asn1In.readObject();
		} catch (IOException e) {
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
	public static String getDumpStr(InputStream in) {
		return ASN1Dump.dumpAsString(decode(in));
	}
}
