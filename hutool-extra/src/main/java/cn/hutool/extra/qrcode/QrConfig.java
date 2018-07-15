package cn.hutool.extra.qrcode;

import java.nio.charset.Charset;
import java.util.HashMap;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.hutool.core.util.CharsetUtil;

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
	private int width;
	/** 长 */
	private int height;
	/** 前景色（二维码颜色） */
	private int foreColor = BLACK;
	/** 背景色 */
	private int backColor = WHITE;
	/** 边距1~4 */
	private Integer margin = 2;
	/** 纠错级别 */
	private ErrorCorrectionLevel errorCorrection;
	/** 编码 */
	private Charset charset = CharsetUtil.CHARSET_UTF_8;
	
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
	 */
	public void setWidth(int width) {
		this.width = width;
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
	 */
	public void setHeight(int height) {
		this.height = height;
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
	 */
	public void setForeColor(int foreColor) {
		this.foreColor = foreColor;
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
	 */
	public void setBackColor(int backColor) {
		this.backColor = backColor;
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
	 */
	public void setMargin(Integer margin) {
		this.margin = margin;
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
	 */
	public void setErrorCorrection(ErrorCorrectionLevel errorCorrection) {
		this.errorCorrection = errorCorrection;
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
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * 转换为Zxing的二维码配置
	 * @return 配置
	 */
	public HashMap<EncodeHintType, Object> toHints() {
		// 配置
		final HashMap<EncodeHintType, Object> hints = new HashMap<>();
		if (null != this.charset) {
			hints.put(EncodeHintType.CHARACTER_SET, charset.toString());
		}
		if(null != this.errorCorrection) {
			hints.put(EncodeHintType.ERROR_CORRECTION, this.errorCorrection);
		}
		if(null != this.margin) {
			hints.put(EncodeHintType.MARGIN, this.margin);
		}
		return hints;
	}
}
