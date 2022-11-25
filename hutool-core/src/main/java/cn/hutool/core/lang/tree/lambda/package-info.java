/**
 * Lambda树形转换工具
 * <p>
 * 背景说明：基于Lambda实现一个快捷易用的树形转换工具，此思想来源于Mybatis-plus的LambdaQuery实现。
 * 核心主要是依赖lambda获取到 SerializedLambda，再通过SerializedLambda获取到对应的方法名，
 * 最后结合MethodHandle进行方法调用（性能有待考究。这里使用反射也同样能够实现，但是我们知道反射在性能
 * 上一直存在一些诟病。 而MethodHandle是模拟字节码层次的方法调用，虚拟机在这方面的优化空间会更大）。
 * </p>
 * <p>
 * 优势：
 * 无须转换指定的bean对象，使用示例：
 * <code>
 *     List<Menu> treeData = LambdaTreeUtil.builder(menuList, 0L)
 * 				.id(Menu::getId) // 指定节点标识function
 * 				.pid(Menu::getPid) // 指定父节点标识function
 * 				.children(Menu::getChildren) // 指定子节点集function
 * 				.sort(Menu::getSortNum) //指定排序function
 * 				.build();
 * </code>
 * <p/>
 *
 * 本人源代码地址：https://github.com/SmithAdoph/AdophCloud/tree/master/adoph-framework/src/main/java/com/adoph/framework/util/tree/v4
 * @author adoph
 */
package cn.hutool.core.lang.tree.lambda;
