package cn.hutool.poi.word;

import org.apache.poi.xwpf.usermodel.Document;

/**
 * Word中的图片类型
 *
 * @author looly
 * @since 5.1.6
 */
public enum PicType {
	EMF(Document.PICTURE_TYPE_EMF),
	WMF(Document.PICTURE_TYPE_WMF),
	PICT(Document.PICTURE_TYPE_PICT),
	JPEG(Document.PICTURE_TYPE_JPEG),
	PNG(Document.PICTURE_TYPE_PNG),
	DIB(Document.PICTURE_TYPE_DIB),
	GIF(Document.PICTURE_TYPE_GIF),
	TIFF(Document.PICTURE_TYPE_TIFF),
	EPS(Document.PICTURE_TYPE_EPS),
	WPG(Document.PICTURE_TYPE_WPG);

	/**
	 * 构造
	 * @param value 图片类型值
	 */
	PicType(int value) {
		this.value = value;
	}

	private final int value;

	/**
	 * 获取图片类型对应值
	 *
	 * @return 图片值
	 */
	public int getValue() {
		return this.value;
	}
}
