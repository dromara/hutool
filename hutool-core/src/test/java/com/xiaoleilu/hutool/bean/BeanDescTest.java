package com.xiaoleilu.hutool.bean;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.bean.BeanDesc;
import com.xiaoleilu.hutool.bean.BeanDesc.PropDesc;
import com.xiaoleilu.hutool.bean.BeanUtil;

/**
 * {@link BeanDesc} 单元测试类
 * 
 * @author looly
 *
 */
public class BeanDescTest {

	@Test
	public void propDescTes() {
		BeanDesc desc = BeanUtil.getBeanDesc(User.class);
		Assert.assertEquals("User", desc.getSimpleName());

		Assert.assertEquals("age", desc.getField("age").getName());
		Assert.assertEquals("getAge", desc.getGetter("age").getName());
		Assert.assertEquals("setAge", desc.getSetter("age").getName());
		Assert.assertEquals(1, desc.getSetter("age").getParameterTypes().length);
		Assert.assertEquals(int.class, desc.getSetter("age").getParameterTypes()[0]);

	}

	@Test
	public void propDescTes2() {
		BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		PropDesc prop = desc.getProp("name");
		Assert.assertEquals("name", prop.getFieldName());
		Assert.assertEquals("getName", prop.getGetter().getName());
		Assert.assertEquals("setName", prop.getSetter().getName());
		Assert.assertEquals(1, prop.getSetter().getParameterTypes().length);
		Assert.assertEquals(String.class, prop.getSetter().getParameterTypes()[0]);
	}

	@Test
	public void propDescOfBooleanTest() {
		BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		Assert.assertEquals("isAdmin", desc.getGetter("isAdmin").getName());
		Assert.assertEquals("setAdmin", desc.getSetter("isAdmin").getName());
		Assert.assertEquals("isGender", desc.getGetter("gender").getName());
		Assert.assertEquals("setGender", desc.getSetter("gender").getName());
	}

	public static class User {
		private String name;
		private int age;
		private boolean isAdmin;
		private boolean gender;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public User setAge(int age) {
			this.age = age;
			return this;
		}

		public String testMethod() {
			return "test for " + this.name;
		}

		public boolean isAdmin() {
			return isAdmin;
		}

		public void setAdmin(boolean isAdmin) {
			this.isAdmin = isAdmin;
		}

		public boolean isGender() {
			return gender;
		}

		public void setGender(boolean gender) {
			this.gender = gender;
		}

		@Override
		public String toString() {
			return "User [name=" + name + ", age=" + age + ", isAdmin=" + isAdmin + ", gender=" + gender + "]";
		}
	}
}
