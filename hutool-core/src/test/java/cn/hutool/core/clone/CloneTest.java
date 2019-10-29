package cn.hutool.core.clone;

import org.junit.Assert;
import org.junit.Test;

/**
 * 克隆单元测试
 * @author Looly
 *
 */
public class CloneTest {
	
	@Test
	public void cloneTest(){
		
		//实现Cloneable接口
		Cat cat = new Cat();
		Cat cat2 = cat.clone();
		Assert.assertEquals(cat, cat2);
		
		//继承CloneSupport类
		Dog dog = new Dog();
		Dog dog2 = dog.clone();
		Assert.assertEquals(dog, dog2);
	}
	
	//------------------------------------------------------------------------------- private Class for test
	/**
	 * 猫猫类，使用实现Cloneable方式
	 * @author Looly
	 *
	 */
	private static class Cat implements Cloneable<Cat>{
		private String name = "miaomiao";
		private int age = 2;
		
		@Override
		public Cat clone() {
			try {
				return (Cat) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new CloneRuntimeException(e);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + age;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Cat other = (Cat) obj;
			if (age != other.age) {
				return false;
			}
			if (name == null) {
				return other.name == null;
			} else return name.equals(other.name);
		}
	}
	
	/**
	 * 狗狗类，用于继承CloneSupport类
	 * @author Looly
	 *
	 */
	private static class Dog extends CloneSupport<Dog>{
		private String name = "wangwang";
		private int age = 3;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + age;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Dog other = (Dog) obj;
			if (age != other.age) {
				return false;
			}
			if (name == null) {
				return other.name == null;
			} else return name.equals(other.name);
		}
	}
}
