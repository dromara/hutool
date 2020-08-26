package cn.hutool.core.lang.tree;

import cn.hutool.core.annotation.LeafCollection;
import cn.hutool.core.annotation.LeafDecide;
import cn.hutool.core.annotation.RootDecide;
import cn.hutool.core.exceptions.LeafCollectionNotFoundException;
import cn.hutool.core.util.ReflectUtil;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

/**
 * 树结构转换工具
 * <p>
 * 由{@link LeafCollection}注解标注实体，用于存放子节点的容器
 * 函数式两个接口{@link RootDecide} {@link LeafDecide} 判断父子节点定义
 * <p>
 * 这样可以尽量不更改接口返回的泛型结构
 *
 * @author shadow (https://github.com/SHADOW-LI0327)
 * @version 5.4.1
 * @since 5.4.1
 */
public class TreeConvert {

    /**
     * 构造私有化，静态方法不需要暴露公共构造器
     * 修复 <code>Add a private constructor to hide the implicit public one</code>
     */
    private TreeConvert() {

    }
    /**
     * 生成树结构
     * 通过反射检测LeafCollection注解,转换为父子结构容器,子数据装入LeafCollection容器中
     * 2020-05-20 修复泛型问题，方便调用
     *
     * @param elements   元素组
     * @param clazz      反射类型 ！警告！ 不可传入包装类型,后续判断失效将抛出异常
     * @param rootDecide 根元素判断函数
     * @param leafDecide 叶子元素判断函数
     * @param <T>        泛型
     * @return List<T>
     */
    public static <T> List<T> convert(Collection<T> elements, Class<?> clazz, RootDecide<T> rootDecide, LeafDecide<T> leafDecide) {
        List<T> treeList = new ArrayList<>();
        //叶子容器
        Field leafCollection = null;
        if (!elements.isEmpty()) {
            Field[] fields = ReflectUtil.getFields(clazz);
            //一般扩展属性会写在成员变量的后面,倒着找比较快
            for (int i = fields.length - 1; i > 0; i--) {
                if (fields[i].getAnnotation(LeafCollection.class) != null) {
                    //找到既退出迭代查找
                    leafCollection = fields[i];
                    break;
                }
            }
            //缺少注解抛出异常
            if (leafCollection == null) {
                throw new LeafCollectionNotFoundException("注解LeafCollection未找到,请确认子容器");
            }
        }
        //迭代容器
        for (T element : elements) {
            if (rootDecide.isRoot(element)) {
                //设立根目录
                treeList.add(element);
                //递归
                try {
                    sort(element, elements, leafDecide, clazz, leafCollection);
                } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return treeList;
    }


    /**
     * 递归排序
     *
     * @param root       根元素
     * @param elements   元素组
     * @param leafDecide 叶子判断函数
     * @param leafField  叶子容器
     * @param clazz      类型
     * @param <T>        泛型
     * @return List<T>
     * @throws IntrospectionException    e
     * @throws InvocationTargetException e
     * @throws IllegalAccessException    e
     *
     * 2020-08-26 修复方法名过长
     */
    private static <T> List<T> sort(T root, Collection<T> elements, LeafDecide<T> leafDecide, Class<?> clazz, Field leafField)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<T> subMenu = null;
        for (T element : elements) {
            if (leafDecide.isLeaf(root, element)) {
                if (subMenu == null) {
                    subMenu = new ArrayList<>();
                }
                List<T> leaf = sort(element, elements, leafDecide, clazz, leafField);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(leafField.getName(), clazz);
                propertyDescriptor.getWriteMethod().invoke(element, leaf);
                subMenu.add(element);
            }
        }
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(leafField.getName(), clazz);
        propertyDescriptor.getWriteMethod().invoke(root, subMenu);
        return subMenu;
    }
}
