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

package org.dromara.hutool.extra.qrcode;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.swing.img.ImgUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * 二维码设置
 *
 * @author looly
 * @since 4.1.2
 */
public class QrConfig {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 宽度
	 */
	protected int width;
	/**
	 * 高度
	 */
	protected int height;
	/**
	 * 前景色（二维码颜色）
	 */
	protected Integer foreColor = BLACK;
	/**
	 * 背景色，默认白色，null表示透明
	 */
	protected Integer backColor = WHITE;
	/**
	 * 边距0~4
	 */
	protected Integer margin = 2;
	/**
	 * 设置二维码中的信息量，可设置0-40的整数
	 */
	protected Integer qrVersion;
	/**
	 * 纠错级别
	 */
	protected ErrorCorrectionLevel errorCorrection = ErrorCorrectionLevel.M;

	/**
	 * 编码
	 */
	protected Charset charset = CharsetUtil.UTF_8;
	/**
	 * 二维码中的Logo
	 */
	protected Image img;
	/**
	 * 二维码中的Logo缩放的比例系数，如5表示长宽最小值的1/5
	 */
	protected int ratio = 6;
	/**
	 * DATA_MATRIX的符号形状
	 */
	protected SymbolShapeHint shapeHint = SymbolShapeHint.FORCE_NONE;

	/**
	 * 生成码的格式，默认为二维码
	 */
	protected BarcodeFormat format = BarcodeFormat.QR_CODE;

	/**
	 * 创建QrConfig
	 *
	 * @return QrConfig
	 * @since 4.1.14
	 */
	public static QrConfig of() {
		return new QrConfig();
	}

