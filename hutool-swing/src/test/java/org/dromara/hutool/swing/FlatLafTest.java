package org.dromara.hutool.swing;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.JFrame;

public class FlatLafTest {
	public static void main(String[] args) {
		FlatDarculaLaf.setup();
		final JFrame frame = new JFrame("我的窗口");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 设置窗口可见
		frame.setVisible(true);
	}
}
