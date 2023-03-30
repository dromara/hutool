package cn.hutool.core.lang.builder;

import cn.hutool.core.text.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link GenericBuilder} 单元测试类
 *
 * @author TomXin
 */
public class GenericBuilderTest {

	@Test
	public void test() {
		final Box box = GenericBuilder
				.of(Box::new)
				.with(Box::setId, 1024L)
				.with(Box::setTitle, "Hello World!")
				.with(Box::setLength, 9)
				.with(Box::setWidth, 8)
				.with(Box::setHeight, 7)
				.build();

		Assertions.assertEquals(1024L, box.getId().longValue());
		Assertions.assertEquals("Hello World!", box.getTitle());
		Assertions.assertEquals(9, box.getLength().intValue());
		Assertions.assertEquals(8, box.getWidth().intValue());
		Assertions.assertEquals(7, box.getHeight().intValue());

		// 对象修改
		final Box boxModified = GenericBuilder
				.of(() -> box)
				.with(Box::setTitle, "Hello Friend!")
				.with(Box::setLength, 3)
				.with(Box::setWidth, 4)
				.with(Box::setHeight, 5)
				.build();

		Assertions.assertEquals(1024L, boxModified.getId().longValue());
		Assertions.assertEquals("Hello Friend!", box.getTitle());
		Assertions.assertEquals(3, boxModified.getLength().intValue());
		Assertions.assertEquals(4, boxModified.getWidth().intValue());
		Assertions.assertEquals(5, boxModified.getHeight().intValue());

		// 多参数构造
		final Box box1 = GenericBuilder
				.of(() -> new Box(2048L, "Hello Partner!", 222, 333, 444))
				.with(Box::alis)
				.build();

		Assertions.assertEquals(2048L, box1.getId().longValue());
		Assertions.assertEquals("Hello Partner!", box1.getTitle());
		Assertions.assertEquals(222, box1.getLength().intValue());
		Assertions.assertEquals(333, box1.getWidth().intValue());
		Assertions.assertEquals(444, box1.getHeight().intValue());
		Assertions.assertEquals("TomXin:\"Hello Partner!\"", box1.getTitleAlias());
	}

	@Test
	public void buildMapTest() {
		//Map创建
		final HashMap<String, String> colorMap = GenericBuilder
				.of(HashMap<String, String>::new)
				.with(Map::put, "red", "#FF0000")
				.with(Map::put, "yellow", "#FFFF00")
				.with(Map::put, "blue", "#0000FF")
				.build();
		Assertions.assertEquals("#FF0000", colorMap.get("red"));
		Assertions.assertEquals("#FFFF00", colorMap.get("yellow"));
		Assertions.assertEquals("#0000FF", colorMap.get("blue"));
	}

	@Getter
	@Setter
	@ToString
	@Accessors(chain = true)
	public static class Box {
		private Long id;
		private String title;
		private Integer length;
		private Integer width;
		private Integer height;
		private String titleAlias;

		public Box() {
		}

		public Box(final Long id, final String title, final Integer length, final Integer width, final Integer height) {
			this.id = id;
			this.title = title;
			this.length = length;
			this.width = width;
			this.height = height;
		}

		public void alis() {
			if (StrUtil.isNotBlank(this.title)) {
				this.titleAlias = "TomXin:\"" + title + "\"";
			}
		}
	}

}
