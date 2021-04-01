package com.yanzhen.mapper;

import java.util.List;

import com.yanzhen.entityylx.Options;

public interface AndroidOptionsDao {

	//查找选项
	public List<Options> findOptionsById(int uid);
}
