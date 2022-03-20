package cn.hutool.core.codec;

import cn.hutool.core.util.ArrayUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Base62编码解码实现，常用于短URL<br>
 * From https://github.com/seruco/base62
 *
 * @author Looly, Sebastian Ruhleder, sebastian@seruco.io
 * @since 4.5.9
 */
public class Base62Codec implements Encoder<byte[], byte[]>, Decoder<byte[], byte[]>, Serializable {
	private static final long serialVersionUID = 1L;

	private static final int STANDARD_BASE = 256;
	private static final int TARGET_BASE = 62;

	public static Base62Codec INSTANCE = new Base62Codec();

	/**
	 * 编码指定消息bytes为Base62格式的bytes
	 *
	 * @param data 被编码的消息
	 * @return Base62内容
	 */
	@Override
	public byte[] encode(byte[] data) {
		return encode(data, false);
	}

	/**
	 * 编码指定消息bytes为Base62格式的bytes
	 *
	 * @param data        被编码的消息
	 * @param useInverted 是否使用反转风格，即将GMP风格中的大小写做转换
	 * @return Base62内容
	 */
	public byte[] encode(byte[] data, boolean useInverted) {
		final Base62Encoder encoder = useInverted ? Base62Encoder.INVERTED_ENCODER : Base62Encoder.GMP_ENCODER;
		return encoder.encode(data);
	}

	/**
	 * 解码Base62消息
	 *
	 * @param encoded Base62内容
	 * @return 消息
	 */
	@Override
	public byte[] decode(byte[] encoded) {
		return decode(encoded, false);
	}

	/**
	 * 解码Base62消息
	 *
	 * @param encoded     Base62内容
	 * @param useInverted 是否使用反转风格，即将GMP风格中的大小写做转换
	 * @return 消息
	 */
	public byte[] decode(byte[] encoded, boolean useInverted) {
		final Base62Decoder decoder = useInverted ? Base62Decoder.INVERTED_DECODER : Base62Decoder.GMP_DECODER;
		return decoder.decode(encoded);
	}

	/**
	 * Base62编码器
	 *
	 * @since 5.8.0
	 */
	public static class Base62Encoder implements Encoder<byte[], byte[]> {
		/**
		 * GMP风格
		 */
		private static final byte[] GMP = { //
				'0', '1', '2', '3', '4', '5', '6', '7', //
				'8', '9', 'A', 'B', 'C', 'D', 'E', 'F', //
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', //
				'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', //
				'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', //
				'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', //
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', //
				'u', 'v', 'w', 'x', 'y', 'z' //
		};

		/**
		 * 反转风格，即将GMP风格中的大小写做转换
		 */
		private static final byte[] INVERTED = { //
				'0', '1', '2', '3', '4', '5', '6', '7', //
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', //
				'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', //
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', //
				'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', //
				'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', //
				'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', //
				'U', 'V', 'W', 'X', 'Y', 'Z' //
		};

		public static Base62Encoder GMP_ENCODER = new Base62Encoder(GMP);
		public static Base62Encoder INVERTED_ENCODER = new Base62Encoder(INVERTED);

		private final byte[] alphabet;

		/**
		 * 构造
		 *
		 * @param alphabet 字符表
		 */
		public Base62Encoder(byte[] alphabet) {
			this.alphabet = alphabet;
		}

		@Override
		public byte[] encode(byte[] data) {
			final byte[] indices = convert(data, STANDARD_BASE, TARGET_BASE);
			return translate(indices, alphabet);
		}
	}

	/**
	 * Base62解码器
	 *
	 * @since 5.8.0
	 */
	public static class Base62Decoder implements Decoder<byte[], byte[]> {

		public static Base62Decoder GMP_DECODER = new Base62Decoder(Base62Encoder.GMP);
		public static Base62Decoder INVERTED_DECODER = new Base62Decoder(Base62Encoder.INVERTED);

		private final byte[] lookupTable;

		/**
		 * 构造
		 *
		 * @param alphabet 字母表
		 */
		public Base62Decoder(byte[] alphabet) {
			lookupTable = new byte['z' + 1];
			for (int i = 0; i < alphabet.length; i++) {
				lookupTable[alphabet[i]] = (byte) i;
			}
		}


		@Override
		public byte[] decode(byte[] encoded) {
			final byte[] prepared = translate(encoded, lookupTable);
			return convert(prepared, TARGET_BASE, STANDARD_BASE);
		}
	}

	// region Private Methods

	/**
	 * 按照字典转换bytes
	 *
	 * @param indices    内容
	 * @param dictionary 字典
	 * @return 转换值
	 */
	private static byte[] translate(byte[] indices, byte[] dictionary) {
		final byte[] translation = new byte[indices.length];

		for (int i = 0; i < indices.length; i++) {
			translation[i] = dictionary[indices[i]];
		}

		return translation;
	}

	/**
	 * 使用定义的字母表从源基准到目标基准
	 *
	 * @param message    消息bytes
	 * @param sourceBase 源基准长度
	 * @param targetBase 目标基准长度
	 * @return 计算结果
	 */
	private static byte[] convert(byte[] message, int sourceBase, int targetBase) {
		// 计算结果长度，算法来自：http://codegolf.stackexchange.com/a/21672
		final int estimatedLength = estimateOutputLength(message.length, sourceBase, targetBase);

		final ByteArrayOutputStream out = new ByteArrayOutputStream(estimatedLength);

		byte[] source = message;

		while (source.length > 0) {
			final ByteArrayOutputStream quotient = new ByteArrayOutputStream(source.length);

			int remainder = 0;

			for (byte b : source) {
				final int accumulator = (b & 0xFF) + remainder * sourceBase;
				final int digit = (accumulator - (accumulator % targetBase)) / targetBase;

				remainder = accumulator % targetBase;

				if (quotient.size() > 0 || digit > 0) {
					quotient.write(digit);
				}
			}

			out.write(remainder);

			source = quotient.toByteArray();
		}

		// pad output with zeroes corresponding to the number of leading zeroes in the message
		for (int i = 0; i < message.length - 1 && message[i] == 0; i++) {
			out.write(0);
		}

		return ArrayUtil.reverse(out.toByteArray());
	}

	/**
	 * 估算结果长度
	 *
	 * @param inputLength 输入长度
	 * @param sourceBase  源基准长度
	 * @param targetBase  目标基准长度
	 * @return 估算长度
	 */
	private static int estimateOutputLength(int inputLength, int sourceBase, int targetBase) {
		return (int) Math.ceil((Math.log(sourceBase) / Math.log(targetBase)) * inputLength);
	}
	// endregion
}
