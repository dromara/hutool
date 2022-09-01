package cn.hutool.poi.excel.model;

/**
 * Excel的模型标题封装类
 *
 * @author dningcheng
 * @since 5.8.6
 */
public class Title {

	private Integer index;
	private String field;
	private String title;

	public Title() {
	}

	public Title(Integer index, String field, String title) {
		this.index = index;
		this.field = field;
		this.title = title;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
