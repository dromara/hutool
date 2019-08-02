package cn.hutool.core.bean.compare;


/**
 * 字段修改数据, 里面可以存储修改字段的类型, 所属bean的类名, 旧值, 新值, bean的hashcode
 * 如果是行操作, 则字段名称会记为{@link ModifyField#ADD_LINE}或{@link ModifyField#DELETE_LINE}
 * @author dawn
 */
public class ModifyField {

	/** 新增行 */
	public static final String ADD_LINE = "ADD_LINE";
	/** 删除行 */
	public static final String DELETE_LINE = "DELETE_LINE";
	/** 类名 */
	private String className;
	/** 字段名 */
	private String fieldName;
	/** 字段类型 */
	private Class<?> propClass;
	/** 类的hashcode */
	private Integer hashCode;
	/** 旧值 */
	private Object oldValue;
	/** 新值 */
	private Object newValue;

	public ModifyField setAddLine(String className, Integer hashCode, Object newValue){
		this.className = className;
		this.fieldName = ADD_LINE;
		this.hashCode = hashCode;
		this.newValue = newValue;
		return this;
	}

	public ModifyField setDeleteLine(String className, Integer hashCode, Object newValue){
		this.className = className;
		this.fieldName = DELETE_LINE;
		this.hashCode = hashCode;
		this.oldValue = newValue;
		return this;
	}

	public ModifyField(String className, String fieldName, Class<?> propClass, Integer hashCode, Object oldValue, Object newValue) {
		this.hashCode = hashCode;
		this.className = className;
		this.propClass = propClass;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public ModifyField() {
	}

	public Integer getHashCode() {
		return hashCode;
	}

	public void setHashCode(Integer hashCode) {
		this.hashCode = hashCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public Class<?> getPropClass() {
		return propClass;
	}

	public void setPropClass(Class<?> propClass) {
		this.propClass = propClass;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"className\":\"")
				.append(className).append('\"');
		sb.append(",\"fieldName\":\"")
				.append(fieldName).append('\"');
		sb.append(",\"propClass\":")
				.append(propClass);
		sb.append(",\"hashCode\":")
				.append(hashCode);
		sb.append(",\"oldValue\":")
				.append(oldValue);
		sb.append(",\"newValue\":")
				.append(newValue);
		sb.append('}');
		return sb.toString();
	}
}
