package cn.hutool.core.swing.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.Serializable;

/**
 * 剪贴板字符串内容监听
 *
 * @author looly
 * @since 4.5.7
 */
public abstract class StrClipboardListener implements ClipboardListener, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Transferable onChange(Clipboard clipboard, Transferable contents) {
		if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return onChange(clipboard, ClipboardUtil.getStr(contents));
		}
		return null;
	}

	/**
	 * 剪贴板变动触发的事件方法<br>
	 * 在此事件中对剪贴板设置值无效，如若修改，需返回修改内容
	 *
	 * @param clipboard 剪贴板对象
	 * @param contents 内容
	 * @return 如果对剪贴板内容做修改，则返回修改的内容，{@code null}表示保留原内容
	 */
	public abstract Transferable onChange(Clipboard clipboard, String contents);
}
