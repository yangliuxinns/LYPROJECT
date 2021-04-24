package com.yanzhen.mapper;

import java.util.List;

import com.yanzhen.entityylx.ResultInfo;

public interface AndroidResultMapper {

	//查询结果
	public List<ResultInfo> findResultInfoById(int uid);
}
