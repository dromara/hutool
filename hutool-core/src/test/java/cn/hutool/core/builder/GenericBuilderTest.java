package cn.hutool.core.builder;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import static org.junit.jupiter.api.Assertions.*;
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
		Box box = GenericBuilder
				.of(Box::new)
				.with(Box::setId, 1024L)
				.with(Box::setTitle, "Hello World!")
				.with(Box::setLength, 9)
				.with(Box::setWidth, 8)
				.with(Box::setHeight, 7)
				.build();

		assertEquals(1024L, box.getId().longValue());
		assertEquals("Hello World!", box.getTitle());
		assertEquals(9, box.getLength().intValue());
		assertEquals(8, box.getWidth().intValue());
		assertEquals(7, box.getHeight().intValue());

		// 对象修改
		Box boxModified = GenericBuilder
				.of(() -> box)
				.with(Box::setTitle, "Hello Friend!")
				.with(Box::setLength, 3)
				.with(Box::setWidth, 4)
				.with(Box::setHeight, 5)
				.build();

		assertEquals(1024L, boxModified.getId().longValue());
		assertEquals("Hello Friend!", box.getTitle());
		assertEquals(3, boxModified.getLength().intValue());
		assertEquals(4, boxModified.getWidth().intValue());
		assertEquals(5, boxModified.getHeight().intValue());

		// 多参数构造
		Box box1 = GenericBuilder
				.of(Box::new, 2048L, "Hello Partner!", 222, 333, 444)
				.with(Box::alis)
				.build();

		assertEquals(2048L, box1.getId().longValue());
		assertEquals("Hello Partner!", box1.getTitle());
		assertEquals(222, box1.getLength().intValue());
		assertEquals(333, box1.getWidth().intValue());
		assertEquals(444, box1.getHeight().intValue());
		assertEquals("TomXin:\"Hello Partner!\"", box1.getTitleAlias());
	}

	@Test
	public void buildMapTest(){
		//Map创建
		HashMap<String, String> colorMap = GenericBuilder
				.of(HashMap<String,String>::new)
				.with(Map::put, "red", "#FF0000")
				.with(Map::put, "yellow", "#FFFF00")
				.with(Map::put, "blue", "#0000FF")
				.build();
		assertEquals("#FF0000", colorMap.get("red"));
		assertEquals("#FFFF00", colorMap.get("yellow"));
		assertEquals("#0000FF", colorMap.get("blue"));
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

		public Box(Long id, String title, Integer length, Integer width, Integer height) {
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
