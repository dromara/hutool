/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.img;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.text.StrUtil;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、旋转、图像类型转换、彩色转黑白、文字水印、图片水印等 <br>
 * 参考：<a href="http://blog.csdn.net/zhangzhikaixinya/article/details/8459400">http://blog.csdn.net/zhangzhikaixinya/article/details/8459400</a>
 *
 * @author Looly
 */
public class ImgUtil {

	// region ----- [const] image type
	/**
	 * GIF
	 */
	public static final String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	/**
	 * JPG
	 */
	public static final String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	/**
	 * JPEG
	 */
	public static final String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	/**
	 * BMP
	 */
	public static final String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	/**
	 * PNG
	 */
	public static final String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	/**
	 * PSD
	 */
	public static final String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop
	// endregion

	// region ----- scale

	/**
	 * 缩放图像（按比例缩放），目标文件的扩展名决定目标文件类型
	 *
	 * @param srcImageFile  源图像文件
	 * @param destImageFile 缩放后的图像文件，扩展名决定目标类型
	 * @param scale         缩放比例。比例大于1时为放大，小于1大于0为缩小
	 */
	public static void scale(final File srcImageFile, final File destImageFile, final float scale) {
		scale(read(srcImageFile), destImageFile, scale);
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcStream  源图像来源流
	 * @param destStream 缩放后的图像写出到的流
	 * @param scale      缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @since 3.0.9
	 */
	public static void scale(final InputStream srcStream, final OutputStream destStream, final float scale) {
		scale(read(srcStream), destStream, scale);
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcStream  源图像来源流
	 * @param destStream 缩放后的图像写出到的流
	 * @param scale      缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @since 3.1.0
	 */
	public static void scale(final ImageInputStream srcStream, final ImageOutputStream destStream, final float scale) {
		scale(read(srcStream), destStream, scale);
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcImg   源图像来源流
	 * @param destFile 缩放后的图像写出到的流
	 * @param scale    缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void scale(final Image srcImg, final File destFile, final float scale) throws IORuntimeException {
		Img.from(srcImg).setTargetImageType(FileNameUtil.extName(destFile)).scale(scale).write(destFile);
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcImg 源图像来源流
	 * @param out    缩放后的图像写出到的流
	 * @param scale  缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void scale(final Image srcImg, final OutputStream out, final float scale) throws IORuntimeException {
		scale(srcImg, getImageOutputStream(out), scale);
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcImg          源图像来源流
	 * @param destImageStream 缩放后的图像写出到的流
	 * @param scale           缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @throws IORuntimeException IO异常
	 * @since 3.1.0
	 */
	public static void scale(final Image srcImg, final ImageOutputStream destImageStream, final float scale) throws IORuntimeException {
		writeJpg(scale(srcImg, scale), destImageStream);
	}

	/**
	 * 缩放图像（按比例缩放）
	 *
	 * @param srcImg 源图像来源流
	 * @param scale  缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @return {@link Image}
	 * @since 3.1.0
	 */
	public static Image scale(final Image srcImg, final float scale) {
		return Img.from(srcImg).scale(scale).getImg();
	}

	/**
	 * 缩放图像（按长宽缩放）<br>
	 * 注意：目标长宽与原图不成比例会变形
	 *
	 * @param srcImg 源图像来源流
	 * @param width  目标宽度
	 * @param height 目标高度
	 * @return {@link Image}
	 * @since 3.1.0
	 */
	public static Image scale(final Image srcImg, final int width, final int height) {
		return Img.from(srcImg).scale(width, height).getImg();
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认格式与源图片相同，无法识别原图片默认JPG
	 *
	 * @param srcImageFile  源图像文件地址
	 * @param destImageFile 缩放后的图像地址
	 * @param width         缩放后的宽度
	 * @param height        缩放后的高度
	 * @param fixedColor    补充的颜色，不补充为{@code null}
	 * @throws IORuntimeException IO异常
	 */
	public static void scale(final File srcImageFile, final File destImageFile, final int width, final int height, final Color fixedColor) throws IORuntimeException {
		Img.from(srcImageFile)//
			.setTargetImageType(FileNameUtil.extName(destImageFile))//
			.scale(width, height, fixedColor)//
			.write(destImageFile);
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 缩放后的图像目标流
	 * @param width      缩放后的宽度
	 * @param height     缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为{@code null}
	 * @throws IORuntimeException IO异常
	 */
	public static void scale(final InputStream srcStream, final OutputStream destStream, final int width, final int height, final Color fixedColor) throws IORuntimeException {
		scale(read(srcStream), getImageOutputStream(destStream), width, height, fixedColor);
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 缩放后的图像目标流
	 * @param width      缩放后的宽度
	 * @param height     缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为{@code null}
	 * @throws IORuntimeException IO异常
	 */
	public static void scale(final ImageInputStream srcStream, final ImageOutputStream destStream, final int width, final int height, final Color fixedColor) throws IORuntimeException {
		scale(read(srcStream), destStream, width, height, fixedColor);
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式，此方法并不关闭流
	 *
	 * @param srcImage        源图像
	 * @param destImageStream 缩放后的图像目标流
	 * @param width           缩放后的宽度
	 * @param height          缩放后的高度
	 * @param fixedColor      比例不对时补充的颜色，不补充为{@code null}
	 * @throws IORuntimeException IO异常
	 */
	public static void scale(final Image srcImage, final ImageOutputStream destImageStream, final int width, final int height, final Color fixedColor) throws IORuntimeException {
		writeJpg(scale(srcImage, width, height, fixedColor), destImageStream);
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 *
	 * @param srcImage   源图像
	 * @param width      缩放后的宽度
	 * @param height     缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为{@code null}
	 * @return {@link Image}
	 */
	public static Image scale(final Image srcImage, final int width, final int height, final Color fixedColor) {
		return Img.from(srcImage).scale(width, height, fixedColor).getImg();
	}
	// endregion

	// region ----- cut and slice

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 *
	 * @param srcImgFile  源图像文件
	 * @param destImgFile 切片后的图像文件
	 * @param rectangle   矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public static void cut(final File srcImgFile, final File destImgFile, final Rectangle rectangle) {
		cut(read(srcImgFile), destImgFile, rectangle);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)，此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 切片后的图像输出流
	 * @param rectangle  矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public static void cut(final InputStream srcStream, final OutputStream destStream, final Rectangle rectangle) {
		cut(read(srcStream), destStream, rectangle);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)，此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 切片后的图像输出流
	 * @param rectangle  矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public static void cut(final ImageInputStream srcStream, final ImageOutputStream destStream, final Rectangle rectangle) {
		cut(read(srcStream), destStream, rectangle);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)，此方法并不关闭流
	 *
	 * @param srcImage  源图像
	 * @param destFile  输出的文件
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void cut(final Image srcImage, final File destFile, final Rectangle rectangle) throws IORuntimeException {
		write(cut(srcImage, rectangle), destFile);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)，此方法并不关闭流
	 *
	 * @param srcImage  源图像
	 * @param out       切片后的图像输出流
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @throws IORuntimeException IO异常
	 * @since 3.1.0
	 */
	public static void cut(final Image srcImage, final OutputStream out, final Rectangle rectangle) throws IORuntimeException {
		cut(srcImage, getImageOutputStream(out), rectangle);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)，此方法并不关闭流
	 *
	 * @param srcImage        源图像
	 * @param destImageStream 切片后的图像输出流
	 * @param rectangle       矩形对象，表示矩形区域的x，y，width，height
	 * @throws IORuntimeException IO异常
	 * @since 3.1.0
	 */
	public static void cut(final Image srcImage, final ImageOutputStream destImageStream, final Rectangle rectangle) throws IORuntimeException {
		writeJpg(cut(srcImage, rectangle), destImageStream);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 *
	 * @param srcImage  源图像
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @return {@link BufferedImage}
	 * @since 3.1.0
	 */
	public static Image cut(final Image srcImage, final Rectangle rectangle) {
		return Img.from(srcImage).setPositionBaseCentre(false).cut(rectangle).getImg();
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)，填充满整个图片（直径取长宽最小值）
	 *
	 * @param srcImage 源图像
	 * @param x        原图的x坐标起始位置
	 * @param y        原图的y坐标起始位置
	 * @return {@link Image}
	 * @since 4.1.15
	 */
	public static Image cut(final Image srcImage, final int x, final int y) {
		return cut(srcImage, x, y, -1);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 *
	 * @param srcImage 源图像
	 * @param x        原图的x坐标起始位置
	 * @param y        原图的y坐标起始位置
	 * @param radius   半径，小于0表示填充满整个图片（直径取长宽最小值）
	 * @return {@link Image}
	 * @since 4.1.15
	 */
	public static Image cut(final Image srcImage, final int x, final int y, final int radius) {
		return Img.from(srcImage).cut(x, y, radius).getImg();
	}

	/**
	 * 图像切片（指定切片的宽度和高度）
	 *
	 * @param srcImageFile 源图像
	 * @param descDir      切片目标文件夹
	 * @param destWidth    目标切片宽度。默认200
	 * @param destHeight   目标切片高度。默认150
	 */
	public static void slice(final File srcImageFile, final File descDir, final int destWidth, final int destHeight) {
		slice(read(srcImageFile), descDir, destWidth, destHeight);
	}

	/**
	 * 图像切片（指定切片的宽度和高度）
	 *
	 * @param srcImage   源图像
	 * @param descDir    切片目标文件夹
	 * @param destWidth  目标切片宽度。默认200
	 * @param destHeight 目标切片高度。默认150
	 */
	public static void slice(final Image srcImage, final File descDir, int destWidth, int destHeight) {
		if (destWidth <= 0) {
			destWidth = 200; // 切片宽度
		}
		if (destHeight <= 0) {
			destHeight = 150; // 切片高度
		}
		final int srcWidth = srcImage.getWidth(null); // 源图宽度
		final int srcHeight = srcImage.getHeight(null); // 源图高度

		if (srcWidth < destWidth) {
			destWidth = srcWidth;
		}
		if (srcHeight < destHeight) {
			destHeight = srcHeight;
		}

		final int cols; // 切片横向数量
		final int rows; // 切片纵向数量
		// 计算切片的横向和纵向数量
		if (srcWidth % destWidth == 0) {
			cols = srcWidth / destWidth;
		} else {
			cols = (int) Math.floor((double) srcWidth / destWidth) + 1;
		}
		if (srcHeight % destHeight == 0) {
			rows = srcHeight / destHeight;
		} else {
			rows = (int) Math.floor((double) srcHeight / destHeight) + 1;
		}
		// 循环建立切片
		Image tag;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				tag = cut(srcImage, new Rectangle(j * destWidth, i * destHeight, destWidth, destHeight));
				// 输出为文件
				write(tag, FileUtil.file(descDir, "_r" + i + "_c" + j + ".jpg"));
			}
		}
	}

	/**
	 * 图像切割（指定切片的行数和列数）
	 *
	 * @param srcImageFile 源图像文件
	 * @param targetDir    切片目标文件夹
	 * @param formatName   格式名称，即图片格式后缀
	 * @param rows         目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols         目标切片列数。默认2，必须是范围 [1, 20] 之内
	 */
	public static void sliceByRowsAndCols(final File srcImageFile, final File targetDir, final String formatName, final int rows, final int cols) {
		sliceByRowsAndCols(read(srcImageFile), targetDir, formatName, rows, cols);
	}

	/**
	 * 图像切割（指定切片的行数和列数），默认RGB模式
	 *
	 * @param srcImage   源图像，如果非{@link BufferedImage}，则默认使用RGB模式
	 * @param destDir    切片目标文件夹
	 * @param formatName 格式名称，即图片格式后缀
	 * @param rows       目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols       目标切片列数。默认2，必须是范围 [1, 20] 之内
	 */
	public static void sliceByRowsAndCols(final Image srcImage, final File destDir, final String formatName, int rows, int cols) {
		if (!destDir.exists()) {
			FileUtil.mkdir(destDir);
		} else if (!destDir.isDirectory()) {
			throw new IllegalArgumentException("Destination must be a Directory !");
		}

		if (rows <= 0 || rows > 20) {
			rows = 2; // 切片行数
		}
		if (cols <= 0 || cols > 20) {
			cols = 2; // 切片列数
		}
		// 读取源图像
		final int srcWidth = srcImage.getWidth(null); // 源图宽度
		final int srcHeight = srcImage.getHeight(null); // 源图高度

		final int targetWidth = NumberUtil.partValue(srcWidth, cols); // 每张切片的宽度
		final int targetHeight = NumberUtil.partValue(srcHeight, rows); // 每张切片的高度

		// 循环建立切片
		Image tag;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tag = cut(srcImage, new Rectangle(j * targetWidth, i * targetHeight, targetWidth, targetHeight));
				// 输出为文件
				write(tag, new File(destDir, "_r" + i + "_c" + j + "." + formatName));
			}
		}
	}
	// endregion

	// region ----- convert

	/**
	 * 图像类型转换：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG
	 *
	 * @param srcImageFile    源图像文件
	 * @param targetImageFile 目标图像文件
	 */
	public static void convert(final File srcImageFile, final File targetImageFile) {
		Assert.notNull(srcImageFile);
		Assert.notNull(targetImageFile);
		Assert.isFalse(srcImageFile.equals(targetImageFile), "Src file is equals to dest file!");

		// 通过扩展名检查图片类型，相同类型直接复制
		final String srcExtName = FileNameUtil.extName(srcImageFile);
		final String destExtName = FileNameUtil.extName(targetImageFile);
		if (StrUtil.equalsIgnoreCase(srcExtName, destExtName)) {
			// 扩展名相同直接复制文件
			FileUtil.copy(srcImageFile, targetImageFile, true);
		}

		Img.from(srcImageFile).write(targetImageFile);
	}

	/**
	 * 图像类型转换：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream    源图像流
	 * @param formatName   包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param targetStream 目标图像输出流
	 * @since 3.0.9
	 */
	public static void convert(final InputStream srcStream, final String formatName, final OutputStream targetStream) {
		write(read(srcStream), formatName, getImageOutputStream(targetStream));
	}

	/**
	 * 图像类型转换：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage          源图像流
	 * @param formatName        包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param targetImageStream 目标图像输出流
	 * @since 4.1.14
	 */
	public static void convert(final Image srcImage, final String formatName, final ImageOutputStream targetImageStream) {
		Img.from(srcImage).setTargetImageType(formatName).write(targetImageStream);
	}
	// endregion

	// region ----- grey

	/**
	 * 彩色转为黑白
	 *
	 * @param srcImageFile  源图像地址
	 * @param destImageFile 目标图像地址
	 */
	public static void gray(final File srcImageFile, final File destImageFile) {
		gray(read(srcImageFile), destImageFile);
	}

	/**
	 * 彩色转为黑白<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 目标图像流
	 * @since 3.0.9
	 */
	public static void gray(final InputStream srcStream, final OutputStream destStream) {
		gray(read(srcStream), getImageOutputStream(destStream));
	}

	/**
	 * 彩色转为黑白<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 目标图像流
	 * @since 3.0.9
	 */
	public static void gray(final ImageInputStream srcStream, final ImageOutputStream destStream) {
		gray(read(srcStream), destStream);
	}

	/**
	 * 彩色转为黑白
	 *
	 * @param srcImage 源图像流
	 * @param outFile  目标文件
	 * @since 3.2.2
	 */
	public static void gray(final Image srcImage, final File outFile) {
		write(gray(srcImage), outFile);
	}

	/**
	 * 彩色转为黑白<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage 源图像流
	 * @param out      目标图像流
	 * @since 3.2.2
	 */
	public static void gray(final Image srcImage, final OutputStream out) {
		gray(srcImage, getImageOutputStream(out));
	}

	/**
	 * 彩色转为黑白<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage        源图像流
	 * @param destImageStream 目标图像流
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public static void gray(final Image srcImage, final ImageOutputStream destImageStream) throws IORuntimeException {
		writeJpg(gray(srcImage), destImageStream);
	}

	/**
	 * 彩色转为黑白
	 *
	 * @param srcImage 源图像流
	 * @return {@link Image}灰度后的图片
	 * @since 3.1.0
	 */
	public static Image gray(final Image srcImage) {
		return Img.from(srcImage).gray().getImg();
	}
	// endregion

	// region ----- binary

	/**
	 * 彩色转为黑白二值化图片，根据目标文件扩展名确定转换后的格式
	 *
	 * @param srcImageFile  源图像地址
	 * @param destImageFile 目标图像地址
	 */
	public static void binary(final File srcImageFile, final File destImageFile) {
		binary(read(srcImageFile), destImageFile);
	}

	/**
	 * 彩色转为黑白二值化图片<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 目标图像流
	 * @param imageType  图片格式(扩展名)
	 * @since 4.0.5
	 */
	public static void binary(final InputStream srcStream, final OutputStream destStream, final String imageType) {
		binary(read(srcStream), getImageOutputStream(destStream), imageType);
	}

	/**
	 * 彩色转为黑白黑白二值化图片<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 目标图像流
	 * @param imageType  图片格式(扩展名)
	 * @since 4.0.5
	 */
	public static void binary(final ImageInputStream srcStream, final ImageOutputStream destStream, final String imageType) {
		binary(read(srcStream), destStream, imageType);
	}

	/**
	 * 彩色转为黑白二值化图片，根据目标文件扩展名确定转换后的格式
	 *
	 * @param srcImage 源图像流
	 * @param outFile  目标文件
	 * @since 4.0.5
	 */
	public static void binary(final Image srcImage, final File outFile) {
		write(binary(srcImage), outFile);
	}

	/**
	 * 彩色转为黑白二值化图片<br>
	 * 此方法并不关闭流，输出JPG格式
	 *
	 * @param srcImage  源图像流
	 * @param out       目标图像流
	 * @param imageType 图片格式(扩展名)
	 * @since 4.0.5
	 */
	public static void binary(final Image srcImage, final OutputStream out, final String imageType) {
		binary(srcImage, getImageOutputStream(out), imageType);
	}

	/**
	 * 彩色转为黑白二值化图片<br>
	 * 此方法并不关闭流，输出JPG格式
	 *
	 * @param srcImage        源图像流
	 * @param destImageStream 目标图像流
	 * @param imageType       图片格式(扩展名)
	 * @throws IORuntimeException IO异常
	 * @since 4.0.5
	 */
	public static void binary(final Image srcImage, final ImageOutputStream destImageStream, final String imageType) throws IORuntimeException {
		write(binary(srcImage), imageType, destImageStream);
	}

	/**
	 * 彩色转为黑白二值化图片
	 *
	 * @param srcImage 源图像流
	 * @return {@link Image}二值化后的图片
	 * @since 4.0.5
	 */
	public static Image binary(final Image srcImage) {
		return Img.from(srcImage).binary().getImg();
	}
	// endregion

	// region ----- press

	/**
	 * 给图片添加文字水印
	 *
	 * @param imageFile 源图像文件
	 * @param destFile  目标图像文件
	 * @param pressText 水印文字
	 */
	public static void pressText(final File imageFile, final File destFile, final DisplayText pressText) {
		pressText(read(imageFile), destFile, pressText);
	}

	/**
	 * 给图片添加文字水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 目标图像流
	 * @param pressText  水印文本信息
	 */
	public static void pressText(final InputStream srcStream, final OutputStream destStream, final DisplayText pressText) {
		pressText(read(srcStream), getImageOutputStream(destStream), pressText);
	}

	/**
	 * 给图片添加文字水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage  源图像
	 * @param destFile  目标流
	 * @param pressText 水印文字信息
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void pressText(final Image srcImage, final File destFile, final DisplayText pressText) throws IORuntimeException {
		write(pressText(srcImage, pressText), destFile);
	}

	/**
	 * 给图片添加文字水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage  源图像
	 * @param to        目标流
	 * @param pressText 水印文字信息
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void pressText(final Image srcImage, final OutputStream to, final DisplayText pressText) throws IORuntimeException {
		pressText(srcImage, getImageOutputStream(to), pressText);
	}

	/**
	 * 给图片添加文字水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage        源图像
	 * @param destImageStream 目标图像流
	 * @param pressText       水印文字信息
	 * @throws IORuntimeException IO异常
	 */
	public static void pressText(final Image srcImage, final ImageOutputStream destImageStream, final DisplayText pressText) throws IORuntimeException {
		writeJpg(pressText(srcImage, pressText), destImageStream);
	}

	/**
	 * 给图片添加文字水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage  源图像
	 * @param pressText 水印文字信息
	 * @return 处理后的图像
	 * @since 3.2.2
	 */
	public static Image pressText(final Image srcImage, final DisplayText pressText) {
		return Img.from(srcImage).pressText(pressText).getImg();
	}

	/**
	 * 给图片添加全屏文字水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage   源图像
	 * @param pressText  水印文字
	 * @param color      水印的字体颜色
	 * @param font       {@link Font} 字体相关信息，如果默认则为{@code null}
	 * @param lineHeight 行高
	 * @param degree     旋转角度，（单位：弧度），以圆点（0,0）为圆心，正代表顺时针，负代表逆时针
	 * @param alpha      透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 处理后的图像
	 */
	public static Image pressTextFull(final Image srcImage, final String pressText, final Color color, final Font font, final int lineHeight, final int degree, final float alpha) {
		return Img.from(srcImage).pressTextFull(pressText, color, font, lineHeight, degree, alpha).getImg();
	}


	/**
	 * 给图片添加图片水印
	 *
	 * @param srcImageFile  源图像文件
	 * @param destImageFile 目标图像文件
	 * @param pressImg      水印图片
	 * @param x             修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y             修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha         透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public static void pressImage(final File srcImageFile, final File destImageFile, final Image pressImg, final int x, final int y, final float alpha) {
		pressImage(read(srcImageFile), destImageFile, pressImg, x, y, alpha);
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param destStream 目标图像流
	 * @param pressImg   水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x          修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y          修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha      透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public static void pressImage(final InputStream srcStream, final OutputStream destStream, final Image pressImg, final int x, final int y, final float alpha) {
		pressImage(read(srcStream), getImageOutputStream(destStream), pressImg, x, y, alpha);
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage 源图像流
	 * @param outFile  写出文件
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x        修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y        修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha    透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void pressImage(final Image srcImage, final File outFile, final Image pressImg, final int x, final int y, final float alpha) throws IORuntimeException {
		write(pressImage(srcImage, pressImg, x, y, alpha), outFile);
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage 源图像流
	 * @param out      目标图像流
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x        修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y        修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha    透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void pressImage(final Image srcImage, final OutputStream out, final Image pressImg, final int x, final int y, final float alpha) throws IORuntimeException {
		pressImage(srcImage, getImageOutputStream(out), pressImg, x, y, alpha);
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage        源图像流
	 * @param destImageStream 目标图像流
	 * @param pressImg        水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x               修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y               修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha           透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @throws IORuntimeException IO异常
	 */
	public static void pressImage(final Image srcImage, final ImageOutputStream destImageStream, final Image pressImg, final int x, final int y, final float alpha) throws IORuntimeException {
		writeJpg(pressImage(srcImage, pressImg, x, y, alpha), destImageStream);
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage 源图像流
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x        修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y        修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha    透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 结果图片
	 */
	public static Image pressImage(final Image srcImage, final Image pressImg, final int x, final int y, final float alpha) {
		return Img.from(srcImage).pressImage(pressImg, x, y, alpha).getImg();
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage  源图像流
	 * @param pressImg  水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height，x,y从背景图片中心计算
	 * @param alpha     透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 结果图片
	 * @since 4.1.14
	 */
	public static Image pressImage(final Image srcImage, final Image pressImg, final Rectangle rectangle, final float alpha) {
		return Img.from(srcImage).pressImage(pressImg, rectangle, alpha).getImg();
	}

	/**
	 * 给图片添加全屏图片水印<br>
	 *
	 * @param imageFile      源图像文件
	 * @param destFile       目标图像文件
	 * @param pressImageFile 水印图像文件
	 * @param lineHeight     行高
	 * @param degree         水印图像旋转角度，（单位：弧度），以圆点（0,0）为圆心，正代表顺时针，负代表逆时针
	 * @param alpha          透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @throws IORuntimeException IO异常
	 */
	public static void pressImageFull(final File imageFile, final File destFile, final File pressImageFile,
									  final int lineHeight, final int degree, final float alpha) throws IORuntimeException {
		write(pressImageFull(read(imageFile), read(pressImageFile), lineHeight, degree, alpha), destFile);
	}

	/**
	 * 给图片添加全屏图像水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream   源图像流
	 * @param destStream  目标图像流
	 * @param pressStream 水印图像流
	 * @param lineHeight  行高
	 * @param degree      水印图像旋转角度，（单位：弧度），以圆点（0,0）为圆心，正代表顺时针，负代表逆时针
	 * @param alpha       透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @throws IORuntimeException IO异常
	 */
	public static void pressImageFull(final InputStream srcStream, final OutputStream destStream,
									  final InputStream pressStream, final int lineHeight, final int degree, final float alpha) throws IORuntimeException {
		writeJpg(pressImageFull(read(srcStream), read(pressStream), lineHeight, degree, alpha), destStream);
	}

	/**
	 * 给图片添加全屏图像水印<br>
	 * 此方法并不关闭流
	 *
	 * @param srcImage   源图像
	 * @param pressImage 水印图像
	 * @param lineHeight 行高
	 * @param degree     水印图像旋转角度，（单位：弧度），以圆点（0,0）为圆心，正代表顺时针，负代表逆时针，（单位：弧度），以圆点（0,0）为圆心，正代表顺时针，负代表逆时针
	 * @param alpha      透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 处理后的图像
	 */
	public static Image pressImageFull(final Image srcImage, final Image pressImage, final int lineHeight, final int degree, final float alpha) {
		return Img.from(srcImage).pressImageFull(pressImage, lineHeight, degree, alpha).getImg();
	}
	// endregion

	// region ----- rotate

	/**
	 * 旋转图片为指定角度<br>
	 * 此方法不会关闭输出流
	 *
	 * @param imageFile 被旋转图像文件
	 * @param degree    旋转角度
	 * @param outFile   输出文件
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void rotate(final File imageFile, final int degree, final File outFile) throws IORuntimeException {
		rotate(read(imageFile), degree, outFile);
	}

	/**
	 * 旋转图片为指定角度<br>
	 * 此方法不会关闭输出流
	 *
	 * @param image   目标图像
	 * @param degree  旋转角度
	 * @param outFile 输出文件
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void rotate(final Image image, final int degree, final File outFile) throws IORuntimeException {
		write(rotate(image, degree), outFile);
	}

	/**
	 * 旋转图片为指定角度<br>
	 * 此方法不会关闭输出流
	 *
	 * @param image  目标图像
	 * @param degree 旋转角度
	 * @param out    输出流
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void rotate(final Image image, final int degree, final OutputStream out) throws IORuntimeException {
		writeJpg(rotate(image, degree), getImageOutputStream(out));
	}

	/**
	 * 旋转图片为指定角度<br>
	 * 此方法不会关闭输出流，输出格式为JPG
	 *
	 * @param image  目标图像
	 * @param degree 旋转角度
	 * @param out    输出图像流
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void rotate(final Image image, final int degree, final ImageOutputStream out) throws IORuntimeException {
		writeJpg(rotate(image, degree), out);
	}

	/**
	 * 旋转图片为指定角度<br>
	 * 来自：<a href="http://blog.51cto.com/cping1982/130066">http://blog.51cto.com/cping1982/130066</a>
	 *
	 * @param image  目标图像
	 * @param degree 旋转角度
	 * @return 旋转后的图片
	 * @since 3.2.2
	 */
	public static Image rotate(final Image image, final int degree) {
		return Img.from(image).rotate(degree).getImg();
	}
	// endregion

	// region ----- flip

	/**
	 * 水平翻转图像
	 *
	 * @param imageFile 图像文件
	 * @param outFile   输出文件
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void flip(final File imageFile, final File outFile) throws IORuntimeException {
		flip(read(imageFile), outFile);
	}

	/**
	 * 水平翻转图像
	 *
	 * @param image   图像
	 * @param outFile 输出文件
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void flip(final Image image, final File outFile) throws IORuntimeException {
		write(flip(image), outFile);
	}

	/**
	 * 水平翻转图像
	 *
	 * @param image 图像
	 * @param out   输出
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void flip(final Image image, final OutputStream out) throws IORuntimeException {
		flip(image, getImageOutputStream(out));
	}

	/**
	 * 水平翻转图像，写出格式为JPG
	 *
	 * @param image 图像
	 * @param out   输出
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static void flip(final Image image, final ImageOutputStream out) throws IORuntimeException {
		writeJpg(flip(image), out);
	}

	/**
	 * 水平翻转图像
	 *
	 * @param image 图像
	 * @return 翻转后的图片
	 * @since 3.2.2
	 */
	public static Image flip(final Image image) {
		return Img.from(image).flip().getImg();
	}
	// endregion

	// region ----- compress

	/**
	 * 压缩图像，输出图像只支持jpg文件
	 *
	 * @param imageFile 图像文件
	 * @param outFile   输出文件，只支持jpg文件
	 * @param quality   压缩比例，必须为0~1
	 * @throws IORuntimeException IO异常
	 * @since 4.3.2
	 */
	public static void compress(final File imageFile, final File outFile, final float quality) throws IORuntimeException {
		Img.from(imageFile).setQuality(quality).write(outFile);
	}
	// endregion


	// region ------ toImage

	/**
	 * {@link Image} 转 {@link RenderedImage}<br>
	 * 首先尝试强转，否则新建一个{@link BufferedImage}后重新绘制，使用 {@link BufferedImage#TYPE_INT_RGB} 模式。
	 *
	 * @param img       {@link Image}
	 * @param imageType 目标图片类型，例如jpg或png等
	 * @return {@link BufferedImage}
	 * @since 4.3.2
	 */
	public static RenderedImage castToRenderedImage(final Image img, final String imageType) {
		if (img instanceof RenderedImage) {
			return (RenderedImage) img;
		}

		return toBufferedImage(img, imageType);
	}

	/**
	 * {@link Image} 转 {@link BufferedImage}<br>
	 * 首先尝试强转，否则新建一个{@link BufferedImage}后重新绘制，使用 imageType 模式
	 *
	 * @param img       {@link Image}
	 * @param imageType 目标图片类型，例如jpg或png等
	 * @return {@link BufferedImage}
	 */
	public static BufferedImage castToBufferedImage(final Image img, final String imageType) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		return toBufferedImage(img, imageType);
	}

	/**
	 * {@link Image} 转 {@link BufferedImage}<br>
	 * 如果源图片的RGB模式与目标模式一致，则直接转换，否则重新绘制<br>
	 * 默认的，png图片使用 {@link BufferedImage#TYPE_INT_ARGB}模式，其它使用 {@link BufferedImage#TYPE_INT_RGB} 模式
	 *
	 * @param image     {@link Image}
	 * @param imageType 目标图片类型，例如jpg或png等
	 * @return {@link BufferedImage}
	 * @since 4.3.2
	 */
	public static BufferedImage toBufferedImage(final Image image, final String imageType) {
		return toBufferedImage(image, imageType, null);
	}

	/**
	 * {@link Image} 转 {@link BufferedImage}<br>
	 * 如果源图片的RGB模式与目标模式一致，则直接转换，否则重新绘制<br>
	 * 默认的，png图片使用 {@link BufferedImage#TYPE_INT_ARGB}模式，其它使用 {@link BufferedImage#TYPE_INT_RGB} 模式
	 *
	 * @param image           {@link Image}
	 * @param imageType       目标图片类型，例如jpg或png等
	 * @param backgroundColor 背景色{@link Color}，{@code null} 表示默认背景色（黑色或者透明）
	 * @return {@link BufferedImage}
	 * @since 4.3.2
	 */
	public static BufferedImage toBufferedImage(final Image image, final String imageType, final Color backgroundColor) {
		final int type = IMAGE_TYPE_PNG.equalsIgnoreCase(imageType)
			? BufferedImage.TYPE_INT_ARGB
			: BufferedImage.TYPE_INT_RGB;
		return toBufferedImage(image, type, backgroundColor);
	}

	/**
	 * {@link Image} 转 {@link BufferedImage}<br>
	 * 如果源图片的RGB模式与目标模式一致，则直接转换，否则重新绘制
	 *
	 * @param image     {@link Image}
	 * @param imageType 目标图片类型，{@link BufferedImage}中的常量，例如黑白等
	 * @return {@link BufferedImage}
	 * @since 5.4.7
	 */
	public static BufferedImage toBufferedImage(final Image image, final int imageType) {
		return toBufferedImage(image, imageType, null);
	}

	/**
	 * {@link Image} 转 {@link BufferedImage}<br>
	 * 如果源图片的RGB模式与目标模式一致，则直接转换，否则重新绘制
	 *
	 * @param image           {@link Image}
	 * @param imageType       目标图片类型，{@link BufferedImage}中的常量，例如黑白等
	 * @param backgroundColor 背景色{@link Color}，{@code null} 表示默认背景色（黑色或者透明）
	 * @return {@link BufferedImage}
	 * @since 5.4.7
	 */
	public static BufferedImage toBufferedImage(final Image image, final int imageType, final Color backgroundColor) {
		BufferedImage bufferedImage;
		if (image instanceof BufferedImage) {
			bufferedImage = (BufferedImage) image;
			if (imageType != bufferedImage.getType()) {
				bufferedImage = copyImage(image, imageType, backgroundColor);
			}
			return bufferedImage;
		}

		bufferedImage = copyImage(image, imageType, backgroundColor);
		return bufferedImage;
	}
	// endregion

	/**
	 * 将已有Image复制新的一份出来
	 *
	 * @param img       {@link Image}
	 * @param imageType 目标图片类型，{@link BufferedImage}中的常量，例如黑白等
	 * @return {@link BufferedImage}
	 * @see BufferedImage#TYPE_INT_RGB
	 * @see BufferedImage#TYPE_INT_ARGB
	 * @see BufferedImage#TYPE_INT_ARGB_PRE
	 * @see BufferedImage#TYPE_INT_BGR
	 * @see BufferedImage#TYPE_3BYTE_BGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR_PRE
	 * @see BufferedImage#TYPE_BYTE_GRAY
	 * @see BufferedImage#TYPE_USHORT_GRAY
	 * @see BufferedImage#TYPE_BYTE_BINARY
	 * @see BufferedImage#TYPE_BYTE_INDEXED
	 * @see BufferedImage#TYPE_USHORT_565_RGB
	 * @see BufferedImage#TYPE_USHORT_555_RGB
	 */
	public static BufferedImage copyImage(final Image img, final int imageType) {
		return copyImage(img, imageType, null);
	}

	/**
	 * 将已有Image复制新的一份出来
	 *
	 * @param img             {@link Image}
	 * @param imageType       目标图片类型，{@link BufferedImage}中的常量，例如黑白等
	 * @param backgroundColor 背景色，{@code null} 表示默认背景色（黑色或者透明）
	 * @return {@link BufferedImage}
	 * @see BufferedImage#TYPE_INT_RGB
	 * @see BufferedImage#TYPE_INT_ARGB
	 * @see BufferedImage#TYPE_INT_ARGB_PRE
	 * @see BufferedImage#TYPE_INT_BGR
	 * @see BufferedImage#TYPE_3BYTE_BGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR
	 * @see BufferedImage#TYPE_4BYTE_ABGR_PRE
	 * @see BufferedImage#TYPE_BYTE_GRAY
	 * @see BufferedImage#TYPE_USHORT_GRAY
	 * @see BufferedImage#TYPE_BYTE_BINARY
	 * @see BufferedImage#TYPE_BYTE_INDEXED
	 * @see BufferedImage#TYPE_USHORT_565_RGB
	 * @see BufferedImage#TYPE_USHORT_555_RGB
	 * @since 4.5.17
	 */
	public static BufferedImage copyImage(Image img, final int imageType, final Color backgroundColor) {
		// ensures that all the pixels loaded
		// issue#1821@Github
		img = new ImageIcon(img).getImage();

		final BufferedImage bImage = new BufferedImage(
			img.getWidth(null),
			img.getHeight(null),
			imageType);
		final Graphics2D bGr = GraphicsUtil.createGraphics(bImage, backgroundColor);
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bImage;
	}

	/**
	 * 将Base64编码的图像信息转为 {@link BufferedImage}
	 *
	 * @param base64 图像的Base64表示
	 * @return {@link BufferedImage}
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedImage toImage(final String base64) throws IORuntimeException {
		return toImage(Base64.decode(base64));
	}

	/**
	 * 将的图像bytes转为 {@link BufferedImage}
	 *
	 * @param imageBytes 图像bytes
	 * @return {@link BufferedImage}
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedImage toImage(final byte[] imageBytes) throws IORuntimeException {
		return read(new ByteArrayInputStream(imageBytes));
	}

	/**
	 * 将图片对象转换为InputStream形式
	 *
	 * @param image     图片对象
	 * @param imageType 图片类型
	 * @return Base64的字符串表现形式
	 * @since 4.2.4
	 */
	public static ByteArrayInputStream toStream(final Image image, final String imageType) {
		return IoUtil.toStream(toBytes(image, imageType));
	}

	/**
	 * 将图片对象转换为Base64的Data URI形式，格式为：data:image/[imageType];base64,[data]
	 *
	 * @param image     图片对象
	 * @param imageType 图片类型
	 * @return Base64的字符串表现形式
	 * @since 5.3.6
	 */
	public static String toBase64DataUri(final Image image, final String imageType) {
		return UrlUtil.getDataUri(
			"image/" + imageType, "base64",
			toBase64(image, imageType));
	}

	/**
	 * 将图片对象转换为Base64形式
	 *
	 * @param image     图片对象
	 * @param imageType 图片类型
	 * @return Base64的字符串表现形式
	 * @since 4.1.8
	 */
	public static String toBase64(final Image image, final String imageType) {
		return Base64.encode(toBytes(image, imageType));
	}

	/**
	 * 将图片对象转换为bytes形式
	 *
	 * @param image     图片对象
	 * @param imageType 图片类型
	 * @return Base64的字符串表现形式
	 * @since 5.2.4
	 */
	public static byte[] toBytes(final Image image, final String imageType) {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		write(image, imageType, out);
		return out.toByteArrayZeroCopyIfPossible();
	}

	// region ----- createImage

	/**
	 * 创建与当前设备颜色模式兼容的 {@link BufferedImage}
	 *
	 * @param width        宽度
	 * @param height       高度
	 * @param transparency 透明模式，见 {@link java.awt.Transparency}
	 * @return {@link BufferedImage}
	 * @since 5.7.13
	 */
	public static BufferedImage createCompatibleImage(final int width, final int height, final int transparency) {
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice gs = ge.getDefaultScreenDevice();
		final GraphicsConfiguration gc = gs.getDefaultConfiguration();
		return gc.createCompatibleImage(width, height, transparency);
	}

	/**
	 * 根据文字创建透明背景的PNG图片
	 *
	 * @param str       文字
	 * @param font      字体{@link Font}
	 * @param fontColor 字体颜色，默认黑色
	 * @param out       图片输出地
	 * @throws IORuntimeException IO异常
	 */
	public static void createTransparentImage(final String str, final Font font, final Color fontColor, final ImageOutputStream out) throws IORuntimeException {
		writePng(createImage(str, font, null, fontColor, BufferedImage.TYPE_INT_ARGB), out);
	}

	/**
	 * 根据文字创建PNG图片
	 *
	 * @param str             文字
	 * @param font            字体{@link Font}
	 * @param backgroundColor 背景颜色，默认透明
	 * @param fontColor       字体颜色，默认黑色
	 * @param out             图片输出地
	 * @throws IORuntimeException IO异常
	 */
	public static void createImage(final String str, final Font font, final Color backgroundColor, final Color fontColor, final ImageOutputStream out) throws IORuntimeException {
		writePng(createImage(str, font, backgroundColor, fontColor, BufferedImage.TYPE_INT_ARGB), out);
	}

	/**
	 * 根据文字创建图片
	 *
	 * @param str             文字
	 * @param font            字体{@link Font}
	 * @param backgroundColor 背景颜色，默认透明
	 * @param fontColor       字体颜色，默认黑色
	 * @param imageType       图片类型，见：{@link BufferedImage}
	 * @return 图片
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedImage createImage(final String str, final Font font, final Color backgroundColor, final Color fontColor, final int imageType) throws IORuntimeException {
		// 获取font的样式应用在str上的整个矩形
		final Rectangle2D r = getRectangle(str, font);
		// 获取单个字符的高度
		final int unitHeight = (int) Math.floor(r.getHeight());
		// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
		final int width = (int) Math.round(r.getWidth()) + 1;
		// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
		final int height = unitHeight + 3;

		// 创建图片
		final BufferedImage image = new BufferedImage(width, height, imageType);
		final Graphics g = GraphicsUtil.createGraphics(image, backgroundColor);
		GraphicsUtil.drawString(g, str, font, fontColor, new Point(0, font.getSize()));
		g.dispose();

		return image;
	}
	// endregion

	/**
	 * 获取font的样式应用在str上的整个矩形
	 *
	 * @param str  字符串，必须非空
	 * @param font 字体，必须非空
	 * @return {@link Rectangle2D}
	 * @since 5.3.3
	 */
	public static Rectangle2D getRectangle(final String str, final Font font) {
		return font.getStringBounds(str,
			new FontRenderContext(AffineTransform.getScaleInstance(1, 1),
				false,
				false));
	}

	// region ----- write

	/**
	 * 写出图像为JPG格式
	 *
	 * @param image           {@link Image}
	 * @param destImageStream 写出到的目标流
	 * @throws IORuntimeException IO异常
	 */
	public static void writeJpg(final Image image, final ImageOutputStream destImageStream) throws IORuntimeException {
		write(image, IMAGE_TYPE_JPG, destImageStream);
	}

	/**
	 * 写出图像为PNG格式
	 *
	 * @param image           {@link Image}
	 * @param destImageStream 写出到的目标流
	 * @throws IORuntimeException IO异常
	 */
	public static void writePng(final Image image, final ImageOutputStream destImageStream) throws IORuntimeException {
		write(image, IMAGE_TYPE_PNG, destImageStream);
	}

	/**
	 * 写出图像为JPG格式
	 *
	 * @param image {@link Image}
	 * @param out   写出到的目标流
	 * @throws IORuntimeException IO异常
	 * @since 4.0.10
	 */
	public static void writeJpg(final Image image, final OutputStream out) throws IORuntimeException {
		write(image, IMAGE_TYPE_JPG, out);
	}

	/**
	 * 写出图像为PNG格式
	 *
	 * @param image {@link Image}
	 * @param out   写出到的目标流
	 * @throws IORuntimeException IO异常
	 * @since 4.0.10
	 */
	public static void writePng(final Image image, final OutputStream out) throws IORuntimeException {
		write(image, IMAGE_TYPE_PNG, out);
	}

	/**
	 * 按照目标格式写出图像：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG<br>
	 * 此方法并不关闭流
	 *
	 * @param srcStream  源图像流
	 * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destStream 目标图像输出流
	 * @since 5.0.0
	 */
	public static void write(final ImageInputStream srcStream, final String formatName, final ImageOutputStream destStream) {
		write(read(srcStream), formatName, destStream);
	}

	/**
	 * 写出图像：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG<br>
	 * 此方法并不关闭流
	 *
	 * @param image     {@link Image}
	 * @param imageType 图片类型（图片扩展名）
	 * @param out       写出到的目标流
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static void write(final Image image, final String imageType, final OutputStream out) throws IORuntimeException {
		write(image, imageType, getImageOutputStream(out));
	}

	/**
	 * 写出图像为目标文件扩展名对应的格式
	 *
	 * @param image      {@link Image}
	 * @param targetFile 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.0
	 */
	public static void write(final Image image, final File targetFile) throws IORuntimeException {
		final String imageType = FileNameUtil.extName(targetFile);
		ImgWriter.of(image, imageType).write(targetFile);
	}

	/**
	 * 写出图像为指定格式：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG<br>
	 * 此方法并不关闭流
	 *
	 * @param image           {@link Image}
	 * @param imageType       图片类型（图片扩展名）
	 * @param destImageStream 写出到的目标流
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static void write(final Image image, final String imageType, final ImageOutputStream destImageStream) throws IORuntimeException {
		write(image, imageType, destImageStream, 1);
	}

	/**
	 * 写出图像为指定格式
	 *
	 * @param image           {@link Image}
	 * @param imageType       图片类型（图片扩展名），{@code null}表示使用RGB模式（JPG）
	 * @param out             写出到的目标流
	 * @param quality         质量，数字为0~1（不包括0和1）表示质量压缩比，除此数字外设置表示不压缩
	 * @param backgroundColor 背景色{@link Color}
	 * @throws IORuntimeException IO异常
	 * @since 4.3.2
	 */
	public static void write(final Image image, final String imageType, final ImageOutputStream out,
							 final float quality, final Color backgroundColor) throws IORuntimeException {
		final BufferedImage bufferedImage = toBufferedImage(image, imageType, backgroundColor);
		write(bufferedImage, imageType, out, quality);
	}

	/**
	 * 通过{@link ImageWriter}写出图片到输出流
	 *
	 * @param image     图片
	 * @param imageType 图片类型
	 * @param output    输出的Image流{@link ImageOutputStream}
	 * @param quality   质量，数字为0~1（不包括0和1）表示质量压缩比，除此数字外设置表示不压缩
	 * @since 4.3.2
	 */
	public static void write(final Image image, final String imageType, final ImageOutputStream output, final float quality) {
		ImgWriter.of(image, imageType)
			.setQuality(quality)
			.write(output);
	}

	/**
	 * 根据给定的Image对象和格式获取对应的{@link ImageWriter}，如果未找到合适的Writer，返回null
	 *
	 * @param img        {@link Image}
	 * @param formatName 图片格式，例如"jpg"、"png"，{@code null}则使用默认值"jpg"
	 * @return {@link ImageWriter}
	 * @since 4.3.2
	 */
	public static ImageWriter getWriter(final Image img, String formatName) {
		if (null == formatName) {
			formatName = IMAGE_TYPE_JPG;
		}
		final ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(toBufferedImage(img, formatName));
		final Iterator<ImageWriter> iter = ImageIO.getImageWriters(type, formatName);
		return iter.hasNext() ? iter.next() : null;
	}

	/**
	 * 根据给定的图片格式或者扩展名获取{@link ImageWriter}，如果未找到合适的Writer，返回null
	 *
	 * @param formatName 图片格式或扩展名，例如"jpg"、"png"，{@code null}则使用默认值"jpg"
	 * @return {@link ImageWriter}
	 * @since 4.3.2
	 */
	public static ImageWriter getWriter(String formatName) {
		if (null == formatName) {
			formatName = IMAGE_TYPE_JPG;
		}

		ImageWriter writer = null;
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(formatName);
		if (iter.hasNext()) {
			writer = iter.next();
		}
		if (null == writer) {
			// 尝试扩展名获取
			iter = ImageIO.getImageWritersBySuffix(formatName);
			if (iter.hasNext()) {
				writer = iter.next();
			}
		}
		return writer;
	}
	// endregion

	// region ----- read

	/**
	 * 从文件中读取图片，请使用绝对路径，使用相对路径会相对于ClassPath
	 *
	 * @param imageFilePath 图片文件路径
	 * @return 图片
	 * @since 4.1.15
	 */
	public static BufferedImage read(final String imageFilePath) {
		return read(FileUtil.file(imageFilePath));
	}

	/**
	 * 从文件中读取图片
	 *
	 * @param imageFile 图片文件
	 * @return 图片
	 * @since 3.2.2
	 */
	public static BufferedImage read(final File imageFile) {
		final BufferedImage result;
		try {
			result = ImageIO.read(imageFile);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == result) {
			throw new IllegalArgumentException("Image type of file [" + imageFile.getName() + "] is not supported!");
		}

		return result;
	}

	/**
	 * 从{@link Resource}中读取图片
	 *
	 * @param resource 图片资源
	 * @return 图片
	 * @since 4.4.1
	 */
	public static BufferedImage read(final Resource resource) {
		return read(resource.getStream());
	}

	/**
	 * 从流中读取图片
	 *
	 * @param imageStream 图片文件
	 * @return 图片
	 * @since 3.2.2
	 */
	public static BufferedImage read(final InputStream imageStream) {
		final BufferedImage result;
		try {
			result = ImageIO.read(imageStream);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == result) {
			throw new IllegalArgumentException("Image type is not supported!");
		}

		return result;
	}

	/**
	 * 从图片流中读取图片
	 *
	 * @param imageStream 图片文件
	 * @return 图片
	 * @since 3.2.2
	 */
	public static BufferedImage read(final ImageInputStream imageStream) {
		final BufferedImage result;
		try {
			result = ImageIO.read(imageStream);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == result) {
			throw new IllegalArgumentException("Image type is not supported!");
		}

		return result;
	}

	/**
	 * 从URL中读取图片
	 *
	 * @param imageUrl 图片文件
	 * @return 图片
	 * @since 3.2.2
	 */
	public static BufferedImage read(final URL imageUrl) {
		final BufferedImage result;
		try {
			result = ImageIO.read(imageUrl);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == result) {
			throw new IllegalArgumentException("Image type of [" + imageUrl + "] is not supported!");
		}

		return result;
	}

	/**
	 * 获得{@link ImageReader}
	 *
	 * @param type 图片文件类型，例如 "jpeg" 或 "tiff"
	 * @return {@link ImageReader}
	 */
	public static ImageReader getReader(final String type) {
		final Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(type);
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}
	// endregion

	// region ----- getImage and getPoint

	/**
	 * 从URL中获取或读取图片对象
	 *
	 * @param url URL
	 * @return {@link Image}
	 * @since 5.5.8
	 */
	public static Image getImage(final URL url) {
		return Toolkit.getDefaultToolkit().getImage(url);
	}

	/**
	 * 获取{@link ImageOutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @return {@link ImageOutputStream}
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static ImageOutputStream getImageOutputStream(final OutputStream out) throws IORuntimeException {
		final ImageOutputStream result;
		try {
			result = ImageIO.createImageOutputStream(out);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == result) {
			throw new IllegalArgumentException("Image type is not supported!");
		}

		return result;
	}

	/**
	 * 获取{@link ImageOutputStream}
	 *
	 * @param outFile {@link File}
	 * @return {@link ImageOutputStream}
	 * @throws IORuntimeException IO异常
	 * @since 3.2.2
	 */
	public static ImageOutputStream getImageOutputStream(final File outFile) throws IORuntimeException {
		final ImageOutputStream result;
		try {
			result = ImageIO.createImageOutputStream(outFile);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == result) {
			throw new IllegalArgumentException("Image type of file [" + outFile.getName() + "] is not supported!");
		}

		return result;
	}

	/**
	 * 获取{@link ImageInputStream}
	 *
	 * @param in {@link InputStream}
	 * @return {@link ImageInputStream}
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static ImageInputStream getImageInputStream(final InputStream in) throws IORuntimeException {
		final ImageOutputStream result;
		try {
			result = ImageIO.createImageOutputStream(in);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null == result) {
			throw new IllegalArgumentException("Image type is not supported!");
		}

		return result;
	}

	/**
	 * 获得修正后的矩形坐标位置，变为以背景中心为基准坐标（即x,y == 0,0时，处于背景正中）
	 *
	 * @param rectangle        矩形
	 * @param backgroundWidth  参考宽（背景宽）
	 * @param backgroundHeight 参考高（背景高）
	 * @return 修正后的{@link Point}
	 * @since 5.3.6
	 */
	public static Point getPointBaseCentre(final Rectangle rectangle, final int backgroundWidth, final int backgroundHeight) {
		return new Point(
			rectangle.x + (Math.abs(backgroundWidth - rectangle.width) / 2), //
			rectangle.y + (Math.abs(backgroundHeight - rectangle.height) / 2)//
		);
	}
	// endregion

	// region ----- backgroundRemoval

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param inputPath  要处理图片的路径
	 * @param outputPath 输出图片的路径
	 * @param tolerance  容差值[根据图片的主题色,加入容差值,值的范围在0~255之间]
	 * @return 返回处理结果 true:图片处理完成 false:图片处理失败
	 */
	public static boolean backgroundRemoval(final String inputPath, final String outputPath, final int tolerance) {
		return BackgroundRemoval.backgroundRemoval(inputPath, outputPath, tolerance);
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param input     需要进行操作的图片
	 * @param output    最后输出的文件
	 * @param tolerance 容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理结果 true:图片处理完成 false:图片处理失败
	 */
	public static boolean backgroundRemoval(final File input, final File output, final int tolerance) {
		return BackgroundRemoval.backgroundRemoval(input, output, tolerance);
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param input     需要进行操作的图片
	 * @param output    最后输出的文件
	 * @param override  指定替换成的背景颜色 为null时背景为透明
	 * @param tolerance 容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理结果 true:图片处理完成 false:图片处理失败
	 */
	public static boolean backgroundRemoval(final File input, final File output, final Color override, final int tolerance) {
		return BackgroundRemoval.backgroundRemoval(input, output, override, tolerance);
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param bufferedImage 需要进行处理的图片流
	 * @param override      指定替换成的背景颜色 为null时背景为透明
	 * @param tolerance     容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理好的图片流
	 */
	public static BufferedImage backgroundRemoval(final BufferedImage bufferedImage, final Color override, final int tolerance) {
		return BackgroundRemoval.backgroundRemoval(bufferedImage, override, tolerance);
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param outputStream 需要进行处理的图片字节数组流
	 * @param override     指定替换成的背景颜色 为null时背景为透明
	 * @param tolerance    容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理好的图片流
	 */
	public static BufferedImage backgroundRemoval(final ByteArrayOutputStream outputStream, final Color override, final int tolerance) {
		return BackgroundRemoval.backgroundRemoval(outputStream, override, tolerance);
	}
	// endregion

	// region ------ transform and filter

	/**
	 * 图片颜色转换<br>
	 * 可以使用灰度 (gray)等
	 *
	 * @param colorSpace 颜色模式，如灰度等
	 * @param image      被转换的图片
	 * @return 转换后的图片
	 * @since 5.7.8
	 */
	public static BufferedImage colorConvert(final ColorSpace colorSpace, final BufferedImage image) {
		return filter(new ColorConvertOp(colorSpace, null), image);
	}

	/**
	 * 转换图片<br>
	 * 可以使用一系列平移 (translation)、缩放 (scale)、翻转 (flip)、旋转 (rotation) 和错切 (shear) 来构造仿射变换。
	 *
	 * @param xform 2D仿射变换，它执行从 2D 坐标到其他 2D 坐标的线性映射，保留了线的“直线性”和“平行性”。
	 * @param image 被转换的图片
	 * @return 转换后的图片
	 * @since 5.7.8
	 */
	public static BufferedImage transform(final AffineTransform xform, final BufferedImage image) {
		return filter(new AffineTransformOp(xform, null), image);
	}

	/**
	 * 图片过滤转换
	 *
	 * @param op    过滤操作实现，如二维转换可传入{@link AffineTransformOp}
	 * @param image 原始图片
	 * @return 过滤后的图片
	 * @since 5.7.8
	 */
	public static BufferedImage filter(final BufferedImageOp op, final BufferedImage image) {
		return op.filter(image, null);
	}

	/**
	 * 图片滤镜，借助 {@link ImageFilter}实现，实现不同的图片滤镜
	 *
	 * @param filter 滤镜实现
	 * @param image  图片
	 * @return 滤镜后的图片
	 * @since 5.7.8
	 */
	public static Image filter(final ImageFilter filter, final Image image) {
		return Toolkit.getDefaultToolkit().createImage(
			new FilteredImageSource(image.getSource(), filter));
	}
	// endregion
}
