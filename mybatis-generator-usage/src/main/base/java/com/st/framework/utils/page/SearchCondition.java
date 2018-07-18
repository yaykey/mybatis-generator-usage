/*
 * 文件名： SearchCondition.java
 * 
 * 创建日期： 2011-4-11
 *
 * Copyright(C) 2011, by xiaozhi.
 *
 * 原始作者: <a href="mailto:xiaozhi19820323@hotmail.com">xiaozhi</a>
 *
 */
package com.st.framework.utils.page;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 查询条件的基类
 *
 * @author <a href="mailto:xiaozhi19820323@hotmail.com">xiaozhi</a>
 *
 * @version $Revision: 1.1 $
 *
 * @Date 上午11:32:53
 *
 * @since 2011-4-11
 */
public class SearchCondition {

	/** 查询的字段 */
	protected String field;
	
	/** 查询字段对应的值 */
	protected Object fieldValue;

	public SearchCondition() {
		super();
	}
	
	public SearchCondition(String field, Object fieldValue) {
		super();
		this.field = field;
		this.fieldValue = fieldValue;
	}

	/**
	 * 获得要查询的字段名
	 * 
	 * @return 返回要查询的字段名。
	 */
	public String getField() {
		return field;
	}

	/**
	 * 设置要查询的字段名
	 * 
	 * @param field 要设置的查询的字段名。
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * 获得要查询的字段值
	 * 
	 * @return 返回要查询的字段值。
	 */
	public Object getFieldValue() {
		return fieldValue;
	}

	/**
	 * 设置要查询的字段值
	 * 
	 * @param fieldValue 要设置的查询的字段值。
	 */
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	public String toString() {
		ToStringBuilder strBuilder = new ToStringBuilder(this);
		strBuilder.append(super.toString());
		strBuilder.append("field", this.getField());
		strBuilder.append("fieldValue", this.getFieldValue());	
		return strBuilder.toString();
	}
}
