package cn.hutool.test;

import org.junit.Test;

/**
 * @author by jiangmenghui
 * @version [版本号, 2019/4/8]
 * @Classname EnumTest
 * @Description TODO
 * @Date 2019/4/8
 */
public class EnumTest {
    @Test
    public void test(){
        Day day = Day.FRIDAY;
        System.out.println(day.getDesc());
    }


}
