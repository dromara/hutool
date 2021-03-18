package cn.hutool.extra.flowcontrol;

/**
 * @author WangChen
 * @since 2021-01-21 16:52
 **/
public class TestMethod {


    @SimpleFlowControl(limit = 3)
    public void test(){
        System.out.println("hello world!");
    }


    @SimpleFlowControl(limit = 10)
    public void test2(){
        System.out.println("hello world!");
    }


}
