package cn.hutool.swing.img;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片写出封装
 */
public class ImgWriter {

	/**
	 * 创建图片写出器
	 *
	 * @param image           图片
	 * @param imageType       图片类型（图片扩展名），{@code null}表示使用RGB模式（JPG）
	 * @param backgroundColor 背景色{@link Color}，{@code null}表示黑色或透明
	 * @return {@code ImgWriter}
	 */
	public static ImgWriter of(final Image image, final String imageType, final Color backgroundColor) {
		return of(ImgUtil.toBufferedImage(image, imageType, backgroundColor), imageType);
	}

	/**
	 * 创建图片写出器
	 *
	 * @param image     图片
	 * @param imageType 图片类型（图片扩展名），{@code null}表示使用RGB模式（JPG）
	 * @return {@code ImgWriter}
	 */
	public static ImgWriter of(final Image image, final String imageType) {
		return new ImgWriter(image, imageType);
	}

	private final RenderedImage image;
	private final ImageWriter writer;
	private ImageWriteParam writeParam;

	/**
	 * 构造
	 *
	 * @param image     {@link Image}
	 * @param imageType 图片类型（图片扩展名），{@code null}表示使用RGB模式（JPG）
	 */
	public ImgWriter(final Image image, final String imageType) {
		this.image = ImgUtil.toRenderedImage(image);
		this.writer = ImgUtil.getWriter(image, imageType);
	}

	/**
	 * 设置写出质量，数字为0~1（不包括0和1）表示质量压缩比，除此数字外设置表示不压缩
	 *
	 * @param quality 写出质量，数字为0~1（不包括0和1）表示质量压缩比，除此数字外设置表示不压缩
	 * @return this
	 */
	public ImgWriter setQuality(final float quality) {
		this.writeParam = buildParam(this.image, this.writer, quality);
		return this;
	}

	/**
	 * 写出图像：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG<br>
	 * 此方法并不关闭流
	 *
	 * @param out     写出到的目标流
	 * @throws IORuntimeException IO异常
	 */
	public void write(final OutputStream out) throws IORuntimeException {
		write(ImgUtil.getImageOutputStream(out));
	}

	/**
	 * 写出图像为目标文件扩展名对应的格式
	 *
	 * @param targetFile 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public void write(final File targetFile) throws IORuntimeException {
		FileUtil.touch(targetFile);
		ImageOutputStream out = null;
		try {
			out = ImgUtil.getImageOutputStream(targetFile);
			write(out);
		} finally {
			IoUtil.close(out);
		}
	}

	/**
	 * 通过{@link ImageWriter}写出图片到输出流
	 *
	 * @param output 输出的Image流{@link ImageOutputStream}， 非空
	 */
	public void write(final ImageOutputStream output) {
		Assert.notNull(output);

		final ImageWriter writer = this.writer;
		final RenderedImage image = this.image;
		writer.setOutput(output);
		// 设置质量
		try {
			if (null != this.writeParam) {
				writer.write(null, new IIOImage(image, null, null), this.writeParam);
			} else {
				writer.write(image);
			}
			output.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			writer.dispose();
		}
	}

	/**
	 * 构建图片写出参数
	 *
	 * @param renderedImage 图片
	 * @param writer        {@link ImageWriter}
	 * @param quality       质量，范围0~1
	 * @return {@link ImageWriteParam} or {@code null}
	 */
	private static ImageWriteParam buildParam(final RenderedImage renderedImage, final ImageWriter writer, final float quality) {
		// 设置质量
		ImageWriteParam imgWriteParams = null;
		if (quality > 0 && quality < 1) {
			imgWriteParams = writer.getDefaultWriteParam();
			if (imgWriteParams.canWriteCompressed()) {
				imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imgWriteParams.setCompressionQuality(quality);
				final ColorModel colorModel = renderedImage.getColorModel();// ColorModel.getRGBdefault();
				imgWriteParams.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
			}
		}
		return imgWriteParams;
	}
}
