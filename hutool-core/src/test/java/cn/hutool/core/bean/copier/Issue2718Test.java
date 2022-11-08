package cn.hutool.core.bean.copier;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

public class Issue2718Test {

	@Test
	public void copyToMapTest(){
		final Deployment deployment = new Deployment();
		deployment.setResources("test");
		final LinkedHashMap<String, Object> target = BeanCopier
				.create(deployment, new LinkedHashMap<String, Object>(), CopyOptions.create().setIgnoreProperties("resources"))
				.copy();

		Assert.assertTrue(target.isEmpty());
	}

	@Test
	public void copyToBeanTest(){
		final Deployment deployment = new Deployment();
		deployment.setResources("test");
		final Deployment target = BeanCopier
				.create(deployment, new Deployment(), CopyOptions.create().setIgnoreProperties("resources"))
				.copy();

		Assert.assertNull(target.resources);
	}

	@Setter
	private static class Deployment{
		public String getResources() {
			// setIgnoreProperties会被转换为propertiesFilter，这个filter是过滤键和值的，因此会获取源对象的值（调用getXXX方法），然后做判断。因此此方法会被执行
			throw new RuntimeException("这个方法不应该被调用");
			//return resources;
		}

		private String resources;
	}
}
