package cn.hutool.core.swing;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.img.ImgUtil;

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
	 * 模拟鼠标移动
	 * 
	 * @param x 移动到的x坐标
	 * @param y 移动到的y坐标
	 * @since 4.5.7
	 */
	public static void mouseMove(int x, int y) {
		robot.mouseMove(x, y);
	}

	/**
	 * 模拟单击<br>
	 * 鼠标单击包括鼠标左键的按下和释放
	 * 
	 * @since 4.5.7
	 */
	public static void click() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	/**
	 * 模拟右键单击<br>
	 * 鼠标单击包括鼠标右键的按下和释放
	 * 
	 * @since 4.5.7
	 */
	public static void rightClick() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	/**
	 * 模拟鼠标滚轮滚动
	 * 
	 * @param wheelAmt 滚动数，负数表示向前滚动，正数向后滚动
	 * @since 4.5.7
	 */
	public static void mouseWheel(int wheelAmt) {
		robot.mouseWheel(wheelAmt);
	}

	/**
	 * 模拟键盘点击<br>
	 * 包括键盘的按下和释放
	 * 
	 * @param keyCodes 按键码列表，见{@link java.awt.event.KeyEvent}
	 * @since 4.5.7
	 */
	public static void keyClick(int... keyCodes) {
		for (int keyCode : keyCodes) {
			robot.keyPress(keyCode);
			robot.keyRelease(keyCode);
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
		ImgUtil.write(captureScreen(), outFile);
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
		ImgUtil.write(captureScreen(screenRect), outFile);
		return outFile;
	}
}
