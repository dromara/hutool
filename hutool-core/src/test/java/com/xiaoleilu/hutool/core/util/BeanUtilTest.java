package com.xiaoleilu.hutool.core.util;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.BeanUtil.CopyOptions;
import com.xiaoleilu.hutool.util.BeanUtil.ValueProvider;

/**
 * Bean工具单元测试
 * @author Looly
 *
 */
public class BeanUtilTest {
	
	@Test
	public void isBeanTest(){
		
		//HashMap不包含setXXX方法，不是bean
		boolean isBean = BeanUtil.isBean(HashMap.class);
		Assert.assertFalse(isBean);
	}
	
	@Test
	public void fillBeanTest(){
		Person person = BeanUtil.fillBean(new Person(), new ValueProvider<String>(){

			@Override
			public Object value(String key, Class<?> valueType) {
				switch (key) {
					case "name":
						return "张三";
					case "age":
						return 18;
				}
				return null;
			}

			@Override
			public boolean containsKey(String key) {
				//总是存在key
				return true;
			}
			
		}, CopyOptions.create());
		
		Assert.assertEquals(person.getName(), "张三");
		Assert.assertEquals(person.getAge(), 18);
	}
	
	public static class Person{
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
	}
}
