package cn.hutool.core.collection.iter;

import cn.hutool.core.collection.ListUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class FilterIterTest {

	@Test
	public void iterNullTest(){
		final Iterator<String> it = ListUtil.of("1", "2").iterator();
		final FilterIter<String> filterIter = new FilterIter<>(it, null);
		int count = 0;
		while (filterIter.hasNext()) {
			if(null != filterIter.next()){
				count++;
			}
		}
		Assert.assertEquals(2, count);
	}
}
