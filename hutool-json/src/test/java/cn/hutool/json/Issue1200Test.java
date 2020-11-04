package cn.hutool.json;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.test.bean.ResultBean;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 测试在bean转换时使用BeanConverter，默认忽略转换失败的字段。
 * 现阶段Converter的问题在于，无法更细粒度的控制转换失败的范围，例如Bean的一个字段为List，
 * list任意一个item转换失败都会导致这个list为null。
 *
 * TODO 需要在Converter中添加ConvertOption，用于更细粒度的控制转换规则
 */
public class Issue1200Test {

	@Test
	@Ignore
	public void toBeanTest(){
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.readUtf8Str("issue1200.json"));
		Console.log(jsonObject);

		final ResultBean resultBean = jsonObject.toBean(ResultBean.class);
		Console.log(resultBean);
	}
}
