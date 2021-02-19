package cn.hutool.core.swing;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 屏幕相关（当前显示设置）工具类
 * 
 * @author looly
 * @since 4.1.14
 */
public class ScreenUtil {
	public static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * 获取屏幕宽度
	 * 
	 * @return 屏幕宽度
	 */
	public static int getWidth() {
		return (int) dimension.getWidth();
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return 屏幕高度
	 */
	public static int getHeight() {
		return (int) dimension.getHeight();
	}
	
	/**
	 * 获取屏幕的矩形
	 * @return 屏幕的矩形
	 */
	public static Rectangle getRectangle() {
		return new Rectangle(getWidth(), getHeight());
	}
	
	//-------------------------------------------------------------------------------------------- 截屏
	/**
	 * 截取全屏
	 * 
	 * @return 截屏的图片
	 * @see RobotUtil#captureScreen()
	 */
	public static BufferedImage captureScreen() {
		return RobotUtil.captureScreen();
	}

	/**
	 * 截取全屏到文件
	 * 
	 * @param outFile 写出到的文件
	 * @return 写出到的文件
	 * @see RobotUtil#captureScreen(File)
	 */
	public static File captureScreen(File outFile) {
		return RobotUtil.captureScreen(outFile);
	}

	/**
	 * 截屏
	 * 
	 * @param screenRect 截屏的矩形区域
	 * @return 截屏的图片
	 * @see RobotUtil#captureScreen(Rectangle)
	 */
	public static BufferedImage captureScreen(Rectangle screenRect) {
		return RobotUtil.captureScreen(screenRect);
	}

	/**
	 * 截屏
	 * 
	 * @param screenRect 截屏的矩形区域
	 * @param outFile 写出到的文件
	 * @return 写出到的文件
	 * @see RobotUtil#captureScreen(Rectangle, File)
	 */
	public static File captureScreen(Rectangle screenRect, File outFile) {
		return RobotUtil.captureScreen(screenRect, outFile);
	}
}
