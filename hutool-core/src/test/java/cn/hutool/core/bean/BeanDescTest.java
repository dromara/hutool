package cn.hutool.core.bean;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link BeanDesc} 单元测试类
 *
 * @author looly
 *
 */
public class BeanDescTest {

	@Test
	public void propDescTes() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);
		Assert.assertEquals("User", desc.getSimpleName());

		Assert.assertEquals("age", desc.getField("age").getName());
		Assert.assertEquals("getAge", desc.getGetter("age").getName());
		Assert.assertEquals("setAge", desc.getSetter("age").getName());
		Assert.assertEquals(1, desc.getSetter("age").getParameterTypes().length);
		Assert.assertSame(int.class, desc.getSetter("age").getParameterTypes()[0]);

	}

	@Test
	public void propDescTes2() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		final PropDesc prop = desc.getProp("name");
		Assert.assertEquals("name", prop.getFieldName());
		Assert.assertEquals("getName", prop.getGetter().getName());
		Assert.assertEquals("setName", prop.getSetter().getName());
		Assert.assertEquals(1, prop.getSetter().getParameterTypes().length);
		Assert.assertSame(String.class, prop.getSetter().getParameterTypes()[0]);
	}

	@Test
	public void propDescOfBooleanTest() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		Assert.assertEquals("isAdmin", desc.getGetter("isAdmin").getName());
		Assert.assertEquals("setAdmin", desc.getSetter("isAdmin").getName());
		Assert.assertEquals("isGender", desc.getGetter("gender").getName());
		Assert.assertEquals("setGender", desc.getSetter("gender").getName());
	}

	@Test
	public void propDescOfBooleanTest2() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		Assert.assertEquals("isIsSuper", desc.getGetter("isSuper").getName());
		Assert.assertEquals("setIsSuper", desc.getSetter("isSuper").getName());
	}

	@Test
	public void getSetTest() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		final User user = new User();
		desc.getProp("name").setValue(user, "张三");
		Assert.assertEquals("张三", user.getName());

		final Object value = desc.getProp("name").getValue(user);
		Assert.assertEquals("张三", value);
	}

	public static class User {
		private String name;
		private int age;
		private boolean isAdmin;
		private boolean isSuper;
		private boolean gender;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public User setAge(final int age) {
			this.age = age;
			return this;
		}

		public String testMethod() {
			return "test for " + this.name;
		}

		public boolean isAdmin() {
			return isAdmin;
		}

		public void setAdmin(final boolean isAdmin) {
			this.isAdmin = isAdmin;
		}

		public boolean isIsSuper() {
			return isSuper;
		}

		public void setIsSuper(final boolean isSuper) {
			this.isSuper = isSuper;
		}

		public boolean isGender() {
			return gender;
		}

		public void setGender(final boolean gender) {
			this.gender = gender;
		}

		@Override
		public String toString() {
			return "User [name=" + name + ", age=" + age + ", isAdmin=" + isAdmin + ", gender=" + gender + "]";
		}
	}
}
