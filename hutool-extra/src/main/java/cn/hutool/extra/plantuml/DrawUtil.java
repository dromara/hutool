package cn.hutool.extra.plantuml;


import net.sourceforge.plantuml.SourceStringReader;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

/**
 * <p>PlantUML 代码图表生成工具类，提供多种生成方式：Base64、OutputStream、本地文件。</p>
 * <p>通过传入PlantUML语法代码，生成相应的图标</p>
 * @author LGXTvT
 */
public class DrawUtil {

	/**
	 * 将给定的 PlantUML 代码生成Base64格式图片
	 * @param plantumlText plantuml代码
	 * @return Base64格式图片数据
	 */
	public static String toBase64(String plantumlText) {
		// 创建PlantUML对象
		SourceStringReader reader = new SourceStringReader(plantumlText);
		try(OutputStream os = new ByteArrayOutputStream()) {
			// 生成并保存图片
			reader.outputImage(os);
			// 转换成base64
			return convertOutputStreamToBase64Image(os, "png");
		} catch (IOException e) {
			throw new RuntimeException(e);
        }
	}

	/**
	 * 将流转换为base64数据
	 * @param outputStream 输出流
	 * @param imageFormat 转换图片格式
	 * @return base64
	 */
	private static String convertOutputStreamToBase64Image(OutputStream outputStream, String imageFormat) throws IOException {
		if (outputStream instanceof ByteArrayOutputStream) {
			ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
			// 获取字节数组
			byte[] byteArray = byteArrayOutputStream.toByteArray();
			// 使用 Base64 编码器将字节数组编码为 Base64 字符串
			String base64String = Base64.getEncoder().encodeToString(byteArray);
			// 构建 Base64 图像数据 URI
            return "data:image/" + imageFormat + ";base64," + base64String;
		} else {
			throw new IllegalArgumentException("OutputStream must be an instance of ByteArrayOutputStream");
		}
	}

	/**
	 * 将给定的 PlantUML 代码生成目标图片
	 * @param plantumlText plantuml代码
	 * @param targetFile 目标文件
	 */
	public static void toFile(String plantumlText, File targetFile) {
		// 创建PlantUML对象
		SourceStringReader reader = new SourceStringReader(plantumlText);
		// 输出文件路径
		try (OutputStream os = Files.newOutputStream(targetFile.toPath())) {
			// 生成并保存图片
			reader.outputImage(os);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将给定的 PlantUML 代码生成目标图片
	 * @param plantumlText plantuml代码
	 * @param path 目标文件路径
	 */
	public static void toFile(String plantumlText, String path) {
		toFile(plantumlText, new File(path));
	}

	/**
	 * 将给定的 PlantUML 代码生成图片并写入输出流。
	 * @param plantumlText plantuml代码
	 * @param os 输出流
	 */
	public static void toOutputStream(String plantumlText, OutputStream os) {
		// 创建PlantUML对象
		SourceStringReader reader = new SourceStringReader(plantumlText);
		try {
			// 生成并保存图片
			reader.outputImage(os);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
