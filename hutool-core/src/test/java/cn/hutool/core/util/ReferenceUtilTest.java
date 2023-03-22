package cn.hutool.core.util;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.mutable.MutableObj;
import org.junit.Assert;
import org.junit.Ignore;
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

	@Test
	@Ignore
	public void gcTest(){
		// https://blog.csdn.net/zmx729618/article/details/54093532
		// 弱引用的对象必须使用可变对象，不能使用常量对象（比如String）
		WeakReference<MutableObj<String>> reference = new WeakReference<>(new MutableObj<>("abc"));
		int i=0;
		while(true){
			if(reference.get()!=null){
				i++;
				Console.log("Object is alive for {} loops - ", i);
				System.gc();
			}else{
				Console.log("Object has been collected.");
				break;
			}
		}
	}
}
