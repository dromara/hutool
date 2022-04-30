package cn.hutool.core.bean;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link DynaBean}单元测试
 *
 * @author Looly
 */
public class DynaBeanTest {

	@Test
	public void beanTest() {
		final User user = new User();
		final DynaBean bean = DynaBean.create(user);
		bean.set("name", "李华");
		bean.set("age", 12);

		final String name = bean.get("name");
		Assert.assertEquals(user.getName(), name);
		final int age = bean.get("age");
		Assert.assertEquals(user.getAge(), age);

		//重复包装测试
		final DynaBean bean2 = new DynaBean(bean);
		final User user2 = bean2.getBean();
		Assert.assertEquals(user, user2);

		//执行指定方法
		final Object invoke = bean2.invoke("testMethod");
		Assert.assertEquals("test for 李华", invoke);
	}


	@Test
	public void beanByStaticClazzConstructorTest() {
		final String name_before = "李华";
		final int age_before = 12;
		final DynaBean bean = DynaBean.create(User.class);
		bean.set("name", name_before);
		bean.set("age", age_before);

		final String name_after = bean.get("name");
		Assert.assertEquals(name_before, name_after);
		final int age_after = bean.get("age");
		Assert.assertEquals(age_before, age_after);

		//重复包装测试
		final DynaBean bean2 = new DynaBean(bean);
		final User user2 = bean2.getBean();
		final User user1 = bean.getBean();
		Assert.assertEquals(user1, user2);

		//执行指定方法
		final Object invoke = bean2.invoke("testMethod");
		Assert.assertEquals("test for 李华", invoke);
	}


	@Test
	public void beanByInstanceClazzConstructorTest() {
		final String name_before = "李华";
		final int age_before = 12;
		final DynaBean bean = new DynaBean(User.class);
		bean.set("name", name_before);
		bean.set("age", age_before);

		final String name_after = bean.get("name");
		Assert.assertEquals(name_before, name_after);
		final int age_after = bean.get("age");
		Assert.assertEquals(age_before, age_after);

		//重复包装测试
		final DynaBean bean2 = new DynaBean(bean);
		final User user2 = bean2.getBean();
		final User user1 = bean.getBean();
		Assert.assertEquals(user1, user2);

		//执行指定方法
		final Object invoke = bean2.invoke("testMethod");
		Assert.assertEquals("test for 李华", invoke);
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
