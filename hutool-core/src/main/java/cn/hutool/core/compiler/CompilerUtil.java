package cn.hutool.core.compiler;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * 源码编译工具类，主要封装{@link JavaCompiler} 相关功能
 *
 * @author looly
 * @since 5.5.2
 */
public class CompilerUtil {

	/**
	 * java 编译器
	 */
	public static final JavaCompiler SYSTEM_COMPILER = ToolProvider.getSystemJavaCompiler();

	/**
	 * 编译指定的源码文件
	 *
	 * @param sourceFiles 源码文件路径
	 * @return 0表示成功，否则其他
	 */
	public static boolean compile(String... sourceFiles) {
		return 0 == SYSTEM_COMPILER.run(null, null, null, sourceFiles);
	}

	/**
	 * 获取{@link StandardJavaFileManager}
	 *
	 * @return {@link StandardJavaFileManager}
	 */
	public static StandardJavaFileManager getFileManager() {
		return getFileManager(null);
	}

	/**
	 * 获取{@link StandardJavaFileManager}
	 *
	 * @param diagnosticListener 异常收集器
	 * @return {@link StandardJavaFileManager}
	 * @since 5.5.8
	 */
	public static StandardJavaFileManager getFileManager(DiagnosticListener<? super JavaFileObject> diagnosticListener) {
		return SYSTEM_COMPILER.getStandardFileManager(diagnosticListener, null, null);
	}

	/**
	 * 新建编译任务
	 *
	 * @param fileManager        {@link JavaFileManager}，用于管理已经编译好的文件
	 * @param diagnosticListener 诊断监听
	 * @param options            选项，例如 -cpXXX等
	 * @param compilationUnits   编译单元，即需要编译的对象
	 * @return {@link JavaCompiler.CompilationTask}
	 */
	public static JavaCompiler.CompilationTask getTask(
			JavaFileManager fileManager,
			DiagnosticListener<? super JavaFileObject> diagnosticListener,
			Iterable<String> options,
			Iterable<? extends JavaFileObject> compilationUnits) {
		return SYSTEM_COMPILER.getTask(null, fileManager, diagnosticListener, options, null, compilationUnits);
	}

	/**
	 * 获取{@link JavaSourceCompiler}
	 *
	 * @param parent 父{@link ClassLoader}
	 * @return {@link JavaSourceCompiler}
	 * @see JavaSourceCompiler#create(ClassLoader)
	 */
	public static JavaSourceCompiler getCompiler(ClassLoader parent) {
		return JavaSourceCompiler.create(parent);
	}
}
