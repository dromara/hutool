package cn.hutool.extra.qrcode;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.Img;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.CharsetUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * 基于Zxing的二维码工具类
 *
 * @author looly
 * @since 4.0.2
 */
public class QrCodeUtil {

	/**
	 * 生成代 logo 图片的 Base64 编码格式的二维码，以 String 形式表示
	 *
	 * @param content    内容
	 * @param qrConfig   二维码配置，包括长、宽、边距、颜色等
	 * @param imageType  图片类型（图片扩展名），见{@link ImgUtil}
	 * @param logoBase64 logo 图片的 base64 编码
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64(String content, QrConfig qrConfig, String imageType, String logoBase64) {
		return generateAsBase64(content, qrConfig, imageType, Base64.decode(logoBase64));
	}

	/**
	 * 生成代 logo 图片的 Base64 编码格式的二维码，以 String 形式表示
	 *
	 * @param content   内容
	 * @param qrConfig  二维码配置，包括长、宽、边距、颜色等
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @param logo      logo 图片的byte[]
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64(String content, QrConfig qrConfig, String imageType, byte[] logo) {
		return generateAsBase64(content, qrConfig, imageType, ImgUtil.toImage(logo));
	}

	/**
	 * 生成代 logo 图片的 Base64 编码格式的二维码，以 String 形式表示
	 *
	 * @param content   内容
	 * @param qrConfig  二维码配置，包括长、宽、边距、颜色等
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @param logo      logo 图片的byte[]
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64(String content, QrConfig qrConfig, String imageType, Image logo) {
		qrConfig.setImg(logo);
		return generateAsBase64(content, qrConfig, imageType);
	}

	/**
	 * 生成 Base64 编码格式的二维码，以 String 形式表示
	 *
	 * <p>
	 *     输出格式为: data:image/[type];base64,[data]
	 * </p>
	 *
	 * @param content   内容
	 * @param qrConfig  二维码配置，包括长、宽、边距、颜色等
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @return 图片 Base64 编码字符串
	 */
	public static String generateAsBase64(String content, QrConfig qrConfig, String imageType) {
		final BufferedImage img = generate(content, qrConfig);
		return ImgUtil.toBase64DateUri(img, imageType);
	}

