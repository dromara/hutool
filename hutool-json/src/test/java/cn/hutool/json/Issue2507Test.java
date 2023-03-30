package cn.hutool.json;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue2507Test {

	@Test
	@Disabled
	public void xmlToJsonTest(){
		String xml = "<MsgInfo> <Msg> <![CDATA[<msg><body><row action=\"select\"><DIET>低盐饮食[嘱托]]><![CDATA[]</DIET></row></body></msg>]]> </Msg> <Msg> <![CDATA[<msg><body><row action=\"select\"><DIET>流质饮食</DIET></row></body></msg>]]> </Msg> </MsgInfo>";
		JSONObject jsonObject = JSONUtil.xmlToJson(xml);

		Console.log(jsonObject.toStringPretty());
	}
}
