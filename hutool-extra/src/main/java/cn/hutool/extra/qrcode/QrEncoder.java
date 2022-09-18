package cn.hutool.extra.qrcode;

import cn.hutool.core.codec.Encoder;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ObjUtil;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码（条形码等）编码器，用于将文本内容转换为二维码
 *
 * @author looly
 * @since 6.0.0
 */
public class QrEncoder implements Encoder<CharSequence, BitMatrix> {

	/**
	 * 创建QrEncoder
	 *
	 * @param config {@link QrConfig}
	 * @return QrEncoder
	 */
	public static QrEncoder of(final QrConfig config) {
		return new QrEncoder(config);
	}

	private final QrConfig config;

	/**
	 * 构造
	 *
	 * @param config {@link QrConfig}
	 */
	public QrEncoder(final QrConfig config) {
		this.config = ObjUtil.defaultIfNull(config, QrConfig::of);
	}

	@Override
	public BitMatrix encode(final CharSequence content) {
		final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

		final BitMatrix bitMatrix;
		try {
			bitMatrix = multiFormatWriter.encode(
					StrUtil.toString(content),
					config.format, config.width, config.height,
					config.toHints());
		} catch (final WriterException e) {
			throw new QrCodeException(e);
		}

		return bitMatrix;
	}
}
