/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.swing.captcha;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.swing.captcha.generator.CodeGenerator;
import org.dromara.hutool.swing.captcha.generator.RandomGenerator;
import org.dromara.hutool.swing.img.ImgUtil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 抽象验证码<br>
 * 抽象验证码实现了验证码字符串的生成、验证，验证码图片的写出<br>
 * 实现类通过实现{@link #createImage(String)} 方法生成图片对象
 *
 * @author looly
 */
public abstract class AbstractCaptcha implements ICaptcha {
	private static final long serialVersionUID = 3180820918087507254L;

	/**
	 * 图片的宽度
	 */
	protected int width;
	/**
	 * 图片的高度
	 */
	protected int height;
	/**
	 * 验证码干扰元素个数
	 */
	protected int interfereCount;
	/**
	 * 字体
	 */
	protected Font font;
	/**
	 * 验证码
	 */
	protected String code;
	/**
	 * 验证码图片
	 */
	protected byte[] imageBytes;
	/**
	 * 验证码生成器
	 */
	protected CodeGenerator generator;
	/**
	 * 背景色
	 */
	protected Color background = Color.WHITE;
	/**
	 * 文字透明度
	 */
	protected AlphaComposite textAlpha;

	/**
	 * 构造，使用随机验证码生成器生成验证码
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param codeCount      字符个数
	 * @param interfereCount 验证码干扰元素个数
	 */
	public AbstractCaptcha(final int width, final int height, final int codeCount, final int interfereCount) {
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
	public AbstractCaptcha(final int width, final int height, final CodeGenerator generator, final int interfereCount) {
		this(width, height, generator, interfereCount, 0.75f);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param generator      验证码生成器
	 * @param interfereCount 验证码干扰元素个数
	 * @param sizeBaseHeight 字体的大小 高度的倍数
	 */
	public AbstractCaptcha(final int width, final int height, final CodeGenerator generator, final int interfereCount, final float sizeBaseHeight) {
		this.width = width;
		this.height = height;
		this.generator = generator;
		this.interfereCount = interfereCount;
		// 字体高度设为验证码高度-2，留边距
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, (int) (this.height * sizeBaseHeight));
	}

	@Override
	public void createCode() {
		generateCode();

		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		Image image = null;
		try {
			image = createImage(this.code);
			ImgUtil.writePng(image, out);
		} finally {
			ImgUtil.flush(image);
		}

		this.imageBytes = out.toByteArray();
	}

	/**
	 * 生成验证码字符串
	 *
	 * @since 3.3.0
	 */
	protected void generateCode() {
		this.code = generator.generate();
	}

	/**
	 * 根据生成的code创建验证码图片
	 *
	 * @param code 验证码
	 * @return Image
	 */
	protected abstract Image createImage(String code);

	@Override
	public String getCode() {
		if (null == this.code) {
			createCode();
		}
		return this.code;
	}

	@Override
	public boolean verify(final String userInputCode) {
		return this.generator.verify(getCode(), userInputCode);
	}

	/**
	 * 验证码写出到文件
	 *
	 * @param path 文件路径
	 * @throws IORuntimeException IO异常
	 */
	public void write(final String path) throws IORuntimeException {
		this.write(FileUtil.touch(path));
	}

	/**
	 * 验证码写出到文件
	 *
	 * @param file 文件
	 * @throws IORuntimeException IO异常
	 */
	public void write(final File file) throws IORuntimeException {
		try (final OutputStream out = FileUtil.getOutputStream(file)) {
			this.write(out);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void write(final OutputStream out) {
		IoUtil.write(out, false, getImageBytes());
	}

	/**
	 * 获取图形验证码图片bytes
	 *
	 * @return 图形验证码图片bytes
	 * @since 4.5.17
	 */
	public byte[] getImageBytes() {
		if (null == this.imageBytes) {
			createCode();
		}
		return this.imageBytes;
	}

	/**
	 * 获取验证码图<br>
	 * 注意返回的{@link BufferedImage}使用完毕后需要调用{@link BufferedImage#flush()}释放资源
	 *
	 * @return 验证码图
	 */
	public BufferedImage getImage() {
		return ImgUtil.read(IoUtil.toStream(getImageBytes()));
	}

	/**
	 * 获得图片的Base64形式
	 *
	 * @return 图片的Base64
	 * @since 3.3.0
	 */
	public String getImageBase64() {
		return Base64.encode(getImageBytes());
	}

	/**
	 * 获取图片带文件格式的 Base64
	 *
	 * @return 图片带文件格式的 Base64
	 * @since 5.3.11
	 */
	public String getImageBase64Data() {
		return UrlUtil.getDataUriBase64("image/png", getImageBase64());
	}

	/**
	 * 自定义字体
	 *
	 * @param font 字体
	 */
	public void setFont(final Font font) {
		this.font = font;
	}

	/**
	 * 获取验证码生成器
	 *
	 * @return 验证码生成器
	 */
	public CodeGenerator getGenerator() {
		return generator;
	}

	/**
	 * 设置验证码生成器
	 *
	 * @param generator 验证码生成器
	 */
	public void setGenerator(final CodeGenerator generator) {
		this.generator = generator;
	}

	/**
	 * 设置背景色，{@code null}表示透明背景
	 *
	 * @param background 背景色
	 * @since 4.1.22
	 */
	public void setBackground(final Color background) {
		this.background = background;
	}

	/**
	 * 设置文字透明度
	 *
	 * @param textAlpha 文字透明度，取值0~1，1表示不透明
	 * @since 4.5.17
	 */
	public void setTextAlpha(final float textAlpha) {
		this.textAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, textAlpha);
	}

}
