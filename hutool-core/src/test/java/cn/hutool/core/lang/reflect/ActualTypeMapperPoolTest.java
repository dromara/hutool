package cn.hutool.core.lang.reflect;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 见：https://gitee.com/dromara/hutool/pulls/447/files
 *
 * TODO 同时继承泛型和实现泛型接口需要解析，此处为F
 */
public class ActualTypeMapperPoolTest {

	@Test
	public void getTypeArgumentTest(){
		final Map<Type, Type> typeTypeMap = ActualTypeMapperPool.get(FinalClass.class);
		typeTypeMap.forEach((key, value)->{
			if("A".equals(key.getTypeName())){
				assertEquals(Character.class, value);
			} else if("B".equals(key.getTypeName())){
				assertEquals(Boolean.class, value);
			} else if("C".equals(key.getTypeName())){
				assertEquals(String.class, value);
			} else if("D".equals(key.getTypeName())){
				assertEquals(Double.class, value);
			} else if("E".equals(key.getTypeName())){
				assertEquals(Integer.class, value);
			}
		});
	}

	@Test
	public void getTypeArgumentStrKeyTest(){
		final Map<String, Type> typeTypeMap = ActualTypeMapperPool.getStrKeyMap(FinalClass.class);
		typeTypeMap.forEach((key, value)->{
			if("A".equals(key)){
				assertEquals(Character.class, value);
			} else if("B".equals(key)){
				assertEquals(Boolean.class, value);
			} else if("C".equals(key)){
				assertEquals(String.class, value);
			} else if("D".equals(key)){
				assertEquals(Double.class, value);
			} else if("E".equals(key)){
				assertEquals(Integer.class, value);
			}
		});
	}

	public interface BaseInterface<A, B, C> {}
	public interface FirstInterface<A, B, D, E> extends BaseInterface<A, B, String> {}
	public interface SecondInterface<A, B, F> extends BaseInterface<A, B, String> {}

	public static class BaseClass<A, D> implements FirstInterface<A, Boolean, D, Integer> {}
	public static class FirstClass extends BaseClass<Character, Double> implements SecondInterface<Character, Boolean, FirstClass> {}
	public static class SecondClass extends FirstClass {}
	public static class FinalClass extends SecondClass {}
}
