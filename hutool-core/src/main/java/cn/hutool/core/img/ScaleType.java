package cn.hutool.core.img;

import java.awt.Image;

/**
 * 图片缩略算法类型
 * 
 * @author looly
 * @since 4.5.8
 */
public enum ScaleType {

	/** 默认 */
	DEFAULT(Image.SCALE_DEFAULT),
	/** 快速 */
	FAST(Image.SCALE_FAST),
	/** 平滑 */
	SMOOTH(Image.SCALE_SMOOTH),
	/** 使用 ReplicateScaleFilter 类中包含的图像缩放算法 */
	REPLICATE(Image.SCALE_REPLICATE),
	/** Area Averaging算法 */
	AREA_AVERAGING(Image.SCALE_AREA_AVERAGING);

	/**
	 * 构造
	 * 
	 * @param value 缩放方式
	 * @see Image#SCALE_DEFAULT
	 * @see Image#SCALE_FAST
	 * @see Image#SCALE_SMOOTH
	 * @see Image#SCALE_REPLICATE
	 * @see Image#SCALE_AREA_AVERAGING
	 */
	ScaleType(int value) {
		this.value = value;
	}

	private final int value;

	public int getValue() {
		return this.value;
	}
}
