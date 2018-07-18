package com.st.framework.business;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.st.framework.module.example.BaseExample;
import com.st.framework.persistence.mapper.BaseBLOBsMapper;

public abstract class BaseBLOBsManager<K, T, E extends BaseExample> extends
		BaseManager<K, T, E> {

	public abstract BaseBLOBsMapper<K, T, E> getMapper();

	public List<T> selectByExampleWithBLOBs(E example) {
		return this.getMapper().selectByExampleWithBLOBs(example);
	}

	public int updateByExampleWithBLOBs(@Param("record") T record,
			@Param("example") E example) {
		return this.getMapper().updateByExampleWithBLOBs(record, example);
	}

	public int updateByPrimaryKeyWithBLOBs(T record) {
		return this.getMapper().updateByPrimaryKeyWithBLOBs(record);
	}

}
