package cn.hutool.poi.ofd;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.PathUtil;
import org.ofdrw.font.Font;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.edit.Annotation;
import org.ofdrw.layout.element.Div;
import org.ofdrw.layout.element.Img;
import org.ofdrw.layout.element.Paragraph;
import org.ofdrw.reader.OFDReader;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;

/**
 * OFD文件生成器
 *
 * @author looly
 * @since 5.5.3
 */
public class OfdWriter implements Serializable, Closeable {
	private static final long serialVersionUID = 1L;

	private final OFDDoc doc;

	/**
	 * 构造
	 *
	 * @param file 生成的文件
	 */
	public OfdWriter(File file) {
		this(file.toPath());
	}

	/**
	 * 构造
	 *
	 * @param file 生成的文件
	 */
	public OfdWriter(Path file) {
		try {
			if(PathUtil.exists(file, true)){
				this.doc = new OFDDoc(new OFDReader(file), file);
			} else{
				this.doc = new OFDDoc(file);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 构造
	 *
	 * @param out 需要输出的流
	 */
	public OfdWriter(OutputStream out) {
		this.doc = new OFDDoc(out);
	}

	/**
	 * 增加文本内容
	 *
	 * @param font  字体
	 * @param texts 文本
	 * @return this
	 */
	public OfdWriter addText(Font font, String... texts) {
		final Paragraph paragraph = new Paragraph();
		if (null != font) {
			paragraph.setDefaultFont(font);
		}
		for (String text : texts) {
			paragraph.add(text);
		}
		return add(paragraph);
	}

	/**
	 * 追加图片
	 *
	 * @param picFile 图片文件
	 * @param width   宽度
	 * @param height  高度
	 * @return this
	 */
	public OfdWriter addPicture(File picFile, int width, int height) {
		return addPicture(picFile.toPath(), width, height);
	}

	/**
	 * 追加图片
	 *
	 * @param picFile 图片文件
	 * @param width   宽度
	 * @param height  高度
	 * @return this
	 */
	public OfdWriter addPicture(Path picFile, int width, int height) {
		final Img img;
		try {
			img = new Img(width, height, picFile);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return add(img);
	}

	/**
	 * 增加节点
	 *
	 * @param div 节点，可以是段落、Canvas、Img或者填充
	 * @return this
	 */
	public OfdWriter add(Div div) {
		this.doc.add(div);
		return this;
	}

	/**
	 * 增加注释，比如水印等
	 *
	 * @param page 页码
	 * @param annotation 节点，可以是段落、Canvas、Img或者填充
	 * @return this
	 */
	public OfdWriter add(int page, Annotation annotation) {
		try {
			this.doc.addAnnotation(page, annotation);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public void close() {
		IoUtil.close(this.doc);
	}
}
