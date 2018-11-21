package cn.hutool.log;

/**
 * 抽象定位感知日志实现<br>
 * 此抽象类实现了LocationAwareLog接口，从而支持完全限定类名(Fully Qualified Class Name)，用于纠正定位错误行号
 * 
 * @author Looly
 *
 */
public abstract class AbstractLocationAwareLog extends AbstractLog implements LocationAwareLog{
	private static final long serialVersionUID = -5529674971846264145L;

}
