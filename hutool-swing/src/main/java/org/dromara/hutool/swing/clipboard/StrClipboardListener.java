/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.clipboard;

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
	public Transferable onChange(final Clipboard clipboard, final Transferable contents) {
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
