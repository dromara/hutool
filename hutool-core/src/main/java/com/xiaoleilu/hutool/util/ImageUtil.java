package com.xiaoleilu.hutool.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
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
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.FileUtil;
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
	 * @param scale 缩放比例。比例大于1时为放大，小于1大于0为缩小
	 */
	public static void scale(File srcImageFile, File destImageFile, float scale) {
		try {
			scale(ImageIO.read(srcImageFile), ImageIO.createImageOutputStream(destImageFile), scale);
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
	 * @param isZoomIn 缩放选择:true 放大; false 缩小;
	 * @since 3.0.9
	 */
	public static void scale(InputStream srcStream, OutputStream destStream, float scale) {
		try {
			scale(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), scale);
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
	 * @param scale 缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @since 3.1.0
	 */
	public static void scale(ImageInputStream srcStream, ImageOutputStream destStream, float scale) {
		try {
			scale(ImageIO.read(srcStream), destStream, scale);
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
	 * @param scale 缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @since 3.1.0
	 */
	public static void scale(Image srcImg, ImageOutputStream destImageStream, float scale) {
		final Image image = scale(srcImg, scale);
		writeJpg(image, destImageStream);
	}
	
	/**
	 * 缩放图像（按比例缩放）
	 * 
	 * @param srcImg 源图像来源流
	 * @param scale 缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @since 3.1.0
	 */
	public static Image scale(Image srcImg, double scale) {
		if(scale < 0) {
			//自动修正负数
			scale = - scale;
		}
		
		int width = NumberUtil.mul(Integer.toString(srcImg.getWidth(null)), Double.toString(scale)).intValue(); // 得到源图宽
		int height = NumberUtil.mul(Integer.toString(srcImg.getHeight(null)), Double.toString(scale)).intValue(); // 得到源图长
		return scale(srcImg, width, height);
	}
	
	/**
	 * 缩放图像（按长宽缩放）<br>
	 * 注意：目标长宽与原图不成比例会变形
	 * 
	 * @param srcImg 源图像来源流
	 * @param width 目标宽度
	 * @param height 目标高度
	 * @since 3.1.0
	 */
	public static Image scale(Image srcImg, int width, int height) {
		int srcHeight = srcImg.getHeight(null);
		int srcWidth = srcImg.getWidth(null);
		int scaleType;
		if(srcHeight == height && srcWidth == width) {
			//源与目标长宽一致返回原图
			return srcImg;
		}else if(srcHeight < height || srcWidth < width) {
			//放大图片使用平滑模式
			scaleType = Image.SCALE_SMOOTH;
		}else {
			scaleType = Image.SCALE_DEFAULT;
		}
		return srcImg.getScaledInstance(width, height, scaleType);
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcImageFile 源图像文件地址
	 * @param destImageFile 缩放后的图像地址
	 * @param width 缩放后的宽度
	 * @param height 缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(File srcImageFile, File destImageFile, int width, int height, Color fixedColor) {
		try {
			scale(ImageIO.read(srcImageFile), ImageIO.createImageOutputStream(destImageFile), width, height, fixedColor);
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
	 * @param width 缩放后的宽度
	 * @param height 缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(InputStream srcStream, OutputStream destStream, int width, int height, Color fixedColor) {
		try {
			scale(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), width, height, fixedColor);
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
	 * @param width 缩放后的宽度
	 * @param height 缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(ImageInputStream srcStream, ImageOutputStream destStream, int width, int height, Color fixedColor) {
		try {
			scale(ImageIO.read(srcStream), destStream, width, height, fixedColor);
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
	 * @param width 缩放后的宽度
	 * @param height 缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 */
	public final static void scale(Image srcImage, ImageOutputStream destImageStream, int width, int height, Color fixedColor) {
		final Image image = scale(srcImage, width, height, fixedColor);
		writeJpg(image, destImageStream);
	}
	
	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param srcImage 源图像
	 * @param width 缩放后的宽度
	 * @param height 缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 * @return {@link Image}
	 */
	public final static Image scale(Image srcImage, int width, int height, Color fixedColor) {
		int srcHeight = srcImage.getHeight(null);
		int srcWidth = srcImage.getWidth(null);
		double heightRatio = NumberUtil.div(height, srcHeight);
		double widthRatio = NumberUtil.div(width, srcWidth);
		if(heightRatio == widthRatio) {
			//长宽都按照相同比例缩放时，返回缩放后的图片
			return scale(srcImage, width, height);
		}
		
		Image itemp = null;
		//宽缩放比例小就按照宽缩放，否则按照高缩放
		if(widthRatio < height) {
			itemp = scale(srcImage, width, (int)(srcHeight * widthRatio));
		}else {
			itemp = scale(srcImage, (int)(srcWidth * heightRatio), height);
		}
			
		if (null == fixedColor) {// 补白
			fixedColor = Color.WHITE;
		}
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		//设置背景
		g.setBackground(fixedColor);
		g.clearRect(0, 0, width, height);
		
		final int itempHeight = itemp.getHeight(null);
		final int itempWidth = itemp.getWidth(null);
		//在中间贴图
		g.drawImage(itemp, (width - itempWidth) / 2, (height - itempHeight) / 2, itempWidth, itempHeight, fixedColor, null);
		
		g.dispose();
		itemp = image;
		return itemp;
	}

	// ---------------------------------------------------------------------------------------------------------------------- cut
	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImgFile 源图像文件
	 * @param destImgFile 切片后的图像文件
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public final static void cut(File srcImgFile, File destImgFile, Rectangle rectangle) {
		try {
			cut(ImageIO.read(srcImgFile), ImageIO.createImageOutputStream(destImgFile), rectangle);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 切片后的图像输出流
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public final static void cut(InputStream srcStream, OutputStream destStream, Rectangle rectangle) {
		try {
			cut(ImageIO.read(srcStream), ImageIO.createImageOutputStream(destStream), rectangle);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcStream 源图像流
	 * @param destStream 切片后的图像输出流
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public final static void cut(ImageInputStream srcStream, ImageOutputStream destStream, Rectangle rectangle) {
		try {
			cut(ImageIO.read(srcStream), destStream, rectangle);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImage 源图像
	 * @param destImageStream 切片后的图像输出流
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public final static void cut(Image srcImage, ImageOutputStream destImageStream, Rectangle rectangle) {
		final BufferedImage tag = cut(srcImage, rectangle);
		writeJpg(tag, destImageStream);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImage 源图像
	 * @param destImageStream 切片后的图像输出流
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @since 3.1.0
	 */
	public static BufferedImage cut(Image srcImage, Rectangle rectangle) {
		ImageFilter cropFilter = new CropImageFilter(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(srcImage.getSource(), cropFilter));
		
		final BufferedImage result = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_RGB);
		draw(result, img, new Rectangle(0, 0, rectangle.width, rectangle.height));
		return result;
	}
	
	/**
	 * 图像切片（指定切片的宽度和高度）
	 * 
	 * @param srcImageFile 源图像
	 * @param descDir 切片目标文件夹
	 * @param destWidth 目标切片宽度。默认200
	 * @param destHeight 目标切片高度。默认150
	 */
	public static void slice(File srcImageFile, File descDir, int destWidth, int destHeight) {
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
		if (destWidth <= 0) {
			destWidth = 200; // 切片宽度
		}
		if (destHeight <= 0) {
			destHeight = 150; // 切片高度
		}
		int srcWidth = srcImage.getHeight(null); // 源图宽度
		int srcHeight = srcImage.getWidth(null); // 源图高度
		
		try {
			if (srcWidth > destWidth && srcHeight > destHeight) {
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
				BufferedImage tag;
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						tag = cut(srcImage,new Rectangle(j * destWidth, i * destHeight, destWidth, destHeight));
						// 输出为文件
						ImageIO.write(tag, IMAGE_TYPE_JPEG, new File(descDir, "_r" + i + "_c" + j + ".jpg"));
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
				BufferedImage tag;
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						tag = cut(srcImage, new Rectangle(j * destWidth, i * destHeight, destWidth, destHeight));
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
		final BufferedImage src = gray(srcImage);
		try {
			ImageIO.write(src, IMAGE_TYPE_JPEG, destImageStream);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 彩色转为黑白
	 * 
	 * @param srcImage 源图像流
	 * @return {@link Image}灰度后的图片
	 * @since 3.1.0
	 */
	public static BufferedImage gray(Image srcImage) {
		BufferedImage grayImage = toBufferedImage(srcImage);
		final ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		grayImage = op.filter(grayImage, null);
		return grayImage;
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
		int width = srcImage.getWidth(null);
		int height = srcImage.getHeight(null);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		//绘制背景
		g.drawImage(srcImage, 0, 0, width, height, null);
		
		g.setColor(color);
		g.setFont(font);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 在指定坐标绘制水印文字
		final int fontSize = font.getSize();
		g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
		g.dispose();
		
		try {
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
		final int width = srcImage.getWidth(null);
		final int height = srcImage.getHeight(null);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		//绘制背景
		g.drawImage(srcImage, 0, 0, width, height, null);
		
		// 水印文件
		int pressImgWidth = pressImg.getWidth(null);
		int pressImgHeight = pressImg.getHeight(null);
		x += (width - pressImgWidth) / 2;
		y += (height - pressImgHeight) / 2;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		g.drawImage(pressImg, x, y, pressImgWidth, pressImgHeight, null);
		
		g.dispose();
		
		try {
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

		return copyImage(img, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * 将已有Image复制新的一份出来
	 * @param img {@link Image}
	 * @param imageType {@link BufferedImage}中的常量
	 * @return {@link BufferedImage}
	 */
	public static BufferedImage copyImage(Image img, int imageType) {
		final BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), imageType);
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
	public static void createImage(String str, Font font, Color backgroundColor, Color fontColor, ImageOutputStream out) throws UtilException {
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
		writePng(image, out);
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
	
	/**
	 * 写出图像为JPG格式
	 * @param image {@link Image}
	 * @param destImageStream 写出到的目标流
	 * @throws IORuntimeException IO异常
	 */
	public static void writeJpg(Image image, ImageOutputStream destImageStream) throws IORuntimeException{
		try {
			ImageIO.write(toBufferedImage(image), IMAGE_TYPE_JPEG, destImageStream);// 输出到文件流
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 写出图像为PNG格式
	 * @param image {@link Image}
	 * @param destImageStream 写出到的目标流
	 * @throws IORuntimeException IO异常
	 */
	public static void writePng(Image image, ImageOutputStream destImageStream) throws IORuntimeException{
		try {
			ImageIO.write(toBufferedImage(image), IMAGE_TYPE_PNG, destImageStream);// 输出到文件流
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 写出图像为目标文件扩展名对应的格式
	 * 
	 * @param image {@link Image}
	 * @param targetFile 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.0
	 */
	public static void write(Image image, File targetFile) throws IORuntimeException{
		try {
			ImageIO.write(toBufferedImage(image), FileUtil.extName(targetFile), targetFile);// 输出到文件
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 获得{@link ImageReader}
	 * @param type 图片文件类型，例如 "jpeg" 或 "tiff"
	 * @return {@link ImageReader}
	 */
	public static ImageReader getReader(String type) {
		Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(type);
		if(iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 将图片绘制在背景上
	 * 
	 * @param backgroundImg 背景图片
	 * @param img 要绘制的图片
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @return 绘制后的背景
	 */
	private static BufferedImage draw(BufferedImage backgroundImg, Image img, Rectangle rectangle) {
		final Graphics g = backgroundImg.getGraphics();
		g.drawImage(img, rectangle.x, rectangle.y, rectangle.width, rectangle.height, null); // 绘制切割后的图
		g.dispose();
		return backgroundImg;
	}
	
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
