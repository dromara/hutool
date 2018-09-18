package cn.hutool.core.swing;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import cn.hutool.core.exceptions.UtilException;

/**
 * 系统剪贴板工具类
 * 
 * @author looly
 * @since 3.2.0
 */
public class ClipboardUtil {

	/**
	 * 获取系统剪贴板
	 * 
	 * @return {@link Clipboard}
	 */
	public static Clipboard getClipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	/**
	 * 设置内容到剪贴板
	 * 
	 * @param contents 内容
	 */
	public static void set(Transferable contents) {
		set(contents, null);
	}

	/**
	 * 设置内容到剪贴板
	 * 
	 * @param contents 内容
	 * @param owner 所有者
	 */
	public static void set(Transferable contents, ClipboardOwner owner) {
		getClipboard().setContents(contents, owner);
	}

	/**
	 * 获取剪贴板内容
	 * 
	 * @param flavor 数据元信息，标识数据类型
	 * @return 剪贴板内容，类型根据flavor不同而不同
	 */
	public static Object get(DataFlavor flavor) {
		return get(flavor, null);
	}

	/**
	 * 获取剪贴板内容
	 * 
	 * @param flavor 数据元信息，标识数据类型
	 * @param owner 所有者
	 * @return 剪贴板内容，类型根据flavor不同而不同
	 */
	public static Object get(DataFlavor flavor, ClipboardOwner owner) {
		final Transferable content = getClipboard().getContents(null);
		if (null != content && content.isDataFlavorSupported(flavor)) {
			try {
				return content.getTransferData(flavor);
			} catch (UnsupportedFlavorException | IOException e) {
				throw new UtilException(e);
			}
		}
		return null;
	}

	/**
	 * 设置字符串文本到剪贴板
	 * 
	 * @param text 字符串文本
	 */
	public static void setStr(String text) {
		set(new StringSelection(text));
	}

	/**
	 * 从剪贴板获取文本
	 * 
	 * @return 文本
	 */
	public static String getStr() {
		return (String) get(DataFlavor.stringFlavor);
	}

	/**
	 * 设置图片到剪贴板
	 * 
	 * @param image 图像
	 */
	public static void setImage(Image image) {
		set(new ImageSelection(image), null);
	}

	/**
	 * 从剪贴板获取图片
	 * 
	 * @return 图片{@link Image}
	 */
	public static Image getImage() {
		return (Image) get(DataFlavor.imageFlavor);
	}

	/**
	 * 图片转换器，用于将图片对象转换为剪贴板支持的对象<br>
	 * 此对象也用于将图像文件和{@link DataFlavor#imageFlavor} 元信息对应
	 * 
	 * @author looly
	 * @since 3.2.0
	 */
	public static class ImageSelection implements Transferable {
		private Image image;

		/**
		 * 构造
		 * 
		 * @param image 图片
		 */
		public ImageSelection(Image image) {
			this.image = image;
		}

		/**
		 * 获取元数据类型信息
		 * 
		 * @return 元数据类型列表
		 */
		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		/**
		 * 是否支持指定元数据类型
		 * 
		 * @param flavor 元数据类型
		 * @return 是否支持
		 */
		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.imageFlavor.equals(flavor);
		}

		/**
		 * 获取图片
		 * 
		 * @param flavor 元数据类型
		 * @return 转换后的对象
		 */
		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (false == DataFlavor.imageFlavor.equals(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return image;
		}
	}
}
