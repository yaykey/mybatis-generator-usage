package com.st.framework.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<K, T, E> {
	int countByExample(E example);

	int deleteByExample(E example);

	int deleteByPrimaryKey(K id);

	int insert(T record);

	int insertSelective(T record);

	List<T> selectByExample(E example);

	T selectByPrimaryKey(K id);

	int updateByExampleSelective(@Param("record") T record,
			@Param("example") E example);

	int updateByExample(@Param("record") T record, @Param("example") E example);

	int updateByPrimaryKeySelective(T record);

	int updateByPrimaryKey(T record);

	void insertBatch(@Param("list") List<T> list);
}
