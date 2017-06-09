package com.xiaoleilu.hutool.core.bean;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.bean.DynaBean;

/**
 * {@link DynaBean}单元测试
 * @author Looly
 *
 */
public class DynaBeanTest {
	
	@Test
	public void beanTest(){
		User user = new User();
		DynaBean bean = new DynaBean(user);
		bean.set("name", "李华");
		bean.set("age", 12);
		
		String name = bean.get("name");
		Assert.assertEquals(user.getName(), name);
		int age = bean.get("age");
		Assert.assertEquals(user.getAge(), age);
		
		//重复包装测试
		DynaBean bean2 = new DynaBean(bean);
		User user2 = bean2.getBean();
		Assert.assertEquals(user, user2);
	}
	
	public static class User{
		private String name;
		private int age;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return "User [name=" + name + ", age=" + age + "]";
		}
	}
}
