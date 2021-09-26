package cn.hutool.core.mapping;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XueRi
 * @since 2021/9/26
 * 支持父类映射 lambda表达式
 */
public class BeanMappingTest {

	/**
	 * 映射对象
	 */
	@Test
	public void tobeBnTest() {
		Cat cat = new Cat();
		cat.setAnimalName("加菲");
		cat.setColor("白色");
		cat.setSex("公");
		cat.setWeight(10);

		Dog dog = BeanMapping.toBean(cat, Dog.class);
		System.out.println(dog);
	}

	/**
	 * 对象内不同字段手动映射
	 * 支持JDK lambda表达式
	 */
	@Test
	public void toBeanManualTest() {
		Cat cat = new Cat();
		cat.setAnimalName("加菲");
		cat.setColor("白色");
		cat.setSex("公");
		cat.setWeight(10);

		// sex 和 gender 字段不同 手动映射
		Dog dog = BeanMapping.toBean(
				cat,
				Dog.class,
				(source, target) -> target.setGender(source.getSex()));
		System.out.println(dog);
	}

	/**
	 * 集合映射
	 */
	@Test
	public void toListManualTest() {
		List<Cat> catList = new ArrayList<>();

		Cat c1 = new Cat();
		c1.setAnimalName("加菲");
		c1.setColor("白色");
		c1.setSex("公");
		c1.setWeight(10);

		Cat c2 = new Cat();
		c2.setAnimalName("折耳");
		c2.setColor("白色");
		c2.setSex("公");
		c2.setWeight(10);

		catList.add(c1);
		catList.add(c2);

		List<Dog> dogs = BeanMapping.toList(catList, Dog.class);
		System.out.println(dogs);
	}

	/**
	 * 集合映射不同字段手动映射
	 */
	@Test
	public void toListTest() {
		List<Cat> catList = new ArrayList<>();

		Cat c1 = new Cat();
		c1.setAnimalName("加菲");
		c1.setColor("白色");
		c1.setSex("公");
		c1.setWeight(10);

		Cat c2 = new Cat();
		c2.setAnimalName("折耳");
		c2.setColor("白色");
		c2.setSex("公");
		c2.setWeight(10);

		catList.add(c1);
		catList.add(c2);

		List<Dog> dogs = BeanMapping.toList(
				catList,
				Dog.class,
				(source, target) -> target.setGender(source.getSex()));
		System.out.println(dogs);
	}

	/**
	 * 集合映射节段
	 */
	@Test
	public void toListSkipTest() {
		List<Cat> catList = new ArrayList<>();

		Cat c1 = new Cat();
		c1.setAnimalName("加菲");
		c1.setColor("白色");
		c1.setSex("公");
		c1.setWeight(10);

		Cat c2 = new Cat();
		c2.setAnimalName("折耳");
		c2.setColor("白色");
		c2.setSex("公");
		c2.setWeight(10);

		catList.add(c1);
		catList.add(c2);

		List<Dog> dogSkip = BeanMapping.toListRange(
				catList,
				Dog.class,
				0,
				1);
		System.out.println(dogSkip);
	}
}
