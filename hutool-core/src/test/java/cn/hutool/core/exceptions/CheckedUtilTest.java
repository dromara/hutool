package cn.hutool.core.exceptions;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.VoidFunc0;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * 方便的执行会抛出受检查类型异常的方法调用或者代码段
 * <p>
 * 该工具通过函数式的方式将那些需要抛出受检查异常的表达式或者代码段转化成一个标准的java8 functional 对象
 * </p>
 *
 * @author conder
 */
public class CheckedUtilTest {


	@Test
	public void sleepTest() {
		VoidFunc0 func = () -> Thread.sleep(1000L);
		func.callWithRuntimeException();
	}


	@SuppressWarnings("ConstantConditions")
	@Test
	public void supplierTest() {
		File noFile = new File("./no-file");
		try {
			//本行代码原本需要抛出受检查异常，现在只抛出运行时异常
			CheckedUtil.uncheck(() -> new FileInputStream(noFile)).call();
		} catch (Exception re) {
			Assert.assertTrue(re instanceof RuntimeException);
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void functionTest() {
		Func1<String, String> afunc = (funcParam) -> {
			if (funcParam.length() > 5) {
				throw new Exception("这是受检查异常需要屌用处显示处理");
			}
			return funcParam.toUpperCase();
		};

		//afunc.apply("hello world"); 直接调用需要处理异常


		try {
			//本行代码原本需要抛出受检查异常，现在只抛出运行时异常
			CheckedUtil.uncheck(afunc).call("hello world");
		} catch (Exception re) {
			Assert.assertTrue(re instanceof RuntimeException);
		}

	}

}
