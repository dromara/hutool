package cn.hutool.core.swing;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ImageUtil;

/**
 * {@link Robot} 封装工具类，提供截屏等工具
 * 
 * @author looly
 * @since 4.1.14
 */
public class RobotUtil {
	public static final Robot robot;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 截取全屏
	 * 
	 * @return 截屏的图片
	 */
	public static BufferedImage captureScreen() {
		return captureScreen(ScreenUtil.getRectangle());
	}

	/**
	 * 截取全屏到文件
	 * 
	 * @param outFile 写出到的文件
	 * @return 写出到的文件
	 */
	public static File captureScreen(File outFile) {
		ImageUtil.write(captureScreen(), outFile);
		return outFile;
	}

	/**
	 * 截屏
	 * 
	 * @param screenRect 截屏的矩形区域
	 * @return 截屏的图片
	 */
	public static BufferedImage captureScreen(Rectangle screenRect) {
		return robot.createScreenCapture(screenRect);
	}

	/**
	 * 截屏
	 * 
	 * @param screenRect 截屏的矩形区域
	 * @param outFile 写出到的文件
	 * @return 写出到的文件
	 */
	public static File captureScreen(Rectangle screenRect, File outFile) {
		ImageUtil.write(captureScreen(screenRect), outFile);
		return outFile;
	}
}
