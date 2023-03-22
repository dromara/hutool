package cn.hutool.system;

/**
 * 系统属性名称常量池
 *
 * <p>
 * 封装了包括Java运行时环境信息、Java虚拟机信息、Java类信息、OS信息、用户信息等<br>
 *
 *
 * @author Looly
 * @since 4.6.7
 */
public interface SystemPropsKeys {

	// ----- Java运行时环境信息 -----/
	/**
	 * Java 运行时环境规范名称
	 */
	String SPECIFICATION_NAME = "java.specification.name";
	/**
	 * Java 运行时环境版本
	 */
	String VERSION = "java.version";
	/**
	 * Java 运行时环境规范版本
	 */
	String SPECIFICATION_VERSION = "java.specification.version";
	/**
	 * Java 运行时环境供应商
	 */
	String VENDOR = "java.vendor";
	/**
	 * Java 运行时环境规范供应商
	 */
	String SPECIFICATION_VENDOR = "java.specification.vendor";
	/**
	 * Java 供应商的 URL
	 */
	String VENDOR_URL = "java.vendor.url";
	/**
	 * Java 安装目录
	 */
	String HOME = "java.home";
	/**
	 * 加载库时搜索的路径列表
	 */
	String LIBRARY_PATH = "java.library.path";
	/**
	 * 默认的临时文件路径
	 */
	String TMPDIR = "java.io.tmpdir";
	/**
	 * 要使用的 JIT 编译器的名称
	 */
	String COMPILER = "java.compiler";
	/**
	 * 一个或多个扩展目录的路径
	 */
	String EXT_DIRS = "java.ext.dirs";

	// ----- Java虚拟机信息 -----/
	/**
	 * Java 虚拟机实现名称
	 */
	String VM_NAME = "java.vm.name";
	/**
	 * Java 虚拟机规范名称
	 */
	String VM_SPECIFICATION_NAME = "java.vm.specification.name";
	/**
	 * Java 虚拟机实现版本
	 */
	String VM_VERSION = "java.vm.version";
	/**
	 * Java 虚拟机规范版本
	 */
	String VM_SPECIFICATION_VERSION = "java.vm.specification.version";
	/**
	 * Java 虚拟机实现供应商
	 */
	String VM_VENDOR = "java.vm.vendor";
	/**
	 * Java 虚拟机规范供应商
	 */
	String VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";

	// ----- Java类信息 -----/
	/**
	 * Java 类格式版本号
	 */
	String CLASS_VERSION = "java.class.version";
	/**
	 * Java 类路径
	 */
	String CLASS_PATH = "java.class.path";

	// ----- OS信息 -----/
	/**
	 * 操作系统的名称
	 */
	String OS_NAME = "os.name";
	/**
	 * 操作系统的架构
	 */
	String OS_ARCH = "os.arch";
	/**
	 * 操作系统的版本
	 */
	String OS_VERSION = "os.version";
	/**
	 * 文件分隔符（在 UNIX 系统中是“/”）
	 */
	String FILE_SEPARATOR = "file.separator";
	/**
	 * 路径分隔符（在 UNIX 系统中是“:”）
	 */
	String PATH_SEPARATOR = "path.separator";
	/**
	 * 行分隔符（在 UNIX 系统中是“\n”）
	 */
	String LINE_SEPARATOR = "line.separator";

	// ----- 用户信息 -----/
	/**
	 * 用户的账户名称
	 */
	String USER_NAME = "user.name";
	/**
	 * 用户的主目录
	 */
	String USER_HOME = "user.home";
	/**
	 * 用户的当前工作目录
	 */
	String USER_DIR = "user.dir";
}
