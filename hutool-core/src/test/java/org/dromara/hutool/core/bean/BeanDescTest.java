/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.bean.path.AbstractBeanDesc;
import org.dromara.hutool.core.reflect.method.MethodInvoker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * {@link StrictBeanDesc} 单元测试类
 *
 * @author looly
 *
 */
public class BeanDescTest {

	@Test
	public void propDescTes() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);
		if(desc instanceof AbstractBeanDesc){
			Assertions.assertEquals("User", ((AbstractBeanDesc) desc).getSimpleName());
		}

		Assertions.assertEquals("age", desc.getProp("age").getFieldName());
		Assertions.assertEquals("getAge", desc.getGetter("age").getName());
		Assertions.assertEquals("setAge", desc.getSetter("age").getName());

		final MethodInvoker setter = (MethodInvoker) desc.getSetter("age");
		Assertions.assertEquals(1, setter.getMethod().getParameterTypes().length);
		Assertions.assertSame(int.class, setter.getMethod().getParameterTypes()[0]);

	}

	@Test
	public void propDescTes2() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		final PropDesc prop = desc.getProp("name");
		Assertions.assertEquals("name", prop.getFieldName());
		Assertions.assertEquals("getName", prop.getGetter().getName());
		Assertions.assertEquals("setName", prop.getSetter().getName());

		final MethodInvoker setter = (MethodInvoker) desc.getSetter("name");
		final Method setterMethod = setter.getMethod();
		Assertions.assertEquals(1, setterMethod.getParameterTypes().length);
		Assertions.assertSame(String.class, setterMethod.getParameterTypes()[0]);
	}

	@Test
	public void propDescOfBooleanTest() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		Assertions.assertEquals("isAdmin", desc.getGetter("isAdmin").getName());
		Assertions.assertEquals("setAdmin", desc.getSetter("isAdmin").getName());
		Assertions.assertEquals("isGender", desc.getGetter("gender").getName());
		Assertions.assertEquals("setGender", desc.getSetter("gender").getName());
	}

	@Test
	public void propDescOfBooleanTest2() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		Assertions.assertEquals("isIsSuper", desc.getGetter("isSuper").getName());
		Assertions.assertEquals("setIsSuper", desc.getSetter("isSuper").getName());
	}

	@Test
	public void propDescOfBooleanTest3() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		Assertions.assertEquals("setLastPage", desc.getSetter("lastPage").getName());
		Assertions.assertEquals("setIsLastPage", desc.getSetter("isLastPage").getName());
	}

	@Test
	public void getSetTest() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		final User user = new User();
		desc.getProp("name").setValue(user, "张三");
		Assertions.assertEquals("张三", user.getName());

		final Object value = desc.getProp("name").getValue(user);
		Assertions.assertEquals("张三", value);
	}

	@Test
	void simpleBeanDescTest() {
		final SimpleBeanDesc desc = new SimpleBeanDesc(User.class);

		final User user = new User();
		desc.getProp("name").setValue(user, "张三");
		Assertions.assertEquals("张三", user.getName());
		Object value = desc.getProp("name").getValue(user);
		Assertions.assertEquals("张三", value);

		desc.getProp("admin").setValue(user, true);
		Assertions.assertTrue(user.isAdmin());
		value = desc.getProp("admin").getValue(user);
		Assertions.assertEquals(true, value);
	}

	public static class User {
		private String name;
		private int age;
		private boolean isAdmin;
		private boolean isSuper;
		private boolean gender;
		private Boolean lastPage;
		private Boolean isLastPage;

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

		public Boolean getIsLastPage() {
			return this.isLastPage;
		}

		public void setIsLastPage(final Boolean isLastPage) {
			this.isLastPage = isLastPage;
		}

		public Boolean getLastPage() {
			return this.lastPage;
		}

		public void setLastPage(final Boolean lastPage) {
			this.lastPage = lastPage;
		}

		@Override
		public String toString() {
			return "User [name=" + name + ", age=" + age + ", isAdmin=" + isAdmin + ", gender=" + gender + "]";
		}
	}
}
