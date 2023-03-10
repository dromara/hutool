package cn.hutool.swing.img;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.builder.Builder;

import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义和管理键和关联值的集合构建器，提供配置包括：
 * <ol>
 *     <li>{@link RenderingHints#KEY_ANTIALIASING}        抗锯齿 </li>
 *     <li>{@link RenderingHints#KEY_TEXT_ANTIALIASING}   文本抗锯齿</li>
 *     <li>{@link RenderingHints#KEY_COLOR_RENDERING}     颜色着色的渲染方式</li>
 *     <li>{@link RenderingHints#KEY_DITHERING}           抖动</li>
 *     <li>{@link RenderingHints#KEY_FRACTIONALMETRICS}   字体规格</li>
 *     <li>{@link RenderingHints#KEY_INTERPOLATION}       内插</li>
 *     <li>{@link RenderingHints#KEY_ALPHA_INTERPOLATION} alpha合成微调</li>
 *     <li>{@link RenderingHints#KEY_RENDERING}           着色</li>
 *     <li>{@link RenderingHints#KEY_STROKE_CONTROL}      笔划规范化控制</li>
 *     <li>{@link RenderingHints#KEY_TEXT_LCD_CONTRAST}   LCD文本对比呈现</li>
 * </ol>
 *
 * @author looly
 * @since 6.0.0
 */
public class RenderingHintsBuilder implements Builder<RenderingHints> {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建{@link RenderingHints} 构建器
	 *
	 * @return {@code RenderingHintsBuilder}
	 */
	public static RenderingHintsBuilder of() {
		return new RenderingHintsBuilder();
	}

	private final Map<RenderingHints.Key, Object> hintsMap;

	/**
	 * 构造
	 */
	private RenderingHintsBuilder() {
		// 共计10项配置
		hintsMap = new HashMap<>(10, 1);
	}

	/**
	 * 设置是否使用抗锯齿
	 *
	 * @param antialias 抗锯齿选项，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setAntialiasing(final Antialias antialias) {
		final RenderingHints.Key key = RenderingHints.KEY_ANTIALIASING;
		if (null == antialias) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, antialias.getValue());
		}
		return this;
	}

	/**
	 * 设置对文本着色时是否抗锯齿
	 *
	 * @param textAntialias 文本抗锯齿方式，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setTextAntialias(final TextAntialias textAntialias) {
		final RenderingHints.Key key = RenderingHints.KEY_TEXT_ANTIALIASING;
		if (null == textAntialias) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, textAntialias.getValue());
		}
		return this;
	}

	/**
	 * 设置控制颜色着色的渲染方式
	 *
	 * @param colorRender 颜色着色的渲染方式，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setColorRendering(final ColorRender colorRender) {
		final RenderingHints.Key key = RenderingHints.KEY_COLOR_RENDERING;
		if (null == colorRender) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, colorRender.getValue());
		}
		return this;
	}

	/**
	 * 设置控制如何处理抖动<br>
	 * 抖动是用一组有限的颜色合成出一个更大范围的颜色的过程，方法是给相邻像素着色以产生不在该组颜色中的新的颜色幻觉。
	 *
	 * @param dither 如何处理抖动，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setDithering(final Dither dither) {
		final RenderingHints.Key key = RenderingHints.KEY_DITHERING;
		if (null == dither) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, dither.getValue());
		}
		return this;
	}

	/**
	 * 设置字体规格
	 *
	 * @param fractionalMetrics 字体规格，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setFractionalMetrics(final FractionalMetrics fractionalMetrics) {
		final RenderingHints.Key key = RenderingHints.KEY_FRACTIONALMETRICS;
		if (null == fractionalMetrics) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, fractionalMetrics.getValue());
		}
		return this;
	}

	/**
	 * 设置怎样做内插<br>
	 * 在对一个源图像做变形时，变形后的像素很少能够恰好对应目标像素位置。<br>
	 * 在这种情况下，每个变形后的像素的颜色值不得不由周围的像素决定。内插就是实现上述过程。
	 *
	 * @param interpolation 内插方式，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setInterpolation(final Interpolation interpolation) {
		final RenderingHints.Key key = RenderingHints.KEY_INTERPOLATION;
		if (null == interpolation) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, interpolation.getValue());
		}
		return this;
	}

	/**
	 * 设置alpha合成微调
	 *
	 * @param alphaInterpolation alpha合成微调，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setAlphaInterpolation(final AlphaInterpolation alphaInterpolation) {
		final RenderingHints.Key key = RenderingHints.KEY_ALPHA_INTERPOLATION;
		if (null == alphaInterpolation) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, alphaInterpolation.getValue());
		}
		return this;
	}

	/**
	 * 设置着色技术，在速度和质量之间进行权衡。
	 *
	 * @param render 着色技术，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setRendering(final Render render) {
		final RenderingHints.Key key = RenderingHints.KEY_RENDERING;
		if (null == render) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, render.getValue());
		}
		return this;
	}

	/**
	 * 设置笔划规范化控制
	 *
	 * @param strokeControl 笔划规范化控制，{@code null}表示移除此选项
	 * @return this
	 */
	public RenderingHintsBuilder setStrokeControl(final StrokeControl strokeControl) {
		final RenderingHints.Key key = RenderingHints.KEY_STROKE_CONTROL;
		if (null == strokeControl) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, strokeControl.getValue());
		}
		return this;
	}

	/**
	 * 设置LCD文本对比呈现<br>
	 * ，100 到 250 之间的正整数。通常，有用值的范围缩小到 140-180
	 *
	 * @param textLCDContrast LCD文本对比呈现，100 到 250 之间的正整数
	 * @return this
	 */
	public RenderingHintsBuilder setTextLCDContrast(final Integer textLCDContrast) {
		final RenderingHints.Key key = RenderingHints.KEY_TEXT_LCD_CONTRAST;
		if (null == textLCDContrast) {
			this.hintsMap.remove(key);
		} else {
			this.hintsMap.put(key, Assert.checkBetween(textLCDContrast.intValue(), 100, 250));
		}
		return this;
	}

	@Override
	public RenderingHints build() {
		return new RenderingHints(this.hintsMap);
	}

	// region ----- enums

	/**
	 * 抗锯齿选项
	 *
	 * @see RenderingHints#VALUE_ANTIALIAS_ON
	 * @see RenderingHints#VALUE_ANTIALIAS_OFF
	 * @see RenderingHints#VALUE_ANTIALIAS_DEFAULT
	 */
	public enum Antialias {
		/**
		 * 使用抗锯齿
		 */
		ON(RenderingHints.VALUE_ANTIALIAS_ON),
		/**
		 * 不使用抗锯齿
		 */
		OFF(RenderingHints.VALUE_ANTIALIAS_OFF),
		/**
		 * 默认的抗锯齿
		 */
		DEFAULT(RenderingHints.VALUE_ANTIALIAS_OFF);

		private final Object value;

		Antialias(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * 文本抗锯齿选项
	 *
	 * @see RenderingHints#VALUE_TEXT_ANTIALIAS_ON
	 * @see RenderingHints#VALUE_TEXT_ANTIALIAS_OFF
	 * @see RenderingHints#VALUE_TEXT_ANTIALIAS_DEFAULT
	 */
	public enum TextAntialias {
		/**
		 * 使用抗锯齿呈现文本
		 */
		ON(RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
		/**
		 * 不使用抗锯齿呈现文本
		 */
		OFF(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF),
		/**
		 * 使用平台默认的文本抗锯齿模式呈现文本
		 */
		DEFAULT(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF),
		/**
		 * 自动的使用字体中的信息决定是否使用抗锯齿或使用实心颜色
		 */
		GASP(RenderingHints.VALUE_TEXT_ANTIALIAS_GASP),
		/**
		 * 针对LCD显示器优化文本显示LCD_HRGB
		 */
		LCD_HRGB(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB),
		/**
		 * 针对LCD显示器优化文本显示LCD_HBGR
		 */
		LCD_HBGR(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR),
		/**
		 * 针对LCD显示器优化文本显示LCD_VRGB
		 */
		LCD_VRGB(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB),
		/**
		 * 针对LCD显示器优化文本显示LCD_VBGR
		 */
		LCD_VBGR(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR);

		private final Object value;

		TextAntialias(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * 颜色着色的渲染方式
	 *
	 * @see RenderingHints#VALUE_COLOR_RENDER_SPEED
	 * @see RenderingHints#VALUE_COLOR_RENDER_QUALITY
	 * @see RenderingHints#VALUE_COLOR_RENDER_DEFAULT
	 */
	public enum ColorRender {
		/**
		 * 追求速度
		 */
		SPEED(RenderingHints.VALUE_COLOR_RENDER_SPEED),
		/**
		 * 追求质量
		 */
		QUALITY(RenderingHints.VALUE_COLOR_RENDER_QUALITY),
		/**
		 * 默认渲染方式
		 */
		DEFAULT(RenderingHints.VALUE_COLOR_RENDER_DEFAULT);

		private final Object value;

		ColorRender(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * 着色技术
	 *
	 * @see RenderingHints#VALUE_RENDER_SPEED
	 * @see RenderingHints#VALUE_RENDER_QUALITY
	 * @see RenderingHints#VALUE_RENDER_DEFAULT
	 */
	public enum Render {
		/**
		 * 追求速度
		 */
		SPEED(RenderingHints.VALUE_RENDER_SPEED),
		/**
		 * 追求质量
		 */
		QUALITY(RenderingHints.VALUE_RENDER_QUALITY),
		/**
		 * 默认
		 */
		DEFAULT(RenderingHints.VALUE_RENDER_DEFAULT);

		private final Object value;

		Render(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * 控制如何处理抖动<br>
	 * 抖动是用一组有限的颜色合成出一个更大范围的颜色的过程，方法是给相邻像素着色以产生不在该组颜色中的新的颜色幻觉。
	 *
	 * @see RenderingHints#VALUE_DITHER_ENABLE
	 * @see RenderingHints#VALUE_DITHER_DISABLE
	 * @see RenderingHints#VALUE_DITHER_DEFAULT
	 */
	public enum Dither {
		/**
		 * 抖动
		 */
		ENABLE(RenderingHints.VALUE_DITHER_ENABLE),
		/**
		 * 不抖动
		 */
		DISABLE(RenderingHints.VALUE_DITHER_DISABLE),
		/**
		 * 默认
		 */
		DEFAULT(RenderingHints.VALUE_DITHER_DEFAULT);

		private final Object value;

		Dither(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * 字体规格
	 *
	 * @see RenderingHints#VALUE_FRACTIONALMETRICS_ON
	 * @see RenderingHints#VALUE_FRACTIONALMETRICS_OFF
	 * @see RenderingHints#VALUE_FRACTIONALMETRICS_DEFAULT
	 */
	public enum FractionalMetrics {
		/**
		 * 启用字体规格
		 */
		SPEED(RenderingHints.VALUE_FRACTIONALMETRICS_ON),
		/**
		 * 禁用字体规格
		 */
		QUALITY(RenderingHints.VALUE_FRACTIONALMETRICS_OFF),
		/**
		 * 默认
		 */
		DEFAULT(RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);

		private final Object value;

		FractionalMetrics(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * 内插<br>
	 * 在对一个源图像做变形时，变形后的像素很少能够恰好对应目标像素位置。<br>
	 * 在这种情况下，每个变形后的像素的颜色值不得不由周围的像素决定。内插就是实现上述过程。
	 *
	 * @see RenderingHints#VALUE_INTERPOLATION_BICUBIC
	 * @see RenderingHints#VALUE_INTERPOLATION_BILINEAR
	 * @see RenderingHints#VALUE_INTERPOLATION_NEAREST_NEIGHBOR
	 */
	public enum Interpolation {
		/**
		 * 双三次插值
		 */
		BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),
		/**
		 * 双线性插值
		 */
		BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
		/**
		 * 最近邻插值
		 */
		NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		private final Object value;

		Interpolation(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * alpha合成微调
	 *
	 * @see RenderingHints#VALUE_ALPHA_INTERPOLATION_SPEED
	 * @see RenderingHints#VALUE_ALPHA_INTERPOLATION_QUALITY
	 * @see RenderingHints#VALUE_ALPHA_INTERPOLATION_DEFAULT
	 */
	public enum AlphaInterpolation {
		/**
		 * 追求速度
		 */
		SPEED(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED),
		/**
		 * 追求质量
		 */
		QUALITY(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
		/**
		 * 平台默认
		 */
		DEFAULT(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

		private final Object value;

		AlphaInterpolation(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * 笔划规范化控制
	 *
	 * @see RenderingHints#VALUE_STROKE_NORMALIZE
	 * @see RenderingHints#VALUE_STROKE_PURE
	 * @see RenderingHints#VALUE_STROKE_DEFAULT
	 */
	public enum StrokeControl {
		/**
		 * 追求速度
		 */
		NORMALIZE(RenderingHints.VALUE_STROKE_NORMALIZE),
		/**
		 * 追求质量
		 */
		PURE(RenderingHints.VALUE_STROKE_PURE),
		/**
		 * 平台默认
		 */
		DEFAULT(RenderingHints.VALUE_STROKE_DEFAULT);

		private final Object value;

		StrokeControl(final Object value) {
			this.value = value;
		}

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		public Object getValue() {
			return this.value;
		}
	}


	// endregion
}
