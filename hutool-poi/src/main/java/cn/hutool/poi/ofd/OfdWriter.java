package cn.hutool.poi.ofd;

import cn.hutool.core.io.IoUtil;
import org.ofdrw.font.Font;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.element.Div;
import org.ofdrw.layout.element.Paragraph;

import java.io.Closeable;
import java.io.Serializable;
import java.nio.file.Path;

public class OfdWriter implements Serializable, Closeable {
	private static final long serialVersionUID = 1L;

	private final Path destFile;
	private final OFDDoc doc;

	public OfdWriter(Path file){
		this.destFile = file;
		this.doc = new OFDDoc(file);
	}

	public OfdWriter addText(Font font, String... texts){
		final Paragraph paragraph = new Paragraph();
		paragraph.setDefaultFont(font);
		for (String text : texts) {
			paragraph.add(text);
		}
		return add(paragraph);
	}

	public OfdWriter add(Div div){
		this.doc.add(div);
		return this;
	}

	@Override
	public void close() {
		IoUtil.close(this.doc);
	}
}
