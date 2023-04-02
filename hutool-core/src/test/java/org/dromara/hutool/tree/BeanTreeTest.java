package org.dromara.hutool.tree;

import org.dromara.hutool.stream.EasyStream;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * TreeHelperTest
 *
 * @author VampireAchao
 */
public class BeanTreeTest {

	@Data
	@Builder
	private static class JavaBean {
		@Tolerate
		public JavaBean() {
			// this is an accessible parameterless constructor.
		}

		private String name;
		private Integer age;
		private Long id;
		private Long parentId;
		private List<JavaBean> children;
		private Boolean matchParent;
	}

	List<JavaBean> originJavaBeanList;
	List<JavaBean> originJavaBeanTree;
	BeanTree<JavaBean, Long> beanTree;

	BeanTreeTest(){
		setUp();
	}

	public void setUp() {
		originJavaBeanList = EasyStream
				.of(
						JavaBean.builder().id(1L).name("dromara").matchParent(true).build(),
						JavaBean.builder().id(2L).name("baomidou").matchParent(true).build(),
						JavaBean.builder().id(3L).name("hutool").parentId(1L).build(),
						JavaBean.builder().id(4L).name("sa-token").parentId(1L).build(),
						JavaBean.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
						JavaBean.builder().id(6L).name("looly").parentId(3L).build(),
						JavaBean.builder().id(7L).name("click33").parentId(4L).build(),
						JavaBean.builder().id(8L).name("jobob").parentId(5L).build()
				).toList();
		originJavaBeanTree = asList(
				JavaBean.builder().id(1L).name("dromara").matchParent(true)
						.children(asList(
								JavaBean.builder().id(3L).name("hutool").parentId(1L)
										.children(singletonList(JavaBean.builder().id(6L).name("looly").parentId(3L).build()))
										.build(),
								JavaBean.builder().id(4L).name("sa-token").parentId(1L)
										.children(singletonList(JavaBean.builder().id(7L).name("click33").parentId(4L).build()))
										.build()))
						.build(),
				JavaBean.builder().id(2L).name("baomidou").matchParent(true)
						.children(singletonList(
								JavaBean.builder().id(5L).name("mybatis-plus").parentId(2L)
										.children(singletonList(
												JavaBean.builder().id(8L).name("jobob").parentId(5L).build()
										))
										.build()))
						.build()
		);
		beanTree = BeanTree.of(JavaBean::getId, JavaBean::getParentId, null, JavaBean::getChildren, JavaBean::setChildren);
	}

	@Test
	public void testToTree() {
		final List<JavaBean> javaBeanTree = beanTree.toTree(originJavaBeanList);
		Assertions.assertEquals(originJavaBeanTree, javaBeanTree);
		final BeanTree<JavaBean, Long> conditionBeanTree = BeanTree.ofMatch(JavaBean::getId, JavaBean::getParentId, s -> Boolean.TRUE.equals(s.getMatchParent()), JavaBean::getChildren, JavaBean::setChildren);
		Assertions.assertEquals(originJavaBeanTree, conditionBeanTree.toTree(originJavaBeanList));
	}

	@Test
	public void testFlat() {
		final List<JavaBean> javaBeanList = beanTree.flat(originJavaBeanTree);
		javaBeanList.sort(Comparator.comparing(JavaBean::getId));
		Assertions.assertEquals(originJavaBeanList, javaBeanList);
	}

	@Test
	public void testFilter() {
		final List<JavaBean> javaBeanTree = beanTree.filter(originJavaBeanTree, s -> "looly".equals(s.getName()));
		Assertions.assertEquals(singletonList(
						JavaBean.builder().id(1L).name("dromara").matchParent(true)
								.children(singletonList(JavaBean.builder().id(3L).name("hutool").parentId(1L)
										.children(singletonList(JavaBean.builder().id(6L).name("looly").parentId(3L).build()))
										.build()))
								.build()),
				javaBeanTree);
	}

	@Test
	public void testForeach() {
		final List<JavaBean> javaBeanList = beanTree.forEach(originJavaBeanTree, s -> s.setName("【open source】" + s.getName()));
		Assertions.assertEquals(asList(
				JavaBean.builder().id(1L).name("【open source】dromara").matchParent(true)
						.children(asList(JavaBean.builder().id(3L).name("【open source】hutool").parentId(1L)
										.children(singletonList(JavaBean.builder().id(6L).name("【open source】looly").parentId(3L).build()))
										.build(),
								JavaBean.builder().id(4L).name("【open source】sa-token").parentId(1L)
										.children(singletonList(JavaBean.builder().id(7L).name("【open source】click33").parentId(4L).build()))
										.build()))
						.build(),
				JavaBean.builder().id(2L).name("【open source】baomidou").matchParent(true)
						.children(singletonList(
								JavaBean.builder().id(5L).name("【open source】mybatis-plus").parentId(2L)
										.children(singletonList(
												JavaBean.builder().id(8L).name("【open source】jobob").parentId(5L).build()
										))
										.build()))
						.build()
		), javaBeanList);
	}

}
