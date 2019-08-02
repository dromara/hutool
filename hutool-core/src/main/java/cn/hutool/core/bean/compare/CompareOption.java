package cn.hutool.core.bean.compare;

import java.io.Serializable;

/**
 * 字段比较选项
 * 1. 可配置比较实现的接口或者类
 * 2. 可配置忽略字段
 * 3. 如果一个bean中的类型比较复杂, 可使用复杂bean比较选项类{@link ComplexCompareOption}, 可对bean中的字段类型进行单独配置
 * @author dawn
 */
public class CompareOption implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类 */
    protected Class<?> editable;
    /** 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值 */
    protected String[] ignoreProperties;

    public CompareOption(Class<?> editable, String[] ignoreProperties) {
        this.editable = editable;
        this.ignoreProperties = ignoreProperties;
    }

    public CompareOption() {
    }

    public Class<?> getEditable() {
        return editable;
    }

    public void setEditable(Class<?> editable) {
        this.editable = editable;
    }

    public String[] getIgnoreProperties() {
        return ignoreProperties;
    }

    public void setIgnoreProperties(String[] ignoreProperties) {
        this.ignoreProperties = ignoreProperties;
    }
}
