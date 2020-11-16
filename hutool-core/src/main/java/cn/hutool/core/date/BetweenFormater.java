package cn.hutool.core.date;

/**
 * 时长格式化器<br>
 *
 *
 * @author Looly
 * @deprecated 拼写错误，请使用{@link BetweenFormatter}
 */
@Deprecated
public class BetweenFormater extends BetweenFormatter {

	public BetweenFormater(long betweenMs, Level level) {
		super(betweenMs, level);
	}

	public BetweenFormater(long betweenMs, Level level, int levelMaxCount) {
		super(betweenMs, level, levelMaxCount);
	}
}
