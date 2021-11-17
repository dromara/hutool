package cn.hutool.core.exceptions;

import java.util.Optional;

/**
 * 异常包裹类，可以把异常包裹一层
 *
 * @author nss
 */
public interface TryMonad<T> {

	/**
	 *
	 * 失败返回传入值,成功返回正常值
	 * @param value 传入值
	 * @return 传入值
	 */
	T orElse(T value);
	/**
	 * 是否失败
	 */
	boolean isFail();
	/**
	 * 获取失败信息
	 * 失败返回异常
	 * 成功返回空Optional
	 * @return 失败信息
	 */
	Optional<Throwable> getException();

	class failure<T> implements TryMonad<T> {
		private final Throwable value;
		failure(Throwable value) {
			this.value = value;
		}

		@Override
		public T orElse(T v1) {
			return v1;
		}

		@Override
		public boolean isFail() {
			return true;
		}

		@Override
		public Optional<Throwable> getException() {
			return Optional.of(value);
		}
	}

	class success<T> implements TryMonad<T> {
		private final T value;

		success(T value) {
			this.value = value;
		}
		/**
		 * 成功返回计算之后的值
		 */
		@Override
		public T orElse(T v1) {
			return value;
		}

		@Override
		public boolean isFail() {
			return false;
		}

		@Override
		public Optional<Throwable> getException() {
			return Optional.empty();
		}
	}
}
