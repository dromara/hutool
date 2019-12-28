package cn.hutool.core.lang;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ObjectId单元测试
 * 
 * @author looly
 *
 */
public class ObjectIdTest {
	
	@Test
	public void distinctTest() {
		//生成10000个id测试是否重复
		HashSet<String> set = IntStream.range(0, 10000).mapToObj(i -> ObjectId.next()).collect(Collectors.toCollection(HashSet::new));

		Assert.assertEquals(10000, set.size());
	}
	
	@Test
	@Ignore
	public void nextTest() {
		Console.log(ObjectId.next());
	}
}
