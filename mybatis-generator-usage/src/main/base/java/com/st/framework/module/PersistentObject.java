/*
 * 文件名： PersistentObject.java
 * 
 * 创建日期： 2011-4-14
 *
 * Copyright(C) 2011, by xiaozhi.
 *
 * 原始作者: <a href="mailto:xiaozhi19820323@hotmail.com">xiaozhi</a>
 *
 */
package com.st.framework.module;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 持久对象的基类,所有的pojo对象都需要继承 *
 * 
 */
public abstract class PersistentObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5417497423315596240L;

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
