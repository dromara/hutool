package cn.hutool.core.img;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>图片背景识别处理、背景替换、背景设置为矢量图</p>
 * <p>根据一定规则算出图片背景色的RGB值，进行替换</p>
 * <p>2020-05-21 16:36</p>
 *
 * @author Dai Yuanchuan
 **/
public class BackgroundRemoval {

	/**
	 * 目前暂时支持的图片类型数组
	 * 其他格式的不保证结果
	 */
	public static String[] IMAGES_TYPE = {"jpg", "png"};

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param inputPath  要处理图片的路径
	 * @param outputPath 输出图片的路径
	 * @param tolerance  容差值[根据图片的主题色,加入容差值,值的范围在0~255之间]
	 * @return 返回处理结果 true:图片处理完成 false:图片处理失败
	 */
	public static boolean backgroundRemoval(String inputPath, String outputPath, int tolerance) {
		return backgroundRemoval(new File(inputPath), new File(outputPath), tolerance);
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param input     需要进行操作的图片
	 * @param output    最后输出的文件
	 * @param tolerance 容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理结果 true:图片处理完成 false:图片处理失败
	 */
	public static boolean backgroundRemoval(File input, File output, int tolerance) {
		return backgroundRemoval(input, output, null, tolerance);
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param input     需要进行操作的图片
	 * @param output    最后输出的文件
	 * @param override  指定替换成的背景颜色 为null时背景为透明
	 * @param tolerance 容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理结果 true:图片处理完成 false:图片处理失败
	 */
	public static boolean backgroundRemoval(File input, File output, Color override, int tolerance) {
		if (fileTypeValidation(input, IMAGES_TYPE)) {
			return false;
		}
		try {
			// 获取图片左上、中上、右上、右中、右下、下中、左下、左中、8个像素点rgb的16进制值
			BufferedImage bufferedImage = ImageIO.read(input);
			// 图片输出的格式为 png
			return ImageIO.write(backgroundRemoval(bufferedImage, override, tolerance), "png", output);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param bufferedImage 需要进行处理的图片流
	 * @param override      指定替换成的背景颜色 为null时背景为透明
	 * @param tolerance     容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理好的图片流
	 */
	public static BufferedImage backgroundRemoval(BufferedImage bufferedImage, Color override, int tolerance) {
		// 容差值 最大255 最小0
		tolerance = Math.min(255, Math.max(tolerance, 0));
		// 绘制icon
		ImageIcon imageIcon = new ImageIcon(bufferedImage);
		BufferedImage image = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		// 绘图工具
		Graphics graphics = image.getGraphics();
		graphics.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
		// 需要删除的RGB元素
		String[] removeRgb = getRemoveRgb(bufferedImage);
		// 获取图片的大概主色调
		String mainColor = getMainColor(bufferedImage);
		int alpha = 0;
		for (int y = image.getMinY(); y < image.getHeight(); y++) {
			for (int x = image.getMinX(); x < image.getWidth(); x++) {
				// 获取像素的16进制
				int rgb = image.getRGB(x, y);
				String hex = ImgUtil.toHex((rgb & 0xff0000) >> 16, (rgb & 0xff00) >> 8, (rgb & 0xff));
				boolean isTrue = ArrayUtil.contains(removeRgb, hex) ||
						areColorsWithinTolerance(hexToRgb(mainColor), new Color(Integer.parseInt(hex.substring(1), 16)), tolerance);
				if (isTrue) {
					rgb = override == null ? ((alpha + 1) << 24) | (rgb & 0x00ffffff) : override.getRGB();
				}
				image.setRGB(x, y, rgb);
			}
		}
		graphics.drawImage(image, 0, 0, imageIcon.getImageObserver());
		return image;
	}

	/**
	 * 背景移除
	 * 图片去底工具
	 * 将 "纯色背景的图片" 还原成 "透明背景的图片"
	 * 将纯色背景的图片转成矢量图
	 * 取图片边缘的像素点和获取到的图片主题色作为要替换的背景色
	 * 再加入一定的容差值,然后将所有像素点与该颜色进行比较
	 * 发现相同则将颜色不透明度设置为0,使颜色完全透明.
	 *
	 * @param outputStream 需要进行处理的图片字节数组流
	 * @param override     指定替换成的背景颜色 为null时背景为透明
	 * @param tolerance    容差值[根据图片的主题色,加入容差值,值的取值范围在0~255之间]
	 * @return 返回处理好的图片流
	 */
	public static BufferedImage backgroundRemoval(ByteArrayOutputStream outputStream, Color override, int tolerance) {
		try {
			return backgroundRemoval(ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray())), override, tolerance);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取要删除的 RGB 元素
	 * 分别获取图片左上、中上、右上、右中、右下、下中、左下、左中、8个像素点rgb的16进制值
	 *
	 * @param image 图片流
	 * @return String数组 包含 各个位置的rgb数值
	 */
	private static String[] getRemoveRgb(BufferedImage image) {
		// 获取图片流的宽和高
		int width = image.getWidth() - 1;
		int height = image.getHeight() - 1;
		// 左上
		int leftUpPixel = image.getRGB(1, 1);
		String leftUp = ImgUtil.toHex((leftUpPixel & 0xff0000) >> 16, (leftUpPixel & 0xff00) >> 8, (leftUpPixel & 0xff));
		// 上中
		int upMiddlePixel = image.getRGB(width / 2, 1);
		String upMiddle = ImgUtil.toHex((upMiddlePixel & 0xff0000) >> 16, (upMiddlePixel & 0xff00) >> 8, (upMiddlePixel & 0xff));
		// 右上
		int rightUpPixel = image.getRGB(width, 1);
		String rightUp = ImgUtil.toHex((rightUpPixel & 0xff0000) >> 16, (rightUpPixel & 0xff00) >> 8, (rightUpPixel & 0xff));
		// 右中
		int rightMiddlePixel = image.getRGB(width, height / 2);
		String rightMiddle = ImgUtil.toHex((rightMiddlePixel & 0xff0000) >> 16, (rightMiddlePixel & 0xff00) >> 8, (rightMiddlePixel & 0xff));
		// 右下
		int lowerRightPixel = image.getRGB(width, height);
		String lowerRight = ImgUtil.toHex((lowerRightPixel & 0xff0000) >> 16, (lowerRightPixel & 0xff00) >> 8, (lowerRightPixel & 0xff));
		// 下中
		int lowerMiddlePixel = image.getRGB(width / 2, height);
		String lowerMiddle = ImgUtil.toHex((lowerMiddlePixel & 0xff0000) >> 16, (lowerMiddlePixel & 0xff00) >> 8, (lowerMiddlePixel & 0xff));
		// 左下
		int leftLowerPixel = image.getRGB(1, height);
		String leftLower = ImgUtil.toHex((leftLowerPixel & 0xff0000) >> 16, (leftLowerPixel & 0xff00) >> 8, (leftLowerPixel & 0xff));
		// 左中
		int leftMiddlePixel = image.getRGB(1, height / 2);
		String leftMiddle = ImgUtil.toHex((leftMiddlePixel & 0xff0000) >> 16, (leftMiddlePixel & 0xff00) >> 8, (leftMiddlePixel & 0xff));
		// 需要删除的RGB元素
		return new String[]{leftUp, upMiddle, rightUp, rightMiddle, lowerRight, lowerMiddle, leftLower, leftMiddle};
	}

	/**
	 * 十六进制颜色码转RGB颜色值
	 *
	 * @param hex 十六进制颜色码
	 * @return 返回 RGB颜色值
	 */
	public static Color hexToRgb(String hex) {
		return new Color(Integer.parseInt(hex.substring(1), 16));
	}


	/**
	 * 判断颜色是否在容差范围内
	 * 对比两个颜色的相似度，判断这个相似度是否小于 tolerance 容差值
	 *
	 * @param color1    颜色1
	 * @param color2    颜色2
	 * @param tolerance 容差值
	 * @return 返回true:两个颜色在容差值之内 false: 不在
	 */
	public static boolean areColorsWithinTolerance(Color color1, Color color2, int tolerance) {
		return areColorsWithinTolerance(color1, color2, new Color(tolerance, tolerance, tolerance));
	}

	/**
	 * 判断颜色是否在容差范围内
	 * 对比两个颜色的相似度，判断这个相似度是否小于 tolerance 容差值
	 *
	 * @param color1    颜色1
	 * @param color2    颜色2
	 * @param tolerance 容差色值
	 * @return 返回true:两个颜色在容差值之内 false: 不在
	 */
	public static boolean areColorsWithinTolerance(Color color1, Color color2, Color tolerance) {
		return (color1.getRed() - color2.getRed() < tolerance.getRed() && color1
				.getRed() - color2.getRed() > -tolerance.getRed())
				&& (color1.getBlue() - color2.getBlue() < tolerance
				.getBlue() && color1.getBlue() - color2.getBlue() > -tolerance
				.getBlue())
				&& (color1.getGreen() - color2.getGreen() < tolerance
				.getGreen() && color1.getGreen()
				- color2.getGreen() > -tolerance.getGreen());
	}

	/**
	 * 获取图片大概的主题色
	 * 循环所有的像素点,取出出现次数最多的一个像素点的RGB值
	 *
	 * @param input 图片文件路径
	 * @return 返回一个图片的大概的色值 一个16进制的颜色码
	 */
	public static String getMainColor(String input) {
		return getMainColor(new File(input));
	}

	/**
	 * 获取图片大概的主题色
	 * 循环所有的像素点,取出出现次数最多的一个像素点的RGB值
	 *
	 * @param input 图片文件
	 * @return 返回一个图片的大概的色值 一个16进制的颜色码
	 */
	public static String getMainColor(File input) {
		try {
			return getMainColor(ImageIO.read(input));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取图片大概的主题色
	 * 循环所有的像素点,取出出现次数最多的一个像素点的RGB值
	 *
	 * @param bufferedImage 图片流
	 * @return 返回一个图片的大概的色值 一个16进制的颜色码
	 */
	public static String getMainColor(BufferedImage bufferedImage) {
		if (bufferedImage == null) {
			throw new IllegalArgumentException("图片流是空的");
		}

		// 存储图片的所有RGB元素
		List<String> list = new ArrayList<>();
		for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
			for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
				int pixel = bufferedImage.getRGB(x, y);
				list.add(((pixel & 0xff0000) >> 16) + "-" + ((pixel & 0xff00) >> 8) + "-" + (pixel & 0xff));
			}
		}

		Map<String, Integer> map = new HashMap<>(list.size());
		for (String string : list) {
			Integer integer = map.get(string);
			if (integer == null) {
				integer = 1;
			} else {
				integer++;
			}
			map.put(string, integer);
		}
		String max = "";
		long num = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			String key = entry.getKey();
			Integer temp = entry.getValue();
			if (StrUtil.isBlank(max) || temp > num) {
				max = key;
				num = temp;
			}
		}
		String[] strings = max.split("-");
		// rgb 的数量只有3个
		int rgbLength = 3;
		if (strings.length == rgbLength) {
			return ImgUtil.toHex(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
					Integer.parseInt(strings[2]));
		}
		return "";
	}

	// -------------------------------------------------------------------------- private

	/**
	 * 文件类型验证
	 * 根据给定文件类型数据，验证给定文件类型.
	 *
	 * @param input      需要进行验证的文件
	 * @param imagesType 文件包含的类型数组
	 * @return 返回布尔值 false:给定文件的文件类型在文件数组中  true:给定文件的文件类型 不在给定数组中。
	 */
	private static boolean fileTypeValidation(File input, String[] imagesType) {
		if (!input.exists()) {
			throw new IllegalArgumentException("给定文件为空");
		}
		// 获取图片类型
		String type = FileTypeUtil.getType(input);
		// 类型对比
		if (!ArrayUtil.contains(imagesType, type)) {
			throw new IllegalArgumentException(StrUtil.format("文件类型{}不支持", type));
		}
		return false;
	}
}