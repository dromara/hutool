package com.xiaoleilu.hutool.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.lang.Base64;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等 <br>
 * 参考：http://blog.csdn.net/zhangzhikaixinya/article/details/8459400
 * 
 * @author Looly
 */
public class ImageUtil {

	public static final String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public static final String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public static final String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public static final String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public static final String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public static final String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

	private ImageUtil() {
	}

	// ---------------------------------------------------------------------------------------------------------------------- scale
	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcImageFile 源图像文件
	 * @param destImageFile 缩放后的图像文件
	 * @param scale 缩放比例
	 * @param flag 缩放选择:true 放大; false 缩小;
	 */
	public final static void scale(File srcImageFile, File destImageFile, int scale, boolean flag) {
		try {
			scale(ImageIO.read(srcImageFile), ImageIO.createImageOutputStream(destImageFile), scale, flag);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcStream 源图像来源流
	 * @param destStream 缩放后的图像写出到的流
	 * @param scale 缩放比例
	 * @param flag 缩放选择:true 放大; false 缩小;
	 * @since 3.0.9
	 */
	public final static void scale(InputStream srcStream, OutputStream destStream, int scale, boolean flag) {
		try {
			scale(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), scale, flag);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcStream 源图像来源流
	 * @param destStream 缩放后的图像写出到的流
	 * @param scale 缩放比例
	 * @param flag 缩放选择:true 放大; false 缩小;
	 * @since 3.0.9
	 */
	public final static void scale(ImageInputStream srcStream, ImageOutputStream destStream, int scale, boolean flag) {
		try {
			scale(ImageIO.read(srcStream), destStream, scale, flag);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 缩放图像（按比例缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcImg 源图像来源流
	 * @param destImageStream 缩放后的图像写出到的流
	 * @param scale 缩放比例
	 * @param flag 缩放选择:true 放大; false 缩小;
	 * @since 3.0.9
	 */
	public final static void scale(Image srcImg, ImageOutputStream destImageStream, int scale, boolean flag) {
		try {
			BufferedImage src = toBufferedImage(srcImg);
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {// 放大
				width = width * scale;
				height = height * scale;
			} else {// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			ImageIO.write(toBufferedImage(image), IMAGE_TYPE_JPEG, destImageStream);// 输出到文件流
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcImageFile 源图像文件地址
	 * @param destImageFile 缩放后的图像地址
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(File srcImageFile, File destImageFile, int height, int width, Color fixedColor) {
		try {
			scale(ImageIO.read(srcImageFile), ImageIO.createImageOutputStream(destImageFile), height, width, fixedColor);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 缩放后的图像目标流
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(InputStream srcStream, OutputStream destStream, int height, int width, Color fixedColor) {
		try {
			scale(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), height, width, fixedColor);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 缩放后的图像目标流
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(ImageInputStream srcStream, ImageOutputStream destStream, int height, int width, Color fixedColor) {
		try {
			scale(ImageIO.read(srcStream), destStream, height, width, fixedColor);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcImage 源图像
	 * @param destImageStream 缩放后的图像目标流
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(Image srcImage, ImageOutputStream destImageStream, int height, int width, Color fixedColor) {
		try {
			double ratio = 0.0; // 缩放比例
			BufferedImage bi = toBufferedImage(srcImage);
			Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = ((double) height) / bi.getHeight();
				} else {
					ratio = ((double) width) / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (null != fixedColor) {// 补白
				final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(fixedColor);
				g.fillRect(0, 0, width, height);

				final int itempHeight = itemp.getHeight(null);
				final int itempWidth = itemp.getWidth(null);
				if (width == itempWidth) {
					// 宽度一致
					g.drawImage(itemp, 0, (height - itempHeight) / 2, itempWidth, itempHeight, fixedColor, null);
				} else {
					g.drawImage(itemp, (width - itempWidth) / 2, 0, itempWidth, itempHeight, fixedColor, null);
				}
				g.dispose();
				itemp = image;
			}
			ImageIO.write(toBufferedImage(itemp), IMAGE_TYPE_JPEG, destImageStream);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------- cut
	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImgFile 源图像文件
	 * @param destImgFile 切片后的图像文件
	 * @param x 目标切片起点坐标X
	 * @param y 目标切片起点坐标Y
	 * @param width 目标切片宽度
	 * @param height 目标切片高度
	 */
	public final static void cut(File srcImgFile, File destImgFile, int x, int y, int width, int height) {
		try {
			cut(ImageIO.read(srcImgFile), ImageIO.createImageOutputStream(destImgFile), x, y, width, height);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 切片后的图像输出流
	 * @param x 目标切片起点坐标X
	 * @param y 目标切片起点坐标Y
	 * @param width 目标切片宽度
	 * @param height 目标切片高度
	 * @since 3.0.9
	 */
	public final static void cut(InputStream srcStream, OutputStream destStream, int x, int y, int width, int height) {
		try {
			cut(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), x, y, width, height);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 切片后的图像输出流
	 * @param x 目标切片起点坐标X
	 * @param y 目标切片起点坐标Y
	 * @param width 目标切片宽度
	 * @param height 目标切片高度
	 * @since 3.0.9
	 */
	public final static void cut(ImageInputStream srcStream, ImageOutputStream destStream, int x, int y, int width, int height) {
		try {
			cut(ImageIO.read(srcStream), destStream, x, y, width, height);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImage 源图像
	 * @param destImageStream 切片后的图像输出流
	 * @param x 目标切片起点坐标X
	 * @param y 目标切片起点坐标Y
	 * @param width 目标切片宽度
	 * @param height 目标切片高度
	 * @since 3.0.9
	 */
	public final static void cut(Image srcImage, ImageOutputStream destImageStream, int x, int y, int width, int height) {
		try {
			// 读取源图像
			BufferedImage bi = toBufferedImage(srcImage);
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
				Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, IMAGE_TYPE_JPEG, destImageStream);
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切片（指定切片的宽度和高度）
	 * 
	 * @param srcImageFile 源图像
	 * @param descDir 切片目标文件夹
	 * @param destWidth 目标切片宽度。默认200
	 * @param destHeight 目标切片高度。默认150
	 */
	public final static void slice(File srcImageFile, File descDir, int destWidth, int destHeight) {
		try {
			slice(ImageIO.read(srcImageFile), descDir, destWidth, destHeight);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切片（指定切片的宽度和高度）
	 * 
	 * @param srcImage 源图像
	 * @param descDir 切片目标文件夹
	 * @param destWidth 目标切片宽度。默认200
	 * @param destHeight 目标切片高度。默认150
	 */
	public final static void slice(Image srcImage, File descDir, int destWidth, int destHeight) {
		try {
			if (destWidth <= 0) destWidth = 200; // 切片宽度
			if (destHeight <= 0) destHeight = 150; // 切片高度
			// 读取源图像
			BufferedImage bi = toBufferedImage(srcImage);
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int cols = 0; // 切片横向数量
				int rows = 0; // 切片纵向数量
				// 计算切片的横向和纵向数量
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descDir, "_r" + i + "_c" + j + ".jpg"));
					}
				}
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 图像切割（指定切片的行数和列数）
	 * 
	 * @param srcImageFile 源图像文件
	 * @param descDir 切片目标文件夹
	 * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
	 */
	public final static void sliceByRowsAndCols(File srcImageFile, File descDir, int rows, int cols) {
		try {
			sliceByRowsAndCols(ImageIO.read(srcImageFile), descDir, rows, cols);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切割（指定切片的行数和列数）
	 * 
	 * @param srcImage 源图像
	 * @param descDir 切片目标文件夹
	 * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
	 */
	public final static void sliceByRowsAndCols(Image srcImage, File descDir, int rows, int cols) {
		try {
			if (rows <= 0 || rows > 20) rows = 2; // 切片行数
			if (cols <= 0 || cols > 20) cols = 2; // 切片列数
			// 读取源图像
			BufferedImage bi = toBufferedImage(srcImage);
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int destWidth = srcWidth; // 每张切片的宽度
				int destHeight = srcHeight; // 每张切片的高度
				// 计算切片的宽度和高度
				if (srcWidth % cols == 0) {
					destWidth = srcWidth / cols;
				} else {
					destWidth = (int) Math.floor(srcWidth / cols) + 1;
				}
				if (srcHeight % rows == 0) {
					destHeight = srcHeight / rows;
				} else {
					destHeight = (int) Math.floor(srcWidth / rows) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, IMAGE_TYPE_JPEG, new File(descDir, "_r" + i + "_c" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------- convert
	/**
	 * 图像类型转换：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG
	 * 
	 * @param srcImageFile 源图像文件
	 * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageFile 目标图像文件
	 */
	public final static void convert(File srcImageFile, String formatName, File destImageFile) {
		try {
			convert(ImageIO.read(srcImageFile), formatName, ImageIO.createImageOutputStream(destImageFile));
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像类型转换：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG
	 * 
	 * @param srcStream 源图像流
	 * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destStream 目标图像输出流
	 * @since 3.0.9
	 */
	public final static void convert(InputStream srcStream, String formatName, OutputStream destStream) {
		try {
			convert(ImageIO.read(srcStream), formatName, ImageIO.createImageOutputStream(destStream));
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像类型转换：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG
	 * 
	 * @param srcStream 源图像流
	 * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destStream 目标图像输出流
	 * @since 3.0.9
	 */
	public final static void convert(ImageInputStream srcStream, String formatName, ImageOutputStream destStream) {
		try {
			convert(ImageIO.read(srcStream), formatName, destStream);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像类型转换：GIF=》JPG、GIF=》PNG、PNG=》JPG、PNG=》GIF(X)、BMP=》PNG
	 * 
	 * @param srcImage 源图像流
	 * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageStream 目标图像输出流
	 * @since 3.0.9
	 */
	public final static void convert(Image srcImage, String formatName, ImageOutputStream destImageStream) {
		try {
			ImageIO.write(toBufferedImage(srcImage), formatName, destImageStream);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------- grey
	/**
	 * 彩色转为黑白
	 * 
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 */
	public final static void gray(File srcImageFile, File destImageFile) {
		try {
			gray(ImageIO.read(srcImageFile), ImageIO.createImageOutputStream(destImageFile));
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 目标图像流
	 * @since 3.0.9
	 */
	public final static void gray(InputStream srcStream, OutputStream destStream) {
		try {
			gray(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream));
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 目标图像流
	 * @since 3.0.9
	 */
	public final static void gray(ImageInputStream srcStream, ImageOutputStream destStream) {
		try {
			gray(ImageIO.read(srcStream), destStream);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param srcImage 源图像流
	 * @param destImageStream 目标图像流
	 * @since 3.0.9
	 */
	public final static void gray(Image srcImage, ImageOutputStream destImageStream) {
		try {
			BufferedImage src = toBufferedImage(srcImage);
			ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
			src = op.filter(src, null);
			ImageIO.write(src, IMAGE_TYPE_JPEG, destImageStream);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------- press
	/**
	 * 给图片添加文字水印
	 * 
	 * @param srcFile 源图像文件
	 * @param destFile 目标图像文件
	 * @param pressText 水印文字
	 * @param color 水印的字体颜色
	 * @param font {@link Font} 字体相关信息，如果默认则为{@code null}
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText(File srcFile, File destFile, String pressText, Color color, Font font, int x, int y, float alpha) {
		try {
			pressText(ImageIO.read(srcFile), ImageIO.createImageOutputStream(destFile), pressText, color, font, x, y, alpha);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 给图片添加文字水印
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 目标图像流
	 * @param pressText 水印文字
	 * @param color 水印的字体颜色
	 * @param font {@link Font} 字体相关信息，如果默认则为{@code null}
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText(InputStream srcStream, OutputStream destStream, String pressText, Color color, Font font, int x, int y, float alpha) {
		try {
			pressText(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), pressText, color, font, x, y, alpha);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 给图片添加文字水印
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 目标图像流
	 * @param pressText 水印文字
	 * @param color 水印的字体颜色
	 * @param font {@link Font} 字体相关信息，如果默认则为{@code null}
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText(ImageInputStream srcStream, ImageOutputStream destStream, String pressText, Color color, Font font, int x, int y, float alpha) {
		try {
			pressText(ImageIO.read(srcStream), destStream, pressText, color, font, x, y, alpha);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 给图片添加文字水印
	 * 
	 * @param srcImage 源图像
	 * @param destImageStream 目标图像流
	 * @param pressText 水印文字
	 * @param color 水印的字体颜色
	 * @param font {@link Font} 字体相关信息，如果默认则为{@code null}
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText(Image srcImage, ImageOutputStream destImageStream, String pressText, Color color, Font font, int x, int y, float alpha) {
		try {
			Image src = toBufferedImage(srcImage);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(font);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// 在指定坐标绘制水印文字
			final int fontSize = font.getSize();
			g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			ImageIO.write((BufferedImage) image, IMAGE_TYPE_JPEG, destImageStream);// 输出到文件流
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 给图片添加图片水印
	 * 
	 * @param srcImageFile 源图像文件
	 * @param destImageFile 目标图像文件
	 * @param pressImg 水印图片
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(File srcImageFile, File destImageFile, Image pressImg, int x, int y, float alpha) {
		try {
			pressImage(ImageIO.read(srcImageFile), ImageIO.createImageOutputStream(destImageFile), pressImg, x, y, alpha);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 给图片添加图片水印
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 目标图像流
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(InputStream srcStream, OutputStream destStream, Image pressImg, int x, int y, float alpha) {
		try {
			pressImage(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), pressImg, x, y, alpha);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 给图片添加图片水印
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 目标图像流
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(ImageInputStream srcStream, ImageOutputStream destStream, Image pressImg, int x, int y, float alpha) {
		try {
			pressImage(ImageIO.read(srcStream), destStream, pressImg, x, y, alpha);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 给图片添加图片水印
	 * 
	 * @param srcImage 源图像流
	 * @param destImageStream 目标图像流
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(Image srcImage, ImageOutputStream destImageStream, Image pressImg, int x, int y, float alpha) {
		try {
			Image src = toBufferedImage(srcImage);
			final int width = src.getWidth(null);
			final int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);

			// 水印文件
			int pressImgWidth = pressImg.getWidth(null);
			int pressImgHeight = pressImg.getHeight(null);
			x += (width - pressImgWidth) / 2;
			y += (height - pressImgHeight) / 2;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawImage(pressImg, x, y, pressImgWidth, pressImgHeight, null);

			g.dispose();
			ImageIO.write(image, IMAGE_TYPE_JPEG, destImageStream);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------- other
	/**
	 * {@link Image} 转 {@link BufferedImage}<br>
	 * 首先尝试强转，否则新建一个{@link BufferedImage}后重新绘制
	 * 
	 * @param img {@link Image}
	 * @return {@link BufferedImage}
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		final BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}

	/**
	 * 将Base64编码的图像信息转为 {@link BufferedImage}
	 * 
	 * @param base64 图像的Base64表示
	 * @return {@link BufferedImage}
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedImage toImage(String base64) throws IORuntimeException {
		byte[] decode = Base64.decode(base64, CharsetUtil.CHARSET_UTF_8);
		return toImage(decode);
	}

	/**
	 * 将Base64编码的图像信息转为 {@link BufferedImage}
	 * 
	 * @param imageBytes 图像bytes
	 * @return {@link BufferedImage}
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedImage toImage(byte[] imageBytes) throws IORuntimeException {
		try {
			return ImageIO.read(new ByteArrayInputStream(imageBytes));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 根据文字创建PNG图片
	 * 
	 * @param str 文字
	 * @param font 字体{@link Font}
	 * @param backgroundColor 背景颜色
	 * @param fontColor 字体颜色
	 * @param out 图片输出地
	 * @throws UtilException IO异常 
	 */
	public static void createImage(String str, Font font, Color backgroundColor, Color fontColor, ImageOutputStream out) throws UtilException{
		// 获取font的样式应用在str上的整个矩形
		Rectangle2D r = font.getStringBounds(str, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
		int unitHeight = (int) Math.floor(r.getHeight());// 获取单个字符的高度
		// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
		int width = (int) Math.round(r.getWidth()) + 1;
		int height = unitHeight + 3;// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
		// 创建图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);// 先用背景色填充整张图片,也就是背景
		g.setColor(fontColor);
		g.setFont(font);// 设置画笔字体
		g.drawString(str, 0, font.getSize());// 画出字符串
		g.dispose();
		try {
			ImageIO.write(image, IMAGE_TYPE_PNG, out);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 根据文件创建字体<br>
	 * 首先尝试创建{@link Font#TRUETYPE_FONT}字体，此类字体无效则创建{@link Font#TYPE1_FONT}
	 * 
	 * @param fontFile 字体文件
	 * @return {@link Font}
	 * @since 3.0.9
	 */
	public static Font createFont(File fontFile) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, fontFile);
		} catch (FontFormatException e) {
			// True Type字体无效时使用Type1字体
			try {
				return Font.createFont(Font.TYPE1_FONT, fontFile);
			} catch (Exception e1) {
				throw new UtilException(e);
			}
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 根据文件创建字体<br>
	 * 首先尝试创建{@link Font#TRUETYPE_FONT}字体，此类字体无效则创建{@link Font#TYPE1_FONT}
	 * 
	 * @param fontStream 字体流
	 * @return {@link Font}
	 * @since 3.0.9
	 */
	public static Font createFont(InputStream fontStream) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (FontFormatException e) {
			// True Type字体无效时使用Type1字体
			try {
				return Font.createFont(Font.TYPE1_FONT, fontStream);
			} catch (Exception e1) {
				throw new UtilException(e);
			}
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 计算text的长度（一个中文算两个字符）
	 * 
	 * @param text 文本
	 * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
	 */
	private final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}
	// ---------------------------------------------------------------------------------------------------------------- Private method end
}
