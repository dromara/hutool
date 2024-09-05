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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.exception.CloneException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 克隆单元测试
 * @author Looly
 *
 */
public class CloneTest {

	@Test
	public void cloneTest(){

		//实现Cloneable接口
		final Cat cat = new Cat();
		final Cat cat2 = cat.clone();
		Assertions.assertEquals(cat, cat2);
	}

	@Test
	public void cloneTest2(){
		//继承CloneSupport类
		final Dog dog = new Dog();
		final Dog dog2 = dog.clone();
		Assertions.assertEquals(dog, dog2);
	}

	//------------------------------------------------------------------------------- private Class for test
	/**
	 * 猫猫类，使用实现Cloneable方式
	 * @author Looly
	 *
	 */
	@Data
	static class Cat implements Cloneable{
		private String name = "miaomiao";
		private int age = 2;

		@Override
		public Cat clone() {
			try {
				return (Cat) super.clone();
			} catch (final CloneNotSupportedException e) {
				throw new CloneException(e);
			}
		}
	}

	/**
	 * 狗狗类，用于继承CloneSupport类
	 * @author Looly
	 *
	 */
	@EqualsAndHashCode(callSuper = false)
	@Data
	static class Dog implements Cloneable{
		private String name = "wangwang";
		private int age = 3;

		@Override
		public Dog clone() {
			try {
				return (Dog) super.clone();
			} catch (final CloneNotSupportedException e) {
				throw new CloneException(e);
			}
		}
	}
}
