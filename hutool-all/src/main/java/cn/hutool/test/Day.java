package cn.hutool.test;

/**
 * @author by jiangmenghui
 * @version [版本号, 2019/4/8]
 * @Classname Day
 * @Description TODO
 * @Date 2019/4/8
 */
public enum Day {
    MONDAY("1"), TUESDAY("1"), WEDNESDAY("3"),
    THURSDAY("4"), FRIDAY("5"), SATURDAY("6"), SUNDAY("7");

    private String desc;
    private Day(String desc){
        this.desc=desc;
    }
    public String getDesc(){
        return desc;
    }
}
