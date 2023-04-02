/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.qrcode;

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
	public BufferedImageLuminanceSource(final BufferedImage image) {
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
	public BufferedImageLuminanceSource(final BufferedImage image, final int left, final int top, final int width, final int height) {
		super(width, height);

		final int sourceWidth = image.getWidth();
		final int sourceHeight = image.getHeight();
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
	public byte[] getRow(final int y, byte[] row) {
		if (y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Requested row is outside the image: " + y);
		}
		final int width = getWidth();
		if (row == null || row.length < width) {
			row = new byte[width];
		}
		image.getRaster().getDataElements(left, top + y, width, 1, row);
		return row;
	}

	@Override
	public byte[] getMatrix() {
		final int width = getWidth();
		final int height = getHeight();
		final int area = width * height;
		final byte[] matrix = new byte[area];
		image.getRaster().getDataElements(left, top, width, height, matrix);
		return matrix;
	}

	@Override
	public boolean isCropSupported() {
		return true;
	}

	@Override
	public LuminanceSource crop(final int left, final int top, final int width, final int height) {
		return new BufferedImageLuminanceSource(image, this.left + left, this.top + top, width, height);
	}

	@Override
	public boolean isRotateSupported() {
		return true;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	public LuminanceSource rotateCounterClockwise() {

		final int sourceWidth = image.getWidth();
		final int sourceHeight = image.getHeight();

		final AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidth);

		final BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth, BufferedImage.TYPE_BYTE_GRAY);

		final Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(image, transform, null);
		g.dispose();

		final int width = getWidth();
		return new BufferedImageLuminanceSource(rotatedImage, top, sourceWidth - (left + width), getHeight(), width);
	}

}
