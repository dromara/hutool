package cn.hutool.core.lang;

import cn.hutool.core.exceptions.UtilException;

/**
 * 调用者。可以通过此类的方法获取调用者、多级调用者以及判断是否被调用
 * 
 * @author Looly
 * @since 3.0.9
 */
public class Caller {
	private static final ICaller CALLER_INSTANCE;
	static {
		CALLER_INSTANCE = tryCreateCaller();
	}
	
	/**
	 * 获得调用者
	 * 
	 * @return 调用者
	 */
	public static Class<?> getCaller() {
		return CALLER_INSTANCE.getCaller();
	}
	
	/**
	 * 获得调用者的调用者
	 * 
	 * @return 调用者的调用者
	 */
	public static Class<?> getCallerCaller() {
		return CALLER_INSTANCE.getCallerCaller();
	}

	/**
	 * 获得调用者，指定第几级调用者<br>
	 * 调用者层级关系：
	 * <pre>
	 * 0 {@link Caller}
	 * 1 调用{@link Caller}中方法的类
	 * 2 调用者的调用者
	 * ...
	 * </pre>
	 * 
	 * @param depth 层级。0表示{@link Caller}本身，1表示调用{@link Caller}的类，2表示调用者的调用者，依次类推
	 * @return 第几级调用者
	 */
	public static Class<?> getCaller(int depth) {
		return CALLER_INSTANCE.getCaller(depth);
	}

	/**
	 * 是否被指定类调用
	 * 
	 * @param clazz 调用者类
	 * @return 是否被调用
	 */
	public static boolean isCalledBy(Class<?> clazz) {
		return CALLER_INSTANCE.isCalledBy(clazz);
	}

	/**
	 * 尝试创建{@link ICaller}实现
	 * 
	 * @return {@link ICaller}实现
	 */
	private static ICaller tryCreateCaller() {
		ICaller caller;
		try {
			caller = new ReflectionCaller();
		} catch (Throwable e) {
			try {
				caller = new SecurityManagerCaller();
			} catch (Throwable e2) {
				caller = new StackTraceCaller();
			}
		}
		return caller;
	}

	// ---------------------------------------------------------------------------------------------- static interface and class
	/**
	 * 调用者接口<br>
	 * 可以通过此接口的实现类方法获取调用者、多级调用者以及判断是否被调用
	 * 
	 * @author Looly
	 *
	 */
	private static interface ICaller {
		/**
		 * 获得调用者
		 * 
		 * @return 调用者
		 */
		Class<?> getCaller();
		
		/**
		 * 获得调用者的调用者
		 * 
		 * @return 调用者的调用者
		 */
		Class<?> getCallerCaller();

		/**
		 * 获得调用者，指定第几级调用者
		 * 调用者层级关系：
		 * <pre>
		 * 0 {@link Caller}
		 * 1 调用{@link Caller}中方法的类
		 * 2 调用者的调用者
		 * ...
		 * </pre>
		 * 
		 * @param depth 层级。0表示{@link Caller}本身，1表示调用{@link Caller}的类，2表示调用者的调用者，依次类推
		 * @return 第几级调用者
		 */
		Class<?> getCaller(int depth);

		/**
		 * 是否被指定类调用
		 * 
		 * @param clazz 调用者类
		 * @return 是否被调用
		 */
		boolean isCalledBy(Class<?> clazz);
	}

	/**
	 * {@link SecurityManager} 方式获取调用者
	 * 
	 * @author Looly
	 */
	private static class SecurityManagerCaller extends SecurityManager implements ICaller {
		private static final int OFFSET = 1;

		@Override
		public Class<?> getCaller() {
			return getClassContext()[OFFSET +1];
		}
		
		@Override
		public Class<?> getCallerCaller() {
			return getClassContext()[OFFSET +2];
		}

		@Override
		public Class<?> getCaller(int depth) {
			final Class<?>[] context = getClassContext();
			if ((OFFSET + depth) < context.length) {
				return context[OFFSET + depth];
			}
			return null;
		}

		@Override
		public boolean isCalledBy(Class<?> clazz) {
			final Class<?>[] classes = getClassContext();
			for (Class<?> contextClass : classes) {
				if (contextClass.equals(clazz)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 通过sun.reflect.Reflection 获取调用者
	 * 
	 * @author Looly
	 *
	 */
	@SuppressWarnings({ "deprecation", "restriction" })
	private static class ReflectionCaller extends SecurityManagerCaller {
		private static final int OFFSET = 2;
		
		@Override
		public Class<?> getCaller() {
			return sun.reflect.Reflection.getCallerClass(OFFSET + 1);
		}
		
		@Override
		public Class<?> getCallerCaller() {
			return sun.reflect.Reflection.getCallerClass(OFFSET + 2);
		}

		@Override
		public Class<?> getCaller(int depth) {
			return sun.reflect.Reflection.getCallerClass(OFFSET + depth);
		}
	}

	/**
	 * 通过StackTrace方式获取调用者。此方式效率最低，不推荐使用
	 * 
	 * @author Looly
	 */
	private static class StackTraceCaller implements ICaller {
		private static final int OFFSET = 2;

		@Override
		public Class<?> getCaller() {
			final String className = Thread.currentThread().getStackTrace()[OFFSET + 1].getClassName();
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new UtilException(e, "[{}] not found!", className);
			}
		}
		
		@Override
		public Class<?> getCallerCaller() {
			final String className = Thread.currentThread().getStackTrace()[OFFSET + 2].getClassName();
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new UtilException(e, "[{}] not found!", className);
			}
		}

		@Override
		public Class<?> getCaller(int depth) {
			final String className = Thread.currentThread().getStackTrace()[OFFSET + depth].getClassName();
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new UtilException(e, "[{}] not found!", className);
			}
		}

		@Override
		public boolean isCalledBy(Class<?> clazz) {
			for (final StackTraceElement element : Thread.currentThread().getStackTrace()) {
				if (element.getClassName().equals(clazz.getName())) {
					return true;
				}
			}
			return false;
		}
	}
}