	/**
	 * 创建QrConfig
	 *
	 * @param width  宽
	 * @param height 高
	 * @return QrConfig
	 * @since 4.1.14
	 */
	public static QrConfig of(final int width, final int height) {
		return new QrConfig(width, height);
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
	 * @param width  宽
	 * @param height 高
	 */
	public QrConfig(final int width, final int height) {
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
	public QrConfig setWidth(final int width) {
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
	public QrConfig setHeight(final int height) {
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
	 * @since 5.1.1
	 */
	public QrConfig setForeColor(final Color foreColor) {
		if (null == foreColor) {
			this.foreColor = null;
		} else {
			this.foreColor = foreColor.getRGB();
		}
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
	 * 设置背景色，例如：Color.BLUE
	 *
	 * @param backColor 背景色,null表示透明背景
	 * @return this
	 * @since 5.1.1
	 */
	public QrConfig setBackColor(final Color backColor) {
		if (null == backColor) {
			this.backColor = null;
		} else {
			this.backColor = backColor.getRGB();
		}
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
	public QrConfig setMargin(final Integer margin) {
		this.margin = margin;
		return this;
	}

	/**
	 * 设置二维码中的信息量，可设置0-40的整数，二维码图片也会根据qrVersion而变化，0表示根据传入信息自动变化
	 *
	 * @return 二维码中的信息量
	 */
	public Integer getQrVersion() {
		return qrVersion;
	}

	/**
	 * 设置二维码中的信息量，可设置0-40的整数，二维码图片也会根据qrVersion而变化，0表示根据传入信息自动变化
	 *
	 * @param qrVersion 二维码中的信息量
	 * @return this
	 */
	public QrConfig setQrVersion(final Integer qrVersion) {
		this.qrVersion = qrVersion;
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
	 * 是否开启ECI编码<br>
	 * 如果enableEci=false,则二维码中不包含ECI信息，即：{@link #charset}字符编码设置为{@code null}, 二维码为英文字符，保持false最佳<br>
	 * 如果enableEci=true,则二维码中包含ECI信息，即：按照{@link #charset}编码进行设置, 二维码为包含中文，保持true最佳，否则会中文乱码<br>
	 *
	 * <ul>
	 *     <li>参考1：<a href="https://github.com/nutzam/nutz-qrcode/issues/6">关于\000026的问题</a></li>
	 *     <li>参考2：<a href="https://en.wikipedia.org/wiki/Extended_Channel_Interpretation">ECI（Extended_Channel_Interpretation）模式</a></li>
	 *     <li>参考3：<a href="https://www.51cto.com/article/414082.html">二维码的生成细节和原理</a></li>
	 * </ul>
	 *
	 * <p>
	 * 二维码编码有ECI模式和非ECI模式的情况之分，在ECI模式下第一个字节是用作编码标识，而非ECI模式下直接就是数据流。
	 * ECI模式其实是更好的方案，这样子解码的时候可以根据标识采用不同的编码方式。而非ECI模式只能按照一种统一的方式处理了。
	 * 但是由于部分设备不支持ECI模式，所以就出现了无法识别的情况。
	 * 使用扫码桩/扫码枪，可能会出现\000026的字符。使用手机扫描、其他二维码解析软件扫描，则不会出现。
	 * </p>
	 *
	 * <p>
	 * ECI编码表可以看出UTF-8就是对应"\000026"（对应数字22）<br><br>
	 * </p>
	 *
	 * <p> 总结建议：如果二维码内容全是字符，没有中文，就不用使用UTF-8等格式进行编码，只有使用中文等特殊符号才需要编码 </p>
	 *
	 * @param enableEci 是否开启ECI
	 * @see EncodeHintType#PDF417_AUTO_ECI
	 */
	public void setEnableEci(final boolean enableEci) {
		if (enableEci) {
			if(null == this.charset){
				this.charset = CharsetUtil.UTF_8;
			}
		} else {
			this.charset = null;
		}
	}

	/**
	 * 设置纠错级别
	 *
	 * @param errorCorrection 纠错级别
	 * @return this
	 */
	public QrConfig setErrorCorrection(final ErrorCorrectionLevel errorCorrection) {
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
	public QrConfig setCharset(final Charset charset) {
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
	public QrConfig setImg(final String imgPath) {
		return setImg(FileUtil.file(imgPath));
	}

	/**
	 * 设置二维码中的Logo文件
	 *
	 * @param imageBytes 二维码中的Logo图片bytes表示形式
	 * @return this;
	 */
	public QrConfig setImg(final byte[] imageBytes) {
		return setImg(ImgUtil.toImage(imageBytes));
	}

	/**
	 * 设置二维码中的Logo文件
	 *
	 * @param imgFile 二维码中的Logo
	 * @return this;
	 */
	public QrConfig setImg(final File imgFile) {
		return setImg(ImgUtil.read(imgFile));
	}

	/**
	 * 设置二维码中的Logo
	 *
	 * @param img 二维码中的Logo
	 * @return this;
	 */
	public QrConfig setImg(final Image img) {
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
	public QrConfig setRatio(final int ratio) {
		this.ratio = ratio;
		return this;
	}

	/**
	 * 设置DATA_MATRIX的符号形状
	 *
	 * @param shapeHint DATA_MATRIX的符号形状
	 * @return this
	 */
	public QrConfig setShapeHint(final SymbolShapeHint shapeHint) {
		this.shapeHint = shapeHint;
		return this;
	}

	/**
	 * 获取码格式
	 *
	 * @return 码格式，默认为二维码
	 */
	public BarcodeFormat getFormat() {
		return format;
	}

	/**
	 * 设置码格式，默认二维码
	 *
	 * @param format 码格式
	 * @return this
	 */
	public QrConfig setFormat(final BarcodeFormat format) {
		this.format = format;
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
		// 只有不禁用（即开启）ECI编码功能，才使用自定义的字符编码
		// 二维码内容就是英文字符，建议不设置编码，没有任何问题；对于中文来说，会乱码
		if (null != this.charset) {
			hints.put(EncodeHintType.CHARACTER_SET, charset.toString().toLowerCase());
		}
		if (null != this.errorCorrection) {
			final Object value;
			if (BarcodeFormat.AZTEC == format || BarcodeFormat.PDF_417 == format) {
				// issue#I4FE3U@Gitee
				value = this.errorCorrection.getBits();
			} else {
				value = this.errorCorrection;
			}

			hints.put(EncodeHintType.ERROR_CORRECTION, value);
			hints.put(EncodeHintType.DATA_MATRIX_SHAPE, shapeHint);
		}
		if (null != this.margin) {
			hints.put(EncodeHintType.MARGIN, this.margin);
		}
		if (null != this.qrVersion) {
			hints.put(EncodeHintType.QR_VERSION, this.qrVersion);
		}
		return hints;
	}
}
