package cn.hutool.extra.qrcode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ImageUtil;

/**
 * 基于Zxing的二维码工具类
 * 
 * @author looly
 * @since 4.0.2
 *
 */
public class QrCodeUtil {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	
	/**
	 * 生成PNG格式的二维码图片，以byte[]形式表示
	 * @param content 内容
	 * @param width 宽度
	 * @param height 高度
	 * @return 图片的byte[]
	 * @since 4.0.10
	 */
	public static byte[] generatePng(String content, int width, int height) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		generate(content, width, height, ImageUtil.IMAGE_TYPE_PNG, out);
		return out.toByteArray();
	}

	/**
	 * 生成二维码到文件，二维码图片格式取决于文件的扩展名
	 * 
	 * @param content 文本内容
	 * @param width 宽度
	 * @param height 高度
	 * @param targetFile 目标文件，扩展名决定输出格式
	 * @return 目标文件
	 */
	public static File generate(String content, int width, int height, File targetFile) {
		final BufferedImage image = generate(content, width, height);
		ImageUtil.write(image, targetFile);
		return targetFile;
	}

	/**
	 * 生成二维码到输出流
	 * 
	 * @param content 文本内容
	 * @param width 宽度
	 * @param height 高度
	 * @param imageType 图片类型（图片扩展名），见{@link ImageUtil}
	 * @param out 目标流
	 */
	public static void generate(String content, int width, int height, String imageType, OutputStream out) {
		final BufferedImage image = generate(content, width, height);
		ImageUtil.write(image, imageType, out);
	}

	/**
	 * 生成二维码图片
	 * 
	 * @param content 文本内容
	 * @param width 宽度
	 * @param height 高度
	 * @return 二维码图片（黑白）
	 */
	public static BufferedImage generate(String content, int width, int height) {
		final BitMatrix bitMatrix = encode(content, width, height);
		return toImage(bitMatrix);
	}
	
	/**
	 * 将文本内容编码为二维码
	 * 
	 * @param content 文本内容
	 * @param width 宽度
	 * @param height 高度
	 * @return {@link BitMatrix}
	 */
	public static BitMatrix encode(String content, int width, int height) {
		return encode(content, BarcodeFormat.QR_CODE, width, height);
	}

	/**
	 * 将文本内容编码为条形码或二维码
	 * 
	 * @param content 文本内容
	 * @param format 格式枚举
	 * @param width 宽度
	 * @param height 高度
	 * @return {@link BitMatrix}
	 */
	public static BitMatrix encode(String content, BarcodeFormat format, int width, int height) {
		final MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		final HashMap<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, CharsetUtil.UTF_8);

		BitMatrix bitMatrix;
		try {
			bitMatrix = multiFormatWriter.encode(content, format, width, height, hints);
		} catch (WriterException e) {
			throw new QrCodeException(e);
		}

		return bitMatrix;
	}

	// ------------------------------------------------------------------------------------------------------------------- 解析二维码
	/**
	 * 解码二维码图片为文本
	 * 
	 * @param qrCodeInputstream 二维码输入流
	 * @return 解码文本
	 */
	public static String decode(InputStream qrCodeInputstream) {
		return decode(ImageUtil.read(qrCodeInputstream));
	}

	/**
	 * 解码二维码图片为文本
	 * 
	 * @param qrCodeFile 二维码文件
	 * @return 解码文本
	 */
	public static String decode(File qrCodeFile) {
		return decode(ImageUtil.read(qrCodeFile));
	}

	/**
	 * 将二维码图片解码为文本
	 * 
	 * @param image {@link Image} 二维码图片
	 * @return 解码后的文本
	 */
	public static String decode(Image image) {
		final MultiFormatReader formatReader = new MultiFormatReader();

		final LuminanceSource source = new BufferedImageLuminanceSource(ImageUtil.toBufferedImage(image));
		final Binarizer binarizer = new HybridBinarizer(source);
		final BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

		final HashMap<DecodeHintType, Object> hints = new HashMap<>();
		hints.put(DecodeHintType.CHARACTER_SET, CharsetUtil.UTF_8);
		Result result;
		try {
			result = formatReader.decode(binaryBitmap, hints);
		} catch (NotFoundException e) {
			throw new QrCodeException(e);
		}

		return result.getText();
	}

	/**
	 * BitMatrix转BufferedImage
	 * 
	 * @param matrix BitMatrix
	 * @return BufferedImage
	 */
	public static BufferedImage toImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
}
