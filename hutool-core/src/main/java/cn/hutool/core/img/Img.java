package cn.hutool.core.img;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 图像编辑器
 * 
 * @author looly
 * @since 4.1.5
 */
public class Img {

	private BufferedImage srcImage;
	private BufferedImage destImage;
	/** 目标图片文件格式，用于写出 */
	private String destImageType = ImageUtil.IMAGE_TYPE_JPG;
	/** 计算x,y坐标的时候是否从中心做为原始坐标开始计算 */
	private boolean positionBaseCentre = true;

	/**
	 * 从文件读取图片并开始处理
	 * 
	 * @param imageFile 图片文件
	 * @return {@link Img}
	 */
	public static Img from(File imageFile) {
		return new Img(ImageUtil.read(imageFile));
	}

	/**
	 * 从流读取图片并开始处理
	 * 
	 * @param in 图片流
	 * @return {@link Img}
	 */
	public static Img from(InputStream in) {
		return new Img(ImageUtil.read(in));
	}

	/**
	 * 从ImageInputStream取图片并开始处理
	 * 
	 * @param imageStream 图片流
	 * @return {@link Img}
	 */
	public static Img from(ImageInputStream imageStream) {
		return new Img(ImageUtil.read(imageStream));
	}

	/**
	 * 从URL取图片并开始处理
	 * 
	 * @param imageUrl 图片URL
	 * @return {@link Img}
	 */
	public static Img from(URL imageUrl) {
		return new Img(ImageUtil.read(imageUrl));
	}

	/**
	 * 从Image取图片并开始处理
	 * 
	 * @param image 图片
	 * @return {@link Img}
	 */
	public static Img from(Image image) {
		return new Img(ImageUtil.toBufferedImage(image));
	}

	/**
	 * 构造
	 * 
	 * @param srcImage 来源图片
	 */
	public Img(BufferedImage srcImage) {
		this.srcImage = srcImage;
	}

	/**
	 * 设置目标图片文件格式，用于写出
	 * 
	 * @param imgType 图片格式
	 * @return this
	 * @see ImageUtil#IMAGE_TYPE_JPG
	 * @see ImageUtil#IMAGE_TYPE_PNG
	 */
	public Img setDestImageType(String imgType) {
		this.destImageType = imgType;
		return this;
	}

	/**
	 * 计算x,y坐标的时候是否从中心做为原始坐标开始计算
	 * 
	 * @param positionBaseCentre 是否从中心做为原始坐标开始计算
	 * @since 4.1.15
	 */
	public Img setPositionBaseCentre(boolean positionBaseCentre) {
		this.positionBaseCentre = positionBaseCentre;
		return this;
	}

	/**
	 * 缩放图像（按比例缩放）
	 * 
	 * @param scale 缩放比例。比例大于1时为放大，小于1大于0为缩小
	 * @return this
	 */
	public Img scale(float scale) {
		if (scale < 0) {
			// 自动修正负数
			scale = -scale;
		}

		final BufferedImage srcImg = getValidSrcImg();
		int width = NumberUtil.mul(Integer.toString(srcImg.getWidth()), Float.toString(scale)).intValue(); // 得到源图宽
		int height = NumberUtil.mul(Integer.toString(srcImg.getHeight()), Float.toString(scale)).intValue(); // 得到源图长
		return scale(width, height);
	}

	/**
	 * 缩放图像（按长宽缩放）<br>
	 * 注意：目标长宽与原图不成比例会变形
	 * 
	 * @param width 目标宽度
	 * @param height 目标高度
	 * @return this
	 */
	public Img scale(int width, int height) {
		final BufferedImage srcImg = getValidSrcImg();
		int srcHeight = srcImg.getHeight();
		int srcWidth = srcImg.getWidth();
		int scaleType;
		if (srcHeight == height && srcWidth == width) {
			// 源与目标长宽一致返回原图
			this.destImage = srcImg;
			return this;
		} else if (srcHeight < height || srcWidth < width) {
			// 放大图片使用平滑模式
			scaleType = Image.SCALE_SMOOTH;
		} else {
			scaleType = Image.SCALE_DEFAULT;
		}
		final Image image = srcImg.getScaledInstance(width, height, scaleType);
		this.destImage = ImageUtil.toBufferedImage(image);
		return this;
	}

