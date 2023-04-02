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

package org.dromara.hutool;

import org.dromara.hutool.clipboard.ClipboardUtil;
import org.dromara.hutool.exceptions.UtilException;
import org.dromara.hutool.img.ImgUtil;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * {@link Robot} 封装工具类，提供截屏等工具
 *
 * @author looly
 * @since 4.1.14
 */
public class RobotUtil {

	private static final Robot ROBOT;
	private static int delay;

	static {
		try {
			ROBOT = new Robot();
		} catch (final AWTException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 获取 Robot 单例实例
	 *
	 * @return {@link Robot}单例对象
	 * @since 5.7.6
	 */
	public static Robot getRobot() {
		return ROBOT;
	}

	/**
	 * 设置默认的延迟时间<br>
	 * 当按键执行完后的等待时间，也可以用ThreadUtil.sleep方法代替
	 *
	 * @param delayMillis 等待毫秒数
	 * @since 4.5.7
	 */
	public static void setDelay(final int delayMillis) {
		delay = delayMillis;
	}

	/**
	 * 获取全局默认的延迟时间
	 *
	 * @return 全局默认的延迟时间
	 * @since 5.7.6
	 */
	public static int getDelay() {
		return delay;
	}

	/**
	 * 模拟鼠标移动
	 *
	 * @param x 移动到的x坐标
	 * @param y 移动到的y坐标
	 * @since 4.5.7
	 */
	public static void mouseMove(final int x, final int y) {
		ROBOT.mouseMove(x, y);
	}

	/**
	 * 模拟单击<br>
	 * 鼠标单击包括鼠标左键的按下和释放
	 *
	 * @since 4.5.7
	 */
	public static void click() {
		ROBOT.mousePress(InputEvent.BUTTON1_MASK);
		ROBOT.mouseRelease(InputEvent.BUTTON1_MASK);
		delay();
	}

	/**
	 * 模拟右键单击<br>
	 * 鼠标单击包括鼠标右键的按下和释放
	 *
	 * @since 4.5.7
	 */
	public static void rightClick() {
		ROBOT.mousePress(InputEvent.BUTTON3_MASK);
		ROBOT.mouseRelease(InputEvent.BUTTON3_MASK);
		delay();
	}

	/**
	 * 模拟鼠标滚轮滚动
	 *
	 * @param wheelAmt 滚动数，负数表示向前滚动，正数向后滚动
	 * @since 4.5.7
	 */
	public static void mouseWheel(final int wheelAmt) {
		ROBOT.mouseWheel(wheelAmt);
		delay();
	}

	/**
	 * 模拟键盘点击<br>
	 * 包括键盘的按下和释放
	 *
	 * @param keyCodes 按键码列表，见{@link java.awt.event.KeyEvent}
	 * @since 4.5.7
	 */
	public static void keyClick(final int... keyCodes) {
		for (final int keyCode : keyCodes) {
			ROBOT.keyPress(keyCode);
			ROBOT.keyRelease(keyCode);
		}
		delay();
	}

	/**
	 * 打印输出指定字符串（借助剪贴板）
	 *
	 * @param str 字符串
	 */
	public static void keyPressString(final String str) {
		ClipboardUtil.setStr(str);
		keyPressWithCtrl(KeyEvent.VK_V);// 粘贴
		delay();
	}

	/**
	 * shift+ 按键
	 *
	 * @param key 按键
	 */
	public static void keyPressWithShift(final int key) {
		ROBOT.keyPress(KeyEvent.VK_SHIFT);
		ROBOT.keyPress(key);
		ROBOT.keyRelease(key);
		ROBOT.keyRelease(KeyEvent.VK_SHIFT);
		delay();
	}

	/**
	 * ctrl+ 按键
	 *
	 * @param key 按键
	 */
	public static void keyPressWithCtrl(final int key) {
		ROBOT.keyPress(KeyEvent.VK_CONTROL);
		ROBOT.keyPress(key);
		ROBOT.keyRelease(key);
		ROBOT.keyRelease(KeyEvent.VK_CONTROL);
		delay();
	}

	/**
	 * alt+ 按键
	 *
	 * @param key 按键
	 */
	public static void keyPressWithAlt(final int key) {
		ROBOT.keyPress(KeyEvent.VK_ALT);
		ROBOT.keyPress(key);
		ROBOT.keyRelease(key);
		ROBOT.keyRelease(KeyEvent.VK_ALT);
		delay();
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
	public static File captureScreen(final File outFile) {
		ImgUtil.write(captureScreen(), outFile);
		return outFile;
	}

	/**
	 * 截屏
	 *
	 * @param screenRect 截屏的矩形区域
	 * @return 截屏的图片
	 */
	public static BufferedImage captureScreen(final Rectangle screenRect) {
		return ROBOT.createScreenCapture(screenRect);
	}

	/**
	 * 截屏
	 *
	 * @param screenRect 截屏的矩形区域
	 * @param outFile 写出到的文件
	 * @return 写出到的文件
	 */
	public static File captureScreen(final Rectangle screenRect, final File outFile) {
		ImgUtil.write(captureScreen(screenRect), outFile);
		return outFile;
	}

	/**
	 * 等待指定毫秒数
	 */
	public static void delay() {
		if (delay > 0) {
			ROBOT.delay(delay);
		}
	}
}
