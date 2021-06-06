package cn.hutool.core.swing.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

/**
 * 剪贴板监听事件处理接口<br>
 * 用户通过实现此接口，实现监听剪贴板内容变化
 *
 * @author looly
 *@since 4.5.6
 */
public interface ClipboardListener {
	/**
	 * 剪贴板变动触发的事件方法<br>
	 * 在此事件中对剪贴板设置值无效，如若修改，需返回修改内容
	 *
	 * @param clipboard 剪贴板对象
	 * @param contents 内容
	 * @return 如果对剪贴板内容做修改，则返回修改的内容，{@code null}表示保留原内容
	 */
	Transferable onChange(Clipboard clipboard, Transferable contents);
}
