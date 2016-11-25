package com.xiaoleilu.hutool.demo;

import java.awt.Color;
import java.awt.Font;

import org.apache.velocity.texen.util.FileUtil;

import com.xiaoleilu.hutool.util.ImageUtil;

/**
 * 图像工具类样例
 * @author Looly
 *
 */
public class ImageUtilDemo {
	/**
	 * 程序入口：用于测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 1-缩放图像：
		// 方法一：按比例缩放
		ImageUtil.scale(FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/abc_scale.jpg"), 2, true);// 测试OK
		// 方法二：按高度和宽度缩放
		ImageUtil.scale(FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/abc_scale2.jpg"), 500, 300, Color.WHITE);// 测试OK

		// 2-切割图像：
		// 方法一：按指定起点坐标和宽高切割
		ImageUtil.cut(FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/abc_cut.jpg"), 0, 0, 400, 400);// 测试OK
		// 方法二：指定切片的行数和列数
		ImageUtil.cutByRowsAndCols(FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/"), 2, 2);// 测试OK
		// 方法三：指定切片的宽度和高度
		ImageUtil.cut(FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/"), 300, 300);// 测试OK

		// 3-图像类型转换：
		ImageUtil.convert(FileUtil.file("e:/abc.jpg"), ImageUtil.IMAGE_TYPE_GIF, FileUtil.file("e:/abc_convert.gif"));// 测试OK

		// 4-彩色转黑白：
		ImageUtil.gray(FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/abc_gray.jpg"));// 测试OK

		// 5-给图片添加文字水印：
		// 方法一：
		ImageUtil.pressText("我是水印文字", FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/abc_pressText.jpg"), "宋体", Font.BOLD, Color.RED, 80, 0, 0, 0.5f);// 测试OK
		// 方法二：
		ImageUtil.pressText("我也是水印文字", FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/abc_pressText2.jpg"), "黑体", 36, Color.BLUE, 80, 0, 0, 0.5f);// 测试OK

		// 6-给图片添加图片水印：
		ImageUtil.pressImage(FileUtil.file("e:/abc2.jpg"), FileUtil.file("e:/abc.jpg"), FileUtil.file("e:/abc_pressImage.jpg"), 10, 50, 0.5f);// 测试OK
	}
}
