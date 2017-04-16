package com.xiaoleilu.hutool.core.util;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.BeanUtil.CopyOptions;
import com.xiaoleilu.hutool.util.BeanUtil.ValueProvider;
import com.xiaoleilu.hutool.util.CollectionUtil;

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
	
	@Test
	public void fillBeanWithMapIgnoreCaseTest(){
		HashMap<String,Object> map = CollectionUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);
		map.put("openId", "DFDFSDFWERWER");
		SubPerson person = BeanUtil.fillBeanWithMapIgnoreCase(map, new SubPerson(), false);
		Assert.assertEquals(person.getName(), "Joe");
		Assert.assertEquals(person.getAge(), 12);
		Assert.assertEquals(person.getOpenid(), "DFDFSDFWERWER");
	}
	
	@Test
	public void mapToBeanIgnoreCaseTest(){
		HashMap<String,Object> map = CollectionUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);
		Person person = BeanUtil.mapToBeanIgnoreCase(map, Person.class, false);
		Assert.assertEquals(person.getName(), "Joe");
		Assert.assertEquals(person.getAge(), 12);
	}
	
	
	//-----------------------------------------------------------------------------------------------------------------
	public static class SubPerson extends Person{
		
	}
	
	public static class Person{
		private String name;
		private int age;
		private String openid;
		
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
		public String getOpenid() {
			return openid;
		}
		public void setOpenid(String openid) {
			this.openid = openid;
		}
	}
}
