package com.st.framework.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseBLOBsMapper<K, T, E> extends BaseMapper<K, T, E> {

	List<T> selectByExampleWithBLOBs(E example);

	int updateByExampleWithBLOBs(@Param("record") T record,
			@Param("example") E example);

	int updateByPrimaryKeyWithBLOBs(T record);

}
