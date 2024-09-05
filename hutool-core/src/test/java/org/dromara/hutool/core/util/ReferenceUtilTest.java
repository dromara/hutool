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

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.ref.ReferenceType;
import org.dromara.hutool.core.lang.ref.ReferenceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceUtilTest {

	@Test
	public void createWeakTest(){
		final Reference<Integer> integerReference = ReferenceUtil.of(ReferenceType.WEAK, 1);
		Assertions.assertInstanceOf(WeakReference.class, integerReference);
		Assertions.assertEquals(Integer.valueOf(1), integerReference.get());
	}

	@Test
	public void createSoftTest(){
		final Reference<Integer> integerReference = ReferenceUtil.of(ReferenceType.SOFT, 1);
		Assertions.assertInstanceOf(SoftReference.class, integerReference);
		Assertions.assertEquals(Integer.valueOf(1), integerReference.get());
	}

	@Test
	public void createPhantomTest(){
		final Reference<Integer> integerReference = ReferenceUtil.of(ReferenceType.PHANTOM, 1);
		Assertions.assertInstanceOf(PhantomReference.class, integerReference);
		// get方法永远都返回null，PhantomReference只能用来监控对象的GC状况
		Assertions.assertNull(integerReference.get());
	}

	@Test
	@Disabled
	public void gcTest(){
		// https://blog.csdn.net/zmx729618/article/details/54093532
		// 弱引用的对象必须使用可变对象，不能使用常量对象（比如String）
		final WeakReference<MutableObj<String>> reference = new WeakReference<>(new MutableObj<>("abc"));
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
