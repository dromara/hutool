package cn.hutool.core.bean;

/**
 * 为了解决反射过程中,需要传递null参数,但是会丢失参数类型而设立的包装类
 */
public class NullWrapperBean {

    private final Class<?> mClasses;

    /**
     * @param classes null的类型
     */
    public NullWrapperBean(Class<?> classes) {
        this.mClasses = classes;
    }

    public Class<?> getClasses() {
        return mClasses;
    }
}
