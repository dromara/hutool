package cn.hutool.core.bean;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link DynaBean}单元测试
 *
 * @author Looly
 */
public class DynaBeanTest {

	@Test
	public void beanTest() {
		final User user = new User();
		final DynaBean bean = DynaBean.of(user);
		bean.set("name", "李华");
		bean.set("age", 12);

		final String name = bean.get("name");
		Assertions.assertEquals(user.getName(), name);
		final int age = bean.get("age");
		Assertions.assertEquals(user.getAge(), age);

		//重复包装测试
		final DynaBean bean2 = new DynaBean(bean);
		final User user2 = bean2.getBean();
		Assertions.assertEquals(user, user2);

		//执行指定方法
		final Object invoke = bean2.invoke("testMethod");
		Assertions.assertEquals("test for 李华", invoke);
	}


	@Test
	public void beanByStaticClazzConstructorTest() {
		final String name_before = "李华";
		final int age_before = 12;
		final DynaBean bean = DynaBean.of(User.class);
		bean.set("name", name_before);
		bean.set("age", age_before);

		final String name_after = bean.get("name");
		Assertions.assertEquals(name_before, name_after);
		final int age_after = bean.get("age");
		Assertions.assertEquals(age_before, age_after);

		//重复包装测试
		final DynaBean bean2 = new DynaBean(bean);
		final User user2 = bean2.getBean();
		final User user1 = bean.getBean();
		Assertions.assertEquals(user1, user2);

		//执行指定方法
		final Object invoke = bean2.invoke("testMethod");
		Assertions.assertEquals("test for 李华", invoke);
	}


	@Test
	public void beanByInstanceClazzConstructorTest() {
		final String name_before = "李华";
		final int age_before = 12;
		final DynaBean bean = new DynaBean(User.class);
		bean.set("name", name_before);
		bean.set("age", age_before);

		final String name_after = bean.get("name");
		Assertions.assertEquals(name_before, name_after);
		final int age_after = bean.get("age");
		Assertions.assertEquals(age_before, age_after);

		//重复包装测试
		final DynaBean bean2 = new DynaBean(bean);
		final User user2 = bean2.getBean();
		final User user1 = bean.getBean();
		Assertions.assertEquals(user1, user2);

		//执行指定方法
		final Object invoke = bean2.invoke("testMethod");
		Assertions.assertEquals("test for 李华", invoke);
	}

	@Data
	public static class User {
		private String name;
		private int age;

		public String testMethod() {
			return "test for " + this.name;
		}
	}
}
