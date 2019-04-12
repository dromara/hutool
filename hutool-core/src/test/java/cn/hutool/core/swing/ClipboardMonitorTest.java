package cn.hutool.core.swing;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.swing.clipboard.ClipboardListener;

public class ClipboardMonitorTest {

	@Test
	@Ignore
	public void monitorTest() {
		ClipboardUtil.listen(new ClipboardListener() {
			
			@Override
			public Transferable onChange(Clipboard clipboard, Transferable contents) {
				Object object = ClipboardUtil.get(contents, DataFlavor.stringFlavor);
				Console.log(object);
				return contents;
			}
			
		});

	}
}
