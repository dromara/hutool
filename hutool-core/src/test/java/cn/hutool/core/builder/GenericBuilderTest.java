package cn.hutool.core.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
	}

}
