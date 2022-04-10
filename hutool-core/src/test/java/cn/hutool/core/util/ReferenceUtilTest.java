package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceUtilTest {

	@Test
	public void createWeakTest(){
		final Reference<Integer> integerReference = ReferenceUtil.create(ReferenceUtil.ReferenceType.WEAK, 1);
		Assert.assertTrue(integerReference instanceof WeakReference);
		Assert.assertEquals(new Integer(1), integerReference.get());
	}

	@Test
	public void createSoftTest(){
		final Reference<Integer> integerReference = ReferenceUtil.create(ReferenceUtil.ReferenceType.SOFT, 1);
		Assert.assertTrue(integerReference instanceof SoftReference);
		Assert.assertEquals(new Integer(1), integerReference.get());
	}

	@Test
	public void createPhantomTest(){
		final Reference<Integer> integerReference = ReferenceUtil.create(ReferenceUtil.ReferenceType.PHANTOM, 1);
		Assert.assertTrue(integerReference instanceof PhantomReference);
		// get方法永远都返回null，PhantomReference只能用来监控对象的GC状况
		Assert.assertNull(integerReference.get());
	}
}
