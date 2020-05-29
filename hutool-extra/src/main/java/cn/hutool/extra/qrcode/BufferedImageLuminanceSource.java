package cn.hutool.extra.qrcode;

import com.google.zxing.LuminanceSource;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * {@link BufferedImage} 图片二维码源<br>
 * 来自：http://blog.csdn.net/yangxin_blog/article/details/50850701<br>
 * 此类同样在zxing-j2se包中也有提供
 * 
 * @author zxing, Looly
 * @since 4.0.2
 */
public final class BufferedImageLuminanceSource extends LuminanceSource {

	private final BufferedImage image;
	private final int left;
	private final int top;

	/**
	 * 构造
	 * 
	 * @param image {@link BufferedImage}
	 */
	public BufferedImageLuminanceSource(BufferedImage image) {
		this(image, 0, 0, image.getWidth(), image.getHeight());
	}

	/**
	 * 构造
	 * 
	 * @param image {@link BufferedImage}
	 * @param left 左边间隔
	 * @param top 顶部间隔
	 * @param width 宽度
	 * @param height 高度
	 */
	public BufferedImageLuminanceSource(BufferedImage image, int left, int top, int width, int height) {
		super(width, height);

		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();
		if (left + width > sourceWidth || top + height > sourceHeight) {
			throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
		}

		for (int y = top; y < top + height; y++) {
			for (int x = left; x < left + width; x++) {
				if ((image.getRGB(x, y) & 0xFF000000) == 0) {
					image.setRGB(x, y, 0xFFFFFFFF); // = white
				}
			}
		}

		this.image = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
		this.image.getGraphics().drawImage(image, 0, 0, null);
		this.left = left;
		this.top = top;
	}

	@Override
	public byte[] getRow(int y, byte[] row) {
		if (y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Requested row is outside the image: " + y);
		}
		int width = getWidth();
		if (row == null || row.length < width) {
			row = new byte[width];
		}
		image.getRaster().getDataElements(left, top + y, width, 1, row);
		return row;
	}

	@Override
	public byte[] getMatrix() {
		int width = getWidth();
		int height = getHeight();
		int area = width * height;
		byte[] matrix = new byte[area];
		image.getRaster().getDataElements(left, top, width, height, matrix);
		return matrix;
	}

	@Override
	public boolean isCropSupported() {
		return true;
	}

	@Override
	public LuminanceSource crop(int left, int top, int width, int height) {
		return new BufferedImageLuminanceSource(image, this.left + left, this.top + top, width, height);
	}

	@Override
	public boolean isRotateSupported() {
		return true;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	public LuminanceSource rotateCounterClockwise() {

		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();

		AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidth);

		BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(image, transform, null);
		g.dispose();

		int width = getWidth();
		return new BufferedImageLuminanceSource(rotatedImage, top, sourceWidth - (left + width), getHeight(), width);
	}

}