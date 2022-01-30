package cn.hutool.core.builder;

import cn.hutool.core.util.StrUtil;
import lombok.*;
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
		System.out.println(box);
		Box boxModified = GenericBuilder
				.of(() -> box)
				.with(Box::setTitle, "Hello Friend!")
				.with(Box::setLength, 3)
				.with(Box::setWidth, 4)
				.with(Box::setHeight, 5)
				.build();
		System.out.println(boxModified);
		Box box1 = GenericBuilder
				.of(Box::new, 2048L, "Hello Partner!", 222, 333, 444)
				.with(Box::alis)
				.build();
		System.out.println(box1);
	}

	@Getter
	@Setter
	@ToString
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
