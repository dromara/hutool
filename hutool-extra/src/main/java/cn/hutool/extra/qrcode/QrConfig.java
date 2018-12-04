package cn.hutool.extra.qrcode;

import java.awt.Image;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ImageUtil;

/**
 * 二维码设置
 * 
 * @author looly
 * @since 4.1.2
 */
public class QrConfig {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	/** 宽 */
	protected int width;
	/** 长 */
	protected int height;
	/** 前景色（二维码颜色） */
	protected int foreColor = BLACK;
	/** 背景色 */
	protected int backColor = WHITE;
	/** 边距1~4 */
	protected Integer margin = 2;
	/** 纠错级别 */
	protected ErrorCorrectionLevel errorCorrection = ErrorCorrectionLevel.L;
	/** 编码 */
	protected Charset charset = CharsetUtil.CHARSET_UTF_8;
	/** 二维码中的Logo */
	protected Image img;
	/** 二维码中的Logo缩放的比例系数，如5表示长宽最小值的1/5 */
	protected int ratio;
	
	/**
	 * 创建QrConfig
	 * @return QrConfig
	 * @since 4.1.14
	 */
	public static QrConfig create() {
		return new QrConfig();
	}

	/**
	 * 构造，默认长宽为300
	 */
	public QrConfig() {
		this(300, 300);
	}

	/**
	 * 构造
	 * 
	 * @param width 宽
	 * @param height 长
	 */
	public QrConfig(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * 获取宽度
	 * 
	 * @return 宽度
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置宽度
	 * 
	 * @param width 宽度
	 * @return this
	 */
	public QrConfig setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * 获取高度
	 * 
	 * @return 高度
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 设置高度
	 * 
	 * @param height 高度
	 * @return this;
	 */
	public QrConfig setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * 获取前景色
	 * 
	 * @return 前景色
	 */
	public int getForeColor() {
		return foreColor;
	}

	/**
	 * 设置前景色，例如：Color.BLUE.getRGB()
	 * 
	 * @param foreColor 前景色
	 * @return this
	 */
	public QrConfig setForeColor(int foreColor) {
		this.foreColor = foreColor;
		return this;
	}

	/**
	 * 获取背景色
	 * 
	 * @return 背景色
	 */
	public int getBackColor() {
		return backColor;
	}

	/**
	 * 设置背景色，例如：Color.BLUE.getRGB()
	 * 
	 * @param backColor 背景色
	 * @return this
	 */
	public QrConfig setBackColor(int backColor) {
		this.backColor = backColor;
		return this;
	}

	/**
	 * 获取边距
	 * 
	 * @return 边距
	 */
	public Integer getMargin() {
		return margin;
	}

	/**
	 * 设置边距
	 * 
	 * @param margin 边距
	 * @return this
	 */
	public QrConfig setMargin(Integer margin) {
		this.margin = margin;
		return this;
	}

	/**
	 * 获取纠错级别
	 * 
	 * @return 纠错级别
	 */
	public ErrorCorrectionLevel getErrorCorrection() {
		return errorCorrection;
	}

	/**
	 * 设置纠错级别
	 * 
	 * @param errorCorrection 纠错级别
	 * @return this
	 */
	public QrConfig setErrorCorrection(ErrorCorrectionLevel errorCorrection) {
		this.errorCorrection = errorCorrection;
		return this;
	}

	/**
	 * 获取编码
	 * 
	 * @return 编码
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置编码
	 * 
	 * @param charset 编码
	 * @return this
	 */
	public QrConfig setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 获取二维码中的Logo
	 * 
	 * @return Logo图片
	 */
	public Image getImg() {
		return img;
	}
	
	/**
	 * 设置二维码中的Logo文件
	 * 
	 * @param imgPath 二维码中的Logo路径
	 * @return this;
	 */
	public QrConfig setImg(String imgPath) {
		return setImg(FileUtil.file(imgPath));
	}
	
	/**
	 * 设置二维码中的Logo文件
	 * 
	 * @param imgFile 二维码中的Logo
	 * @return this;
	 */
	public QrConfig setImg(File imgFile) {
		return setImg(ImageUtil.read(imgFile));
	}

	/**
	 * 设置二维码中的Logo
	 * 
	 * @param img 二维码中的Logo
	 * @return this;
	 */
	public QrConfig setImg(Image img) {
		this.img = img;
		return this;
	}

	/**
	 * 获取二维码中的Logo缩放的比例系数，如5表示长宽最小值的1/5
	 * 
	 * @return 二维码中的Logo缩放的比例系数，如5表示长宽最小值的1/5
	 */
	public int getRatio() {
		return this.ratio;
	}

	/**
	 * 设置二维码中的Logo缩放的比例系数，如5表示长宽最小值的1/5
	 * 
	 * @param ratio 二维码中的Logo缩放的比例系数，如5表示长宽最小值的1/5
	 * @return this;
	 */
	public QrConfig setRatio(int ratio) {
		this.ratio = ratio;
		return this;
	}

	/**
	 * 转换为Zxing的二维码配置
	 * 
	 * @return 配置
	 */
	public HashMap<EncodeHintType, Object> toHints() {
		// 配置
		final HashMap<EncodeHintType, Object> hints = new HashMap<>();
		if (null != this.charset) {
			hints.put(EncodeHintType.CHARACTER_SET, charset.toString());
		}
		if (null != this.errorCorrection) {
			hints.put(EncodeHintType.ERROR_CORRECTION, this.errorCorrection);
		}
		if (null != this.margin) {
			hints.put(EncodeHintType.MARGIN, this.margin);
		}
		return hints;
	}
}
