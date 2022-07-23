package cn.hutool.core.lang;

import cn.hutool.core.event.DefaultContextTest;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class ParameterizedTypeImplTest {

	@Test
	public void testEquals() {
		ParameterizedType parameterizedType1 = new ParameterizedTypeImpl(new Class[]{Integer.class}, List.class, null);
		ParameterizedType parameterizedType2 = new ParameterizedTypeImpl(new Class[]{Integer.class}, List.class, null);
		ParameterizedType parameterizedType3 = new ParameterizedTypeImpl(new Class[]{String.class}, List.class, null);
		Assert.assertEquals(parameterizedType1,parameterizedType2);
		Assert.assertNotEquals(parameterizedType3,parameterizedType2);
	}
}
