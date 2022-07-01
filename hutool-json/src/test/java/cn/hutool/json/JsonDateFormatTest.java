package cn.hutool.json;

import cn.hutool.json.test.bean.JsonDateFormatBean;
import org.junit.Test;

import java.sql.Time;
import java.util.Date;

/**
 * 测试json时间格式化
 *
 *
 * @author duhanmin
 */
public class JsonDateFormatTest {

	@Test
	public void dateFormat() {
		JsonDateFormatBean jsonDateFormatBean = new JsonDateFormatBean();
		jsonDateFormatBean.setDate1(new Date());
		jsonDateFormatBean.setDate2(new java.sql.Date(new Date().getTime()));
		jsonDateFormatBean.setDate3(new Time(new Date().getTime()));
		jsonDateFormatBean.setDate4(new java.sql.Timestamp(System.currentTimeMillis()));

		System.out.println(JSONUtil.toJsonStr(jsonDateFormatBean));

		JSONConfig config = new JSONConfig();
		config.setDateFormat("yyyy-mm-dd");
		System.out.println(JSONUtil.toJsonStr(jsonDateFormatBean,config));

	}
}
