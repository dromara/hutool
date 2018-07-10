package cn.hutool.captcha;

import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 抽象验证码<br>
 * 抽象验证码实现了验证码字符串的生成、验证，验证码图片的写出<br>
 * 实现类通过实现{@link #createImage(String)} 方法生成图片对象
 * 
 * @author looly
 *
 */
public abstract class AbstractCaptcha implements ICaptcha {
	private static final long serialVersionUID = 3180820918087507254L;

	// 图片的宽度。
	protected int width = 100;
	// 图片的高度。
	protected int height = 37;
	// 验证码干扰元素个数
	protected int interfereCount = 15;
	// 字体
	protected Font font;
	// 验证码
	protected String code;
	// 验证码图片
	protected byte[] imageBytes;
	/** 验证码生成器 */
	protected CodeGenerator generator;

	/**
	 * 构造，使用随机验证码生成器生成验证码
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 * @param codeCount 字符个数
	 * @param interfereCount 验证码干扰元素个数
	 */
	public AbstractCaptcha(int width, int height, int codeCount, int interfereCount) {
		this(width, height, new RandomGenerator(codeCount), interfereCount);
	}

	/**
	 * 构造
	 * 
	 * @param width 图片宽
	 * @param height 图片高
	 * @param generator 验证码生成器
	 * @param interfereCount 验证码干扰元素个数
	 */
	public AbstractCaptcha(int width, int height, CodeGenerator generator, int interfereCount) {
		this.width = width;
		this.height = height;
		this.generator = generator;
		this.interfereCount = interfereCount;
		// 字体高度设为验证码高度-2，留边距
		this.font = new Font("Courier", Font.PLAIN, this.height - 2);
		createCode();
	}

	@Override
	public void createCode() {
		generateCode();

		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageUtil.writePng(createImage(this.code), out);
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
	 */
	protected abstract Image createImage(String code);

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public boolean verify(String userInputCode) {
		if (StrUtil.isNotBlank(userInputCode)) {
			return StrUtil.equalsIgnoreCase(getCode(), userInputCode);
		}
		return false;
	}

	/**
	 * 验证码写出到文件
	 * 
	 * @param path 文件路径
	 * @throws IORuntimeException IO异常
	 */
	public void write(String path) throws IORuntimeException {
		this.write(FileUtil.touch(path));
	}

	/**
	 * 验证码写出到文件
	 * 
	 * @param file 文件
	 * @throws IORuntimeException IO异常
	 */
	public void write(File file) throws IORuntimeException {
		try (OutputStream out = FileUtil.getOutputStream(file)) {
			this.write(out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void write(OutputStream out) {
		IoUtil.write(out, false, this.imageBytes);
	}

	/**
	 * 获取验证码图
	 * 
	 * @return 验证码图
	 */
	public BufferedImage getImage() {
		if (null == this.imageBytes) {
			createCode();
		}
		return ImageUtil.read(new ByteArrayInputStream(this.imageBytes));
	}

	/**
	 * 获得图片的Base64形式
	 * 
	 * @return 图片的Base64
	 * @since 3.3.0
	 */
	public String getImageBase64() {
		return Base64.encode(this.imageBytes);
	}

	/**
	 * 自定义字体
	 * 
	 * @param font 字体
	 */
	public void setFont(Font font) {
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
	public void setGenerator(CodeGenerator generator) {
		this.generator = generator;
	}
}
