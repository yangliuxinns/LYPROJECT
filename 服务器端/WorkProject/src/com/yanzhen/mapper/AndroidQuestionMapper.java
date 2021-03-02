package com.yanzhen.mapper;

import java.util.List;

import com.yanzhen.entityylx.Question;

public interface AndroidQuestionMapper {

	
	//查询Question
	
	public List<Question> findQuestionById(int uid);
}