	/**
	 * 缩放图像（按高度和宽度缩放）<br>
	 * 缩放后默认为jpeg格式
	 * 
	 * @param width 缩放后的宽度
	 * @param height 缩放后的高度
	 * @param fixedColor 比例不对时补充的颜色，不补充为<code>null</code>
	 * @return this
	 */
	public Img scale(int width, int height, Color fixedColor) {
		final BufferedImage srcImage = getValidSrcImg();
		int srcHeight = srcImage.getHeight(null);
		int srcWidth = srcImage.getWidth(null);
		double heightRatio = NumberUtil.div(height, srcHeight);
		double widthRatio = NumberUtil.div(width, srcWidth);
		if (heightRatio == widthRatio) {
			// 长宽都按照相同比例缩放时，返回缩放后的图片
			return scale(width, height);
		}

		// 宽缩放比例小就按照宽缩放，否则按照高缩放
		if (widthRatio < heightRatio) {
			scale(width, (int) (srcHeight * widthRatio));
		} else {
			scale((int) (srcWidth * heightRatio), height);
		}

		if (null == fixedColor) {// 补白
			fixedColor = Color.WHITE;
		}
		final BufferedImage image = new BufferedImage(width, height, getTypeInt());
		Graphics2D g = image.createGraphics();

		// 设置背景
		g.setBackground(fixedColor);
		g.clearRect(0, 0, width, height);

		final BufferedImage itemp = this.destImage;
		final int itempHeight = itemp.getHeight();
		final int itempWidth = itemp.getWidth();
		// 在中间贴图
		g.drawImage(itemp, (width - itempWidth) / 2, (height - itempHeight) / 2, itempWidth, itempHeight, fixedColor, null);

		g.dispose();
		this.destImage = image;
		return this;
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height
	 * @return this
	 */
	public Img cut(Rectangle rectangle) {
		final BufferedImage srcImage = getValidSrcImg();
		rectangle = fixRectangle(rectangle, srcImage.getWidth(), srcImage.getHeight());

		final ImageFilter cropFilter = new CropImageFilter(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		final Image image = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(srcImage.getSource(), cropFilter));
		this.destImage = ImageUtil.toBufferedImage(image);
		return this;
	}

	/**
	 * 图像切割为圆形(按指定起点坐标和半径切割)，填充满整个图片（直径取长宽最小值）
	 * 
	 * @param x 原图的x坐标起始位置
	 * @param y 原图的y坐标起始位置
	 * @return this
	 * @since 4.1.15
	 */
	public Img cut(int x, int y) {
		return cut(x, y, -1);
	}

	/**
	 * 图像切割为圆形(按指定起点坐标和半径切割)
	 * 
	 * @param x 原图的x坐标起始位置
	 * @param y 原图的y坐标起始位置
	 * @param radius 半径，小于0表示填充满整个图片（直径取长宽最小值）
	 * @return this
	 * @since 4.1.15
	 */
	public Img cut(int x, int y, int radius) {
		final BufferedImage srcImage = getValidSrcImg();
		final int width = srcImage.getWidth();
		final int height = srcImage.getHeight();

		final int diameter = radius > 0 ? radius * 2 : Math.min(width, height);
		final BufferedImage destImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = destImage.createGraphics();
		g.setClip(new Ellipse2D.Double(0, 0, diameter, diameter));

		if (this.positionBaseCentre) {
			x = x - width / 2 + diameter / 2;
			y = y - height / 2 + diameter / 2;
		}
		g.drawImage(srcImage, x, y, null);
		g.dispose();
		this.destImage = destImage;
		return this;
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @return this
	 */
	public Img gray() {
		final ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		this.destImage = op.filter(getValidSrcImg(), null);
		return this;
	}

	/**
	 * 彩色转为黑白二值化图片
	 * 
	 * @return this
	 */
	public Img binary() {
		this.destImage = ImageUtil.copyImage(getValidSrcImg(), BufferedImage.TYPE_BYTE_BINARY);
		return this;
	}

	/**
	 * 给图片添加文字水印<br>
	 * 此方法并不关闭流
	 * 
	 * @param pressText 水印文字
	 * @param color 水印的字体颜色
	 * @param font {@link Font} 字体相关信息
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 处理后的图像
	 */
	public Img pressText(String pressText, Color color, Font font, int x, int y, float alpha) {
		final BufferedImage destImage = getValidSrcImg();
		final Graphics2D g = destImage.createGraphics();

		// 抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		g.setFont(font);
		// 透明度
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 在指定坐标绘制水印文字
		final FontMetrics metrics = g.getFontMetrics(font);
		final int textLength = metrics.stringWidth(pressText);
		final int textHeight = metrics.getAscent() - metrics.getLeading() - metrics.getDescent();
		g.drawString(pressText, Math.abs(destImage.getWidth() - textLength) / 2 + x, Math.abs(destImage.getHeight() + textHeight) / 2 + y);
		g.dispose();
		this.destImage = destImage;

		return this;
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 * 
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param x 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param y 修正值。 默认在中间，偏移量相对于中间偏移
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return this
	 */
	public Img pressImage(Image pressImg, int x, int y, float alpha) {
		final int pressImgWidth = pressImg.getWidth(null);
		final int pressImgHeight = pressImg.getHeight(null);

		return pressImage(pressImg, new Rectangle(x, y, pressImgWidth, pressImgHeight), alpha);
	}

	/**
	 * 给图片添加图片水印<br>
	 * 此方法并不关闭流
	 * 
	 * @param pressImg 水印图片，可以使用{@link ImageIO#read(File)}方法读取文件
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height，x,y从背景图片中心计算
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return this
	 * @since 4.1.14
	 */
	public Img pressImage(Image pressImg, Rectangle rectangle, float alpha) {
		final BufferedImage destImg = getValidSrcImg();

		rectangle = fixRectangle(rectangle, destImg.getWidth(), destImg.getHeight());
		draw(destImg, pressImg, rectangle, alpha);
		this.destImage = destImg;
		return this;
	}

	/**
	 * 旋转图片为指定角度<br>
	 * 来自：http://blog.51cto.com/cping1982/130066
	 * 
	 * @param degree 旋转角度
	 * @return 旋转后的图片
	 * @since 3.2.2
	 */
	public Img rotate(int degree) {
		final BufferedImage image = getValidSrcImg();
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		final Rectangle rectangle = calcRotatedSize(width, height, degree);
		final BufferedImage destImg = new BufferedImage(rectangle.width, rectangle.height, getTypeInt());
		Graphics2D graphics2d = destImg.createGraphics();
		// 抗锯齿
		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// 从中心旋转
		graphics2d.translate((rectangle.width - width) / 2, (rectangle.height - height) / 2);
		graphics2d.rotate(Math.toRadians(degree), width / 2, height / 2);
		graphics2d.drawImage(image, 0, 0, null);
		graphics2d.dispose();
		this.destImage = destImg;
		return this;
	}

	/**
	 * 水平翻转图像
	 * 
	 * @return this
	 */
	public Img flip() {
		final BufferedImage image = getValidSrcImg();
		int width = image.getWidth();
		int height = image.getHeight();
		final BufferedImage destImg = new BufferedImage(width, height, getTypeInt());
		Graphics2D graphics2d = destImg.createGraphics();
		graphics2d.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
		graphics2d.dispose();
		this.destImage = destImg;
		return this;
	}

	// ----------------------------------------------------------------------------------------------------------------- Write
	/**
	 * 获取处理过的图片
	 * 
	 * @return 处理过的图片
	 */
	public BufferedImage getImg() {
		return this.destImage;
	}

	/**
	 * 写出图像
	 * 
	 * @param out 写出到的目标流
	 * @throws IORuntimeException IO异常
	 */
	public void write(OutputStream out) throws IORuntimeException {
		write(ImageUtil.getImageOutputStream(out));
	}

	/**
	 * 写出图像为PNG格式
	 * 
	 * @param destImageStream 写出到的目标流
	 * @throws IORuntimeException IO异常
	 */
	public void write(ImageOutputStream destImageStream) throws IORuntimeException {
		try {
			ImageIO.write(this.destImage, this.destImageType, destImageStream);// 输出到文件流
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 写出图像为目标文件扩展名对应的格式
	 * 
	 * @param targetFile 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public void write(File targetFile) throws IORuntimeException {
		String formatName = FileUtil.extName(targetFile);
		if (StrUtil.isBlank(formatName)) {
			formatName = this.destImageType;
		}
		try {
			ImageIO.write(this.destImage, formatName, targetFile);// 输出到文件
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 将图片绘制在背景上
	 * 
	 * @param backgroundImg 背景图片
	 * @param img 要绘制的图片
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height，x,y从背景图片中心计算
	 * @return 绘制后的背景
	 */
	private static BufferedImage draw(BufferedImage backgroundImg, Image img, Rectangle rectangle, float alpha) {
		final Graphics2D g = backgroundImg.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		g.drawImage(img, rectangle.x, rectangle.y, rectangle.width, rectangle.height, null); // 绘制切割后的图
		g.dispose();
		return backgroundImg;
	}

	/**
	 * 获取int类型的图片类型
	 * 
	 * @return 图片类型
	 * @see BufferedImage#TYPE_INT_ARGB
	 * @see BufferedImage#TYPE_INT_RGB
	 */
	private int getTypeInt() {
		switch (this.destImageType) {
		case ImageUtil.IMAGE_TYPE_PNG:
			return BufferedImage.TYPE_INT_ARGB;
		default:
			return BufferedImage.TYPE_INT_RGB;
		}
	}

	/**
	 * 获取有效的源图片，首先检查上一次处理的结果图片，如无则使用用户传入的源图片
	 * 
	 * @return 有效的源图片
	 */
	private BufferedImage getValidSrcImg() {
		return ObjectUtil.defaultIfNull(this.destImage, this.srcImage);
	}

	/**
	 * 修正矩形框位置，如果{@link Img#setPositionFromCentre(boolean)} 设为{@code true}，则坐标修正为基于图形中心，否则基于左上角
	 * 
	 * @param rectangle 矩形
	 * @param baseWidth 参考宽
	 * @param baseHeight 参考高
	 * @return 修正后的{@link Rectangle}
	 * @since 4.1.15
	 */
	private Rectangle fixRectangle(Rectangle rectangle, int baseWidth, int baseHeight) {
		if (this.positionBaseCentre) {
			// 修正图片位置从背景的中心计算
			rectangle.setLocation(//
					rectangle.x + (int) (Math.abs(baseWidth - rectangle.width) / 2), //
					rectangle.y + (int) (Math.abs(baseHeight - rectangle.height) / 2)//
			);
		}
		return rectangle;
	}

	/**
	 * 计算旋转后的图片尺寸
	 * 
	 * @param width 宽度
	 * @param height 高度
	 * @param degree 旋转角度
	 * @return 计算后目标尺寸
	 * @since 4.1.20
	 */
	private static Rectangle calcRotatedSize(int width, int height, int degree) {
		if (degree >= 90) {
			if (degree / 90 % 2 == 1) {
				int temp = height;
				height = width;
				width = temp;
			}
			degree = degree % 90;
		}
		double r = Math.sqrt(height * height + width * width) / 2;
		double len = 2 * Math.sin(Math.toRadians(degree) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(degree)) / 2;
		double angel_dalta_width = Math.atan((double) height / width);
		double angel_dalta_height = Math.atan((double) width / height);
		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = width + len_dalta_width * 2;
		int des_height = height + len_dalta_height * 2;

		return new Rectangle(des_width, des_height);
	}
	// ---------------------------------------------------------------------------------------------------------------- Private method end
}