	/**
	 * 生成PNG格式的二维码图片，以byte[]形式表示
	 *
	 * @param content 内容
	 * @param width   宽度
	 * @param height  高度
	 * @return 图片的byte[]
	 * @since 4.0.10
	 */
	public static byte[] generatePng(String content, int width, int height) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		generate(content, width, height, ImgUtil.IMAGE_TYPE_PNG, out);
		return out.toByteArray();
	}

	/**
	 * 生成PNG格式的二维码图片，以byte[]形式表示
	 *
	 * @param content 内容
	 * @param config  二维码配置，包括长、宽、边距、颜色等
	 * @return 图片的byte[]
	 * @since 4.1.2
	 */
	public static byte[] generatePng(String content, QrConfig config) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		generate(content, config, ImgUtil.IMAGE_TYPE_PNG, out);
		return out.toByteArray();
	}

	/**
	 * 生成二维码到文件，二维码图片格式取决于文件的扩展名
	 *
	 * @param content    文本内容
	 * @param width      宽度
	 * @param height     高度
	 * @param targetFile 目标文件，扩展名决定输出格式
	 * @return 目标文件
	 */
	public static File generate(String content, int width, int height, File targetFile) {
		final BufferedImage image = generate(content, width, height);
		ImgUtil.write(image, targetFile);
		return targetFile;
	}

	/**
	 * 生成二维码到文件，二维码图片格式取决于文件的扩展名
	 *
	 * @param content    文本内容
	 * @param config     二维码配置，包括长、宽、边距、颜色等
	 * @param targetFile 目标文件，扩展名决定输出格式
	 * @return 目标文件
	 * @since 4.1.2
	 */
	public static File generate(String content, QrConfig config, File targetFile) {
		final BufferedImage image = generate(content, config);
		ImgUtil.write(image, targetFile);
		return targetFile;
	}

	/**
	 * 生成二维码到输出流
	 *
	 * @param content   文本内容
	 * @param width     宽度
	 * @param height    高度
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @param out       目标流
	 */
	public static void generate(String content, int width, int height, String imageType, OutputStream out) {
		final BufferedImage image = generate(content, width, height);
		ImgUtil.write(image, imageType, out);
	}

	/**
	 * 生成二维码到输出流
	 *
	 * @param content   文本内容
	 * @param config    二维码配置，包括长、宽、边距、颜色等
	 * @param imageType 图片类型（图片扩展名），见{@link ImgUtil}
	 * @param out       目标流
	 * @since 4.1.2
	 */
	public static void generate(String content, QrConfig config, String imageType, OutputStream out) {
		final BufferedImage image = generate(content, config);
		ImgUtil.write(image, imageType, out);
	}

	/**
	 * 生成二维码图片
	 *
	 * @param content 文本内容
	 * @param width   宽度
	 * @param height  高度
	 * @return 二维码图片（黑白）
	 */
	public static BufferedImage generate(String content, int width, int height) {
		return generate(content, new QrConfig(width, height));
	}

	/**
	 * 生成二维码或条形码图片
	 *
	 * @param content 文本内容
	 * @param format  格式，可选二维码或者条形码
	 * @param width   宽度
	 * @param height  高度
	 * @return 二维码图片（黑白）
	 */
	public static BufferedImage generate(String content, BarcodeFormat format, int width, int height) {
		return generate(content, format, new QrConfig(width, height));
	}

	/**
	 * 生成二维码图片
	 *
	 * @param content 文本内容
	 * @param config  二维码配置，包括长、宽、边距、颜色等
	 * @return 二维码图片（黑白）
	 * @since 4.1.2
	 */
	public static BufferedImage generate(String content, QrConfig config) {
		return generate(content, BarcodeFormat.QR_CODE, config);
	}

	/**
	 * 生成二维码或条形码图片<br>
	 * 只有二维码时QrConfig中的图片才有效
	 *
	 * @param content 文本内容
	 * @param format  格式，可选二维码、条形码等
	 * @param config  二维码配置，包括长、宽、边距、颜色等
	 * @return 二维码图片（黑白）
	 * @since 4.1.14
	 */
	public static BufferedImage generate(String content, BarcodeFormat format, QrConfig config) {
		final BitMatrix bitMatrix = encode(content, format, config);
		final BufferedImage image = toImage(bitMatrix, config.foreColor, config.backColor);
		final Image logoImg = config.img;
		if (null != logoImg && BarcodeFormat.QR_CODE == format) {
			// 只有二维码可以贴图
			final int qrWidth = image.getWidth();
			final int qrHeight = image.getHeight();
			int width;
			int height;
			// 按照最短的边做比例缩放
			if (qrWidth < qrHeight) {
				width = qrWidth / config.ratio;
				height = logoImg.getHeight(null) * width / logoImg.getWidth(null);
			} else {
				height = qrHeight / config.ratio;
				width = logoImg.getWidth(null) * height / logoImg.getHeight(null);
			}

			Img.from(image).pressImage(//
					Img.from(logoImg).round(0.3).getImg(), // 圆角
					new Rectangle(width, height), //
					1//
			);
		}
		return image;
	}

	// ------------------------------------------------------------------------------------------------------------------- encode

	/**
	 * 将文本内容编码为二维码
	 *
	 * @param content 文本内容
	 * @param width   宽度
	 * @param height  高度
	 * @return {@link BitMatrix}
	 */
	public static BitMatrix encode(String content, int width, int height) {
		return encode(content, BarcodeFormat.QR_CODE, width, height);
	}

	/**
	 * 将文本内容编码为二维码
	 *
	 * @param content 文本内容
	 * @param config  二维码配置，包括长、宽、边距、颜色等
	 * @return {@link BitMatrix}
	 * @since 4.1.2
	 */
	public static BitMatrix encode(String content, QrConfig config) {
		return encode(content, BarcodeFormat.QR_CODE, config);
	}

	/**
	 * 将文本内容编码为条形码或二维码
	 *
	 * @param content 文本内容
	 * @param format  格式枚举
	 * @param width   宽度
	 * @param height  高度
	 * @return {@link BitMatrix}
	 */
	public static BitMatrix encode(String content, BarcodeFormat format, int width, int height) {
		return encode(content, format, new QrConfig(width, height));
	}

	/**
	 * 将文本内容编码为条形码或二维码
	 *
	 * @param content 文本内容
	 * @param format  格式枚举
	 * @param config  二维码配置，包括长、宽、边距、颜色等
	 * @return {@link BitMatrix}
	 * @since 4.1.2
	 */
	public static BitMatrix encode(String content, BarcodeFormat format, QrConfig config) {
		final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		if (null == config) {
			// 默认配置
			config = new QrConfig();
		}
		BitMatrix bitMatrix;
		try {
			bitMatrix = multiFormatWriter.encode(content, format, config.width, config.height, config.toHints());
		} catch (WriterException e) {
			throw new QrCodeException(e);
		}

		return bitMatrix;
	}

	// ------------------------------------------------------------------------------------------------------------------- decode

	/**
	 * 解码二维码图片为文本
	 *
	 * @param qrCodeInputstream 二维码输入流
	 * @return 解码文本
	 */
	public static String decode(InputStream qrCodeInputstream) {
		return decode(ImgUtil.read(qrCodeInputstream));
	}

	/**
	 * 解码二维码图片为文本
	 *
	 * @param qrCodeFile 二维码文件
	 * @return 解码文本
	 */
	public static String decode(File qrCodeFile) {
		return decode(ImgUtil.read(qrCodeFile));
	}

	/**
	 * 将二维码图片解码为文本
	 *
	 * @param image {@link Image} 二维码图片
	 * @return 解码后的文本
	 */
	public static String decode(Image image) {
		return decode(image, true, false);
	}

	/**
	 * 将二维码图片解码为文本
	 *
	 * @param image         {@link Image} 二维码图片
	 * @param isTryHarder   是否优化精度
	 * @param isPureBarcode 是否使用复杂模式，扫描带logo的二维码设为true
	 * @return 解码后的文本
	 * @since 4.3.1
	 */
	public static String decode(Image image, boolean isTryHarder, boolean isPureBarcode) {
		final MultiFormatReader formatReader = new MultiFormatReader();

		final LuminanceSource source = new BufferedImageLuminanceSource(ImgUtil.toBufferedImage(image));
		final Binarizer binarizer = new HybridBinarizer(source);
		final BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

		final HashMap<DecodeHintType, Object> hints = new HashMap<>();
		hints.put(DecodeHintType.CHARACTER_SET, CharsetUtil.UTF_8);
		// 优化精度
		hints.put(DecodeHintType.TRY_HARDER, isTryHarder);
		// 复杂模式，开启PURE_BARCODE模式
		hints.put(DecodeHintType.PURE_BARCODE, isPureBarcode);
		Result result;
		try {
			result = formatReader.decode(binaryBitmap, hints);
		} catch (NotFoundException e) {
			// 报错尝试关闭复杂模式
			hints.remove(DecodeHintType.PURE_BARCODE);
			try {
				result = formatReader.decode(binaryBitmap, hints);
			} catch (NotFoundException e1) {
				throw new QrCodeException(e1);
			}
		}

		return result.getText();
	}

	/**
	 * BitMatrix转BufferedImage
	 *
	 * @param matrix    BitMatrix
	 * @param foreColor 前景色
	 * @param backColor 背景色(null表示透明背景)
	 * @return BufferedImage
	 * @since 4.1.2
	 */
	public static BufferedImage toImage(BitMatrix matrix, int foreColor, Integer backColor) {
		final int width = matrix.getWidth();
		final int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, null == backColor ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (matrix.get(x, y)) {
					image.setRGB(x, y, foreColor);
				} else if (null != backColor) {
					image.setRGB(x, y, backColor);
				}
			}
		}
		return image;
	}
}
