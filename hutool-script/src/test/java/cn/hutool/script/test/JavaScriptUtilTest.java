package cn.hutool.script.test;

import cn.hutool.script.JavaScriptUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * JavaScript脚本工具类单元测试类
 *
 * @author dahuoyzs
 *
 */
public class JavaScriptUtilTest {

    @Test
    public void execTest(){
        String script1 = "function sum(a,b){return a+b}";
        String script2 = "function mul(a,b){return a*b}";
        Assert.assertEquals(JavaScriptUtil.exec(script1, "sum", 1, 2),3.0);
        Assert.assertEquals(JavaScriptUtil.exec(script2, "mul", 1, 2),2.0);
    }

}
