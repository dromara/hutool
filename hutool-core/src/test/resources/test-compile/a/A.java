package a;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.ConsoleTable;
import cn.hutool.core.lang.caller.CallerUtil;

public class A {
    private class InnerClass {
    }

    public A() {
        new InnerClass() {{
            int i = 0;
			Console.log("初始化 " + getClass() + " 的调用链为: ");
			Class<?> caller = CallerUtil.getCaller(i);
			while (caller != null) {
				Console.log("{} {}", caller, caller.getClassLoader());
                caller = CallerUtil.getCaller(++i);
            }
        }};
    }
}
