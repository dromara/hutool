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

package org.dromara.hutool.swing.captcha;


import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.util.RandomUtil;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.dromara.hutool.swing.captcha.generator.CodeGenerator;
import org.dromara.hutool.swing.captcha.generator.RandomGenerator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * Gif验证码类
 *
 * @author hsoftxl
 * @since 5.5.2
 */
public class GifCaptcha extends AbstractCaptcha {
	private static final long serialVersionUID = 7091627304326538464L;

	//量化器取样间隔 - 默认是10ms
	private int quality = 10;
	// 帧循环次数
	private int repeat = 0;
	//设置随机颜色时，最小的取色范围
	private int minColor = 0;
	//设置随机颜色时，最大的取色范围
	private int maxColor = 255;


	/**
	 * 可以设置验证码宽度，高度的构造函数
	 *
	 * @param width  验证码宽度
	 * @param height 验证码高度
	 */
	public GifCaptcha(final int width, final int height) {
		this(width, height, 5);
	}

	/**
	 * @param width     验证码宽度
	 * @param height    验证码高度
	 * @param codeCount 验证码个数
	 */
	public GifCaptcha(final int width, final int height, final int codeCount) {
		this(width, height, codeCount, 10);
	}

	/**
	 * @param width          验证码宽度
	 * @param height         验证码高度
	 * @param codeCount      验证码个数
	 * @param interfereCount 干扰个数
	 */
	public GifCaptcha(final int width, final int height, final int codeCount, final int interfereCount) {
		this(width, height, new RandomGenerator(codeCount), interfereCount);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param generator      验证码生成器
	 * @param interfereCount 验证码干扰元素个数
	 */
	public GifCaptcha(final int width, final int height, final CodeGenerator generator, final int interfereCount) {
		super(width, height, generator, interfereCount);
	}

	/**
	 * 设置图像的颜色量化(转换质量 由GIF规范允许的最大256种颜色)。
	 * 低的值(最小值= 1)产生更好的颜色,但处理显著缓慢。
	 * 10是默认,并产生良好的颜色而且有以合理的速度。
	 * 值更大(大于20)不产生显著的改善速度
	 *
	 * @param quality 大于1
	 * @return this
	 */
	public GifCaptcha setQuality(int quality) {
		if (quality < 1) {
			quality = 1;
		}
		this.quality = quality;
		return this;
	}

	/**
	 * 设置GIF帧应该播放的次数。
	 * 默认是 0; 0意味着无限循环。
	 * 必须在添加的第一个图像之前被调用。
	 *
	 * @param repeat 必须大于等于0
	 * @return this
	 */
	public GifCaptcha setRepeat(final int repeat) {
		if (repeat >= 0) {
			this.repeat = repeat;
		}
		return this;
	}

	/**
	 * 设置验证码字符颜色
	 *
	 * @param maxColor 颜色
	 * @return this
	 */
	public GifCaptcha setMaxColor(final int maxColor) {
		this.maxColor = maxColor;
		return this;
	}

	/**
	 * 设置验证码字符颜色
	 *
	 * @param minColor 颜色
	 * @return this
	 */
	public GifCaptcha setMinColor(final int minColor) {
		this.minColor = minColor;
		return this;
	}

	@Override
	public void createCode() {
		generateCode();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		final AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();// gif编码类
		//生成字符
		gifEncoder.start(out);
		gifEncoder.setQuality(quality);//设置量化器取样间隔
		// 帧延迟 (默认100)
		final int delay = 100;
		gifEncoder.setDelay(delay);//设置帧延迟
		gifEncoder.setRepeat(repeat);//帧循环次数
		BufferedImage frame;
		final char[] chars = code.toCharArray();
		final Color[] fontColor = new Color[chars.length];
		for (int i = 0; i < chars.length; i++) {
			fontColor[i] = getRandomColor(minColor, maxColor);
			frame = graphicsImage(chars, fontColor, chars, i);
			gifEncoder.addFrame(frame);
			frame.flush();
		}
		gifEncoder.finish();
		this.imageBytes = out.toByteArray();
	}

	@Override
	protected Image createImage(final String code) {
		return null;
	}

	/**
	 * 画随机码图
	 *
	 * @param fontColor 随机字体颜色
	 * @param words     字符数组
	 * @param flag      透明度使用
	 * @return BufferedImage
	 */
	private BufferedImage graphicsImage(final char[] chars, final Color[] fontColor, final char[] words, final int flag) {
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//或得图形上下文
		final Graphics2D g2d = image.createGraphics();
		//利用指定颜色填充背景
		g2d.setColor(ObjUtil.defaultIfNull(this.background, Color.WHITE));
		g2d.fillRect(0, 0, width, height);
		AlphaComposite ac;
		// 字符的y坐标
		final float y = (height >> 1) + (font.getSize() >> 1);
		final float m = 1.0f * (width - (chars.length * font.getSize())) / chars.length;
		//字符的x坐标
		final float x = Math.max(m / 2.0f, 2);
		g2d.setFont(font);
		// 指定透明度
		if (null != this.textAlpha) {
			g2d.setComposite(this.textAlpha);
		}
		for (int i = 0; i < chars.length; i++) {
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(chars.length, flag, i));
			g2d.setComposite(ac);
			g2d.setColor(fontColor[i]);
			g2d.drawOval(
				RandomUtil.randomInt(width),
				RandomUtil.randomInt(height),
				RandomUtil.randomInt(5, 30), 5 + RandomUtil.randomInt(5, 30)
			);//绘制椭圆边框
			g2d.drawString(words[i] + "", x + (font.getSize() + m) * i, y);
		}
		g2d.dispose();
		return image;
	}

	/**
	 * 获取透明度,从0到1,自动计算步长
	 *
	 * @return float 透明度
	 */
	private float getAlpha(final int v, final int i, final int j) {
		final int num = i + j;
		final float r = (float) 1 / v;
		final float s = (v + 1) * r;
		return num > v ? (num * r - s) : num * r;
	}

	/**
	 * 通过给定范围获得随机的颜色
	 *
	 * @return Color 获得随机的颜色
	 */
	private Color getRandomColor(int min, int max) {
		if (min > 255) {
			min = 255;
		}
		if (max > 255) {
			max = 255;
		}
		if (min < 0) {
			min = 0;
		}
		if (max < 0) {
			max = 0;
		}
		if (min > max) {
			min = 0;
			max = 255;
		}
		return new Color(
			RandomUtil.randomInt(min, max),
			RandomUtil.randomInt(min, max),
			RandomUtil.randomInt(min, max));
	}
}
