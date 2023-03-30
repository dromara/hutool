package cn.hutool.core.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * https://github.com/dromara/hutool/issues/2009
 */
public class Issue2009Test {

	@SuppressWarnings("InnerClassMayBeStatic")
	public class BaseA {
		private String paPss;

		public String getPaPss() {
			return paPss;
		}

		public void setPaPss(final String paPss) {
			this.paPss = paPss;
		}
	}


	public class A extends BaseA {
		private String papss;

		public String getPapss() {
			return papss;
		}

		public void setPapss(final String papss) {
			this.papss = papss;
		}

		@Override
		public String toString() {
			return "A{" +
					"papss='" + papss + '\'' +
					'}';
		}
	}


	public class B extends BaseA {
		private String papss;

		public B(final String papss) {
			this.papss = papss;
		}

		public String getPapss() {
			return papss;
		}

		public void setPapss(final String papss) {
			this.papss = papss;
		}

		@Override
		public String toString() {
			return "B{" +
					"papss='" + papss + '\'' +
					'}';
		}
	}

	@Test
	public void test() {
		final B b = new B("a string text");
		final A a = new A();
		BeanUtil.copyProperties(b, a);

		Assertions.assertEquals(b.getPapss(), a.getPapss());
	}
}
