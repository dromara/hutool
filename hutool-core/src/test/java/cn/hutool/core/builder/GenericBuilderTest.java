package cn.hutool.core.builder;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.junit.Assert;
import org.junit.Test;

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

		Assert.assertEquals(1024L, box.getId().longValue());
		Assert.assertEquals("Hello World!", box.getTitle());
		Assert.assertEquals(9, box.getLength().intValue());
		Assert.assertEquals(8, box.getWidth().intValue());
		Assert.assertEquals(7, box.getHeight().intValue());

		// 对象修改
		Box boxModified = GenericBuilder
				.of(() -> box)
				.with(Box::setTitle, "Hello Friend!")
				.with(Box::setLength, 3)
				.with(Box::setWidth, 4)
				.with(Box::setHeight, 5)
				.build();

		Assert.assertEquals(1024L, boxModified.getId().longValue());
		Assert.assertEquals("Hello Friend!", box.getTitle());
		Assert.assertEquals(3, boxModified.getLength().intValue());
		Assert.assertEquals(4, boxModified.getWidth().intValue());
		Assert.assertEquals(5, boxModified.getHeight().intValue());

		// 多参数构造
		Box box1 = GenericBuilder
				.of(Box::new, 2048L, "Hello Partner!", 222, 333, 444)
				.with(Box::alis)
				.build();

		Assert.assertEquals(2048L, box1.getId().longValue());
		Assert.assertEquals("Hello Partner!", box1.getTitle());
		Assert.assertEquals(222, box1.getLength().intValue());
		Assert.assertEquals(333, box1.getWidth().intValue());
		Assert.assertEquals(444, box1.getHeight().intValue());
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
